package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityChangPasswordBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ChangPassword extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ChangPassword.class.getCanonicalName();
    private final int checkValid = 1;
    AlertDialog.Builder builder1;
    private Context sContext;
    private ActivityChangPasswordBinding binding;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private boolean isHide = false;
    private String user_pub_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chang_password);
        sContext = ChangPassword.this;
        prefrence = SharedPrefrence.getInstance(sContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        user_pub_id = userDTO.getUser_id();
        builder1 = new AlertDialog.Builder(this);
        setUiAction();
    }

    private void setUiAction() {
        binding.changSave.setOnClickListener(this);
        binding.ivConfirmPass.setOnClickListener(this);
        binding.ivNewPass.setOnClickListener(this);
        binding.ivOldPass.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.chang_save:
                if (NetworkManager.isConnectToInternet(sContext)) {
                    submitForm();
                } else {
                    ProjectUtils.InternetAlertDialog(sContext);
                }
                break;
            case R.id.ivOldPass:
                if (isHide) {
                    binding.ivOldPass.setImageResource(R.drawable.eye);
                    binding.etOldPass.setTransformationMethod(null);
                    binding.etOldPass.setSelection(binding.etOldPass.getText().length());
                    isHide = false;
                } else {
                    binding.ivOldPass.setImageResource(R.drawable.passwordhide);
                    binding.etOldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etOldPass.setSelection(binding.etOldPass.getText().length());
                    isHide = true;
                }
                break;
            case R.id.ivNewPass:
                if (isHide) {
                    binding.ivNewPass.setImageResource(R.drawable.eye);
                    binding.etNewPass.setTransformationMethod(null);
                    binding.etNewPass.setSelection(binding.etNewPass.getText().length());
                    isHide = false;
                } else {
                    binding.ivNewPass.setImageResource(R.drawable.passwordhide);
                    binding.etNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etNewPass.setSelection(binding.etNewPass.getText().length());
                    isHide = true;
                }
                break;
            case R.id.ivConfirmPass:
                if (isHide) {
                    binding.ivConfirmPass.setImageResource(R.drawable.eye);
                    binding.etConfirmPass.setTransformationMethod(null);
                    binding.etConfirmPass.setSelection(binding.etConfirmPass.getText().length());
                    isHide = false;
                } else {
                    binding.ivConfirmPass.setImageResource(R.drawable.passwordhide);
                    binding.etConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etConfirmPass.setSelection(binding.etConfirmPass.getText().length());
                    isHide = true;
                }
                break;


        }

    }

    private void submitForm() {

        if (!validateCurrentPin()) {
            return;
        } else if (!validateNewPin()) {
            return;
        } else if (!validateNewPin()) {
            return;
        } else {
            checkpass();
        }
    }


    public boolean validateCurrentPin() {
        if (binding.etOldPass.getText().toString().trim().equalsIgnoreCase("")) {
            binding.etOldPass.setError(getString(R.string.enter_pass));
            binding.etOldPass.requestFocus();
            return false;
        } else {
            if (!ProjectUtils.isPasswordValid(binding.etOldPass.getText().toString().trim())) {
                binding.etOldPass.setError(getString(R.string.password_required_validation));
                binding.etOldPass.requestFocus();
                return false;
            } else {
                binding.etOldPass.setError(null);
                binding.etOldPass.clearFocus();
                return true;
            }
        }

    }


    public boolean validateNewPin() {
        if (binding.etNewPass.getText().toString().trim().equalsIgnoreCase("")) {
            binding.etNewPass.setError(getString(R.string.enter_new_pass));
            binding.etNewPass.requestFocus();
            return false;
        } else {
            if (!ProjectUtils.isPasswordValid(binding.etNewPass.getText().toString().trim())) {
                binding.etNewPass.setError(getString(R.string.new_pwd_required_validation));
                binding.etNewPass.requestFocus();
                return false;
            } else {
                binding.etNewPass.setError(null);
                binding.etNewPass.clearFocus();
                return true;
            }
        }

    }

    public void checkpass() {

        if (binding.etNewPass.getText().toString().trim().equals("")) {
            binding.etNewPass.setError(getString(R.string.new_pwd_required_validation));
        } else if (binding.etConfirmPass.getText().toString().trim().equals("")) {
            binding.etConfirmPass.setError(getString(R.string.confirm_pwd_required_validation1));
        } else if (!binding.etNewPass.getText().toString().trim().equals(binding.etConfirmPass.getText().toString().trim())) {
            binding.etConfirmPass.setError(getString(R.string.confirm_pwd_validation1));
        } else {
            binding.etConfirmPass.setError(null);//removes error
            binding.etConfirmPass.clearFocus();    //clear focus from edittext
            //ProjectUtils.showProgressDialog(sContext, true, getString(R.string.please_wait));
            changePassword();

        }


    }

    private void changePassword() {

        ProjectUtils.showProgressDialog(sContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.CHANGEPASSWORD, getParamsChangePass(), sContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(sContext, msg);
                    Intent in = new Intent(sContext, Login.class);
                    startActivity(in);
                    finishAffinity();

                } else {

                    ProjectUtils.showToast(sContext, msg);

                }
            }
        });

    }


    protected HashMap<String, String> getParamsChangePass() {
        HashMap<String, String> paramsChangePass = new HashMap<>();

        paramsChangePass.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.etOldPass));
        paramsChangePass.put(Consts.NEW_PASSWORD, ProjectUtils.getEditTextValue(binding.etNewPass));
        paramsChangePass.put(Consts.USER_ID, userDTO.getUser_id());

        ProjectUtils.showLog(TAG + "---Params --->", paramsChangePass.toString());

        return paramsChangePass;
    }

}
