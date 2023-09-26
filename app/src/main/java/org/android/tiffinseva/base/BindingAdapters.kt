package org.android.tiffinseva.base

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import org.android.tiffinseva.manageaddress.SpinnerSelectionListener
import org.android.tiffinseva.manageaddress.StateSpinnerAdapter
import org.android.tiffinseva.model.BaseSpinnerData
import org.android.tiffinseva.utils.ImageUtils


class BindingAdapters {

    companion object {

        @JvmStatic
        @BindingAdapter("errorText")
        fun BindErrorInEditText(view: EditText, error: String?) {
            if (error.isNullOrEmpty())
                view.error = null
            else
                view.error = error
        }

        @JvmStatic
        @BindingAdapter("app:srcCompat")
        fun bindSrcCompat(imageView: ImageView, imageId: Int?) {
            imageId?.let {
                imageView.setImageResource(imageId)
            }
        }


        @JvmStatic
        @BindingAdapter("app:visibility")
        fun setViewVisibility(view: View, visibility: Boolean?) {
            visibility?.let {
                if (visibility)
                    view.visibility = View.VISIBLE
                else
                    view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["ttsImageUrl", "placeHolderUrl", "ttsErrorUrl"], requireAll = false)
        fun loadImageUrl(imageView: ImageView, imageUrl: String?, placeHolderUrl: String?, ttsErrorUrl: String?) {
            if (!imageUrl.isNullOrEmpty())
                ImageUtils.instance?.loadBgImage(imageView, imageUrl)
        }

        @JvmStatic
        @BindingAdapter(value = ["spinnerItemList", "selectedItemIndex", "selectedItemIndexAttrChanged", "spinnerItemSelectionListener"], requireAll = false)
        fun setSpinnerData(spinner: Spinner, spinnerList: List<BaseSpinnerData>?, selectedItemIndex: Int, listener: InverseBindingListener?, spinnerItemSelectionListener: SpinnerSelectionListener?) {
            if (spinnerList == null) return
            spinner.adapter = StateSpinnerAdapter(spinner.context, android.R.layout.simple_spinner_dropdown_item, spinnerList)
            setCurrentSelection(spinner, selectedItemIndex)
            setSpinnerListener(spinner, listener!!, spinnerItemSelectionListener)
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "selectedItemIndex")
        fun getSelectedData(spinner: Spinner): Int {
            return spinner.selectedItemPosition
        }

        private fun setSpinnerListener(spinner: Spinner, listener: InverseBindingListener, spinnerItemSelectionListener: SpinnerSelectionListener?) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    listener.onChange()
                    spinnerItemSelectionListener?.onSpinnerItemSelect(spinner.getItemAtPosition
                    (spinner.selectedItemPosition) as BaseSpinnerData, spinner.selectedItemPosition)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    listener.onChange()
                    spinnerItemSelectionListener?.onSpinnerItemSelect(spinner.getItemAtPosition
                    (spinner.selectedItemPosition) as BaseSpinnerData, spinner.selectedItemPosition)
                }
            }
        }

        private fun setCurrentSelection(spinner: Spinner, selectedItemIndex: Int): Boolean {
            for (index in 0 until spinner.adapter.count) {
                if (spinner.getItemAtPosition(index) == selectedItemIndex) {
                    spinner.setSelection(index)
                    return true
                }
            }
            return false
        }
    }
}