package com.ampn.proyectocriminal.datos

import android.content.Context
import androidx.room.Room
import com.ampn.proyectocriminal.models.Crimen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "crimen-database"
@OptIn(DelicateCoroutinesApi::class)
class CrimenRepository private constructor(context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope){

    private val database = Room.databaseBuilder(
        context.applicationContext,
        CrimenDatabase::class.java,
        DATABASE_NAME
    ).build()

    fun actualizarCrimen(crimen: Crimen){
        coroutineScope.launch {
            database.crimenDao().actualizarCrimen(crimen)
        }
    }
    fun getCrimenes(): Flow<List<Crimen>> = database.crimenDao().getCrimenes()
    suspend fun getCrimen(id: UUID): Crimen = database.crimenDao().getCrimen(id)
    suspend fun ingresarCrimen(crimen: Crimen) {
        database.crimenDao().ingresarCrimen(crimen)
    }

    // INICIO CAMBIO: Se añade la función que faltaba.
    suspend fun eliminarCrimen(crimen: Crimen) {
        database.crimenDao().eliminarCrimen(crimen)
    }
    // FIN CAMBIO

    companion object{
        private var INSTANCE: CrimenRepository? = null
        fun inicializar(context: Context){
            if(INSTANCE == null){
                INSTANCE = CrimenRepository(context)
            }
        }

        fun get(): CrimenRepository{
            return INSTANCE ?:
            throw IllegalStateException("Debe inicializar el repositorio")
        }
    }
}