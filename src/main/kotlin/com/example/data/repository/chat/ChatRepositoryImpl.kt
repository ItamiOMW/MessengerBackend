package com.example.data.repository.chat

import com.example.data.database.exposed.DatabaseFactory.dbQuery
import com.example.data.database.exposed.entity.ChatEntity
import com.example.data.database.exposed.entity.MessageEntity
import com.example.data.database.exposed.table.*
import com.example.data.mapper.toChat
import com.example.data.mapper.toMessage
import com.example.data.mapper.toUser
import com.example.data.model.chat.*
import com.example.util.getCurrentDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

class ChatRepositoryImpl : ChatRepository {

    override suspend fun getChats(userId: Int): List<Chat> {
        val chatEntities = dbQuery {
            (Participants innerJoin Chats innerJoin Users)
                .select { Participants.userId eq userId }
                .mapNotNull { row ->
                    ChatEntity.wrapRow(row)
                }

        }
        return chatEntities.map { chatEntity ->
            val participants = getChatParticipants(chatEntity.id.value)
            chatEntity.toChat(participants)
        }
    }

    override suspend fun getChatById(chatId: Int): Chat? {
        val participants = getChatParticipants(chatId)
        return dbQuery {
            ChatEntity.findById(chatId)?.toChat(participants)
        }
    }

    override suspend fun isChatParticipant(chatId: Int, userId: Int): Boolean {
        return dbQuery {
            Participants
                .select { (Participants.chatId eq chatId) and (Participants.userId eq userId) }
                .count() > 0
        }
    }

    override suspend fun getDialogChatByUsers(firstUserId: Int, secondUserId: Int): Chat? {
        val chatId = dbQuery {
            (Participants innerJoin Chats)
                .select {
                    (Chats.type eq ChatType.DIALOG) and (Participants.userId inList listOf(firstUserId, secondUserId))
                }.firstOrNull()?.let { row ->
                    ChatEntity.wrapRow(row).id.value
                }
        }
        return chatId?.let { getChatById(it) }
    }

    override suspend fun createChat(chat: Chat): Chat? {
        val chatId = dbQuery {
            val chatEntity = ChatEntity.new {
                this.name = chat.name
                this.type = chat.type
                this.chatPictureUrl = chat.chatPictureUrl
            }
            chat.participants.forEach { participant ->
                Participants.insert { table ->
                    table[chatId] = chatEntity.id.value
                    table[userId] = participant.user.id
                    table[participantRole] = participant.participantRole
                }
            }
            chatEntity.id
        }
        return getChatById(chatId.value)
    }

    override suspend fun updateChat(chatId: Int, updateChat: UpdateChat): Chat? {
        dbQuery {
            ChatEntity.findById(chatId)?.let { chatEntity ->
                chatEntity.name = updateChat.name
                chatEntity.chatPictureUrl = updateChat.chatPictureUrl
                chatEntity.isActive = updateChat.isActive
            }
        }
        return getChatById(chatId)
    }

    override suspend fun deleteChat(chatId: Int) {
        dbQuery {
            Chats.deleteWhere { Chats.id eq chatId }
        }
    }

    override suspend fun addChatParticipants(chatId: Int, participants: List<ChatParticipant>) {
        dbQuery {
            participants.forEach { participant ->
                Participants.insert { table ->
                    table[this.chatId] = chatId
                    table[this.userId] = participant.user.id
                    table[this.participantRole] = participant.participantRole
                }
            }
        }
    }

    override suspend fun deleteChatParticipants(chatId: Int, participantIds: List<Int>) {
        dbQuery {
            Participants.deleteWhere {
                (this.chatId eq chatId) and (this.userId inList participantIds)
            }
        }
    }

    override suspend fun changeChatParticipantRole(chatId: Int, participantId: Int, role: ParticipantRole) {
        dbQuery {
            Participants.update(
                where = { (Participants.chatId eq chatId) and (Participants.userId eq participantId) },
                body = { statement ->
                    statement[participantRole] = role
                }
            )
        }
    }

    override suspend fun getMessagesForChat(chatId: Int, page: Int, pageSize: Int): List<Message> {
        val messages = dbQuery {
            MessageEntity.find { Messages.chatId eq chatId }
                .orderBy(Messages.createdAt to SortOrder.DESC)
                .limit(n = pageSize, offset = ((page - 1) * pageSize).toLong())
                .map { it.toMessage(null, emptyList()) }

        }
        return messages.map { message ->
            val pictureUrls = getMessagePictureUrls(message.id)
            val usersSeenMessage = getUsersSeenMessage(message.id)
            message.copy(pictureUrls = pictureUrls, userIdsSeenMessage = usersSeenMessage)
        }
    }

