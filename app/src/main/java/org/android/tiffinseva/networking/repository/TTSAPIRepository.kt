package org.android.tiffinseva.networking.repository

import org.android.tiffinseva.homeflow.homelistingfragment.TiffinPersonaListTO
import org.android.tiffinseva.homeflow.homelistingfragment.TiffinSearchRequestTO
import org.android.tiffinseva.networking.NetworkHelper
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.TTSInValidResponseCodeException
import org.android.tiffinseva.networking.TTSRetrofitClient
import org.android.tiffinseva.networking.apis.*
import org.android.tiffinseva.pushnotif.FirebaseRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TTSAPIRepository private constructor() {
    private val tssApis: TTSApis = TTSRetrofitClient.getInstance().create(TTSApis::class.java)

    private object HOLDER {
        val INSTANCE = TTSAPIRepository()
    }

    private object AddressRepoObjHolder {
        private val tssAddressApis = TTSRetrofitClient.getInstance().create(TTSAddressApis::class.java)
        val INSTANCE = AddressRepo(tssAddressApis)
    }

    private object TiffinRepoObjHolder {
        private val tssTiffinApis = TTSRetrofitClient.getInstance().create(TTStiffinApis::class.java)
        val INSTANCE = TiffinRepo(tssTiffinApis)
    }

    private object LoginRepoObjHolder {
        private val tssLoginApis = TTSRetrofitClient.getInstance().create(TTSLoginApis::class.java)
        val INSTANCE = LoginRepo(tssLoginApis)
    }

    private object FirebaseRepoObjHolder {
        private val tssfirebasApis = TTSRetrofitClient.getInstance().create(TTSFirebseApis::class.java)
        val INSTANCE = FirebaseRepo(tssfirebasApis)
    }

    private object otpRepoObjHolder {
        private val tssotpApis = TTSRetrofitClient.getInstance().create(TTSVerifyOtpApis::class.java)
        val INSTANCE = OtpRepo(tssotpApis)
    }

    private object HomeRepoObjHolder {
        private val tssHomeApis = TTSRetrofitClient.getInstance().create(TTSHomeApis::class.java)
        val INSTANCE = HomeRepo(tssHomeApis)
    }

    private object SignUpRepoObjHolder {
        private val ttsSignUpApis = TTSRetrofitClient.getInstance().create(TTSSignUpApis::class.java)
        val INSTANCE = SignUpRepo(ttsSignUpApis)
    }

    companion object {
        @JvmStatic
        fun getInstance(): TTSAPIRepository {
            val instance: TTSAPIRepository by lazy { HOLDER.INSTANCE }
            return instance
        }
    }

    fun getRequestPersonaList(tiffinSearchRequestTO: TiffinSearchRequestTO,
                              resultCallBack: ResultCallBack<List<TiffinPersonaListTO>>) {
        val networkHelper = NetworkHelper<List<TiffinPersonaListTO>>()
        tssApis.tiffinPersonaList(tiffinSearchRequestTO).enqueue(object : Callback<List<TiffinPersonaListTO>> {
            override fun onFailure(call: Call<List<TiffinPersonaListTO>>, t: Throwable) {
                resultCallBack.onFail(networkHelper.mapThrowableToTtsError(t as java.lang.Exception))
            }

            override fun onResponse(call: Call<List<TiffinPersonaListTO>>, response: Response<List<TiffinPersonaListTO>>) {
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


    fun getAddressRepo(): AddressRepo {
        val instance: AddressRepo by lazy { AddressRepoObjHolder.INSTANCE }
        return instance
    }

    fun getTiffinRepo(): TiffinRepo {
        val instance: TiffinRepo by lazy { TiffinRepoObjHolder.INSTANCE }
        return instance
    }

    fun getLoginRepo(): LoginRepo {
        val instance: LoginRepo by lazy { LoginRepoObjHolder.INSTANCE }
        return instance
    }

    fun getOtpRepo(): OtpRepo {
        val instance: OtpRepo by lazy { otpRepoObjHolder.INSTANCE }
        return instance
    }

    fun getHomeRepo(): HomeRepo {
        val instance: HomeRepo by lazy { HomeRepoObjHolder.INSTANCE }
        return instance
    }

    fun getSignUpRepo(): SignUpRepo {
        val instance: SignUpRepo by lazy { SignUpRepoObjHolder.INSTANCE }
        return instance
    }
}

interface ResultCallBack<Any> {
    fun onSuccess(response: Any)
    fun onFail(ttsError: TTSError)
}

