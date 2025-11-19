package com.ampn.proyectocriminal.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ampn.proyectocriminal.R
import com.ampn.proyectocriminal.adaptadores.CrimenAdapter
import com.ampn.proyectocriminal.databinding.FragmentListaCrimenesBinding
import com.ampn.proyectocriminal.datos.ListaCrimenesViewModel
import com.ampn.proyectocriminal.models.Crimen
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

@Suppress("DEPRECATION")
class ListaCrimenesFragment : Fragment() {
    private val listaCrimenesViewModel: ListaCrimenesViewModel by viewModels()
    private var _binding: FragmentListaCrimenesBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "No se puede acceder al binding porque es nulo. ¿La vista está creada?" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaCrimenesBinding.inflate(inflater, container, false)
        binding.crimenRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                listaCrimenesViewModel.crimenes.collect { crimenes ->
                    // Lógica para mostrar/ocultar la vista vacía.
                    binding.crimenRecyclerView.adapter = CrimenAdapter(crimenes) { crimenId ->
                        findNavController().navigate(ListaCrimenesFragmentDirections.mostrarCrimen(crimenId))
                    }
                    binding.vistaVacia.visibility = if (crimenes.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        // Acción para el nuevo botón para agrega un crimen en lista vacia.
        binding.btnNuevoCrimen.setOnClickListener {
            mostraNuevoCrimen()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragmento_lista_crimenes, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nuevo_crimen -> {
                mostraNuevoCrimen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostraNuevoCrimen() {
        viewLifecycleOwner.lifecycleScope.launch {
            val nuevoCrimen = Crimen(
                id = UUID.randomUUID(),
                titulo = "",
                fecha = Date(),
                resuelto = false,
                sospechoso = ""
            )
            listaCrimenesViewModel.ingresarCrimen(nuevoCrimen)
            findNavController().navigate(
                ListaCrimenesFragmentDirections.mostrarCrimen(nuevoCrimen.id)
            )
        }
    }
}