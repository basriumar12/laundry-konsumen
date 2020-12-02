package com.samyotech.laundry.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.FragmentNearByBinding;
import com.samyotech.laundry.https.HttpsRequest;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.model.PopLaundryDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.network.NetworkManager;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Dashboard;
import com.samyotech.laundry.ui.activity.SearchActivity;
import com.samyotech.laundry.ui.adapter.PopularLaundriesAdapter;
import com.samyotech.laundry.utils.AnchorSheetBehavior;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class NearByFragment extends Fragment {
    private final String TAG = NearByFragment.class.getSimpleName();
    private final ArrayList<MarkerOptions> optionsList = new ArrayList<>();
    private final HashMap<String, String> parmsCategory = new HashMap<>();
    HashMap<String, String> parms = new HashMap<>();
    FragmentNearByBinding binding;
    LinearLayoutManager linearLayoutManager;
    PopularLaundriesAdapter popularLaundriesAdapter;
    private NearByFragment nearByFragment;
    private GoogleMap googleMap;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private ArrayList<PopLaundryDTO> laundryList;
    private Hashtable<String, PopLaundryDTO> markers;
    private Marker marker;
    private Dashboard dashboard;
    private AnchorSheetBehavior<LinearLayout> bsBehavior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_near_by, container, false);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        binding.mapView.onCreate(savedInstanceState);
        markers = new Hashtable<String, PopLaundryDTO>();

        getNearByLaundry();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tapActionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bsBehavior.getState() == AnchorSheetBehavior.STATE_COLLAPSED) {
                    bsBehavior.setState(AnchorSheetBehavior.STATE_ANCHOR);
                }
            }
        });

        binding.searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), SearchActivity.class));
            }
        });

        bsBehavior = AnchorSheetBehavior.from(binding.bottomsheetMap);
        bsBehavior.setState(AnchorSheetBehavior.STATE_COLLAPSED);

//anchor offset. any value between 0 and 1 depending upon the position u want
        bsBehavior.setAnchorOffset(0.5f);
        bsBehavior.setAnchorSheetCallback(new AnchorSheetBehavior.AnchorSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == AnchorSheetBehavior.STATE_COLLAPSED) {
                    //action if needed
                }

                if (newState == AnchorSheetBehavior.STATE_EXPANDED) {

                }

                if (newState == AnchorSheetBehavior.STATE_DRAGGING) {

                }

                if (newState == AnchorSheetBehavior.STATE_ANCHOR) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                float h = bottomSheet.getHeight();
                float off = h * slideOffset;

                switch (bsBehavior.getState()) {
                    case AnchorSheetBehavior.STATE_DRAGGING:
                        setMapPaddingBotttom(off);
                        //reposition marker at the center
//                        if (mLoc != null)
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(mLoc));
                        break;
                    case AnchorSheetBehavior.STATE_SETTLING:
                        setMapPaddingBotttom(off);
                        //reposition marker at the center
//                        if (mLoc != null) googleMap.moveCamera(CameraUpdateFactory.newLatLng(mLoc));
                        break;
                    case AnchorSheetBehavior.STATE_HIDDEN:
                        break;
                    case AnchorSheetBehavior.STATE_EXPANDED:
                        break;
                    case AnchorSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }
        });
    }

    private void setMapPaddingBotttom(Float offset) {
        //From 0.0 (min) - 1.0 (max) // bsExpanded - bsCollapsed;
        Float maxMapPaddingBottom = 1.0f;
        binding.mapView.setPadding(0, 0, 0, Math.round(offset * maxMapPaddingBottom));

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
        parms.put(Consts.Count, "20");
        parms.put(Consts.LATITUDE, prefrence.getValue(Consts.LATITUDE));
        parms.put(Consts.LONGITUDE, prefrence.getValue(Consts.LONGITUDE));

//        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GETALLLAUNDRYSHOP, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
//                    ProjectUtils.pauseProgressDialog();

                    try {

                        Log.e(TAG, "backResponse: " + response);

                        laundryList = new ArrayList<>();
                        Type popLaundryDTO = new TypeToken<List<PopLaundryDTO>>() {
                        }.getType();
                        laundryList = new Gson().fromJson(response.getJSONArray("data").toString(), popLaundryDTO);

                        for (int i = 0; i < laundryList.size(); i++) {

                            PopLaundryDTO l = laundryList.get(i);
                            optionsList.add(new MarkerOptions().position(new LatLng(Double.parseDouble(l.getLatitude()), Double.parseDouble(l.getLongitude())))
                                    .title(l.getShop_name())
                                    .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(requireContext()))));

                        }

                        //    binding.mapView.onResume(); // needed to get the map to display immediately

                        try {
                            MapsInitializer.initialize(requireContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        binding.mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap mMap) {
                                googleMap = mMap;

                                // For showing a move to my location button
                                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the fabcustomer grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }

                                // For dropping a marker at a point on the Map
                                LatLng currentPos = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));
                                //  googleMap.addMarker(new MarkerOptions().position(currentPos).title(userDTO.getName()).title("My Location").snippet(userDTO.getUser_id()));

                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(14).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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

//                                    options.position(options.getPosition());
//                                    options.title(options.getTitle());
//                                    options.snippet(options.getSnippet());
//                                    options.draggable(false);
                                    final Marker marker = googleMap.addMarker(options);

//                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(options.getPosition()).zoom(12).build();
//                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                    for (int i = 0; i < laundryList.size(); i++) {
                                        if (!laundryList.get(i).getLatitude().equalsIgnoreCase(prefrence.getValue(Consts.LATITUDE)) &&
                                                !laundryList.get(i).getLongitude().equalsIgnoreCase(prefrence.getValue(Consts.LONGITUDE))) {
                                            if (laundryList.get(i).getUser_id().equalsIgnoreCase(options.getSnippet()))

                                                markers.put(marker.getId(), laundryList.get(i));
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
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvLaundrytab.setLayoutManager(linearLayoutManager);
        popularLaundriesAdapter = new PopularLaundriesAdapter(getActivity(), laundryList);
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
