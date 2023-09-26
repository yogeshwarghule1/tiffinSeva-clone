package org.android.tiffinseva.loginflow.verifyotpfragment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VerifyOtpRequestTO(

	@field:SerializedName("otp")
	var otp: String? = null,

	@field:SerializedName("mobileNo")
	var mobileNo: String? = null,

	@field:SerializedName("firebaseToken")
    var firebaseToken: String? = null
):Serializable