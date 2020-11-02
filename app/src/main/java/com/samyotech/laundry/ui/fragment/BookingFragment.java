package com.samyotech.laundry.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.ModelClass.BookingDTO;
import com.samyotech.laundry.ModelClass.CurrencyDTO;
import com.samyotech.laundry.ModelClass.PopLaundryDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentBookingBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.BookingAdapter;
import com.samyotech.laundry.ui.adapter.PopularLaundriesAdapter;
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
public class BookingFragment extends Fragment {

    String TAG = BookingFragment.class.getSimpleName();
    FragmentBookingBinding binding;
    LinearLayoutManager linearLayoutManager;
    BookingAdapter bookingAdapter;
    ArrayList<BookingDTO> bookingDTOS = new ArrayList<>();
    HashMap<String,String> params=new HashMap<>();
    BookingDTO bookingDTO;
    UserDTO userDTO;
    private SharedPrefrence prefrence;
    CurrencyDTO currencyDTO;
    int i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking, container, false);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO=prefrence.getParentUser(Consts.USER_DTO);
        currencyDTO=(CurrencyDTO)prefrence.getCurrency(Consts.CURRENCYDTO);
        getAllBookings();

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    binding.ctvHead.setVisibility(View.GONE);
                    binding.cetSearch.setVisibility(View.VISIBLE);
                    binding.ivSearch.setImageResource(R.drawable.ic_cancel);

                    i=1;
                }else {

                    binding.ctvHead.setVisibility(View.VISIBLE);
                    binding.cetSearch.setVisibility(View.GONE);

                    binding.ivSearch.setImageResource(R.drawable.ic_search);
                    i=0;
                }
            }
        });

        binding.cetSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    bookingAdapter.filter(s.toString());


                } else {


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return binding.getRoot();
    }

    public void getAllBookings() {
        ProjectUtils.getProgressDialog(getActivity());
        params.put(Consts.USER_ID,userDTO.getUser_id());
        new HttpsRequest(Consts.GETBOOKINGLIST, params,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        bookingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), BookingDTO.class);

//                        bookingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), BookingDTO.class);
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


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvBooking.setLayoutManager(linearLayoutManager);
        bookingAdapter = new BookingAdapter(getActivity(), bookingDTO.getOrder_list(),BookingFragment.this,currencyDTO);
        binding.rvBooking.setAdapter(bookingAdapter);
    }


}
