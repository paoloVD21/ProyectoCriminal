package com.ampn.proyectocriminal.datos

import androidx.room.TypeConverter
import java.util.Date

class CrimenTypeConverter {
    @TypeConverter
    fun fromDate(fecha: Date):Long{
        return fecha.time
    }

    @TypeConverter
    fun toDate(miliseg:Long):Date{
        return Date(miliseg)
    }
}