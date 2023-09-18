package com.example.routes.user

import com.example.API_VERSION
import io.ktor.resources.*


object UserRoutes {

    private const val USERS = "$API_VERSION/users"

    private const val SEARCH_FOR_USERS = "$API_VERSION/users/search"
    private const val PROFILE = "$USERS/profile"
    private const val UPDATE_PROFILE = "$PROFILE/update"
    private const val GET_PROFILE = "$PROFILE/{id}"
    private const val BLOCK_USER = "$USERS/{id}/block"
    private const val UNBLOCK_USER = "$USERS/{id}/unblock"

    @Resource(UPDATE_PROFILE)
    class UpdateProfileRoute()

    @Resource(SEARCH_FOR_USERS)
    class SearchForUsers

    @Resource(GET_PROFILE)
    class GetUserProfileRoute(val id: Int)

    @Resource(USERS)
    class GetUsersByIdsRoute

    @Resource(BLOCK_USER)
    class BlockUserRoute(val id: Int)

    @Resource(UNBLOCK_USER)
    class UnblockUserRoute(val id: Int)


}