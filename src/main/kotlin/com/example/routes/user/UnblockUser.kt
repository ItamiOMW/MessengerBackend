package com.example.routes.user


import com.example.data.response.SuccessfulResponse
import com.example.service.BlockService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.unblockUser(
    blockService: BlockService
) {
    authenticate {
        post<UserRoutes.UnblockUserRoute> { blockUserRoute ->
            val userId = call.userId()
            val userIdToUnblock = blockUserRoute.id

            blockService.unblockUser(userId, userIdToUnblock)

            call.respond(
                status = HttpStatusCode.OK,
                SuccessfulResponse<Unit>(
                    message = "Successfully unblocked the user.",
                )
            )
        }
    }
}