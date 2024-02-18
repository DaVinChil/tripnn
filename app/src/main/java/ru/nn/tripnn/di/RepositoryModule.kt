package ru.nn.tripnn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.remote.repository.FakeHistoryRepositoryImpl
import ru.nn.tripnn.data.remote.repository.FakePlaceRepositoryImpl
import ru.nn.tripnn.data.remote.repository.FakeRouteRepositoryImpl
import ru.nn.tripnn.data.remote.repository.FakeScreenDataRepositoryImpl
import ru.nn.tripnn.data.remote.repository.FakeUserRepositoryImpl
import ru.nn.tripnn.domain.repository.HistoryRepository
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
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
        userRepository: FakeUserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    @Fake
    abstract fun bindPlaceRepository(
        placeRepository: FakePlaceRepositoryImpl
    ): PlaceRepository

    @Binds
    @Singleton
    @Fake
    abstract fun bindRouteRepository(
        routeRepository: FakeRouteRepositoryImpl
    ): RouteRepository

    @Binds
    @Singleton
    @Fake
    abstract fun bindHistoryRepository(
        historyRepository: FakeHistoryRepositoryImpl
    ): HistoryRepository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Fake