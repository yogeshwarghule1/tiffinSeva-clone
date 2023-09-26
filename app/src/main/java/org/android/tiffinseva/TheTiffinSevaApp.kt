package org.android.tiffinseva
import android.app.Application
import androidx.databinding.ObservableField
import com.crashlytics.android.BuildConfig
import com.crashlytics.android.Crashlytics
import com.google.gson.reflect.TypeToken
import org.android.tiffinseva.networking.repository.ApiRepoFactory
import org.android.tiffinseva.networking.repository.DataBaseImpl
import org.android.tiffinseva.utils.AppSharedPrefRepository
import org.android.tiffinseva.utils.AppSharedPrefRepository.Companion.CURRENT_CITY
import org.android.tiffinseva.utils.ReleaseTree
import org.android.tiffinseva.utils.Utils
import org.android.tiffinseva.utils.dbProvider
import timber.log.Timber

class TheTiffinSevaApp : Application() {
    private val stateList = ArrayList<State>()
    private val currentCityName = ObservableField<String>()
    private var apiRepoFactory: ApiRepoFactory? = null

    companion object {
        private lateinit var theTiffinSevaApp: TheTiffinSevaApp
        fun getApplicationInstance(): TheTiffinSevaApp {
            return theTiffinSevaApp
        }
    }

    override fun onCreate() {
        super.onCreate()
        theTiffinSevaApp = this
        AppSharedPrefRepository.getInstance().initPref(this)
        DataBaseImpl.getInstance()
        initCrashlytics()
        apiRepoFactory = ApiRepoFactory.getInstance()
        loadStateAndStoreInPreference()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return String.format("Class:%s: Line: %s, Method: %s",
                            super.createStackElementTag(element), element.lineNumber, element.methodName)
                }
            })
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    private fun initCrashlytics() {
        Crashlytics.setUserIdentifier(dbProvider.getDataBase().getSharedPreference().getString(AppConstants.KEY_TOKEN, "EMPTY"));
    }

    fun loadStateAndStoreInPreference() {
        val listStr = Utils.readAssetsFile(AppConstants.STATE_LIST_FILE)
        val type = object : TypeToken<List<State>>() {}.type
        val states: List<State>? = Utils.convertStringToClass(listStr, type)
        states?.let {
            stateList.addAll(states)
        }
    }

    fun getStateList(): ArrayList<State> {
        return stateList
    }

    fun setCurrentCityName(city: String) {
        dbProvider.getDataBase().getSharedPreference().setString(CURRENT_CITY, city);
        currentCityName.set(dbProvider.getDataBase().getSharedPreference().getString(CURRENT_CITY, ""))
    }

    fun getCurrentCityName(): ObservableField<String> {
        return currentCityName
    }
}
