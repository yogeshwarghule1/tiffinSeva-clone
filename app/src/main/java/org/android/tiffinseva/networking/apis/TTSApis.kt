package org.android.tiffinseva.networking.apis

import org.android.tiffinseva.ApiConstant.Companion.ALL_ADDRESS_BY_USER_ID_API
import org.android.tiffinseva.ApiConstant.Companion.CHANGE_CITY_AND_STATE_API
import org.android.tiffinseva.ApiConstant.Companion.CITY_INFO_API
import org.android.tiffinseva.ApiConstant.Companion.CREATE_USER_END_POINT
import org.android.tiffinseva.ApiConstant.Companion.GEN_OTP_END_POINT
import org.android.tiffinseva.ApiConstant.Companion.PIN_CODE_CITY_API
import org.android.tiffinseva.ApiConstant.Companion.SAVE_ADDRESS_API
import org.android.tiffinseva.ApiConstant.Companion.TIFFIN_HOME_END_POINT
import org.android.tiffinseva.ApiConstant.Companion.TIFFIN_PERSONA_LIST_END_POINT
import org.android.tiffinseva.ApiConstant.Companion.TIFFIN_SAVE
import org.android.tiffinseva.ApiConstant.Companion.UPDATE_FIREBASE_TOKEN
import org.android.tiffinseva.ApiConstant.Companion.USER_META_INFO
import org.android.tiffinseva.ApiConstant.Companion.VERIFY_OTP_END_POINT
import org.android.tiffinseva.UserType
import org.android.tiffinseva.homeflow.dashboardfragment.DashBoardRespTO
import org.android.tiffinseva.homeflow.homelistingfragment.TiffinPersonaListTO
import org.android.tiffinseva.homeflow.homelistingfragment.TiffinSearchRequestTO
import org.android.tiffinseva.loginflow.loginfragment.LoginRequestTO
import org.android.tiffinseva.loginflow.loginfragment.LoginResponseTO
import org.android.tiffinseva.loginflow.signupfragment.UserRespTO
import org.android.tiffinseva.loginflow.signupfragment.UserTO
import org.android.tiffinseva.loginflow.verifyotpfragment.VerifyOtpRequestTO
import org.android.tiffinseva.loginflow.verifyotpfragment.VerifyOtpResponseTO
import org.android.tiffinseva.manageaddress.addresstos.ChangeCityRequestTO
import org.android.tiffinseva.manageaddress.addresstos.ChangeCityResponseTO
import org.android.tiffinseva.manageaddress.saveaddress.SaveAddressRequestTO
import org.android.tiffinseva.manageaddress.saveaddress.SaveAddressResponseTO
import org.android.tiffinseva.networking.repository.tiffinrepo.AllAddressResponseTO
import org.android.tiffinseva.networking.tos.addresstos.CityPinCodeResponseTO
import org.android.tiffinseva.networking.tos.addresstos.CityResponseTO
import org.android.tiffinseva.pushnotif.FirebaseTokenRqTO
import org.android.tiffinseva.tiffinrequestreceive.TiffinResponseTO
import org.android.tiffinseva.tiffinrequestreceive.TiffinSaveTO
import org.android.tiffinseva.tiffinrequestreceive.UserMetaInfo
import retrofit2.Call
import retrofit2.http.*

interface TTSApis {

    @POST(TIFFIN_PERSONA_LIST_END_POINT)
    fun tiffinPersonaList(@Body tiffinSearchRequestTO: TiffinSearchRequestTO): Call<List<TiffinPersonaListTO>>

}

interface TTSSignUpApis {
    @POST(CREATE_USER_END_POINT)
    fun createUser(@Body userTO: UserTO?): Call<UserRespTO>
}

interface TTSHomeApis {
    @get:GET(TIFFIN_HOME_END_POINT)
    val tiffinHomeDetails: Call<DashBoardRespTO>
}

interface TTSVerifyOtpApis {
    @POST(VERIFY_OTP_END_POINT)
    fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequestTO?): Call<VerifyOtpResponseTO>
}

interface TTSLoginApis {
    @POST(GEN_OTP_END_POINT)
    fun genOtpUsingMobileNumber(@Body loginRequestTO: LoginRequestTO?): Call<LoginResponseTO>
}

interface TTSFirebseApis {
    @PUT(UPDATE_FIREBASE_TOKEN)
    fun updateFirebaseToken(@Body firebaseTokenRqTO: FirebaseTokenRqTO?): Call<UserRespTO>
}

interface TTSAddressApis {
    @GET(PIN_CODE_CITY_API)
    fun getCityAndPinCodeList(@Query("state") state: String): Call<List<CityPinCodeResponseTO>>

    // @Headers( "Content-Type: application/json;charset=UTF-8")
    @GET(CITY_INFO_API)
    fun getCityMetaInfo(@Query("pincode") state: String): Call<CityResponseTO>


    @POST(SAVE_ADDRESS_API)
    fun saveAddressApi(@Body saveAddressRequestTO: SaveAddressRequestTO): Call<SaveAddressResponseTO>

    @Headers( "Content-Type: application/json;charset=UTF-8")
    @PUT(CHANGE_CITY_AND_STATE_API)
    fun changeCityAndStateApi(@Body changeCityRequestTO: ChangeCityRequestTO): Call<ChangeCityResponseTO>

    @get:GET(ALL_ADDRESS_BY_USER_ID_API)
    val getAddressListByUserId: Call<AllAddressResponseTO>
}

interface TTStiffinApis {
    @POST(TIFFIN_SAVE)
    fun saveTiffinApi(@Body saveAddressRequestTO: TiffinSaveTO): Call<TiffinResponseTO>

    @GET(USER_META_INFO)
    fun getUserMetaInfo(@Path("userType") userType: UserType): Call<UserMetaInfo>

}