package org.android.tiffinseva.networking.repository

import org.android.tiffinseva.loginflow.verifyotpfragment.VerifyOtpRequestTO
import org.android.tiffinseva.loginflow.verifyotpfragment.VerifyOtpResponseTO
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.apis.TTSVerifyOtpApis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpRepo(val verifyOtpApis: TTSVerifyOtpApis) {
    fun verifyOtp(verifyOtpRequestTO: VerifyOtpRequestTO, resultCallBack: ResultCallBack<VerifyOtpResponseTO>) {
        verifyOtpApis.verifyOtp(verifyOtpRequestTO).enqueue(object : Callback<VerifyOtpResponseTO> {
            val networkHelper = NetworkHelper<VerifyOtpResponseTO>()
            override fun onResponse(call: Call<VerifyOtpResponseTO>, response: Response<VerifyOtpResponseTO>) {
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

            override fun onFailure(call: Call<VerifyOtpResponseTO?>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError((Exception("Not valid response code"))))
            }
        })
    }
}