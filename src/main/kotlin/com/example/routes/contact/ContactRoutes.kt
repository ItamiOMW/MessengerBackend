package com.example.routes.contact

import com.example.API_VERSION
import io.ktor.resources.*

object ContactRoutes {

    private const val CONTACTS = "$API_VERSION/contacts"

    private const val REQUESTS = "$CONTACTS/requests"

    private const val CONTACTS_SEND_REQUEST = "${REQUESTS}/send/{id}"
    private const val CONTACTS_ACCEPT_REQUEST = "${REQUESTS}/{id}/accept"
    private const val CONTACTS_DECLINE_REQUEST = "${REQUESTS}/{id}/decline"
    private const val CONTACTS_CANCEL_REQUEST = "${REQUESTS}/{id}/cancel"


    @Resource(CONTACTS)
    class GetContacts

    @Resource(CONTACTS_SEND_REQUEST)
    class SendContactRequestRoute(val id: Int)

    @Resource(CONTACTS_ACCEPT_REQUEST)
    class AcceptContactRequestRoute(val id: Int)

    @Resource(CONTACTS_DECLINE_REQUEST)
    class DeclineContactRequestRoute(val id: Int)

    @Resource(CONTACTS_CANCEL_REQUEST)
    class CancelContactRequestRoute(val id: Int)

    @Resource(REQUESTS)
    class GetContactRequestsRoute()


}