package com.example.service

import com.example.data.mapper.toSimpleUserResponse
import com.example.data.mapper.toUpdateUser
import com.example.data.model.contact.ContactRequestStatus
import com.example.data.model.users.MessagesPermission
import com.example.data.model.users.User
import com.example.data.repository.block.BlockRepository
import com.example.data.repository.contact.ContactRepository
import com.example.data.repository.user.UserRepository
import com.example.data.request.UpdateProfileRequest
import com.example.data.response.MyUserResponse
import com.example.data.response.ProfileResponse
import com.example.data.response.SimpleUserResponse
import com.example.exceptions.ConflictException
import com.example.exceptions.NotFoundException
import com.example.util.toLong

class UserService(
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
    private val blockRepository: BlockRepository
) {

    private suspend fun getUserById(id: Int): User {
        return userRepository.getUserById(id) ?: throw NotFoundException("User not found.")
    }

    suspend fun updateProfile(
        userId: Int,
        updateProfile: UpdateProfileRequest,
        profilePictureUrl: String? = null,
    ): MyUserResponse {
        val user = getUserById(userId)

        updateProfile.username?.let { username ->
            val isUsernameTaken = userRepository.getUserByUsername(username) != null

            if (isUsernameTaken) {
                throw ConflictException("Username is already taken.")
            }
        }

        val updateUser = user.toUpdateUser().copy(
            fullName = updateProfile.fullName?.trim() ?: user.fullName,
            username = updateProfile.username?.trim(),
            bio = updateProfile.bio?.trim(),
            profilePictureUrl = profilePictureUrl ?: user.profilePictureUrl
        )

        userRepository.updateUser(updateUser, userId)

        return getMyUser(userId)
    }

    suspend fun getMyUser(userId: Int): MyUserResponse {
        val user = getUserById(userId)

        val activeContactRequestsCount = contactRepository.getContactRequestsByUserId(userId)
            .count { it.status == ContactRequestStatus.PENDING }

        val blockedUsersCount = blockRepository.getBlockedUsers(userId).count()

        return MyUserResponse(
            id = user.id,
            email = user.email,
            fullName = user.fullName,
            username = user.username,
            bio = user.bio,
            profilePictureUrl = user.profilePictureUrl,
            contactRequestsCount = activeContactRequestsCount,
            blockedUsersCount = blockedUsersCount,
            messagesPermission = user.messagesPermission
        )
    }

    suspend fun searchForUsersByUsername(username: String): List<User> {
        return userRepository.searchUsersByUsername(username)
    }

    suspend fun getProfile(userId: Int, userIdToGetProfile: Int): ProfileResponse {
        val user = getUserById(userId)

        val isBlockedByMe = blockRepository.isBlocked(userId, userIdToGetProfile)
        val isBlockedByUser = blockRepository.isBlocked(userIdToGetProfile, userId)
        val areContacts = contactRepository.areContacts(userId, userIdToGetProfile)

        val canSendMessage = !(isBlockedByUser or isBlockedByMe or
                (user.messagesPermission == MessagesPermission.CONTACTS_ONLY && !areContacts))

        return ProfileResponse(
            userId = user.id,
            fullName = user.fullName,
            username = user.username,
            bio = user.bio,
            profilePictureUrl = user.profilePictureUrl,
            isOwnProfile = userId == userIdToGetProfile,
            isContact = areContacts,
            canSendMessage = canSendMessage,
            isBlockedByMe = isBlockedByMe,
            isBlockedByUser = isBlockedByUser,
            isOnline = user.isOnline,
            lastActivity = user.lastActivity.toLong()
        )
    }

    suspend fun getUsersByIds(userId: Int, ids: List<Int>): List<SimpleUserResponse> {
        return userRepository.getUsersByIds(ids).map { it.toSimpleUserResponse() }
    }

}