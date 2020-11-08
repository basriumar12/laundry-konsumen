package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.SpecialOffersBinding;
import com.samyotech.laundry.model.OfferDTO;

import java.util.ArrayList;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    SpecialOffersBinding binding;
    Context kContext;
    ArrayList<OfferDTO> specialOfferPkgDTOArrayList;

    public OffersAdapter(Context kContext, ArrayList<OfferDTO> specialOfferPkgDTOArrayList) {
        this.kContext = kContext;
        this.specialOfferPkgDTOArrayList = specialOfferPkgDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.special_offers, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.offer.setText(specialOfferPkgDTOArrayList.get(position).getOffer_name());
    }

    @Override
    public int getItemCount() {
        return specialOfferPkgDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SpecialOffersBinding binding;

        public MyViewHolder(@NonNull SpecialOffersBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
