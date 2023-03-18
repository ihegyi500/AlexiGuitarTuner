package com.example.alexiguitartuner.feat_chordtable.presentation

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
import androidx.lifecycle.lifecycleScope
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.databinding.FragmentChordTableBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
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

        lifecycleScope.launch {
            adapterOfInstruments.addAll(viewModel.getInstrumentNames())
            binding.actInstrument.setText(adapterOfInstruments.getItem(0))
            binding.actInstrument.setAdapter(adapterOfInstruments)

            adapterOfTunings.addAll(
                viewModel.getTuningsByInstrument(
                    binding.actInstrument.text.toString()
                )
            )
            binding.actTuning.setText(adapterOfTunings.getItem(0))
            binding.actTuning.setAdapter(adapterOfTunings)

            adapterOfChords.addAll(
                viewModel.getChordsByTuning(
                    binding.actInstrument.text.toString(),
                    binding.actTuning.text.toString()
                )
            )
            binding.actChord.setText(adapterOfChords.getItem(0))
            binding.actChord.setAdapter(adapterOfChords)
        }

        binding.actInstrument.setOnItemClickListener { _, _, pos, _ ->
            lifecycleScope.launch {
                adapterOfTunings.clear()
                adapterOfChords.clear()

                adapterOfTunings.addAll(
                    viewModel.getTuningsByInstrument(
                        adapterOfInstruments.getItem(pos) as String
                        //binding.actInstrument.text.toString()
                    )
                )
                adapterOfTunings.notifyDataSetChanged()
                binding.actTuning.setText(
                    if(!adapterOfTunings.isEmpty)
                        adapterOfTunings.getItem(0)
                    else ""
                )
                //binding.actTuning.setAdapter(adapterOfTunings)

                /*adapterOfChords.addAll(
                    viewModel.getChordsByTuning(
                        adapterOfInstruments.getItem(pos) as String,
                        if(!adapterOfTunings.isEmpty)
                            adapterOfTunings.getItem(0) as String
                        else ""
                        /*binding.actInstrument.text.toString(),
                        binding.actTuning.text.toString()*/
                    )
                )
                adapterOfChords.notifyDataSetChanged()
                binding.actChord.setText(
                    if(!adapterOfChords.isEmpty)
                        adapterOfChords.getItem(0)
                    else ""
                )*/

                //binding.actChord.setAdapter(adapterOfChords)

            }
        }

        binding.actTuning.setOnItemClickListener { _, _, _, _ ->
            lifecycleScope.launch {
                adapterOfChords.clear()

                adapterOfChords.addAll(
                    viewModel.getChordsByTuning(
                        binding.actInstrument.text.toString(),
                        binding.actTuning.text.toString()
                    )
                )
                binding.actChord.setText(
                    if(!adapterOfChords.isEmpty)
                        adapterOfChords.getItem(0)
                    else ""
                )
                binding.actChord.setAdapter(adapterOfChords)
            }
        }

        binding.actChord.setOnItemClickListener { _, _, _, _ ->

        }
        /**/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}