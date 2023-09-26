package org.android.tiffinseva.networking.repository

import org.android.tiffinseva.UserType
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.apis.TTStiffinApis
import org.android.tiffinseva.tiffinrequestreceive.TiffinResponseTO
import org.android.tiffinseva.tiffinrequestreceive.TiffinSaveTO
import org.android.tiffinseva.tiffinrequestreceive.UserMetaInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TiffinRepo(private val ttStiffinApis: TTStiffinApis) {

    fun saveTiffin(tiffinSaveTO: TiffinSaveTO, resultCallBack: ResultCallBack<TiffinResponseTO>) {
        val networkHelper = NetworkHelper<TiffinResponseTO>()
        ttStiffinApis.saveTiffinApi(tiffinSaveTO).enqueue(object : Callback<TiffinResponseTO> {
            override fun onFailure(call: Call<TiffinResponseTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<TiffinResponseTO>, response: Response<TiffinResponseTO>) {
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

    fun getUserMetaInfo(userType: UserType, resultCallBack: ResultCallBack<UserMetaInfo>) {
        val networkHelper = NetworkHelper<UserMetaInfo>()
        ttStiffinApis.getUserMetaInfo(userType).enqueue(object : Callback<UserMetaInfo> {
            override fun onFailure(call: Call<UserMetaInfo>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<UserMetaInfo>, response: Response<UserMetaInfo>) {
                networkHelper.setResponse(response)
                if (networkHelper.isValidResponse())
                    response.body()?.let {
                        resultCallBack.onSuccess(it)
                    } ?: kotlin.run {
                        resultCallBack.onFail(networkHelper.mapThrowableToTtsError(java.lang.NullPointerException()))
                    }
                else
                    resultCallBack.onFail(networkHelper
                            .mapThrowableToTtsError(TTSInValidResponseCodeException(response.code(), response.message())))
            }
        })
    }
}