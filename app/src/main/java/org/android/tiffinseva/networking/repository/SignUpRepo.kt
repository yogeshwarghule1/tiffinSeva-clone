package org.android.tiffinseva.networking.repository
import org.android.tiffinseva.loginflow.signupfragment.UserRespTO
import org.android.tiffinseva.loginflow.signupfragment.UserTO
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.apis.TTSSignUpApis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SignUpRepo(val signUpApis: TTSSignUpApis) {

    fun createUserRequest(userTO: UserTO, callBack: ResultCallBack<UserRespTO>) {
        val networkHelper = NetworkHelper<UserRespTO>()
        signUpApis.createUser(userTO).enqueue(object : Callback<UserRespTO> {
            override fun onFailure(call: Call<UserRespTO>, t: Throwable) {
                Timber.e("Exception .." + t.cause)
                callBack.onFail(networkHelper.mapThrowableToTtsError(t as Exception))
            }

            override fun onResponse(call: Call<UserRespTO>, response: Response<UserRespTO>) {
                networkHelper.setResponse(response)
                if (networkHelper.isValidResponse())
                    response.body()?.let {
                        callBack.onSuccess(it)
                    } ?: kotlin.run {
                        callBack.onFail(networkHelper.mapThrowableToTtsError(NullPointerException()))
                    }
                else
                    callBack.onFail(networkHelper.mapThrowableToTtsError(TTSInValidResponseCodeException(
                                    response.code(), response.message())))
            }
        })

    }

}