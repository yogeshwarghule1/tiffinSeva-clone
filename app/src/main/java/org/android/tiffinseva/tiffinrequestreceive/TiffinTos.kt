package org.android.tiffinseva.tiffinrequestreceive

import org.android.tiffinseva.UserType

data class TiffinSaveTO(val noOfTiffin: Int, val tiffinDateTime: String,
                        val userType: UserType, val addressId: Int)

data class TiffinResponseTO(val code: Int, val message: String, val status: String)