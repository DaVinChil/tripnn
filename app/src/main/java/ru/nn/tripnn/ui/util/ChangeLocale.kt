package ru.nn.tripnn.ui.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import ru.nn.tripnn.data.database.usersettings.Language

fun changeLocales(context: Context, localeString: String) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(localeString)
    }
}

fun getIsoLang(language: Language) = when (language) {
    Language.RUSSIAN -> "ru"
    Language.ENGLISH -> "en"
}