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
import com.samyotech.laundry.ModelClass.PopLaundryDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterPopularLaundriesBinding;
import com.samyotech.laundry.databinding.AdapterTopservicesBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.ui.activity.Schedule_Activity;
import com.samyotech.laundry.ui.activity.ServiceAcitivity;

import java.util.ArrayList;

public class PopularFullLaundriesAdapter extends RecyclerView.Adapter<PopularFullLaundriesAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterTopservicesBinding binding;
    Context kContext;
    ArrayList<PopLaundryDTO> popLaundryDTOArrayList;

    public PopularFullLaundriesAdapter(Context kContext, ArrayList<PopLaundryDTO> popLaundryDTOArrayList) {
        this.kContext = kContext;
        this.popLaundryDTOArrayList = popLaundryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_topservices, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Glide.with(kContext).load(popLaundryDTOArrayList.get(position).getImage()).placeholder(R.drawable.laundryshop).into(holder.binding.ivImage);

        holder.binding.title.setText(popLaundryDTOArrayList.get(position).getShop_name());
        holder.binding.location.setText(popLaundryDTOArrayList.get(position).getAddress());

        holder.binding.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(kContext, ServiceAcitivity.class);
                in.putExtra(Consts.SHOPDTO,popLaundryDTOArrayList.get(position));
                kContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popLaundryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterTopservicesBinding binding;

        public MyViewHolder(@NonNull AdapterTopservicesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
