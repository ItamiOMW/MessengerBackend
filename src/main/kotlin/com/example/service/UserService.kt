package com.example.service

import com.example.data.mapper.toUpdateUser
import com.example.data.model.MessagesPermission
import com.example.data.model.User
import com.example.data.repository.contact.ContactRepository
import com.example.data.repository.user.UserRepository
import com.example.data.request.UpdateProfileRequest
import com.example.data.response.ProfileResponse
import com.example.exceptions.ConflictException
import com.example.exceptions.NotFoundException
import com.example.util.toLong

class UserService(
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository
) {

    private suspend fun getUserById(id: Int): User {
        return userRepository.getUserById(id) ?: throw NotFoundException("User not found.")
    }


    suspend fun updateProfile(userId: Int, updateProfile: UpdateProfileRequest): User {
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
            bio = updateProfile.bio?.trim()
        )

        return userRepository.updateUser(updateUser, userId) ?: throw NotFoundException("User not found.")
    }


    suspend fun searchForUsersByUsername(username: String): List<User> {
        return userRepository.searchUsersByUsername(username)
    }


    suspend fun getProfile(userId: Int, userIdToGetProfile: Int): ProfileResponse {
        val user = userRepository.getUserById(userIdToGetProfile) ?: throw NotFoundException("User not found.")

        val isBlockedByMe = userRepository.isBlocked(userId, userIdToGetProfile)
        val isBlockedByUser = userRepository.isBlocked(userIdToGetProfile, userId)
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
            lastActivity = user.lastActivity.toLong()
        )
    }


    suspend fun blockUser(userId: Int, userIdToBlock: Int) {

        if (userId == userIdToBlock) {
            throw ConflictException("You cannot block yourself.")
        }

        userRepository.blockUser(userId, userIdToBlock)
    }


    suspend fun unblockUser(userId: Int, userIdToUnblock: Int) {
        userRepository.unblockUser(userId, userIdToUnblock)
    }

}