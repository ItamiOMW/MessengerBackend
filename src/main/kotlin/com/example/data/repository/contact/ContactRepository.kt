package com.example.data.repository.contact

import com.example.data.model.contact.Contact
import com.example.data.model.contact.ContactRequest
import com.example.data.model.users.User

interface ContactRepository {

    suspend fun createContactRequest(sender: User, recipient: User): ContactRequest?

    suspend fun acceptContactRequest(contactRequest: ContactRequest)

    suspend fun declineContactRequest(contactRequest: ContactRequest)

    suspend fun getContactsByUserId(userId: Int): List<User>

    suspend fun getContactRequestsByUserId(userId: Int): List<ContactRequest>

    suspend fun getContactRequestById(id: Int): ContactRequest?

    suspend fun areContacts(userId: Int, contactId: Int): Boolean

    suspend fun deleteContact(contact: Contact)

    suspend fun cancelContactRequest(contactRequest: ContactRequest)

    suspend fun getActiveContactRequestByUsers(firstUserId: Int, secondUserId: Int): ContactRequest?

}