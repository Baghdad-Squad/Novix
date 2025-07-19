package com.baghdad.local_datasource.util

fun calculatePageOffset(
    pageSize: Int,
    page: Int
) = pageSize * (page - 1)