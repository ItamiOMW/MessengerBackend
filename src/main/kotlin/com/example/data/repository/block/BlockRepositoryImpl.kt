package com.example.data.repository.block

import com.example.data.database.exposed.DatabaseFactory.dbQuery
import com.example.data.database.exposed.entity.UserEntity
import com.example.data.database.exposed.table.BlockedUsers
import com.example.data.database.exposed.table.Users
import com.example.data.mapper.toUser
import com.example.data.model.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class BlockRepositoryImpl : BlockRepository {

    override suspend fun getBlockedUsers(userId: Int): List<User> {
        return dbQuery {
            val ids = BlockedUsers
                .select { BlockedUsers.userId eq userId }
                .map { it[BlockedUsers.blockedUserId].value }

            UserEntity.find {
                Users.id inList ids
            }.map { it.toUser() }
        }
    }

    override suspend fun blockUser(userId: Int, userIdToBlock: Int) {
        dbQuery {
            BlockedUsers.insert { table ->
                table[this.userId] = userId
                table[this.blockedUserId] = userIdToBlock
            }
        }
    }

    override suspend fun unblockUser(userId: Int, userIdToUnblock: Int) {
        dbQuery {
            BlockedUsers.deleteWhere {
                (BlockedUsers.userId eq userId) and (blockedUserId eq userIdToUnblock)
            }
        }

    }

    override suspend fun isBlocked(userId: Int, blockedByUserId: Int): Boolean {
        return dbQuery {
            BlockedUsers.select {
                (BlockedUsers.userId eq userId) and (BlockedUsers.blockedUserId eq blockedByUserId)
            }.count() > 0
        }

    }


}