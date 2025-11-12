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

    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val fechaSeleccionada = GregorianCalendar(year, month, dayOfMonth).time
            setFragmentResult(
                CLAVE_FECHA_SOLICITADA,
                bundleOf(CLAVE_FECHA_SELECCIONADA to fechaSeleccionada)
            )
        }

        val calendario = Calendar.getInstance()
        calendario.time = args.fechaCrimen
        val anioInicial = calendario.get(Calendar.YEAR)
        val mesInicial = calendario.get(Calendar.MONTH)
        val diaInicial = calendario.get(Calendar.DAY_OF_MONTH)

        // Se crea el diálogo de selección de fecha.
        val dialogo = DatePickerDialog(
            requireContext(),
            dateListener,
            anioInicial,
            mesInicial,
            diaInicial
        )

        // Desafio 1: Se establece la fecha máxima a "hoy" para evitar fechas futuras.
        dialogo.datePicker.maxDate = System.currentTimeMillis()

        return dialogo
    }

    companion object {
        const val CLAVE_FECHA_SOLICITADA = "Fecha solicitada"
        const val CLAVE_FECHA_SELECCIONADA = "Fecha seleccionada"
    }
}