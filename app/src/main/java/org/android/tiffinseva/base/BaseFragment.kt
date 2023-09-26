package org.android.tiffinseva.base
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import org.android.tiffinseva.homeflow.HomeActivity
import org.android.tiffinseva.viewhelper.ProgressBarHelper
import org.android.tiffinseva.viewhelper.SnackBarHelper

/**
 *
 */
abstract class BaseFragment : Fragment() {
    var progressBarHelper: ProgressBarHelper? = null
    var snackBarHelper: SnackBarHelper? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressBarHelper = ProgressBarHelper(context)
        snackBarHelper = SnackBarHelper(context)
    }

    fun setToolBarTitle(title: String) {
        (context as HomeActivity).mainToolBarHelper.setToolBarTitle(title)
    }

    fun setCityLayoutVisile(visible: Int) {
        (context as HomeActivity).mainToolBarHelper.setCityLayoutVisibility(visible)
    }

    fun setToolbarBackArrow(icon : Int){
        (context as HomeActivity).mainToolBarHelper.setToolBarIcon(icon)
    }

//    fun setUpSideNavigationView(enabled: Boolean){
//        (context as HomeActivity).mainToolBarHelper.setUpNavigationView(enabled)
//    }

    fun showProgressBar(message: String = "Loading") {
        progressBarHelper?.showProgressBarDialog(message)
    }

    fun hideProgressBar() {
        progressBarHelper?.hideProgressBarDialog()
    }

    fun showError(errorMessage: String) {
        snackBarHelper?.showSnackBar(getFragmentBaseView(), errorMessage, false)
    }

    fun showSuccess(successMessage: String) {
        snackBarHelper?.showSnackBar(getFragmentBaseView(), successMessage, true)
    }

    open fun showKeyboard(ctx: Context?) {
        try {
            ctx?.let {
                val imm =
                    ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun hideKeyboard(ctx: Context?) {
        try {
            ctx?.let {
                val inputManager = ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val v = (ctx as Activity).currentFocus ?: return
                inputManager.hideSoftInputFromWindow(v.windowToken, 0)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun <T> replaceFragment(bundle: Bundle, clazz: Class<T>) {
        (activity as BaseActivity).replaceFragment(bundle, clazz)
    }

    abstract fun getFragmentBaseView(): View?
}