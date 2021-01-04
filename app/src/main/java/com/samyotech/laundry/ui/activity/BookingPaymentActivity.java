package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.laundry.GlobalState;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityBookingPaymentBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.ItemDTO;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.AppFormat;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class BookingPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityBookingPaymentBinding binding;
    Context mContext;
    UserDTO userDTO;
    GlobalState globalState;
    ItemDTO itemServiceDTO;
    PopLaundryDTO popLaundryDTO;
    JSONArray jsonArray = new JSONArray();
    String TAG = PreviewOrderActivity.class.getSimpleName();
    String totalPrice = "0", totalPriceBef = "0", promoCode = "", latitude = "", longitude = "", discounted_price = "0", discounted_value = "0";
    String otpGenrate = "";
    float discountValue = 0;
    HashMap<String, String> parms = new HashMap<>();
    HashMap<String, String> parmsSubmit = new HashMap<>();
    boolean checkCoup = true;
    int k = 0;
    CurrencyDTO currencyDTO;
    private SharedPrefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_payment);
        mContext = BookingPaymentActivity.this;

        parmsSubmit = (HashMap<String, String>) getIntent().getSerializableExtra("map");

        globalState = (GlobalState) getApplication();
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        currencyDTO = prefrence.getCurrency(Consts.CURRENCYDTO);
        itemServiceDTO = GlobalState.getInstance().itemServiceDTO();
        popLaundryDTO = GlobalState.getInstance().getPopLaundryDTO();

        setUiAction();
    }

    private void setUiAction() {
        if (!globalState.getCost().equals("") && !globalState.getCostbefo().equals("")) {
            binding.total.setText(prefrence.getCurrency() + " " + AppFormat.addDelimiter(((int) Double.parseDouble(globalState.getCost())) + ""));
            binding.subtotal.setText(prefrence.getCurrency() + " " + AppFormat.addDelimiter(((int) Double.parseDouble(globalState.getCostbefo())) + ""));
        } else {
            if (globalState.getCost().equals("")) {
                binding.total.setText(prefrence.getCurrency() + " " + AppFormat.addDelimiter((0 + "")));
            } else {
                binding.subtotal.setText(prefrence.getCurrency() + " " + AppFormat.addDelimiter((0 + "")));
            }
        }

        totalPriceBef = globalState.getCostbefo();
        totalPrice = globalState.getCost();
        promoCode = globalState.getPromoCode();

        discountValue = Float.parseFloat(globalState.getDiscountcost());
        if (discountValue == 0) {
            binding.promoBtn.setOnClickListener(this);
        } else {
            binding.discount.setText(globalState.getDiscountcost());
            discounted_value = globalState.getDiscountcost();
        }
        Log.e(TAG, "setUiAction: " + discountValue);
        try {
            binding.ctvbShopName.setText(popLaundryDTO.getShop_name());
            binding.ctvAddress.setText(popLaundryDTO.getAddress());
            if (popLaundryDTO.getImage() != null) {
                Glide.with(mContext)
                        .load(Consts.BASE_URL + popLaundryDTO.getImage())
                        .error(R.drawable.shop_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivImage);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.shop_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivImage);
            }
        } catch (Exception ex) {
            Glide.with(mContext)
                    .load(R.drawable.shop_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivImage);
            binding.ctvbShopName.setText("Tidak dapat mengambil info nama");
            binding.ctvAddress.setText("Tidak dapat mengambil info alamat");
        }

        binding.confirmBtn.setOnClickListener(this);


    }


    private void addPromocode() {

        parms.put(Consts.TOTAL_PRICE, totalPrice);
        parms.put(Consts.SHOP_ID, itemServiceDTO.getItem_list().get(0).getServices().get(0).getShop_id());
        parms.put(Consts.PROMOCODE, ProjectUtils.getEditTextValue(binding.kodePromo));
        new HttpsRequest(Consts.APPLYPROMOCODE, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {
                    discounted_price = (response.getString("data"));
                    Log.e(TAG, "backResponse: " + discounted_price);
                    discounted_value = String.valueOf(Float.valueOf(totalPrice) - Float.valueOf(discounted_price));
                    totalPrice = discounted_price;
                    checkCoup = false;

                    binding.kodePromo.setFocusable(false);

                    binding.discount.setText(currencyDTO.getCurrency_symbol() + " " + discounted_value);
                    binding.total.setText(currencyDTO.getCurrency_symbol() + " " + discounted_price);
                } else {

                    ProjectUtils.showToast(mContext, msg);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.promo_btn:
                if (checkCoup) {
                    addPromocode();
                } else {
                    binding.kodePromo.setText("");
                    totalPriceBef = globalState.getCostbefo();
                    totalPrice = globalState.getCost();

                    binding.kodePromo.setFocusableInTouchMode(true);
                    binding.discount.setText(currencyDTO.getCurrency_symbol() + " 0");
                    binding.total.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
                    checkCoup = true;

                }

                break;
            case R.id.confirm_btn:
                setData();
                break;
        }
    }

    private void setData() {
        Random otp1 = new Random();
        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 9; count++) {
            builder.append(otp1.nextInt(10));
        }
        otpGenrate = builder.toString();

        for (int i = 0; i < itemServiceDTO.getItem_list().size(); i++) {
            for (int j = 0; j < itemServiceDTO.getItem_list().get(i).getServices().size(); j++) {

                JSONObject jsonObject = new JSONObject();
                int count = Integer.parseInt(itemServiceDTO.getItem_list().get(i).getServices().get(j).getCount());
                if (count > 0) {
                    try {
                        jsonObject.putOpt(Consts.ITEM_ID, itemServiceDTO.getItem_list().get(i).getServices().get(j).getShop_id());
                        jsonObject.putOpt(Consts.ITEM_NAME, itemServiceDTO.getItem_list().get(i).getServices().get(j).getItem_name());
                        jsonObject.putOpt(Consts.PRICE, itemServiceDTO.getItem_list().get(i).getServices().get(j).getPrice());
                        jsonObject.putOpt(Consts.IMAGE, itemServiceDTO.getItem_list().get(i).getServices().get(j).getImage());
                        jsonObject.putOpt(Consts.QUANTITY, itemServiceDTO.getItem_list().get(i).getServices().get(j).getCount());
                        jsonObject.putOpt(Consts.S_NO, itemServiceDTO.getItem_list().get(i).getServices().get(j).getS_no());
                        jsonObject.putOpt(Consts.IMAGE, itemServiceDTO.getItem_list().get(i).getServices().get(j).getImage());
                        jsonObject.putOpt(Consts.STATUS, itemServiceDTO.getItem_list().get(i).getServices().get(j).getStatus());
                        jsonObject.putOpt(Consts.CREATED_AT, itemServiceDTO.getItem_list().get(i).getServices().get(j).getCreated_at());
                        jsonObject.putOpt(Consts.UPDATED_AT, itemServiceDTO.getItem_list().get(i).getServices().get(j).getUpdated_at());
                        jsonObject.putOpt(Consts.SERVICE_NAME, itemServiceDTO.getItem_list().get(i).getServices().get(j).getService_name());

                        jsonArray.put(k, jsonObject);
                        k++;
                /*        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 200);
*/

                    } catch (Exception e) {

                    }
                }
            }
        }
        confirmorder();


    }


    private void confirmorder() {
        parmsSubmit.put(Consts.ORDER_ID, otpGenrate);
        parmsSubmit.put(Consts.USER_ID, userDTO.getUser_id());
        try {
            parmsSubmit.put(Consts.SHOP_ID, popLaundryDTO.getShop_id());
        } catch (Exception ex) {
            parmsSubmit.put(Consts.SHOP_ID, "YZ65d0");
        }

        parmsSubmit.put(Consts.PRICE, totalPriceBef);
        parmsSubmit.put(Consts.DISCOUNT, discounted_value);
        parmsSubmit.put(Consts.FINAL_PRICE, totalPrice);
        parmsSubmit.put(Consts.CURRENCY_CODE, itemServiceDTO.getCurrency_code());
        parmsSubmit.put(Consts.ITEM_DETAILS, String.valueOf(jsonArray));
        globalState.setCost(binding.total.getText().toString().trim());

        new HttpsRequest(Consts.ORDERSUBMIT, parmsSubmit, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {

                    Intent in = new Intent(mContext, BookingConfirmActivity.class);
                    in.putExtra("map", parmsSubmit);
                    startActivity(in);
                    finish();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }
}
