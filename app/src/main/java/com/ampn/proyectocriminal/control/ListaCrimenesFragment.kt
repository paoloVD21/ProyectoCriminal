package com.ampn.proyectocriminal.control

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ampn.proyectocriminal.R
import com.ampn.proyectocriminal.adaptadores.CrimenAdapter
import com.ampn.proyectocriminal.databinding.FragmentListaCrimenesBinding
import com.ampn.proyectocriminal.datos.ListaCrimenesViewModel

private const val TAG = "ListaCrimenesFragment"

class ListaCrimenesFragment : Fragment() {
    private val listaCrimenesViewModel: ListaCrimenesViewModel by viewModels()
    private var _binding: FragmentListaCrimenesBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total de crimenes: ${listaCrimenesViewModel.crimenes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaCrimenesBinding.inflate(inflater, container, false)
        binding.crimenRecyclerView.layoutManager = LinearLayoutManager(context)
        val crimenes = listaCrimenesViewModel.crimenes
        val adapter = CrimenAdapter(crimenes)
        binding.crimenRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}