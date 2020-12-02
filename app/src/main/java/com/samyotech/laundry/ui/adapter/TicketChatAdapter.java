package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.model.TicketCommentDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.utils.ProjectUtils;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by VARUN on 01/01/19.
 */

public class TicketChatAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<TicketCommentDTO> ticketCommentDTOSList;
    private final UserDTO userDTO;

    public TicketChatAdapter(Context mContext, ArrayList<TicketCommentDTO> ticketCommentDTOSList, UserDTO userDTO) {
        this.mContext = mContext;
        this.ticketCommentDTOSList = ticketCommentDTOSList;
        this.userDTO = userDTO;

    }

    @Override
    public int getCount() {
        return ticketCommentDTOSList.size();
    }

    @Override
    public Object getItem(int postion) {
        return ticketCommentDTOSList.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //ViewHolder holder = new ViewHolder();
        TicketCommentDTO item = ticketCommentDTOSList.get(position);
        if (item.getIs_admin().equalsIgnoreCase("1")) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment_my, parent, false);
        }

        EmojiconTextView textViewMessage = view.findViewById(R.id.textViewMessage);
        TextView textViewTime = view.findViewById(R.id.textViewTime);
        TextView status = view.findViewById(R.id.status);
        if (item.getIs_read().equals("1")) {
            status.setText("Dibaca");
        } else {
            status.setText("");
        }

        textViewMessage.setText(item.getMessage());
//        tvName.setText(ticketCommentDTOSList.get(position).get());

        try {
            textViewTime.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(item.getCreated_at()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
