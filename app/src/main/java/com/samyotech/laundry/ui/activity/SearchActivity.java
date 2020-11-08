package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivitySearchBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.ui.adapter.PopularFullLaundriesAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private final String TAG = TicketsActivity.class.getSimpleName();
    ActivitySearchBinding binding;
    Context mContext;
    HashMap<String, String> parms = new HashMap<>();
    PopularFullLaundriesAdapter popularFullLaundriesAdapter;
    ArrayList<PopLaundryDTO> popLaundryDTOArrayList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        mContext = SearchActivity.this;
        binding.cetSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                parms.put(Consts.SHOP_NAME, String.valueOf(s));
                getSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getSearch() {

        new HttpsRequest(Consts.SEARCH, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {
                    try {
                        popLaundryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<PopLaundryDTO>>() {
                        }.getType();
                        popLaundryDTOArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

                        binding.rvLaundry.setLayoutManager(layoutManager);
                        popularFullLaundriesAdapter = new PopularFullLaundriesAdapter(mContext, popLaundryDTOArrayList);
                        binding.rvLaundry.setAdapter(popularFullLaundriesAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }


}
