package com.example.data.repository.chat

import com.example.data.model.chat.*

interface ChatRepository {

    suspend fun getChats(userId: Int): List<Chat>

    suspend fun getChatById(chatId: Int): Chat?

    suspend fun isChatParticipant(chatId: Int, userId: Int): Boolean

    suspend fun getDialogChatByUsers(firstUserId: Int, secondUserId: Int): Chat?

    suspend fun createChat(chat: Chat): Chat?

    suspend fun updateChat(chatId: Int, updateChat: UpdateChat): Chat?

    suspend fun deleteChat(chatId: Int)

    suspend fun addChatParticipants(chatId: Int, participants: List<ChatParticipant>)

    suspend fun changeChatParticipantRole(chatId: Int, participantId: Int, role: ParticipantRole)

    suspend fun deleteChatParticipants(chatId: Int, participantIds: List<Int>)

    suspend fun getMessagesForChat(chatId: Int, page: Int, pageSize: Int): List<Message>

    suspend fun getMessageById(messageId: Int): Message?

    suspend fun getLastMessageForChat(chatId: Int): Message?

    suspend fun getUnreadMessagesCountForUser(userId: Int, chatId: Int): Int

    suspend fun readMessage(userId: Int, messageId: Int)

    suspend fun isMessageRead(messageId: Int): Boolean

    suspend fun isMessageReadByUser(userId: Int, messageId: Int): Boolean

    suspend fun createMessage(createMessage: CreateMessage): Message?

    suspend fun updateMessage(messageId: Int, updateMessage: UpdateMessage): Message?

    suspend fun deleteMessage(messageId: Int)

}