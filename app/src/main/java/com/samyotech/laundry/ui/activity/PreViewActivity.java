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
import com.samyotech.laundry.databinding.ActivityPreViewBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.ItemDTO;
import com.samyotech.laundry.model.ItemServiceDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.PreviewAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PreViewActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    String TAG = PreViewActivity.class.getSimpleName();

    HashMap<String, String> parms = new HashMap<>();
    ActivityPreViewBinding binding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pre_view);
        mContext = PreViewActivity.this;

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        currencyDTO = prefrence.getCurrency(Consts.CURRENCYDTO);

        globalState = (GlobalState) getApplication();
        itemServiceDTO = GlobalState.getInstance().itemServiceDTO();
        Log.e(TAG, "onPostExecute: " + itemServiceDTO.getItem_list().get(0).getServices().get(0).getCount());

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
            binding.ctvSubTotalValue.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
            binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
        }
        parms.put(Consts.TOTAL_PRICE, totalPrice);
        parms.put(Consts.SHOP_ID, itemServiceDTO.getItem_list().get(0).getServices().get(0).getShop_id());

        binding.ctvHaveProcode.setOnClickListener(this);
        binding.llSubmit.setOnClickListener(this);
        binding.ctvSchedule.setOnClickListener(this);
        binding.ctvAddmore.setOnClickListener(this);
        binding.ctvEdit.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
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

        binding.cetEnterProcode.addTextChangedListener(mTextEditorWatcher);
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
        binding.rvReview.setLayoutManager(linearLayoutManager);
        previewAdapter = new PreviewAdapter(mContext, categoryArrayList, currencyDTO);
        binding.rvReview.setAdapter(previewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doubleClick = true;
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
                    totalPrice = getIntent().getStringExtra(Consts.TOTAL_PRICE);
                    discounted_price = "0";
                    binding.cetEnterProcode.setFocusableInTouchMode(true);
                    binding.ctvDiscountValue.setText(currencyDTO.getCurrency_symbol() + " 0");
                    binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + totalPrice);
                    checkCoup = true;

                }
                break;
            case R.id.ivBack:
            case R.id.ctvAddmore:
            case R.id.ctvEdit:
                if (doubleClick) {
                    doubleClick = false;
                    onBackPressed();

                }
                break;
            case R.id.ctvSchedule:
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

                    Intent in = new Intent(mContext, SchedulePickup.class);
                    startActivity(in);
                }
                break;
        }
    }

    private void addPromocode() {
        parms.put(Consts.PROMOCODE, ProjectUtils.getEditTextValue(binding.cetEnterProcode));
        new HttpsRequest(Consts.APPLYPROMOCODE, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {
                    discounted_price = (response.getString("data"));
                    Log.e(TAG, "backResponse: " + discounted_price);
                    globalState.setPromoCode(ProjectUtils.getEditTextValue(binding.cetEnterProcode));
                    discounted_value = String.valueOf(Float.valueOf(totalPrice) - Float.valueOf(discounted_price));
                    binding.cetEnterProcode.setFocusable(false);
                    binding.ctvApply.setText(getResources().getText(R.string.cancel));
                    checkCoup = false;

                    binding.ctvDiscountValue.setText(currencyDTO.getCurrency_symbol() + " " + discounted_value);
                    binding.ctvPayAbleAmount.setText(currencyDTO.getCurrency_symbol() + " " + discounted_price);
                } else {

                    ProjectUtils.showToast(mContext, msg);
                }

            }
        });

    }
}
