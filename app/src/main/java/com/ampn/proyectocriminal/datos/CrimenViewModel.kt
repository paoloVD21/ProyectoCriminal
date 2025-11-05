package com.ampn.proyectocriminal.datos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ampn.proyectocriminal.models.Crimen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimenViewModel(crimenId: UUID) : ViewModel() {
    private val repositorio = CrimenRepository.get()
    private val _crimen: MutableStateFlow<Crimen?> = MutableStateFlow(null)
    val crimen: StateFlow<Crimen?> = _crimen.asStateFlow()
    init {
        viewModelScope.launch {
            _crimen.value = repositorio.getCrimen(crimenId)
        }
    }

    fun actualizarCrimen (onUpdate: (Crimen) -> Crimen){
        _crimen.update{
                crimenAnterior -> crimenAnterior?.let{ onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
            _crimen.value?.let { repositorio.actualizarCrimen(it) }
    }
}

class CrimenViewModelFactory (
    private val crimenId: UUID
): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimenViewModel(crimenId) as T
    }
}

