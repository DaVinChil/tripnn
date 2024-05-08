package ru.nn.tripnn.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.screen.ResourceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesColumn(
    onEmpty: @Composable () -> Unit,
    places: ResourceState<List<Place>>,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    if (places.isError) {
        InternetProblemScreen()
        return
    }

    if (places.isLoading) {
        LoadingCircleScreen()
        return
    }

    if (places.value.isNullOrEmpty()) {
        onEmpty()
        return
    }

    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(PLACE_1) }

    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items = places.value, key = Place::id) { place ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromFavouriteRedCardOption(
                        onClick = { removeFromFavourite(place.id) })
                }
            DraggableCard(option1 = option) {
                PlaceCard(
                    place = place,
                    onCardClick = {
                        pickedPlace = place
                        showCardInfo = true
                    },
                    shadowColor = Color.Black.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            place = pickedPlace,
            onDismissRequest = { showCardInfo = false },
            sheetState = sheetState,
            removeFromFavourite = { removeFromFavourite(pickedPlace.id) },
            addToFavourite = { addToFavourite(pickedPlace.id) },
            toPhotos = toPhotos
        )
    }
}