package com.samyotech.laundry.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.laundry.ModelClass.PopLaundryDTO;
import com.samyotech.laundry.ModelClass.UserDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentNearByBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Dashboard;
import com.samyotech.laundry.ui.adapter.PopularLaundriesAdapter;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class NearByFragment extends Fragment {
    private final String TAG = NearByFragment.class.getSimpleName();
    private NearByFragment nearByFragment;
    private GoogleMap googleMap;
    private final ArrayList<MarkerOptions> optionsList = new ArrayList<>();
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    HashMap<String, String> parms = new HashMap<>();
    private ArrayList<PopLaundryDTO> allAtristListDTOList;
    private Hashtable<String, PopLaundryDTO> markers;
    private Marker marker;
    private Dashboard dashboard;
    private final HashMap<String, String> parmsCategory = new HashMap<>();
    FragmentNearByBinding binding;

    LinearLayoutManager linearLayoutManager;
    PopularLaundriesAdapter popularLaundriesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil .inflate(inflater,R.layout.fragment_near_by, container, false);





        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);




        binding.mapView.onCreate(savedInstanceState);
        markers = new Hashtable<String, PopLaundryDTO>();
        binding.mapView.getMapAsync(new OnMapReadyCallback() {
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
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));
                //  googleMap.addMarker(new MarkerOptions().position(sydney).title(userDTO.getName()).title("My Location").snippet(userDTO.getUser_id()));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });



        getNearByLaundry();



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {


            getNearByLaundry();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }


    public void getNearByLaundry() {
        parms.put(Consts.Count,"20");
        parms.put(Consts.LATITUDE,prefrence.getValue(Consts.LATITUDE));
        parms.put(Consts.LONGITUDE,prefrence.getValue(Consts.LONGITUDE));


//        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GETALLLAUNDRYSHOP, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
//                    ProjectUtils.pauseProgressDialog();

                    try {

                        Log.e(TAG, "backResponse: "+response );

                        allAtristListDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<PopLaundryDTO>>() {
                        }.getType();
                        allAtristListDTOList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0; i < allAtristListDTOList.size(); i++) {

                                optionsList.add(new MarkerOptions().position(new LatLng(Double.parseDouble(allAtristListDTOList.get(i).getLatitude()), Double.parseDouble(allAtristListDTOList.get(i).getLongitude()))).title(allAtristListDTOList.get(i).getShop_name()).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(getActivity()))));

                        }

                        //    binding.mapView.onResume(); // needed to get the map to display immediately

                        try {
                            MapsInitializer.initialize(getActivity().getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        binding.mapView.getMapAsync(new OnMapReadyCallback() {
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
                                googleMap.setMyLocationEnabled(true);


                                // For dropping a marker at a point on the Map

                            /*    for (LatLng point : latlngs) {
                                    options.position(point);
                                    options.title("SAMYOTECH");
                                    options.snippet("SAMYOTECH");
                                    googleMap.addMarker(options);
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                }*/


                                for (MarkerOptions options : optionsList) {

                                    options.position(options.getPosition());
                                    options.title(options.getTitle());
                                    options.snippet(options.getSnippet());
                                    options.draggable(false);
                                    final Marker hamburg = googleMap.addMarker(options);


//                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(options.getPosition()).zoom(12).build();
//                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                    for (int i = 0; i < allAtristListDTOList.size(); i++) {
                                        if (!allAtristListDTOList.get(i).getLatitude().equalsIgnoreCase(prefrence.getValue(Consts.LATITUDE)) && !allAtristListDTOList.get(i).getLongitude().equalsIgnoreCase(prefrence.getValue(Consts.LATITUDE))) {
                                            if (allAtristListDTOList.get(i).getUser_id().equalsIgnoreCase(options.getSnippet()))

                                                markers.put(hamburg.getId(), allAtristListDTOList.get(i));
                                            // CameraPosition cameraPosition = new CameraPosition.Builder().target(options.getPosition()).zoom(12).build();
                                            // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                    }
                                }

                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(final Marker marker) {

                                        marker.showInfoWindow();

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                marker.showInfoWindow();

                                            }
                                        }, 200);

                                        return false;
                                    }
                                });


                    setData();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.pauseProgressDialog();

                    googleMap.clear();
                }
            }
        });
    }

    private void setData() {



        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        binding.rvLaundrytab.setLayoutManager(linearLayoutManager);
        popularLaundriesAdapter=new PopularLaundriesAdapter(getActivity(),allAtristListDTOList);
        binding.rvLaundrytab.setAdapter(popularLaundriesAdapter);


    }


    public Bitmap createCustomMarker(final Context context) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_maker_layout, null);
        ConstraintLayout constraintLayout = marker.findViewById(R.id.llCustom);




        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);


        return bitmap;
    }



}
