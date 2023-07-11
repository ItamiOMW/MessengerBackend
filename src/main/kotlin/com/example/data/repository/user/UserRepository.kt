package com.example.data.repository.user

import com.example.data.model.UpdateUser
import com.example.data.model.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun updateUser(updateUser: UpdateUser, id: Int): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserById(id: Int): User?

    suspend fun searchUsersByUsername(username: String): List<User>

}