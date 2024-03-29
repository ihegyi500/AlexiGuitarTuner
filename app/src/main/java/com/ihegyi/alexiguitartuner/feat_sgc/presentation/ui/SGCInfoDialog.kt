package com.ihegyi.alexiguitartuner.feat_sgc.presentation.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ihegyi.alexiguitartuner.R

class SGCInfoDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.info))
            .setPositiveButton(getString(R.string.accept)) { _,_ -> }
            .create()
}