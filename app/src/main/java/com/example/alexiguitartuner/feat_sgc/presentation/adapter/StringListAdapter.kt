package com.example.alexiguitartuner.feat_sgc.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.databinding.StringlistRowBinding
import com.example.alexiguitartuner.feat_sgc.presentation.viewmodel.SGCViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StringListAdapter(
    private val sgcViewModel: SGCViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
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
            lifecycleScope.launch(Dispatchers.IO) {
                val pitchName = sgcViewModel.getPitch(instrumentString.frequency)?.name
                val gauge = sgcViewModel.calculateStringGauge(instrumentString)
                withContext(Dispatchers.Main) {
                    binding.tvNumber.text = instrumentString.stringNumber.toString()
                    binding.etScaleLength.setText(instrumentString.scaleLength.toString())
                    binding.etTension.setText(instrumentString.tension.toString())
                    binding.etName.setText(pitchName)
                    binding.tvGauge.text = gauge
                }
            }
            binding.fabCalculate.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    val bufferFrequency = sgcViewModel.getPitchByName(binding.etName.text.toString())?.frequency ?: 0.0
                    val bufferString = InstrumentString(
                        instrumentString.stringNumber,
                        bufferFrequency,
                        binding.etScaleLength.text.toString().toDouble(),
                        binding.etTension.text.toString().toDouble()
                    )
                    withContext(Dispatchers.Main) {
                        binding.tvGauge.text = sgcViewModel.calculateStringGauge(bufferString)
                    }
                    if (bufferFrequency != 0.0) {
                        sgcViewModel.updateString(bufferString)
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.etName.error = "Invalid pitch name!"
                        }
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