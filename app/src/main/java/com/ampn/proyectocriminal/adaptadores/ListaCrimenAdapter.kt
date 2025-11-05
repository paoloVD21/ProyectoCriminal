package com.ampn.proyectocriminal.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ampn.proyectocriminal.databinding.ItemListaCrimenBinding
import com.ampn.proyectocriminal.models.Crimen
import java.util.UUID


class CrimenHolder(
    val binding: ItemListaCrimenBinding
): RecyclerView.ViewHolder(binding.root){
    fun enlazar(crimen: Crimen, onCrimenPulsado: (crimenId: UUID) -> Unit){
        binding.apply {
            tituloCrimen.text = crimen.titulo
            fechaCrimen.text = crimen.fechaFormateada
            // en el root porque se selecciona toda la vista
            root.setOnClickListener {
                onCrimenPulsado(crimen.id)
            }
        }

        binding.imgCrimenResuelto.visibility = if (crimen.resuelto){
            View.VISIBLE
        }else{
            View.GONE
        }
    }
}

class CrimenAdapter(private val crimenes: List<Crimen>, private val onCrimenPulsado: (crimenId: UUID) -> Unit):
    RecyclerView.Adapter<CrimenHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimenHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListaCrimenBinding.inflate(inflater, parent, false)
        return CrimenHolder(binding)
    }
    override fun getItemCount() = crimenes.size
    override fun onBindViewHolder(holder: CrimenHolder, position: Int) {
        val crimen = crimenes[position]
        holder.enlazar(crimen, onCrimenPulsado)
    }
}