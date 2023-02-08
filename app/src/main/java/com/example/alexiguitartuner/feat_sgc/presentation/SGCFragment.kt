package com.example.alexiguitartuner.feat_sgc.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.databinding.FragmentSGCBinding

class SGCFragment : Fragment() {

    private lateinit var viewModel: SGCViewModel

    private var _binding: FragmentSGCBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSGCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}