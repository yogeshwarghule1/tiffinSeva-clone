package org.android.tiffinseva.networking.repository

import org.android.tiffinseva.utils.AppSharedPrefRepository
import org.android.tiffinseva.utils.IDbProvider

class DataBaseImpl private constructor() : IDbProvider {

    private object HOLDER {
        val INSTANCE = DataBaseImpl()
    }

    companion object {
        fun getInstance(): DataBaseImpl {
            val instance: DataBaseImpl by lazy { HOLDER.INSTANCE }
            return instance
        }
    }

    override fun getSharedPreference(): AppSharedPrefRepository {
        return AppSharedPrefRepository.getInstance()
    }
}