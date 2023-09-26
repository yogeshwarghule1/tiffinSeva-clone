package org.android.tiffinseva.homeflow.dashboardfragment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DashBoardRespTO(

        @field:SerializedName("tiffinServerCount")
        val tiffinProviderCount: String = "",

        @field:SerializedName("tiffinServerImagePath")
        val tiffinProviderBgImgPath: String? = null,

        @field:SerializedName("tiffinRequesterImagePath")
        val tiffinSeekerBgImgPath: String? = null,

        @field:SerializedName("tiffinRequesterCount")
        val tiffinSeekerCount: String = "",

        val lastCity: String = "") : Serializable