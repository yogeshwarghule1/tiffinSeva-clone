package org.android.tiffinseva.loginflow.signupfragment

import com.google.gson.annotations.SerializedName

data class UserTO(

	@field:SerializedName("firstName")
	var firstName: String? = null,

	@field:SerializedName("lastName")
	var lastName: String? = null,

	@field:SerializedName("currentAddressId")
	var currentAddressId: Int? = null,

	@field:SerializedName("emailId")
	var emailId: String? = null,

	@field:SerializedName("middleName")
	var middleName: String? = null,

	@field:SerializedName("mobileNo")
	var mobileNo: String? = null
)