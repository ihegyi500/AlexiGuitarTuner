package com.example.alexiguitartuner.feat_metronome.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.databinding.FragmentMetronomeBinding
import com.example.alexiguitartuner.feat_metronome.data.MetronomeService
import com.example.alexiguitartuner.feat_metronome.domain.Rhythm
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MetronomeFragment : Fragment() {

    private val metronomeViewModel : MetronomeViewModel by viewModels()

    private var _binding: FragmentMetronomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMetronomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderFragment()
    }

    private fun renderFragment() {

        lifecycleScope.launch {
            metronomeViewModel.bpm.collectLatest {
                binding.tvBPMData.text = it.toString()
            }
        }

        binding.btnStart.setOnClickListener {
            metronomeViewModel.startService(requireContext())
        }

        binding.btnStop.setOnClickListener {
            metronomeViewModel.stopService(requireContext())
        }

        binding.sbBPM.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                metronomeViewModel.setBPM(binding.sbBPM.progress)
                binding.tvBPMData.text = binding.sbBPM.progress.toString()
                binding.rhythmView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.btnPlus.setOnClickListener {
            try {
                metronomeViewModel.insertNote()
                binding.rhythmView.invalidate()
            } catch (e:Exception){
                Snackbar.make(binding.root,e.message.toString(),Snackbar.LENGTH_LONG).show()
            }
        }

        binding.btnMinus.setOnClickListener {
            try {
                metronomeViewModel.removeNote()
                binding.rhythmView.invalidate()
            } catch (e:Exception){
                Snackbar.make(binding.root,e.message.toString(),Snackbar.LENGTH_LONG).show()
            }
        }

        binding.ivNoteForeground.setOnClickListener {
            metronomeViewModel.setRhythm()
            noteForegroundImageSelector()
        }

        noteForegroundImageSelector()
    }

    private fun noteForegroundImageSelector() {
        when (metronomeViewModel.getRhythm()) {
            Rhythm.QUARTER -> binding.ivNoteForeground.setImageResource(R.drawable.ic_quarter_note)
            Rhythm.EIGHTH -> binding.ivNoteForeground.setImageResource(R.drawable.ic_eighth_note)
            Rhythm.SIXTEENTH -> binding.ivNoteForeground.setImageResource(R.drawable.ic_sixteenth_note)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}