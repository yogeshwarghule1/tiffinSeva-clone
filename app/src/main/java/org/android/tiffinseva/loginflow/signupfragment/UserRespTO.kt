package org.android.tiffinseva.loginflow.signupfragment

import com.google.gson.annotations.SerializedName

data class UserRespTO(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)