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

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterNearBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.NearBYDTO;
import com.samyotech.laundry.ui.activity.ShopAcitivity;

import java.util.ArrayList;

public class LaundriesNearAdapter extends RecyclerView.Adapter<LaundriesNearAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterNearBinding binding;
    Context kContext;
    ArrayList<NearBYDTO> nearBYDTOArrayList;

    public LaundriesNearAdapter(Context kContext, ArrayList<NearBYDTO> nearBYDTOArrayList) {
        this.kContext = kContext;
        this.nearBYDTOArrayList = nearBYDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_near, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final NearBYDTO item = nearBYDTOArrayList.get(position);
        Glide.with(kContext)
                .load(Consts.BASE_URL + item.getShop_image())
                .placeholder(R.drawable.banner_img)
                .error(R.drawable.banner_img)
                .into(holder.binding.image);

        binding.namatoko.setText(item.getShop_name());
        binding.alamat.setText(item.getAddress());

        String jenisMitra = "";
        if (item.getType().equalsIgnoreCase("1")) {
            jenisMitra = "Agen";
            holder.binding.laundryTypeContainer.setBackgroundColor(ContextCompat.getColor(kContext, R.color.yellow_rating));
        } else {
            jenisMitra = "Mitra";
            holder.binding.laundryTypeContainer.setBackgroundColor(ContextCompat.getColor(kContext, R.color.green));
        }
        holder.binding.laundryType.setText(jenisMitra);

        holder.binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(kContext, ShopAcitivity.class);
                in.putExtra(Consts.NEARSHOPDTO, item);
                kContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        int showItem;
        int size = nearBYDTOArrayList.size();

        showItem = Math.min(size, 12);
        return showItem;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterNearBinding binding;

        public MyViewHolder(@NonNull AdapterNearBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
