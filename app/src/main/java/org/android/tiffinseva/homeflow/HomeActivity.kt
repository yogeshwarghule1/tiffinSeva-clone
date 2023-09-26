package org.android.tiffinseva.homeflow
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_newhome.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.BundleConstants
import org.android.tiffinseva.R
import org.android.tiffinseva.base.BaseActivity
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.homeflow.dashboardfragment.DashBoardFragment
import org.android.tiffinseva.homeflow.homelistingfragment.HomeListingFragment
import org.android.tiffinseva.manageaddress.ManageAddressFragment
import org.android.tiffinseva.tiffinrequestreceive.TiffinRequestReceiveFragment
import org.android.tiffinseva.utils.PhoneCallClickListener
import org.android.tiffinseva.viewhelper.MainToolBarHelper


class HomeActivity : BaseActivity(), PhoneCallClickListener, NavigationView.OnNavigationItemSelectedListener{
    private val PHONE_CALL_CODE: Int = 200
    private val HOME_HOLDER_FRAGMENT_TAG = "HOME_HOLDER_FRAGMENT_TAG"
    private val TIFFIN_SEEKER_FRAGMENT_TAG = "TIFFIN_SEEKER_FRAGMENT_TAG"
    private val TIFFIN_PROVIDER_FRAGMENT_TAG = "TIFFIN_PROVIDER_FRAGMENT_TAG"
    private val REQUEEST_TIFFIN_FRAGMEENT_TAG = "REQUEEST_TIFFIN_FRAGMEENT_TAG"

    open val mainToolBarHelper = MainToolBarHelper(this)
    companion object {
        val CHANGE_ADDRESS_FRAG_TAG = "CHANGE_ADDRESS_FRAG_TAG"
        /**
         *
         * @param context
         * @return
         */
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpNavigationView()
        fragmentHelper.replaceFragment(R.id.fragContainer,
                DashBoardFragment.newInstance(), HOME_HOLDER_FRAGMENT_TAG)
    }

    private fun setUpNavigationView() {
       // navigationView.setNavigationItemSelectedListener(this)
        mainToolBarHelper.initializeToolbar(mainToolbar)
//        mainToolbar.setNavigationOnClickListener {
//            if(fragmentHelper.getBackStackEntryCount() == 1) {
//                drawer_layout.openDrawer(GravityCompat.START)
//            } else {
//                onBackPressed()
//            }
//        }
    }

    fun getMainToolbarHelper() = mainToolBarHelper

    override fun replaceFragment(viewEvent: ViewEvent) {
        if (viewEvent.event == AppConstants.FRAGMENT_ID.TIFFIN_HOME_FRAGMENT.ordinal) {
            fragmentHelper.replaceFragment(R.id.fragContainer,
                    DashBoardFragment.newInstance(), HOME_HOLDER_FRAGMENT_TAG)
        } else if (viewEvent.event == AppConstants.FRAGMENT_ID.TIFFIN_PROVIDER_FRAGMENT.ordinal) {
            fragmentHelper.replaceFragment(R.id.fragContainer,
                    HomeListingFragment.newInstance(BundleConstants.TIFFIN_LIST_SWITCHER.TIFFIN_PROVIDER.ordinal), TIFFIN_PROVIDER_FRAGMENT_TAG)
        } else if (viewEvent.event == AppConstants.FRAGMENT_ID.TIFFIN_SEEKER_FRAGMENT.ordinal) {
            fragmentHelper.replaceFragment(R.id.fragContainer,
                    HomeListingFragment.newInstance(BundleConstants.TIFFIN_LIST_SWITCHER.TIFFIN_SEEKER.ordinal), TIFFIN_SEEKER_FRAGMENT_TAG)
        } else if (viewEvent.event == AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_REQUEST.ordinal) {
            fragmentHelper.replaceFragment(R.id.fragContainer,
                    TiffinRequestReceiveFragment.newInstance(AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_REQUEST.ordinal), REQUEEST_TIFFIN_FRAGMEENT_TAG)
        }else if (viewEvent.event == AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_SERVER.ordinal) {
            fragmentHelper.replaceFragment(R.id.fragContainer,
                    TiffinRequestReceiveFragment.newInstance(AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_SERVER.ordinal), REQUEEST_TIFFIN_FRAGMEENT_TAG)
        } else if (viewEvent.event == AppConstants.FRAGMENT_ID.CHANGE_ADDRESS_FRAG.ordinal) {
            fragmentHelper.replaceFragment(R.id.fragContainer,
                    ManageAddressFragment.getInstance(), CHANGE_ADDRESS_FRAG_TAG)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(!(fragmentHelper.getTopFragment() is DashBoardFragment)){
            onBackPressed()
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PHONE_CALL_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this,"Permission Granted please click on Call button",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,"Please provide phone call permission to make a call",Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }

    }

    fun makePhoneCall(mobNumber:String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$mobNumber")
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE), PHONE_CALL_CODE)
            return
        }
        startActivity(callIntent)
    }

    override fun getUserMobileNumber(mobNumber: String) {
        makePhoneCall(mobNumber)
    }

    /***
     * Need to implement related functionality
     */
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
}
