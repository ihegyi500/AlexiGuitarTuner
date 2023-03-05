package com.example.alexiguitartuner.feat_sgc.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.commons.domain.InstrumentString
import com.example.alexiguitartuner.databinding.StringlistRowBinding
import com.example.alexiguitartuner.feat_chordtable.presentation.ChordTableViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class StringListAdapter(
    private val context: Context,
    private val sgcViewModel : SGCViewModel
) : ListAdapter<InstrumentString, StringListAdapter.ViewHolder>(InstrumentStringDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding: StringlistRowBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.stringlist_row,
            parent, false)
        binding.adapter = this
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instrumentString = getItem(position)
        holder.bind(instrumentString)
    }

    inner class ViewHolder(
        private val binding: StringlistRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        /*val tuningAdapter : ArrayAdapter<String> = ArrayAdapter(
            this@StringListAdapter.context,
            android.R.layout.simple_spinner_dropdown_item
        )
        val selectedChordAdapter : ArrayAdapter<String> = ArrayAdapter(
            this@StringListAdapter.context,
            android.R.layout.simple_spinner_dropdown_item
        )*/

        fun bind(instrumentString: InstrumentString) {
            binding.instrumentString = instrumentString

            binding.etName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    //binding.tvGauge.text = sgcViewModel.calculateGauge(stringToCalculate)
                    sgcViewModel.setIsSaved(false)
                    binding.tvGauge.text = binding.etName.text

                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
/*

            GlobalScope.launch(Dispatchers.IO) {
                    sgcViewModel.isSaved.collectLatest {
                        if (it) {
                            try {
                                sgcViewModel.updateString(InstrumentString(
                                    binding.tvNumber.text.toString().toInt(),
                                    binding.etName.text.toString(),
                                    binding.etScaleLength.text.toString().toDouble(),
                                    binding.etTension.text.toString().toDouble()
                                ))
                                Log.d("isSaved", "Called from adapter")
                            } catch (e : Exception) {
                                Log.d("isSaved", "error: $e")
                            }
                        }
                    }
                }
            */
            }
        }

    fun deleteString(string: InstrumentString) {
        sgcViewModel.deleteString(string)
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