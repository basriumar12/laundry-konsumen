package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.samyotech.laundry.GlobalState;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityPreviewOrderBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.ItemDTO;
import com.samyotech.laundry.model.ItemServiceDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.PreviewAdapter;
import com.samyotech.laundry.utils.AppFormat;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PreviewOrderActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    String TAG = PreviewOrderActivity.class.getSimpleName();

    HashMap<String, String> parms = new HashMap<>();
    UserDTO userDTO;
    ItemDTO itemServiceDTO;
    PreviewAdapter previewAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ItemServiceDTO> categoryArrayList = new ArrayList<>();
    GlobalState globalState;
    String totalPrice = "0", price = "", discounted_price = "0", discounted_value = "0";
    int value = 0;
    boolean doubleClick = true;
    CurrencyDTO currencyDTO;
    boolean checkCoup = true;
    private SharedPrefrence prefrence;
    private TextWatcher mTextEditorWatcher;
    private ActivityPreviewOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview_order);
        mContext = PreviewOrderActivity.this;

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        currencyDTO = prefrence.getCurrency(Consts.CURRENCYDTO);

        globalState = (GlobalState) getApplication();
        itemServiceDTO = GlobalState.getInstance().itemServiceDTO();

        for (int i = 0; i < itemServiceDTO.getItem_list().size(); i++) {
            for (int j = 0; j < itemServiceDTO.getItem_list().get(i).getServices().size(); j++) {
                int value = 0;
                value = Integer.parseInt(itemServiceDTO.getItem_list().get(i).getServices().get(j).getCount());
                if (value > 0) {
                    categoryArrayList.add(itemServiceDTO.getItem_list().get(i).getServices().get(j));
                }
            }
        }
        if (getIntent().hasExtra(Consts.TOTAL_PRICE)) {
            totalPrice = getIntent().getStringExtra(Consts.TOTAL_PRICE);
            binding.subtotal.setText(prefrence.getCurrency() + " " + AppFormat.addDelimiter(((int)Double.parseDouble(totalPrice))+""));
            binding.total.setText(prefrence.getCurrency() + " " + AppFormat.addDelimiter(((int)Double.parseDouble(totalPrice))+""));
        }
        parms.put(Consts.TOTAL_PRICE, totalPrice);
        parms.put(Consts.SHOP_ID, getIntent().getStringExtra(Consts.SHOP_ID));

        binding.promoBtn.setOnClickListener(this);
        binding.bookingBtn.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        setData();

        mTextEditorWatcher = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "beforeTextChanged:start " + start);
                Log.e(TAG, "count " + count);
                Log.e(TAG, "beforeTextChanged: after" + after);

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length

                Log.e(TAG, "onTextChanged:start " + start);
                Log.e(TAG, "onTextChangedbefore " + before);
                Log.e(TAG, "onTextChanged: count " + count);
          /*   if(){

                        binding.cetEnterProcode.setText("");
                        binding.ctvApply.setText(getResources().getText(R.string.apply));
                        totalPrice = getIntent().getStringExtra(Consts.TOTAL_PRICE);

                        binding.ctvDiscountValue.setText(currencyDTO.getCurrency_symbol() + " 0");
                        binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
                        checkCoup = true;
                    }*/
            }

            public void afterTextChanged(Editable s) {

            }
        };

        binding.kodePromo.addTextChangedListener(mTextEditorWatcher);
/*

        binding.cetEnterProcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){

                    binding.cetEnterProcode.setText("");
                    binding.ctvApply.setText(getResources().getText(R.string.apply));
                    totalPrice = getIntent().getStringExtra(Consts.TOTAL_PRICE);

                    binding.ctvDiscountValue.setText(currencyDTO.getCurrency_symbol() + " 0");
                    binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
                    checkCoup = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/

    }

    private void setData() {

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerviewOrders.setLayoutManager(linearLayoutManager);
        previewAdapter = new PreviewAdapter(mContext, categoryArrayList, currencyDTO);
        binding.recyclerviewOrders.setAdapter(previewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doubleClick = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.promo_btn:
                if (checkCoup) {
                    addPromocode();
                } else {
                    binding.kodePromo.setText("");
                    totalPrice = getIntent().getStringExtra(Consts.TOTAL_PRICE);
                    discounted_price = "0";
                    binding.kodePromo.setFocusableInTouchMode(true);
                    binding.discount.setText(currencyDTO.getCurrency_symbol() + " 0");
                    binding.total.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
                    checkCoup = true;

                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.booking_btn:
                if (doubleClick) {
                    doubleClick = false;

                    if (checkCoup) {
                        globalState.setCost(totalPrice);
                        globalState.setDiscountcost("0");
                        globalState.setCostbefo(totalPrice);

                    } else {
                        globalState.setDiscountcost(discounted_value);
                        globalState.setCost(discounted_price);
                        globalState.setCostbefo(totalPrice);
                    }

                    Intent in = new Intent(mContext, BookingPickAddressActivity.class);
                    startActivity(in);
                }
                break;
        }
    }

    private void addPromocode() {
        Log.e("TAG","shop id add"+itemServiceDTO.getItem_list().get(0).getServices().get(0).getShop_id());

        parms.put(Consts.PROMOCODE, ProjectUtils.getEditTextValue(binding.kodePromo));
        new HttpsRequest(Consts.APPLYPROMOCODE, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {
                    discounted_price = (response.getString("data"));
                    Log.e(TAG, "backResponse: " + discounted_price);
                    globalState.setPromoCode(ProjectUtils.getEditTextValue(binding.kodePromo));
                    discounted_value = String.valueOf(Float.valueOf(totalPrice) - Float.valueOf(discounted_price));
                    binding.kodePromo.setFocusable(false);
                    checkCoup = false;

                    binding.discount.setText(currencyDTO.getCurrency_symbol() + " " + discounted_value);
                    binding.total.setText(currencyDTO.getCurrency_symbol() + " " + discounted_price);
                } else {

                    ProjectUtils.showToast(mContext, msg);
                }

            }
        });

    }
}
