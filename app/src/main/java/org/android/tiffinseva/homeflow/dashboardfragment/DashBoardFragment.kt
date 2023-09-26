package org.android.tiffinseva.homeflow.dashboardfragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_dash_board.*
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.R
import org.android.tiffinseva.TheTiffinSevaApp
import org.android.tiffinseva.base.BaseFragment
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.homeflow.HomeActivity
import org.android.tiffinseva.manageaddress.ChangeCityDialog
import org.android.tiffinseva.manageaddress.IDialogListener
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.utils.FirebaseTokenGenerationHelper
import org.android.tiffinseva.utils.ImageUtils
import org.android.tiffinseva.utils.vmFactoryProvider
import org.android.tiffinseva.viewhelper.IToolBarCityClickListener
import timber.log.Timber

class DashBoardFragment : BaseFragment(), View.OnClickListener, IToolBarCityClickListener, IDialogListener {
    override fun getFragmentBaseView(): View? {
        return view;
    }

    companion object {
        fun newInstance() = DashBoardFragment()
    }

    private lateinit var viewModel: DashBoardViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board, container, false)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_home))
        setToolbarBackArrow(R.drawable.ic_app_home)
        setCityLayoutVisile(View.VISIBLE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated called")
        btnTiffinProvider.setOnClickListener(this)
        btnTiffinSeekerRequest.setOnClickListener(this)
        ivTiffinSeekerHome.setOnClickListener(this)
        ivTiffinProviderHome.setOnClickListener(this)
        (activity as HomeActivity).getMainToolbarHelper().toolBarCityClickListener = this
        viewModel = ViewModelProviders.of(this, vmFactoryProvider.getViewModelFactory()).get(DashBoardViewModel::class.java)
        loadDashBoardData()
    }

    private fun loadDashBoardData() {
        showProgressBar()
        viewModel.homeRepo.getHomeDetails(dashBoardResultCallBack)
    }

    val dashBoardResultCallBack = object : ResultCallBack<DashBoardRespTO> {
        override fun onSuccess(response: DashBoardRespTO) {
            try {
                hideProgressBar()
                checkCurrentCity(response.lastCity)
                tvNumberOfTiffinProvider.text = response.tiffinProviderCount
                tvNumberOfTiffinSeekers.text = response.tiffinSeekerCount
                if (response.tiffinProviderBgImgPath != null)
                    ImageUtils.instance?.loadBgImage(
                        response.tiffinProviderBgImgPath,
                        ivTiffinProviderHome,
                        ContextCompat.getDrawable(context!!, R.drawable.iv_placeholder)
                    )

                if (response.tiffinSeekerBgImgPath != null)
                    ImageUtils.instance?.loadBgImage(
                        response.tiffinSeekerBgImgPath,
                        ivTiffinSeekerHome,
                        ContextCompat.getDrawable(context!!, R.drawable.iv_placeholder)
                    )
            }catch (ex:Exception){
                Timber.e(ex,"Exception ..")
            }
        }

        override fun onFail(ttsError: TTSError) {
            hideProgressBar()
            showError(ttsError.errorMessage)
        }
    }

    private fun checkCurrentCity(city: String?) {
        TheTiffinSevaApp.getApplicationInstance().setCurrentCityName(city?:"")
        val curCity = TheTiffinSevaApp.getApplicationInstance().getCurrentCityName().get() ?: ""
        if (curCity.isEmpty() || curCity.equals(getString(R.string.change_city))) {
            context?.let {
                openChangeCityDialog(it)
                showError(getString(R.string.please_set_city))
            }
        }
    }

    override fun onClick(view: View?) {
        var viewEvent: ViewEvent? = null
        if (view!!.id == R.id.ivTiffinSeekerHome) {
            viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.TIFFIN_SEEKER_FRAGMENT.ordinal, "")
        } else if (view.id == R.id.ivTiffinProviderHome) {
            viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.TIFFIN_PROVIDER_FRAGMENT.ordinal, "")
        } else if (view.id == R.id.btnTiffinSeekerRequest) {
            viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_REQUEST.ordinal, "")
        } else if (view.id == R.id.btnTiffinProvider) {
            viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.TIFFIN_FRAGMENT_SERVER.ordinal, "")
        }
        (activity as HomeActivity).replaceFragment(viewEvent!!)
    }

    override fun onCityChangeClick() {
        context?.let {
            openChangeCityDialog(it)
        }
    }

    private fun openChangeCityDialog(mContext: Context) {
        val changeCityDialog = ChangeCityDialog(mContext, this)
        changeCityDialog.show()
    }

    override fun onCitySuccessChange() {
        showSuccess(getString(R.string.city_chage))
    }

    override fun onDialogClose() {
        loadDashBoardData()
    }

}