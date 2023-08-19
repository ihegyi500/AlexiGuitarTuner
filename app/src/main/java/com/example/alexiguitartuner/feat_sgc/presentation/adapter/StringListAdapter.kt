package com.example.alexiguitartuner.feat_sgc.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexiguitartuner.commons.domain.InstrumentString
import com.example.alexiguitartuner.databinding.StringlistRowBinding
import com.example.alexiguitartuner.feat_sgc.presentation.SGCViewModel
import kotlinx.coroutines.launch

class StringListAdapter(
    private val sgcViewModel : SGCViewModel
) : ListAdapter<InstrumentString, StringListAdapter.ViewHolder>(InstrumentStringDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: StringlistRowBinding = StringlistRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instrumentString = getItem(position)
        holder.bind(instrumentString)
    }

    inner class ViewHolder(
        private val binding: StringlistRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(instrumentString: InstrumentString) {
            sgcViewModel.viewModelScope.launch {
                binding.tvNumber.text = instrumentString.stringNumber.toString()
                binding.etScaleLength.setText(instrumentString.scaleLength.toString())
                binding.etTension.setText(instrumentString.tension.toString())
                binding.etName.setText(sgcViewModel.getPitch(instrumentString.frequency)?.name)
                binding.tvGauge.text = sgcViewModel.calculateStringGauge(instrumentString)
            }
            binding.fabCalculate.setOnClickListener {
                sgcViewModel.viewModelScope.launch {
                    val bufferFrequency = sgcViewModel.getPitchByName(binding.etName.text.toString())?.frequency ?: 0.0
                    val bufferString = InstrumentString(
                        instrumentString.stringNumber,
                        bufferFrequency,
                        binding.etScaleLength.text.toString().toDouble(),
                        binding.etTension.text.toString().toDouble()
                    )
                    binding.tvGauge.text = sgcViewModel.calculateStringGauge(bufferString)
                    if (bufferFrequency != 0.0) {
                        sgcViewModel.updateString(bufferString)
                    } else {
                        binding.etName.error = "Invalid pitch name"
                    }
                }
            }

            binding.fabDelete.setOnClickListener {
                sgcViewModel.deleteString(instrumentString)
            }
        }
    }
}

class InstrumentStringDiffCallback : DiffUtil.ItemCallback<InstrumentString>() {
    override fun areItemsTheSame(oldItem: InstrumentString, newItem: InstrumentString): Boolean {
        return oldItem.stringNumber == newItem.stringNumber
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: InstrumentString, newItem: InstrumentString): Boolean {
        return oldItem == newItem
    }
}