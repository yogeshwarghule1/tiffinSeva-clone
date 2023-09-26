package org.android.tiffinseva.utils

import android.content.Context
import android.content.SharedPreferences
import org.android.tiffinseva.TheTiffinSevaApp

class AppSharedPrefRepository private constructor() {
    private var sharedPreferences: SharedPreferences? = null
    fun initPref(context: Context): Boolean {
        sharedPreferences = context.getSharedPreferences(TheTiffinSevaApp::class.java.simpleName, Context.MODE_PRIVATE)
        return true
    }

    fun setString(key: String, value: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences?.getString(key, null) ?: defaultValue
    }

    private object HOLDER {
        val INSTANCE = AppSharedPrefRepository()
    }

    companion object {
        @JvmStatic
        fun getInstance(): AppSharedPrefRepository {
            val instance: AppSharedPrefRepository by lazy { HOLDER.INSTANCE }
            return instance
        }


        const val CURRENT_CITY = "CURRENT_CITY"
    }
}