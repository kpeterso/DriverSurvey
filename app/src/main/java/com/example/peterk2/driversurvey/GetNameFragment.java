package com.example.peterk2.driversurvey;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetNameFragment extends Fragment{

    private static final String driverArrayString = "driverArray";
    private static final String carArrayString = "carArray";

    private String[] driverArray;
    private String[] carArray;

    private Spinner driverSpinner, recorderSpinner, carSpinner;

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @param drivers String array of driver names.
     * @param cars String array of car names.
     * @return A new instance of fragment GetNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetNameFragment newInstance(String[] drivers, String[] cars) {
        GetNameFragment fragment = new GetNameFragment();
        Bundle args = new Bundle();
        args.putStringArray(driverArrayString, drivers);
        args.putStringArray(carArrayString, cars);
        fragment.setArguments(args);
        return fragment;
    }

    public GetNameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            driverArray = getArguments().getStringArray(driverArrayString);
            carArray = getArguments().getStringArray(carArrayString);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_name, container, false);

        //set up spinners
        spinnerSetUp(view);

        return view;
    }

    private void spinnerSetUp(View view) {
        driverSpinner = (Spinner) view.findViewById(R.id.driverSpinner);
        recorderSpinner = (Spinner) view.findViewById(R.id.recorderSpinner);
        carSpinner = (Spinner) view.findViewById(R.id.carSpinner);

        ArrayAdapter<String> driverAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),R.layout.support_simple_spinner_dropdown_item,driverArray);
        ArrayAdapter<String> carAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),R.layout.support_simple_spinner_dropdown_item,carArray);

        driverSpinner.setAdapter(driverAdapter);
        recorderSpinner.setAdapter(driverAdapter);
        carSpinner.setAdapter(carAdapter);

    }
}
