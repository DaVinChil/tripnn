package ru.nn.tripnn.ui.screen.main.takingroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
@GoogleMapComposable
fun Path(currentRoute: CurrentRoute) {
    var directionResult by remember { mutableStateOf<DirectionsResult?>(null) }

    LaunchedEffect(currentRoute.places) {
        withContext(Dispatchers.IO) {
            directionResult = getDirections(currentRoute.places, maxOf(currentRoute.currentPlaceIndex - 1, 0))
        }
    }

    if (directionResult != null) {
        for (route in directionResult!!.routes) {
            for (leg in route.legs) {
                for (step in leg.steps) {
                    val polyline = PolylineOptions().apply {
                        step.polyline.decodePath().forEach { latLng ->
                            add(LatLng(latLng.lat, latLng.lng))
                        }
                    }
                    Polyline(
                        points = polyline.points,
                        color = TripNnTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

suspend fun getDirections(
    places: List<Place>,
    from: Int
): DirectionsResult {
    val geoApiContext = GeoApiContext.Builder()
        .apiKey("xxx")
        .build()

    return DirectionsApi
        .newRequest(geoApiContext)
        .mode(TravelMode.WALKING)
        .origin("${places[from].lat!!},${places[from].lon!!}")
        .destination("${places.last().lat!!},${places.last().lon!!}")
        .waypoints(*(places.mapIndexedNotNull { i, place ->
            if (i <= from || i == places.lastIndex) null
            else com.google.maps.model.LatLng(
                place.lat!!,
                place.lon!!
            )
        }
            .toTypedArray()))
        .await()
}