    override suspend fun getLastMessageForChat(chatId: Int): Message? {
        val message = dbQuery {
            MessageEntity.find { Messages.chatId eq chatId }
                .orderBy(Messages.createdAt to SortOrder.DESC)
                .firstOrNull()?.toMessage(null, emptyList())
        }
        val pictureUrls = message?.id?.let { getMessagePictureUrls(it) }
        val usersSeenMessage = message?.id?.let { getUsersSeenMessage(it) }
        return message?.copy(pictureUrls = pictureUrls, userIdsSeenMessage = usersSeenMessage ?: emptyList())
    }

    override suspend fun getUnreadMessagesCountForUser(userId: Int, chatId: Int): Int {
        val messages = dbQuery {
            MessageEntity.find {
                Messages.chatId eq chatId
            }.map { it.toMessage(null, emptyList()) }
        }

        var count = 0
        messages.forEach {
            val seenMessage = getUsersSeenMessage(it.id).any { it == userId }
            if (!seenMessage) {
                count++
            }
        }
        return count
    }

    override suspend fun readMessage(userId: Int, messageId: Int) {
        dbQuery {
            UsersSeenMessage.insert { table ->
                table[UsersSeenMessage.messageId] = messageId
                table[UsersSeenMessage.userId] = userId
            }
        }
    }

    override suspend fun isMessageRead(messageId: Int): Boolean {
        val message = dbQuery {
            MessageEntity.findById(messageId)?.toMessage(null, emptyList())
        }

        val usersSeenMessage = message?.id?.let {
            getUsersSeenMessage(it).filter { userId -> userId != message.author.id }
        } ?: emptyList()

        return usersSeenMessage.isNotEmpty()
    }

    override suspend fun isMessageReadByUser(userId: Int, messageId: Int): Boolean {
        val message = dbQuery {
            MessageEntity.findById(messageId)?.toMessage(null, emptyList())
        }

        return message?.id?.let { getUsersSeenMessage(it).any { id -> id == userId } } ?: false
    }

    override suspend fun createMessage(createMessage: CreateMessage): Message? {
        val messageId = dbQuery {
            val insertedMessageId = Messages.insertAndGetId { table ->
                table[chatId] = createMessage.chatId
                table[authorId] = createMessage.authorId
                table[text] = createMessage.text
                table[type] = createMessage.type
                table[createdAt] = getCurrentDateTime()
            }.value
            createMessage.pictureUrls?.forEach { url ->
                MessagePictureUrls.insert { table ->
                    table[messageId] = insertedMessageId
                    table[pictureUrl] = url
                }
            }
            insertedMessageId
        }
        return getMessageById(messageId)
    }

    override suspend fun updateMessage(messageId: Int, updateMessage: UpdateMessage): Message? {
        dbQuery {
            MessageEntity[messageId].let {
                it.text = updateMessage.text
                it.updatedAt = getCurrentDateTime()
            }
        }
        return getMessageById(messageId)
    }

    override suspend fun deleteMessage(messageId: Int) {
        dbQuery {
            Messages.deleteWhere { Messages.id eq messageId }
        }
    }

    override suspend fun getMessageById(messageId: Int): Message? {
        val message = dbQuery {
            MessageEntity.findById(messageId)?.toMessage(null, emptyList())
        }

        val pictureUrls = message?.id?.let { getMessagePictureUrls(it) }
        val usersSeenMessage = message?.id?.let { getUsersSeenMessage(it) }
        return message?.copy(pictureUrls = pictureUrls, userIdsSeenMessage = usersSeenMessage ?: emptyList())
    }

    private suspend fun getMessagePictureUrls(messageId: Int): List<String> {
        return dbQuery {
            MessagePictureUrls
                .select { MessagePictureUrls.messageId eq messageId }
                .mapNotNull { it[MessagePictureUrls.pictureUrl] }
        }
    }

    private suspend fun getUsersSeenMessage(messageId: Int): List<Int> {
        return dbQuery {
            UsersSeenMessage
                .select { UsersSeenMessage.messageId eq messageId }
                .mapNotNull { it[UsersSeenMessage.userId].value }
        }
    }

    private suspend fun getChatParticipants(chatId: Int): List<ChatParticipant> {
        return dbQuery {
            (Participants innerJoin Users)
                .select { Participants.chatId eq chatId }
                .map { row ->
                    val user = row.toUser()
                    val role = row[Participants.participantRole]
                    ChatParticipant(user, role)
                }
        }
    }

}