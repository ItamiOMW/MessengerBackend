package com.example.authentication

import com.auth0.jwt.JWTVerifier
import com.example.data.model.User

interface TokenManager {

    fun generateToken(user: User): String

}