package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityDashboardBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.CurrencyDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.fragment.BookingFragment;
import com.samyotech.laundry.ui.fragment.HomeFragment;
import com.samyotech.laundry.ui.fragment.NearByFragment;
import com.samyotech.laundry.ui.fragment.OffersFragment;
import com.samyotech.laundry.ui.fragment.ProfileFragment;
import com.samyotech.laundry.utils.ProjectUtils;

public class Dashboard extends AppCompatActivity {
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private final String TAG = Dashboard.class.getCanonicalName();
    public String type = "";
    ActivityDashboardBinding binding;
    Fragment fragment;
    Context mContext;
    HomeFragment homeFragment = new HomeFragment();
    NearByFragment nearByFragment = new NearByFragment();
    BookingFragment bookingFragment = new BookingFragment();
    OffersFragment offersFragment = new OffersFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FragmentManager fragmentManager;
    SharedPrefrence prefrence;
    CurrencyDTO currencyDTO;
    private Location mylocation;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(this);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if (getIntent().hasExtra(Consts.SCREEN_TAG)) {
            type = getIntent().getStringExtra(Consts.SCREEN_TAG);
            navController.setGraph(R.navigation.order_navigation);
        }
    }
}
//            }
//        });*/

//
//                    binding= DataBindingUtil.setContentView(this,R.layout.activity_dashboard);
//                    mContext=Dashboard.this;
//
//                    prefrence=SharedPrefrence.getInstance(mContext);
//
//                    fragmentManager = getSupportFragmentManager();
//                    fragmentManager.beginTransaction().add(R.id.container, homeFragment).commit();
//                    binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_second, null), null, null);
//
//                    if (getIntent().hasExtra(Consts.SCREEN_TAG)) {
//                    type = getIntent().getStringExtra(Consts.SCREEN_TAG);
//                    }
//
//
//
///*
//        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//
//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
//
//                        fragment = new HomeFragment();
//                        loadFragment(fragment);
//
//                        return true;
//                    case R.id.navigation_loc:
//                        fragment = new NearByFragment();
//                        loadFragment(fragment);
//
//                        return true;
//                    case R.id.navigation_bookings:
//                        fragment = new BookingFragment();
//                        loadFragment(fragment);
//
//                        return true;
//
//                        case R.id.navigation_offers:
//                            fragment = new OffersFragment();
//                            loadFragment(fragment);
//
//                            return true;
//                        case R.id.navigation_profile:
//                            fragment = new ProfileFragment();
//                            loadFragment(fragment);
//
//                            return true;
//                }
//                return false;
//
//        if (savedInstanceState == null) {
//            if (type != null) {
//                if (type.equalsIgnoreCase(Consts.BOOKINGFRAGMENT)) {
//                    setSelected(false, false, true, false, false);
//                    fragmentManager.beginTransaction().replace(R.id.container, bookingFragment).commit();
//                }else if (type.equalsIgnoreCase(Consts.CHATNOTIFICATION)) {
//                    Intent in =new Intent(mContext,ChatList.class);
//                    startActivity(in);
//                }else if (type.equalsIgnoreCase(Consts.TICKETNOTIFICATION)) {
//                    Intent in =new Intent(mContext,TicketsActivity.class);
//                    startActivity(in);
//                } else if (type.equalsIgnoreCase(Consts.ORDERNOTIFICATION)) {
//                    setSelected(false, false, true, false, false);
//                    fragmentManager.beginTransaction().replace(R.id.container, bookingFragment).commit();
//                } else {
//                    setSelected(true, false, false, false, false);
//                    fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
//                }
//            } else {
//                setSelected(true, false, false, false, false);
//                fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
//            }
//
//
//        }
//        setUiAction();
//
//
//
//
//    }
//
//    public void setUiAction() {
//        binding.tvHome.setOnClickListener(this);
//        binding.tvbooking.setOnClickListener(this);
//        binding.tvnearby.setOnClickListener(this);
//        binding.tvOffer.setOnClickListener(this);
//        binding.tvProfile.setOnClickListener(this);
//
//        getCurrencyCode();
//        setUpGClient();
//    }
//
//    private void getCurrencyCode() {
//
//        new HttpsRequest(Consts.GETCURRENCY,mContext).stringGet(TAG, new Helper() {
//            @Override
//            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
//                if(flag){
//                    try{
//                        currencyDTO = new Gson().fromJson(response.getJSONObject("data").toString(), CurrencyDTO.class);
//                        prefrence.setSubscription(currencyDTO, Consts.CURRENCYDTO);}catch (Exception e){e.printStackTrace();}
//
//                }else {}
//            }
//        });
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//
//        if(nearByFragment!=null && nearByFragment.isVisible()){
////            binding.bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(1).scaleY(1);
//            binding.bar.setVisibility(View.VISIBLE);
//
//            setSelected(true, false, false, false, false);
//            fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
//        } else if(bookingFragment!=null && bookingFragment.isVisible()){
////            binding.bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(1).scaleY(1);
//            binding.bar.setVisibility(View.VISIBLE);
//
//            setSelected(true, false, false, false, false);
//            fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
//        } else if(offersFragment!=null && offersFragment.isVisible()){
//            binding.bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(1).scaleY(1);
//            binding.bar.setVisibility(View.VISIBLE);
//
//            setSelected(true, false, false, false, false);
//            fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
//        }  else if(profileFragment!=null && profileFragment.isVisible()){
//            binding.bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(1).scaleY(1);
//            binding.bar.setVisibility(View.VISIBLE);
//
//            setSelected(true, false, false, false, false);
//            fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
//        } else {
//            alertDialog();
//        }
//    }
//
//
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commitAllowingStateLoss();
//
//
//    }
//    public void alertDialog() {
//        new AlertDialog.Builder(this)
//                .setIcon(R.mipmap.ic_launcher)
//                .setTitle(getString(R.string.app_name))
//                .setMessage(getResources().getString(R.string.closeMsg))
//                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent i = new Intent();
//                        i.setAction(Intent.ACTION_MAIN);
//                        i.addCategory(Intent.CATEGORY_HOME);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                        finish();
//                    }
//                })
//                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//
//            case R.id.tvHome:
//                setSelected(true, false, false, false, false);
//                fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
//                break;
//            case R.id.tvnearby:
//                setSelected(false, true, false, false, false);
//                fragmentManager.beginTransaction().replace(R.id.container, nearByFragment).commit();
//                break;
//            case R.id.tvbooking:
//                setSelected(false, false, true, false, false);
//                fragmentManager.beginTransaction().replace(R.id.container, bookingFragment).commit();
//                break;
//            case R.id.tvOffer:
//                setSelected(false, false, false, true, false);
//                fragmentManager.beginTransaction().replace(R.id.container, offersFragment).commit();
//                break;
//            case R.id.tvProfile:
//                setSelected(false, false, false, false, true);
//                fragmentManager.beginTransaction().replace(R.id.container, profileFragment).commit();
//                break;
//        }
//    }
//
//
//
//    public void setSelected(boolean firstBTN, boolean secondBTN, boolean thirdBTN, boolean fourthBTN, boolean fifthBTN) {
//        if (firstBTN) {
//
//            binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_second, null), null, null);
//            binding.tvnearby.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc, null), null, null);
//            binding.tvbooking.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_commerce_and_shopping, null), null, null);
//            binding.tvOffer.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_offers, null), null, null);
//            binding.tvProfile.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_profile, null), null, null);
//        }
//        if (secondBTN) {
//
//
//            binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home, null), null, null);
//            binding.tvnearby.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc_second, null), null, null);
//            binding.tvbooking.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_commerce_and_shopping, null), null, null);
//            binding.tvOffer.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_offers, null), null, null);
//            binding.tvProfile.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_profile, null), null, null);
//        }
//        if (thirdBTN) {
//
//
//            binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home, null), null, null);
//            binding.tvnearby.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc, null), null, null);
//            binding.tvbooking.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_commerce_and_shopping_second, null), null, null);
//            binding.tvOffer.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_offers, null), null, null);
//            binding.tvProfile.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_profile, null), null, null);
//        }
//
//        if (fourthBTN) {
//
//
//            binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home, null), null, null);
//            binding.tvnearby.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc, null), null, null);
//            binding.tvbooking.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_commerce_and_shopping, null), null, null);
//            binding.tvOffer.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_offers_second, null), null, null);
//            binding.tvProfile.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_profile, null), null, null);
//        }
//        if (fifthBTN) {
//
//
//            binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home, null), null, null);
//            binding.tvnearby.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc, null), null, null);
//            binding.tvbooking.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_commerce_and_shopping, null), null, null);
//            binding.tvOffer.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_offers, null), null, null);
//            binding.tvProfile.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_profile_second, null), null, null);
//        }
//
//        binding.tvHome.setSelected(firstBTN);
//        binding.tvnearby.setSelected(secondBTN);
//        binding.tvbooking.setSelected(thirdBTN);
//        binding.tvOffer.setSelected(fourthBTN);
//        binding.tvProfile.setSelected(fifthBTN);
//
//    }
//
//
//
//
//public void openFragment(){
//
//    setSelected(false, false, true, false, false);
//    fragmentManager.beginTransaction().replace(R.id.container, bookingFragment).commit();
//}
//
//
//
//    private void getMyLocation() {
//        if (googleApiClient != null) {
//            if (googleApiClient.isConnected()) {
//                int permissionLocation = ContextCompat.checkSelfPermission(Dashboard.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                    LocationRequest locationRequest = new LocationRequest();
//                    locationRequest.setInterval(3000);
//                    locationRequest.setFastestInterval(3000);
//                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                            .addLocationRequest(locationRequest);
//                    builder.setAlwaysShow(true);
//                    LocationServices.FusedLocationApi
//                            .requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
//                    PendingResult result =
//                            LocationServices.SettingsApi
//                                    .checkLocationSettings(googleApiClient, builder.build());
//                    result.setResultCallback(new ResultCallback() {
//
//                        @Override
//                        public void onResult(@NonNull Result result) {
//                            final Status status = result.getStatus();
//                            switch (status.getStatusCode()) {
//                                case LocationSettingsStatusCodes.SUCCESS:
//                                    // All location settings are satisfied.
//                                    // You can initialize location requests here.
//                                    int permissionLocation = ContextCompat
//                                            .checkSelfPermission(Dashboard.this,
//                                                    Manifest.permission.ACCESS_FINE_LOCATION);
//                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//                                        mylocation = LocationServices.FusedLocationApi
//                                                .getLastLocation(googleApiClient);
//
//                                    }
//                                    break;
//                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                    // Location settings are not satisfied.
//                                    // But could be fixed by showing the fabcustomer a dialog.
//                                    try {
//                                        // Show the dialog by calling startResolutionForResult(),
//                                        // and check the result in onActivityResult().
//                                        // Ask to turn on GPS automatically
//                                        status.startResolutionForResult(Dashboard.this,
//                                                REQUEST_CHECK_SETTINGS_GPS);
//                                    } catch (IntentSender.SendIntentException e) {
//                                        // Ignore the error.
//                                    }
//                                    break;
//                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                                    // Location settings are not satisfied. However, we have no way to fix the
//                                    // settings so we won't show the dialog.
//                                    //finish();
//                                    break;
//                            }
//                        }
//
//
//                    });
//                }
//            }
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CHECK_SETTINGS_GPS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        getMyLocation();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        break;
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        checkPermissions();
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mylocation = location;
//        if (mylocation != null) {
//            Double latitude = mylocation.getLatitude();
//            Double longitude = mylocation.getLongitude();
//            prefrence.setValue(Consts.LATITUDE, latitude + "");
//            prefrence.setValue(Consts.LONGITUDE, longitude + "");
//
//        }
//    }
//
//
//    private void checkPermissions() {
//        int permissionLocation = ContextCompat.checkSelfPermission(Dashboard.this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            if (!listPermissionsNeeded.isEmpty()) {
//                ActivityCompat.requestPermissions(Dashboard.this,
//                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
//            }
//        } else {
//            getMyLocation();
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        int permissionLocation = ContextCompat.checkSelfPermission(Dashboard.this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//            getMyLocation();
//        }
//    }
//
//    private synchronized void setUpGClient() {
//        googleApiClient = new GoogleApiClient.Builder(Dashboard.this)
//                .enableAutoManage(Dashboard.this, 0, this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        googleApiClient.connect();
//    }
//
//
//
//}