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
import com.samyotech.laundry.GlobalState;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityPaymentBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.ItemDTO;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPaymentBinding binding;
    Context mContext;
    UserDTO userDTO;
    GlobalState globalState;
    ItemDTO itemServiceDTO;
    PopLaundryDTO popLaundryDTO;
    JSONArray jsonArray = new JSONArray();
    String TAG = PreViewActivity.class.getSimpleName();
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        mContext = PaymentActivity.this;

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
        binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + globalState.getCost());
        binding.ctvSubTotalValue.setText(currencyDTO.getCurrency_symbol() + " " + globalState.getCostbefo());

        totalPriceBef = globalState.getCostbefo();
        totalPrice = globalState.getCost();
        promoCode = globalState.getPromoCode();

        discountValue = Float.parseFloat(globalState.getDiscountcost());
        if (discountValue == 0) {
            binding.llSubmit.setOnClickListener(this);
            binding.ctvHaveProcode.setOnClickListener(this);


        } else {
            binding.ctvHaveProcode.setText(getResources().getText(R.string.applied_code) + " " + promoCode);
            binding.ctvDiscountValue.setText(globalState.getDiscountcost());
            discounted_value = globalState.getDiscountcost();

        }
        Log.e(TAG, "setUiAction: " + discountValue);
        Glide.with(mContext)
                .load(Consts.DEV_URL + popLaundryDTO.getImage())
                .error(R.drawable.shop_image)
                .into(binding.ivImage);
        binding.ctvbShopName.setText(popLaundryDTO.getShop_name());
        binding.ctvAddress.setText(popLaundryDTO.getAddress());

        binding.rlConfirm.setOnClickListener(this);


    }


    private void addPromocode() {

        parms.put(Consts.TOTAL_PRICE, totalPrice);
        parms.put(Consts.SHOP_ID, itemServiceDTO.getItem_list().get(0).getServices().get(0).getShop_id());
        parms.put(Consts.PROMOCODE, ProjectUtils.getEditTextValue(binding.cetEnterProcode));
        new HttpsRequest(Consts.APPLYPROMOCODE, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {
                    discounted_price = (response.getString("data"));
                    Log.e(TAG, "backResponse: " + discounted_price);
                    discounted_value = String.valueOf(Float.valueOf(totalPrice) - Float.valueOf(discounted_price));
                    totalPrice = discounted_price;
                    binding.ctvApply.setText(getResources().getText(R.string.cancel));
                    checkCoup = false;

                    binding.cetEnterProcode.setFocusable(false);

                    binding.ctvDiscountValue.setText(currencyDTO.getCurrency_symbol() + " " + discounted_value);
                    binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + discounted_price);
                } else {

                    ProjectUtils.showToast(mContext, msg);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ctvHaveProcode:
                binding.llEnterCode.setVisibility(View.VISIBLE);
                binding.llSave.setVisibility(View.GONE);
                break;
            case R.id.llSubmit:
                if (checkCoup) {
                    addPromocode();
                } else {
                    binding.cetEnterProcode.setText("");
                    binding.ctvApply.setText(getResources().getText(R.string.apply));
                    totalPriceBef = globalState.getCostbefo();
                    totalPrice = globalState.getCost();

                    binding.cetEnterProcode.setFocusableInTouchMode(true);
                    binding.ctvDiscountValue.setText(currencyDTO.getCurrency_symbol() + " 0");
                    binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
                    checkCoup = true;

                }

                break;
            case R.id.rlConfirm:
                if (binding.rdbtn.isChecked()) {
                    setData();
                } else {
                    Toast.makeText(mContext, R.string.val_payment, Toast.LENGTH_SHORT).show();
                }
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
        parmsSubmit.put(Consts.SHOP_ID, popLaundryDTO.getShop_id());
        parmsSubmit.put(Consts.PRICE, totalPriceBef);
        parmsSubmit.put(Consts.DISCOUNT, discounted_value);
        parmsSubmit.put(Consts.FINAL_PRICE, totalPrice);
        parmsSubmit.put(Consts.CURRENCY_CODE, itemServiceDTO.getCurrency_code());
        parmsSubmit.put(Consts.ITEM_DETAILS, String.valueOf(jsonArray));
        globalState.setCost(binding.ctvPayAbleAmount.getText().toString().trim());

        new HttpsRequest(Consts.ORDERSUBMIT, parmsSubmit, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {

                    Intent in = new Intent(mContext, ConfirmedOrder.class);
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
