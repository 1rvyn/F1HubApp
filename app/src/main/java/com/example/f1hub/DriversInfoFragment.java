package com.example.f1hub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriversInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriversInfoFragment extends Fragment implements View.OnClickListener {


    public static final String ARG_DATA_YEAR = "dataYear";
    // member variables for setting up display
    private int mDataYear;

    public DriversInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param dataYear THE YEAR THE DATA HAS TO BE SHOWN FOR
     * @return A new instance of fragment DriversInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriversInfoFragment newInstance(int dataYear) {
        DriversInfoFragment fragment = new DriversInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DATA_YEAR, dataYear);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataYear = getArguments().getInt(ARG_DATA_YEAR);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drivers_info, container, false);

        Button btnGetDriverData = view.findViewById(R.id.btnGetDriverData);
        btnGetDriverData.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        // Get the year entered
        EditText etEnterYear = getView().findViewById(R.id.etEnterYear);
        int yearOfData = Integer.parseInt(etEnterYear.getText().toString());

        // create bundle
        Bundle args = new Bundle();
        args.putInt("dataYear", yearOfData);

         if (v.getId() == R.id.btnGetDriverData){
            Navigation.findNavController(v).navigate(
                    R.id.action_driversInfoFragment_to_dataFragment, args);
        }
    }
}