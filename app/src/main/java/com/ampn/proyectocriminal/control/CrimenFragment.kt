package com.ampn.proyectocriminal.control

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ampn.proyectocriminal.R
import com.ampn.proyectocriminal.databinding.FragmentCrimenBinding
import com.ampn.proyectocriminal.datos.CrimenViewModel
import com.ampn.proyectocriminal.datos.CrimenViewModelFactory
import com.ampn.proyectocriminal.models.Crimen
import kotlinx.coroutines.launch
import java.util.Date

private const val FORMATO_FECHA = "EEE, MMM, dd"
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

    private val selectorSospechos = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let { obtenerContactoSeleccionado(it) }
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

            crimenReporte.setOnClickListener {
                crimenViewModel.crimen.value?.let {
                    val intentReporte=Intent(Intent.ACTION_SEND).apply {
                        type="text/plain"
                        putExtra(Intent.EXTRA_TEXT, getReporteCrimen(it))
                        putExtra(Intent.EXTRA_SUBJECT,getString(R.string.reporte_asunto))
                    }
                    val intentSelector = Intent.createChooser(intentReporte,
                        getString(R.string.enviar_reporte))

                    if (puedeResolverIntent(intentSelector)) {
                        startActivity(intentSelector)
                    } else {
                        Toast.makeText(requireContext(), "No se encontró una aplicación para enviar el reporte", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            crimenSuspect.setOnClickListener {
                selectorSospechos.launch(null)
            }

            val intentSeleccionarSospechoso = selectorSospechos.contract.createIntent(requireContext(), null)
            crimenSuspect.isEnabled = puedeResolverIntent(intentSeleccionarSospechoso)
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

        // Configuración del menú para eliminar.
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_crimen, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.eliminar_crimen -> {
                        crimenViewModel.eliminarCrimen()
                        // Se regresa a la pantalla anterior, como lo haría finish()
                        findNavController().popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun actualizarUI(crimen: Crimen) {
        binding.apply {
            if (txtTituloCrimen.text.toString() != crimen.titulo) {
                txtTituloCrimen.setText(crimen.titulo)
            }
            btnFechaCrimen.text = crimen.fechaFormateada
            chkCrimenResuelto.isChecked = crimen.resuelto
            if (crimen.sospechoso.isNotBlank()){
                crimenSuspect.text = crimen.sospechoso
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getReporteCrimen(crimen: Crimen): String {
        val stringCrimenResuelto = if (crimen.resuelto) {
            getString(R.string.reporte_resuelto)
        } else {
            getString(R.string.reporte_no_resuelto)
        }

        val stringFecha = DateFormat.format(FORMATO_FECHA, crimen.fecha).toString()
        val textoSospechoso = if (crimen.sospechoso.isBlank()){
            getString(R.string.reporte_sin_sospechoso)
        } else {
            getString(R.string.reporte_con_sospechoso, crimen.sospechoso)
        }

        return getString(
            R.string.reporte_Crimen,
            crimen.titulo,
            stringFecha,
            stringCrimenResuelto,
            textoSospechoso
        )
    }

    private fun obtenerContactoSeleccionado(uriContacto: Uri) {
        val campoConsulta = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val cursorConsulta = requireActivity().contentResolver.query(
            uriContacto, campoConsulta, null, null, null
        )
        cursorConsulta?.use { cursor ->
            if (cursor.moveToFirst()) {
                val culpable = cursor.getString(0)
                crimenViewModel.actualizarCrimen { anterior ->
                    anterior.copy(sospechoso = culpable)
                }
            }
        }
    }

    private fun puedeResolverIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolveActivity = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveActivity != null
    }
}