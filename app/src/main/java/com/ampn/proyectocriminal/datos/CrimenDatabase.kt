package com.ampn.proyectocriminal.datos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ampn.proyectocriminal.models.Crimen

@Database(entities = [Crimen::class], version = 1)
@TypeConverters(CrimenTypeConverter::class)
abstract class CrimenDatabase: RoomDatabase() {
    abstract fun crimenDao(): CrimenDAO
}
