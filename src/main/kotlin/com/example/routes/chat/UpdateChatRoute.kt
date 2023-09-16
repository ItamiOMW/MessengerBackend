package com.example.routes.chat


import com.example.data.database.firebase.FirebaseStorageUrl
import com.example.data.database.firebase.FirebaseStorageUrl.getDownloadUrl
import com.example.data.database.firebase.FirebaseStorageUrl.reference
import com.example.data.request.UpdateChatRequest
import com.example.data.response.SuccessfulResponse
import com.example.exceptions.BadRequestException
import com.example.service.chat.ChatService
import com.example.util.convert
import com.example.util.userId
import com.google.cloud.storage.Bucket
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.updateChat(
    chatService: ChatService,
    bucket: Bucket,
    gson: Gson,
) {
    authenticate {
        put<ChatRoutes.Chat> { route ->
            val multipart = call.receiveMultipart()
            var updateChatRequest: UpdateChatRequest? = null
            var chatPictureUrl: String? = null

            val userId = call.userId()
            val chatId = route.id

            val chat = chatService.getChatById(userId, chatId)

            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_chat_request") {
                            updateChatRequest = gson.fromJson(
                                partData.value,
                                UpdateChatRequest::class.java
                            )
                        }
                    }

                    is PartData.FileItem -> {
                        if (partData.name == "chat_picture") {
                            chat.chatPictureUrl?.let { url ->
                                bucket.storage.delete(url)
                            }
                            val (fileName, fileBytes) = partData.convert()
                            bucket.create("${FirebaseStorageUrl.profilePicturePath}/$fileName", fileBytes, "image/png")
                            chatPictureUrl = FirebaseStorageUrl
                                .basePath reference FirebaseStorageUrl.profilePicturePath getDownloadUrl fileName
                        }
                    }

                    else -> Unit
                }
            }

            updateChatRequest?.let { request ->
                val chatResponse = chatService.updateChat(userId, chatId, request, chatPictureUrl)

                call.respond(
                    status = HttpStatusCode.OK,
                    SuccessfulResponse(
                        message = "Successful.",
                        data = chatResponse
                    )
                )
            } ?: throw BadRequestException()
        }
    }
}