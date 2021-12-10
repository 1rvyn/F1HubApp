package com.example.f1hub;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.f1hub.data.DriverDataParser;
import com.example.f1hub.data.DriverInfo;
import com.example.f1hub.data.DriverInfoRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Driver;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters
    // TODO: Rename and change types of parameters
    private int mDataYear;

    private DriverInformationRecyclerViewAdapter adapter;

    private DriverInfoRepo driverInfoRepo;


    private List<DriverInfo> driverInfo;


    private static final String TAG = "TryingToCodeInAcar";

    public DataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dataYear THE YEAR THE DATA HAS TO BE SHOWN FOR
     * @return A new instance of fragment DataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataFragment newInstance(int dataYear) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putInt(DriversInfoFragment.ARG_DATA_YEAR, dataYear);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataYear = getArguments().getInt(DriversInfoFragment.ARG_DATA_YEAR);
        }
        driverInfoRepo = DriverInfoRepo.getRepository(getContext());
        // get the data to display - a placeholder while waiting to download
        driverInfo = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // update the year from the user input (through arg im lazy)
        TextView tvYearValue = view.findViewById(R.id.tvYearValue);
        String label = getContext().getString(R.string.tvYearDisplay);
        tvYearValue.setText(label);


        // create the adapter for the RecyclerView
        adapter = new DriverInformationRecyclerViewAdapter(getContext(), driverInfo);

        // get the RecyclerView
        RecyclerView rvDriverInformation = view.findViewById(R.id.rv_DriverInformation);

        // setup the RecyclerView with the adapter
        rvDriverInformation.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDriverInformation.setAdapter(adapter);
        Log.d(TAG, "the view is created");

        //List<DriverInfo> driverInfo = DriverInfoRepo.getRepository(getContext()).getDriverInformation(1000);
        RecyclerView recyclerView = view.findViewById(R.id.rv_DriverInformation);
        // get from repo
        RecyclerView.Adapter adapter = new DriverInformationRecyclerViewAdapter(getContext(), driverInfo);
        // set the adapter on the rv
        recyclerView.setAdapter(adapter);


        // get it from the DB

        DriverInfoRepo.getDriverInfoFromDB(mDataYear).observe(getViewLifecycleOwner(), new Observer<List<DriverInfo>>() {
            @Override
            public void onChanged(List<DriverInfo> newDriverInfo) {
                if (newDriverInfo.size() > 0) {
                    driverInfo.clear();
                    driverInfo.addAll(newDriverInfo);
                    adapter.notifyDataSetChanged();
                } else {
                    // download it
                    downloadDriverData();
                }
            }

        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnHomePage) {
            Navigation.findNavController(v).navigate(R.id.action_dataFragment_to_locationSelectionFragment);
        }
    }


    // make the HTTP get request


    private void downloadDriverData() {
        // build URI
        // create util & parser in seperate class
        Uri uri = Uri.parse("https://ergast.com/api/f1/");
        Uri.Builder uriBuilder = uri.buildUpon();
        uriBuilder.appendPath(String.valueOf(mDataYear));
        uriBuilder.appendPath("driverStandings.json");
        // append again if u wanted to add constructors with a new arg but im restricting the scope
        Log.d(TAG, "the downloadDriverData method has been called");
        // final URL
        uri = uriBuilder.build();

        // volley req
        // use Volley to make the request
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri.toString(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // clear the data in driverInfo if there is any
                        driverInfo.clear();
                        // parse the response
                        DriverDataParser parser = new DriverDataParser();

                        Log.d(TAG, "on response has been ran");
                        try {
                            Log.d(TAG, " the try has been ran");
                            List<DriverInfo> driverInfo = parser.convertDriverInfoJson(response);
                            // store the Driver information to the database on a background thread
                            driverInfoRepo.storeInDatabase(driverInfo);
                            int i = 0;
                        } catch (JSONException | ParseException e) {
                            Log.d(TAG, e.getLocalizedMessage());
                            // display a mesasge to the user
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.driver_info_error), Toast.LENGTH_LONG);
                        }

                        // inform the adapter that the data has changed, to update the RecyclerView
                        // no need to do this as LiveData will alert us
                        // adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.driver_info_error), Toast.LENGTH_LONG);
                //Log.e(TAG, error.getLocalizedMessage());
            }
        });
        // now make the request
        Log.d(TAG, "the request is being queued with volley");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

}