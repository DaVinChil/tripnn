package ru.nn.tripnn.ui.screen.main.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceService
import ru.nn.tripnn.ui.util.toResourceStateFlow

@HiltViewModel(assistedFactory = PhotosViewModel.Factory::class)
class PhotosViewModel @AssistedInject constructor(
    private val searchPlaceService: SearchPlaceService,
    @Assisted placeId: String
) : ViewModel() {

    val photos = searchPlaceService.findById(placeId)
        .map { Result.success(it.getOrThrow().photos) }
        .toResourceStateFlow(viewModelScope)

    @AssistedFactory
    interface Factory {
        fun create(placeId: String): PhotosViewModel
    }
}