package com.techdoctorbd.taskmanagermongodb.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.techdoctorbd.taskmanagermongodb.R

class CustomProgressDialog(private val context: Context) {

    private lateinit var alertDialog: AlertDialog

    fun show(message: String) {
        val builder = MaterialAlertDialogBuilder(context)
        val dialogView = View.inflate(context, R.layout.custom_progress_dialog, null)
        builder.setCancelable(false)
        builder.setView(dialogView)

        val progressText = dialogView.findViewById<TextView>(R.id.progress_bar_text)

        progressText.text = message

        alertDialog = builder.create()

        alertDialog.show()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }
}