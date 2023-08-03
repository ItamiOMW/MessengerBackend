package com.example.data.repository.user

import com.example.data.database.exposed.DatabaseFactory.dbQuery
import com.example.data.database.exposed.entity.UserEntity
import com.example.data.database.exposed.table.Users
import com.example.data.mapper.toUser
import com.example.data.model.UpdateUser
import com.example.data.model.User
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
                this.isPasswordResetAllowed = user.isPasswordResetAllowed
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
                it.passwordResetCode = updateUser.passwordResetCode
                it.isPasswordResetAllowed = updateUser.isPasswordResetAllowed
                it.isActive = updateUser.isActive
            }

            userEntity?.toUser()
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