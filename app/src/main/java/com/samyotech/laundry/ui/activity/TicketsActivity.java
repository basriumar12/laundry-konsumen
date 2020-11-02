package com.samyotech.laundry.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.ModelClass.TicketDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityTicketsBinding;
import com.samyotech.laundry.databinding.DailogAddTicketBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.TicketAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TicketsActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    private String TAG = TicketsActivity.class.getSimpleName();
    private TicketAdapter ticketAdapter;
    private ArrayList<TicketDTO> ticketDTOSList;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    ActivityTicketsBinding binding;
    private HashMap<String, String> parmsadd = new HashMap<>();
    private HashMap<String, String> parmsGet = new HashMap<>();
    DailogAddTicketBinding binding1;
    private Dialog dialog;
    JSONObject jsonObject=new JSONObject();
    JSONObject getjsonObject=new JSONObject();
    String ranDom="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_tickets);

        mContext=TicketsActivity.this;

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        binding.back.setOnClickListener(this);
        binding.ivPost.setOnClickListener(this);

        try {
            parmsadd.put(Consts.USER_ID,userDTO.getUser_id());
            parmsGet.put(Consts.USER_ID,userDTO.getUser_id());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mContext)) {
            getTicket();

        } else {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_connection));
        }
    }

    public void getTicket() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.TIKETLIST, parmsGet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
//                    binding.tvNo.setVisibility(View.GONE);
                    binding.RVhistorylist.setVisibility(View.VISIBLE);
                    try {
                        ticketDTOSList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<TicketDTO>>() {
                        }.getType();
                        ticketDTOSList = (ArrayList<TicketDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
//                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.RVhistorylist.setVisibility(View.GONE);
                }
            }
        });
    }


    public void showData() {
        binding.RVhistorylist.setLayoutManager(new LinearLayoutManager(mContext));
        ticketAdapter = new TicketAdapter(TicketsActivity.this,mContext, ticketDTOSList, userDTO);
        binding.RVhistorylist.setAdapter(ticketAdapter);
    }




    public void dialogshow() {/*
        dialog = new Dialog(mContext*//*, android.R.style.Theme_Dialog*//*);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_add_ticket);*/

        dialog = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding1 = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.dailog_add_ticket, null, false);
        dialog.setContentView(binding1.getRoot());
        //   final CommentBinding binding=DataBindingUtil.setContentView(this,R.layout.comment);
        dialog.show();
        dialog.setCancelable(true);



        binding1.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding1.ctvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    public void submitForm() {
        if (!validateReason()) {
            return;
        } else if (!validateDescription()) {
            return;
        } else {
            addTicket();

        }
    }

    public boolean validateReason() {
        if (binding1.etReason.getText().toString().trim().equalsIgnoreCase("")) {
            binding1.etReason.setError(getResources().getString(R.string.val_title));
            binding1.etReason.requestFocus();
            return false;
        } else {
            binding1.etReason.setError(null);
            binding1.etReason.clearFocus();
            return true;
        }
    }

    public boolean validateDescription() {
        if (binding1.etDescription.getText().toString().trim().equalsIgnoreCase("")) {
            binding1.etDescription.setError(getResources().getString(R.string.val_description));
            binding1.etDescription.requestFocus();
            return false;
        } else {
            binding1.etDescription.setError(null);
            binding1.etDescription.clearFocus();
            return true;
        }
    }


    public void addTicket() {
        Random otp1 = new Random();
        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 3; count++) {
            builder.append(otp1.nextInt(10));
        }
        ranDom = builder.toString();

        try {


            parmsadd.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(binding1.etDescription));
            parmsadd.put(Consts.TITLE, ProjectUtils.getEditTextValue(binding1.etReason));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADDTIKET, parmsadd, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    dialog.dismiss();
                    ProjectUtils.showToast(mContext, msg);
                    getTicket();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.ivPost:
                dialogshow();
                break;
        }

    }
}
