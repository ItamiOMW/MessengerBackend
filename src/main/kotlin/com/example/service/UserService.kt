package com.example.service

import com.example.data.model.UpdateUser
import com.example.data.model.User
import com.example.data.repository.user.UserRepository

class UserService(
    private val userRepository: UserRepository,
) {


    suspend fun getUserById(id: Int): User? {
        return userRepository.getUserById(id)
    }

    suspend fun updateProfile(userId: Int, updateUser: UpdateUser): User? {
        return userRepository.updateUser(updateUser, userId)
    }

    suspend fun searchForUsersByUsername(username: String): List<User> {
        return userRepository.searchUsersByUsername(username)
    }

}