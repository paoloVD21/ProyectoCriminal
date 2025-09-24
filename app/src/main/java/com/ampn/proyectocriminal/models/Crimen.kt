package com.ampn.proyectocriminal.models

import java.util.UUID
import java.util.Date

data class Crimen (
    val id:UUID, 
    val titulo:String,
    val fecha:Date,
    val resuelto:Boolean
)