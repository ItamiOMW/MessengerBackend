package com.example.di

import com.example.authentication.JwtTokenManager
import com.example.authentication.TokenManager
import com.example.data.repository.block.BlockRepository
import com.example.data.repository.block.BlockRepositoryImpl
import com.example.data.repository.chat.ChatRepository
import com.example.data.repository.chat.ChatRepositoryImpl
import com.example.data.repository.contact.ContactRepository
import com.example.data.repository.contact.ContactRepositoryImpl
import com.example.data.repository.user.UserRepository
import com.example.data.repository.user.UserRepositoryImpl
import com.example.email.EmailManager
import com.example.email.SimpleEmailManager
import com.example.service.AuthService
import com.example.service.BlockService
import com.example.service.ContactService
import com.example.service.UserService
import com.example.service.chat.ChatService
import com.google.cloud.storage.Bucket
import com.google.firebase.cloud.StorageClient
import com.google.gson.Gson
import org.koin.dsl.module

val mainModule = module {
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }
    single<ContactRepository> { ContactRepositoryImpl() }
    single<BlockRepository> { BlockRepositoryImpl() }
    single<ChatRepository> { ChatRepositoryImpl() }

    // Services
    single { AuthService(get(), get(), get()) }
    single { ContactService(get(), get()) }
    single { UserService(get(), get(), get()) }
    single { BlockService(get()) }
    single { ChatService(get(), get(), get(), get(), get()) }

    // Other
    single { Gson() }
    single<TokenManager> { JwtTokenManager() }
    single<EmailManager> { SimpleEmailManager() }
    single<Bucket> { StorageClient.getInstance().bucket() }
}