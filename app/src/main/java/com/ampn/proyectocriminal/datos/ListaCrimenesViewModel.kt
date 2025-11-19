package com.ampn.proyectocriminal.datos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ampn.proyectocriminal.models.Crimen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class ListaCrimenesViewModel : ViewModel() {

    private val crimenRepository = CrimenRepository.get()

    // Consulta cuando el estado de ese flujo ha cambiado -> StateFlow
    private val _crimenes: MutableStateFlow<List<Crimen>> = MutableStateFlow(emptyList())
    val crimenes: StateFlow<List<Crimen>>
        get() = _crimenes.asStateFlow()

    init {
        viewModelScope.launch {
            for (i in 0 until 3) {
                val crimen = Crimen(
                    id = UUID.randomUUID(),
                    titulo = "Crimen de prueba #${i + 1}",
                    fecha = Date(),
                    resuelto = false,
                    sospechoso = "Hola"
                )
                crimenRepository.ingresarCrimen(crimen)
            }

            crimenRepository.getCrimenes().collect {
                _crimenes.value = it
            }
        }
    }

    suspend fun ingresarCrimen(crimen: Crimen) {
        crimenRepository.ingresarCrimen(crimen)
    }
}