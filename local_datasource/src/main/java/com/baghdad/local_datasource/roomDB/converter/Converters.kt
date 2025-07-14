package com.baghdad.local_datasource.roomDB.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> = value.split(",")

    @TypeConverter
    fun listToString(list: List<String>): String = list.joinToString(",")
}