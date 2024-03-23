package ru.nn.tripnn.ui.screen.main.photos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.ui.screen.authentication.ResourceState
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    @Fake val placeRepository: PlaceRepository
) : ViewModel() {

    var photos by mutableStateOf(ResourceState<List<String>>())
        private set

    fun init(placeId: String) {
        loadPhotos(placeId)
    }

    private fun loadPhotos(placeId: String) {
        viewModelScope.launch {
            photos = photos.copy(
                isLoading = true,
                value = null,
                error = null,
                isError = false
            )

            when (val result = placeRepository.findById(placeId)) {
                is RemoteResource.Success -> {
                    photos = photos.copy(
                        value = result.data?.photos,
                        isLoading = false
                    )
                }

                is RemoteResource.Error -> {
                    photos = photos.copy(
                        isError = true,
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}