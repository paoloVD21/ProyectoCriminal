package com.ampn.proyectocriminal.datos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ampn.proyectocriminal.models.Crimen

@Database(entities = [Crimen::class], version = 2)
@TypeConverters(CrimenTypeConverter::class)
abstract class CrimenDatabase: RoomDatabase() {
    abstract fun crimenDao(): CrimenDAO
}

val migration_1_2 = object: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE crimen ADD COLUMN sospechoso TEXT NOT NULL DEFAULT ''"
        )
    }
}