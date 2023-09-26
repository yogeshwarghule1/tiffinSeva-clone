package org.android.tiffinseva.networking.repository

class ApiRepoFactory private constructor() {

    private object HOLDER {
        val INSTANCE = ApiRepoFactory()
    }

    companion object {
        fun getInstance(): ApiRepoFactory {
            val instance: ApiRepoFactory by lazy { HOLDER.INSTANCE }
            return instance
        }
    }

    fun getTssApiRepository(): TTSAPIRepository {
        return TTSAPIRepository.getInstance()
    }
}