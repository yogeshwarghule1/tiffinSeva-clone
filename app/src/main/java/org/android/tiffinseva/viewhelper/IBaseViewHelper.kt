package org.android.tiffinseva.viewhelper

import android.content.Context
import android.view.View

interface IBaseViewHelper {
    fun showError(errorMessage: String)
    fun showSuccess(successMessage: String)
    fun hideProgressBar()
    fun showProgressBar(progressMessage: String)
}

class BaseViewHelperImpl(val context: Context, val view: View) : IBaseViewHelper {
    val snackBarHelper = SnackBarHelper(context)
    val progressBarHelper = ProgressBarHelper(context)
    override fun showError(errorMessage: String) {
        snackBarHelper.showSnackBar(view, errorMessage, false)
    }

    override fun showSuccess(successMessage: String) {
        snackBarHelper.showSnackBar(view, successMessage, true)
    }

    override fun hideProgressBar() {
        progressBarHelper.hideProgressBarDialog()
    }

    override fun showProgressBar(progressMessage: String) {
        progressBarHelper.showProgressBarDialog(progressMessage)
    }

}