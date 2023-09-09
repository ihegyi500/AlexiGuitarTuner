package com.example.alexiguitartuner.feat_chordtable.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.alexiguitartuner.databinding.FragmentChordTableBinding
import com.example.alexiguitartuner.feat_chordtable.presentation.viewmodel.ChordTableViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        lateinit var adapterOfInstruments : ArrayAdapter<String>
        lateinit var adapterOfTunings : ArrayAdapter<String>
        lateinit var adapterOfChords : ArrayAdapter<String>

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

        binding.actChord.setOnItemClickListener { _, _, pos, _ ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.selectChord(viewModel.uiState.value.chordList[pos])
                }
            }
        }

        binding.fabIncrementPos.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.increaseChordTablePosition()
                }
            }
        }

        binding.fabDecrementPos.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.decreaseChordTablePosition()
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    adapterOfInstruments = ArrayAdapter(this@ChordTableFragment.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())
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
                    adapterOfTunings = ArrayAdapter(this@ChordTableFragment.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())
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
                launch {
                    adapterOfChords = ArrayAdapter(this@ChordTableFragment.requireContext(), android.R.layout.simple_spinner_item, ArrayList<String>())
                    binding.actChord.setAdapter(adapterOfChords)
                    viewModel.uiState.collectLatest { state ->
                        state.chordList.also { list ->
                            adapterOfChords.clear()
                            adapterOfChords.addAll(list.map { it.name })
                            adapterOfChords.notifyDataSetChanged()
                        }
                        binding.actChord.setText(state.selectedChord?.name, false)
                    }
                }
                launch {
                    viewModel.uiState.collectLatest { state ->
                        state.selectedPosition.also { pos ->
                            binding.tvPositionData.text = pos.plus(1).toString()
                            binding.chordTableView.setPitchList(
                                state.tuningPitches,
                                state.selectedChordTable?.pitchPos ?: emptyList()
                            )
                            binding.chordTableView.invalidate()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                _binding = null
            }
        })
    }
}