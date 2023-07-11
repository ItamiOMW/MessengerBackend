package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class MyUserResponse(
    val id: Int,
    val email: String,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val isActive: Boolean,
    val isAdmin: Boolean,
    val isPasswordResetAllowed: Boolean,
//    val contacts: List<SimpleUser>,
//    val contactRequests: List<SimpleUser>,
//    val myContactRequests: List<SimpleUser>,
//    val blockedUsers: List<SimpleUser>,
//    val enableNotification: Boolean,
//    val whoCanSendMessages: Int
) {
    companion object {
        const val ANYONE_CAN_SEND_MESSAGE = 0
        const val CONTACTS_ONLY_CAN_SEND_MESSAGE = 1
    }
}
