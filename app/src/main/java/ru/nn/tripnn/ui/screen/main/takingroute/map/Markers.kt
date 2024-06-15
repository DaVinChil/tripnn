package ru.nn.tripnn.ui.screen.main.takingroute.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState
import ru.nn.tripnn.domain.CurrentRoute
import kotlin.math.max

@Composable
@GoogleMapComposable
fun PlaceMarkers(currentRoute: CurrentRoute) {
    for (i in (max(0, currentRoute.currentPlaceIndex - 1)) until (currentRoute.places.size)) {
        val place = currentRoute.places[i]
        Marker(state = MarkerState(position = LatLng(place.lat!!, place.lon!!)))
    }
}