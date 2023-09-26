package org.android.tiffinseva.networking

import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.AppConstants.Companion.HTTP_EXCEPTION
import retrofit2.Response

class NetworkHelper<Any> {
    private val ttsError = TTSError(HTTP_EXCEPTION, "something went wrong")
    private var networkResponse: Response<Any>? = null

    init {
        setResponseCode()
    }

    fun setResponse(networkResponse: Response<Any>) {
        this.networkResponse = networkResponse
    }

    fun setResponseCode() {
        if (networkResponse?.code() == AppConstants.HTTP_OK) {
            ttsError.responseCode = AppConstants.HTTP_OK
        } else if (networkResponse?.code() == AppConstants.HTTP_CREATED) {
            ttsError.responseCode = AppConstants.HTTP_CREATED
        } else if (networkResponse?.code() == AppConstants.HTTP_UNAUTHORISED) {
            ttsError.responseCode = AppConstants.HTTP_UNAUTHORISED
        } else if (networkResponse?.code() == AppConstants.HTTP_FORBIDDEN) {
            ttsError.responseCode = AppConstants.HTTP_FORBIDDEN
        } else if (networkResponse?.code() == AppConstants.HTTP_NOT_FOUND) {
            ttsError.responseCode = AppConstants.HTTP_NOT_FOUND
        }
    }

    fun mapThrowableToTtsError(exception: Exception): TTSError {
        ttsError.exception = exception
        ttsError.errorMessage = "something went wrong"
        if (exception is TTSInValidResponseCodeException)
            ttsError.errorMessage = exception.errorMessage
        ttsError.responseCode = HTTP_EXCEPTION
        return ttsError
    }

    fun isValidResponse(): Boolean {
        return networkResponse?.code() == AppConstants.HTTP_OK || networkResponse?.code() == AppConstants.HTTP_CREATED
    }
}

class TTSError(var responseCode: Int, var errorMessage: String) : Exception(errorMessage) {
    var exception: Exception? = null
    override fun toString(): String {
        return "TTSError(responseCode=$responseCode, errorMessage='$errorMessage', exception=${exception.toString()})"
    }
}


class TTSInValidResponseCodeException(var responseCode: Int, var errorMessage: String) : Exception(errorMessage) {
    var exception: Exception? = null
    override fun toString(): String {
        return "TTSError(responseCode=$responseCode, errorMessage='$errorMessage', exception=${exception.toString()})"
    }
}