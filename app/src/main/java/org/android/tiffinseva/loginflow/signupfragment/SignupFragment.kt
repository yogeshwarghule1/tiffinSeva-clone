package org.android.tiffinseva.loginflow.signupfragment


import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.custom_edittext.view.*
import kotlinx.android.synthetic.main.signup_fragment.*
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.R
import org.android.tiffinseva.base.BaseFragment
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.loginflow.LoginActivity
import org.android.tiffinseva.loginflow.loginfragment.LoginRequestTO
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.utils.TssDebounceOnClickListener
import org.android.tiffinseva.utils.vmFactoryProvider
import timber.log.Timber


class SignupFragment : BaseFragment() {
    private lateinit var userTO:UserTO
    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signup_fragment, container, false)
    }

    override fun getFragmentBaseView(): View? {
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, vmFactoryProvider.getViewModelFactory()).get(SignupViewModel::class.java)
        val firstNameLength = arrayOfNulls<InputFilter>(1)
        firstNameLength[0] = InputFilter.LengthFilter(30)

        val lastNameLength = arrayOfNulls<InputFilter>(1)
        lastNameLength[0] = InputFilter.LengthFilter(30)
        tvFirstNameLabel.text = "First Name"
        tvLastNameLabel.text = "Last Name"

        etFirstNameSignup.filters = firstNameLength
        etLastNameSignup.filters = lastNameLength
        val signUpRequestClickListener =
                SignUpRequestClickListener(AppConstants.BUTTON_CLICK_DEBOUNCE_INTERVAL)
        btnSignUp.setOnClickListener(signUpRequestClickListener)

    }

    inner class SignUpRequestClickListener(clickInterval: Long) : TssDebounceOnClickListener(clickInterval) {

        override fun debounceOnClick(view: View?) {
            if(!validateFields()){
                Timber.e("validation failed")
                return
            }
            hideKeyboard(activity)
            showProgressBar()
            userTO = UserTO()
            userTO.firstName = etFirstNameSignup.text.toString()
            userTO.lastName = etLastNameSignup.text.toString()
            userTO.mobileNo = "91" + etMobNumber.editTextview.text.toString()
            createUserRequest(userTO)
        }

    }

    val signUpRequestResultListener = object:ResultCallBack<UserRespTO>{
        override fun onSuccess(response: UserRespTO) {
            hideProgressBar()
            val loginRequestTO = LoginRequestTO()
            loginRequestTO.mobileNo = userTO.mobileNo
            val viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.OTP_FRAGMENT.ordinal, loginRequestTO)
            (activity as LoginActivity).replaceFragment(viewEvent)
        }

        override fun onFail(ttsError: TTSError) {
            hideProgressBar()
           showError("something went wrong, please try again")
        }

    }
    fun createUserRequest(userTo: UserTO) {
        viewModel.sinRepo.createUserRequest(userTo,signUpRequestResultListener)
    }

    fun validateFields():Boolean{
        if(etFirstNameSignup.text.length == 0){
            etFirstNameSignup.error = "please enter first name"
            return false
        }
        if(etLastNameSignup.text.length == 0){
            etLastNameSignup.error = "please enter last name"
            return false
        }
        if(etMobNumber.editTextview.text.length <10 ){
            etMobNumber.editTextview.error = "please enter valid mobile number"
            return false
        }
        return true
    }
}
