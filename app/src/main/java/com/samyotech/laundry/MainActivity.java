package com.samyotech.laundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Dashboard;
import com.samyotech.laundry.ui.activity.LanguageSelection;
import com.samyotech.laundry.ui.activity.WelcomeScreens;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SharedPrefrence prefference;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions = new String[]{Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PRIVILEGED,
            Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean cameraAccepted, storageAccepted, accessNetState,  call_privilage, callPhone,fineLoc, corasLoc;
    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 3000;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefference = SharedPrefrence.getInstance(MainActivity.this);
        mContext=MainActivity.this;
    }



    Runnable mTask = new Runnable() {
        @Override
        public void run() {


            if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {
                Intent in = new Intent(mContext, Dashboard.class);
                language(prefference.getValue(Consts.LANGUAGE));
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }else {

                Intent in = new Intent(mContext, LanguageSelection.class);
                in.putExtra(Consts.TYPE,1);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }


        }

    };


    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissions(MainActivity.this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            handler.postDelayed(mTask, SPLASH_TIME_OUT);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                    storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                    accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);


                    call_privilage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CALL_PRIVILAGE, call_privilage);

                    callPhone = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CALL_PHONE, callPhone);

                    fineLoc = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);

                    corasLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);
                    handler.postDelayed(mTask, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void language(String language) {
        String languageToLoad = language; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());

    }


}
