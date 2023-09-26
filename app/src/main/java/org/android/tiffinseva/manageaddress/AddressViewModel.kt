package org.android.tiffinseva.manageaddress
import org.android.tiffinseva.R
import org.android.tiffinseva.base.TtsBaseViewModel
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.manageaddress.saveaddress.SaveAddressRequestTO
import org.android.tiffinseva.manageaddress.saveaddress.SaveAddressResponseTO
import org.android.tiffinseva.model.IEditTextChangeListener
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.AddressRepo
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.networking.tos.addresstos.CityResponseTO
import org.android.tiffinseva.utils.AppResourceProvider
import org.android.tiffinseva.utils.BaseClickHandler

class AddressViewModel(val addressRepo: AddressRepo, val appResourceProvider: AppResourceProvider)
    : TtsBaseViewModel<AddressVM>(), BaseClickHandler, IEditTextChangeListener {

    private val addressVM = AddressVM(appResourceProvider, this, this)

    companion object{
        val REMOVE_FRAGMENT = 11
    }
    override fun getScreenVm(): AddressVM {
        return addressVM
    }

    val savedAddressResultCallBack = object : ResultCallBack<SaveAddressResponseTO> {
        override fun onSuccess(response: SaveAddressResponseTO) {
            hideProgressBar()
            showSuccess(response.message)
            sendTtsDataEvent(ViewEvent(REMOVE_FRAGMENT,""))
        }

        override fun onFail(ttsError: TTSError) {
            hideProgressBar()
            showError(ttsError.errorMessage)
        }
    }
    override fun onStart() {
    }

    override fun onViewClick() {
        if (addressVM.houseNameObservable.get()?.editTextData?.get()?.isNullOrEmpty()!!)
            addressVM.houseNameObservable.get()?.errorText?.set(appResourceProvider.getString(R.string.house_name_error))
        else if (addressVM.addressLineObservable.get()?.editTextData?.get().isNullOrEmpty())
            addressVM.addressLineObservable.get()?.errorText?.set(appResourceProvider.getString(R.string.road_name_error))
        else if (addressVM.landMarkObservable.get()?.editTextData?.get().isNullOrEmpty())
            addressVM.landMarkObservable.get()?.errorText?.set(appResourceProvider.getString(R.string.landmark_name_error))
        else if (addressVM.pinCodeEditTextData.get()?.editTextData?.get().isNullOrEmpty())
            addressVM.pinCodeEditTextData.get()?.errorText?.set(appResourceProvider.getString(R.string.pincode_alert))
        else if (addressVM.pinCodeEditTextData.get()?.editTextData?.get()?.length != 6)
            addressVM.pinCodeEditTextData.get()?.errorText?.set(appResourceProvider.getString(R.string.valid_pincode_alert))
        else if(addressVM.textViewStateData.get()?.textData.isNullOrEmpty())
            showError(appResourceProvider.getString(R.string.valid_pincode_alert))
        else if(addressVM.textViewCityData.get()?.textData.isNullOrEmpty())
            showError(appResourceProvider.getString(R.string.valid_pincode_alert))
        else {
            val addressLine1 = addressVM.houseNameObservable.get()?.editTextData?.get() ?: ""
            val addressLine2 = addressVM.addressLineObservable.get()?.editTextData?.get() ?: ""
            val landMark = addressVM.landMarkObservable.get()?.editTextData?.get() ?: ""
            val stateName = addressVM.textViewStateData.get()?.textData?: ""
            val cityName = addressVM.textViewCityData.get()?.textData?: ""
            val pinCode = addressVM.pinCodeEditTextData.get()?.editTextData?.get() ?: ""
            saveAddressApiCall(addressLine1, addressLine2, landMark, stateName, cityName, pinCode)
        }
    }

    private fun saveAddressApiCall(addressLine1: String, addressLine2: String, landMark: String,
                                   stateName: String, cityName: String, pincode: String) {
        showProgressBar(appResourceProvider.getString(R.string.loading_str))
        addressRepo.saveUserAddress(SaveAddressRequestTO(addressLine1,
                addressLine2, landMark, stateName, cityName, pincode), savedAddressResultCallBack)
    }


    override fun onTextChange(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.length == 6) {
            addressRepo.getCityMetaInfoFromPincode(s.toString(), changeCityResultCallBack)
        } else if(s.length < 6) {
            addressVM.setState("")
            addressVM.setCity("")
        }
    }

    val changeCityResultCallBack = object : ResultCallBack<CityResponseTO> {
        override fun onSuccess(response: CityResponseTO) {
            addressVM.setState(response.state)
            addressVM.setCity(response.city)
        }

        override fun onFail(ttsError: TTSError) {
            showError(ttsError.errorMessage)
            addressVM.setState("")
            addressVM.setCity("")
        }
    }

}