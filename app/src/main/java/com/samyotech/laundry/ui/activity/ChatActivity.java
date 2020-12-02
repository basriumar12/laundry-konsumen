package com.samyotech.laundry.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.GetCommentDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.ChatAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = ChatActivity.class.getSimpleName();
    private final String id = "";
    private final HashMap<String, String> parmsGet = new HashMap<>();
    private final HashMap<String, File> paramsFile = new HashMap<>();
    boolean clickcheck = true;
    IntentFilter intentFilter = new IntentFilter();
    private final boolean actions_container_visible = false;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    byte[] resultByteArray;
    File file;
    Bitmap bitmap = null;
    private ListView lvComment;
    private final boolean img_container_visible = false;
    HashMap<String, String> params = new HashMap<>();
    private ArrayList<GetCommentDTO> getCommentDTOList;
    private InputMethodManager inputManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmojiconEditText edittextMessage;
    private RelativeLayout relative;
    private Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Consts.BROADCAST)) {
                getComment();
                Log.e("BROADCAST", "BROADCAST");

            }
        }
    };
    private EditText etMessage;
    private ImageView buttonSendMessage, IVback;
    private ChatAdapter adapterViewComment;
    private FrameLayout mContainerImg;
    private TextView tvNameHedar;
    private ImageView mActionImage, mPreviewImg, mDeleteImg, mActionContainerImg;
    private String ar_id = "", ar_name = "", image = "";
    private ImageView galleryBtn;
    private ImageView cameraBtn;
    private ImageView closeImgBtn;
    private ImageView previewImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = ChatActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        intentFilter.addAction(Consts.BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);

        if (getIntent().hasExtra(Consts.SHOP_ID)) {

            ar_id = getIntent().getStringExtra(Consts.SHOP_ID);
            ar_name = getIntent().getStringExtra(Consts.SHOP_NAME);

            parmsGet.put(Consts.TO_USER_ID, ar_id);
            parmsGet.put(Consts.USER_ID, userDTO.getUser_id());
            image = getIntent().getStringExtra(Consts.IMAGE);

            params.put(Consts.TO_USER_ID, ar_id);
        }
        if (getIntent().hasExtra(Consts.TO_USER_ID)) {

            ar_id = getIntent().getStringExtra(Consts.TO_USER_ID);
            ar_name = getIntent().getStringExtra(Consts.NAME);
            image = getIntent().getStringExtra(Consts.IMAGE);

            parmsGet.put(Consts.TO_USER_ID, ar_id);
            parmsGet.put(Consts.USER_ID, userDTO.getUser_id());
            params.put(Consts.TO_USER_ID, ar_id);
        }
        setUiAction();

    }

    public void setUiAction() {

        mContainerImg = findViewById(R.id.container_img);
        closeImgBtn = findViewById(R.id.close_img_btn);
        closeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainerImg.setVisibility(View.GONE);
                file = null;
            }
        });
        previewImg = findViewById(R.id.preview_img);
        mContainerImg.setVisibility(View.GONE);

        relative = findViewById(R.id.relative);
        tvNameHedar = findViewById(R.id.header_title);
        edittextMessage = findViewById(R.id.edittextMessage);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        lvComment = findViewById(R.id.lvComment);
        buttonSendMessage = findViewById(R.id.sendBtn);
        IVback = findViewById(R.id.back);
        galleryBtn = findViewById(R.id.gallery_btn);
        galleryBtn.setOnClickListener(this);
        cameraBtn = findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(this);
        buttonSendMessage.setOnClickListener(this);
        IVback.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {

                        Log.e("Runnable", "FIRST");
                        if (NetworkManager.isConnectToInternet(mContext)) {
                            swipeRefreshLayout.setRefreshing(true);
                            getComment();

                        } else {
                            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                        }
                    }
                }
        );
        tvNameHedar.setText(ar_name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            file = ImagePicker.Companion.getFile(data);
            paramsFile.put(Consts.IMAGE, file);
            mContainerImg.setVisibility(View.VISIBLE);
            Glide.with(this).load(file).into(previewImg);
        }
    }

    public boolean validateMessage() {
        if (edittextMessage.getText().toString().trim().length() <= 0) {
            edittextMessage.setError(getResources().getString(R.string.val_comment));
            edittextMessage.requestFocus();
            return false;
        } else {
            edittextMessage.setError(null);
            edittextMessage.clearFocus();
            return true;
        }
    }

    public void submit() {
        if (validateMessage()) {
            mContainerImg.setVisibility(View.GONE);
            try {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (NetworkManager.isConnectToInternet(mContext)) {
                if (clickcheck) {
                    doComment();
                }
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                submit();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.camera_btn:
                ImagePicker.Companion.with(this)
                        .cameraOnly()
                        .crop()
                        .compress(512)
                        .start();
                break;
            case R.id.gallery_btn:
                ImagePicker.Companion.with(this)
                        .galleryOnly()
                        .crop()
                        .compress(512)
                        .start();
                break;
        }
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getComment();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    public void getComment() {
        //ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GETMESSAGE, parmsGet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    try {
                        getCommentDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<GetCommentDTO>>() {
                        }.getType();
                        clickcheck = true;
                        getCommentDTOList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                }
            }
        });
    }

    public void showData() {
        adapterViewComment = new ChatAdapter(mContext, getCommentDTOList, userDTO, userDTO.getImage(), image);
        lvComment.setAdapter(adapterViewComment);
        lvComment.setSelection(getCommentDTOList.size() - 1);
    }

    public void doComment() {
        params.put(Consts.FROM_USER_ID, userDTO.getUser_id());
        params.put(Consts.MESSAGE, ProjectUtils.getEditTextValue(edittextMessage));

        if (file != null) {
            params.put(Consts.MEDIA, file.getPath());
        } else {
            params.put(Consts.MEDIA, "");
        }

        new HttpsRequest(Consts.SETMESSAGE, params, paramsFile, mContext)
                .imagePost(TAG, new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        ProjectUtils.pauseProgressDialog();
                        if (flag) {
                            edittextMessage.setText("");
//                    hideImageContainer();
                            getComment();

                            file = null;
                            pathOfImage = "";
                        } else {
                        }
                    }
                });
    }
}