package org.android.tiffinseva.manageaddress

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField
import org.android.tiffinseva.R
import org.android.tiffinseva.TheTiffinSevaApp
import org.android.tiffinseva.databinding.DialogChangeCityBinding
import org.android.tiffinseva.manageaddress.addresstos.ChangeCityRequestTO
import org.android.tiffinseva.manageaddress.addresstos.ChangeCityResponseTO
import org.android.tiffinseva.model.EditTextData
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.AddressRepo
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.networking.repository.TTSAPIRepository
import org.android.tiffinseva.networking.tos.addresstos.CityResponseTO
import org.android.tiffinseva.utils.AppResourceProvider
import org.android.tiffinseva.utils.BaseClickHandler
import org.android.tiffinseva.viewhelper.BaseViewHelperImpl


class ChangeCityDialog(val mContext: Context, val dialogListener: IDialogListener) : Dialog(mContext, R.style.City_Change_Dialog), View {

    lateinit var mViewHelper: BaseViewHelperImpl
    val changeCityViewModel = ChangeCityDialogViewModel(AppResourceProvider,
        TTSAPIRepository.getInstance().getAddressRepo(), this, dialogListener)
    lateinit var bindingLayout: DialogChangeCityBinding

    init {
        TheTiffinSevaApp.getApplicationInstance().getCurrentCityName().get()?.let {
            if (it.isEmpty() || it.equals(mContext.getString(R.string.change_city)))
                setCancelable(false)
            else
                setCancelable(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        bindingLayout = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.dialog_change_city, null, false)
        setContentView(bindingLayout.root)
        mViewHelper = BaseViewHelperImpl(mContext, bindingLayout.root)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bindingLayout.obj = changeCityViewModel.changeCityVM
        changeCityViewModel.onStart()
    }

    override fun showError(errorMessage: String) {
        mViewHelper.showError(errorMessage)
    }

    override fun showSuccess(successMessage: String) {
        mViewHelper.showSuccess(successMessage)
    }

    override fun hideProgressBar() {
        mViewHelper.hideProgressBar()
    }

    override fun showProgressBar(progressMessage: String) {
        mViewHelper.showProgressBar(progressMessage)
    }

    override fun closeDialog() {
        dismiss()
        dialogListener.onDialogClose()
    }
}

interface View {
    fun showError(errorMessage: String)
    fun showSuccess(successMessage: String)
    fun hideProgressBar()
    fun showProgressBar(progressMessage: String)
    fun closeDialog()
}

interface IDialogListener {
    fun onCitySuccessChange()
    fun onDialogClose()
}

class ChangeCityDialogViewModel(val appResourceProvider: AppResourceProvider
                                , val addressRepo: AddressRepo, val mView: View,
                                val dialogListener: IDialogListener) : BaseClickHandler {
    val changeCityVM = ChangeCityVM(appResourceProvider, this)

    var city = ""
    var state = ""

    val changeCityStateCallBack = object : ResultCallBack<ChangeCityResponseTO> {
        override fun onSuccess(response: ChangeCityResponseTO) {
            mView.hideProgressBar()
            mView.closeDialog()
            TheTiffinSevaApp.getApplicationInstance().setCurrentCityName(city)
            dialogListener.onCitySuccessChange()
        }
        override fun onFail(throwable: TTSError) {
            mView.hideProgressBar()
        }
    }

    val cityStateFromPinCodeResultCallBack = object : ResultCallBack<CityResponseTO> {
        override fun onSuccess(response: CityResponseTO) {
            mView.hideProgressBar()
            checkResponse(response)
        }

        override fun onFail(ttsError: TTSError) {
            mView.hideProgressBar()
            mView.showError(ttsError.errorMessage)
        }
    }

    var callback: OnPropertyChangedCallback = object : OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            if (sender == changeCityVM.pinCodeEditText.get()?.editTextData) {
                val editTextStr = changeCityVM.pinCodeEditText.get()?.editTextData?.get()
                if (editTextStr?.isNullOrEmpty() == false && editTextStr.trim().length == 6) {
                    getCityStateFromPinCodeApiCall(editTextStr.trim())
                } else if (editTextStr?.isNullOrEmpty() == false && editTextStr.trim().length > 6) {
                    changeCityVM.pinCodeEditText.get()?.errorText?.set(appResourceProvider.getString(R.string.pin_code_lenght_error))
                }
            }
        }
    }

    private fun checkResponse(response: CityResponseTO) {
        changeCityVM.pincodeVerificationTvVisibility.set(true)
        if (response.city.isNullOrEmpty()) {
            changeCityVM.isValidPinCode.set(false)
            changeCityVM.pincodeVerificationTvText.set(appResourceProvider.getString(R.string.invalid_pincode))
            return
        }
        changeCityVM.isValidPinCode.set(true)
        changeCityVM.pincodeVerificationTvText.set(appResourceProvider.getString(R.string.valid_pincode))
        this.city = response.city
        this.state = response.state
    }

    fun changeCityAndState(state: String, city: String) {
        changeCityVM.pinCodeEditText.get()?.editTextData
        mView.showProgressBar(appResourceProvider.getString(R.string.loading_str))
        addressRepo.changeCityAndState(ChangeCityRequestTO(state, city), changeCityStateCallBack)
    }

    override fun onViewClick() {
        when {
            changeCityVM.pinCodeEditText.get()?.editTextData?.get()?.isNullOrEmpty() ?: false -> {
                mView.showError(appResourceProvider.getString(R.string.pincode_alert))
            }
            changeCityVM.pinCodeEditText.get()?.editTextData?.get()?.length!! != 6 -> {
                mView.showError(appResourceProvider.getString(R.string.valid_pincode_alert))
            }
            else -> {
                changeCityAndState(state, city)
            }
        }
    }

    private fun getCityStateFromPinCodeApiCall(pincode: String) {
        mView.showProgressBar(appResourceProvider.getString(R.string.loading_str))
        addressRepo.getCityMetaInfoFromPincode(pincode, cityStateFromPinCodeResultCallBack)
    }

    fun onStart() {
        changeCityVM.pinCodeEditText.get()?.editTextData?.addOnPropertyChangedCallback(callback)
    }
}

class ChangeCityVM(appResourceProvider: AppResourceProvider, val clickHandler: BaseClickHandler) {
    val pincodeVerificationTvVisibility = ObservableField(false)
    val pincodeVerificationTvText = ObservableField("")
    val isValidPinCode = ObservableField(false)
    val pinCodeEditText = ObservableField(EditTextData(ObservableField(""),
            appResourceProvider.getString(R.string.str_pincode), ObservableField(""), ObservableField(6), ObservableField(InputType.TYPE_CLASS_NUMBER)))
}