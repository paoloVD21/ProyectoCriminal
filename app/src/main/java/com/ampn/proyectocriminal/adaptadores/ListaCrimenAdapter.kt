package com.ampn.proyectocriminal.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ampn.proyectocriminal.databinding.ItemListaCrimenBinding
import com.ampn.proyectocriminal.models.Crimen

class CrimenHolder(
    val binding: ItemListaCrimenBinding
): RecyclerView.ViewHolder(binding.root){
    fun enlazar(crimen: Crimen){
        binding.apply {
            tituloCrimen.text = crimen.titulo
            fechaCrimen.text = crimen.fecha.toString()
            // en el root porque se selecciona toda la vista
            root.setOnClickListener {
                Toast.makeText(root.context, "${crimen.titulo}!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class CrimenAdapter(private val crimenes: List<Crimen>): RecyclerView.Adapter<CrimenHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimenHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListaCrimenBinding.inflate(inflater, parent, false)
        return CrimenHolder(binding)
    }

    override fun getItemCount() = crimenes.size
    override fun onBindViewHolder(holder: CrimenHolder, position: Int) {
        val crimen = crimenes[position]
        holder.enlazar(crimen)
    }
}