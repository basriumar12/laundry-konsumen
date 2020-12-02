package com.samyotech.laundry.ui.adapter;
/**
 * Created by VARUN on 01/01/19.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.ChatListDTO;
import com.samyotech.laundry.ui.activity.ChatActivity;
import com.samyotech.laundry.utils.ProjectUtils;

import java.util.ArrayList;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ChatListDTO> chatList;


    public ChatListAdapter(Context mContext, ArrayList<ChatListDTO> chatList) {
        this.mContext = mContext;
        this.chatList = chatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvTitle.setText(chatList.get(position).getUser_name());
        holder.tvMsg.setText(chatList.get(position).getMessage());
        try {
            holder.tvDate.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(chatList.get(position).getUpdated_at()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ChatActivity.class);
                in.putExtra(Consts.TO_USER_ID, chatList.get(position).getUser_id());
                in.putExtra(Consts.NAME, chatList.get(position).getUser_name());
                in.putExtra(Consts.IMAGE, chatList.get(position).getUser_image());
                mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {

        return chatList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvDay, tvDate, tvMsg;
        public LinearLayout container;

        public MyViewHolder(View view) {
            super(view);

            container = view.findViewById(R.id.container);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDate = view.findViewById(R.id.tvDate);
            tvMsg = view.findViewById(R.id.tvMsg);

        }
    }


}