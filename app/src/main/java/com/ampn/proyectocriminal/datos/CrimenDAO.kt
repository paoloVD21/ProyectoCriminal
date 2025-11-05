package com.ampn.proyectocriminal.datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ampn.proyectocriminal.models.Crimen
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CrimenDAO {
    @Query("SELECT * FROM crimen")
    fun getCrimenes(): Flow<List<Crimen>>

    @Query("SELECT * FROM Crimen WHERE id = (:id)")
    suspend fun getCrimen(id: UUID): Crimen
    @Insert
    suspend fun ingresarCrimen(crimen: Crimen)

    @Update
    suspend fun actualizarCrimen(crimen: Crimen)
}