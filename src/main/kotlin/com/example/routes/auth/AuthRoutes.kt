
package com.example.routes.auth

import com.example.API_VERSION
import io.ktor.resources.*

object AuthRoutes {

    private const val AUTH = "$API_VERSION/auth"

    private const val AUTH_REGISTER = "$AUTH/register"
    private const val AUTH_LOGIN = "$AUTH/login"
    private const val AUTH_AUTHENTICATE = "$AUTH/authenticate"
    private const val AUTH_VERIFY_EMAIL = "$AUTH/verify-email"
    private const val AUTH_VERIFY_PASSWORD_RESET = "$AUTH/verify-password-reset"
    private const val AUTH_SEND_VERIFICATION_CODE = "$AUTH/send-verification-code"
    private const val AUTH_SEND_PASSWORD_RESET_CODE = "$AUTH/send-password-reset-code"
    private const val AUTH_RESET_PASSWORD = "$AUTH/reset-password"

    @Resource(AUTH_REGISTER)
    class UserRegisterRoute

    @Resource(AUTH_LOGIN)
    class UserLoginRoute

    @Resource(AUTH_AUTHENTICATE)
    class AuthenticateRoute

    @Resource(AUTH_SEND_VERIFICATION_CODE)
    class SendVerificationCodeRoute

    @Resource(AUTH_VERIFY_EMAIL)
    class VerifyEmailRoute

    @Resource(AUTH_SEND_PASSWORD_RESET_CODE)
    class SendPasswordResetCodeRoute

    @Resource(AUTH_VERIFY_PASSWORD_RESET)
    class VerifyPasswordResetRoute

    @Resource(AUTH_RESET_PASSWORD)
    class ResetPasswordRoute


}