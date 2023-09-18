package com.example.service.chat

enum class WebSocketEvent {
    SEND_MESSAGE,
    EDIT_MESSAGE,
    READ_MESSAGE,
    DELETE_MESSAGE,
    CREATE_CHAT,
    DELETE_CHAT,
    UPDATE_CHAT,
    DELETE_CHAT_PARTICIPANT,
    ADD_CHAT_PARTICIPANTS,
    LEAVE_CHAT,
    ERROR,
}