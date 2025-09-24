package com.ampn.proyectocriminal.datos

import androidx.lifecycle.ViewModel
import com.ampn.proyectocriminal.models.Crimen
import java.util.Date
import java.util.UUID

class ListaCrimenesViewModel: ViewModel() {
    // Crea una nueva lista mutable
    // Donde se le pueden agregar, quitar o modificar elementos
    val crimenes = mutableListOf<Crimen>()
    
    
    init {
        for (i in 0 until 100){
            val crimen = Crimen(
                id = UUID.randomUUID(),
                titulo = "Crimen $i",
                fecha = Date(),
                resuelto = i % 2 == 0
            )
            crimenes.add(crimen)
        }
    }

}