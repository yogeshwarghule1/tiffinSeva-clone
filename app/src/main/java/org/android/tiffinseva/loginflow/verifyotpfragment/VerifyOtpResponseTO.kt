package org.android.tiffinseva.loginflow.verifyotpfragment

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponseTO(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)