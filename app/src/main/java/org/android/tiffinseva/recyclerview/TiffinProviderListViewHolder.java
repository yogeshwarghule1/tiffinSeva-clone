package org.android.tiffinseva.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.android.tiffinseva.R;
import org.android.tiffinseva.homeflow.homelistingfragment.TiffinPersonaListTO;
import org.android.tiffinseva.utils.PhoneCallClickListener;

public class TiffinProviderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView ivPersonaImg;
    private TextView tvPersonaName, tvPersonaMobNumber, tvTiffinCount, tvAddressLine, tvCityAndState,tvLandmark;
    private ImageButton ibCall;
    private Context context;
    private TiffinPersonaListTO tiffinPersonaListTO;
    private PhoneCallClickListener phoneCallClickListener;
    private String mobNumber;

    public TiffinProviderListViewHolder(@NonNull View itemView, Context context, PhoneCallClickListener phoneCallClickListener) {
        super(itemView);
        this.context = context;
        this.phoneCallClickListener = phoneCallClickListener;
        ivPersonaImg = itemView.findViewById(R.id.ivPersonaImg);
        tvPersonaName = itemView.findViewById(R.id.tvPersonaName);
        tvPersonaMobNumber = itemView.findViewById(R.id.tvPersonaMobNumber);
        tvTiffinCount = itemView.findViewById(R.id.tvTiffinCount);
        ibCall = itemView.findViewById(R.id.ibCall);
        tvAddressLine = itemView.findViewById(R.id.tvAddressLine);
        tvCityAndState = itemView.findViewById(R.id.tvCityAndState);
        tvLandmark = itemView.findViewById(R.id.tvLandmark);
        ibCall.setOnClickListener(this);
    }

    public void populateData(TiffinPersonaListTO tiffinPersonaListTO) {
        this.tiffinPersonaListTO = tiffinPersonaListTO;
        try {
            if (tiffinPersonaListTO.getFullName() != null) {
                tvPersonaName.setText(tiffinPersonaListTO.getFullName());
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(String.valueOf(tiffinPersonaListTO.getFullName().charAt(0)),
                                Color.parseColor("#f49d00"));
                ivPersonaImg.setImageDrawable(drawable);
            }
            if (tiffinPersonaListTO.getMobileNo() != null) {
                mobNumber = tiffinPersonaListTO.getMobileNo().substring(2);
                tvPersonaMobNumber.setText(mobNumber);
            }
            if (tiffinPersonaListTO.getNoOfTiffin() != null) {
                tvTiffinCount.setText(String.valueOf(tiffinPersonaListTO.getNoOfTiffin()));
            }
            if (tiffinPersonaListTO.getAddressDTO() != null) {
                if (tiffinPersonaListTO.getAddressDTO().getAddressLine1() != null &&
                        !tiffinPersonaListTO.getAddressDTO().getAddressLine1().isEmpty()) {
                    tvAddressLine.setText(tiffinPersonaListTO.getAddressDTO().getAddressLine1());
                }
                if (tiffinPersonaListTO.getAddressDTO().getCity() != null && tiffinPersonaListTO.getAddressDTO().getState() != null) {
                    tvCityAndState.setText(tiffinPersonaListTO.getAddressDTO().getCity() + ", " +
                            tiffinPersonaListTO.getAddressDTO().getState());
                }
                if(tiffinPersonaListTO.getAddressDTO().getLandmark()!=null){
                    tvLandmark.setText(tiffinPersonaListTO.getAddressDTO().getLandmark());
                }
            }
        } catch (Exception ex) {
            Log.e("log", "populateData: exception", ex);
        }
    }

    @Override
    public void onClick(View view) {
        if (tiffinPersonaListTO.getMobileNo() != null) {
            phoneCallClickListener.getUserMobileNumber(mobNumber);
        }
    }
}
