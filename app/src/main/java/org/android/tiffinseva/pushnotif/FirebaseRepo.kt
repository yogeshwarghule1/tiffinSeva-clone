package org.android.tiffinseva.pushnotif
import org.android.tiffinseva.loginflow.loginfragment.LoginRequestTO
import org.android.tiffinseva.loginflow.loginfragment.LoginResponseTO
import org.android.tiffinseva.loginflow.signupfragment.UserRespTO
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.apis.TTSFirebseApis
import org.android.tiffinseva.networking.repository.ResultCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirebaseRepo(val tssFirebaseToken: TTSFirebseApis) {
    fun updateFirebaseToken(firebaseTokenRqTO: FirebaseTokenRqTO, resultCallBack: ResultCallBack<UserRespTO>) {
        val networkHelper = NetworkHelper<UserRespTO>()
        tssFirebaseToken.updateFirebaseToken(firebaseTokenRqTO).enqueue(object :
            Callback<UserRespTO> {
            override fun onFailure(call: Call<UserRespTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<UserRespTO>, response: Response<UserRespTO>) {
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