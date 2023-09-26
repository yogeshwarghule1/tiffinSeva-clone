package org.android.tiffinseva.loginflow

import android.os.Bundle
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.R
import org.android.tiffinseva.base.BaseActivity
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.loginflow.loginfragment.LoginFragment
import org.android.tiffinseva.loginflow.signupfragment.SignupFragment
import org.android.tiffinseva.loginflow.verifyotpfragment.OtpVerificationFragment
import org.android.tiffinseva.loginflow.loginfragment.LoginRequestTO

class LoginActivity : BaseActivity() {

    val LOGIN_FRAGMENT_TAG ="tag_login_fragment"
    private val OTP_VERIFICATION_FRAGMENT_TAG:String = "OTP_VERIFICATION_FRAGMENT_TAG"
    private val SIGN_UP_FRAGMENT_TAG:String = "SIGN_UP_FRAGMENT_TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        fragmentHelper.replaceFragment(R.id.frameContainer, LoginFragment.getInstance(), LOGIN_FRAGMENT_TAG)
    }

    override fun replaceFragment(viewEvent: ViewEvent) {
        when (viewEvent.event) {
            AppConstants.FRAGMENT_ID.LOGIN_FRAGMENT.ordinal -> fragmentHelper.replaceFragment(
                R.id.frameContainer, LoginFragment.getInstance(), LOGIN_FRAGMENT_TAG
            )

            AppConstants.FRAGMENT_ID.OTP_FRAGMENT.ordinal -> fragmentHelper.replaceFragment(
                R.id.frameContainer,
                OtpVerificationFragment.newInstance(viewEvent.data as LoginRequestTO),
                OTP_VERIFICATION_FRAGMENT_TAG
            )

            AppConstants.FRAGMENT_ID.SIGNUP_FRAGMENT.ordinal -> fragmentHelper.replaceFragment(
                R.id.frameContainer,
                SignupFragment.newInstance(),
                SIGN_UP_FRAGMENT_TAG
            )

            else -> {
                fragmentHelper.replaceFragment(
                    R.id.frameContainer, LoginFragment.getInstance(), LOGIN_FRAGMENT_TAG
                )
            }
        }
    }
}