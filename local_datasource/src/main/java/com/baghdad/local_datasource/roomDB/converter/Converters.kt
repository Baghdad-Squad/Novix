package com.baghdad.local_datasource.roomDB.converter

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromString(value: String): List<Long> =
        value.takeIf { it.isNotBlank() }?.split(",")?.map {
            it.toLong()
        } ?: emptyList()


    @TypeConverter
    fun listToString(list: List<Long>): String = list.joinToString(",")
}