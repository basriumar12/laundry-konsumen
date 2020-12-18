package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityManageProfileBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.ProjectUtils;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ManageProfile extends AppCompatActivity implements View.OnClickListener {
    public boolean checkAdd = true, doubleCheck = true;
    String TAG = ManageProfile.class.getSimpleName();
    ActivityManageProfileBinding binding;
    Context mContext;
    ProjectUtils projectUtils;
    NetworkManager networkManager;
    SharedPrefrence prefrence;
    UserDTO userDTO;
    HashMap<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_profile);
        mContext = ManageProfile.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        params.put(Consts.USER_ID, userDTO.getUser_id());

        setdata();
        binding.simpan.setOnClickListener(this);
        binding.location.setOnClickListener(this);
        binding.back.setOnClickListener(this);

    }

    private void setdata() {
        Log.e(TAG, "setdata: " + userDTO.getUser_id());

        binding.namaLengkap.setText(userDTO.getName());
        binding.nomorHp.setText(userDTO.getMobile());
        binding.email.setText(userDTO.getEmail());

        if (!userDTO.getAddress().equalsIgnoreCase("")) {
            binding.alamat.setText(userDTO.getAddress());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.simpan:

                if (!ProjectUtils.isEditTextFilled(binding.namaLengkap)) {
                    Toast.makeText(mContext, R.string.addname, Toast.LENGTH_SHORT).show();
                } else if (!ProjectUtils.isEditTextFilled(binding.alamat)) {
                    Toast.makeText(mContext, R.string.addAddress, Toast.LENGTH_SHORT).show();
                } else if (!ProjectUtils.isPhoneNumberValid(binding.nomorHp.getText().toString().trim())) {
                    Toast.makeText(mContext, R.string.val_num, Toast.LENGTH_SHORT).show();
                } else {
                    if (checkAdd) {
                        params.put(Consts.LATITUDE, userDTO.getLatitude());
                        params.put(Consts.LONGITUDE, userDTO.getLatitude());
                    }
                    params.put(Consts.ADDRESS, ProjectUtils.getEditTextValue(binding.alamat));

                    getParams();
                    updateProfile();
                }
                break;
            case R.id.location:
                findPlace();
                break;
            case R.id.back:
                onBackPressed();
                break;


        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                try {
                    getAddress(data.getDoubleExtra(Consts.LATITUDE, 0.0), data.getDoubleExtra(Consts.LONGITUDE, 0.0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateProfile() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));

        new HttpsRequest(Consts.USERUPDATE, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Log.e(TAG, "backResponse: " + response.toString());
                        ProjectUtils.showToast(mContext, msg);
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                        Intent in = new Intent(mContext, Dashboard.class);
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }

            }
        });
    }

    private HashMap<String, String> getParams() {
        if (!ProjectUtils.isEditTextFilled(binding.namaLengkap))
            params.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.namaLengkap));
        else
            Toast.makeText(mContext, R.string.addname, Toast.LENGTH_SHORT).show();

        params.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.namaLengkap));
        params.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.nomorHp));
        params.put(Consts.COUNTRY_CODE, "62");
        params.put(Consts.DEVICE_TYPE, "ANDROID");
        params.put(Consts.DEVICE_TOKEN, "ANDROID");

        return params;
    }


    private void findPlace() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                .withLocation(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)),
                        Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)))
                .build(mContext);

        startActivityForResult(locationPickerIntent, 101);
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
            binding.alamat.setText(obj.getAddressLine(0));

            params.put(Consts.ADDRESS, obj.getAddressLine(0));
            params.put(Consts.LATITUDE, String.valueOf(obj.getLatitude()));
            params.put(Consts.LONGITUDE, String.valueOf(obj.getLongitude()));


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkAdd = true;

    }


}
