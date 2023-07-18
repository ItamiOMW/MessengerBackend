package com.example.service

import com.example.data.mapper.toContactRequestResponse
import com.example.data.model.ContactRequestStatus
import com.example.data.repository.contact.ContactRepository
import com.example.data.repository.user.UserRepository
import com.example.data.response.ContactRequestResponse
import com.example.exceptions.ConflictException
import com.example.exceptions.ForbiddenException
import com.example.exceptions.NotFoundException

class ContactService(
    private val contactRepository: ContactRepository,
    private val userRepository: UserRepository
) {

    suspend fun createContactRequest(senderId: Int, recipientId: Int): ContactRequestResponse {

        if (senderId == recipientId) {
            throw ConflictException("You can't send contact request to yourself.")
        }

        val sender = userRepository.getUserById(senderId) ?: throw NotFoundException("Sender not found")
        val recipient = userRepository.getUserById(recipientId) ?: throw NotFoundException("Recipient not found.")

        val areContacts = contactRepository.areContacts(senderId, recipientId)

        if (areContacts) {
            throw ConflictException("You are already contacts.")
        }

        val hasActiveRequests = contactRepository.hasActiveFriendRequest(senderId, recipientId)

        if (hasActiveRequests) {
            throw ConflictException("You already have an active contact request with this user.")
        }

        val contactRequest = contactRepository.createContactRequest(sender, recipient) ?: throw Exception()

        return contactRequest.toContactRequestResponse()
    }


    suspend fun acceptContactRequest(userId: Int, requestId: Int) {
        val contactRequest = contactRepository.getContactRequestById(requestId)
            ?: throw NotFoundException("Contact Request not found.")

        if (userId != contactRequest.recipient.id) {
            throw ForbiddenException("Only the recipient can accept the request.")
        }

        if (contactRequest.status != ContactRequestStatus.PENDING) {
            throw ForbiddenException("Contact request has been canceled or declined.")
        }

        contactRepository.acceptContactRequest(contactRequest)
    }


    suspend fun declineContactRequest(userId: Int, requestId: Int) {
        val contactRequest = contactRepository.getContactRequestById(requestId)
            ?: throw NotFoundException("Contact Request not found.")

        if (userId != contactRequest.recipient.id) {
            throw ForbiddenException("Only the recipient can decline the request.")
        }

        contactRepository.declineContactRequest(contactRequest)
    }


    suspend fun cancelContactRequest(userId: Int, requestId: Int) {
        val contactRequest = contactRepository.getContactRequestById(requestId)
            ?: throw NotFoundException("Contact Request not found.")

        if (contactRequest.sender.id != userId) {
            throw ForbiddenException("Only the sender can cancel the request.")
        }

        contactRepository.cancelContactRequest(contactRequest)
    }


    suspend fun getContactRequests(userId: Int): List<ContactRequestResponse> {
        return contactRepository.getContactRequestsByUserId(userId)
            .filter { it.status == ContactRequestStatus.PENDING }
            .map { it.toContactRequestResponse() }
    }




}