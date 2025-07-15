package com.baghdad.local_datasource.language

import com.baghdad.repository.language.LanguageProvider
import java.util.Locale

class AppLanguageProvider(
) : LanguageProvider {
    override fun getCurrentLanguage(): String {
        return Locale.getDefault().toLanguageTag()
    }
}