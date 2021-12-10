package com.example.f1hub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LocationSelectionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Location Collection";
    private ActivityResultLauncher<String[]> mLocationPermissionRequest;
    private Boolean mCoarseLocationGranted = null;
    private Boolean mFineLocationGranted = null;
    private FusedLocationProviderClient mFusedLocationClient;

    // member variables for setting up screen
    public static final String ARG_LOCATION1 = "dataLat";
    public static final String ARG_LOCATION2 = "dataLong";

    private String mDataLat;
    private String mDataLong;

    public LocationSelectionFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationSelectionFragment newInstance(String dataLat, String dataLong) {
        LocationSelectionFragment fragment = new LocationSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION1, dataLat);
        args.putString(ARG_LOCATION2, dataLong);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataLat = getArguments().getString(ARG_LOCATION1);
            mDataLong = getArguments().getString(ARG_LOCATION2);

        }
        registerForLocationPermissionCheck();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationPermissionRequest.unregister();
    }

    private void registerForLocationPermissionCheck() {
        mLocationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                        .RequestMultiplePermissions(), result -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        mFineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        mCoarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    } else {
                        mFineLocationGranted = ContextCompat.checkSelfPermission(
                                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED;
                        // same thing for less accurate location
                        mCoarseLocationGranted = ContextCompat.checkSelfPermission(
                                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED;

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_selection, container, false);

        Button btnDriversInfo = view.findViewById(R.id.btnDriversInfo);
        btnDriversInfo.setOnClickListener(this);


        Button btnShowNextCircut = view.findViewById(R.id.btnShowNextCircut);
        btnShowNextCircut.setOnClickListener(this);


        return view;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnDriversInfo) {
            Navigation.findNavController(v).navigate(R.id.action_locationSelectionFragment_to_driversInfoFragment);
        } else if (v.getId() == R.id.btnShowNextCircut) {
            checkIfLocationPermissionGranted();
            getTheCurrentLocation();
        }
    }


    private void checkIfLocationPermissionGranted() {
        if (mFineLocationGranted != null && mFineLocationGranted) {
            // Precise location access granted.
            Log.d(TAG, "ACCURATE location granted");
        } else if (mCoarseLocationGranted != null && mCoarseLocationGranted) {
            // Only approximate location access granted.
            Log.d(TAG, "inaccurate location granted");
        } else {
            requestLocationPermissions();
            Log.d(TAG, "re-requesting the permission");
        }
    }

    private void requestLocationPermissions(){
        mLocationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

    }


    private void getTheCurrentLocation() {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermissions();
                return;
            }

        GoogleApiAvailability gaa = new GoogleApiAvailability();
        if (ConnectionResult.SUCCESS == gaa.isGooglePlayServicesAvailable(getContext())) {
            // use google play services
            Executor executor = Executors.newSingleThreadExecutor();
            mFusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY,null)
                    .addOnSuccessListener(executor, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Log.d(TAG, "Google play location service used" + location.getLatitude() + "," + location.getLongitude());
                                switchToRaceMap(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
                                //switchToRaceMap(location.getLatitude(), location.getLongitude());
                            }
                            else{

                                Log.d(TAG,"Play location didnt return a location");
                            }
                        }
                    });
        }
        else{
            // do it using location manager
            Log.d(TAG, "No location services on the device");
            LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(getContext().LOCATION_SERVICE);
            // check if the GPS Provider is available
            boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (gpsEnabled){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null,
                            Executors.newSingleThreadExecutor(),
                            new Consumer<Location>() {
                                @Override
                                public void accept(Location location) {
                                    if (location != null) {
                                        // Logic to handle location object
                                        Log.d(TAG, "LocationManager Location " + location.getLatitude() + ", " + location.getLongitude());
                                        switchToRaceMap(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                                    } else {
                                        Log.d(TAG, "LocationManager did not return a GPS location ");
                                    }
                                }
                            });
                }
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                // similar code to above, but using the network provider
            }
        }
    }
    // function to switch to the map and take the user location in args
    // i will probably change this to make it just launch an intent of google maps
    private void switchToRaceMap (String currentLat, String currentLong){
        Handler handle = new Handler(Looper.getMainLooper());
        handle.post(new Runnable() {
            @Override
            public void run() {
                Bundle args = new Bundle();
                args.putString("dataLat", currentLat);
                args.putString("dataLong", currentLong);
                Navigation.findNavController(getView()).navigate(R.id.action_locationSelectionFragment_to_raceLocationFragment, args);
            }
        });

    }

}