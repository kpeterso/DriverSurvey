package com.example.peterk2.driversurvey;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SettingsFragment extends Fragment {
    private static final String LOG_TAG = "LikeDislikeFragment Log";

    private OnFragmentInteractionListener mListener;
    private EditText emailAddress;


    /**
     * Use this factory method to create a new instance of
     * this fragment
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_settings, container, false);
        View.OnClickListener buttonListener=new View.OnClickListener() {
            public void onClick(View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                Log.i(LOG_TAG,"Button Pushed");
                onButtonPressed();
            }
        };
        emailAddress=(EditText) view.findViewById(R.id.email_address);
        emailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
            }
        });
        Button sendEmailButton = (Button) view.findViewById(R.id.send_email_button);
        sendEmailButton.setOnClickListener(buttonListener);

        return view;
    }

    public void onButtonPressed() {
        String email = emailAddress.getText().toString();
        if (mListener != null) {
            Log.i("Settings", "Sending email to "+email);
            mListener.onSendEmailInteraction(email);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onSendEmailInteraction(String email);
    }
}