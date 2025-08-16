package com.baghdad.ui.feature.authentication

data class PasswordMessages(
    val pageNotFound: String,
    val error: String,
    val success: String,
    val successToast: String,
    val errorToast: String="",
    val mainHeader: String = ""
)