package org.android.tiffinseva.utils

import androidx.databinding.ObservableField
import org.android.tiffinseva.TheTiffinSevaApp
import org.android.tiffinseva.manageaddress.City
import org.android.tiffinseva.manageaddress.StateSpinner
import org.android.tiffinseva.model.SpinnerData
import org.android.tiffinseva.networking.tos.addresstos.CityPinCodeResponseTO

class SpinnerIncludeViewUtility {
    companion object {
        fun getSelectedItemFromSpinner(stateSpinnerData: ObservableField<SpinnerData>): String {
            return stateSpinnerData.get()?.spinnerData?.get(stateSpinnerData.get()?.selectedItemPosition
                    ?: 0)?.name ?: "Default"
        }

        fun verifySpinnerData(spinnerData: ObservableField<SpinnerData>): Boolean {
            var isValidSpinnerData = false
            if (spinnerData.get()?.selectedItemPosition != -1) {
                val selectedPos = spinnerData.get()?.selectedItemPosition ?: 0
                if (!spinnerData.get()?.spinnerData?.get(selectedPos)?.name.equals("None"))
                    isValidSpinnerData = true
            }
            return isValidSpinnerData
        }

        fun getCityListFromResponse(response: List<CityPinCodeResponseTO>): ArrayList<City> {
            val cityList = ArrayList<City>()
            for (cityPinCodeTO in response)
                cityList.add(City(cityPinCodeTO.city))
            cityList.add(0, City("None"))
            return cityList
        }

        fun getSpinnerDataStateList(): ArrayList<StateSpinner> {
            val appStateList = TheTiffinSevaApp.getApplicationInstance()?.getStateList()
            val stateList = ArrayList<StateSpinner>()
            if (appStateList != null) {
                for (eachState in appStateList)
                    stateList.add(StateSpinner(eachState.id, eachState.state))
            }
            return stateList
        }
    }

}