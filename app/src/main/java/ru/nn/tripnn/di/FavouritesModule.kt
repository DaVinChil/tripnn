package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.database.place.favourite.FavouritePlacesDao
import ru.nn.tripnn.data.database.place.favourite.FavouritePlacesDatabase
import ru.nn.tripnn.data.database.route.favourite.FavouriteRoutesDao
import ru.nn.tripnn.data.database.route.favourite.FavouriteRoutesDatabase
import ru.nn.tripnn.data.database.route.localroute.LocalRouteDao
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSourceImpl
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.data.repository.favourite.FavouritesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavouritesModule {

    // Places

    @Provides
    @Singleton
    fun favouritePlacesDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, FavouritePlacesDatabase::class.java, "favourite_places.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun favouritePlacesDao(favouritePlacesDatabase: FavouritePlacesDatabase) =
        favouritePlacesDatabase.favouritePlacesDao()

    // Routes

    @Provides
    @Singleton
    fun favouriteRoutesDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, FavouriteRoutesDatabase::class.java, "favourite_routes.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun favouriteRoutesDao(favouriteRoutesDatabase: FavouriteRoutesDatabase) =
        favouriteRoutesDatabase.favouriteRoutesDao()

    // Data Source

    @Provides
    @Singleton
    fun favouritesDataSource(
        favouritePlacesDao: FavouritePlacesDao,
        favouriteRoutesDao: FavouriteRoutesDao,
        localRouteDao: LocalRouteDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): FavouritesDataSource = FavouritesDataSourceImpl(favouritePlacesDao, favouriteRoutesDao, localRouteDao, ioDispatcher)

    // Repository

    @Provides
    @Singleton
    fun favouritesRepository(
        favouritesDataSource: FavouritesDataSource,
        placeDataAggregator: PlaceDataAggregator,
        routeDataAggregator: RouteDataAggregator
    ): FavouritesRepository = FavouritesRepositoryImpl(favouritesDataSource, placeDataAggregator, routeDataAggregator)
}