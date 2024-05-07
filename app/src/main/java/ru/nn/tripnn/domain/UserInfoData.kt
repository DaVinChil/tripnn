package ru.nn.tripnn.domain

import android.graphics.Bitmap

data class UserInfoData(
    val name: String,
    val email: String,
    val avatar: Bitmap?
)