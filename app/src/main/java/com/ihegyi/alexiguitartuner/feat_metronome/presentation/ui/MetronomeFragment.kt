package com.ihegyi.alexiguitartuner.feat_metronome.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ihegyi.alexiguitartuner.MainActivity
import com.ihegyi.alexiguitartuner.R
import com.ihegyi.alexiguitartuner.databinding.FragmentMetronomeBinding
import com.ihegyi.alexiguitartuner.feat_metronome.domain.Rhythm
import com.ihegyi.alexiguitartuner.feat_metronome.presentation.viewmodel.MetronomeViewModel
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
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                metronomeViewModel.getMetronomeState().collectLatest {
                    binding.rhythmView.setRhythmViewState(it)
                    binding.rhythmView.invalidate()
                    binding.tvBPMData.text = it.bpm.toString()
                    binding.sbBPM.progress = it.bpm
                    noteForegroundImageSelector(it.rhythm)
                }
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
                metronomeViewModel.setBPM(progress)
                binding.tvBPMData.text = progress.toString()
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
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                (activity as MainActivity).setBottomNav()
            }
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                _binding = null
            }
        })
    }
    private fun noteForegroundImageSelector(rhythm: Rhythm) {
        when (rhythm) {
            Rhythm.QUARTER -> binding.ivNoteForeground.setImageResource(R.drawable.ic_quarter_note)
            Rhythm.EIGHTH -> binding.ivNoteForeground.setImageResource(R.drawable.ic_eighth_note)
            Rhythm.SIXTEENTH -> binding.ivNoteForeground.setImageResource(R.drawable.ic_sixteenth_note)
        }
    }
}