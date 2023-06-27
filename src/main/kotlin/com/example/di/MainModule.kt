package com.example.di

import com.example.authentication.JwtTokenManager
import com.example.authentication.TokenManager
import com.example.data.repository.user.UserRepository
import com.example.data.repository.user.UserRepositoryImpl
import com.example.email.EmailManager
import com.example.email.SimpleEmailManager
import com.example.service.UserService
import org.koin.dsl.module

val mainModule = module {
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }

    // Services
    single { UserService(get(), get(), get()) }

    // Other
    single<TokenManager> { JwtTokenManager() }
    single<EmailManager> { SimpleEmailManager() }
}