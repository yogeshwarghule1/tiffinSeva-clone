package org.android.tiffinseva.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer

abstract class TtsBaseFragment<Any> : BaseFragment() {

    var binding: ViewDataBinding? = null
    private var rootView: View? = null
    var mViewModel: TtsBaseViewModel<Any>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        binding?.setLifecycleOwner(this);
        rootView = binding?.root
        return rootView
    }

    private fun observeMutableLiveEventData() {
        mViewModel?.getTtsEventData()?.observe(viewLifecycleOwner, Observer {
            onTtsDataEvent(it)
        })
    }

    open fun onTtsDataEvent(viewEvent: ViewEvent) {
        when (viewEvent.event) {
            ERROR_ID -> showError(viewEvent.data as String)
            SUCCESS_ID -> showSuccess(viewEvent.data as String)
            SHOW_PROGRESS_BAR -> showProgressBar(viewEvent.data as String)
            HIDE_PROGRESS_BAR -> hideProgressBar()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = getViewModel()
        mViewModel = getViewModel()
        binding?.setVariable(getBindingVariableId(), mViewModel?.getScreenVm())
        mViewModel?.onStart()
        observeMutableLiveEventData()
    }

    override fun getFragmentBaseView(): View? {
        return rootView
    }

    abstract fun getLayoutId(): Int
    abstract fun getBindingVariableId(): Int
    abstract fun getViewModel(): TtsBaseViewModel<Any>

}