package org.android.tiffinseva.homeflow.homelistingfragment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TiffinPersonaListTO(

	@field:SerializedName("noOfTiffin")
	var noOfTiffin: Int? = null,

	@field:SerializedName("addressDTO")
	var addressDTO: AddressDTO? = null,

	@field:SerializedName("fullName")
	var fullName: String? = null,

	@field:SerializedName("mobileNo")
	var mobileNo: String? = null
):Serializable