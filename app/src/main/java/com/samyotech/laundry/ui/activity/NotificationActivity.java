package com.samyotech.laundry.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.ModelClass.NotificationDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityNotificationBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.AdapterNotifcation;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private String TAG = NotificationActivity.class.getCanonicalName();
    ActivityNotificationBinding binding;
    Context mContext;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    AdapterNotifcation adapterNotifcation;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<NotificationDTO> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_notification);
        mContext=NotificationActivity.this;
        sharedPrefrence=SharedPrefrence.getInstance(mContext);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        setData();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setData() {
        HashMap<String,String>params=new HashMap<>();
        params.put(Consts.USER_ID,userDTO.getUser_id());
        new HttpsRequest(Consts.GET_NOTIFICATION,params,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if(flag){


                    arrayList = new ArrayList<>();
                    Type getpetDTO = new TypeToken<List<NotificationDTO>>() {
                    }.getType();
                    arrayList = (ArrayList<NotificationDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                    showData();

                }else {
                    ProjectUtils.showToast(mContext,msg);
                }
            }
        });
    }

    private void showData() {
        layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(mContext));
        adapterNotifcation = new AdapterNotifcation(mContext, arrayList);
        binding.rvNotification.setAdapter(adapterNotifcation);

    }


}
