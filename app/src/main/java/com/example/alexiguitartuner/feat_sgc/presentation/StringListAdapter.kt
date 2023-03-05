package com.example.alexiguitartuner.feat_sgc.presentation

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexiguitartuner.commons.domain.InstrumentString
import com.example.alexiguitartuner.databinding.StringlistRowBinding

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
            binding.tvNumber.text = instrumentString.stringNumber.toString()
            binding.etName.setText(instrumentString.name)
            binding.etScaleLength.setText(instrumentString.scaleLength.toString())
            binding.etTension.setText(instrumentString.tension.toString())

            binding.fabDelete.setOnClickListener {
                sgcViewModel.deleteString(instrumentString)
            }

            binding.etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) { updateString(binding,instrumentString) }
            })

            binding.etScaleLength.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) { updateString(binding,instrumentString) }
            })

            binding.etTension.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) { updateString(binding,instrumentString) }
            })

        }
    }

    fun updateString(binding : StringlistRowBinding, instrumentString: InstrumentString) {
        val doublePattern = Regex("^[1-9]?[0-9].[0-9]$")
        val namePattern = Regex("^[A-H][b#]?[0-9]$")

        if (namePattern.containsMatchIn(binding.etName.text.toString()) &&
            doublePattern.containsMatchIn(binding.etScaleLength.text.toString()) &&
            doublePattern.containsMatchIn(binding.etTension.text.toString())) {

            binding.tvGauge.text = sgcViewModel.calculateStringGauge(instrumentString)

            sgcViewModel.updateString(InstrumentString(
                binding.tvNumber.text.toString().toInt(),
                binding.etName.text.toString(),
                binding.etScaleLength.text.toString().toDouble(),
                binding.etTension.text.toString().toDouble()
            ))
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