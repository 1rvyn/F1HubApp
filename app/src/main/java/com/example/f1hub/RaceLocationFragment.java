package com.example.f1hub;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;


import androidx.constraintlayout.core.motion.utils.Utils;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.f1hub.data.DriverInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RaceLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RaceLocationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_LOCATION1 = "dataLat";
    private static final String ARG_LOCATION2 = "dataLong";
    private String TAG = "checking the args location";
    private String TAG3 = "the value of long";


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
     * @param dataLat  The latitude of the current user
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
        downloadNextRaceLocation();
    }

    public void downloadNextRaceLocation() {
        //http://ergast.com/api/f1/2021.json

        //uri build

        Uri uri = Uri.parse("https://ergast.com/api/f1/current/next.json");
        // final uri
        // use Volley to make the request
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri.toString(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject MRData = rootObject.getJSONObject("MRData");
                            JSONObject RaceTable = MRData.getJSONObject("RaceTable");

                            JSONArray Races = RaceTable.getJSONArray("Races");
                            for (int i = 0, j = Races.length(); i < j; i++) {
                                JSONObject RacesObj = Races.getJSONObject(i);
                                JSONObject Circuit = RacesObj.getJSONObject("Circuit");

                                JSONObject raceLocation = Circuit.getJSONObject("Location");
                                String longg = raceLocation.getString("long");
                                String lat = raceLocation.getString("lat");

                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.Race_Next_Location_Error), Toast.LENGTH_LONG);
                Log.e(TAG, error.getLocalizedMessage());
            }
        });
        // now make the request
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_race_location, container, false);

        Button btnGetGPSLocation = view.findViewById(R.id.btnGetGPSLocation);
        btnGetGPSLocation.setOnClickListener(this);

        Button btnGetRaceLocation = view.findViewById(R.id.btnGetRaceLocation);
        btnGetRaceLocation.setOnClickListener(this);

        Button btnCreateTrip = view.findViewById(R.id.btnCreateTrip);
        btnCreateTrip.setOnClickListener(this);


        return view;
    }

    // had issues so this is rushed very sorry horrible practice (i know)
    // allows each of them to do their required functions all be it hideously
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGetGPSLocation) {
            // launch the map app to show this location.

            Uri navigationIntentUri = Uri.parse("geo:0,0?q=" + mDataLat + "," + mDataLong);//creating intent with latlng
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

        if (v.getId() == R.id.btnGetRaceLocation) {
            // do this
            //http://ergast.com/api/f1/2021.json

            //uri build

            Uri uri = Uri.parse("https://ergast.com/api/f1/current/next.json");
            // final uri
            // use Volley to make the request
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri.toString(),
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject rootObject = new JSONObject(response);
                                JSONObject MRData = rootObject.getJSONObject("MRData");
                                JSONObject RaceTable = MRData.getJSONObject("RaceTable");

                                JSONArray Races = RaceTable.getJSONArray("Races");
                                for (int i = 0, j = Races.length(); i < j; i++) {
                                    JSONObject RacesObj = Races.getJSONObject(i);
                                    JSONObject Circuit = RacesObj.getJSONObject("Circuit");

                                    JSONObject raceLocation = Circuit.getJSONObject("Location");
                                    String longg = raceLocation.getString("long");
                                    String lat = raceLocation.getString("lat");


                                    Uri navigationIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + longg);//creating intent with latlng
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                }

                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.Race_Next_Location_Error), Toast.LENGTH_LONG);
                    Log.e(TAG, error.getLocalizedMessage());
                }
            });
            // now make the request
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);
        }
        if (v.getId() == R.id.btnCreateTrip) {

            Uri uri = Uri.parse("https://ergast.com/api/f1/current/next.json");
            // use Volley to make the request
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri.toString(),
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject rootObject = new JSONObject(response);
                                JSONObject MRData = rootObject.getJSONObject("MRData");
                                JSONObject RaceTable = MRData.getJSONObject("RaceTable");

                                JSONArray Races = RaceTable.getJSONArray("Races");
                                for (int i = 0, j = Races.length(); i < j; i++) {
                                    JSONObject RacesObj = Races.getJSONObject(i);
                                    JSONObject Circuit = RacesObj.getJSONObject("Circuit");

                                    JSONObject raceLocation = Circuit.getJSONObject("Location");
                                    String longg = raceLocation.getString("long");
                                    String lat = raceLocation.getString("lat");


                                    String link = "https://www.google.com/maps/dir/?api=1&origin=" + mDataLat + "," + mDataLong +
                                            "&destination="+ lat +","+ longg;



                                    String url = link;
                                    Intent ii = new Intent(Intent.ACTION_VIEW);
                                    ii.setData(Uri.parse(url));
                                    startActivity(ii);
                                }

                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.Race_Next_Location_Error), Toast.LENGTH_LONG);
                    Log.e(TAG, error.getLocalizedMessage());
                }
            });
            // now make the request
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);

        }
    }
}
