package com.samyotech.laundry.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.samyotech.laundry.GlobalState;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentDynamicBinding;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.model.ItemDTO;
import com.samyotech.laundry.model.ItemListDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Schedule_Activity;
import com.samyotech.laundry.ui.adapter.ItemAdapter;

import java.util.ArrayList;

public class DynamicFragment extends Fragment {
    FragmentDynamicBinding binding;
    View view;
    int val;
    TextView c;
    Schedule_Activity schedule_activity;
    LinearLayoutManager linearLayoutManager;
    GlobalState globalState;
    ItemAdapter itemAdapter;
    CurrencyDTO currencyDTO;
    ArrayList<ItemListDTO> offerDTOS = new ArrayList<>();
    ItemListDTO itemListDTO;
    ItemDTO itemServiceDTO;
    int newval = 0;

    SharedPrefrence prefrence;

    public static DynamicFragment addfrag(int val, ItemListDTO itemListDTO, CurrencyDTO currencyDTO) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putSerializable("someInt", itemListDTO);
        args.putSerializable("someCurency", currencyDTO);
        args.putInt("Int", val);
        fragment.setArguments(args);

        return fragment;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dynamic, container, false);
        itemListDTO = (ItemListDTO) getArguments().getSerializable("someInt");
        currencyDTO = (CurrencyDTO) getArguments().getSerializable("someCurency");
        val = getArguments().getInt("Int");
        setData(itemListDTO, currencyDTO);
        globalState = (GlobalState) getActivity().getApplication();

        return binding.getRoot();
    }

    private void setData(ItemListDTO itemListDTO, CurrencyDTO currencyDTO) {

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvDetails.setLayoutManager(linearLayoutManager);
        itemAdapter = new ItemAdapter(getActivity(), itemListDTO.getServices(), schedule_activity, currencyDTO);
        binding.rvDetails.setAdapter(itemAdapter);

        newval = 1;

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        schedule_activity = (Schedule_Activity) context;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (newval > 0) {

        }

    }
}