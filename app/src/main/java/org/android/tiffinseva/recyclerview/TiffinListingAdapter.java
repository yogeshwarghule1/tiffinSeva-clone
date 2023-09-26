package org.android.tiffinseva.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.android.tiffinseva.R;
import org.android.tiffinseva.homeflow.homelistingfragment.TiffinPersonaListTO;
import org.android.tiffinseva.utils.PhoneCallClickListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TiffinListingAdapter extends RecyclerView.Adapter implements Filterable {
    private Context context;
    private boolean toggleFlag;
    private List<TiffinPersonaListTO> tiffinPersonaListTOList;
    private List<TiffinPersonaListTO> filteredTiffinPersonaListTOList;
    private PhoneCallClickListener phoneCallListener;

    public TiffinListingAdapter(Context context, boolean toggleFlag,
                                List<TiffinPersonaListTO> tiffinPersonaListTOList, PhoneCallClickListener phoneCallListener) {
        this.context = context;
        this.toggleFlag = toggleFlag;
        this.tiffinPersonaListTOList = tiffinPersonaListTOList;
        this.filteredTiffinPersonaListTOList = tiffinPersonaListTOList;
        this.phoneCallListener = phoneCallListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tiffin_prd_skr_item_row,parent,false);
        return new TiffinProviderListViewHolder(view,context,phoneCallListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TiffinProviderListViewHolder)holder).populateData(filteredTiffinPersonaListTOList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredTiffinPersonaListTOList.size();
    }

    public void setTiffinPersonaListTOList(List<TiffinPersonaListTO> tiffinPersonaListTOList) {
        this.tiffinPersonaListTOList = tiffinPersonaListTOList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSearch = charSequence.toString();
                Timber.e("Search query in filter is = "+charSearch);
                if (charSearch.isEmpty()) {
                    filteredTiffinPersonaListTOList = tiffinPersonaListTOList;
                } else {
                    List<TiffinPersonaListTO> resultList = new ArrayList<>();
                    for (TiffinPersonaListTO tiffinFilter : tiffinPersonaListTOList) {
                        if (tiffinFilter.getAddressDTO().getSearchString().toLowerCase().contains(charSearch.toLowerCase())) {
                            Timber.d("inside contains check = "+tiffinFilter);
                            resultList.add(tiffinFilter);
                        }
                    }
                    filteredTiffinPersonaListTOList = resultList;
                    Timber.d("new fileredTiffinList is = "+filteredTiffinPersonaListTOList);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredTiffinPersonaListTOList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredTiffinPersonaListTOList = (ArrayList<TiffinPersonaListTO>) filterResults.values;
                Timber.d("new fileredTiffinList is in publishResults is = "+filteredTiffinPersonaListTOList);
                notifyDataSetChanged();
            }
        };
    }
}
