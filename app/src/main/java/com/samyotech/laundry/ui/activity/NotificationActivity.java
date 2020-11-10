package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityNotificationBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.NotificationDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.AdapterNotifcation;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class NotificationActivity extends AppCompatActivity {

    private final String TAG = NotificationActivity.class.getCanonicalName();
    ActivityNotificationBinding binding;
    Context mContext;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    AdapterNotifcation adapterNotifcation;
    RecyclerView.LayoutManager layoutManager;
    List<NotificationDTO> originalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        mContext = NotificationActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        setData();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData(0, binding.pending);
            }
        });

        binding.dikonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData(1, binding.dikonfirmasi);
            }
        });
        binding.diambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData(2, binding.diambil);
            }
        });
        binding.diproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData(3, binding.diproses);
            }
        });
        binding.dikirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData(4, binding.dikirim);
            }
        });
        binding.sampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData(5, binding.sampai);
            }
        });
    }

    private void setData() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, userDTO.getUser_id());
        new HttpsRequest(Consts.GET_NOTIFICATION, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if (flag) {

                    Type getpetDTO = new TypeToken<List<NotificationDTO>>() {
                    }.getType();
                    List<NotificationDTO> items = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                    originalList.addAll(items);
                    filterData(0, binding.pending);

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void showData() {
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(mContext));
        adapterNotifcation = new AdapterNotifcation(mContext, originalList);
        binding.rvNotification.setAdapter(adapterNotifcation);
        adapterNotifcation.notifyDataSetChanged();
    }

    private void filterData(final int i, FancyButton btn) {
        ViewGroup parent = (ViewGroup) btn.getParent();
        for (int j = 0; j < parent.getChildCount(); j++) {
            FancyButton childAt = (FancyButton) parent.getChildAt(j);
            childAt.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        btn.setTextColor(ContextCompat.getColor(this, R.color.white));

        List<NotificationDTO> filtered = Stream.of(originalList).filter(new Predicate<NotificationDTO>() {
            @Override
            public boolean test(NotificationDTO value) {
                return value.getStatus().equals(String.valueOf(i));
            }
        }).toList();
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(mContext));
        adapterNotifcation = new AdapterNotifcation(mContext, filtered);
        binding.rvNotification.setAdapter(adapterNotifcation);
        adapterNotifcation.notifyDataSetChanged();
    }
}
