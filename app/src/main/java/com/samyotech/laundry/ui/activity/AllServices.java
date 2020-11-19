package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityAllServicesBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.ServicesDTO;
import com.samyotech.laundry.ui.adapter.AllServicesAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllServices extends AppCompatActivity {
    private final String TAG = AllServices.class.getSimpleName();
    ActivityAllServicesBinding binding;
    Context mContext;
    GridLayoutManager layoutManagerServ;
    AllServicesAdapter allServicesAdapter;
    ArrayList<ServicesDTO> getServic = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_services);
        mContext = AllServices.this;

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getAllServices();

    }

    private void getAllServices() {
        new HttpsRequest(Consts.GETALLSERVICE, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {

                    try {
                        getServic = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<ServicesDTO>>() {
                        }.getType();
                        getServic = new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);

                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void setData() {
        layoutManagerServ = new GridLayoutManager(this, 3);
        binding.recyclerview.setLayoutManager(layoutManagerServ);
        allServicesAdapter = new AllServicesAdapter(mContext, getServic);
        binding.recyclerview.setAdapter(allServicesAdapter);
    }


}
