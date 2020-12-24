package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterBookingBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.OrderListDTO;
import com.samyotech.laundry.ui.activity.OrderDetails;
import com.samyotech.laundry.ui.fragment.BookingFragment;
import com.samyotech.laundry.utils.AppFormat;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private final String TAG = BookingAdapter.class.getSimpleName();
    LayoutInflater layoutInflater;
    AdapterBookingBinding binding;
    Context kContext;
    ArrayList<OrderListDTO> servicesDTOArrayList;
    BookingFragment bookingFragment;
    CurrencyDTO currencyDTO;
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
        holder.binding.harga.setText(item.getCurrency_code() + " " + AppFormat.addDelimiter((int)Double.parseDouble(item.getPrice()) + ""));

        holder.binding.selengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(kContext, OrderDetails.class);
                in.putExtra(Consts.ORDERLISTDTO, objects.get(position));
                kContext.startActivity(in);
            }
        });

        holder.binding.batalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objects.get(position).getOrder_status().equalsIgnoreCase("6")) {
                    Toast.makeText(kContext, R.string.ordercanceled, Toast.LENGTH_SHORT).show();
                } else
                    alertDialogBatal(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void alertDialogBatal(final int pos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(kContext, R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(kContext).inflate(R.layout.dialog_batal_booking, null, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        FancyButton cancel = dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        FancyButton ok = dialogView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(objects.get(pos).getOrder_id());
                alertDialog.dismiss();
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
