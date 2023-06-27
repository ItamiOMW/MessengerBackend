package com.example.email

interface EmailManager {

    fun sendEmail(targetEmail: String, message: String, subject: String)

}