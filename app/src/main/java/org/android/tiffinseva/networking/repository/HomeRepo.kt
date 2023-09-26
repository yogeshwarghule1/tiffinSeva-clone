package org.android.tiffinseva.networking.repository

import org.android.tiffinseva.homeflow.dashboardfragment.DashBoardRespTO
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.apis.TTSHomeApis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepo(val homeApis: TTSHomeApis) {

    val LOG_TAG = "HomeRepo"
    fun getHomeDetails(resultCallBack: ResultCallBack<DashBoardRespTO>) {

        val networkHelper = NetworkHelper<DashBoardRespTO>()
        homeApis.tiffinHomeDetails.enqueue(object : Callback<DashBoardRespTO> {
            override fun onFailure(call: Call<DashBoardRespTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<DashBoardRespTO>, response: Response<DashBoardRespTO>) {
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