package org.android.tiffinseva.fragmenthelper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.android.tiffinseva.base.BaseFragment

class FragmentHelper(val fragmentManager: FragmentManager) {

    fun replaceFragment(container: Int, fragment: BaseFragment, fragmentTag: String) {
        try {
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(container, fragment, fragmentTag)
            fragmentTransaction.addToBackStack(fragmentTag)
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBackStackEntryCount(): Int {
        return fragmentManager.backStackEntryCount
    }

    fun getTopFragment(): Fragment? {
        val count = fragmentManager.backStackEntryCount - 1
        val backEntry = fragmentManager.getBackStackEntryAt(count)
        val tag = backEntry.name
        val fragment = fragmentManager.findFragmentByTag(tag)
        return fragment
    }

}