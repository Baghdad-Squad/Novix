package com.baghdad.local_datasource.roomDB.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringToListString(value: String): List<String> = value.split(",")

    @TypeConverter
    fun StringlistToString(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun fromStringToListLong(value: String): List<Long> =
        value.takeIf { it.isNotBlank() }?.split(",")?.map {
            it.toLong()
        } ?: emptyList()


    @TypeConverter
    fun longListToString(list: List<Long>): String = list.joinToString(",")
}