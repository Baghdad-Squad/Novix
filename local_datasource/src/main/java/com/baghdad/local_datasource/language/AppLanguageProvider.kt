package com.baghdad.local_datasource.language

import com.baghdad.repository.language.LanguageProvider
import java.util.Locale
import javax.inject.Inject

class AppLanguageProvider @Inject constructor(
) : LanguageProvider {
    override fun getCurrentLanguage(): String {
        return Locale.getDefault().language
    }
}