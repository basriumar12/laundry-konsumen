package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterAddressBinding;
import com.samyotech.laundry.model.AddressDTO;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterAddressBinding binding;
    Context kContext;
    ArrayList<AddressDTO> popLaundryDTOArrayList;

    public AddressAdapter(Context kContext, ArrayList<AddressDTO> popLaundryDTOArrayList) {
        this.kContext = kContext;
        this.popLaundryDTOArrayList = popLaundryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_address, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.address.setText(popLaundryDTOArrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return popLaundryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterAddressBinding binding;

        public MyViewHolder(@NonNull AdapterAddressBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
