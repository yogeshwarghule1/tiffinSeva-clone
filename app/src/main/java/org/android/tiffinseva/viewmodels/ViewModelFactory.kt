package org.android.tiffinseva.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.android.tiffinseva.homeflow.dashboardfragment.DashBoardViewModel
import org.android.tiffinseva.loginflow.loginfragment.LoginViewModel
import org.android.tiffinseva.loginflow.signupfragment.SignupViewModel
import org.android.tiffinseva.loginflow.verifyotpfragment.OtpVerificationViewModel
import org.android.tiffinseva.networking.repository.ApiRepoFactory
import org.android.tiffinseva.networking.repository.TTSAPIRepository
import org.android.tiffinseva.manageaddress.AddressViewModel
import org.android.tiffinseva.tiffinrequestreceive.TiffinRequestReceiveViewModel
import org.android.tiffinseva.utils.AppResourceProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val ttsApiRepository: TTSAPIRepository) : ViewModelProvider.Factory {

    private object HOLDER {
        val INSTANCE = ViewModelFactory(ApiRepoFactory.getInstance().getTssApiRepository())
    }

    companion object {
        @JvmStatic
        fun getInstance(): ViewModelFactory {
            val instance: ViewModelFactory by lazy { HOLDER.INSTANCE }
            return instance
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(ttsApiRepository.getAddressRepo(), AppResourceProvider) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(ttsApiRepository.getLoginRepo()) as T
        } else if (modelClass.isAssignableFrom(OtpVerificationViewModel::class.java)) {
            return OtpVerificationViewModel(ttsApiRepository.getOtpRepo()) as T
        } else if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)) {
            return DashBoardViewModel(ttsApiRepository.getHomeRepo()) as T
        } else if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(ttsApiRepository.getSignUpRepo()) as T
        } else if (modelClass.isAssignableFrom(TiffinRequestReceiveViewModel::class.java)) {
            return TiffinRequestReceiveViewModel(ttsApiRepository.getTiffinRepo(),
                    ttsApiRepository.getAddressRepo(), AppResourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class");
    }
}