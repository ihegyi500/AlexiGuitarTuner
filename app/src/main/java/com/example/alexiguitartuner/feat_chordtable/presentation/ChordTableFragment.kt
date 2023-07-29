package com.example.alexiguitartuner.feat_chordtable.presentation

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.databinding.FragmentChordTableBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChordTableFragment : Fragment() {

    private val viewModel : ChordTableViewModel by viewModels()

    private var _binding: FragmentChordTableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChordTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderFragment()
    }

    private fun renderFragment() {

        val adapterOfInstruments = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())
        val adapterOfTunings = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())
        val adapterOfChords = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())

        binding.actInstrument.apply {
            setAdapter(adapterOfInstruments)
            setOnItemClickListener { _, _, pos, _ ->
                /*viewModel.updateTuningListByInstrument(
                    adapterOfInstruments.getItem(pos)!!
                )*/
            }
        }

        binding.actTuning.apply {
            setAdapter(adapterOfTunings)
            setOnItemClickListener { _, _, pos, _ ->
                /*viewModel.getChordsByTuning(
                    viewModel.chordTableUIState.value.selectedInstrument,
                    adapterOfTunings.getItem(pos)!!
                )*/
            }
        }

        binding.actChord.apply {
            setAdapter(adapterOfChords)
            setOnItemClickListener { _, _, pos, _ ->
                //viewModel.getTuningsByInstrument(adapterOfInstruments.getItem(pos)!!)
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chordTableUIState.collect {
                    adapterOfInstruments.clear()
                    adapterOfInstruments.addAll(it.listOfInstruments)
                    adapterOfInstruments.notifyDataSetChanged()

                    adapterOfTunings.clear()
                    adapterOfTunings.addAll(it.listOfTunings)
                    adapterOfTunings.notifyDataSetChanged()

                    adapterOfChords.clear()
                    adapterOfChords.addAll(it.listOfChords)
                    adapterOfChords.notifyDataSetChanged()

                    binding.actInstrument.setText(it.selectedInstrument,false)
                    binding.actTuning.setText(it.selectedTuning,false)
                    binding.actChord.setText(it.selectedChord,false)

                    when(it.selectedChord){
                        "" -> {
                            binding.chordTableView.visibility = View.INVISIBLE
                        }
                        else -> {
                            binding.chordTableView.visibility = View.VISIBLE
                        }
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