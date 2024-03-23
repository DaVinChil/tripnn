package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.remote.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.remote.currentroute.CurrentRouteRepositoryImpl
import ru.nn.tripnn.data.remote.history.FakeHistoryRepositoryImpl
import ru.nn.tripnn.data.remote.history.HistoryRepository
import ru.nn.tripnn.data.remote.place.FakePlaceRepositoryImpl
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.remote.route.FakeRouteRepositoryImpl
import ru.nn.tripnn.data.remote.route.RouteRepository
import ru.nn.tripnn.data.remote.userinfo.FakeUserInfoRepositoryImpl
import ru.nn.tripnn.data.remote.userinfo.UserInfoRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    @Fake
    fun userInfoRepository(): UserInfoRepository = FakeUserInfoRepositoryImpl()

    @Provides
    @Singleton
    @Fake
    fun currentRouteRepository(): CurrentRouteRepository = CurrentRouteRepositoryImpl()

    @Provides
    @Singleton
    @Fake
    fun placeRepository(): PlaceRepository = FakePlaceRepositoryImpl()

    @Provides
    @Singleton
    @Fake
    fun routeRepository(): RouteRepository = FakeRouteRepositoryImpl()

    @Provides
    @Singleton
    @Fake
    fun historyRepository(): HistoryRepository = FakeHistoryRepositoryImpl()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Fake