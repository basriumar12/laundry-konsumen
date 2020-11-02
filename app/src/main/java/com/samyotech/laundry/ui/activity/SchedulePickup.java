package com.samyotech.laundry.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivitySchdulePickupBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.schibstedspain.leku.LocationPickerActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class SchedulePickup extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    ActivitySchdulePickupBinding binding;
    private double lats = 0;
    private double longs = 0;
    int i = 0;
    String address = "", landmark = "";
    boolean doubleClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schdule_pickup);
        mContext = SchedulePickup.this;

        binding.cvHome.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.cvWork.setOnClickListener(this);
        binding.cvOther.setOnClickListener(this);
        binding.rdbtn.setOnClickListener(this);
        binding.rdbtnWork.setOnClickListener(this);
        binding.rdbtnOther.setOnClickListener(this);
        binding.rlSelectAddress.setOnClickListener(this);
    }


    private void findPlace() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                //.withLocation(41.4036299, 2.1743558)
                .build(mContext);

        startActivityForResult(locationPickerIntent, 101);
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(SchedulePickup.this, Locale.getDefault());
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

            if (i == 1) {
                binding.address.setText(obj.getAddressLine(0));
                address = obj.getAddressLine(0);
                if (obj.getLocality().equals(null)) {
                } else {
                    landmark = obj.getLocality();
                }


            } else if (i == 2) {
                binding.addressWork.setText(obj.getAddressLine(0));
                address = obj.getAddressLine(0);
                if (obj.getLocality().equals(null)) {
                } else {
                    landmark = obj.getLocality();
                }
            } else {
                binding.addressOther.setText(obj.getAddressLine(0));
                address = obj.getAddressLine(0);
                if (obj.getLocality().equals(null)) {
                } else {
                    landmark = obj.getLocality();
                }

            }

            lats = lat;
            longs = lng;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.cvHome:
            case R.id.rdbtn:
                findPlace();
                i = 1;
                binding.rdbtn.setChecked(true);
                binding.rdbtnWork.setChecked(false);
                binding.rdbtnOther.setChecked(false);
                break;
            case R.id.cvWork:
            case R.id.rdbtnWork:
                findPlace();
                i = 2;
                binding.rdbtn.setChecked(false);
                binding.rdbtnWork.setChecked(true);
                binding.rdbtnOther.setChecked(false);
                break;
            case R.id.cvOther:
            case R.id.rdbtnOther:
                findPlace();
                i = 3;
                binding.rdbtn.setChecked(false);
                binding.rdbtnWork.setChecked(false);
                binding.rdbtnOther.setChecked(true);
                break;
            case R.id.rlSelectAddress:

                if (binding.rdbtn.isChecked() || binding.rdbtnWork.isChecked() || binding.rdbtnOther.isChecked()) {
                    if (doubleClick) {
                        doubleClick = false;
                        Intent in = new Intent(mContext, Schedule_PickupDate.class);
                        in.putExtra("map", getParams());
                        startActivity(in);
                    }
                } else {
                    Toast.makeText(mContext, R.string.val_Address, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        doubleClick = true;
    }

    private HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.SHIPPING_ADDRESS, address);
        params.put(Consts.LATITUDE, String.valueOf(lats));
        if (landmark.equalsIgnoreCase("")) {
            params.put(Consts.LANDMARK, "");
        } else {
            params.put(Consts.LANDMARK, landmark);
        }
        params.put(Consts.LONGITUDE, String.valueOf(longs));

        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                try {
                    getAddress(data.getDoubleExtra(LATITUDE, 0.0), data.getDoubleExtra(LONGITUDE, 0.0));


                } catch (Exception e) {

                }
            }
        }
    }
}
