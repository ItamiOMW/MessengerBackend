package com.example.plugins

import com.example.exceptions.generalStatusPages
import com.example.routes.auth.*
import com.example.routes.chat.*
import com.example.routes.contact.*
import com.example.routes.user.*
import com.example.service.AuthService
import com.example.service.BlockService
import com.example.service.ContactService
import com.example.service.UserService
import com.example.service.chat.ChatService
import com.google.cloud.storage.Bucket
import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(StatusPages) {
        generalStatusPages()
    }

    val authService: AuthService by inject()
    val contactService: ContactService by inject()
    val userService: UserService by inject()
    val blockService: BlockService by inject()
    val chatService: ChatService by inject()
    val cloudStorageBucket: Bucket by inject()

    val gson: Gson by inject()

    routing {
        get("/") {
            call.respondText("Welcome to Itami Chat!")
        }

        //Auth
        register(authService)
        login(authService, userService)
        authenticate(userService)
        sendVerification(authService)
        sendPasswordChangeCode(authService)
        sendPasswordResetCode(authService)
        verifyEmail(authService, userService)
        verifyPasswordChangeCode(authService)
        changePassword(authService)
        resetPassword(authService)
        deleteAccount(authService)
        changeMessagesPermission(authService)

        //Contacts
        sendContactRequest(contactService)
        acceptContactRequest(contactService)
        declineContactRequest(contactService)
        cancelContactRequest(contactService)
        getContactRequests(contactService)
        getContacts(contactService)
        deleteContact(contactService)

        //Users
        updateProfile(userService, cloudStorageBucket, gson)
        getUserProfile(userService)
        getUsersByIds(userService)
        blockUser(blockService)
        unblockUser(blockService)
        searchUsers(userService)
        getBlockedUsers(userService)

        //Chats
        chatsWebsocket(chatService)
        getChatById(chatService)
        getDialogChatByUserId(chatService)
        chatWebsocket(chatService)
        getChats(chatService)
        getMessagesForChat(chatService)
        createChat(chatService, cloudStorageBucket, gson)
        deleteChat(chatService, cloudStorageBucket)
        updateChat(chatService, bucket = cloudStorageBucket, gson = gson)
        addChatParticipants(chatService)
        deleteChatParticipant(chatService)
        leaveChat(chatService)
        removeAdmin(chatService)
        assignAdmin(chatService)
    }

}
