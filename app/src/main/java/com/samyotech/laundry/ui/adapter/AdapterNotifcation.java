package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.ModelClass.NotificationDTO;
import com.samyotech.laundry.ModelClass.PopLaundryDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterNotificationBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.ui.activity.ServiceAcitivity;
import com.samyotech.laundry.utils.ProjectUtils;

import java.util.ArrayList;

public class AdapterNotifcation extends RecyclerView.Adapter<AdapterNotifcation.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterNotificationBinding binding;
    Context kContext;
    ArrayList<NotificationDTO> popLaundryDTOArrayList;

    public AdapterNotifcation(Context kContext, ArrayList<NotificationDTO> popLaundryDTOArrayList) {
        this.kContext = kContext;
        this.popLaundryDTOArrayList = popLaundryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_notification, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.binding.title.setText(popLaundryDTOArrayList.get(position).getTitle());
        holder.binding.ctvMessage.setText(popLaundryDTOArrayList.get(position).getMessage());
        holder.binding.ctvtime.setText(ProjectUtils.convertTimestampDateToTime(/*ProjectUtils.correctTimestamp(*/Long.parseLong(popLaundryDTOArrayList.get(position).getCreated_at())));
        Log.e("Adap", "onBindViewHolder: "+ProjectUtils.correctTimestamp(Long.parseLong(popLaundryDTOArrayList.get(position).getCreated_at())) );
    }

    @Override
    public int getItemCount() {
        return popLaundryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterNotificationBinding binding;

        public MyViewHolder(@NonNull AdapterNotificationBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
