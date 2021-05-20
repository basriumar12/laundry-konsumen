package com.samyotech.laundry.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.HomeFragmentBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.GetBannerDTO;
import com.samyotech.laundry.model.HomeDTO;
import com.samyotech.laundry.model.NearBYDTO;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.ServicesDTO;
import com.samyotech.laundry.model.SpecialOfferPkgDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Dashboard;
import com.samyotech.laundry.ui.activity.NotificationActivity;
import com.samyotech.laundry.ui.activity.SearchActivity;
import com.samyotech.laundry.ui.activity.TopServices;
import com.samyotech.laundry.ui.adapter.ImageAdapter;
import com.samyotech.laundry.ui.adapter.LaundriesNearAdapter;
import com.samyotech.laundry.ui.adapter.PopularLaundriesAdapter;
import com.samyotech.laundry.ui.adapter.SpecialOffersAdapter;
import com.samyotech.laundry.ui.adapter.TopServiceAdapter;
import com.samyotech.laundry.utils.ProjectUtils;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment implements View.OnClickListener {
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    HomeFragmentBinding binding;
    View view;
    ArrayList<GetBannerDTO> imageDTOArrayList;
    int currentPage = 0;
    Timer timer;
    Dashboard dashBoard;
    String TAG = HomeFragment.class.getSimpleName();
    TopServiceAdapter topServiceAdapter;
    ArrayList<ServicesDTO> servicesDTOArrayList;
    GridLayoutManager layoutManagerServ;
    PopularLaundriesAdapter popularLaundriesAdapter;
    ArrayList<PopLaundryDTO> popLaundryDTOArrayList;
    RecyclerView.LayoutManager layoutManagerPop;
    SpecialOffersAdapter specialOffersAdapter;
    ArrayList<SpecialOfferPkgDTO> specialOffersAdapterArrayList;
    RecyclerView.LayoutManager layoutManagerOffer;
    LaundriesNearAdapter laundriesNearAdapter;
    ArrayList<NearBYDTO> nearBYDTOArrayList;
    RecyclerView.LayoutManager layoutManagerNear;
    HashMap<String, String> params = new HashMap<>();
    HomeDTO homeDTO;
    UserDTO userDTO;
    private ImageAdapter imageAdapter;
    private SharedPrefrence prefrence;
    private FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        view = binding.getRoot();
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {

                                prefrence.setValue(Consts.LATITUDE, location.getLatitude() + "");
                                prefrence.setValue(Consts.LONGITUDE, location.getLongitude() + "");
                                getAddress(location.getLatitude(), location.getLongitude());
                                getData();
                            }else {
                                dialogLokasi();
                                Toast.makeText(dashBoard, "Aktifkan Lokasi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception ex) {
            dialogLokasi();
            Toast.makeText(dashBoard, "Aktifkan Lokasi", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    void dialogLokasi (){
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = requireView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_logout, viewGroup, false);
        TextView tv = dialogView.findViewById(R.id.text);
        tv.setText("Data lokasi tidak di dapatkan. \n Aktikan lokasi gps di handphone anda dan coba tutup aplikasi kemudian buka kembali dan akses menu ini.");
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        FancyButton cancel = dialogView.findViewById(R.id.cancel);

        cancel.setVisibility(View.GONE);

        FancyButton ok = dialogView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.alamat.setText(userDTO.getAddress());
        binding.gantiAlamatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace();
            }
        });

    }

    public void getData() {
        params.put(Consts.LATITUDE, prefrence.getValue(Consts.LATITUDE));
        params.put(Consts.LONGITUDE, prefrence.getValue(Consts.LONGITUDE));
        params.put(Consts.USER_ID, userDTO.getUser_id());
        ProjectUtils.showProgressDialog(requireActivity(), true, getResources().getString(R.string.please_wait));

        new HttpsRequest(Consts.GETHOMEDATA, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {

                ProjectUtils.cancelDialog();
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {

                        homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);
                        Log.e("TAG","data home "+homeDTO);
                        setupViewPager();

                    } catch (Exception e) {
                        e.getMessage();
                    }


                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });

    }


    private void setupViewPager() {

        imageAdapter = new ImageAdapter(homeDTO.getAdvertis(), getActivity());
        binding.viewpager.setAdapter(imageAdapter);
        binding.tabDots.setViewPager(binding.viewpager);

        layoutManagerServ = new GridLayoutManager(getActivity(), 3);
        binding.layananKamiRecyclerview.setLayoutManager(layoutManagerServ);
        topServiceAdapter = new TopServiceAdapter(getActivity(), homeDTO.getService());
        binding.layananKamiRecyclerview.setAdapter(topServiceAdapter);

        layoutManagerNear = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.laundryTerdekatRecyclerview.setLayoutManager(layoutManagerNear);
        laundriesNearAdapter = new LaundriesNearAdapter(getActivity(), homeDTO.getNear_by());
        binding.laundryTerdekatRecyclerview.setAdapter(laundriesNearAdapter);

        binding.lihatSemuaLaundryTerdekat.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.ivNotification.setOnClickListener(this);
    }

    private void findPlace() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                .withLocation(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)),
                        Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)))
                .build(requireActivity());

        startActivityForResult(locationPickerIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                try {
                    getAddress(data.getDoubleExtra(Consts.LATITUDE, 0.0), data.getDoubleExtra(Consts.LONGITUDE, 0.0));
                    updateProfile();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getAddress(double lat, double lng) {

        try {
            Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
            binding.alamat.setText(obj.getAddressLine(0));

            params.put(Consts.ADDRESS, obj.getAddressLine(0));
            params.put(Consts.LATITUDE, String.valueOf(obj.getLatitude()));
            params.put(Consts.LONGITUDE, String.valueOf(obj.getLongitude()));

            prefrence.setValue(Consts.LATITUDE, String.valueOf(obj.getLatitude()));
            prefrence.setValue(Consts.LONGITUDE, String.valueOf(obj.getLongitude()));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateProfile() {
        ProjectUtils.showProgressDialog(requireActivity(), true, getResources().getString(R.string.please_wait)).show();

        new HttpsRequest(Consts.USERUPDATE, params, requireActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
               ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        ProjectUtils.showProgressDialog(requireActivity(), true, getResources().getString(R.string.please_wait)).dismiss();

                        Log.e(TAG, "backResponse: " + response.toString());
                     //   ProjectUtils.showToast(requireContext(), msg);
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                        Intent in = new Intent(requireContext(), Dashboard.class);
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    ProjectUtils.showToast(mContext, msg);
                }

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        //  stopLocationUpdates();
        super.onPause();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dashBoard = (Dashboard) context;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lihat_semua_laundry_terdekat:
                Intent in2 = new Intent(getActivity(), TopServices.class);
                startActivity(in2);
                break;
//            case R.id.lihat_semua_layanan_kami:
//                Intent in = new Intent(getActivity(), AllServices.class);
//                startActivity(in);
//                break;
            case R.id.ivSearch:
                Intent in3 = new Intent(getActivity(), SearchActivity.class);
                startActivity(in3);
                break;
            case R.id.ivNotification:
                Intent in4 = new Intent(getActivity(), NotificationActivity.class);
                startActivity(in4);
                break;
        }
    }


}
