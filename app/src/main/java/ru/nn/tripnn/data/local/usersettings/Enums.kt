package ru.nn.tripnn.data.local.usersettings

import androidx.annotation.StringRes
import ru.nn.tripnn.R

enum class Theme(@StringRes val resId: Int, val id: Int) {
    LIGHT(R.string.light_theme, 0),
    DARK(R.string.dark_theme, 1),
    SYSTEM(R.string.system_theme, 2),
    MOONLIGHT(R.string.moonlight, 3);

    companion object {
        fun fromId(id: Int) = getEntryById(Theme.entries, Theme::id, id)
    }
}

enum class Language(@StringRes val resId: Int, val id: Int) {
    RUSSIAN(R.string.ru_lang, 0),
    ENGLISH(R.string.en_lang, 1);

    companion object {
        fun fromId(id: Int) = getEntryById(Language.entries, Language::id, id)
    }
}

enum class Currency(@StringRes val resId: Int, val id: Int) {
    RUB(R.string.rub, 0),
    USD(R.string.usd, 1);

    companion object {
        fun fromId(id: Int) = getEntryById(entries, Currency::id, id)
    }
}

fun <T> getEntryById(entries: List<T>, getId: T.() -> Int, id: Int): T {
    for(entry in entries) {
        if(entry.getId() == id) {
            return entry
        }
    }
    return entries.first()
}

fun getIsoLang(language: Language) = when (language) {
    Language.RUSSIAN -> "ru"
    Language.ENGLISH -> "en"
    else -> "en"
}