package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.language.LanguageProvider

class Config {
    var apiKey: String = ""
    lateinit var languageProvider: LanguageProvider
}
