package com.example.data.repository.contact

import com.example.data.database.exposed.DatabaseFactory.dbQuery
import com.example.data.database.exposed.entity.ContactRequestEntity
import com.example.data.database.exposed.entity.UserEntity
import com.example.data.database.exposed.table.ContactRequests
import com.example.data.database.exposed.table.Contacts
import com.example.data.database.exposed.table.Users
import com.example.data.mapper.toContactRequest
import com.example.data.mapper.toUser
import com.example.data.model.contact.Contact
import com.example.data.model.contact.ContactRequest
import com.example.data.model.contact.ContactRequestStatus
import com.example.data.model.users.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ContactRepositoryImpl : ContactRepository {

    override suspend fun createContactRequest(sender: User, recipient: User): ContactRequest? {
        return dbQuery {
            val contactRequest = ContactRequestEntity.new {
                this.sender = UserEntity[sender.id]
                this.recipient = UserEntity[recipient.id]
            }.toContactRequest()

            return@dbQuery contactRequest
        }
    }

    override suspend fun acceptContactRequest(contactRequest: ContactRequest) {
        dbQuery {
            val contactRequestEntity = ContactRequestEntity.findById(contactRequest.id)

            contactRequestEntity?.let {
                it.status = ContactRequestStatus.ACCEPTED
            }
            Contacts.insert { table ->
                table[firstUserId] = contactRequest.sender.id
                table[secondUserId] = contactRequest.recipient.id
            }
        }
    }

    override suspend fun declineContactRequest(contactRequest: ContactRequest) {
        dbQuery {
            val contactRequestEntity = ContactRequestEntity.findById(contactRequest.id)

            contactRequestEntity?.let {
                it.status = ContactRequestStatus.DECLINED
            }
        }
    }

    override suspend fun getContactsByUserId(userId: Int): List<User> {
        return dbQuery {
            val ids = (Contacts.slice(Contacts.firstUserId, Contacts.secondUserId)
                .select {
                    (Contacts.firstUserId eq userId) or (Contacts.secondUserId eq userId)
                }
                .flatMap { listOfNotNull(it[Contacts.firstUserId], it[Contacts.secondUserId]) }
                .distinct())
                .filter { it.value != userId }
            UserEntity.find { Users.id inList ids }.map { it.toUser() }
        }
    }

    override suspend fun getContactRequestsByUserId(userId: Int): List<ContactRequest> {
        return dbQuery {
            ContactRequestEntity.find {
                ContactRequests.recipientId eq userId
            }.mapNotNull { it.toContactRequest() }
        }
    }

    override suspend fun getContactRequestById(id: Int): ContactRequest? {
        return dbQuery {
            ContactRequestEntity.findById(id).toContactRequest()
        }
    }

    override suspend fun getMyContactRequestsByUserId(userId: Int): List<ContactRequest> {
        return dbQuery {
            ContactRequestEntity.find {
                ContactRequests.senderId eq userId
            }.mapNotNull { it.toContactRequest() }
        }
    }

    override suspend fun areContacts(userId: Int, contactId: Int): Boolean {
        return dbQuery {
            val count = Contacts.select {
                ((Contacts.firstUserId eq userId) and (Contacts.secondUserId eq contactId)) or
                        (((Contacts.firstUserId eq contactId) and (Contacts.secondUserId eq userId)))
            }.count()
            return@dbQuery count > 0
        }
    }

    override suspend fun deleteContact(contact: Contact) {
        dbQuery {
            Contacts.deleteWhere {
                ((firstUserId eq contact.firstUserId) and (secondUserId eq contact.secondUserId)) or
                        (((firstUserId eq contact.secondUserId) and (secondUserId eq contact.firstUserId)))
            }
        }
    }

    override suspend fun cancelContactRequest(contactRequest: ContactRequest) {
        dbQuery {
            val contactRequestEntity = ContactRequestEntity.findById(contactRequest.id)

            contactRequestEntity?.let {
                it.status = ContactRequestStatus.CANCELED
            }
        }
    }

    override suspend fun getActiveContactRequestByUsers(firstUserId: Int, secondUserId: Int): ContactRequest? {
        return dbQuery {
             ContactRequestEntity.find {
                ((ContactRequests.senderId eq firstUserId) and (ContactRequests.recipientId eq secondUserId)) or
                        (((ContactRequests.senderId eq secondUserId) and (ContactRequests.recipientId eq firstUserId))) and
                        (ContactRequests.status eq ContactRequestStatus.PENDING)
            }.firstOrNull().toContactRequest()
        }
    }

}