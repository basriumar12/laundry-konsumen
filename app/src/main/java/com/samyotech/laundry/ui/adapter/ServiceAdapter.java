package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ServicesAdapterBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.ShopServicesDTO;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    ServicesAdapterBinding binding;
    Context kContext;
    ArrayList<ShopServicesDTO> servicesDTOArrayList;

    public ServiceAdapter(Context kContext, ArrayList<ShopServicesDTO> servicesDTOArrayList) {
        this.kContext = kContext;
        this.servicesDTOArrayList = servicesDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.services_adapter, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ShopServicesDTO item = servicesDTOArrayList.get(position);
        holder.binding.namaJasa.setText(item.getService_name());
        Glide.with(kContext).load(Consts.DEV_URL + item.getImage()).placeholder(R.drawable.icon_service_118).into(binding.image);
    }

    @Override
    public int getItemCount() {
        return servicesDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ServicesAdapterBinding binding;

        public MyViewHolder(@NonNull ServicesAdapterBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
