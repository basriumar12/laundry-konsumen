package com.samyotech.laundry.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterBookingBinding;
import com.samyotech.laundry.databinding.DailogCancelOrderBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.OrderListDTO;
import com.samyotech.laundry.ui.fragment.BookingFragment;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private final String TAG = BookingAdapter.class.getSimpleName();
    LayoutInflater layoutInflater;
    AdapterBookingBinding binding;
    Context kContext;
    ArrayList<OrderListDTO> servicesDTOArrayList;
    BookingFragment bookingFragment;
    CurrencyDTO currencyDTO;
    private Dialog dialog;
    private ArrayList<OrderListDTO> objects = null;

    public BookingAdapter(Context kContext, ArrayList<OrderListDTO> objects, BookingFragment bookingFragment, CurrencyDTO currencyDTO) {
        this.kContext = kContext;
        this.currencyDTO = currencyDTO;
        this.bookingFragment = bookingFragment;
        this.objects = objects;
        this.servicesDTOArrayList = new ArrayList<OrderListDTO>();
        this.servicesDTOArrayList.addAll(objects);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_booking, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final OrderListDTO item = objects.get(position);

        holder.binding.namaToko.setText(item.getShop_name());
        if (item.getOrder_status().equals("5")) {
            Glide.with(kContext)
                    .load(R.drawable.icon_green_check)
                    .into(holder.binding.status);
        } else {
            Glide.with(kContext)
                    .load(R.drawable.icon_orange_reload)
                    .into(holder.binding.status);
        }
        holder.binding.layanan.setText(item.getService_name());
        holder.binding.diterima.setText(item.getPickup_date() + " " + item.getPickup_time());
        holder.binding.dikirim.setText(item.getDelivery_date() + " " + item.getDelivery_time());
        holder.binding.harga.setText(currencyDTO.getCurrency_symbol() + " " + item.getPrice());

//        holder.binding.selengkapnyaBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(kContext, OrderDetails.class);
//                in.putExtra(Consts.ORDERLISTDTO, item);
//                kContext.startActivity(in);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void dialogCancel(final int pos) {
        dialog = new Dialog(kContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final DailogCancelOrderBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(kContext), R.layout.dailog_cancel_order, null, false);
        dialog.setContentView(binding1.getRoot());
        ///dialog.getWindow().setBackgroundDrawableResource(R.color.black);

        dialog.show();
        dialog.setCancelable(false);
        binding1.cbCancelDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding1.cbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(objects.get(pos).getOrder_id());
                dialog.dismiss();

            }
        });

    }

    private void cancelOrder(String orderid) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.ORDER_ID, orderid);
        new HttpsRequest(Consts.ORDERCANCEL, params, kContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {
                    bookingFragment.getAllBookings();
                    notifyDataSetChanged();
                    bookingFragment.getAllBookings();
                } else {
                    ProjectUtils.showToast(kContext, msg);
                }
            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(servicesDTOArrayList);
        } else {
            for (OrderListDTO appliedJobDTO : servicesDTOArrayList) {
                if (appliedJobDTO.getShop_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    objects.add(appliedJobDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterBookingBinding binding;

        public MyViewHolder(@NonNull AdapterBookingBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }


}
