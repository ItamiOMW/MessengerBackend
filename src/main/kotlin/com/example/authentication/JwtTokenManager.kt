package com.example.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.util.Constants
import com.example.data.model.users.User
import java.util.Date

class JwtTokenManager : TokenManager {

    private val issuer = System.getenv(Constants.JWT_ISSUER_KEY)
    private val jwtSecret = System.getenv(Constants.JWT_SECRET_KEY)
    private val algorithm = Algorithm.HMAC256(jwtSecret)
    private val subject = System.getenv(Constants.JWT_SUBJECT_KEY)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    override fun generateToken(user: User): String {
        return JWT.create()
            .withSubject(subject)
            .withIssuer(issuer)
            .withClaim("id", user.id)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + Constants.MILLIS_IN_MONTH))
            .sign(algorithm)
    }

}