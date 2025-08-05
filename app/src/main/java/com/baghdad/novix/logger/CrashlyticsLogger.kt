package com.baghdad.novix.logger

import com.baghdad.repository.logger.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class CrashlyticsLogger @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : Logger {
    override fun logException(e: Exception) {
        crashlytics.recordException(e)
    }
}