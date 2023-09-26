package org.android.tiffinseva.tiffinrequestreceive

import androidx.databinding.ObservableField
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.R
import org.android.tiffinseva.UserType
import org.android.tiffinseva.base.TtsBaseViewModel
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.model.TTSTextViewClickListener
import org.android.tiffinseva.model.TextViewData
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.AddressRepo
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.networking.repository.TiffinRepo
import org.android.tiffinseva.networking.repository.tiffinrepo.AddressesItem
import org.android.tiffinseva.networking.repository.tiffinrepo.AllAddressResponseTO
import org.android.tiffinseva.utils.AppResourceProvider

class TiffinRequestReceiveViewModel(val tiffinRepo: TiffinRepo, val addressRepo: AddressRepo,
                                    val appResource: AppResourceProvider)
    : TtsBaseViewModel<TiffinRequestReceiveVm>(), TTSTextViewClickListener, TiffinRequestReceiveVm.Handler {

    var tiffinUserType = UserType.SERVER
    val tiffinVm = TiffinRequestReceiveVm(appResource, this, this)

    companion object {
        val SET_ADAPTER = 10
        val SHOW_DATE_PICKER = 11
        val ADD_NEW_ADDRESS_FRAGMENT = 14
        val CLOSE_FRAGMENT = 15
    }

    val allAddressListener = object : ResultCallBack<AllAddressResponseTO> {
        override fun onSuccess(response: AllAddressResponseTO) {
            hideProgressBar()
            response.addresses?.let {
                val addressVmList = mapAddressItemList(response.addresses)
                getScreenVm().setUserAddress(addressVmList)
                sendTtsDataEvent(ViewEvent(SET_ADAPTER, ""))
            } ?: kotlin.run {
                showError("No address")
            }
        }

        override fun onFail(ttsError: TTSError) {
            hideProgressBar()
            showError(ttsError.errorMessage)
        }
    }

    val saveTiffinAPICallBack = object : ResultCallBack<TiffinResponseTO> {
        override fun onSuccess(response: TiffinResponseTO) {
            hideProgressBar()
            showSuccess(getSuccessMessage(tiffinUserType))
            sendTtsDataEvent(ViewEvent(CLOSE_FRAGMENT, ""))
        }

        override fun onFail(ttsError: TTSError) {
            hideProgressBar()
            showError(ttsError.errorMessage)
        }
    }

    val cityStatFromPinCodeCallback = object : ResultCallBack<UserMetaInfo> {
        override fun onSuccess(response: UserMetaInfo) {
            hideProgressBar()
            setUserData(response)
        }

        override fun onFail(ttsError: TTSError) {
            hideProgressBar()
            showError(ttsError.errorMessage)
        }
    }

    private fun mapAddressItemList(addresses: List<AddressesItem>): ArrayList<BaseAddress> {
        val addressVmList = ArrayList<BaseAddress>()
        for (address in addresses)
            addressVmList.add(AddressVm(address.country, address.pincode, address.city,
                    address.addressLine1, address.addressLine2, address.id, address.state,
                    address.landmark, ObservableField(false), this))
        addressVmList.add(AddNewAddress(this))
        return addressVmList
    }

    private fun setUserData(response: UserMetaInfo) {
        tiffinVm.tiffinHistoryCount.set(response.tiffinCount.toString())
        tiffinVm.imageUrl.set(response.avatarUrl)
    }

    private fun getSuccessMessage(userType: UserType): String {
        return when (userType) {
            UserType.REQUESTER -> appResource.getString(R.string.tiffin_request_success)
            UserType.SERVER -> appResource.getString(R.string.tiffin_provider_success)
        }
    }

    override fun onStart() {
        showProgressBar(appResource.getString(R.string.loading_str))
        addressRepo.getAllAddress(allAddressListener)
    }

    fun getUserData() {
        tiffinRepo.getUserMetaInfo(tiffinUserType, cityStatFromPinCodeCallback)
    }

    override fun getScreenVm(): TiffinRequestReceiveVm {
        return tiffinVm
    }

    fun setTiffinUserType(type: Int) {
        if (type == AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_SERVER.ordinal)
            setViewData(appResource.getString(R.string.tiffin_provide_btn),
                appResource.getString(R.string.tiffin_provided), UserType.SERVER
            )
        else if (type == AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_REQUEST.ordinal)
            setViewData(appResource.getString(R.string.tiffin_request_btn)
                , appResource.getString(R.string.tiffin_requested), UserType.REQUESTER
            )
    }

    fun setViewData(buttonText: String, tvText: String, mUserType: UserType) {
        tiffinVm.buttonText.set(buttonText)
        tiffinVm.userTypeText.set(tvText)
        this.tiffinUserType = mUserType
    }

    override fun onTextViewClick(textViewData: TextViewData) {
        sendTtsDataEvent(ViewEvent(SHOW_DATE_PICKER, ""))
    }

    fun setTiffinTime(tiffinDate: String) {
        tiffinVm.setTiffinDate(tiffinDate)
    }

    private fun isValidDataDataAndMakeApiCall() {
        if (tiffinVm.addressId == -1)
            showError(getString(R.string.tiffin_request_receive_address_error))
        else if (tiffinVm.tiffinReuestTimeInISO.isEmpty())
            showError(getString(R.string.tiffin_request_receive_data_error))
        else if (tiffinVm.etNumberOfTiffin.get()?.editTextData?.get().isNullOrEmpty())
            showError(getString(R.string.tiffin_request_receive_tiffin_error))
        else if (tiffinVm.etNumberOfTiffin.get()?.editTextData?.get().isNullOrEmpty() == false && tiffinVm.etNumberOfTiffin.get()?.editTextData?.get()?.toInt() ?: 0 >= 100)
            showError(getString(R.string.tiffin_request_receive_tiffin_count_error))
        else if(!tiffinVm.termsAndConds.get()?.checkType!!)
            showError(getString(R.string.str_terms_n_conds_error))
        else
            makeTiffinAPICall(tiffinVm.etNumberOfTiffin.get()?.editTextData?.get()?.toInt() ?: -1
                , tiffinVm.tiffinReuestTimeInISO, tiffinUserType, tiffinVm.addressId)
    }

    private fun makeTiffinAPICall(
        numberOfTiffin: Int,
        tiffinTime: String,
        userType: UserType,
        addressId: Int
    ) {
        showProgressBar(getString(R.string.loading_str))
        tiffinRepo.saveTiffin(TiffinSaveTO(numberOfTiffin, tiffinTime, userType,
                addressId), saveTiffinAPICallBack)
    }

    fun setTiffinTimeInISO(timeInMillis: String) {
        tiffinVm.setTiffinTimeInISO(timeInMillis)
    }

    fun getString(id: Int): String {
        return appResource.getString(id)
    }

    override fun onSubmitButtonClick() {
        isValidDataDataAndMakeApiCall()
    }

    override fun onAddressSelected(addressVm: AddressVm) {
        for (vm in tiffinVm.addressObservableList)
            (vm as? AddressVm)?.isSelected?.set(false)
        addressVm.isSelected.set(true)
        tiffinVm.addressId = addressVm.id ?: -1
    }

    override fun addNewAddressCardClick() {
        sendTtsDataEvent(ViewEvent(ADD_NEW_ADDRESS_FRAGMENT, ""))
    }
}