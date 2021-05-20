package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityOrderDetailsBinding;
import com.samyotech.laundry.databinding.DailogRatingBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.BookingDTO;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.IpaymuDTO;
import com.samyotech.laundry.model.IpaymuDataDTO;
import com.samyotech.laundry.model.ItemDetailsDTO;
import com.samyotech.laundry.model.OrderListDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.PreviewBookingAdapter;
import com.samyotech.laundry.utils.AppFormat;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = OrderDetails.class.getSimpleName();
    ActivityOrderDetailsBinding binding;
    Context mContext;
    OrderListDTO bookingDTO;
    int CALL_PERMISSION = 101;
    SharedPrefrence sharedPrefrence;
    PreviewBookingAdapter previewAdapter;
    LinearLayoutManager linearLayoutManager;
    UserDTO userDTO;
    float rating = 0;
    CurrencyDTO currencyDTO;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        mContext = OrderDetails.this;

        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        currencyDTO = sharedPrefrence.getCurrency(Consts.CURRENCYDTO);
        if (getIntent().hasExtra(Consts.ORDERLISTDTO)) {
            bookingDTO = (OrderListDTO) getIntent().getSerializableExtra(Consts.ORDERLISTDTO);
        }

        setUIAction();
    }

    private void setUIAction() {
        sharedPrefrence.setCurrency(bookingDTO.getCurrency_code());
        binding.total.setText(bookingDTO.getCurrency_code() + " " + AppFormat.addDelimiter((int) Double.parseDouble(bookingDTO.getFinal_price()) + ""));
        binding.subtotal.setText(bookingDTO.getCurrency_code() + " " + AppFormat.addDelimiter((int) Double.parseDouble(bookingDTO.getPrice()) + ""));
        binding.discount.setText(bookingDTO.getCurrency_code() + " " + AppFormat.addDelimiter((int) Double.parseDouble(bookingDTO.getDiscount()) + ""));
        binding.tax.setText(bookingDTO.getCurrency_code() + " 0");
        binding.pickupAddress.setText(bookingDTO.getShipping_address());
        binding.deliveryAddress.setText(bookingDTO.getShipping_address());
        binding.pickupDay.setText(bookingDTO.getPickup_date());
        binding.pickupTime.setText(bookingDTO.getPickup_time());
        binding.deliveryTime.setText(bookingDTO.getDelivery_time());
        binding.deliveryDate.setText(bookingDTO.getDelivery_date());
        binding.hubungiTitle.setText(String.format("Hubungi %s", bookingDTO.getShop_name()));


        Log.e(TAG, bookingDTO.getOrder_id());
        Log.e(TAG, bookingDTO.getOrder_status());
        Log.e(TAG, bookingDTO.getPayment_status());
        /**
         * 1.1 Jika status pesanan !== pending|cancel && status pembayaran === pending maka muncul tombol lakukan pembayaran
         * 1.2 Jika status pesanan !== pending|cancel && status pembayaran === complete then "Pembayaran telah sukses dilakukan."
         * 1.3 Jika status pesanan === pending && status pembayaran === pending then "Jumlah pembayaran yang harus dibayarkan sedang dalam konfirmasi"
         */
        if ((!bookingDTO.getOrder_status().equals("0") && !bookingDTO.getOrder_status().equals("6")) && bookingDTO.getPayment_status().equals("0")) {
            binding.buyNow.setVisibility(View.VISIBLE);
            binding.textStatus.setVisibility(View.GONE);
        } else if ((!bookingDTO.getOrder_status().equals("0") && !bookingDTO.getOrder_status().equals("6")) && bookingDTO.getPayment_status().equals("1")) {
            binding.buyNow.setVisibility(View.GONE);
            binding.textStatus.setText("Pembayaran telah sukses dilakukan.");
            binding.textStatus.setVisibility(View.VISIBLE);
        } else if (bookingDTO.getOrder_status().equals("0") && bookingDTO.getPayment_status().equals("0")) {
            binding.buyNow.setVisibility(View.GONE);
            binding.textStatus.setText("Jumlah pembayaran yang harus dibayarkan sedang dalam konfirmasi");
            binding.textStatus.setVisibility(View.VISIBLE);
        } else {
            binding.buyNow.setVisibility(View.GONE);
            binding.textStatus.setVisibility(View.GONE);
        }


        binding.back.setOnClickListener(this);
        binding.kirimPesanBtn.setOnClickListener(this);
        binding.rateNowBtn.setOnClickListener(this);
        binding.buyNow.setOnClickListener(this);
        setData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.llCall:

//                Toast.makeText(mContext, R.string.optionwill, Toast.LENGTH_SHORT).show();

/*
                        if (ProjectUtils.hasPermissionInManifest(OrderDetails.this, CALL_PERMISSION, Manifest.permission.CALL_PHONE)) {
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + bookingDTO.getS_no()));
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(callIntent);
                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            }
                        }*/

//                break;
            case R.id.kirim_pesan_btn:

                Intent in = new Intent(mContext, ChatActivity.class);
                in.putExtra(Consts.SHOP_ID, bookingDTO.getShop_id());
                in.putExtra(Consts.SHOP_NAME, bookingDTO.getShop_name());
                in.putExtra(Consts.IMAGE, bookingDTO.getShop_image());
                startActivity(in);
                break;
            case R.id.rate_now_btn:
                rateDialog();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.buy_now:
                buyNow();
                break;
        }

    }

    public void rateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialog);
        final DailogRatingBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_rating, null, false);

        builder.setView(binding1.getRoot());
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        binding1.simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = binding1.rbReview.getRating();
                submitRating();
            }
        });
    }

    private void submitRating() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.SHOP_ID, bookingDTO.getShop_id());
        params.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.RATING, String.valueOf(rating));
        new HttpsRequest(Consts.ADDRATING, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    alertDialog.dismiss();

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void buyNow() {
        int k = 0;
        JSONArray jsonArray = new JSONArray();
        for (ItemDetailsDTO data : bookingDTO.getItem_details()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.putOpt(Consts.ITEM_ID, data.getItem_id());
                jsonObject.putOpt(Consts.ITEM_NAME, data.getItem_name());
                jsonObject.putOpt(Consts.PRICE, data.getPrice());
                jsonObject.putOpt(Consts.QUANTITY, data.getQuantity());
                jsonArray.put(k, jsonObject);
                k++;
            } catch (Exception e) {
                Log.e(TAG, "buyNow: " + e.getMessage());
            }
        }
        Log.e(TAG, jsonArray.toString());

        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.ORDER_ID, String.valueOf(bookingDTO.getOrder_id()));
        params.put(Consts.ITEM_DETAILS, String.valueOf(jsonArray));
        new HttpsRequest(Consts.ORDERIPAYMU, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                try {
                    IpaymuDataDTO data = new Gson().fromJson(response.getJSONObject("data").toString(), IpaymuDataDTO.class);
                    Log.e("TAG","data "+ new Gson().toJson(response));
                    Intent in = new Intent(mContext, IpayMuPayment.class);
                    in.putExtra(Consts.ORDERLINK, data.Data.Url);
                    mContext.startActivity(in);
                }catch (NullPointerException a){
                    Toast.makeText(mContext, "Gagal dapatkan pembayaran IPaymu, "+new Gson().toJson(response), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void setData() {

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerviewOrders.setLayoutManager(linearLayoutManager);
        previewAdapter = new PreviewBookingAdapter(mContext, bookingDTO.getItem_details(), currencyDTO);
        binding.recyclerviewOrders.setAdapter(previewAdapter);
    }


}
