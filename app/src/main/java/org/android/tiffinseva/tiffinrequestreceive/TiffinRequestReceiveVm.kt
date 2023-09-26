package org.android.tiffinseva.tiffinrequestreceive

import android.text.InputType
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import org.android.tiffinseva.R
import org.android.tiffinseva.model.CheckboxData
import org.android.tiffinseva.model.EditTextData
import org.android.tiffinseva.model.TTSTextViewClickListener
import org.android.tiffinseva.model.TextViewData
import org.android.tiffinseva.utils.AppResourceProvider

class TiffinRequestReceiveVm(val appResourceProvider: AppResourceProvider,
                             val tiffinDateClickListener: TTSTextViewClickListener,
                             val clickHandler: Handler):BaseObservable() {
    val addressObservableList = ObservableArrayList<BaseAddress>()
    var tiffinReuestTimeInISO = ""
    var addressId = -1
    val imageUrl = ObservableField("")
    val userTypeText = ObservableField("")
    val buttonText = ObservableField("")
    val tiffinHistoryCount = ObservableField("")
    val tiffinDate = ObservableField(TextViewData("",
        appResourceProvider.getString(R.string.tiffin_date), R.drawable.ic_calendar, tiffinDateClickListener))
    val etNumberOfTiffin = ObservableField(EditTextData(ObservableField(""),
            appResourceProvider.getString(R.string.no_of_tiffin), ObservableField(""), inputType = ObservableField(InputType.TYPE_CLASS_NUMBER)))
    val termsAndConds = ObservableField(CheckboxData(false, appResourceProvider.getString(R.string.str_terms_n_conds)))

    fun setUserAddress(addressList: List<BaseAddress>) {
        this.addressObservableList.clear()
        this.addressObservableList.addAll(addressList)
    }

    fun setTiffinDate(mTiffinDate: String) {
        tiffinDate.set(TextViewData(mTiffinDate,
            appResourceProvider.getString(R.string.tiffin_date), R.drawable.ic_calendar, tiffinDateClickListener))
    }

    fun setTiffinTimeInISO(timeInISO: String) {
        tiffinReuestTimeInISO = timeInISO
    }

    fun setmAddressId(id: Int) {
        addressId = id
    }

    interface Handler {
        fun onSubmitButtonClick()
        fun onAddressSelected(addressVm: AddressVm)
        fun addNewAddressCardClick()
    }
}

class AddressVm(val country: String? = null,
                val pincode: String? = null,
                val city: String? = null,
                val addressLine1: String? = null,
                val addressLine2: String? = null,
                val id: Int? = null,
                val state: String? = null,
                val landmark: String? = null,
                var isSelected: ObservableField<Boolean>,
                val clickHandler: TiffinRequestReceiveVm.Handler) : BaseAddress()

open class BaseAddress()

class AddNewAddress(val clickHandler: TiffinRequestReceiveVm.Handler) : BaseAddress()