package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterServicesBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.ServicesDTO;
import com.samyotech.laundry.ui.activity.ServiceActivity;

import java.util.ArrayList;

public class TopServiceAdapter extends RecyclerView.Adapter<TopServiceAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterServicesBinding binding;
    Context kContext;
    ArrayList<ServicesDTO> servicesDTOArrayList;

    public TopServiceAdapter(Context kContext, ArrayList<ServicesDTO> servicesDTOArrayList) {
        this.kContext = kContext;
        this.servicesDTOArrayList = servicesDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_services, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final ServicesDTO item = servicesDTOArrayList.get(position);
        Glide.with(kContext).load(Consts.BASE_URL + item.getImage())
                .placeholder(R.drawable.laundryshop)
                .into(holder.binding.servImage);

        holder.binding.servName.setText(item.getService_name());
        holder.binding.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(kContext, ServiceActivity.class);
                in.putExtra(Consts.SERVICEDTO, item);
                kContext.startActivity(in);

            }
        });

    }

    @Override
    public int getItemCount() {
        return servicesDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterServicesBinding binding;

        public MyViewHolder(@NonNull AdapterServicesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
