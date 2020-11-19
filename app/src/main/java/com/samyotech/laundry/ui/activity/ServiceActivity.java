package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityServiceAcitivityBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.NearBYDTO;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.RatingDTO;
import com.samyotech.laundry.model.ServicesDTO;
import com.samyotech.laundry.model.ShopServicesDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.fragment.AboutFragment;
import com.samyotech.laundry.ui.fragment.OfferShopFragment;
import com.samyotech.laundry.ui.fragment.ServicesFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceActivity extends AppCompatActivity {
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    //    private int mMaxScrollSize;
    private final boolean mIsAvatarShown = true;
    String TAG = ServiceActivity.class.getSimpleName();
    ServicesFragment servicesFragment = new ServicesFragment();
    OfferShopFragment offerShopFragment = new OfferShopFragment();
    AboutFragment aboutFragment = new AboutFragment();
    ArrayList<PopLaundryDTO> popLaundryDTO = new ArrayList<>();
    PopLaundryDTO object;
    ServicesDTO servicesDTO;
    NearBYDTO nearBYDTO;
    ActivityServiceAcitivityBinding binding;
    HashMap<String, String> params = new HashMap<>();
    UserDTO userDTO;
    SharedPrefrence sharedPrefrence;
    Context mContext;
    float rating = 0;
    RatingDTO ratingDTO;
    private Bundle bundle;
    private ShopServicesDTO shopServicesDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_acitivity);
        mContext = ServiceActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        if (getIntent().hasExtra(Consts.SERVICEDTO)) {
            servicesDTO = (ServicesDTO) getIntent().getSerializableExtra(Consts.SERVICEDTO);
            setupUi();
        }

        if (getIntent().hasExtra(Consts.SHOPSERVICEDTO)) {
            shopServicesDTO = (ShopServicesDTO) getIntent().getSerializableExtra(Consts.SHOPSERVICEDTO);
            setupUi2();
        }
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupUi2() {
        binding.ivShopName.setText(shopServicesDTO.getService_name());
        binding.des.setText(shopServicesDTO.getDescription());
        Glide.with(this).load(Consts.DEV_URL + shopServicesDTO.getImage()).into(binding.serviceIcon);
    }

    private void setupUi() {
        binding.ivShopName.setText(servicesDTO.getService_name());
        binding.des.setText(servicesDTO.getDescription());
        Glide.with(this).load(Consts.DEV_URL + servicesDTO.getImage()).into(binding.serviceIcon);
    }
}