package com.samyotech.laundry.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.ModelClass.OfferDTO;
import com.samyotech.laundry.ModelClass.PopLaundryDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentOfferShopBinding;
import com.samyotech.laundry.databinding.FragmentOffersBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.ui.activity.Schedule_Activity;
import com.samyotech.laundry.ui.activity.ServiceAcitivity;
import com.samyotech.laundry.ui.adapter.OffersAdapter;
import com.samyotech.laundry.ui.adapter.OffersOtherAdapter;
import com.samyotech.laundry.utils.EndlessRecyclerOnScrollListener;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferShopFragment extends Fragment {

    String TAG = OfferShopFragment.class.getSimpleName();

    FragmentOfferShopBinding binding;
    LinearLayoutManager linearLayoutManager;
    ArrayList<OfferDTO> offerDTOS=new ArrayList<>();
    HashMap<String,String> params=new HashMap<>();
    OffersOtherAdapter offersAdapter;

    private Bundle bundle;
    PopLaundryDTO popLaundryDTO;
    ServiceAcitivity serviceAcitivity;
    boolean checkClick=true;
    private int currentVisibleItemCount = 0;
    int page = 20;
    boolean request = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate( inflater,R.layout.fragment_offer_shop, container, false);


        bundle = this.getArguments();
        popLaundryDTO = (PopLaundryDTO) bundle.getSerializable(Consts.SHOPDTO);
        getOffer();


        binding.cvSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkClick) {

                Intent in = new Intent(getActivity(), Schedule_Activity.class);
                in.putExtra(Consts.SHOPDTO,popLaundryDTO);
                startActivity(in);
                    checkClick=false;
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvoffer.setLayoutManager(linearLayoutManager);

/*
        binding.rvoffer.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager)linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page, int totalItemCount) {
                currentVisibleItemCount = totalItemCount;
                if (request) {
                    page = page + 2;


                }else {
                    page = 2;
                    getOffer();

                }

            }
        });
        */
        getOffer();


        return binding.getRoot();


    }

    private void getOffer() {

        ProjectUtils.getProgressDialog(getActivity());
        params.put(Consts.Count, String.valueOf(page));
        new HttpsRequest(Consts.GETALLOFFER, params,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        offerDTOS = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<OfferDTO>>() {
                        }.getType();
                        offerDTOS = (ArrayList<OfferDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);


                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });


    }

    private void setData() {


        offersAdapter = new OffersOtherAdapter(getActivity(), offerDTOS);
        binding.rvoffer.setAdapter(offersAdapter);
        binding.rvoffer.smoothScrollToPosition(currentVisibleItemCount);
        binding.rvoffer.scrollToPosition(currentVisibleItemCount-1);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        serviceAcitivity = (ServiceAcitivity) context;
    }



    @Override
    public void onResume() {
        super.onResume();
        checkClick=true;
    }


}
