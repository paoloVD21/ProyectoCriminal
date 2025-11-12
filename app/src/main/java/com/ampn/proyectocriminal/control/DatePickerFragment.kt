package com.ampn.proyectocriminal.control

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    // Recibe el argumento (la fecha) de forma segura usando navArgs.
    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Listener que se ejecuta cuando el usuario selecciona una fecha y pulsa "OK".
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            // 1. Se crea un objeto Date a partir de la selección del usuario.
            val fechaSeleccionada = GregorianCalendar(year, month, dayOfMonth).time

            // 2. Se envía el resultado de vuelta al fragmento anterior.
            setFragmentResult(
                CLAVE_FECHA_SOLICITADA, // La "pregunta" que se está respondiendo.
                bundleOf(CLAVE_FECHA_SELECCIONADA to fechaSeleccionada) // El "paquete" con la respuesta.
            )
        }

        // Se usa la fecha recibida para inicializar el diálogo.
        val calendario = Calendar.getInstance()
        calendario.time = args.fechaCrimen
        val anioInicial = calendario.get(Calendar.YEAR)
        val mesInicial = calendario.get(Calendar.MONTH)
        val diaInicial = calendario.get(Calendar.DAY_OF_MONTH)

        // Se crea y devuelve el diálogo de selección de fecha.
        return DatePickerDialog(
            requireContext(),
            dateListener,
            anioInicial,
            mesInicial,
            diaInicial
        )
    }

    // Companion object para definir las claves que se usan en la comunicación entre fragmentos.
    companion object {
        // La clave para la "pregunta" (¿Qué fecha se ha solicitado?).
        const val CLAVE_FECHA_SOLICITADA = "Fecha solicitada"
        // La clave para la "respuesta" (La fecha que se ha seleccionado).
        const val CLAVE_FECHA_SELECCIONADA = "Fecha seleccionada"
    }
}