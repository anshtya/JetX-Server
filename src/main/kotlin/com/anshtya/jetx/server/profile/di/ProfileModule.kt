package com.anshtya.jetx.server.profile.di

import com.anshtya.jetx.server.profile.ProfileRepository
import com.anshtya.jetx.server.profile.ProfileRepositoryImpl
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ProfileModule {
    @Single
    fun provideProfileRepositoryImpl(): ProfileRepositoryImpl = ProfileRepositoryImpl()

    @Single
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl
}