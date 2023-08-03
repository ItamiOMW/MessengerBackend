package com.example.validation

import com.example.data.request.UpdateProfileRequest
import com.example.exceptions.BadRequestException
import com.example.util.Constants
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.profileRequestsValidation() {
    validate<UpdateProfileRequest> { updateProfileRequest ->

        with(updateProfileRequest) {
            fullName?.let {
                if (it.isBlank()) throw BadRequestException("Full name is blank.")
                if (it.length > Constants.MAX_FULL_NAME_LENGTH) throw BadRequestException("Full name is too long.")
            }
            bio?.let {
                if (it.isBlank()) throw BadRequestException("Bio is blank.")
                if (it.length > Constants.MAX_BIO_LENGTH) throw BadRequestException("Bio is too long.")
            }
            username?.let {
                if (it.isBlank()) throw BadRequestException("Username is blank.")
                if (it.length > Constants.MAX_USERNAME_LENGTH) throw BadRequestException("Username is too long.")
                if (it.length < Constants.MIN_USERNAME_LENGTH) throw BadRequestException("Username is too short.")
            }
        }

        ValidationResult.Valid
    }
}