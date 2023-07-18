package com.example.routes.user

import com.example.data.mapper.toMyUserResponse
import com.example.data.request.UpdateProfileRequest
import com.example.data.response.ApiResponse
import com.example.service.UserService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.updateProfile(userService: UserService) {
     authenticate {
         put<UserRoutes.UpdateProfileRoute> {
             val userId = call.userId()
             val updateProfile = call.receive<UpdateProfileRequest>()

             val updatedUser = userService.updateProfile(userId, updateProfile)

             call.respond(
                 status = HttpStatusCode.OK,
                 ApiResponse(
                     successful = true,
                     message = "Successfully updated profile.",
                     data = updatedUser.toMyUserResponse()
                 )
             )
         }
     }
}