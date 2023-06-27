package com.example.email

import com.example.util.Constants
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class SimpleEmailManager: EmailManager {

    private val hostEmail = System.getenv(Constants.EMAIL_HOST_USER_KEY)
    private val hostPassword = System.getenv(Constants.EMAIL_HOST_PASSWORD_KEY)
    private val host = System.getenv(Constants.EMAIL_HOST_KEY)

    override fun sendEmail(targetEmail: String, message: String, subject: String) {
        val simpleEmail = SimpleEmail()
        simpleEmail.hostName = host
        simpleEmail.setSmtpPort(465)
        simpleEmail.setAuthenticator(DefaultAuthenticator(hostEmail, hostPassword))
        simpleEmail.isSSLOnConnect = true
        simpleEmail.setFrom(hostEmail)
        simpleEmail.subject = subject
        simpleEmail.setMsg(message)
        simpleEmail.addTo(targetEmail)
        simpleEmail.send()
    }

}