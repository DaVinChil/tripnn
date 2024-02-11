package ru.nn.tripnn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.repository.FakeScreenDataRepositoryImpl
import ru.nn.tripnn.data.repository.FakeUserRepositoryImpl
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.repository.UserRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    @Fake
    abstract fun bindScreenDataRepository(
        screenDataRepository: FakeScreenDataRepositoryImpl
    ): ScreenDataRepository

    @Binds
    @Singleton
    @Fake
    abstract fun bindUserRepository(
        userRepositoryImpl: FakeUserRepositoryImpl
    ): UserRepository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Fake