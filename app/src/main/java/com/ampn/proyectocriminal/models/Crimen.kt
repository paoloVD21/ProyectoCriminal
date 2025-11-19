package com.ampn.proyectocriminal.models

import android.text.format.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crimen (
    @PrimaryKey
    val id: UUID,
    val titulo: String,
    val fecha: Date,
    val resuelto: Boolean,
    val sospechoso: String
) {
    val fechaFormateada: String
        get() = DateFormat.format("EEEE, d 'de' MMMM 'de' yyyy", fecha).toString()
}
