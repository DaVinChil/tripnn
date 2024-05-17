package ru.nn.tripnn.ui.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.screen.ResourceState
import java.io.ByteArrayOutputStream

fun Uri?.toBitmap(contentResolver: ContentResolver): Bitmap? =
    this?.let {
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(contentResolver, it)
            ImageDecoder.decodeBitmap(source)
        }
    }

fun Bitmap?.toByteArray(): ByteArray? =
    this?.let { bitmap ->
        ByteArrayOutputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.toByteArray()
        }
    }

fun convertImageByteArrayToBitmap(imageData: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
}

fun convertRouteToCurrentRoute(route: Route): CurrentRoute {
    return CurrentRoute(
        places = route.places,
        buildInProgress = false,
        remoteRouteId = route.remoteId
    )
}

fun <T> Flow<Result<T>>.toResourceStateFlow(
    viewModelScope: CoroutineScope,
    initialValue: ResourceState<T> = ResourceState.loading(),
): StateFlow<ResourceState<T>> {
    return map { r -> r.toResource() }.catch { e ->
        ResourceState<T>(
            state = null,
            isError = true,
            isLoading = false,
            error = e
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialValue
        )
}

fun <T> Result<T>.toResource(): ResourceState<T> = when {
    isSuccess -> {
        ResourceState(
            state = getOrNull(),
            isError = false,
            error = null,
            isLoading = false
        )
    }

    isFailure -> {
        ResourceState(
            state = null,
            isError = true,
            error = exceptionOrNull(),
            isLoading = false
        )
    }

    else -> ResourceState.loading()
}
