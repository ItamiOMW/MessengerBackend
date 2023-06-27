package com.example.exceptions

data class ExceptionCode(
    val code: String,
    val description: String
) {

    companion object {

        // 400s
        val MissingArguments = ExceptionCode("400.1", "Missing arguments")
        val InvalidRegisterCredentials = ExceptionCode("400.2", "Invalid email or password.")
        val InvalidVerificationCode = ExceptionCode("400.3", "Invalid verification code.")
        val InvalidPasswordResetCode = ExceptionCode("400.4", "Invalid password reset code.")
        val InvalidEmail = ExceptionCode("400.5", "Invalid password email.")
        val InvalidPassword = ExceptionCode("400.6", "Invalid password.")

        // 401s
        val InvalidLoginCredentials = ExceptionCode("401.1", "Invalid email or password.")

        // 403s
        val UserNotActive = ExceptionCode("403.1", "User's account is not activated.")
        val PasswordResetNotAllowed = ExceptionCode("403.2", "Password reset is not allowed.")

        // 404s
        val UserDoesNotExist = ExceptionCode("404.1", "User with this email does not exists.")

        // 409s
        val UserAlreadyExists = ExceptionCode("409.1", "User with this email already exists.")

    }

}