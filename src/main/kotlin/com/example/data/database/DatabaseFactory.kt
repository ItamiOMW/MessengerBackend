package com.example.data.database

import com.example.data.database.table.ContactRequests
import com.example.data.database.table.Contacts
import com.example.util.Constants
import com.example.data.database.table.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(ContactRequests)
            SchemaUtils.create(Contacts)
        }
    }


    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv(Constants.JDBC_DRIVER_KEY)
        config.jdbcUrl = System.getenv(Constants.DATABASE_URL_KEY)
        config.username = System.getenv(Constants.DATABASE_USERNAME_KEY)
        config.password = System.getenv(Constants.DATABASE_PASSWORD_KEY)
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = System.getenv(Constants.TRANSACTION_ISOLATION_KEY)
        config.validate()

        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

    suspend fun drop() {
        dbQuery { SchemaUtils.drop(Users) }
    }

}