package com.example.alexiguitartuner.feat_tuner.presentation

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.alexiguitartuner.databinding.FragmentTunerBinding
import com.example.alexiguitartuner.feat_tuner.data.TunerDataSource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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

        lifecycleScope.launchWhenStarted {
            tunerViewModel.detectedHz.collectLatest {
                binding.tvFrequency.text = it.toString()
            }
        }

        binding.btnE.setOnClickListener {
            tunerViewModel.startPitchGeneration(82.41)
        }

        binding.btnA.setOnClickListener {
            tunerViewModel.startPitchGeneration(110.00)
        }

        binding.btnD.setOnClickListener {
            tunerViewModel.startPitchGeneration(146.83)
        }

        binding.btnG.setOnClickListener {
            tunerViewModel.startPitchGeneration(196.00)
        }

        binding.btnB.setOnClickListener {
            tunerViewModel.startPitchGeneration(246.94)
        }

        binding.btnE2.setOnClickListener {
            tunerViewModel.startPitchGeneration(329.63)
        }
    }

    override fun onPause() {
        tunerViewModel.stopAudioProcessing()
        tunerViewModel.stopPitchGeneration()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}