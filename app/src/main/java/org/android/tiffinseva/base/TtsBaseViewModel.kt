package org.android.tiffinseva.base

import androidx.lifecycle.MutableLiveData

abstract class TtsBaseViewModel<T> : BaseViewModel() {

    private val ttsEventData = MutableLiveData<ViewEvent>()
    abstract fun onStart()

    fun sendTtsDataEvent(viewEvent: ViewEvent) {
        ttsEventData.value = viewEvent
    }

    fun showError(errorMessage: String) {
        ttsEventData.value = ViewEvent(ERROR_ID, errorMessage)
    }

    fun showSuccess(successMessage: String) {
        ttsEventData.value = ViewEvent(SUCCESS_ID, successMessage)
    }

    fun showProgressBar(message: String) {
        sendTtsDataEvent(ViewEvent(SHOW_PROGRESS_BAR, message))
    }

    fun hideProgressBar() {
        sendTtsDataEvent(ViewEvent(HIDE_PROGRESS_BAR, ""))
    }

    fun getTtsEventData() = ttsEventData

    abstract fun getScreenVm(): T
}