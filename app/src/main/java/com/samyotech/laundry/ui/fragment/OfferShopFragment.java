package com.samyotech.laundry.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentOfferShopBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.OfferDTO;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.ui.activity.ShopAcitivity;
import com.samyotech.laundry.ui.adapter.OffersOtherAdapter;
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

    private final int currentVisibleItemCount = 0;
    String TAG = OfferShopFragment.class.getSimpleName();
    FragmentOfferShopBinding binding;
    LinearLayoutManager linearLayoutManager;
    ArrayList<OfferDTO> offerDTOS = new ArrayList<>();
    HashMap<String, String> params = new HashMap<>();
    OffersOtherAdapter offersAdapter;
    PopLaundryDTO popLaundryDTO;
    ShopAcitivity serviceAcitivity;
    boolean checkClick = true;
    int page = 20;
    boolean request = false;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_shop, container, false);

        bundle = this.getArguments();
        popLaundryDTO = (PopLaundryDTO) bundle.getSerializable(Consts.SHOPDTO);
        getOffer();

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
        params.put(Consts.SHOP_ID, popLaundryDTO.getShop_id());
        new HttpsRequest(Consts.GETOFFERFORLAUNDRYSHOP, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        offerDTOS = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<OfferDTO>>() {
                        }.getType();
                        offerDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        binding.rvoffer.setVisibility(View.VISIBLE);
                        binding.ctvnodata.setVisibility(View.GONE);
                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    binding.rvoffer.setVisibility(View.GONE);
                    binding.ctvnodata.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void setData() {

        offersAdapter = new OffersOtherAdapter(getActivity(), offerDTOS);
        binding.rvoffer.setAdapter(offersAdapter);
        binding.rvoffer.smoothScrollToPosition(currentVisibleItemCount);
        binding.rvoffer.scrollToPosition(currentVisibleItemCount - 1);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        serviceAcitivity = (ShopAcitivity) context;
    }


    @Override
    public void onResume() {
        super.onResume();
        checkClick = true;
    }


}
