package org.android.tiffinseva.loginflow.loginfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.custom_edittext.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.R
import org.android.tiffinseva.base.BaseFragment
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.loginflow.LoginActivity
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.utils.TssDebounceOnClickListener
import org.android.tiffinseva.utils.vmFactoryProvider
import timber.log.Timber

class LoginFragment : BaseFragment() {
    private lateinit var loginViewModel: LoginViewModel
    private val loginRequestTO = LoginRequestTO()

    companion object {
        fun getInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun getFragmentBaseView(): View? {
        return view
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel = ViewModelProviders.of(this, vmFactoryProvider.getViewModelFactory())
                .get(LoginViewModel::class.java)
        val generateOtpButtonOnClickListener =
                GenerateOtpButtonOnClickListener(AppConstants.BUTTON_CLICK_DEBOUNCE_INTERVAL)
        btnGenerateOtp.setOnClickListener(generateOtpButtonOnClickListener)
        tvSingupText.setOnClickListener(generateOtpButtonOnClickListener)
    }

    inner class GenerateOtpButtonOnClickListener(btnClickInterval: Long) :
            TssDebounceOnClickListener(btnClickInterval) {

        override fun debounceOnClick(view: View?) {
            if (view!!.id == R.id.tvSingupText) {
                val viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.SIGNUP_FRAGMENT.ordinal, " ")
                (activity as LoginActivity).replaceFragment(viewEvent)
            } else if (view!!.id == R.id.btnGenerateOtp) {
                if (editTextLayout.editTextview.text.length < 10) {
                    editTextLayout.editTextview.error = "enter valid number"
                    return
                }
                Timber.d("entered mobile number is " + editTextLayout.editTextview.text.toString())
                generateOtpFromMobNumber("91" + editTextLayout.editTextview.text.toString())
            }
        }

    }

    val generateAPIResultCallBack = object : ResultCallBack<LoginResponseTO> {
        override fun onSuccess(response: LoginResponseTO) {
            hideProgressBar()
            val viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.OTP_FRAGMENT.ordinal, loginRequestTO)
            (activity as LoginActivity).replaceFragment(viewEvent)
        }

        override fun onFail(throwable: TTSError) {
            hideProgressBar()
            showError(throwable.errorMessage)
        }
    }

    fun generateOtpFromMobNumber(mobNum: String) {
        hideKeyboard(context)
        showProgressBar()
        loginRequestTO.mobileNo = mobNum
        loginViewModel.generateOtp(loginRequestTO, generateAPIResultCallBack)
    }
}