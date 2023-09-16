package com.example.routes.chat


import com.example.data.database.firebase.FirebaseStorageUrl
import com.example.data.database.firebase.FirebaseStorageUrl.getDownloadUrl
import com.example.data.database.firebase.FirebaseStorageUrl.reference
import com.example.data.request.CreateChatRequest
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

fun Route.createChat(
    chatService: ChatService,
    bucket: Bucket,
    gson: Gson,
) {
    authenticate {
        post<ChatRoutes.Chats> { route ->
            val multipart = call.receiveMultipart()
            var createChatRequest: CreateChatRequest? = null
            var chatPictureUrl: String? = null

            val userId = call.userId()

            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "create_chat_request") {
                            createChatRequest = gson.fromJson(
                                partData.value,
                                CreateChatRequest::class.java
                            )
                        }
                    }

                    is PartData.FileItem -> {
                        if (partData.name == "profile_picture") {
                            val (fileName, fileBytes) = partData.convert()
                            bucket.create("${FirebaseStorageUrl.profilePicturePath}/$fileName", fileBytes, "image/png")
                            chatPictureUrl = FirebaseStorageUrl
                                .basePath reference FirebaseStorageUrl.profilePicturePath getDownloadUrl fileName
                        }
                    }

                    else -> Unit
                }
            }

            createChatRequest?.let { request ->
                val chatResponse = chatService.createChat(userId, request, chatPictureUrl)

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