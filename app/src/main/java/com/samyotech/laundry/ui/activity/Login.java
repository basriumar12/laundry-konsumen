package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityLoginBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = Login.class.getSimpleName();
    Context mContext;
    ActivityLoginBinding binding;
    boolean doubleClick = true;
    String newToken = "";
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private SharedPreferences firebase;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mContext = Login.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));

//        genrate();
        setUiAction();
    }

    private void genrate() {
        newToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("tokensss", newToken);


    }

    public void setUiAction() {

        binding.login.setOnClickListener(this);
        binding.registerNow.setOnClickListener(this);
        binding.forgotPassword.setOnClickListener(this);

        binding.cetPasword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    binding.cetPasword.setText(result);
                    binding.cetPasword.setSelection(result.length());
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        } else {
            lastClickTime = SystemClock.elapsedRealtime();
            switch (v.getId()) {

                case R.id.login:
//                    if(doubleClick)
                    clickForSubmit();
                    break;
                case R.id.register_now:
//                    if(doubleClick){
                    Intent intent_register_now = new Intent(mContext, Register.class);
                    startActivity(intent_register_now);
                    doubleClick = false;
//            }
                    break;
                case R.id.forgot_password:
//                    if(doubleClick) {
                    Intent intent_forgot_password = new Intent(mContext, ForgotPassword.class);
                    startActivity(intent_forgot_password);
                    doubleClick = false;
//            }
                    break;
            }

        }
    }


    public void clickForSubmit() {
        if (!ProjectUtils.isEmailValid(binding.cetEmailADD.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_email));
        } else if (!ProjectUtils.isPasswordValid(binding.cetPasword.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                login();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }


    public void login() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.LOGIN, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        doubleClick = false;
                        ProjectUtils.showToast(mContext, msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);

                        Intent in = new Intent(mContext, Dashboard.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        doubleClick = true;
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.cetEmailADD));
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.cetPasword));
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbar.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        snackbarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.closeMsg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        doubleClick = true;
    }
}
