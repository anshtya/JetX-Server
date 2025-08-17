package com.anshtya.jetx.server.di

import com.anshtya.jetx.server.profile.di.ProfileModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        ProfileModule::class,
    ]
)
class AppModule