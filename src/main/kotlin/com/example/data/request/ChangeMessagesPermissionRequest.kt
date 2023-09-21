package com.example.data.request

import com.example.data.model.users.MessagesPermission
import kotlinx.serialization.Serializable

@Serializable
data class ChangeMessagesPermissionRequest(
    val messagesPermission: MessagesPermission
)