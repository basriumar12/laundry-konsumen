package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityForgotPasswordBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private String TAG = ForgotPassword.class.getSimpleName();
    Context mContext;
    ActivityForgotPasswordBinding binding;
    boolean doubleClick=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ForgotPassword.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        setUiAction();
    }

    public void setUiAction() {


        binding.cvGyNp.setOnClickListener(this);
        binding.registerNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvGyNp:
                if(doubleClick)
                clickForSubmit();

                break;
            case R.id.register_now:
                if(doubleClick){
                Intent intent_register_now = new Intent(mContext, Register.class);
                startActivity(intent_register_now);
                    doubleClick=false;
                }
                break;
        }
    }



    public void clickForSubmit() {
        if (!ProjectUtils.isEmailValid(binding.cetEmailADD.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_email));
        }  else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                forgetpassword();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }


    }

    private void forgetpassword() {

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.FORGOTPASSWORD, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        doubleClick=false;
                        ProjectUtils.showToast(mContext, msg);
/*

                        Intent in = new Intent(mContext, CheckEmail.class);
                        startActivity(in);*/
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    doubleClick=true;
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }
    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.cetEmailADD));
        Log.e(TAG + " Forget", parms.toString());
        return parms;

    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbarF, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

}
