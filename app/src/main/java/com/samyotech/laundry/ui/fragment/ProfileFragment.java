package com.samyotech.laundry.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentProfileBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.About;
import com.samyotech.laundry.ui.activity.ChangPassword;
import com.samyotech.laundry.ui.activity.ChatList;
import com.samyotech.laundry.ui.activity.Dashboard;
import com.samyotech.laundry.ui.activity.Login;
import com.samyotech.laundry.ui.activity.ManageProfile;
import com.samyotech.laundry.ui.activity.NotificationActivity;
import com.samyotech.laundry.ui.activity.TicketsActivity;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final int RESULT_OK = -1;
    private final String TAG = ProfileFragment.class.getSimpleName();
    FragmentProfileBinding binding;
    Dashboard dashboard;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    HashMap<String, File> fileHashMap = new HashMap<>();
    HashMap<String, String> hashMap = new HashMap<>();
    private BottomSheetFragment bottomSheetFragment;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        hashMap.put(Consts.USER_ID, userDTO.getUser_id());
        setUIAction();
        return binding.getRoot();
    }

    private void setUIAction() {

        Glide.with(requireActivity())
                .load(Consts.BASE_URL + "assets/images/user/" + userDTO.getImage())
                .error(R.drawable.profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivAvtaimg);
        Glide.with(requireActivity())
                .load(Consts.BASE_URL + "assets/images/user/background/" + userDTO.getBackground())
                .error(R.drawable.cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivBanner);

//        camera();

        binding.tvName.setText(userDTO.getName());

//        mMaxScrollSize = binding.appbar.getTotalScrollRange();

        binding.ctvprofile.setOnClickListener(this);
        binding.ctvChangePassword.setOnClickListener(this);
        binding.ctvnotification.setOnClickListener(this);
        binding.ctvChat.setOnClickListener(this);
        binding.ctvSupport.setOnClickListener(this);
        binding.ctvAbout.setOnClickListener(this);
        binding.ctvLogout.setOnClickListener(this);

        binding.updatePhoto.setOnClickListener(this);
        binding.updateBackground.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ctvLogout:
                alertDialogLogout();
                break;
            case R.id.ctvprofile:
                Intent in123 = new Intent(getActivity(), ManageProfile.class);
                startActivity(in123);
                break;
            case R.id.ctvSupport:
                Intent in = new Intent(getActivity(), TicketsActivity.class);
                startActivity(in);
                break;
            case R.id.ctvChat:
                Intent in1 = new Intent(getActivity(), ChatList.class);
                startActivity(in1);
                break;
            case R.id.ctvnotification:
                Intent in2 = new Intent(getActivity(), NotificationActivity.class);
                startActivity(in2);
                break;
            case R.id.ctvChangePassword:
                Intent in3 = new Intent(getActivity(), ChangPassword.class);
                startActivity(in3);
                break;
            case R.id.ctvAbout:
                Intent in4 = new Intent(getActivity(), About.class);
                startActivity(in4);
                break;
            case R.id.updatePhoto:
//                builder.show();
                showBs("image");
                break;
            case R.id.updateBackground:
//                builder.show();
                showBs("background");
                break;
        }
    }


    public void logout() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.LOGOUT, hashMap, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        sharedPrefrence.clearAllPreferences();
                        Intent i = new Intent(dashboard, Login.class);
                        i.putExtra("finish", true);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        dashboard.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public void alertDialogLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = requireView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_logout, viewGroup, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        FancyButton cancel = dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        FancyButton ok = dialogView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                logout();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dashboard = (Dashboard) context;
    }

    public void deleteAccount() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.DELETEACCOUNT, hashMap, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        sharedPrefrence.clearAllPreferences();
                        dashboard.finish();
                        Intent i = new Intent(dashboard, Login.class);
                        i.putExtra("finish", true);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dashboard.finish();
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }


    public void alertDialogDelete() {
        new AlertDialog.Builder(dashboard)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.deleteMsg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        deleteAccount();
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

    private void showBs(String type) {
        this.type = type;
        bottomSheetFragment = new BottomSheetFragment(new BottomSheetFragment.ClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(int which) {
                switch (which) {
                    case R.id.camera:
                        ImagePicker.Companion.with(ProfileFragment.this)
                                .cameraOnly()
                                .crop()
                                .compress(512)
                                .start();
                        break;
                    case R.id.gallery:
                        ImagePicker.Companion.with(ProfileFragment.this)
                                .galleryOnly()
                                .crop()
                                .compress(512)
                                .start();
                        break;
                }
            }
        });
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (type.equals("image")) {
                File file = ImagePicker.Companion.getFile(data);
                fileHashMap.put(Consts.IMAGE, file);
                hashMap.put(Consts.IMAGE, file.getPath());
                if (bottomSheetFragment != null) {
                    bottomSheetFragment.dismiss();
                }
                updateProfile();
            } else if (type.equals("background")) {
                File file = ImagePicker.Companion.getFile(data);
                fileHashMap.put(Consts.BACKGROUND, file);
                hashMap.put(Consts.BACKGROUND, file.getPath());
                if (bottomSheetFragment != null) {
                    bottomSheetFragment.dismiss();
                }
                updateProfile();
            }
        }
    }

    private void updateProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(requireActivity(), R.style.CustomAlertDialog);
        progressDialog.setMessage("loading");
        progressDialog.show();
        new HttpsRequest(Consts.USERUPDATE, hashMap, fileHashMap, getActivity())
                .imagePost(TAG, new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                        progressDialog.dismiss();
                        if (flag) {

                            userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                            sharedPrefrence.setParentUser(userDTO, Consts.USER_DTO);

                            sharedPrefrence.setBooleanValue(Consts.IS_REGISTERED, true);

                            Glide.with(requireActivity())
                                    .load(Consts.BASE_URL + "assets/images/user/" + userDTO.getImage())
                                    .error(R.drawable.ic_avatar)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivAvtaimg);
                            Glide.with(requireActivity())
                                    .load(Consts.BASE_URL + "assets/images/user/background/" + userDTO.getBackground())
                                    .error(R.drawable.banner_img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivBanner);

                        } else {
                            ProjectUtils.showToast(getActivity(), msg);
                        }
                    }
                });


    }
}
