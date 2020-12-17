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
import com.samyotech.laundry.databinding.AdapterOffersBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.OfferDTO;

import java.util.ArrayList;

public class OffersOtherAdapter extends RecyclerView.Adapter<OffersOtherAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterOffersBinding binding;
    Context kContext;
    ArrayList<OfferDTO> specialOfferPkgDTOArrayList;

    public OffersOtherAdapter(Context kContext, ArrayList<OfferDTO> specialOfferPkgDTOArrayList) {
        this.kContext = kContext;
        this.specialOfferPkgDTOArrayList = specialOfferPkgDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_offers, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        OfferDTO item = specialOfferPkgDTOArrayList.get(position);

        holder.binding.presentase.setText(item.getAmount() + item.getAmount_type());
        holder.binding.produk.setText(item.getService_name());
        holder.binding.rating.setText(item.getRating());
        holder.binding.arb.setRating(Float.parseFloat(item.getRating()));
        holder.binding.nama.setText(item.getShop_name());
        holder.binding.alamat.setText(item.getAddress());
        holder.binding.produk.setText(item.getDescription());
        String string = Consts.BASE_URL + item.getImage();
        Glide.with(kContext)
                .load(string)
                .error(R.drawable.offernewpa)
                .into(holder.binding.image);

        holder.binding.container.setOnClickListener(new View.OnClickListener() {
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
        AdapterOffersBinding binding;

        public MyViewHolder(@NonNull AdapterOffersBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
