package ru.nn.tripnn.ui.screen.main.takingroute.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import ru.nn.tripnn.domain.CurrentRoute

@Composable
fun CurrentRouteMap(
    modifier: Modifier = Modifier,
    currentRoute: CurrentRoute
) {
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 14f)
    }

    LaunchedEffect(key1 = currentRoute.currentPlaceIndex) {
        val curPlace = currentRoute.currentPlace
        cameraPosition.animate(
            CameraUpdateFactory.newLatLng(
                LatLng(curPlace.lat!!, curPlace.lon!!)
            )
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPosition
    ) {
        Path(currentRoute = currentRoute)
        PlaceMarkers(currentRoute = currentRoute)
    }
}