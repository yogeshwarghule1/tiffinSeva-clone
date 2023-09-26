package org.android.tiffinseva.networking.repository

import org.android.tiffinseva.loginflow.loginfragment.LoginRequestTO
import org.android.tiffinseva.loginflow.loginfragment.LoginResponseTO
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.apis.TTSLoginApis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepo(val tssLoginApi: TTSLoginApis) {
    val LOG_TAG = "LoginRepo"

    fun generateOtp(loginRequestTO: LoginRequestTO, resultCallBack: ResultCallBack<LoginResponseTO>) {
        val networkHelper = NetworkHelper<LoginResponseTO>()
        tssLoginApi.genOtpUsingMobileNumber(loginRequestTO).enqueue(object : Callback<LoginResponseTO> {
            override fun onFailure(call: Call<LoginResponseTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<LoginResponseTO>, response: Response<LoginResponseTO>) {
                networkHelper.setResponse(response)
                if (networkHelper.isValidResponse())
                    response.body()?.let {
                        resultCallBack.onSuccess(it)
                    } ?: kotlin.run {
                        resultCallBack.onFail(networkHelper.mapThrowableToTtsError(NullPointerException()))
                    }
                else
                    resultCallBack.onFail(networkHelper
                            .mapThrowableToTtsError(TTSInValidResponseCodeException(response.code(), response.message())))
            }
        })
    }
}