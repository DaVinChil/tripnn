package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.database.route.localroute.LocalRouteDao
import ru.nn.tripnn.data.database.route.localroute.LocalRouteDatabase
import ru.nn.tripnn.data.datasource.localroute.LocalRouteDataSource
import ru.nn.tripnn.data.datasource.localroute.LocalRouteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalRouteModule {
    @Provides
    @Singleton
    fun localRouteDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, LocalRouteDatabase::class.java, "local_route.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun localRouteDao(localRouteDatabase: LocalRouteDatabase) =
        localRouteDatabase.localRouteDao()

    @Provides
    @Singleton
    fun localRouteDataSource(
        localRouteDao: LocalRouteDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): LocalRouteDataSource = LocalRouteDataSourceImpl(localRouteDao, ioDispatcher)
}