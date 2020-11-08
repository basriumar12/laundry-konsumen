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
import com.samyotech.laundry.ModelClass.WelcomeDTO;
import com.samyotech.laundry.R;

import java.util.ArrayList;


public class WelcomeImageAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    AppCompatImageView background;
    TextView desc;
    TextView title;
    ArrayList<WelcomeDTO> imageDTOArrayList;


    public WelcomeImageAdapter(ArrayList<WelcomeDTO> imageDTOArrayList, Context mContext) {
        this.imageDTOArrayList = imageDTOArrayList;
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_welcomescreens, container, false);

        background = (AppCompatImageView) itemView.findViewById(R.id.background);
        title = (TextView) itemView.findViewById(R.id.title);
        desc = (TextView) itemView.findViewById(R.id.desc);

        Glide.with(mContext).load(imageDTOArrayList.get(position).getBackground()).into(background);
        title.setText(imageDTOArrayList.get(position).getHeading());
        desc.setText(imageDTOArrayList.get(position).getDesc());

        container.addView(itemView);
        return itemView;
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