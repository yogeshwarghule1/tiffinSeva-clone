package org.android.tiffinseva.homeflow.homelistingfragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.home_listing_fragment.*
import org.android.tiffinseva.BundleConstants
import org.android.tiffinseva.BundleConstants.Companion.TIFFIN_LIST_DECIDER
import org.android.tiffinseva.BundleConstants.Companion.TIFFIN_SEARCH_USER_TYPE_PROVIDER
import org.android.tiffinseva.BundleConstants.Companion.TIFFIN_SEARCH_USER_TYPE_REQUESTER
import org.android.tiffinseva.R
import org.android.tiffinseva.TheTiffinSevaApp
import org.android.tiffinseva.base.BaseFragment
import org.android.tiffinseva.networking.TTSError
import org.android.tiffinseva.networking.repository.ResultCallBack
import org.android.tiffinseva.recyclerview.TiffinListingAdapter
import org.android.tiffinseva.utils.PhoneCallClickListener
import timber.log.Timber

class HomeListingFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private var tiffinSwitchValue: Int = 0
    val tiffinSearchRequestTO = TiffinSearchRequestTO()
    var tiffinPersonaListTOList: ArrayList<TiffinPersonaListTO> = ArrayList()
    private lateinit var phoneCallClickListener:PhoneCallClickListener

    companion object {
        fun newInstance(tiffinListDecider: Int): HomeListingFragment {
            val fragment = HomeListingFragment()
            val bundle = Bundle().apply {
                putInt(TIFFIN_LIST_DECIDER, tiffinListDecider)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: HomeListingViewModel
    private lateinit var tiffinListingAdapter: TiffinListingAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        tiffinSwitchValue = arguments!!.getInt(TIFFIN_LIST_DECIDER)
        return inflater.inflate(R.layout.home_listing_fragment, container, false)
    }

    override fun getFragmentBaseView(): View? {
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cancelIcon = svTiffinProvider.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)
        viewModel = ViewModelProviders.of(this).get(HomeListingViewModel::class.java)

        rvTiffinProvider.layoutManager = LinearLayoutManager(activity)
        rvTiffinProvider.itemAnimator = DefaultItemAnimator()

        svTiffinProvider.setOnQueryTextListener(this)

        tiffinListingAdapter =
                TiffinListingAdapter(activity, false, tiffinPersonaListTOList,phoneCallClickListener)
        rvTiffinProvider.adapter = tiffinListingAdapter


        tiffinSearchRequestTO.city = TheTiffinSevaApp.getApplicationInstance().getCurrentCityName().get()
        if (tiffinSwitchValue == BundleConstants.TIFFIN_LIST_SWITCHER.TIFFIN_PROVIDER.ordinal) {
            tiffinSearchRequestTO.userType = TIFFIN_SEARCH_USER_TYPE_PROVIDER
            setToolBarTitle("Tiffin Provider")
        } else {
            tiffinSearchRequestTO.userType = TIFFIN_SEARCH_USER_TYPE_REQUESTER
            setToolBarTitle(getString(R.string.str_tiffin_requester))
        }
        setToolbarBackArrow(R.drawable.ic_arrow_back_black_24dp)
        setCityLayoutVisile(View.INVISIBLE)
        getHomeScreenData()
    }

    override fun onResume() {
        super.onResume()
        if (tiffinSwitchValue == BundleConstants.TIFFIN_LIST_SWITCHER.TIFFIN_PROVIDER.ordinal)
            setToolBarTitle("Tiffin Provider")
        else
            setToolBarTitle(getString(R.string.str_tiffin_requester))

        setToolbarBackArrow(R.drawable.ic_arrow_back_black_24dp)
        //setUpSideNavigationView(false)
    }

    private fun getHomeScreenData() {
        showProgressBar()
        viewModel.getTiffinProviderList(tiffinSearchRequestTO, homeAPIResultCallBack)
    }

    val homeAPIResultCallBack = object : ResultCallBack<List<TiffinPersonaListTO>> {
        override fun onSuccess(response: List<TiffinPersonaListTO>) {
            if (response.size > 0) {
                tvNoDataAvailable.visibility = View.GONE
                tiffinPersonaListTOList.clear()
                tiffinPersonaListTOList.addAll(response)
                tiffinListingAdapter.notifyDataSetChanged()
            } else {
                tvNoDataAvailable.visibility = View.VISIBLE
                if(tiffinSwitchValue == BundleConstants.TIFFIN_LIST_SWITCHER.TIFFIN_PROVIDER.ordinal) {
                    tvNoDataAvailable.text = getString(R.string.str_no_tiffin_provider)
                } else {
                    tvNoDataAvailable.text = getString(R.string.str_no_tiffin_requester)
                }

            }
            hideProgressBar()
        }

        override fun onFail(throwable: TTSError) {
            hideProgressBar()
            showError(throwable.errorMessage)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Timber.d("onQuery = %s", newText)
        tiffinListingAdapter.filter.filter(newText)
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        phoneCallClickListener = context as PhoneCallClickListener
    }
}
