package com.example.data.repository.user

import com.example.data.database.exposed.DatabaseFactory.dbQuery
import com.example.data.database.exposed.entity.UserEntity
import com.example.data.database.exposed.table.Users
import com.example.data.mapper.toUser
import com.example.data.model.users.MessagesPermission
import com.example.data.model.users.UpdateUser
import com.example.data.model.users.User
import com.example.util.getCurrentDateTime
import org.jetbrains.exposed.sql.*

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(user: User) {
        dbQuery {
            UserEntity.new {
                this.email = user.email
                this.hashPassword = user.hashPassword
                this.fullName = user.fullName
                this.username = user.username
                this.bio = user.bio
                this.profilePictureUrl = user.profilePictureUrl
                this.emailVerificationCode = user.emailVerificationCode
                this.passwordResetCode = user.passwordResetCode
                this.isPasswordChangeAllowed = user.isPasswordChangeAllowed
                this.isActive = user.isActive
                this.isAdmin = user.isAdmin
            }
        }
    }

    override suspend fun updateUser(updateUser: UpdateUser, id: Int): User? {
        return dbQuery {
            val userEntity = UserEntity.find {
                Users.id eq id
            }.firstOrNull()

            userEntity?.let {
                it.hashPassword = updateUser.hashPassword
                it.fullName = updateUser.fullName
                it.username = updateUser.username
                it.bio = updateUser.bio
                it.profilePictureUrl = updateUser.profilePictureUrl
                it.emailVerificationCode = updateUser.emailVerificationCode
                it.passwordChangeCode = updateUser.passwordChangeCode
                it.passwordResetCode = updateUser.passwordResetCode
                it.isPasswordChangeAllowed = updateUser.isPasswordChangeAllowed
                it.isActive = updateUser.isActive
                it
            }

            userEntity?.toUser()
        }
    }

    override suspend fun deleteUser(userId: Int) {
        dbQuery {
            UserEntity.findById(userId)?.delete()
        }
    }

    override suspend fun changeMessagesPermission(userId: Int, permission: MessagesPermission) {
        dbQuery {
            UserEntity.findById(userId)?.let {
                it.messagesPermission = permission
            }
        }
    }

    override suspend fun setUserOnlineStatus(id: Int, isOnline: Boolean) {
        dbQuery {
            val userEntity = UserEntity.find { Users.id eq id }.firstOrNull()
            userEntity?.let {
                it.isOnline = isOnline
                it.lastActivity = getCurrentDateTime()
            }
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return dbQuery {
            UserEntity.find {
                Users.email eq email
            }.firstOrNull()?.toUser()
        }
    }

    override suspend fun getUserById(id: Int): User? {
        return dbQuery {
            UserEntity.findById(id)?.toUser()
        }
    }

    override suspend fun getUsersByIds(idList: List<Int>): List<User> {
        return dbQuery {
            UserEntity.find { Users.id inList idList }.map { it.toUser() }
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return dbQuery {
            UserEntity.find {
                Users.username eq username
            }.firstOrNull()?.toUser()
        }
    }

    override suspend fun searchUsersByUsername(username: String): List<User> {
        return dbQuery {
            UserEntity.find {
                Users.username
                    .trim()
                    .lowerCase()
                    .like("%$username%")
            }.map { it.toUser() }
        }
    }

}