package org.android.tiffinseva.homeflow.homelistingfragment

import org.android.tiffinseva.base.BaseViewModel
import org.android.tiffinseva.networking.repository.ResultCallBack

class HomeListingViewModel : BaseViewModel() {

    fun getTiffinProviderList(tiffinSearchRequestTO: TiffinSearchRequestTO,
                              resultCallBack: ResultCallBack<List<TiffinPersonaListTO>>) {
        return getTTSAPIRepository().getRequestPersonaList(tiffinSearchRequestTO, resultCallBack)
    }
}
