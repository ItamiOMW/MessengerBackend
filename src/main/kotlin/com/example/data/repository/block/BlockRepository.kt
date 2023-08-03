package com.example.data.repository.block

import com.example.data.model.User

interface BlockRepository {

    suspend fun getBlockedUsers(userId: Int): List<User>

    suspend fun blockUser(userId: Int, userIdToBlock: Int)

    suspend fun unblockUser(userId: Int, userIdToUnblock: Int)

    suspend fun isBlocked(userId: Int, blockedByUserId: Int): Boolean

}