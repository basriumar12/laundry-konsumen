package com.samyotech.laundry.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentAboutBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Schedule_Activity;
import com.samyotech.laundry.ui.activity.ShopAcitivity;

import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    FragmentAboutBinding binding;
    PopLaundryDTO popLaundryDTO;
    MarkerOptions markerOptions;
    LatLng sydney;
    ShopAcitivity serviceAcitivity;
    boolean checkClick = true;
    private SharedPrefrence prefrence;
    private GoogleMap googleMap;
    private UserDTO userDTO;
    private Bundle bundle;
    private Hashtable<String, PopLaundryDTO> markers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        bundle = this.getArguments();
        popLaundryDTO = (PopLaundryDTO) bundle.getSerializable(Consts.SHOPDTO);

        setUIAction();

        binding.mvAddress.onCreate(savedInstanceState);
        binding.mvAddress.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
//                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                if (popLaundryDTO.getLongitude().equalsIgnoreCase("")) {

                    sydney = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));

                } else {

                    sydney = new LatLng(Double.parseDouble(popLaundryDTO.getLatitude()), Double.parseDouble(popLaundryDTO.getLongitude()));

                }
                googleMap.addMarker(new MarkerOptions().position(sydney).title(userDTO.getName()).title(popLaundryDTO.getShop_name()).snippet(userDTO.getUser_id()));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });
        return binding.getRoot();
    }

    private void setUIAction() {
        binding.namaToko.setText(popLaundryDTO.getShop_name());
        binding.ctvAboutUS.setText(popLaundryDTO.getDescription());
        binding.hariBuka.setText("Buka : " + popLaundryDTO.getMulai_hari() + " - " + popLaundryDTO.getSampai_hari());
        binding.jamBuka.setText("Jam : " + popLaundryDTO.getOpening_time() + " - " + popLaundryDTO.getClosing_time());
        binding.alamat.setText(popLaundryDTO.getAddress());
        binding.rating.setText(popLaundryDTO.getRating());
        binding.arb.setRating(Float.parseFloat(popLaundryDTO.getRating()));
        binding.bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TESTONG", popLaundryDTO.toString());
                Intent in = new Intent(requireContext(), Schedule_Activity.class);
                in.putExtra(Consts.SHOPDTO, popLaundryDTO);
                startActivity(in);
            }
        });
        binding.mvAddress.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the fabcustomer grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
//                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map

                            /*    for (LatLng point : latlngs) {
                                    options.position(point);
                                    options.title("SAMYOTECH");
                                    options.snippet("SAMYOTECH");
                                    googleMap.addMarker(options);
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                }*/

                if (popLaundryDTO.getLongitude().equalsIgnoreCase("")) {

                    sydney = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));

                } else {

                    sydney = new LatLng(Double.parseDouble(popLaundryDTO.getLatitude()), Double.parseDouble(popLaundryDTO.getLongitude()));

                    googleMap.addMarker(new MarkerOptions().position(sydney).title(userDTO.getName()).title(popLaundryDTO.getShop_name()).snippet(userDTO.getUser_id()));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }

                binding.mvAddress.onResume();

                // CameraPosition cameraPosition = new CameraPosition.Builder().target(options.getPosition()).zoom(12).build();
                // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {

                        marker.showInfoWindow();


                  /*      final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                marker.showInfoWindow();

                            }
                        }, 200);
*/
                        return false;
                    }
                });


            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        checkClick = true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        serviceAcitivity = (ShopAcitivity) context;
    }


    @Override
    public void onPause() {
        super.onPause();
        binding.mvAddress.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mvAddress.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mvAddress.onLowMemory();
    }

}
