package com.ampn.proyectocriminal.control

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.ampn.proyectocriminal.databinding.FragmentCrimenBinding
import com.ampn.proyectocriminal.models.Crimen
import java.util.Date
import java.util.UUID

class CrimenFragment : Fragment() {
    private lateinit var crimen: Crimen
    // realmente obtiene la referencia a la vista
    private var _binding: FragmentCrimenBinding?=null
    private val binding
        get() = checkNotNull(_binding){

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crimen = Crimen(id = UUID.randomUUID(), titulo = "", fecha = Date(), resuelto = false)
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
                // Crimen como es un data class, tiene acceso al copy y se reemplaza el titulo de crimen
                crimen=crimen.copy(titulo = texto.toString())
            }

            btnFechaCrimen.apply {
                text=crimen.fecha.toString()
                isEnabled=false
            }

            chkCrimenResuelto.setOnCheckedChangeListener { _, seleccionado ->
                crimen=crimen.copy(resuelto = seleccionado)
            }
        }
    }

    // se utiliza para eliminar el fragmento para que al inicializar se crea uno nuevo
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}