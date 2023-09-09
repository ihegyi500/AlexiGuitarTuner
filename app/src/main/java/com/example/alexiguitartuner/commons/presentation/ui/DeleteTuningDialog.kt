package com.example.alexiguitartuner.commons.presentation.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.alexiguitartuner.commons.presentation.viewmodel.UserSettingsViewModel
import com.google.android.material.snackbar.Snackbar

class DeleteTuningDialog: DialogFragment() {

    private val viewModel: UserSettingsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Are You sure You want to delete all custom tunings You made? This action cannot be undone!")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllCustomTunings()
                Snackbar.make(requireParentFragment().requireView(), "All custom tunings deleted!", Snackbar.LENGTH_LONG).show()
                dismiss()
            }
            .setNegativeButton("No") { _, _ ->
                dismiss()
            }
            .create()
}