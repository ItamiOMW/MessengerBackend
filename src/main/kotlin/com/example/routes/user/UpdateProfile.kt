package com.example.routes.user

import com.example.data.database.firebase.FirebaseStorageUrl
import com.example.data.database.firebase.FirebaseStorageUrl.getDownloadUrl
import com.example.data.database.firebase.FirebaseStorageUrl.reference
import com.example.data.request.UpdateProfileRequest
import com.example.data.response.SuccessfulResponse
import com.example.exceptions.BadRequestException
import com.example.service.UserService
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


fun Route.updateProfile(userService: UserService, bucket: Bucket, gson: Gson) {
    authenticate {

        put<UserRoutes.UpdateProfileRoute> {
            val multipart = call.receiveMultipart()
            var updateProfile: UpdateProfileRequest? = null
            var profilePictureUrl: String? = null

            val userId = call.userId()
            val user = userService.getMyUser(userId)

            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                            updateProfile = gson.fromJson(
                                partData.value,
                                UpdateProfileRequest::class.java
                            )
                        }
                    }

                    is PartData.FileItem -> {
                        if (partData.name == "profile_picture") {
                            user.profilePictureUrl?.let { url ->
                                bucket.storage.delete(url)
                            }
                            val (fileName, fileBytes) = partData.convert()
                            bucket.create("${FirebaseStorageUrl.profilePicturePath}/$fileName", fileBytes, "image/png")
                            profilePictureUrl = FirebaseStorageUrl
                                .basePath reference FirebaseStorageUrl.profilePicturePath getDownloadUrl fileName
                        }
                    }

                    else -> Unit
                }
            }

            updateProfile?.let { request ->
                val myUserResponse = userService.updateProfile(userId, request, profilePictureUrl)

                call.respond(
                    status = HttpStatusCode.OK,
                    SuccessfulResponse(
                        message = "Successfully updated profile.",
                        data = myUserResponse
                    )
                )
            } ?: throw BadRequestException()

        }
    }
}