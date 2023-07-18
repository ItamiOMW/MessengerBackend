package com.example.data.repository.contact

import com.example.data.model.Contact
import com.example.data.model.ContactRequest
import com.example.data.model.User

interface ContactRepository {

    suspend fun createContactRequest(sender: User, recipient: User): ContactRequest?

    suspend fun acceptContactRequest(contactRequest: ContactRequest)

    suspend fun declineContactRequest(contactRequest: ContactRequest)

    suspend fun getContactsByUserId(userId: Int): List<User>

    suspend fun getContactRequestsByUserId(userId: Int): List<ContactRequest>

    suspend fun getContactRequestById(id: Int): ContactRequest?

    suspend fun getMyContactRequestsByUserId(userId: Int): List<ContactRequest>

    suspend fun areContacts(userId: Int, contactId: Int): Boolean

    suspend fun deleteContact(contact: Contact)

    suspend fun cancelContactRequest(contactRequest: ContactRequest)

    suspend fun hasActiveFriendRequest(firstUserId: Int, secondUserId: Int): Boolean

}