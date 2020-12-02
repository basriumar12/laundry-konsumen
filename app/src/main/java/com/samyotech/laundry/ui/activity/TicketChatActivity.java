package com.samyotech.laundry.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.TicketCommentDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.TicketChatAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class TicketChatActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = TicketChatActivity.class.getSimpleName();
    private final String id = "";
    private final HashMap<String, String> parmsGet = new HashMap<>();
    IntentFilter intentFilter = new IntentFilter();
    private ListView lvComment;
    private ImageView sendBtn, IVback;
    private TicketChatAdapter adapterViewCommentTicket;
    private ArrayList<TicketCommentDTO> ticketCommentDTOSList;
    private InputMethodManager inputManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmojiconEditText edittextMessage;
    private RelativeLayout relative;
    private Context mContext;
    private TextView tvNameHedar;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Consts.BROADCAST)) {
                getComment();
                Log.e("BROADCAST", "BROADCAST");
               /* if (Projectutils.mInterstitialAd != null && Projectutils.mInterstitialAd.isLoaded()) {
                    Projectutils.mInterstitialAd.show();
                } else {
                    Projectutils.initInterAdd(ShoppingDhashActivity.this);

                }*/
            }
        }
    };
    private String ticket_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_chat);
        mContext = TicketChatActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        intentFilter.addAction(Consts.BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);

        if (getIntent().hasExtra(Consts.TICKET_ID)) {

            ticket_id = getIntent().getStringExtra(Consts.TICKET_ID);

            parmsGet.put(Consts.TIKET_ID, ticket_id);
            parmsGet.put(Consts.USER_ID, userDTO.getUser_id());


        }
        setUiAction();

    }

    public void setUiAction() {
        tvNameHedar = findViewById(R.id.header_title);
        relative = findViewById(R.id.relative);
        edittextMessage = findViewById(R.id.edittextMessage);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        lvComment = findViewById(R.id.lvComment);
        sendBtn = findViewById(R.id.sendBtn);
        IVback = findViewById(R.id.back);
        sendBtn.setOnClickListener(this);
        IVback.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
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
        if (!validateMessage()) {
            return;
        } else {
            try {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
            }
            if (NetworkManager.isConnectToInternet(mContext)) {
                doComment();
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
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GETTIKETCOMMENT, parmsGet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    try {
                        ticketCommentDTOSList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<TicketCommentDTO>>() {
                        }.getType();
                        ticketCommentDTOSList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
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
        adapterViewCommentTicket = new TicketChatAdapter(mContext, ticketCommentDTOSList, userDTO);
        lvComment.setAdapter(adapterViewCommentTicket);
        lvComment.setSelection(ticketCommentDTOSList.size() - 1);
    }

    public void doComment() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADDTIKETCOMMENT, getParamDO(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    edittextMessage.setText("");
                    getComment();
                } else {
                }
            }
        });
    }

    public HashMap<String, String> getParamDO() {
        HashMap<String, String> values = new HashMap<>();
        values.put(Consts.TIKET_ID, ticket_id);
        values.put(Consts.MESSAGE, ProjectUtils.getEditTextValue(edittextMessage));
        Log.e("POST", values.toString());
        return values;
    }

}