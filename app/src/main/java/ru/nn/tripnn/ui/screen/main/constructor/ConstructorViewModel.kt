package ru.nn.tripnn.ui.screen.main.constructor

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ConstructorViewModel @Inject constructor(

) : ViewModel() {
    var restriction: Restriction? = null


}

data class Restriction(
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.MIDNIGHT,
    val endTime: LocalTime = LocalTime.MIDNIGHT,
    val maxDistance: Int = 0,
    val considerCurrentLocation: Boolean = false
)