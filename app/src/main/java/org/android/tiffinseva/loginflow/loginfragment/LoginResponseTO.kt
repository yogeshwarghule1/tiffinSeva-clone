package org.android.tiffinseva.loginflow.loginfragment

import com.google.gson.annotations.SerializedName

data class LoginResponseTO(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)