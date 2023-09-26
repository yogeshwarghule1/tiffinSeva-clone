package org.android.tiffinseva.viewhelper

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import org.android.tiffinseva.R

class SnackBarHelper(val context: Context?) {

    fun showSnackBar(view: View?, message: String, isSuccess: Boolean) {
        try {
            context?.let { context -> run {
                    view?.let {
                        var color = ContextCompat.getColor(context, R.color.successColor)
                        if (!isSuccess)
                            color = ContextCompat.getColor(context, R.color.errorColor)
                        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                        val snackBarView = snackbar.view
                        snackBarView.setBackgroundColor(color)
                        snackbar.show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}