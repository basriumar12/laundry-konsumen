package com.samyotech.laundry.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.samyotech.laundry.ModelClass.NearBYDTO;
import com.samyotech.laundry.ModelClass.PopLaundryDTO;
import com.samyotech.laundry.ModelClass.RatingDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityServiceAcitivityBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.fragment.AboutFragment;
import com.samyotech.laundry.ui.fragment.OfferShopFragment;
import com.samyotech.laundry.ui.fragment.OffersFragment;
import com.samyotech.laundry.ui.fragment.ServicesFragment;
import com.samyotech.laundry.utils.ImageCompression;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceAcitivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    String TAG = ServiceAcitivity.class.getSimpleName();
    ServicesFragment servicesFragment=new ServicesFragment();
    OfferShopFragment offerShopFragment=new OfferShopFragment();
    AboutFragment aboutFragment=new AboutFragment();
    private Bundle bundle;
    private ViewPagerAdapter adapter;
    private int mMaxScrollSize;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
        ArrayList<PopLaundryDTO> popLaundryDTO= new ArrayList<>();
        PopLaundryDTO object;

    PopLaundryDTO popLaundryDTOs;
    NearBYDTO nearBYDTO;
    ActivityServiceAcitivityBinding binding;
    HashMap<String,String>params=new HashMap<>();
    UserDTO userDTO;
    SharedPrefrence sharedPrefrence;
    Context mContext;
    float rating=0;
    RatingDTO ratingDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil.setContentView(this,R.layout.activity_service_acitivity);
    mContext=ServiceAcitivity.this;
    sharedPrefrence=SharedPrefrence.getInstance(mContext);
    userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);


    if(getIntent().hasExtra(Consts.SHOPDTO)){
        popLaundryDTOs=(PopLaundryDTO) getIntent().getSerializableExtra(Consts.SHOPDTO);
        setUiAction1();
        getRating1();

    }
    if(getIntent().hasExtra(Consts.NEARSHOPDTO)){
        nearBYDTO=(NearBYDTO) getIntent().getSerializableExtra(Consts.NEARSHOPDTO);


        object= new PopLaundryDTO();
        object.setS_no(nearBYDTO.getS_no());
        object.setShop_id(nearBYDTO.getShop_id());
        object.setUser_id(nearBYDTO.getUser_id());
        object.setShop_name(nearBYDTO.getShop_name());
        object.setCountry_code(nearBYDTO.getCountry_code());
        object.setMobile(nearBYDTO.getMobile());
        object.setAddress(nearBYDTO.getAddress());
        object.setLatitude(nearBYDTO.getLatitude());
        object.setLongitude(nearBYDTO.getLongitude());
        object.setOpening_time(nearBYDTO.getOpening_time());
        object.setClosing_time(nearBYDTO.getClosing_time());
        object.setDescription(nearBYDTO.getDescription());
        object.setImage(nearBYDTO.getImage());
        object.setStatus(nearBYDTO.getStatus());
        object.setCreated_at(nearBYDTO.getCreated_at());
        object.setUpdated_at(nearBYDTO.getUpdated_at());
        popLaundryDTO.add(object);






        getRating();
        setUiAction();
    }




    }

    private void getRating() {
        params.put(Consts.USER_ID,userDTO.getUser_id());
        params.put(Consts.SHOP_ID,popLaundryDTO.get(0).getShop_id());
        new HttpsRequest(Consts.GETRATING,params,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if(flag){

                    ratingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), RatingDTO.class);


                    binding.ratingbar.setRating(Float.valueOf(ratingDTO.getAverage()));
                    binding.ctvReview.setText("("+ratingDTO.getAverage()+" "+getResources().getText(R.string.review));
                }else {
                    ProjectUtils.showToast(mContext,msg);
                }
            }
        });
    }
    private void getRating1() {
        params.put(Consts.USER_ID,userDTO.getUser_id());
        params.put(Consts.SHOP_ID,popLaundryDTOs.getShop_id());
        new HttpsRequest(Consts.GETRATING,params,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                if(flag){

                    ratingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), RatingDTO.class);


                    binding.ratingbar.setRating(Float.valueOf(ratingDTO.getAverage()));
                    binding.ctvReview.setText("("+ratingDTO.getAverage()+" "+getResources().getText(R.string.review));
                }else {
                    ProjectUtils.showToast(mContext,msg);
                }
            }
        });
    }

    private void setUiAction() {
        bundle = new Bundle();
        bundle.putSerializable(Consts.SHOPDTO, popLaundryDTO.get(0));
        binding.ivShopName.setText(popLaundryDTO.get(0).getShop_name());
        binding.ctvAddress.setText(popLaundryDTO.get(0).getAddress());
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mMaxScrollSize = binding.appbar.getTotalScrollRange();
        servicesFragment.setArguments(bundle);
        offerShopFragment.setArguments(bundle);
        aboutFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(aboutFragment, "About");
        adapter.addFragment(servicesFragment, "Services");
        adapter.addFragment(offerShopFragment, "Offers");

        binding.pager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.pager);
    }

    private void setUiAction1() {
        bundle = new Bundle();
        bundle.putSerializable(Consts.SHOPDTO, popLaundryDTOs);
        binding.ivShopName.setText(popLaundryDTOs.getShop_name());
        binding.ctvAddress.setText(popLaundryDTOs.getAddress());
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mMaxScrollSize = binding.appbar.getTotalScrollRange();
        servicesFragment.setArguments(bundle);
        offerShopFragment.setArguments(bundle);
        aboutFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(aboutFragment, "About");
        adapter.addFragment(servicesFragment, "Services");
        adapter.addFragment(offerShopFragment, "Offers");

        binding.pager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.pager);
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            binding.ivBanner.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            binding.ivBanner.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }



}
