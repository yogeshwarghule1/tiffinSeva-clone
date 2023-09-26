package org.android.tiffinseva.networking.repository

import org.android.tiffinseva.manageaddress.addresstos.ChangeCityRequestTO
import org.android.tiffinseva.manageaddress.addresstos.ChangeCityResponseTO
import org.android.tiffinseva.manageaddress.saveaddress.SaveAddressRequestTO
import org.android.tiffinseva.manageaddress.saveaddress.SaveAddressResponseTO
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.apis.TTSAddressApis
import org.android.tiffinseva.networking.repository.tiffinrepo.AllAddressResponseTO
import org.android.tiffinseva.networking.tos.addresstos.CityPinCodeResponseTO
import org.android.tiffinseva.networking.tos.addresstos.CityResponseTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepo(private val ttsAddressApis: TTSAddressApis) {

    fun getPinCodeFromState(state: String, resultCallBack: ResultCallBack<List<CityPinCodeResponseTO>>) {
        val networkHelper = NetworkHelper<List<CityPinCodeResponseTO>>()
        ttsAddressApis.getCityAndPinCodeList(state).enqueue(object : Callback<List<CityPinCodeResponseTO>> {
            override fun onFailure(call: Call<List<CityPinCodeResponseTO>>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<List<CityPinCodeResponseTO>>, response: Response<List<CityPinCodeResponseTO>>) {
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


    fun getCityMetaInfoFromPincode(pincode: String, resultCallBack: ResultCallBack<CityResponseTO>) {
        val networkHelper = NetworkHelper<CityResponseTO>()
        ttsAddressApis.getCityMetaInfo(pincode).enqueue(object : Callback<CityResponseTO> {
            override fun onFailure(call: Call<CityResponseTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<CityResponseTO>, response: Response<CityResponseTO>) {
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

    fun getAllAddress(resultCallBack: ResultCallBack<AllAddressResponseTO>) {
        val networkHelper = NetworkHelper<AllAddressResponseTO>()
        ttsAddressApis.getAddressListByUserId.enqueue(object : Callback<AllAddressResponseTO> {
            override fun onFailure(call: Call<AllAddressResponseTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<AllAddressResponseTO>, response: Response<AllAddressResponseTO>) {
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

    fun saveUserAddress(saveAddressRequestTO: SaveAddressRequestTO, resultCallBack: ResultCallBack<SaveAddressResponseTO>) {
        val networkHelper = NetworkHelper<SaveAddressResponseTO>()
        ttsAddressApis.saveAddressApi(saveAddressRequestTO).enqueue(object : Callback<SaveAddressResponseTO> {
            override fun onFailure(call: Call<SaveAddressResponseTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<SaveAddressResponseTO>, response: Response<SaveAddressResponseTO>) {
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

    fun changeCityAndState(saveAddressRequestTO: ChangeCityRequestTO, resultCallBack: ResultCallBack<ChangeCityResponseTO>) {
        val networkHelper = NetworkHelper<ChangeCityResponseTO>()
        ttsAddressApis.changeCityAndStateApi(saveAddressRequestTO).enqueue(object : Callback<ChangeCityResponseTO> {
            override fun onFailure(call: Call<ChangeCityResponseTO>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<ChangeCityResponseTO>, response: Response<ChangeCityResponseTO>) {
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