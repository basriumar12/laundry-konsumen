package com.samyotech.laundry.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.samyotech.laundry.GlobalState;
import com.samyotech.laundry.ModelClass.CurrencyDTO;
import com.samyotech.laundry.ModelClass.ItemDTO;
import com.samyotech.laundry.ModelClass.PopLaundryDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityConfirmedOrderBinding;
import com.samyotech.laundry.interfaces.Consts;

import java.util.HashMap;

public class ConfirmedOrder extends AppCompatActivity {
    ActivityConfirmedOrderBinding binding;
    Context mContext;
    UserDTO userDTO;
    GlobalState globalState;
    PopLaundryDTO popLaundryDTO;
    ItemDTO itemServiceDTO;
    Dashboard dashboard;
    HashMap<String,String> parmsSubmit=new HashMap<>();
    CurrencyDTO currencyDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil. setContentView(this,R.layout.activity_confirmed_order);
       mContext=ConfirmedOrder.this;
        globalState= (GlobalState) getApplication();
        parmsSubmit = (HashMap<String, String>) getIntent().getSerializableExtra("map");


        itemServiceDTO = (ItemDTO) GlobalState.getInstance().itemServiceDTO();
        popLaundryDTO = (PopLaundryDTO) GlobalState.getInstance().getPopLaundryDTO();
        setData();

    }

    private void setData() {
        binding.ctvFinalAmountpaid.setText(globalState.getCost());
        binding.ctvShop.setText(popLaundryDTO.getShop_name());
        binding.ctvbOrdernum.setText(parmsSubmit.get(Consts.ORDER_ID));
        binding.ctvbPickupDate.setText(parmsSubmit.get(Consts.PICKUP_DATE)+" "+parmsSubmit.get(Consts.PICKUP_TIME));
        binding.rlgotostatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in= new Intent(mContext,Dashboard.class);
                in.putExtra(Consts.SCREEN_TAG,Consts.BOOKINGFRAGMENT);
                startActivity(in);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in= new Intent(mContext,Dashboard.class);
                startActivity(in);
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent in= new Intent(mContext,Dashboard.class);
        startActivity(in);

    }
}
