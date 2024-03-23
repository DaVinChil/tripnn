package ru.nn.tripnn.domain.model

import android.graphics.Bitmap

data class UserInfo(
    val name: String,
    val email: String,
    val avatar: Bitmap?
)