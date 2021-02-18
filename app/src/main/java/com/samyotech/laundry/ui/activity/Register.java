package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityRegisterBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.RegisterNewDto;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.ProjectUtils;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = Register.class.getSimpleName();
    private SharedPrefrence prefrence;
    Context mContext;
    ActivityRegisterBinding binding;
    boolean doubleClick = true;
    boolean numCheck = false;
    private boolean isHide = false;
    HashMap<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 20-Nov-20 add alamat field

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mContext = Register.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        prefrence = SharedPrefrence.getInstance(this);

        setUiAction();

    }

    public void setUiAction() {

        binding.cvRegister.setOnClickListener(this);
        binding.loginNow.setOnClickListener(this);
        binding.ivOldPass.setOnClickListener(this);
        binding.ivNewPass.setOnClickListener(this);
        binding.location.setOnClickListener(this);

        binding.cetNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9) {
                    if (isValidPhoneNumber(String.valueOf(s))) {
                        boolean status = validateUsing_libphonenumber(String.valueOf(s));
                        // Toast.makeText(mContext, "Valid Phone Number (libphonenumber)", Toast.LENGTH_SHORT).show();
                        //  tvIsValidPhone.setText("Valid Phone Number (libphonenumber)");
                        /*
                            binding.cetNumber.setError(getResources().getString(R.string.entValNumber));
                            binding.cetNumber.requestFocus();*/
                        //   tvIsValidPhone.setText("Invalid Phone Number (libphonenumber)");
                        numCheck = status;
                    } else {

                        numCheck = true;
                        // tvIsValidPhone.setText("Invalid Phone Number (isValidPhoneNumber)");
                    }

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_now:
                if (doubleClick) {
                    Intent intent_forgot_password = new Intent(mContext, Login.class);
                    startActivity(intent_forgot_password);
                    doubleClick = false;
                }
                break;

            case R.id.cvRegister:
                if (doubleClick)
                    clickForSubmit();
                break;

            case R.id.ivOldPass:
                if (isHide) {
                    binding.ivOldPass.setImageResource(R.drawable.eye);
                    binding.cetPassword1.setTransformationMethod(null);
                    binding.cetPassword1.setSelection(binding.cetPassword1.getText().length());
                    isHide = false;
                } else {
                    binding.ivOldPass.setImageResource(R.drawable.passwordhide);
                    binding.cetPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.cetPassword1.setSelection(binding.cetPassword1.getText().length());
                    isHide = true;
                }
                break;
            case R.id.ivNewPass:
                if (isHide) {
                    binding.ivNewPass.setImageResource(R.drawable.eye);
                    binding.cetPassword2.setTransformationMethod(null);
                    binding.cetPassword2.setSelection(binding.cetPassword2.getText().length());
                    isHide = false;
                } else {
                    binding.ivNewPass.setImageResource(R.drawable.passwordhide);
                    binding.cetPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.cetPassword2.setSelection(binding.cetPassword2.getText().length());
                    isHide = true;
                }
                break;
            case R.id.location:
                findPlace();
                break;
        }
    }

    private void findPlace() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                //.withLocation(41.4036299, 2.1743558)
                .build(mContext);

        startActivityForResult(locationPickerIntent, 101);
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

    public void clickForSubmit() {
        if (!ProjectUtils.isEditTextFilled(binding.cetName)) {
            showSickbar(getResources().getString(R.string.val_name));
        } else if (!ProjectUtils.isEmailValid(binding.cetEmail.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_email));
        } else if (!ProjectUtils.isPhoneNumberValid(binding.cetNumber.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_num));
        } else if (!numCheck) {
            showSickbar(getResources().getString(R.string.val_num));
        } else if (!ProjectUtils.isPasswordValid(binding.cetPassword1.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        } else if (!ProjectUtils.isPasswordValid(binding.cetPassword2.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        } else if (ProjectUtils.getEditTextValue(binding.cetPassword1).equals(ProjectUtils.getEditTextValue(binding.cetPassword2))) {
            if (NetworkManager.isConnectToInternet(mContext)) {
                registerUser();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }

        } else showSickbar(getResources().getString(R.string.pass_notmatch));


    }


    public void registerUser() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        Log.d(TAG, "registerUser: " + getParams().toString());
        new HttpsRequest(Consts.SIGNUP, getParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        RegisterNewDto registerNewDto = new Gson().fromJson(response.getJSONObject("data").toString(), RegisterNewDto.class);
                        prefrence.setParentUserRegister(registerNewDto, Consts.REGISTER_DTO);

                        doubleClick = false;
                        ProjectUtils.showToast(mContext, "Berhasil register, otp akan dikirim ke nomor yang telah di daftarkan");
                        Intent in = new Intent(mContext, OtpActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    doubleClick = true;
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    public HashMap<String, String> getParams() {
        params.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.cetName));
        params.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.cetEmail));
        params.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.cetPassword2));
        params.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.cetNumber));
        params.put(Consts.ADDRESS, ProjectUtils.getEditTextValue(binding.alamat));
        params.put(Consts.COUNTRY_CODE, "62");
        params.put(Consts.DEVICE_TYPE, "ANDROID");
        params.put(Consts.DEVICE_TOKEN, "ANDROID");
        return params;
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbar.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        snackbarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        snackbar.show();
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private boolean validateUsing_libphonenumber(String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(mContext);
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt("+62"));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (NumberParseException e) {
            System.err.println(e);
        }
        try {
            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            if (isValid) {
                String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                // Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
                return true;
            } else {
                // Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
