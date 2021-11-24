package com.example.f1hub;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.f1hub.data.DriverInfo;
import com.example.f1hub.data.DriverInfoRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Driver;
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

    private static final String TAG = "Year_DATA_HERE";
    private static final String TAG2 = "DataListArray";

    public DataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        // create the button when the view is created
        Button btnHomePage = view.findViewById(R.id.btnHomePage);
        btnHomePage.setOnClickListener(this);


        // recycler view creation in view
        // this is just placeholder while we wait to download the data
        List<DriverInfo> driverInfo = DriverInfoRepo.getRepository(getContext()).getDriverInformation(1000);


        RecyclerView recyclerView = view.findViewById(R.id.rv_DriverInformation);
        // get from repo
        RecyclerView.Adapter adapter = new DriverInformationRecyclerViewAdapter(getContext(), driverInfo);
        // set the adapter on the rv
        recyclerView.setAdapter(adapter);

        // make the HTTP get request

        // build URI
        Uri uri = Uri.parse("https://ergast.com/api/f1/");
        Uri.Builder uriBuilder = uri.buildUpon();
        uriBuilder.appendPath(String.valueOf(mDataYear));
        uriBuilder.appendPath("driverStandings.json");
        // append again if u wanted to add constructors with a new arg but im restricting the scope

        // final URL
        uri = uriBuilder.build();

        // volley req
        StringRequest request = new StringRequest(Request.Method.GET,
                uri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        // time to actually process if we get given a response and not an error
                        // will essentially be displaying the data in the DriverInfo list
                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject MRData = rootObject.getJSONObject("MRData");
                            JSONObject standingsObj = MRData.getJSONObject("StandingsTable");
                            JSONArray standingsArray = standingsObj.getJSONArray("StandingsLists");
                            driverInfo.clear();
                            for (int i = 0, j = standingsArray.length(); i < j; i++){
                                JSONObject standingsArrayObj = standingsArray.getJSONObject(i);
                                JSONArray driverStandings = standingsArrayObj.getJSONArray("DriverStandings");
                                for (int ii = 0, jj = driverStandings.length(); ii < jj; ii++) {
                                    JSONObject dStandingsObj = driverStandings.getJSONObject(ii);
                                    String wins = dStandingsObj.getString("wins");
                                    String points = dStandingsObj.getString("points");
                                    String position = dStandingsObj.getString("position");
                                    JSONObject driverObj = dStandingsObj.getJSONObject("Driver");
                                    String driverName = driverObj.getString("driverId");
                                    // test Log.d(TAG2, "drivers name are as follows ;" + driverName);
                                    DriverInfo di = new DriverInfo();
                                    di.setDriverName(driverName);
                                    di.setDriverPoints(points);
                                    di.setDriverWins(wins);
                                    di.setDriverPos(position);
                                    driverInfo.add(di);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                getString(R.string.data_frag_download_error),Toast.LENGTH_LONG);
                        Log.e(TAG, error.getLocalizedMessage());
                    }
        });

        // make the request on the build URI/URL
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // add the year from the user input (through arg im lazy)
        TextView tvYearValue = view.findViewById(R.id.tvYearValue);
        tvYearValue.setText(String.valueOf(mDataYear));

        // see if the year is getting passed through the fragments correctly
        Log.d(TAG, String.valueOf(mDataYear));

        return view;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnHomePage) {
            Navigation.findNavController(v).navigate(R.id.action_dataFragment_to_locationSelectionFragment);
        }
    }
}