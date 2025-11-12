package com.ampn.proyectocriminal.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ampn.proyectocriminal.databinding.FragmentCrimenBinding
import com.ampn.proyectocriminal.datos.CrimenViewModel
import com.ampn.proyectocriminal.datos.CrimenViewModelFactory
import com.ampn.proyectocriminal.models.Crimen
import kotlinx.coroutines.launch
import java.util.Date

class CrimenFragment : Fragment() {
    private var _binding: FragmentCrimenBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "No se puede acceder al binding porque es nulo. ¿La vista está creada?"
        }

    private val args: CrimenFragmentArgs by navArgs()
    private val crimenViewModel: CrimenViewModel by viewModels() {
        CrimenViewModelFactory(args.crimenId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.txtTituloCrimen.text.toString().isBlank()) {
                    Toast.makeText(requireContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.apply {
            txtTituloCrimen.doOnTextChanged { texto, _, _, _ ->
                crimenViewModel.actualizarCrimen { anterior ->
                    anterior.copy(titulo = texto.toString())
                }
            }

            // Se quita la línea 'isEnabled = false' para que el botón esté activo.
            // El listener se configura aquí una sola vez, que es más eficiente.
            btnFechaCrimen.setOnClickListener {
                crimenViewModel.crimen.value?.let { crimen ->
                    findNavController().navigate(
                        CrimenFragmentDirections.mostrarDate(crimen.fecha)
                    )
                }
            }

            chkCrimenResuelto.setOnCheckedChangeListener { _, seleccionado ->
                crimenViewModel.actualizarCrimen { anterior ->
                    anterior.copy(resuelto = seleccionado)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimenViewModel.crimen.collect { crimen ->
                    crimen?.let { actualizarUI(it) }
                }
            }
        }

        setFragmentResultListener(DatePickerFragment.CLAVE_FECHA_SOLICITADA) { _, bundle ->
            val nuevaFecha = bundle.getSerializable(DatePickerFragment.CLAVE_FECHA_SELECCIONADA, Date::class.java)
            nuevaFecha?.let { date ->
                crimenViewModel.actualizarCrimen { it.copy(fecha = date) }
            }
        }
    }

    private fun actualizarUI(crimen: Crimen) {
        binding.apply {
            if (txtTituloCrimen.text.toString() != crimen.titulo) {
                txtTituloCrimen.setText(crimen.titulo)
            }
            btnFechaCrimen.text = crimen.fechaFormateada
            chkCrimenResuelto.isChecked = crimen.resuelto
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}