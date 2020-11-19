package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.model.WelcomeDTO;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;


public class WelcomeImageAdapter extends PagerAdapter {

    private final Context mContext;
    private final LayoutInflater layoutInflater;
    private final ClickListener listener;
    AppCompatImageView background;
    TextView desc;
    TextView title;
    ArrayList<WelcomeDTO> imageDTOArrayList;
    FancyButton registerBtn;

    public WelcomeImageAdapter(ArrayList<WelcomeDTO> imageDTOArrayList, Context mContext, ClickListener listener) {
        this.imageDTOArrayList = imageDTOArrayList;
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_welcomescreens, container, false);

        background = itemView.findViewById(R.id.background);
        title = itemView.findViewById(R.id.title);
        desc = itemView.findViewById(R.id.desc);
        registerBtn = itemView.findViewById(R.id.register_btn);

        Glide.with(mContext).load(imageDTOArrayList.get(position).getBackground()).into(background);
        title.setText(imageDTOArrayList.get(position).getHeading());
        desc.setText(imageDTOArrayList.get(position).getDesc());

        if (position == imageDTOArrayList.size() - 1) {
            registerBtn.setVisibility(View.VISIBLE);
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick();
                }
            });
        } else {
            registerBtn.setVisibility(View.GONE);
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public interface ClickListener {
        void onClick();
    }


    @Override
    public int getCount() {
        return imageDTOArrayList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }


}