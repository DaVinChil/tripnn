package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.remote.routebuilder.FakeRouteBuilderServiceImpl
import ru.nn.tripnn.data.remote.routebuilder.RouteBuilderService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Fake
    @Provides
    @Singleton
    fun routeBuilderService(): RouteBuilderService = FakeRouteBuilderServiceImpl()
}