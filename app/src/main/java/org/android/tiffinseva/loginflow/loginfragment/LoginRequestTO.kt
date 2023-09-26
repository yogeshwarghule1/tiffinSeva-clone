package org.android.tiffinseva.loginflow.loginfragment


import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class LoginRequestTO(

	@field:SerializedName("mobileNo")
	var mobileNo: String? = null
):Serializable