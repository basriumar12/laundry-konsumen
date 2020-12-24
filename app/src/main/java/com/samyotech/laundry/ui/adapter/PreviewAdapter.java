package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterPreviewBinding;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.ItemServiceDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.AppFormat;

import java.util.ArrayList;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterPreviewBinding binding;
    Context kContext;
    ArrayList<ItemServiceDTO> servicesDTOArrayList;
    CurrencyDTO currencyDTO;
    private SharedPrefrence prefrence;

    public PreviewAdapter(Context kContext, ArrayList<ItemServiceDTO> servicesDTOArrayList, CurrencyDTO currencyDTO) {
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
        int i = 0;
        prefrence = SharedPrefrence.getInstance(kContext);

        i = Integer.parseInt(servicesDTOArrayList.get(position).getCount());
        if (i > 0) {
            holder.binding.ctvName.setText(servicesDTOArrayList.get(position).getItem_name()/*+"("+servicesDTOArrayList.get(position).getService_name()+")"*/);
            holder.binding.ctvQuantity.setText(servicesDTOArrayList.get(position).getCount());
//            holder.binding.ctvCategory.setText(servicesDTOArrayList.get(position).getService_name());
            holder.binding.ctvPrice.setText(prefrence.getCurrency() + " " + AppFormat.addDelimiter(servicesDTOArrayList.get(position).getPrice()) + " / " + servicesDTOArrayList.get(position).getType());
        } else holder.binding.clCard.setVisibility(View.GONE);
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
