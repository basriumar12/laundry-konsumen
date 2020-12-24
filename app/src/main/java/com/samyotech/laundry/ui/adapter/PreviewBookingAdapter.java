package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterPreviewBinding;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.ItemDetailsDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.AppFormat;

import java.util.ArrayList;

public class PreviewBookingAdapter extends RecyclerView.Adapter<PreviewBookingAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterPreviewBinding binding;
    Context kContext;
    ArrayList<ItemDetailsDTO> servicesDTOArrayList;
    CurrencyDTO currencyDTO;
    SharedPrefrence sharedPrefrence;

    public PreviewBookingAdapter(Context kContext, ArrayList<ItemDetailsDTO> servicesDTOArrayList, CurrencyDTO currencyDTO) {
        this.kContext = kContext;
        this.servicesDTOArrayList = servicesDTOArrayList;
        this.currencyDTO = currencyDTO;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_preview, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        sharedPrefrence = SharedPrefrence.getInstance(kContext);
        holder.binding.ctvName.setText(servicesDTOArrayList.get(position).getItem_name());
        holder.binding.ctvQuantity.setText(servicesDTOArrayList.get(position).getQuantity());
        holder.binding.ctvPrice.setText( sharedPrefrence.getCurrency() + " " + AppFormat.addDelimiter((int)Double.parseDouble(servicesDTOArrayList.get(position).getPrice()) + ""));
    }

    @Override
    public int getItemCount() {
        return servicesDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPreviewBinding binding;

        public MyViewHolder(@NonNull AdapterPreviewBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
