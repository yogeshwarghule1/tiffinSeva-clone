package org.android.tiffinseva.base

import androidx.lifecycle.ViewModel
import org.android.tiffinseva.networking.repository.ApiRepoFactory
import org.android.tiffinseva.networking.repository.TTSAPIRepository

open class BaseViewModel : ViewModel() {

    fun getTTSAPIRepository(): TTSAPIRepository {
        return ApiRepoFactory.getInstance().getTssApiRepository()
    }
}