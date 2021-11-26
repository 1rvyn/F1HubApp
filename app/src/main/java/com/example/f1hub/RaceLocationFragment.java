package com.example.f1hub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RaceLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RaceLocationFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_LOCATION1 = "dataLat";
    private static final String ARG_LOCATION2 = "dataLong";
    private String TAG = "checking the args location";

    // member vars
    private String mDataLat;
    private String mDataLong;

    public RaceLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dataLong The longitude of the current user
     * @param dataLat The latitude of the current user
     * @return A new instance of fragment RaceLocationFragment.
     */
    public static RaceLocationFragment newInstance(String dataLat, String dataLong) {
        RaceLocationFragment fragment = new RaceLocationFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_race_location, container, false);

        Button btnGetGPSLocation = view.findViewById(R.id.btnGetGPSLocation);
        btnGetGPSLocation.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGetGPSLocation){

            // do what is needed to get the next race location and then create a trip through a map
            // even if its through an intent or through a mapview
        }
    }
}