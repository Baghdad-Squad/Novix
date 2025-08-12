package com.baghdad.localDatasource.roomDB.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringToListLong(value: String): List<Long> =
        value.takeIf { it.isNotBlank() }?.split(",")?.map { it.toLong() } ?: emptyList()


    @TypeConverter
    fun longListToString(list: List<Long>): String = list.joinToString(",")
}