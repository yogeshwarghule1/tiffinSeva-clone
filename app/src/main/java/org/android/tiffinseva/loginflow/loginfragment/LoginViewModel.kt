package org.android.tiffinseva.loginflow.loginfragment

import org.android.tiffinseva.base.BaseViewModel
import org.android.tiffinseva.networking.repository.LoginRepo
import org.android.tiffinseva.networking.repository.ResultCallBack

class LoginViewModel(val loginRepo: LoginRepo) : BaseViewModel() {

    fun generateOtp(triggetOtpTO: LoginRequestTO, resultCallBack: ResultCallBack<LoginResponseTO>) {
        return loginRepo.generateOtp(triggetOtpTO, resultCallBack)
    }
}