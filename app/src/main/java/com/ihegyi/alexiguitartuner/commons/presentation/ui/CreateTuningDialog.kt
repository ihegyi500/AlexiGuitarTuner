package com.ihegyi.alexiguitartuner.commons.presentation.ui

import android.R
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
import com.ihegyi.alexiguitartuner.commons.presentation.adapter.PitchListAdapter
import com.ihegyi.alexiguitartuner.commons.presentation.viewmodel.CreateTuningViewModel
import com.ihegyi.alexiguitartuner.databinding.DialogCreateTuningBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreateTuningDialog : DialogFragment() {

    private val viewModel: CreateTuningViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: DialogCreateTuningBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogCreateTuningBinding.inflate(layoutInflater)

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

        lateinit var adapterOfInstruments: ArrayAdapter<String>

        binding.actInstrument.setOnItemClickListener { _, _, pos, _ ->
            viewModel.changeSelectedInstrument(pos)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    adapterOfInstruments = ArrayAdapter(
                        this@CreateTuningDialog.requireContext(),
                        R.layout.simple_spinner_item,
                        ArrayList<String>()
                    )
                    binding.actInstrument.setAdapter(adapterOfInstruments)
                    viewModel.uiState.collectLatest { uiState ->
                        adapterOfInstruments.clear()
                        adapterOfInstruments.addAll(uiState.instrumentList.map { it.name })
                        adapterOfInstruments.notifyDataSetChanged()
                        if (uiState.instrumentList.isNotEmpty()) {
                            binding.actInstrument.setText(uiState.selectedInstrument?.name, false)
                        }
                    }
                }
                launch {
                    val pitchListAdapter = PitchListAdapter(viewModel,lifecycleScope)
                    binding.pitchListRecyclerView.adapter = pitchListAdapter
                    viewModel.uiState.collectLatest {
                        pitchListAdapter.submitList(it.pitchesOfTuning)
                    }
                }
            }
        }

        binding.btnOk.setOnClickListener {
            viewModel.insertTuning(binding.mtwTuningName.text.toString())
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
