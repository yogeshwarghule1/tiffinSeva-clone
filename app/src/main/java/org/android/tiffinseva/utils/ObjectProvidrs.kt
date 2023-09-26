package org.android.tiffinseva.utils

import org.android.tiffinseva.viewmodels.ViewModelFactory
import org.android.tiffinseva.networking.repository.DataBaseImpl


interface IDbProvider {
    fun getSharedPreference(): AppSharedPrefRepository
}

interface IDataBaseProvider {
    fun getDataBase(): DataBaseImpl
}

object dbProvider : IDataBaseProvider {
    override fun getDataBase(): DataBaseImpl {
        return DataBaseImpl.getInstance()
    }
}

interface ITTSViewModelFactoryProvider {
    fun getViewModelFactory(): ViewModelFactory
}

object vmFactoryProvider : ITTSViewModelFactoryProvider {
    override fun getViewModelFactory(): ViewModelFactory {
        return ViewModelFactory.getInstance()
    }
}