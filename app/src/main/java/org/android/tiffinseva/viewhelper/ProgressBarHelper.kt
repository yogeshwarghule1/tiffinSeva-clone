package org.android.tiffinseva.viewhelper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.progress_dialog_layout.view.*
import org.android.tiffinseva.R


class ProgressBarHelper(val context: Context) {
    var progressDialog: AlertDialog? = null
    val dialogBuilder: AlertDialog.Builder
    val dialogView: View

    init {
        dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dialogView = inflater.inflate(R.layout.progress_dialog_layout, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        progressDialog = dialogBuilder.create()
    }

    fun showProgressBarDialog(message: String) {
        dialogView.tvMessage.text = message
        progressDialog?.show()
    }

    fun hideProgressBarDialog() {
        progressDialog?.dismiss()
    }
}