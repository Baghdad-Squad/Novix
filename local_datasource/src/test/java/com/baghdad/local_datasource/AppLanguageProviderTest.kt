package com.baghdad.local_datasource

import com.baghdad.local_datasource.language.AppLanguageProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Locale

class AppLanguageProviderTest {
    @Test
    fun `getCurrentLanguage should return correct system language`() {
        val originalLocale = Locale.getDefault()
        Locale.setDefault(Locale("fr"))

        val provider = AppLanguageProvider()
        val language = provider.getCurrentLanguage()

        Assertions.assertEquals("fr", language)

        Locale.setDefault(originalLocale)
    }

}