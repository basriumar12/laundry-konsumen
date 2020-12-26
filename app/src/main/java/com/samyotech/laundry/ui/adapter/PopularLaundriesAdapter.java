package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterPopularLaundriesBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.ui.activity.ShopAcitivity;

import java.util.ArrayList;

public class PopularLaundriesAdapter extends RecyclerView.Adapter<PopularLaundriesAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterPopularLaundriesBinding binding;
    Context kContext;
    ArrayList<PopLaundryDTO> popLaundryDTOArrayList;

    public PopularLaundriesAdapter(Context kContext, ArrayList<PopLaundryDTO> popLaundryDTOArrayList) {
        this.kContext = kContext;
        this.popLaundryDTOArrayList = popLaundryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_popular_laundries, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final PopLaundryDTO item = popLaundryDTOArrayList.get(position);
        holder.binding.laundryName.setText(item.getShop_name());
        holder.binding.alamat.setText(item.getAddress());
        holder.binding.rating.setText(item.getRating());
        holder.binding.arb.setRating(Float.parseFloat(item.getRating()));

        String jenisMitra = "";
        if (item.getType().equalsIgnoreCase("1")) {
            jenisMitra = "Agen";
            holder.binding.laundryTypeContainer.setBackgroundColor(ContextCompat.getColor(kContext, R.color.yellow_rating));
        } else {
            jenisMitra = "Mitra";
            holder.binding.laundryTypeContainer.setBackgroundColor(ContextCompat.getColor(kContext, R.color.green));
        }
        holder.binding.laundryType.setText(jenisMitra);

        holder.binding.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(kContext, ShopAcitivity.class);
                in.putExtra(Consts.SHOPDTO, item);
                kContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popLaundryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPopularLaundriesBinding binding;

        public MyViewHolder(@NonNull AdapterPopularLaundriesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
