package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.SpecialOffersBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.SpecialOfferPkgDTO;

import java.util.ArrayList;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    SpecialOffersBinding binding;
    Context kContext;
    ArrayList<SpecialOfferPkgDTO> specialOfferPkgDTOArrayList;

    public SpecialOffersAdapter(Context kContext, ArrayList<SpecialOfferPkgDTO> specialOfferPkgDTOArrayList) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.binding.offer.setText(specialOfferPkgDTOArrayList.get(position).getDescription());
        holder.binding.ctvCode.setText(specialOfferPkgDTOArrayList.get(position).getPromocode());
        holder.binding.ctvOffer.setText(specialOfferPkgDTOArrayList.get(position).getOffer_name());
        Glide.with(kContext)
                .load(Consts.BASE_URL + specialOfferPkgDTOArrayList.get(position).getImage())
                .error(R.drawable.offernewpa)
                .into(holder.binding.acivOfferImage);

        holder.binding.ctvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(kContext, specialOfferPkgDTOArrayList.get(position).getPromocode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialOfferPkgDTOArrayList.size();
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Code", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SpecialOffersBinding binding;

        public MyViewHolder(@NonNull SpecialOffersBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
