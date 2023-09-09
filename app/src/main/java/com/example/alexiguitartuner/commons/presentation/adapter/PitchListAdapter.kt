package com.example.alexiguitartuner.commons.presentation.adapter

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.presentation.viewmodel.CreateTuningViewModel
import com.example.alexiguitartuner.databinding.PitchlistRowBinding
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class PitchListAdapter(
    private val createTuningViewModel: CreateTuningViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
) : ListAdapter<Pitch, PitchListAdapter.ViewHolder>(PitchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PitchlistRowBinding = PitchlistRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pitch = getItem(position)
        holder.bind(pitch, position)
    }

    inner class ViewHolder(
        private val binding: PitchlistRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pitch: Pitch, rowPosition: Int) {
            val adapterOfPitch = ArrayAdapter(
                binding.root.context,
                R.layout.simple_spinner_item,
                ArrayList<String>()
            )
            adapterOfPitch.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.actPitch.setAdapter(adapterOfPitch)

            lifecycleScope.launch {
                createTuningViewModel.uiState.collect {
                    adapterOfPitch.clear()
                    adapterOfPitch.addAll(createTuningViewModel.pitchList.map { it.name })
                    adapterOfPitch.notifyDataSetChanged()
                    binding.mtvFreq.text = ((pitch.frequency * 100.0).roundToInt() / 100.0).toString()
                    binding.actPitch.setText(pitch.name, false)
                }
            }

            binding.actPitch.setOnItemClickListener { _, _, pos, _ ->
                lifecycleScope.launch {
                    createTuningViewModel.updatePitchInList(rowPosition, pos)
                    binding.mtvFreq.text = ((createTuningViewModel.uiState.value.pitchesOfTuning[rowPosition].frequency * 100.0).roundToInt() / 100.0).toString()
                }
            }
        }
    }
}

class PitchDiffCallback : DiffUtil.ItemCallback<Pitch>() {
    override fun areItemsTheSame(oldItem: Pitch, newItem: Pitch): Boolean {
        return oldItem.frequency == newItem.frequency
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Pitch, newItem: Pitch): Boolean {
        return oldItem == newItem
    }
}