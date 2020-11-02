package com.samyotech.laundry.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentProfileBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.ChangPassword;
import com.samyotech.laundry.ui.activity.ChatList;
import com.samyotech.laundry.ui.activity.Dashboard;
import com.samyotech.laundry.ui.activity.LanguageSelection;
import com.samyotech.laundry.ui.activity.Login;
import com.samyotech.laundry.ui.activity.ManageProfile;
import com.samyotech.laundry.ui.activity.NotificationActivity;
import com.samyotech.laundry.ui.activity.TicketsActivity;
import com.samyotech.laundry.utils.ImageCompression;
import com.samyotech.laundry.utils.MainFragment;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private static final int RESULT_OK = -1;
    FragmentProfileBinding binding;

    private String TAG = ProfileFragment.class.getSimpleName();
    Dashboard dashboard;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    BottomSheet.Builder builder;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    Uri picUri;
    ImageCompression imageCompression;
    String pathOfImage;
    File file;
    Bitmap bm;
    int fileAvailable = 0;
    public ArrayList<File> files = new ArrayList<>();
    HashMap<String, File> fileHashMap = new HashMap<>();
    HashMap<String, String> hashMap = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        hashMap.put(Consts.USER_ID,userDTO.getUser_id());
        setUIAction();
        return binding.getRoot();
    }

    private void setUIAction() {

        Glide.with(getActivity())
                .load(userDTO.getImage())
                .error(R.drawable.ic_avatar)
                .into(binding.civimage);
            camera();

        binding.tvName.setText(userDTO.getName());
        binding.tvAddress.setText(userDTO.getAddress());

//        mMaxScrollSize = binding.appbar.getTotalScrollRange();

        binding.ctvDelete.setOnClickListener(this);
        binding.rlEditProfile.setOnClickListener(this);
        binding.ctvprofile.setOnClickListener(this);
        binding.ctvMyOrder.setOnClickListener(this);
        binding.ctvLogout.setOnClickListener(this);
        binding.ctvSupport.setOnClickListener(this);
        binding.ctvChat.setOnClickListener(this);
        binding.ctvnotification.setOnClickListener(this);
        binding.ctvChangePassword.setOnClickListener(this);
        binding.ctvChangeLanguage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.ctvLogout:
        alertDialogLogout();
            break;
        case R.id.rlEditProfile:
            builder.show();
            break;
        case R.id.ctvDelete:
        alertDialogDelete();
            break;
        case R.id.ctvprofile:
        Intent in123= new Intent(getActivity(), ManageProfile.class);
        startActivity(in123);
            break;
        case R.id.ctvMyOrder:

            Intent in12= new Intent(getActivity(),Dashboard.class);
            in12.putExtra(Consts.SCREEN_TAG,Consts.BOOKINGFRAGMENT);
            startActivity(in12);            break;
        case R.id.ctvSupport:
            Intent in =new Intent(getActivity(), TicketsActivity.class);
            startActivity(in);
            break;
        case R.id.ctvChat:
            Intent in1 =new Intent(getActivity(), ChatList.class);
            startActivity(in1);
            break;
        case R.id.ctvnotification:
            Intent in2 =new Intent(getActivity(), NotificationActivity.class);
            startActivity(in2);
            break;
        case R.id.ctvChangePassword:
            Intent in3 =new Intent(getActivity(), ChangPassword.class);
            startActivity(in3);
            break;
        case R.id.ctvChangeLanguage:
            Intent in4 =new Intent(getActivity(), LanguageSelection.class);
            startActivity(in4);
            break;
    }
    }


    public void logout() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.LOGOUT, getparm(), getActivity()).stringPost(TAG, new Helper() {
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


    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID,userDTO.getUser_id());
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }
    public void alertDialogLogout() {
        new AlertDialog.Builder(dashboard)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.logoutMsg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                logout();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dashboard = (Dashboard) context;
    }

/*
    @Override
    public void onResume() {
        super.onResume();

        Glide.with(getActivity())
                .load(userDTO.getImage())
                .error(R.drawable.ic_avatar)
                .into(binding.civimage);
        camera();
    }*/

    public void deleteAccount() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.DELETEACCOUNT, getparm(), getActivity()).stringPost(TAG, new Helper() {
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
    }/*

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            binding.rlProPic.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            binding.rlProPic.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }*/




    private void camera() {

        builder = new BottomSheet.Builder(getActivity()).sheet(R.menu.menu_card);
        builder.title("Please select image");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);

                                        picUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),"com.samyotech.laundry"+".fileprovider",file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }



                                    sharedPrefrence.setValue(Consts.IMAGE, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Please select image."), PICK_FROM_GALLERY);


                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });


    }

    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Consts.APP_NAME);
        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            fileAvailable=1;
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);
                                fileHashMap.put(Consts.IMAGE,file);

                                updateProfile();
                               /* Log.e("image", imagePath);
                                paramsFile.put(Const.IMAGE, file);*/

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {



                            fileAvailable=1;
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);
                                fileHashMap.put(Consts.IMAGE,file);

                                updateProfile();

                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(sharedPrefrence.getValue(Consts.IMAGE));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(sharedPrefrence
                        .getValue(Consts.IMAGE));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri imgUri = data.getData();

                startCropping(imgUri, CROP_GALLERY_IMAGE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }




    }

    private void updateProfile() {
        new HttpsRequest(Consts.USERUPDATE,hashMap,fileHashMap,getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if(flag){



                    userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                    sharedPrefrence.setParentUser(userDTO, Consts.USER_DTO);

                    sharedPrefrence.setBooleanValue(Consts.IS_REGISTERED, true);



                    Glide.with(getActivity()).load(userDTO.getImage())
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.civimage);

                }else {
                    ProjectUtils.showToast(getActivity(),msg);
                }
            }
        });


    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }


}
