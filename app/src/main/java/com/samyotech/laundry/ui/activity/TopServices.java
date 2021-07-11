package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityTopServicesBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.PopularFullLaundriesAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopServices extends AppCompatActivity implements View.OnClickListener {

    ActivityTopServicesBinding binding;
    String TAG = TopServices.class.getSimpleName();

    PopularFullLaundriesAdapter popularFullLaundriesAdapter;
    ArrayList<PopLaundryDTO> popLaundryDTOArrayList;
    SharedPrefrence prefrence;

    RecyclerView.LayoutManager layoutManager;
    Context kContext;
    String serviceID = "";
    HashMap<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kContext = TopServices.this;
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_top_services);
        binding.back.setOnClickListener(this);

        prefrence = SharedPrefrence.getInstance(kContext);

        if (getIntent().hasExtra(Consts.SERVICE_ID)) {
            serviceID = getIntent().getStringExtra(Consts.SERVICE_ID);
            params.put(Consts.SERVICE_ID, serviceID);

            getSerivese();
        } else {
            setdata();
        }

    }

    private void getSerivese() {

        params.put(Consts.LATITUDE, prefrence.getValue(Consts.LATITUDE));
        params.put(Consts.LONGITUDE, prefrence.getValue(Consts.LONGITUDE));

        new HttpsRequest(Consts.GETLAUNDRYBYSERVICE, params, kContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    try {
                        popLaundryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<PopLaundryDTO>>() {
                        }.getType();
                        popLaundryDTOArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(kContext, LinearLayoutManager.VERTICAL, false);

                        binding.recyclerview.setLayoutManager(layoutManager);
                        popularFullLaundriesAdapter = new PopularFullLaundriesAdapter(kContext, popLaundryDTOArrayList);
                        binding.recyclerview.setAdapter(popularFullLaundriesAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {

                    ProjectUtils.showToast(kContext, msg);
                }
            }
        });


    }


    public void setdata() {
        params.put(Consts.Count, "70");
        params.put(Consts.LATITUDE, prefrence.getValue(Consts.LATITUDE));
        params.put(Consts.LONGITUDE, prefrence.getValue(Consts.LONGITUDE));

        new HttpsRequest(Consts.GETALLLAUNDRYSHOP, params, kContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    try {
                        popLaundryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<PopLaundryDTO>>() {
                        }.getType();
                        popLaundryDTOArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(kContext, LinearLayoutManager.VERTICAL, false);

                        binding.recyclerview.setLayoutManager(layoutManager);
                        popularFullLaundriesAdapter = new PopularFullLaundriesAdapter(kContext, popLaundryDTOArrayList);
                        binding.recyclerview.setAdapter(popularFullLaundriesAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {

                    ProjectUtils.showToast(kContext, msg);
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }

    }
}
