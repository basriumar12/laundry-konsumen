package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.ModelClass.GetBannerDTO;
import com.samyotech.laundry.R;

import java.util.ArrayList;


public class ImageAdapter extends PagerAdapter {

    private final Context mContext;
    private final LayoutInflater layoutInflater;
    ImageView imageView, ivBackImage;
    TextView desc;
    TextView lonndes;
    TextView title;
    ArrayList<GetBannerDTO> imageDTOArrayList;


    public ImageAdapter(ArrayList<GetBannerDTO> imageDTOArrayList, Context mContext) {
        this.imageDTOArrayList = imageDTOArrayList;
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.viewpager_sliding_screens, container, false);

        imageView = itemView.findViewById(R.id.image);
        title = itemView.findViewById(R.id.tv_title);
        desc = itemView.findViewById(R.id.short_description);
        lonndes = itemView.findViewById(R.id.long_description);
        Glide.with(mContext).load(imageDTOArrayList.get(position).getImage()).placeholder(R.drawable.laundryshop).into(imageView);

        title.setText(imageDTOArrayList.get(position).getTitle());
        desc.setText(imageDTOArrayList.get(position).getShort_description());
        lonndes.setText(imageDTOArrayList.get(position).getLong_description());


        container.addView(itemView);


        return itemView;
    }


    @Override
    public int getCount() {
        return imageDTOArrayList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }


}