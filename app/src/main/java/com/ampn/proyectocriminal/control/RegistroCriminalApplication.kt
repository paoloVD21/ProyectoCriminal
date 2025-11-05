package com.ampn.proyectocriminal.control

import android.app.Application
import com.ampn.proyectocriminal.datos.CrimenRepository

class RegistroCriminalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CrimenRepository.inicializar(this)
    }
}