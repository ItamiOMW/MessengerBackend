package com.example.data.repository.contact

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.table.ContactRequests
import com.example.data.database.table.Contacts
import com.example.data.database.table.Users
import com.example.data.mapper.toContactRequest
import com.example.data.mapper.toUser
import com.example.data.model.Contact
import com.example.data.model.ContactRequest
import com.example.data.model.ContactRequestStatus
import com.example.data.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ContactRepositoryImpl : ContactRepository {


    override suspend fun createContactRequest(contactRequest: ContactRequest): Int {
        return dbQuery {
            val insertStatement = ContactRequests.insert { table ->
                table[senderId] = contactRequest.senderId
                table[recipientId] = contactRequest.recipientId
            }
            return@dbQuery insertStatement[ContactRequests.id]
        }
    }

    override suspend fun acceptContactRequest(contactRequest: ContactRequest) {
        dbQuery {
            ContactRequests.update({ ContactRequests.id eq contactRequest.id }) { table ->
                table[status] = ContactRequestStatus.ACCEPTED
            }
            Contacts.insert { table ->
                table[firstUserId] = contactRequest.senderId
                table[secondUserId] = contactRequest.recipientId
            }
        }
    }

    override suspend fun declineContactRequest(contactRequest: ContactRequest) {
        dbQuery {
            ContactRequests.update({ ContactRequests.id eq contactRequest.id }) { table ->
                table[status] = ContactRequestStatus.DECLINED
            }
        }
    }

    override suspend fun getContactsByUserId(userId: Int): List<User> {
        return dbQuery {
            return@dbQuery (Contacts innerJoin Users)
                .slice(Users.columns)
                .select { (Contacts.firstUserId eq userId) or (Contacts.secondUserId eq userId) }
                .mapNotNull<ResultRow, User> { it.toUser() }
        }
    }

    override suspend fun getContactRequestsByUserId(userId: Int): List<User> {
        return dbQuery {
            return@dbQuery (ContactRequests innerJoin Users)
                .slice(Users.columns)
                .select { (ContactRequests.recipientId eq userId) }
                .mapNotNull<ResultRow, User> { it.toUser() }
        }
    }

    override suspend fun getContactRequestsById(id: Int): ContactRequest? {
        return dbQuery {
            ContactRequests.select { ContactRequests.id eq id }.firstOrNull().toContactRequest()
        }
    }

    override suspend fun getMyContactRequestsByUserId(userId: Int): List<User> {
        return dbQuery {
            return@dbQuery (ContactRequests innerJoin Users)
                .slice(Users.columns)
                .select { (ContactRequests.senderId eq userId) }
                .mapNotNull<ResultRow, User> { it.toUser() }
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
            ContactRequests.update({ ContactRequests.id eq contactRequest.id }) { table ->
                table[status] = ContactRequestStatus.CANCELED
            }
        }
    }

    override suspend fun hasActiveFriendRequest(firstUserId: Int, secondUserId: Int): Boolean {
        return dbQuery {
            val activeRequests = ContactRequests.select {
                ((ContactRequests.senderId eq firstUserId) and (ContactRequests.recipientId eq secondUserId)) or
                        (((ContactRequests.senderId eq secondUserId) and (ContactRequests.recipientId eq firstUserId))) and
                        (ContactRequests.status eq ContactRequestStatus.PENDING)
            }.count()

            return@dbQuery activeRequests > 0
        }
    }

}