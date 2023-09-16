package com.example.data.model.chat

enum class MessageType {
    MESSAGE,
    CHAT_CREATED,
    CHAT_NAME_UPDATED,
    CHAT_PICTURE_UPDATED,
    INVITED,
    LEFT,
    KICKED,
    ADMIN_ROLE_ASSIGNED,
    ADMIN_ROLE_REMOVED
}