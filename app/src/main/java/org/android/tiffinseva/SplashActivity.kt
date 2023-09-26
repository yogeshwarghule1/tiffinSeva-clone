package org.android.tiffinseva

import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import org.android.tiffinseva.base.BaseActivity
import org.android.tiffinseva.base.IntentManager
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.homeflow.HomeActivity
import org.android.tiffinseva.utils.AppSharedPrefRepository

class SplashActivity : BaseActivity() {

    val DURATION = 3 * 1000
    override fun replaceFragment(viewEvent: ViewEvent) {

    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            if (!TextUtils.isEmpty(AppSharedPrefRepository.getInstance().getString(AppConstants.KEY_TOKEN, ""))) {
                TheTiffinSevaApp.getApplicationInstance().setCurrentCityName(getString(R.string.change_city))
                startActivity(HomeActivity.createIntent(this));
            } else {
                TheTiffinSevaApp.getApplicationInstance().setCurrentCityName(getString(R.string.change_city))
                IntentManager.startLoginActivity(this)
                finish()
            }
        }, DURATION.toLong()
        )
    }

}