package com.samyotech.laundry.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.laundry.ModelClass.GetCommentDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.utils.CustomTextView;
import com.samyotech.laundry.utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by VARUN on 01/01/19.
 */
public class AdapterViewComment extends BaseAdapter {
    private Context mContext;
    private ArrayList<GetCommentDTO> getCommentDTOList;
    private UserDTO userDTO;

    private ImageView ivImageD;
    private CustomTextView tvCloseD, tvNameD;
    private Dialog dialogImg;
    String mypic="",otherpic="";

    public AdapterViewComment(Context mContext, ArrayList<GetCommentDTO> getCommentDTOList, UserDTO userDTO,String mypic,String otherpic) {
        this.mContext = mContext;
        this.getCommentDTOList = getCommentDTOList;
        this.userDTO = userDTO;
        this.mypic = mypic;
        this.otherpic = otherpic;

    }

    @Override
    public int getCount() {
        return getCommentDTOList.size();
    }

    @Override
    public Object getItem(int postion) {
        return getCommentDTOList.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        //ViewHolder holder = new ViewHolder();
        if (!getCommentDTOList.get(position).getTo_user_id().equalsIgnoreCase(userDTO.getUser_id())) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment_my, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment, parent, false);

        }

        CustomTextView textViewMessage = (CustomTextView) view.findViewById(R.id.textViewMessage);
        CustomTextView textViewTime = (CustomTextView) view.findViewById(R.id.textViewTime);
        CustomTextView tvName = (CustomTextView) view.findViewById(R.id.tvName);
        ImageView ivView = (ImageView) view.findViewById(R.id.ivView);
        ImageView imageViewmessageTicks = (ImageView) view.findViewById(R.id.imageViewmessageTicks);
/*
        if (getCommentDTOList.get(position).getChat_type().equalsIgnoreCase("2")) {
            ivView.setVisibility(View.VISIBLE);
        } else {
            ivView.setVisibility(View.GONE);
        }*/
        if (!getCommentDTOList.get(position).getTo_user_id().equalsIgnoreCase(userDTO.getUser_id())) {
            Glide.with(mContext).
                    load(mypic)
                    .placeholder(R.drawable.ic_user_dummy)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivView);
        } else {
            Glide.with(mContext).
                load(otherpic)
                .placeholder(R.drawable.ic_user_dummy)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivView);
        }


        ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogshare(position);
            }
        });
        textViewMessage.setText(getCommentDTOList.get(position).getMessage());

        try {
            textViewTime.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(getCommentDTOList.get(position).getCreated_at()))));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void dialogshare(int pos) {
        dialogImg = new Dialog(mContext, android.R.style.Theme_Dialog);
        dialogImg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogImg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogImg.setContentView(R.layout.dailog_image_view);

/*
        tvCloseD = (CustomTextView) dialogImg.findViewById(R.id.tvCloseD);
        tvNameD = (CustomTextView) dialogImg.findViewById(R.id.tvNameD);

        ivImageD = (ImageView) dialogImg.findViewById(R.id.ivImageD);*/
        dialogImg.show();
        dialogImg.setCancelable(false);

        tvCloseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImg.dismiss();

            }
        });

    }


}
