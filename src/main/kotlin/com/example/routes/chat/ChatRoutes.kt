package com.example.routes.chat

import com.example.API_VERSION
import io.ktor.resources.*

object ChatRoutes {

    private const val CHATS = "$API_VERSION/chats"

    private const val GET_MESSAGE_FOR_CHAT = "$CHATS/{id}/messages/{page}/{pageSize}"
    private const val GET_DIALOG_CHAT = "$CHATS/dialog/{userId}"
    private const val CHAT = "$CHATS/{id}"

    private const val CHAT_PARTICIPANTS = "$CHAT/participants"

    private const val CHAT_PARTICIPANT = "$CHAT_PARTICIPANTS/{userId}"
    private const val PARTICIPANT_ADMIN_ROLE = "$CHAT_PARTICIPANT/admin"

    private const val LEAVE_CHAT = "$CHAT/leave"

    const val CHATS_WEBSOCKET = "$CHATS/ws"
    const val CHAT_WEBSOCKET = "$CHATS/{chatId}/ws"


    @Resource(CHATS)
    class Chats

    @Resource(PARTICIPANT_ADMIN_ROLE)
    class AdminRole(val id: Int, val userId: Int)

    @Resource(GET_MESSAGE_FOR_CHAT)
    class GetMessagesForChat(val id: Int, val page: Int, val pageSize: Int)

    @Resource(CHAT)
    class Chat(val id: Int)

    @Resource(LEAVE_CHAT)
    class LeaveChat(val id: Int)

    @Resource(CHAT_PARTICIPANT)
    class ChatParticipant(val id: Int, val userId: Int)

    @Resource(CHAT_PARTICIPANTS)
    class ChatParticipants(val id: Int)

    @Resource(GET_DIALOG_CHAT)
    class GetDialogChat(val userId: Int)
}