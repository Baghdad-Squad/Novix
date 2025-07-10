package com.baghdad.local_datasource.roomDB.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> = value.split(",")
    @TypeConverter
    fun listToString(list: List<String>): String = list.joinToString(",")
}