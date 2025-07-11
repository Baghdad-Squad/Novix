package com.baghdad.local_datasource.roomDB.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString() // Stores as ISO-8601 string (e.g., "2023-12-31")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }

    }
    @TypeConverter
    fun fromString(value: String): List<String> = value.split(",")
    @TypeConverter
    fun listToString(list: List<String>): String = list.joinToString(",")
}