package com.example.data.repository.user

import com.example.data.model.users.UpdateUser
import com.example.data.model.users.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun updateUser(updateUser: UpdateUser, id: Int): User?

    suspend fun setUserOnlineStatus(id: Int, isOnline: Boolean)

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserById(id: Int): User?

    suspend fun getUsersByIds(idList: List<Int>): List<User>

    suspend fun getUserByUsername(username: String): User?

    suspend fun searchUsersByUsername(username: String): List<User>

}