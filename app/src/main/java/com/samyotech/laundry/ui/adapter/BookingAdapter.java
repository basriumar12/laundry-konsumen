package com.samyotech.laundry.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.ModelClass.BookingDTO;
import com.samyotech.laundry.ModelClass.CurrencyDTO;
import com.samyotech.laundry.ModelClass.OrderListDTO;
import com.samyotech.laundry.ModelClass.ServicesDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterBookingBinding;
import com.samyotech.laundry.databinding.AdapterServicesBinding;
import com.samyotech.laundry.databinding.DailogCancelOrderBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.ui.activity.OrderDetails;
import com.samyotech.laundry.ui.fragment.BookingFragment;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private String TAG = BookingAdapter.class.getSimpleName();

    LayoutInflater layoutInflater;
    AdapterBookingBinding binding;
    Context kContext;
    ArrayList<OrderListDTO> servicesDTOArrayList;
    BookingFragment bookingFragment;
    CurrencyDTO currencyDTO;
    private Dialog dialog;
    private ArrayList<OrderListDTO> objects = null;

    public BookingAdapter(Context kContext, ArrayList<OrderListDTO> objects, BookingFragment bookingFragment,CurrencyDTO currencyDTO) {
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


        holder.binding.ctvName.setText(objects.get(position).getShop_name());
        holder.binding.ctvOrder.setText(objects.get(position).getOrder_id());
        holder.binding.ctvPrice.setText(currencyDTO.getCurrency_symbol()+" "+objects.get(position).getPrice());



        if (objects.get(position).getOrder_status().equalsIgnoreCase("0")) {
            holder.binding.ctvStatus.setText(kContext.getResources().getString(R.string.pending));
        } else if (objects.get(position).getOrder_status().equalsIgnoreCase("1")) {
            holder.binding.ctvStatus.setText(kContext.getResources().getString(R.string.confirmed));
            holder.binding.llConfirm.setBackgroundColor(kContext.getResources().getColor(R.color.white));
        } else if (objects.get(position).getOrder_status().equalsIgnoreCase("2")) {
            holder.binding.ctvStatus.setText(kContext.getResources().getString(R.string.pickedup));
            holder.binding.llConfirm.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llpickUp.setBackgroundColor(kContext.getResources().getColor(R.color.white));
        } else if (objects.get(position).getOrder_status().equalsIgnoreCase("3")) {
            holder.binding.ctvStatus.setText(kContext.getResources().getString(R.string.inprogress));
            holder.binding.llConfirm.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llpickUp.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llProcess.setBackgroundColor(kContext.getResources().getColor(R.color.white));
        } else if (objects.get(position).getOrder_status().equalsIgnoreCase("4")) {
            holder.binding.ctvStatus.setText(kContext.getResources().getString(R.string.shipped));
            holder.binding.llConfirm.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llpickUp.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llProcess.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llShiped.setBackgroundColor(kContext.getResources().getColor(R.color.white));
        } else if (objects.get(position).getOrder_status().equalsIgnoreCase("5")) {
            holder.binding.ctvStatus.setText(kContext.getResources().getString(R.string.delivered));
            holder.binding.llConfirm.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llpickUp.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llProcess.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llShiped.setBackgroundColor(kContext.getResources().getColor(R.color.white));
            holder.binding.llDeliver.setBackgroundColor(kContext.getResources().getColor(R.color.white));
        } else if (objects.get(position).getOrder_status().equalsIgnoreCase("6")) {
            holder.binding.ctvStatus.setText(kContext.getResources().getString(R.string.cancel));
        }


        holder.binding.ctvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(kContext, OrderDetails.class);
                in.putExtra(Consts.ORDERLISTDTO,objects.get(position));
                kContext.startActivity(in);
            }
        });


        holder.binding.cvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objects.get(position).getOrder_status().equalsIgnoreCase("6")) {
                    Toast.makeText(kContext, R.string.ordercanceled, Toast.LENGTH_SHORT).show();
                }else
                dialogCancel(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterBookingBinding binding;

        public MyViewHolder(@NonNull AdapterBookingBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
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
        HashMap<String,String>params=new HashMap<>();
        params.put(Consts.ORDER_ID,orderid);
        new HttpsRequest(Consts.ORDERCANCEL,params,kContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if(flag){
                    bookingFragment.getAllBookings();
                    notifyDataSetChanged();
                    bookingFragment.getAllBookings();
                }else {
                    ProjectUtils.showToast(kContext,msg);
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


}
