package com.example.authentication

import com.example.data.model.users.User

interface TokenManager {

    fun generateToken(user: User): String

}