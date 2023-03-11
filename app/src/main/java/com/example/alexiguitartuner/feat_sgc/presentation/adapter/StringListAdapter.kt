package com.example.alexiguitartuner.feat_sgc.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexiguitartuner.commons.domain.InstrumentString
import com.example.alexiguitartuner.databinding.StringlistRowBinding
import com.example.alexiguitartuner.feat_sgc.presentation.SGCViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            var bufferString = instrumentString
            binding.tvNumber.text = instrumentString.stringNumber.toString()
            binding.etName.setText(instrumentString.name)
            binding.etScaleLength.setText(instrumentString.scaleLength.toString())
            binding.etTension.setText(instrumentString.tension.toString())

            CoroutineScope(Dispatchers.Main).launch {
                binding.tvGauge.text = sgcViewModel.calculateStringGauge(bufferString)
            }

            binding.fabDelete.setOnClickListener {
                sgcViewModel.deleteString(instrumentString)
            }

            binding.etName.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    val currentName = binding.etName.text.toString()
                    if (validateString(binding) && currentName != instrumentString.name) {
                        bufferString = instrumentString.copy(name = currentName)
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.tvGauge.text = sgcViewModel.calculateStringGauge(bufferString)
                        }
                        sgcViewModel.updateString(bufferString)
                    }
                }
            }

            binding.etScaleLength.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    val currentScaleLength = binding.etScaleLength.text.toString().toDouble()
                    if (validateString(binding) && currentScaleLength != instrumentString.scaleLength) {
                        bufferString = instrumentString.copy(scaleLength = currentScaleLength)
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.tvGauge.text = sgcViewModel.calculateStringGauge(bufferString)
                        }
                        sgcViewModel.updateString(bufferString)
                    }
                }
            }

            binding.etTension.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    val currentTension = binding.etTension.text.toString().toDouble()
                    if (validateString(binding) && currentTension != instrumentString.tension) {
                        bufferString = instrumentString.copy(tension = currentTension)
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.tvGauge.text = sgcViewModel.calculateStringGauge(bufferString)
                        }
                        sgcViewModel.updateString(bufferString)
                    }
                }
            }
        }
    }

    fun validateString(binding : StringlistRowBinding) : Boolean {
        val doublePattern = Regex("^[1-9]?[0-9](.[0-9])?$")
        val namePattern = Regex("^[A-H][b#]?[0-8]$")

        return namePattern.containsMatchIn(binding.etName.text.toString()) &&
            doublePattern.containsMatchIn(binding.etScaleLength.text.toString()) &&
            doublePattern.containsMatchIn(binding.etTension.text.toString())
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