package com.ampn.proyectocriminal.control

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.ampn.proyectocriminal.databinding.FragmentCrimenBinding
import com.ampn.proyectocriminal.datos.CrimenViewModelFactory
import com.ampn.proyectocriminal.models.Crimen
import com.ampn.proyectocriminal.datos.CrimenViewModel
import kotlinx.coroutines.launch
import java.util.UUID


private const val TAG = "registroCriminal"
class CrimenFragment : Fragment() {
    private lateinit var crimen: Crimen
    // realmente obtiene la referencia a la vista
    private var _binding: FragmentCrimenBinding?=null
    private val binding
        get() = checkNotNull(_binding){

        }

    private val args: CrimenFragmentArgs by navArgs()
    private val crimenViewModel: CrimenViewModel by viewModels(){
        CrimenViewModelFactory(args.crimenId)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Se crea la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimenBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Después de la creación de la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Para todo lo que va dentro de aca se llamara al titulo
        binding.apply {
            // aca se actualizara con el texto que se escribira
            // doOnTextChanged -> es una funcion lambda en la cajita de texto, evento que se maneja con interfaces(implementa una funcional)
            // recibe un texto cuando el interfaz esta corriendo
            // texto, _, _, _ (son atributos, datos de entrada)
            txtTituloCrimen.doOnTextChanged { texto, _, _, _ ->
                crimenViewModel.actualizarCrimen{ anterior ->
                    anterior.copy(titulo=texto.toString())
                }
            }

            btnFechaCrimen.apply {
                // text=crimen.fecha.toString()
                isEnabled=false
            }

            chkCrimenResuelto.setOnCheckedChangeListener { _, seleccionado ->
                crimenViewModel.actualizarCrimen { anterior ->
                    anterior.copy(resuelto = seleccionado)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                crimenViewModel.crimen.collect{ crimen ->
                    crimen?.let {actualizarUI(crimen)}
                }
            }
        }
    }

    private fun actualizarUI(crimen: Crimen){
        binding.apply {
            if(txtTituloCrimen.text.toString()!=crimen.titulo){
                txtTituloCrimen.setText(crimen.titulo)
            }
            btnFechaCrimen.text=crimen.fecha.toString()
            chkCrimenResuelto.isChecked=crimen.resuelto
        }
    }

    // se utiliza para eliminar el fragmento para que al inicializar se crea uno nuevo
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}