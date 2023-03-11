package com.example.alexiguitartuner.feat_sgc.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.alexiguitartuner.R

class SGCInfoDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.info))
            .setPositiveButton(getString(R.string.accept)) { _,_ -> }
            .create()
}