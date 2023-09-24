package com.example.service.chat

import com.example.data.mapper.toChatParticipantResponse
import com.example.data.mapper.toChatResponse
import com.example.data.mapper.toMessageResponse
import com.example.data.model.chat.*
import com.example.data.model.users.MessagesPermission
import com.example.data.repository.block.BlockRepository
import com.example.data.repository.chat.ChatRepository
import com.example.data.repository.contact.ContactRepository
import com.example.data.repository.user.UserRepository
import com.example.data.request.*
import com.example.data.response.ChatParticipantResponse
import com.example.data.response.ChatResponse
import com.example.data.response.MessageResponse
import com.example.exceptions.ConflictException
import com.example.exceptions.ForbiddenException
import com.example.exceptions.NotFoundException
import com.google.gson.Gson
import io.ktor.websocket.*
import java.util.*

class ChatService(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
    private val blockRepository: BlockRepository,
    private val gson: Gson,
) {
    private val serverConnections = Collections.synchronizedSet<ServerConnection?>(LinkedHashSet())
    private val chatConnections = Collections.synchronizedSet<ChatConnection?>(LinkedHashSet())

    suspend fun onConnect(userId: Int, socket: WebSocketSession) {
        serverConnections += ServerConnection(userId, socket)
        userRepository.setUserOnlineStatus(id = userId, isOnline = true)
    }

    suspend fun onDisconnect(userId: Int, session: WebSocketSession) {
        chatConnections.removeAll { it.session == session }
        userRepository.setUserOnlineStatus(id = userId, isOnline = false)
    }

    suspend fun onConnectToChat(userId: Int, chatId: Int, session: WebSocketSession) {
        chatConnections += ChatConnection(userId, chatId, session)
    }

    suspend fun onDisconnectFromChat(session: WebSocketSession) {
        chatConnections.removeAll { it.session == session }
    }

    suspend fun createChat(creatorId: Int, createChatRequest: CreateChatRequest, pictureUrl: String?): ChatResponse {
        val users = userRepository.getUsersByIds(createChatRequest.participantIds + creatorId)
        val chat = chatRepository.createChat(
            Chat(
                name = createChatRequest.name,
                type = createChatRequest.type,
                chatPictureUrl = pictureUrl,
                participants = users.map { user ->
                    val role = when (createChatRequest.type) {
                        ChatType.DIALOG -> ParticipantRole.MEMBER
                        ChatType.GROUP -> if (user.id == creatorId) ParticipantRole.CREATOR else ParticipantRole.MEMBER
                    }
                    ChatParticipant(user, role)
                }
            )
        ) ?: throw Exception()
        if (chat.type == ChatType.GROUP) {
            sendMessage(
                userId = creatorId,
                sendMessageRequest = SendMessageRequest(
                    chatId = chat.id,
                    text = "Group chat created.",
                    type = MessageType.CHAT_CREATED
                )
            )
        }
        val participantIds = chat.participants.map { it.user.id }
        val lastMessage = chatRepository.getLastMessageForChat(chat.id)
        val chatResponse = chat.toChatResponse(
            lastMessage = lastMessage?.toMessageResponse(),
            unreadMessagesCount = 1
        )
        val chatFrameText = gson.toJson(chatResponse)
        serverConnections.filter { it.userId in participantIds }.forEach { serverConnection ->
            serverConnection.session.send(Frame.Text("${WebSocketEvent.CREATE_CHAT}#$chatFrameText"))
        }
        return chat.toChatResponse(lastMessage?.toMessageResponse(), 1)
    }

    suspend fun updateChat(
        userId: Int,
        chatId: Int,
        updateChatRequest: UpdateChatRequest,
        pictureUrl: String?
    ): ChatResponse {
        val chat = chatRepository.getChatById(chatId) ?: throw ConflictException("Chat does not exist.")
        val participantIds = chat.participants.map { it.user.id }
        if (!participantIds.contains(userId)) {
            throw ForbiddenException("You are not allowed to edit this chat.")
        }

        val updatedChat = chatRepository.updateChat(
            chatId,
            updateChat = UpdateChat(
                name = updateChatRequest.name ?: chat.name,
                chatPictureUrl = pictureUrl ?: chat.chatPictureUrl,
                isActive = true
            )
        ) ?: throw Exception()
        val lastMessage = chatRepository.getLastMessageForChat(chatId)?.toMessageResponse()

        serverConnections.filter { it.userId in participantIds }.forEach { serverConnection ->
            val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, chatId)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }

        chatConnections.filter { it.chatId == chatId }.forEach { connection ->
            val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(connection.userId, chatId)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            connection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }
        if (pictureUrl != null) {
            sendMessage(
                userId,
                SendMessageRequest(
                    chatId = chatId,
                    text = null,
                    type = MessageType.CHAT_PICTURE_UPDATED
                )
            )
        }

        if (updateChatRequest.name != chat.name) {
            sendMessage(
                userId,
                SendMessageRequest(
                    chatId = chatId,
                    text = updateChatRequest.name,
                    type = MessageType.CHAT_NAME_UPDATED
                )
            )
        }
        val unreadMessageCount = chatRepository.getUnreadMessagesCountForUser(userId, chatId)
        return updatedChat.toChatResponse(lastMessage, unreadMessageCount)
    }

    suspend fun deleteChat(userId: Int, chatId: Int) {
        val chat = chatRepository.getChatById(chatId) ?: throw ConflictException("Chat does not exist.")
        val participantIds = chat.participants.map { it.user.id }
        if (!participantIds.contains(userId)) {
            throw ForbiddenException("You are not allowed to delete this chat.")
        }

        if (chat.type == ChatType.GROUP) {
            val userRole = chat.participants.find { it.user.id == userId }?.participantRole
                ?: throw ForbiddenException("You are not allowed to delete this chat.")
            if (userRole != ParticipantRole.CREATOR) {
                throw ForbiddenException("You are not allowed to delete this chat.")
            }
        }
        chatRepository.deleteChat(chat.id)
        val chatFrame = gson.toJson(chat.toChatResponse(lastMessage = null, unreadMessagesCount = 0))
        chatConnections.filter { it.chatId == chat.id }.forEach { chatConnection ->
            chatConnection.session.send(Frame.Text("${WebSocketEvent.DELETE_CHAT}#$chatFrame"))
        }
        serverConnections.filter { it.userId in participantIds }.forEach { serverConnection ->
            serverConnection.session.send(Frame.Text("${WebSocketEvent.DELETE_CHAT}#$chatFrame"))
        }
    }

    suspend fun readMessage(userId: Int, readMessageRequest: ReadMessageRequest) {
        if (chatRepository.isMessageReadByUser(userId, readMessageRequest.id)) {
            return
        }

        chatRepository.readMessage(userId, readMessageRequest.id)

        val message = chatRepository.getMessageById(readMessageRequest.id)
            ?: throw ConflictException("Message does not exist")

        val chatLastMessage = chatRepository.getLastMessageForChat(message.chatId)

        if (chatLastMessage?.id == message.id) {
            val chat = chatRepository.getChatById(message.chatId) ?: throw ConflictException("Chat does not exist")
            val participantIds = chat.participants.map { it.user.id }
            serverConnections.filter { it.userId in participantIds }.forEach { serverConnection ->
                val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, chat.id)
                val chatFrame = gson.toJson(
                    chat.toChatResponse(
                        lastMessage = chatLastMessage.toMessageResponse(),
                        unreadMessagesCount = unreadMessagesCount
                    )
                )
                serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrame"))
            }
        }

        val messageFrame = gson.toJson(message.toMessageResponse())
        chatConnections.filter { it.chatId == message.chatId }.forEach { chatConnection ->
            chatConnection.session.send(Frame.Text("${WebSocketEvent.READ_MESSAGE}#$messageFrame"))
        }
    }

    suspend fun sendMessage(userId: Int, sendMessageRequest: SendMessageRequest) {
        val chat = chatRepository.getChatById(sendMessageRequest.chatId)

        val createMessage = chat?.let {
            CreateMessage(
                authorId = userId,
                chatId = it.id,
                text = sendMessageRequest.text,
                type = sendMessageRequest.type,
            )
        } ?: throw ConflictException("Chat does not exist.")

        if (chat.type == ChatType.DIALOG) {
            val dialogUser = chat.participants.find { it.user.id != userId }?.user
                ?: throw ConflictException("No second chat participant in the dialog")

            val isBlockedByUser = blockRepository.isBlocked(userId, dialogUser.id)
            val isBlockedByMe = blockRepository.isBlocked(dialogUser.id, userId)
            val areContacts = contactRepository.areContacts(userId, dialogUser.id)
            if (dialogUser.messagesPermission == MessagesPermission.CONTACTS_ONLY && !areContacts) {
                throw ForbiddenException("Only contacts can send message to this user.")
            }

            if (isBlockedByUser) {
                throw ForbiddenException("The user has blocked you.")
            }

            if (isBlockedByMe) {
                throw ForbiddenException("You have blocked the user.")
            }
        }

        val message = chatRepository.createMessage(createMessage) ?: throw Exception()
        if (!chat.isActive) {
            chatRepository.updateChat(
                chatId = chat.id,
                updateChat = UpdateChat(chat.name, chat.chatPictureUrl, isActive = true)
            )
        }
        val messageResponse = message.toMessageResponse()
        val messageFrameText = gson.toJson(messageResponse)

        val participantIds = chat.participants.map { it.user.id }
        val onlineUsersToNotify = serverConnections.filter { it.userId in participantIds }
        val chatSessionUsersToNotify = chatConnections.filter {
            it.chatId == chat.id && it.userId in participantIds
        }

        chatSessionUsersToNotify.forEach { chatSession ->
            chatSession.session.send(Frame.Text("${WebSocketEvent.SEND_MESSAGE}#$messageFrameText"))
        }
        onlineUsersToNotify.forEach { serverConnection ->
            val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, chat.id)
            val chatResponse = chat.toChatResponse(messageResponse, unreadMessagesCount)
            val chatFrameText = gson.toJson(chatResponse)
            serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }
    }

    suspend fun editMessage(userId: Int, editMessageRequest: EditMessageRequest) {
        val messageToEdit = chatRepository.getMessageById(editMessageRequest.id)
            ?: throw ConflictException("Message does not exist.")

        val chat = chatRepository.getChatById(messageToEdit.chatId)
            ?: throw ConflictException("Chat does not exist.")

        if (messageToEdit.author.id != userId) {
            throw ForbiddenException("You are not allowed to edit this message.")
        }

        val updateMessage = UpdateMessage(editMessageRequest.text)
        val updatedMessage = chatRepository.updateMessage(
            messageId = editMessageRequest.id,
            updateMessage = updateMessage
        ) ?: throw Exception()
        val messageResponse = updatedMessage.toMessageResponse()
        val messageFrameText = gson.toJson(messageResponse)

        val chatLastMessage = chatRepository.getLastMessageForChat(messageToEdit.chatId)

        if (chatLastMessage?.id == messageToEdit.id) {
            val usersToNotify = serverConnections.filter { connection ->
                chat.participants.map { it.user.id }.contains(connection.userId)
            }
            usersToNotify.forEach { serverConnection ->
                val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, chat.id)
                val chatResponse = chat.toChatResponse(messageResponse, unreadMessagesCount)
                val chatFrameText = gson.toJson(chatResponse)
                serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
            }
        }

        chatConnections.filter { it.chatId == messageToEdit.chatId }.forEach { connection ->
            connection.session.send(Frame.Text("${WebSocketEvent.EDIT_MESSAGE}#$messageFrameText"))
        }
    }

    suspend fun deleteMessage(userId: Int, deleteMessageRequest: DeleteMessageRequest) {
        val message = chatRepository.getMessageById(deleteMessageRequest.id)
            ?: throw ConflictException("Message does not exist.")

        if (message.author.id != userId) {
            throw ForbiddenException("You are not allowed to delete this message.")
        }

        val chat = chatRepository.getChatById(message.chatId) ?: throw ConflictException("Chat does not exist.")

        val chatLastMessage = chatRepository.getLastMessageForChat(chat.id)
        chatRepository.deleteMessage(message.id)
        val newChatLastMessage = chatRepository.getLastMessageForChat(chat.id)
        if (chatLastMessage?.id == message.id) {
            val usersToNotify = serverConnections.filter { serverConnection ->
                chat.participants.map { it.user.id }.contains(serverConnection.userId)
            }
            val chatResponse = chat.toChatResponse(newChatLastMessage?.toMessageResponse(), 0)
            val chatFrameText = gson.toJson(chatResponse)
            usersToNotify.forEach { serverConnection ->
                serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
            }
        }

        val messageResponse = message.toMessageResponse()
        val messageFrameText = gson.toJson(messageResponse)
        chatConnections.filter { it.chatId == message.chatId }.forEach { connection ->
            connection.session.send(Frame.Text("${WebSocketEvent.DELETE_MESSAGE}#$messageFrameText"))
        }
    }

    suspend fun addChatParticipants(
        userId: Int,
        participantIdsToAdd: List<Int>,
        chatId: Int,
    ): List<ChatParticipantResponse> {
        val chat = chatRepository.getChatById(chatId)
            ?: throw ConflictException("Chat does not exist")

        if (chat.type == ChatType.DIALOG) {
            throw ConflictException("User already in the chat.")
        }

        val userInviting = chat.participants.find {
            it.user.id == userId
        } ?: throw ForbiddenException("You are not chat participant")

        val participantIds = chat.participants.map { it.user.id }
        if (participantIds.any { participantIdsToAdd.contains(it) }) {
            throw ConflictException("User already in the chat.")
        }
        val usersToBeInvited = userRepository.getUsersByIds(participantIdsToAdd).filter {
            contactRepository.areContacts(userId, it.id) && !participantIds.contains(it.id)
        }
        val userIdsToBeInvited = usersToBeInvited.map { it.id }
        chatRepository.addChatParticipants(
            chatId = chat.id,
            participants = usersToBeInvited.map { user ->
                ChatParticipant(
                    user = user,
                    participantRole = ParticipantRole.MEMBER
                )
            }
        )
        usersToBeInvited.forEach { user ->
            sendMessage(
                userId = user.id,
                sendMessageRequest = SendMessageRequest(
                    text = "${userInviting.user.fullName} invited ${user.fullName}",
                    chatId = chat.id,
                    type = MessageType.INVITED
                )
            )
        }
        val updatedChat = chatRepository.getChatById(chat.id) ?: throw Exception()
        val lastMessage = chatRepository.getLastMessageForChat(updatedChat.id)?.toMessageResponse()
        serverConnections.filter { it.userId in participantIds }.forEach { serverConnection ->
            val unreadMessagesCount =
                chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, updatedChat.id)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }

        val newParticipants = updatedChat.participants.filter { it.user.id in userIdsToBeInvited }.map {
            it.toChatParticipantResponse()
        }
        val newParticipantsFrameText = gson.toJson(newParticipants)
        chatConnections.filter { it.chatId == updatedChat.id }.forEach { connection ->
            connection.session.send(Frame.Text("${WebSocketEvent.ADD_CHAT_PARTICIPANTS}#$newParticipantsFrameText"))
        }
        return newParticipants
    }

    suspend fun deleteChatParticipant(
        userId: Int,
        participantId: Int,
        chatId: Int,
    ) {
        val chat = chatRepository.getChatById(chatId)
            ?: throw ConflictException("Chat does not exist")

        if (chat.type == ChatType.DIALOG) {
            throw ConflictException("You can't delete chat participant from dialog.")
        }

        val userDeleting = chat.participants.find {
            it.user.id == userId
        } ?: throw ForbiddenException("You are not chat participant")

        val userToDelete = chat.participants.find {
            it.user.id == participantId
        } ?: throw ForbiddenException("User is not in the chat.")

        when (userDeleting.participantRole) {
            ParticipantRole.MEMBER -> {
                throw ForbiddenException("Only admin or owner can delete participants.")
            }

            ParticipantRole.ADMIN -> {
                if (userToDelete.participantRole != ParticipantRole.MEMBER) {
                    throw ForbiddenException("Only owner can delete this user.")
                }
            }

            ParticipantRole.CREATOR -> {
                Unit
            }
        }
        chatRepository.deleteChatParticipants(chat.id, listOf(participantId))
        sendMessage(
            userId = userToDelete.user.id,
            sendMessageRequest = SendMessageRequest(
                text = "${userDeleting.user.fullName} kicked ${userToDelete.user.fullName}",
                chatId = chat.id,
                type = MessageType.KICKED
            )
        )
        val updatedChat = chatRepository.getChatById(chat.id) ?: throw Exception()
        val lastMessage = chatRepository.getLastMessageForChat(updatedChat.id)?.toMessageResponse()
        val participantsIds = chat.participants.map { it.user.id }.filter { it != participantId }

        serverConnections.filter { it.userId == participantId }.forEach { serverConnection ->
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(null, 0))
            serverConnection.session.send(Frame.Text("${WebSocketEvent.DELETE_CHAT}#$chatFrameText"))
        }

        serverConnections.filter { it.userId in participantsIds }.forEach { serverConnection ->
            val unreadMessagesCount =
                chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, updatedChat.id)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }

        chatConnections.filter { it.chatId == updatedChat.id }.forEach { connection ->
            connection.session.send(Frame.Text("${WebSocketEvent.DELETE_CHAT_PARTICIPANT}#${userToDelete.user.id}"))
        }
    }

    suspend fun assignAdminRole(userId: Int, assignToId: Int, chatId: Int) {
        val chat = chatRepository.getChatById(chatId)
            ?: throw ConflictException("Chat does not exist")

        val userAssigning = chat.participants.find {
            it.user.id == userId
        } ?: throw ForbiddenException("User is not in the chat.")

        if (userAssigning.participantRole != ParticipantRole.CREATOR) {
            throw ForbiddenException("Only owner can assign admin role.")
        }

        chatRepository.changeChatParticipantRole(
            chatId,
            assignToId,
            ParticipantRole.ADMIN
        )

        sendMessage(
            assignToId,
            SendMessageRequest(
                chatId = chatId,
                text = null,
                type = MessageType.ADMIN_ROLE_ASSIGNED
            )
        )

        val participantIds = chat.participants.map { it.user.id }
        val updatedChat = chatRepository.getChatById(chat.id) ?: throw Exception()
        val lastMessage = chatRepository.getLastMessageForChat(updatedChat.id)?.toMessageResponse()
        serverConnections.filter { it.userId in participantIds }.forEach { serverConnection ->
            val unreadMessagesCount =
                chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, updatedChat.id)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }

        chatConnections.filter { it.chatId == updatedChat.id }.forEach { connection ->
            val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(connection.userId, updatedChat.id)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            connection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }
    }

    suspend fun removeAdminRole(userId: Int, assignToId: Int, chatId: Int) {
        val chat = chatRepository.getChatById(chatId)
            ?: throw ConflictException("Chat does not exist")

        val userAssigning = chat.participants.find {
            it.user.id == userId
        } ?: throw ForbiddenException("User is not in the chat.")

        if (userAssigning.participantRole != ParticipantRole.CREATOR) {
            throw ForbiddenException("Only owner can remove admin role.")
        }

        chatRepository.changeChatParticipantRole(
            chatId,
            assignToId,
            ParticipantRole.MEMBER
        )

        sendMessage(
            assignToId,
            SendMessageRequest(
                chatId = chatId,
                text = null,
                type = MessageType.ADMIN_ROLE_REMOVED
            )
        )

        val participantIds = chat.participants.map { it.user.id }
        val updatedChat = chatRepository.getChatById(chat.id) ?: throw Exception()
        val lastMessage = chatRepository.getLastMessageForChat(updatedChat.id)?.toMessageResponse()
        serverConnections.filter { it.userId in participantIds }.forEach { serverConnection ->
            val unreadMessagesCount =
                chatRepository.getUnreadMessagesCountForUser(serverConnection.userId, updatedChat.id)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            serverConnection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }

        chatConnections.filter { it.chatId == updatedChat.id }.forEach { connection ->
            val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(connection.userId, updatedChat.id)
            val chatFrameText = gson.toJson(updatedChat.toChatResponse(lastMessage, unreadMessagesCount))
            connection.session.send(Frame.Text("${WebSocketEvent.UPDATE_CHAT}#$chatFrameText"))
        }
    }

    suspend fun leaveChat(userId: Int, chatId: Int) {
        val chat = chatRepository.getChatById(chatId)
            ?: throw ConflictException("Chat does not exist")

        val userLeaving = chat.participants.find {
            it.user.id == userId
        } ?: throw ForbiddenException("User is not in the chat.")

        if (userLeaving.participantRole == ParticipantRole.CREATOR) {
            deleteChat(userId, chat.id)
            return
        }

        chatRepository.deleteChatParticipants(chat.id, listOf(userLeaving.user.id))
        sendMessage(
            userId = userLeaving.user.id,
            sendMessageRequest = SendMessageRequest(
                text = "${userLeaving.user.fullName} left the chat",
                chatId = chat.id,
                type = MessageType.LEFT
            )
        )
        val chatFrame = gson.toJson(chat.toChatResponse(null, 0))
        serverConnections.filter { it.userId == userId }.map { serverConnection ->
            serverConnection.session.send(Frame.Text("${WebSocketEvent.DELETE_CHAT}#$chatFrame"))
        }
        chatConnections.filter { it.chatId == chat.id }.forEach { connection ->
            connection.session.send(Frame.Text("${WebSocketEvent.LEAVE_CHAT}#$userId"))
        }
    }

    suspend fun getChats(userId: Int): List<ChatResponse> {
        val userChats = chatRepository.getChats(userId).filter { it.isActive }

        return userChats.map { chat ->
            val lastMessage = chatRepository.getLastMessageForChat(chat.id)
            val unreadMessageCount = chatRepository.getUnreadMessagesCountForUser(userId, chat.id)
            chat.toChatResponse(lastMessage?.toMessageResponse(), unreadMessageCount)
        }
    }

    suspend fun getChatById(userId: Int, chatId: Int): ChatResponse {
        val chat = chatRepository.getChatById(chatId = chatId) ?: throw NotFoundException("Chat not found.")

        if (chat.participants.none { it.user.id == userId }) {
            throw ForbiddenException("You are not chat participant.")
        }
        val lastMessage = chatRepository.getLastMessageForChat(chat.id)
        val unreadMessagesCount = chatRepository.getUnreadMessagesCountForUser(userId, chat.id)
        return chat.toChatResponse(lastMessage?.toMessageResponse(), unreadMessagesCount)
    }

    suspend fun getDialogChatByUsers(userId: Int, dialogUserId: Int): ChatResponse {
        val chat = chatRepository.getDialogChatByUsers(userId, dialogUserId)

        if (chat != null) {
            val lastMessage = chatRepository.getLastMessageForChat(chat.id)
            val unreadMessageCount = chatRepository.getUnreadMessagesCountForUser(userId, chat.id)
            return chat.toChatResponse(lastMessage?.toMessageResponse(), unreadMessageCount)
        }

        val firstUser = userRepository.getUserById(userId) ?: throw ConflictException("User does not exist")
        val secondUser = userRepository.getUserById(dialogUserId) ?: throw ConflictException("User does not exist")

        val createdChat = chatRepository.createChat(
            Chat(
                name = null,
                type = ChatType.DIALOG,
                chatPictureUrl = null,
                participants = listOf(
                    ChatParticipant(user = firstUser),
                    ChatParticipant(user = secondUser)
                ),
                isActive = false,
            )
        ) ?: throw Exception()
        return createdChat.toChatResponse(null, 0)
    }

    suspend fun getMessagesForChat(userId: Int, chatId: Int, page: Int, pageSize: Int): List<MessageResponse> {
        val isChatParticipant = chatRepository.isChatParticipant(chatId, userId)
        if (!isChatParticipant) {
            throw ForbiddenException("You are not a chat participant.")
        }

        val messages = chatRepository.getMessagesForChat(chatId, page, pageSize)
        return messages.map { message ->
            message.toMessageResponse()
        }
    }
}