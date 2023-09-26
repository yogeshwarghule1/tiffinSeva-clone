package org.android.tiffinseva.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import org.android.tiffinseva.R
import org.android.tiffinseva.fragmenthelper.FragmentHelper
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {
    lateinit var fragmentHelper: FragmentHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHelper = FragmentHelper(supportFragmentManager)
    }

    abstract fun replaceFragment(viewEvent: ViewEvent)

    fun <T> replaceFragment(bundle: Bundle, clazz: Class<T>) {
        val fragment = clazz.newInstance() as BaseFragment
        if(fragment is BaseFragment){
            fragment.arguments = bundle
            fragmentHelper.replaceFragment(R.id.fragContainer, fragment, clazz.simpleName.toUpperCase() + "_TAG")
        } else {
            Timber.d("Extend fragments to BaseFragment")
        }
    }

    override fun onBackPressed() {
        if (fragmentHelper.getBackStackEntryCount() == 1)
            finish()
        else
            super.onBackPressed()
    }
}