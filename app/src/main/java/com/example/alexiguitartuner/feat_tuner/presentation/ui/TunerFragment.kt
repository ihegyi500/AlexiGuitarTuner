package com.example.alexiguitartuner.feat_tuner.presentation.ui

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.databinding.FragmentTunerBinding
import com.example.alexiguitartuner.feat_tuner.presentation.viewmodel.TunerViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TunerFragment : Fragment() {

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.RECORD_AUDIO
    }

    private val tunerViewModel : TunerViewModel by viewModels()

    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private var _binding: FragmentTunerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTunerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                renderFragment()
            }
            else {
                Snackbar.make(
                    binding.root,
                    "Permission is not granted!\n" +
                            "If You want to use the tuner function in the future,\n " +
                            "please grant record audio permission for the app in the settings of the mobile!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionLauncher.launch(REQUIRED_PERMISSION)
    }

    private fun renderFragment() {
        tunerViewModel.startAudioProcessing()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    tunerViewModel.detectedPitch.collectLatest { pitch ->
                        binding.tvPitch.text = pitch.name
                        binding.tvFrequency.text = pitch.frequency.toString()
                    }
                }
                launch {
                    tunerViewModel.initPitchButtons()
                    tunerViewModel.pitchButtonsUIState.collectLatest { state ->
                        binding.tvTuning.text = state.tuningName
                        binding.llButtons.removeAllViews()
                        val buttonLayoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                        )
                        for (i in state.pitchList) {
                            val button = Button(requireContext())
                                .apply {
                                    text = i.name.dropLast(1)
                                    setOnClickListener {
                                        tunerViewModel.startPitchGeneration(i.frequency)
                                    }
                                    layoutParams = buttonLayoutParams
                                }
                            binding.llButtons.addView(button)
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                tunerViewModel.stopAudioProcessing()
                tunerViewModel.stopPitchGeneration()
            }
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                _binding = null
            }
        })
    }
}