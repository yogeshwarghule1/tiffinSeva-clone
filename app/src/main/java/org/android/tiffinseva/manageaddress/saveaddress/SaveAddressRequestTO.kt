package org.android.tiffinseva.manageaddress.saveaddress

data class SaveAddressRequestTO(val addressLine1: String, val addressLine2: String, val landmark: String
                                , val state: String, val city: String,
                                val pincode: String, val country: String = "India")