package org.android.tiffinseva.tiffinrequestreceive

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_tiffin_request_receive.*
import org.android.tiffinseva.AppConstants
import org.android.tiffinseva.BR
import org.android.tiffinseva.BundleConstants.Companion.TIFFIN_USER_TYPE
import org.android.tiffinseva.R
import org.android.tiffinseva.base.BaseActivity
import org.android.tiffinseva.base.TtsBaseFragment
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.tiffinrequestreceive.TiffinRequestReceiveViewModel.Companion.ADD_NEW_ADDRESS_FRAGMENT
import org.android.tiffinseva.tiffinrequestreceive.TiffinRequestReceiveViewModel.Companion.CLOSE_FRAGMENT
import org.android.tiffinseva.tiffinrequestreceive.TiffinRequestReceiveViewModel.Companion.SET_ADAPTER
import org.android.tiffinseva.tiffinrequestreceive.TiffinRequestReceiveViewModel.Companion.SHOW_DATE_PICKER
import org.android.tiffinseva.ttfrecyclerview.IItemBinding
import org.android.tiffinseva.ttfrecyclerview.TtsBaseAdapter
import org.android.tiffinseva.utils.Utils.Companion.TIME_FORMAT
import org.android.tiffinseva.utils.Utils.Companion.convertTimeInMillisToHumanReadableFormat
import org.android.tiffinseva.utils.Utils.Companion.convertTimeInMillisToISO
import org.android.tiffinseva.utils.vmFactoryProvider
import java.util.*


class TiffinRequestReceiveFragment : TtsBaseFragment<TiffinRequestReceiveVm>() {

    companion object {
        fun newInstance(tiffinUserType: Int): TiffinRequestReceiveFragment {
            val fragment = TiffinRequestReceiveFragment()
            val bundle = Bundle().apply {
                putInt(TIFFIN_USER_TYPE, tiffinUserType)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tiffin_request_receive
    }

    override fun getBindingVariableId(): Int {
        return BR.obj
    }

    override fun getViewModel(): TiffinRequestReceiveViewModel {
        return ViewModelProviders.of(this, vmFactoryProvider.getViewModelFactory())
                .get(TiffinRequestReceiveViewModel::class.java)
    }

    override fun onTtsDataEvent(viewEvent: ViewEvent) {
        super.onTtsDataEvent(viewEvent)
        when (viewEvent.event) {
            SET_ADAPTER -> setAdapter()
            SHOW_DATE_PICKER -> showDataPicker()
            ADD_NEW_ADDRESS_FRAGMENT -> addManageAddressFragment()
            CLOSE_FRAGMENT -> (context as BaseActivity).onBackPressed()
        }
    }

    private fun addManageAddressFragment() {
        val viewEvent = ViewEvent(AppConstants.FRAGMENT_ID.CHANGE_ADDRESS_FRAG.ordinal, " ")
        (activity as BaseActivity).replaceFragment(viewEvent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        readBundleData()
        setToolBarTitle(getToolbarTitle())
        setCityLayoutVisile(View.INVISIBLE)
        setToolbarBackArrow(R.drawable.ic_arrow_back_black_24dp)
        //setUpSideNavigationView(false)
        (mViewModel as TiffinRequestReceiveViewModel).getUserData()
    }

    private fun getToolbarTitle(): String {
        val userType = (mViewModel as TiffinRequestReceiveViewModel).tiffinUserType
        return if (userType == org.android.tiffinseva.UserType.SERVER)
            getString(R.string.tiffin_provide_btn)
        else
            getString(R.string.tiffin_request_btn)
    }

    private fun readBundleData() {
        arguments?.let {
            (mViewModel as TiffinRequestReceiveViewModel).setTiffinUserType(it.getInt(TIFFIN_USER_TYPE))
        }
    }

    private fun showDataPicker() {
        val myCalendar = Calendar.getInstance()
        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        DatePickerDialog(context!!, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateLabel(myCalendar: Calendar) {
        val humanReadableFormat = convertTimeInMillisToHumanReadableFormat(myCalendar.timeInMillis, TIME_FORMAT)
        val isoFormat = convertTimeInMillisToISO(myCalendar.timeInMillis)
        (mViewModel as TiffinRequestReceiveViewModel).setTiffinTime(humanReadableFormat)
        (mViewModel as TiffinRequestReceiveViewModel).setTiffinTimeInISO(isoFormat)
    }

    private fun setAdapter() {
        rvSavedAddress.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvSavedAddress.adapter = TtsBaseAdapter((mViewModel as TiffinRequestReceiveViewModel).tiffinVm.addressObservableList, mLayoutBindingListener)
    }

    val mLayoutBindingListener = object : IItemBinding<BaseAddress> {
        override fun getLayoutIdForPosition(baseAddress: BaseAddress): Int {
            return if (baseAddress is AddressVm)
                R.layout.item_saved_address
            else
                R.layout.item_add_new_address
        }

        override fun getBindingVariableId(baseAddress: BaseAddress): Int {
            return if (baseAddress is AddressVm)
                BR.obj
            else
                BR.obj
        }
    }
}