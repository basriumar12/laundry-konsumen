package com.samyotech.laundry.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentOffersBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.OfferDTO;
import com.samyotech.laundry.ui.activity.NotificationActivity;
import com.samyotech.laundry.ui.adapter.OffersOtherAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OffersFragment extends Fragment {

    private final int currentVisibleItemCount = 0;
    String TAG = OffersFragment.class.getSimpleName();
    FragmentOffersBinding binding;
    LinearLayoutManager linearLayoutManager;
    ArrayList<OfferDTO> offerDTOS = new ArrayList<>();
    HashMap<String, String> params = new HashMap<>();
    OffersOtherAdapter offersAdapter;
    int page = 20;
    boolean request = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        binding.rvoffer.setLayoutManager(linearLayoutManager);

      /*  binding.rvoffer.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page, int totalItemCount) {
                currentVisibleItemCount = totalItemCount;
                if (request) {
                    page = page + 2;
                    getOffer();


                }else {
                    page = 2;
                    getOffer();

                }

            }
        });
*/
        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in4 = new Intent(getActivity(), NotificationActivity.class);
                startActivity(in4);
            }
        });

        getOffer();

        return binding.getRoot();


    }

    private void getOffer() {

        ProjectUtils.getProgressDialog(getActivity());
        params.put(Consts.Count, String.valueOf(page));
        new HttpsRequest(Consts.GETALLOFFER, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        offerDTOS = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<OfferDTO>>() {
                        }.getType();
                        offerDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        setData();
                        request = true;
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
        binding.rvoffer.scrollToPosition(currentVisibleItemCount - 1);
    }


}
