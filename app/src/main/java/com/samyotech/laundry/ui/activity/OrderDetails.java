package com.samyotech.laundry.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.samyotech.laundry.ModelClass.BookingDTO;
import com.samyotech.laundry.ModelClass.CurrencyDTO;
import com.samyotech.laundry.ModelClass.OrderListDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityOrderDetailsBinding;
import com.samyotech.laundry.databinding.DailogCancelOrderBinding;
import com.samyotech.laundry.databinding.DailogRatingBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.PreviewAdapter;
import com.samyotech.laundry.ui.adapter.PreviewBookingAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private String TAG = OrderDetails.class.getSimpleName();

    ActivityOrderDetailsBinding binding;
    Context mContext;
    OrderListDTO bookingDTO;
    Dialog dialog;
    int CALL_PERMISSION = 101;
    SharedPrefrence sharedPrefrence;
    PreviewBookingAdapter previewAdapter;
    LinearLayoutManager linearLayoutManager;
    UserDTO userDTO;
    float rating=0;
    CurrencyDTO currencyDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil. setContentView(this,R.layout.activity_order_details);
        mContext=OrderDetails.this;


        sharedPrefrence=SharedPrefrence.getInstance(mContext);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        currencyDTO=(CurrencyDTO)sharedPrefrence.getCurrency(Consts.CURRENCYDTO);
        if(getIntent().hasExtra(Consts.ORDERLISTDTO)){
            bookingDTO =(OrderListDTO)getIntent().getSerializableExtra(Consts.ORDERLISTDTO);
        }

            setUIAction();
    }

    private void setUIAction() {
        binding.ctvPaidViaValue.setText(currencyDTO.getCurrency_symbol()+" "+bookingDTO.getFinal_price());
        binding.ctvSubTotalValue.setText(currencyDTO.getCurrency_symbol()+" "+bookingDTO.getPrice());
        binding.ctvDiscountValue.setText(currencyDTO.getCurrency_symbol()+" "+bookingDTO.getDiscount());
        binding.ctvTaxValue.setText(currencyDTO.getCurrency_symbol()+" 0");
        binding.ctvAddress.setText(bookingDTO.getShipping_address());
        binding.ctvPickUpDay.setText(bookingDTO.getPickup_date());
        binding.ctvPickUpTime.setText(bookingDTO.getPickup_time());
        binding.ctvDeliveryTime.setText(bookingDTO.getDelivery_time());
        binding.ctvDeliveryDate.setText(bookingDTO.getDelivery_date());
//        binding.ctvPaidVia.setText(bookingDTO.getPayment_status());

        binding.ivBack.setOnClickListener(this);
        binding.llMsg.setOnClickListener(this);
        binding.llCall.setOnClickListener(this);
        binding.cvRatenow.setOnClickListener(this);
        setData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llCall:

                Toast.makeText(mContext,R.string.optionwill, Toast.LENGTH_SHORT).show();

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

                break;
            case R.id.llMsg:

                Intent in = new Intent(mContext,OneTwoOneChat.class);
                in.putExtra(Consts.SHOP_ID,bookingDTO.getShop_id());
                in.putExtra(Consts.SHOP_NAME,bookingDTO.getShop_name());
                in.putExtra(Consts.IMAGE,bookingDTO.getShop_image());
                startActivity(in);
                break;
            case R.id.cvRatenow:
                dialogRating();
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }

    }




    public void dialogRating() {
        dialog = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final DailogRatingBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_rating, null, false);
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
            rating=binding1.rbReview.getRating();
                submitRating();

            }
        });

    }

    private void submitRating() {

        HashMap<String,String> params=new HashMap<>();
        params.put(Consts.SHOP_ID,bookingDTO.getShop_id());
        params.put(Consts.USER_ID,userDTO.getUser_id());
        params.put(Consts.RATING, String.valueOf(rating));
        new HttpsRequest(Consts.ADDRATING,params,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if(flag){
                    ProjectUtils.showToast(mContext,msg);
                    dialog.dismiss();

                }else {
                    ProjectUtils.showToast(mContext,msg);
                }
            }
        });
    }


    private void setData() {


        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.rvReview.setLayoutManager(linearLayoutManager);
        previewAdapter = new PreviewBookingAdapter(mContext, bookingDTO.getItem_details(),currencyDTO);
        binding.rvReview.setAdapter(previewAdapter);
    }



}
