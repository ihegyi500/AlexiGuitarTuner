package com.example.alexiguitartuner.feat_metronome.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.alexiguitartuner.databinding.FragmentMetronomeBinding

class MetronomeFragment : Fragment() {

    private lateinit var metronomeViewModel: MetronomeViewModel

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
        metronomeViewModel = ViewModelProvider(this)[MetronomeViewModel::class.java]

    }

    private fun renderFragment() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}