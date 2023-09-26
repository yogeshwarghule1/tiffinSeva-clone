package org.android.tiffinseva.viewhelper

import android.content.Context
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.Observable
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_newhome.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import org.android.tiffinseva.R
import org.android.tiffinseva.TheTiffinSevaApp
import org.android.tiffinseva.base.BaseActivity

class MainToolBarHelper(val context: Context) {
    var toolBarCityClickListener: IToolBarCityClickListener? = null
    var mToolbar: Toolbar? = null
    //var mDrawerLayout: DrawerLayout? = null
    fun initializeToolbar(mToolbar: Toolbar) {
        this.mToolbar = mToolbar
      //  this.mDrawerLayout = drawerLayout;
        if (context is BaseActivity) {
            context.setSupportActionBar(mToolbar)
            context.supportActionBar?.let {
                it.title = ""
                it.setDisplayHomeAsUpEnabled(true)
                it.setDisplayShowHomeEnabled(true)
            }
        }
        setToolBarTitle(context.getString(R.string.str_home))
        setToolBarIcon(R.drawable.ic_menu)
        setCityChangeObserver(mToolbar)
        setCityClickListener(mToolbar)
        setCityName(mToolbar)
    }

    fun setToolBarIcon(iconId: Int) {
        if (context is BaseActivity) {
            context.supportActionBar?.let {
                it.setHomeAsUpIndicator(iconId)
            }
        }
    }

    fun setToolBarTitle(toolbarTitle: String) {
        mToolbar?.screenName?.text = toolbarTitle
    }

    fun setCityLayoutVisibility(visible: Int) {
        mToolbar!!.cityName.visibility = visible
    }

    private fun setCityClickListener(mToolbar: Toolbar) {
        mToolbar.cityName.setOnClickListener {
            toolBarCityClickListener?.onCityChangeClick()
        }
    }

    private fun setCityChangeObserver(mToolbar: Toolbar) {
        TheTiffinSevaApp.getApplicationInstance()
                .getCurrentCityName()
                .addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        setCityName(mToolbar)
                    }
                })
    }

    private fun setCityName(mToolbar: Toolbar) {
        mToolbar.cityName.text = TheTiffinSevaApp.getApplicationInstance().getCurrentCityName().get()
    }

//    fun setUpNavigationView(enabled: Boolean) {
//        val lockMode =
//            if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
//        this.mDrawerLayout!!.setDrawerLockMode(lockMode)
//    }
}

interface IToolBarCityClickListener {
    fun onCityChangeClick()
}