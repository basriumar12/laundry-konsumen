package com.samyotech.laundry.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.samyotech.laundry.R;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.RegisterNewDto;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.ProjectUtils;
import com.samyotech.laundry.widget.OtpReceivedInterface;
import com.samyotech.laundry.widget.PinEntryEditText;
import com.samyotech.laundry.widget.SmsBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class OtpActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    private int RESOLVE_HINT = 2;
    HashMap<String, String> params = new HashMap<>();
    FancyButton btn_konfirmasi;
    private SharedPrefrence prefrence;
    PinEntryEditText txtPinEntry;
    private String userid;
    private RegisterNewDto registerNewDto;
    ProgressBar progress_circular;
    private PinView otpView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        prefrence = SharedPrefrence.getInstance(this);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
        progress_circular = findViewById(R.id.progress_circular);
        otpView = findViewById(R.id.pinview);
        params = new HashMap<>();
        activasi();
        resenOtp();
        cekRuntime();
        startSMSListener();


        registerNewDto = prefrence.getParentUserRegister(Consts.REGISTER_DTO);
        userid = registerNewDto.getUserId();

        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        getHintPhoneNumber();


    }




    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {
                    Log.e("TAG","sukses sms start");
               }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                Log.e("TAG","error sms start");
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  < – will need to process phone number string
                    Log.e("TAG", "onReceive: data "+credential.getId());

                }
            }
        }

    }


    void cekRuntime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 100);
        }
    }


    public void getHintPhoneNumber() {
        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();

        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void activasi() {





        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpView.getText().toString().isEmpty()){
                    ProjectUtils.showToast(getBaseContext(), "Otp tidak bisa kosong");
                } else

                progress_circular.setVisibility(View.VISIBLE);
                Log.e("TAG", " urlpin --->" + otpView.getText().toString());
                new HttpsRequest(Consts.OTPSMS, getBaseContext(), userid, otpView.getText().toString()).stringOTP("TAG", new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {

                        progress_circular.setVisibility(View.GONE);
                        if (flag) {

                            try {
                                finish();
                                String message = response.getJSONObject("message").toString();
                                ProjectUtils.showToast(getBaseContext(), msg);

                            } catch (Exception e) {
                                e.getMessage();
                            }


                        } else {
                            ProjectUtils.showToast(getBaseContext(), msg);
                        }
                    }
                });
            }
        });

    }

    private void resenOtp() {

        TextView tv_kirim_ulang = findViewById(R.id.tv_kirim_ulang);
        tv_kirim_ulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress_circular.setVisibility(View.VISIBLE);
                new HttpsRequest(Consts.RESENDOTP, getBaseContext(), userid, "").stringResendOTP("TAG", new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {


                        progress_circular.setVisibility(View.GONE);
                        if (flag) {

                            try {
                                startSMSListener();
                                ProjectUtils.showToast(getBaseContext(), msg);


                            } catch (Exception e) {
                                e.getMessage();
                            }


                        } else {
                            ProjectUtils.showToast(getBaseContext(), msg);
                        }
                    }
                });

            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ProjectUtils.showToast(getBaseContext(), "koneksi failed ");
    }

    @Override
    public void onOtpReceived(String otp) {
        Log.e("TAG","otp "+otp);
        otpView.setText("");
        otpView.setText(otp);
    }

    @Override
    public void onOtpTimeout() {
        ProjectUtils.showToast(getBaseContext(), "Klik kirim ulang otp");

    }
}