package com.samyotech.laundry.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.ModelClass.ChatListDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.ChatListAdapter;
import com.samyotech.laundry.utils.CustomTextView;
import com.samyotech.laundry.utils.CustomTextViewBold;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatList extends AppCompatActivity {
    private String TAG = ChatList.class.getSimpleName();
    private RecyclerView rvChatList;
    private ChatListAdapter chatListAdapter;
    private ArrayList<ChatListDTO> chatList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextView tvNo;
    Context mContext;


    ImageView IVback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mContext=ChatList.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        setUiAction();
    }


    public void setUiAction() {
        tvNo = findViewById(R.id.tvNo);
        IVback = findViewById(R.id.IVback);
        rvChatList = findViewById(R.id.rvChatList);

        mLayoutManager = new LinearLayoutManager(mContext);
        rvChatList.setLayoutManager(mLayoutManager);

        chatListAdapter = new ChatListAdapter(mContext, chatList);
        rvChatList.setAdapter(chatListAdapter);

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mContext)) {
            getChat();

        } else {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
        }
    }

    public void getChat() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GETMESSAGEHISTORY, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    rvChatList.setVisibility(View.VISIBLE);
                    try {
                        chatList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ChatListDTO>>() {
                        }.getType();
                        chatList = (ArrayList<ChatListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    rvChatList.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void showData() {
        chatListAdapter = new ChatListAdapter(mContext, chatList);
        rvChatList.setAdapter(chatListAdapter);
    }


}
