package ru.nn.tripnn.ui.screen.main.photos

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceService
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.state.ResFlow

@HiltViewModel(assistedFactory = PhotosViewModel.Factory::class)
class PhotosViewModel @AssistedInject constructor(
    private val searchPlaceService: SearchPlaceService,
    @Assisted placeId: String
) : ViewModel() {

    private val _photos = ResFlow(scope = viewModelScope) {
        searchPlaceService.findById(placeId).map { it.getOrThrow().photos }.toResultFlow()
    }

    val photos @Composable get() = _photos.state

    @AssistedFactory
    interface Factory {
        fun create(placeId: String): PhotosViewModel
    }
}