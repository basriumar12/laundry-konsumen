package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityOrderDetailsBinding;
import com.samyotech.laundry.databinding.DailogRatingBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.OrderListDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.PreviewBookingAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

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
        binding.total.setText(currencyDTO.getCurrency_symbol() + " " + bookingDTO.getFinal_price());
        binding.subtotal.setText(currencyDTO.getCurrency_symbol() + " " + bookingDTO.getPrice());
        binding.discount.setText(currencyDTO.getCurrency_symbol() + " " + bookingDTO.getDiscount());
        binding.tax.setText(currencyDTO.getCurrency_symbol() + " 0");
        binding.pickupAddress.setText(bookingDTO.getShipping_address());
        binding.deliveryAddress.setText(bookingDTO.getShipping_address());
        binding.pickupDay.setText(bookingDTO.getPickup_date());
        binding.pickupTime.setText(bookingDTO.getPickup_time());
        binding.deliveryTime.setText(bookingDTO.getDelivery_time());
        binding.deliveryDate.setText(bookingDTO.getDelivery_date());
        binding.hubungiTitle.setText(String.format("Hubungi %s", bookingDTO.getShop_name()));

        binding.back.setOnClickListener(this);
        binding.kirimPesanBtn.setOnClickListener(this);
        binding.rateNowBtn.setOnClickListener(this);
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
        }

    }

    public void rateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialog);
        final DailogRatingBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_rating, null, false);

        builder.setView(binding1.getRoot());
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
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


    private void setData() {

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerviewOrders.setLayoutManager(linearLayoutManager);
        previewAdapter = new PreviewBookingAdapter(mContext, bookingDTO.getItem_details(), currencyDTO);
        binding.recyclerviewOrders.setAdapter(previewAdapter);
    }


}
