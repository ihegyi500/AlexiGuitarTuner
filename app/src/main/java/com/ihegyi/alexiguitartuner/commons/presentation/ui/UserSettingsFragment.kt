package com.ihegyi.alexiguitartuner.commons.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ihegyi.alexiguitartuner.commons.presentation.viewmodel.UserSettingsViewModel
import com.ihegyi.alexiguitartuner.databinding.FragmentUserSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserSettingsFragment : Fragment() {

    private val viewModel: UserSettingsViewModel by viewModels()
    private var _binding: FragmentUserSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderFragment()
    }

    private fun renderFragment() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userSettings.collectLatest {
                    binding.tbHalfNoteDisplay.isChecked = it.useSharp
                    binding.tbNotation.isChecked = it.useEnglish
                }
            }
        }

        binding.tbHalfNoteDisplay.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateUseSharp(isChecked)
        }

        binding.tbNotation.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateUseEnglish(isChecked)
        }

        binding.btnSelectTuning.setOnClickListener {
            SelectTuningDialog().show(childFragmentManager, null)
        }

        binding.btnCreateTuning.setOnClickListener {
            CreateTuningDialog().show(childFragmentManager, null)
        }

        binding.btnDeleteTuning.setOnClickListener {
            DeleteTuningDialog().show(childFragmentManager, null)
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                viewModel.updateUserSettings()
            }
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                _binding = null
            }
        })
    }
}