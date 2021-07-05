package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.samyotech.laundry.GlobalState;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityScheduleBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.HomeDTO;
import com.samyotech.laundry.model.ItemDTO;
import com.samyotech.laundry.model.ItemListDTO;
import com.samyotech.laundry.model.ItemServiceDTO;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.ServicesDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.TabsAdapter;
import com.samyotech.laundry.utils.AppFormat;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Schedule_Activity extends AppCompatActivity implements View.OnClickListener {
    ActivityScheduleBinding binding;
    Context mContext;
    HashMap<String, String> params = new HashMap<>();
    String TAG = Schedule_Activity.class.getSimpleName();
    ItemDTO itemDTOS;

    String valname, valprice, shopid = "", itemid_se;
    UserDTO userDTO;
    GlobalState globalState;
    TabsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    int quantity = 0, check = 0;
    float price = 0;
    boolean doubleClick = true;
    PopLaundryDTO popLaundryDTO;
    CurrencyDTO currencyDTO;
    ServicesDTO servicesDTO = new ServicesDTO();
    private SharedPrefrence prefrence;
    int idx = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule);
        mContext = Schedule_Activity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        currencyDTO = prefrence.getCurrency(Consts.CURRENCYDTO);
        globalState = (GlobalState) getApplication();
        binding.ctvNext.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);

        if (getIntent().hasExtra(Consts.SHOP_ID)) {
            // popLaundryDTO = (PopLaundryDTO) getIntent().getSerializableExtra(Consts.SHOPDTO);
            shopid = getIntent().getStringExtra(Consts.SHOP_ID);
            params = new HashMap<>();
            params.put(Consts.SHOP_ID, shopid);
            new HttpsRequest(Consts.GETLAUNDRYSHOP, params, getBaseContext()).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                    if (flag) {

                        try {
                            popLaundryDTO = new Gson().fromJson(response.getJSONObject("data").toString(), PopLaundryDTO.class);
                            servicesDTO = (ServicesDTO) getIntent().getSerializableExtra(Consts.SERVICEDTO);
                            Log.e(TAG, "Hasil" );
                            Log.e(TAG, popLaundryDTO.toString() );
                            getItem();

                        } catch (Exception e) {
                            e.getMessage();
                        }


                    } else {
                        ProjectUtils.showToast(getBaseContext(), msg);
                    }
                }
            });
            getItem();

        }

        if (getIntent().hasExtra(Consts.SHOPDTO)) {
            popLaundryDTO = (PopLaundryDTO) getIntent().getSerializableExtra(Consts.SHOPDTO);
            getItem();
        }

    }

    private void getItem() {
        params = new HashMap<>();
//        ProjectUtils.getProgressDialog(mContext);
        if (shopid.equals("")) {
            params.put(Consts.SHOP_ID, popLaundryDTO.getShop_id());
        } else {
            params.put(Consts.SHOP_ID, shopid);
        }

        new HttpsRequest(Consts.GETITEMBYSHOPID, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
//                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        itemDTOS = new Gson().fromJson(response.getJSONObject("data").toString(), ItemDTO.class);
                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });


    }

    private void setData() {

        ArrayList<ItemListDTO> items = itemDTOS.getItem_list();
        Log.e(TAG, items.size() + "");
        binding.tabLayout.removeAllTabs();
        for (int k = 0; k < items.size(); k++) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(items.get(k).getService_name()));


            if (servicesDTO.getService_name().equalsIgnoreCase(items.get(k).getService_name())) {
                idx = k;
                Log.e(TAG, "setData: " + servicesDTO.getService_name() );
                Log.e(TAG, "setData: " + items.get(k).getService_name() );
            }
        }

        Log.e(TAG, "setData: " + idx );

        currencyDTO.setCurrency_code(itemDTOS.getCurrency_code());

        adapter = new TabsAdapter
                (getSupportFragmentManager(), binding.tabLayout.getTabCount(), itemDTOS, currencyDTO);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(1);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.viewPager.setCurrentItem(idx);


        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("llllllllllllllllllllll", "" + tab.getPosition());
                binding.viewPager.setCurrentItem(tab.getPosition());

                //   filter(mothRealPosition);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //getData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        doubleClick = true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ctvNext:
                if (doubleClick) {
                    if (price == 0) {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.val_Item));
                    } else {
                        globalState.setItem(itemDTOS);
                        globalState.setQuantity(String.valueOf(quantity));
                        globalState.setPopLaundryDTO(popLaundryDTO);

                        Intent in = new Intent(mContext, PreviewOrderActivity.class);
                        in.putExtra(Consts.TOTAL_PRICE, String.valueOf(price));
                        in.putExtra(Consts.SHOP_ID, popLaundryDTO.getShop_id());

                        startActivity(in);
                        doubleClick = false;
                    }
                }
                break;

            case R.id.ivBack:
                onBackPressed();
                break;
        }

    }


    public void addData(ItemServiceDTO categoryArrayList) {
        Boolean status = false;
        int x, y;

        for (int i = 0; i < itemDTOS.getItem_list().size(); i++) {
            for (int j = 0; j < itemDTOS.getItem_list().get(i).getServices().size(); j++) {

                if (itemDTOS.getItem_list().get(i).getServices().get(j).getItem_id().equalsIgnoreCase(categoryArrayList.getItem_id())) {

                    itemDTOS.getItem_list().get(i).getServices().get(j).getCount().replace(itemDTOS.getItem_list().get(i).getServices().get(j).getCount(), categoryArrayList.getCount());
                    x = i;
                    y = j;
                    quantity = quantity + 1;
                    price = price + (Float.parseFloat(categoryArrayList.getPrice()));
                    Log.e(TAG, "addData:quantity " + quantity);
                    Log.e(TAG, "addData:price " + price);
                    prefrence.setCurrency(itemDTOS.getCurrency_code());
                    binding.ctvTotalPrice.setText(getResources().getText(R.string.total) + " " + itemDTOS.getCurrency_code() + AppFormat.addDelimiter(((int)price) + ""));
                    binding.ctvAdded.setText(quantity + " " + getResources().getText(R.string.itemsadd));
                }
            }
        }
    }

    public void subData(ItemServiceDTO categoryArrayList) {
        Boolean status = false;
        int x, y;

        for (int i = 0; i < itemDTOS.getItem_list().size(); i++) {
            for (int j = 0; j < itemDTOS.getItem_list().get(i).getServices().size(); j++) {

                if (itemDTOS.getItem_list().get(i).getServices().get(j).getItem_id().equalsIgnoreCase(categoryArrayList.getItem_id())) {

                    itemDTOS.getItem_list().get(i).getServices().get(j).getCount().replace(itemDTOS.getItem_list().get(i).getServices().get(j).getCount(), categoryArrayList.getCount());
                    x = i;
                    y = j;

                    quantity = quantity - 1;
                    price = price - (Float.parseFloat(categoryArrayList.getPrice()));
                    Log.e(TAG, "addData:quantity " + quantity);
                    Log.e(TAG, "addData:price " + price);

                    binding.ctvTotalPrice.setText(getResources().getText(R.string.total) + " " + itemDTOS.getCurrency_code() + AppFormat.addDelimiter(((int)price) + ""));
                    binding.ctvAdded.setText(quantity + " " + getResources().getText(R.string.itemsadd));
                }
            }


        }


    }


}
