package org.android.tiffinseva.utils

import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {

        val TIME_FORMAT = "dd/MMM/yyyy"
        fun readAssetsFile(inFile: String): String {
            var tContents = ""
            try {
                AppResourceProvider.getAssets()?.let {
                    val stream: InputStream = it.open(inFile)
                    val size: Int = stream.available()
                    val buffer = ByteArray(size)
                    stream.read(buffer)
                    stream.close()
                    tContents = String(buffer)
                }
            } catch (e: IOException) { // Handle exceptions here
            }
            return tContents
        }

        fun <T> convertStringToClass(jsonName: String, type: Class<T>): T? {
            try {
                val gson = Gson()
                return gson.fromJson(jsonName, type) as T
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


        fun <T> convertStringToClass(jsonName: String, type: Type): List<T>? {
            try {
                val gson = Gson()
                return gson.fromJson(jsonName, type) as List<T>
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun convertTimeInMillisToISO(timeInMillis: Long): String {
            val tz = TimeZone.getTimeZone("UTC")
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
            df.setTimeZone(tz)
            val date = Date(timeInMillis)
            val timeAsISO: String = df.format(date)
            return timeAsISO
        }

        fun convertTimeInMillisToHumanReadableFormat(timeInMillis: Long, format: String): String {
            val sdf = SimpleDateFormat(format, Locale.US)
            return sdf.format(timeInMillis)
        }
    }

}