package com.example.data.mapper

import com.example.data.database.exposed.entity.ChatEntity
import com.example.data.database.exposed.entity.MessageEntity
import com.example.data.model.chat.Chat
import com.example.data.model.chat.ChatParticipant
import com.example.data.model.chat.Message
import com.example.data.response.ChatParticipantResponse
import com.example.data.response.ChatResponse
import com.example.data.response.MessageResponse
import com.example.util.toLong


fun MessageEntity.toMessage(pictureUrls: List<String>?, usersSeenMessage: List<Int>): Message = Message(
    id = this.id.value,
    text = this.text,
    type = this.type,
    userIdsSeenMessage = usersSeenMessage,
    pictureUrls = pictureUrls,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    author = this.author.toUser(),
    chatId = this.chat.id.value
)

fun ChatEntity.toChat(participants: List<ChatParticipant>): Chat = Chat(
    id = this.id.value,
    name = this.name,
    type = this.type,
    isActive = this.isActive,
    chatPictureUrl = this.chatPictureUrl,
    participants = participants
)

fun Chat.toChatResponse(lastMessage: MessageResponse? = null, unreadMessagesCount: Int): ChatResponse = ChatResponse(
    id = this.id,
    name = this.name,
    chatType = this.type,
    chatPictureUrl = this.chatPictureUrl,
    participants = this.participants.map { it.toChatParticipantResponse() },
    lastMessage = lastMessage,
    unreadMessagesCount = unreadMessagesCount
)

fun ChatParticipant.toChatParticipantResponse() = ChatParticipantResponse(
    user = this.user.toSimpleUserResponse(),
    role = this.participantRole
)

fun Message.toMessageResponse() = MessageResponse(
    id = this.id,
    chatId = this.chatId,
    authorId = this.author.id,
    authorFullName = this.author.fullName,
    authorProfilePictureUrl = this.author.profilePictureUrl,
    text = this.text,
    type = this.type,
    isRead = this.userIdsSeenMessage.any { it != this.author.id },
    usersSeenMessage = this.userIdsSeenMessage,
    pictureUrls = this.pictureUrls,
    createdAt = this.createdAt.toLong(),
    updatedAt = this.updatedAt?.toLong()
)

