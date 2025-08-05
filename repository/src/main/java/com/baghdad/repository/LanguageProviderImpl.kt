package com.baghdad.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.baghdad.domain.repository.LanguageProviderRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class LanguageProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LanguageProviderRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override suspend fun getLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, Locale.getDefault().language)
            ?: Locale.getDefault().language
    }

    override suspend fun setLanguage(languageCode: String) {
        withContext(Dispatchers.Main) {
            sharedPreferences.edit {
                putString(KEY_LANGUAGE, languageCode)
            }
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
        }
    }

    companion object {
        private const val PREF_NAME = "settings"
        private const val KEY_LANGUAGE = "language_code"
    }
}