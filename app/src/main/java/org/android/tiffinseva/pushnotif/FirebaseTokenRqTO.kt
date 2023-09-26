package org.android.tiffinseva.pushnotif

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FirebaseTokenRqTO(@field:SerializedName("firebaseToken")
                        var firebaseToken: String?): Serializable