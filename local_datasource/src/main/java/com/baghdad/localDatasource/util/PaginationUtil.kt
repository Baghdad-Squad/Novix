package com.baghdad.localDatasource.util

fun calculatePageOffset(
    pageSize: Int,
    page: Int
) = pageSize * (page - 1)