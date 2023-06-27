@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.routes.auth

import com.example.API_VERSION
import io.ktor.server.locations.*

object AuthRoutes {

    private const val AUTH = "$API_VERSION/auth"

    const val AUTH_REGISTER = "$AUTH/register"
    const val AUTH_LOGIN = "$AUTH/login"
    const val AUTH_AUTHENTICATE = "$AUTH/authenticate"
    const val AUTH_VERIFY_EMAIL = "$AUTH/verify-email"
    const val AUTH_VERIFY_PASSWORD_RESET = "$AUTH/verify-password-reset"
    const val AUTH_SEND_VERIFICATION_CODE = "$AUTH/send-verification-code"
    const val AUTH_SEND_PASSWORD_RESET_CODE = "$AUTH/send-password-reset-code"
    const val AUTH_RESET_PASSWORD = "$AUTH/reset-password"

    @Location(AUTH_REGISTER)
    class UserRegisterRoute

    @Location(AUTH_LOGIN)
    class UserLoginRoute

    @Location(AUTH_AUTHENTICATE)
    class AuthenticateRoute

    @Location(AUTH_SEND_VERIFICATION_CODE)
    class SendVerificationCodeRoute

    @Location(AUTH_VERIFY_EMAIL)
    class VerifyEmailRoute

    @Location(AUTH_SEND_PASSWORD_RESET_CODE)
    class SendPasswordResetCodeRoute

    @Location(AUTH_VERIFY_PASSWORD_RESET)
    class VerifyPasswordResetRoute

    @Location(AUTH_RESET_PASSWORD)
    class ResetPasswordRoute


}