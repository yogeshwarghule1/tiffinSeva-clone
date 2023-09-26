package org.android.tiffinseva.manageaddress

import android.view.View
import androidx.lifecycle.ViewModelProviders
import org.android.tiffinseva.BR
import org.android.tiffinseva.R
import org.android.tiffinseva.base.BaseActivity
import org.android.tiffinseva.base.TtsBaseFragment
import org.android.tiffinseva.base.ViewEvent
import org.android.tiffinseva.manageaddress.AddressViewModel.Companion.REMOVE_FRAGMENT
import org.android.tiffinseva.utils.vmFactoryProvider

class ManageAddressFragment : TtsBaseFragment<AddressVM>() {

    companion object {
        fun getInstance(): ManageAddressFragment {
            return ManageAddressFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_manage_address
    }

    override fun getBindingVariableId(): Int {
        return BR.obj
    }

    override fun getViewModel(): AddressViewModel {
        return ViewModelProviders.of(this, vmFactoryProvider.getViewModelFactory())
                .get(AddressViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.add_new_addreess))
        setCityLayoutVisile(View.INVISIBLE)
    }

    override fun onTtsDataEvent(viewEvent: ViewEvent) {
        super.onTtsDataEvent(viewEvent)
        when (viewEvent.event) {
            REMOVE_FRAGMENT -> (activity as BaseActivity).onBackPressed()
        }
    }

}