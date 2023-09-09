package com.example.alexiguitartuner.commons.presentation.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.alexiguitartuner.commons.presentation.viewmodel.SelectTuningViewModel
import com.example.alexiguitartuner.databinding.DialogSelectTuningBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectTuningDialog : DialogFragment() {

    private val viewModel: SelectTuningViewModel by viewModels(
        ownerProducer = {requireParentFragment()}
    )

    private var _binding: DialogSelectTuningBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogSelectTuningBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lateinit var adapterOfInstruments : ArrayAdapter<String>
        lateinit var adapterOfTunings : ArrayAdapter<String>

        binding.actInstrument.setOnItemClickListener { _, _, pos, _ ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.selectInstrument(viewModel.uiState.value.instrumentList[pos])
                }
            }
        }

        binding.actTuning.setOnItemClickListener { _, _, pos, _ ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.selectTuning(viewModel.uiState.value.tuningList[pos])
                }
            }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.initTuningList()
                }
                launch {
                    adapterOfInstruments = ArrayAdapter(this@SelectTuningDialog.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())
                    binding.actInstrument.setAdapter(adapterOfInstruments)
                    viewModel.uiState.collectLatest { state ->
                        state.instrumentList.also { list ->
                            adapterOfInstruments.clear()
                            adapterOfInstruments.addAll(list.map { it.name })
                            adapterOfInstruments.notifyDataSetChanged()
                        }
                        binding.actInstrument.setText(state.selectedInstrument?.name, false)
                    }
                }
                launch {
                    adapterOfTunings = ArrayAdapter(this@SelectTuningDialog.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())
                    binding.actTuning.setAdapter(adapterOfTunings)
                    viewModel.uiState.collectLatest { state ->
                        state.tuningList.also { list ->
                            adapterOfTunings.clear()
                            adapterOfTunings.addAll(list.map { it.name })
                            adapterOfTunings.notifyDataSetChanged()
                        }
                        binding.actTuning.setText(state.selectedTuning?.name, false)
                    }
                }
            }
        }

        binding.btnOk.setOnClickListener {
            viewModel.updateSelectedTuning()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                _binding = null
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }
}