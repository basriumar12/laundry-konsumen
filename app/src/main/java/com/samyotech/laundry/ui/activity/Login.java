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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
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
    private final int GOOGLE_SIGN_IN = 1;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mContext = Login.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, "ANDROID"));

//        genrate();
        setUiAction();

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //fixme update token
                .requestEmail()
                .build();
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        prefrence.setValue(Consts.DEVICE_TOKEN, token);
                        // Log and toast
                        Log.d(TAG, token);
                    }
                });
    }

    private void genrate() {
        newToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("tokensss", newToken);
    }

    public void setUiAction() {

        binding.login.setOnClickListener(this);
        binding.registerNow.setOnClickListener(this);
        binding.forgotPassword.setOnClickListener(this);
        binding.googleLogin.setOnClickListener(this);

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
                case R.id.google_login:
                    Intent signInIntent = googleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                    break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d("GOOGLE_", account.toString());
                googleLogin(account.getEmail());
            } else {
                Toast.makeText(this, "Gagal mendapatkan user detail", Toast.LENGTH_LONG).show();
            }
        } catch (ApiException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() );
            Log.w("GOOGLE_", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Login gagal, Silahkan coba beberapa menit lagi.", Toast.LENGTH_LONG).show();
        }
    }

    private void googleLogin(String email) {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.LOGIN_GOOGLE, getparmGoogle(email), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                    try {
                        doubleClick = false;
                        ProjectUtils.showToast(mContext, msg);
                        if (response.get("status").toString().equals("1")) {
                            userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                            prefrence.setParentUser(userDTO, Consts.USER_DTO);

                            prefrence.setBooleanValue(Consts.IS_REGISTERED, true);

                            Intent in = new Intent(mContext, Dashboard.class);
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        }
                    } catch (Exception e) {
                        doubleClick = true;
                        e.printStackTrace();
                    }

            }
        });
    }


    public void clickForSubmit() {
        if (!ProjectUtils.isEmailValid(binding.cetEmailADD.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_email));
        } /*else if (!ProjectUtils.isPasswordValid(binding.cetPasword.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        }*/ else {
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
                    showDialog(msg);
//                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public HashMap<String, String> getparmGoogle(String email) {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL, email);
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, "ANDROID"));
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.cetEmailADD));
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.cetPasword));
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, "ANDROID"));
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

    public void showDialog(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.auth_dialog, viewGroup, false);
        TextView text = dialogView.findViewById(R.id.text);
        text.setText(msg);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
