package org.android.tiffinseva.loginflow.verifyotpfragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.otp_verification_fragment.*
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.R
import org.android.tiffinseva.base.BaseFragment
import org.android.tiffinseva.homeflow.HomeActivity
import org.android.tiffinseva.loginflow.loginfragment.LoginRequestTO
import org.android.tiffinseva.loginflow.loginfragment.LoginResponseTO
import org.android.tiffinseva.loginflow.loginfragment.LoginViewModel
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.utils.AppSharedPrefRepository
import org.android.tiffinseva.utils.FirebaseTokenGenerationHelper
import org.android.tiffinseva.utils.TssDebounceOnClickListener
import org.android.tiffinseva.utils.vmFactoryProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit

class OtpVerificationFragment : BaseFragment(), TextWatcher, View.OnKeyListener, FirebaseTokenGenerationHelper.IFirebaseToken {
    private lateinit var loginRequestTO: LoginRequestTO
    private var otpCountDownTimer: CountDownTimer? = null

    companion object {
        fun newInstance(loginRequestTO: LoginRequestTO): OtpVerificationFragment {
            val fragment = OtpVerificationFragment()
            val bundle = Bundle().apply {
                putSerializable(AppConstants.INTENT_TRIGGER_OTP_TO, loginRequestTO)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: OtpVerificationViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loginRequestTO =
                arguments?.getSerializable(AppConstants.INTENT_TRIGGER_OTP_TO) as LoginRequestTO
        Timber.d("loginRequestTO passed from LoginFragment is $loginRequestTO")
        FirebaseTokenGenerationHelper(this).getFirebaseInstance();
        return inflater.inflate(R.layout.otp_verification_fragment, container, false)
    }

    override fun getFragmentBaseView(): View? {
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showKeyboard(context)
        viewModel = ViewModelProviders.of(this, vmFactoryProvider.getViewModelFactory()).get(OtpVerificationViewModel::class.java)
        loginViewModel = ViewModelProviders.of(this, vmFactoryProvider.getViewModelFactory()).get(LoginViewModel::class.java)
        etOtp1.addTextChangedListener(this)
        etOtp1.setOnKeyListener(this)
        etOtp2.addTextChangedListener(this)
        etOtp2.setOnKeyListener(this)
        etOtp3.addTextChangedListener(this)
        etOtp3.setOnKeyListener(this)
        etOtp4.addTextChangedListener(this)
        etOtp4.setOnKeyListener(this)
        etOtp5.addTextChangedListener(this)
        etOtp5.setOnKeyListener(this)
        etOtp6.addTextChangedListener(this)
        etOtp6.setOnKeyListener(this)

        val verifyOtpBtnOnClickListener =
                VerifyOtpBtnOnClickListener(AppConstants.BUTTON_CLICK_DEBOUNCE_INTERVAL)
        btnVerifyOtp.setOnClickListener(verifyOtpBtnOnClickListener)
        tvResendOtp.setOnClickListener(verifyOtpBtnOnClickListener)
        startCountDownTimer(AppConstants.COUNTDOWN_TIME)

    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        if (etOtp1.text.length == 1) {
            etOtp2.requestFocus()
        } else {
            etOtp1.requestFocus()
            return
        }
        if (etOtp2.text.length == 1) {
            etOtp3.requestFocus()
        } else {
            etOtp2.requestFocus()
            return
        }
        if (etOtp3.text.length == 1) {
            etOtp4.requestFocus()
        } else {
            etOtp3.requestFocus()
            return
        }
        if (etOtp4.text.length == 1) {
            etOtp5.requestFocus()
        } else {
            etOtp4.requestFocus()
            return
        }
        if (etOtp5.text.length == 1) {
            etOtp6.requestFocus()
        } else {
            etOtp5.requestFocus()
            return
        }

        if (etOtp1.text.length == 1 && etOtp2.text.length == 1 && etOtp3.text.length == 1 &&
                etOtp4.text.length == 1 && etOtp5.text.length == 1 && etOtp6.text.length == 1
        ) {
            val mgr =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mgr.hideSoftInputFromWindow(etOtp6.windowToken, 0)
        }
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        Log.d("OtpVerificationFragment","key event is ="+p1)
        //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
        if (p1 == KeyEvent.KEYCODE_DEL) { //this is for backspace
            if (p0!!.id == R.id.etOtp1) {
                etOtp1.requestFocus()
            } else if (p0.id == R.id.etOtp2) {
                etOtp2.requestFocus()
            } else if (p0.id == R.id.etOtp3) {
                etOtp3.requestFocus()
            } else if (p0.id == R.id.etOtp4) {
                etOtp4.requestFocus()
            } else if (p0.id == R.id.etOtp5) {
                etOtp5.requestFocus()
            } else if (p0.id == R.id.etOtp6) {
                etOtp6.requestFocus()
            }
        }
        return false
    }

    inner class VerifyOtpBtnOnClickListener(btnClickInterval: Long) :
            TssDebounceOnClickListener(btnClickInterval) {

        override fun debounceOnClick(view: View?) {
            if (view!!.id == R.id.btnVerifyOtp) {
                if (etOtp1.text.length == 0 && etOtp2.text.length == 0 && etOtp3.text.length == 0 &&
                        etOtp4.text.length == 0 && etOtp5.text.length == 0 && etOtp6.text.length == 0) {
                    hideKeyboard(activity)
                    showError("please enter all the digits of otp")
                    return
                }
                val otpDigits = etOtp1.text.toString() + etOtp2.text.toString() +
                        etOtp3.text.toString() + etOtp4.text.toString() + etOtp5.text.toString() +
                        etOtp6.text.toString()
                Timber.d("entered otp digits are = $otpDigits")
                val verifyOtpRequestTO = VerifyOtpRequestTO()
                verifyOtpRequestTO.mobileNo = loginRequestTO.mobileNo
                verifyOtpRequestTO.otp = otpDigits
                verifyOtpRequestTO.firebaseToken = AppSharedPrefRepository.getInstance().getString(AppConstants.FIREBASE_TOKEN, "")
                requestOtpVerification(verifyOtpRequestTO)
            } else if (view.id == R.id.tvResendOtp) {
                tvResendOtp.visibility = View.GONE
                tvDidNotGetOtp.visibility = View.GONE
                tvTimer.visibility = View.VISIBLE
                startCountDownTimer(AppConstants.COUNTDOWN_TIME)
                generateOtpFromMobNumber(loginRequestTO)
            }

        }

    }

    val submitOptResultCallBack = object : ResultCallBack<VerifyOtpResponseTO> {
        override fun onSuccess(response: VerifyOtpResponseTO) {
            if (otpCountDownTimer != null) {
                otpCountDownTimer!!.cancel()
                otpCountDownTimer = null
            }
            AppSharedPrefRepository.getInstance().setString(AppConstants.KEY_TOKEN, response.token ?: "")
            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
            hideProgressBar()
        }

        override fun onFail(throwable: TTSError) {
            showError(throwable.errorMessage)
            hideKeyboard(context)
            hideProgressBar()
        }
    }

    fun requestOtpVerification(verifyOtpRequestTO: VerifyOtpRequestTO) {
        showProgressBar()
        viewModel.otpRepo.verifyOtp(verifyOtpRequestTO, submitOptResultCallBack)
    }

    fun startCountDownTimer(time: Long?) {
        otpCountDownTimer = object : CountDownTimer(time!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(
                                1
                        )
                )
                println(hms)
                tvTimer?.let {
                    tvTimer.text = "" + hms
                }
            }

            override fun onFinish() {
                tvResendOtp.visibility = View.VISIBLE
                tvDidNotGetOtp.visibility = View.VISIBLE
                tvTimer.visibility = View.GONE

            }
        }.start()
    }


    private val generateAPIResultCallBack = object : ResultCallBack<LoginResponseTO> {
        override fun onSuccess(response: LoginResponseTO) {
            hideProgressBar()
            showSuccess("Otp resend successfull")
        }

        override fun onFail(throwable: TTSError) {
            hideProgressBar()
            showError(throwable.errorMessage)
        }
    }

    fun generateOtpFromMobNumber(loginRequestTO: LoginRequestTO) {
        showProgressBar()
        hideKeyboard(context)
        loginViewModel.generateOtp(loginRequestTO, generateAPIResultCallBack)
    }

    override fun onTokenChange(token: String?) {
        Timber.d("TOKEN FROM CALLBACK%s", token)
        AppSharedPrefRepository.getInstance().setString(AppConstants.FIREBASE_TOKEN, token!!)
    }
}
