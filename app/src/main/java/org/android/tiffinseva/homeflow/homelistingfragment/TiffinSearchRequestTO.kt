package org.android.tiffinseva.homeflow.homelistingfragment

import com.google.gson.annotations.SerializedName

data class TiffinSearchRequestTO(

	@field:SerializedName("pincode")
	var pincode: String? = null,

	@field:SerializedName("city")
	var city: String? = null,

	@field:SerializedName("tiffinDateTime")
	var tiffinDateTime: String? = null,

	@field:SerializedName("userType")
	var userType: String? = null
)