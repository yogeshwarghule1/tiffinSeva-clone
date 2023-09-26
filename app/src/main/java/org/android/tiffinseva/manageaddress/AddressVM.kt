package org.android.tiffinseva.manageaddress

import android.text.InputType
import androidx.databinding.ObservableField
import org.android.tiffinseva.R
import org.android.tiffinseva.model.*
import org.android.tiffinseva.utils.AppResourceProvider
import org.android.tiffinseva.utils.BaseClickHandler

class AddressVM(val appResourceProvider: AppResourceProvider,
                val clickHandler: BaseClickHandler, editTextChangeListener: IEditTextChangeListener) {

    val houseNameObservable = ObservableField(EditTextData(ObservableField(""), appResourceProvider.getString(R.string.address_house_num), ObservableField("")))
    val addressLineObservable = ObservableField(EditTextData(ObservableField(""), appResourceProvider.getString(R.string.address_road_name),ObservableField("")))
    val landMarkObservable = ObservableField(EditTextData(ObservableField(""), appResourceProvider.getString(R.string.address_landmark_name),ObservableField("")))
    val pinCodeEditTextData = ObservableField(EditTextData(ObservableField(""),
            appResourceProvider.getString(R.string.str_pincode), ObservableField(""), ObservableField(6), ObservableField(InputType.TYPE_CLASS_NUMBER), editTextChangeListener))
    val textViewStateData = ObservableField(TextViewData("", appResourceProvider.getString(R.string.select_state)))
    val textViewCityData = ObservableField(TextViewData("", appResourceProvider.getString(R.string.select_city)))

    fun setState(state: String) {
        textViewStateData.set(TextViewData(state, appResourceProvider.getString(R.string.select_state)))
    }

    fun setCity(city: String) {
        textViewCityData.set(TextViewData(city, appResourceProvider.getString(R.string.select_city)))
    }
}


interface SpinnerSelectionListener {
    fun onSpinnerItemSelect(spinnerData: BaseSpinnerData, selectedIndex: Int)
}