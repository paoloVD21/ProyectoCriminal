package com.ampn.proyectocriminal.control


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ampn.proyectocriminal.adaptadores.CrimenAdapter
import com.ampn.proyectocriminal.databinding.FragmentListaCrimenesBinding
import com.ampn.proyectocriminal.datos.ListaCrimenesViewModel
import kotlinx.coroutines.launch

class ListaCrimenesFragment : Fragment() {
    private val listaCrimenesViewModel: ListaCrimenesViewModel by viewModels()
    private var _binding: FragmentListaCrimenesBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "No se puede acceder al binding porque es nulo. ¿La vista está creada?" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaCrimenesBinding.inflate(inflater, container, false)
        binding.crimenRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                listaCrimenesViewModel.crimenes.collect { crimenes ->
                    binding.crimenRecyclerView.adapter = CrimenAdapter(crimenes){ crimenId ->
                        findNavController().navigate(ListaCrimenesFragmentDirections.mostrarCrimen(crimenId))
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}