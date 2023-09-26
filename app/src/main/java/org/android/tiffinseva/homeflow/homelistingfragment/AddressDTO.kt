package org.android.tiffinseva.homeflow.homelistingfragment

import com.google.gson.annotations.SerializedName

data class AddressDTO(

    @field:SerializedName("pincode")
    var pincode: Any? = null,

    @field:SerializedName("country")
    var country: String? = null,

    @field:SerializedName("city")
    var city: String? = null,

    @field:SerializedName("addressLine1")
    var addressLine1: String? = null,

    @field:SerializedName("addressLine2")
    var addressLine2: String? = null,

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("state")
    var state: String? = null,

    @field:SerializedName("landmark")
    var landmark: String? = null
) {
    fun getSearchString(): String {
        return addressLine1 + " " + addressLine2 + " " + landmark + " " + city + " " + state
    }
}