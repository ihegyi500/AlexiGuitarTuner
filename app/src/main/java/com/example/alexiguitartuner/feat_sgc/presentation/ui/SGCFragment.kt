package com.example.alexiguitartuner.feat_sgc.presentation.ui

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
import com.example.alexiguitartuner.databinding.FragmentSGCBinding
import com.example.alexiguitartuner.feat_sgc.presentation.viewmodel.SGCViewModel
import com.example.alexiguitartuner.feat_sgc.presentation.adapter.StringListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SGCFragment : Fragment() {

    companion object {
        const val DIALOG_TAG = "SGC_Info_Dialog"
    }

    private val viewModel: SGCViewModel by viewModels()
    private lateinit var stringListAdapter : StringListAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderFragment()
    }

    private fun renderFragment() {
        stringListAdapter = StringListAdapter(viewModel,  lifecycleScope)
        binding.stringListRecyclerView.adapter = stringListAdapter

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listOfStrings.collectLatest {
                    stringListAdapter.submitList(it)
                }
            }
        }

        binding.fabAddString.setOnClickListener {
            viewModel.insertString()
        }

        binding.fabShowInfo.setOnClickListener {
            SGCInfoDialog().show(
                childFragmentManager,
                DIALOG_TAG
            )
        }
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                _binding = null
            }
        })
    }
}