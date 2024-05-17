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
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.common.card.PlaceCard
import ru.nn.tripnn.ui.common.card.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.screen.ResourceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesColumn(
    onEmpty: @Composable () -> Unit,
    places: ResourceState<List<Place>>,
    removeFromFavourite: (Place) -> Unit,
    addToFavourite: (Place) -> Unit,
    hideIndication: Boolean = false,
    option2: @Composable ((Place) -> Unit)? = null,
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

    if (places.state.isNullOrEmpty()) {
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
        items(items = places.state, key = Place::id) { place ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromFavouriteGoldCardOption(
                        onClick = { removeFromFavourite(place) })
                }
            PlaceCard(
                modifier = Modifier.fillMaxWidth(),
                place = place,
                onCardClick = {
                    pickedPlace = place
                    showCardInfo = true
                },
                hideIndication = hideIndication,
                option1 = option,
                option2 = if (option2 != null) {
                    @Composable { option2(place) }
                } else {
                    null
                }
            )
        }
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            place = pickedPlace,
            onDismissRequest = { showCardInfo = false },
            sheetState = sheetState,
            removeFromFavourite = { removeFromFavourite(pickedPlace) },
            addToFavourite = { addToFavourite(pickedPlace) },
            toPhotos = toPhotos
        )
    }
}