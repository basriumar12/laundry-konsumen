package com.samyotech.laundry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.ModelClass.WelcomeDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.utils.CustomTextView;
import com.samyotech.laundry.utils.CustomTextViewMuliBold;
import com.samyotech.laundry.utils.CustomTextViewpoppins_bold;

import java.util.ArrayList;


public class WelcomeImageAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    AppCompatImageView imageView,botmImage;
    CustomTextView desc;
    CustomTextViewpoppins_bold    lonndes;
    CustomTextViewpoppins_bold title;
    ArrayList<WelcomeDTO>imageDTOArrayList;



    public WelcomeImageAdapter(ArrayList<WelcomeDTO>imageDTOArrayList, Context mContext) {
        this.imageDTOArrayList = imageDTOArrayList;
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_welcomescreens, container, false);

        imageView = (AppCompatImageView) itemView.findViewById(R.id.topImage);
        botmImage = (AppCompatImageView) itemView.findViewById(R.id.bottomImage);
        title = (CustomTextViewpoppins_bold) itemView.findViewById(R.id.mainTitle);
        desc = (CustomTextView) itemView.findViewById(R.id.desc);

        imageView.setImageResource(imageDTOArrayList.get(position).getTopImage());
        botmImage.setImageResource(imageDTOArrayList.get(position).getBtmImage());

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
        container.removeView((ConstraintLayout) object);
    }


}