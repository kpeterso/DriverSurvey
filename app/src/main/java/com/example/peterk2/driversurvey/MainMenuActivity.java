package com.example.peterk2.driversurvey;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;


public class MainMenuActivity extends ActionBarActivity implements SettingsFragment.OnFragmentInteractionListener{

    private static final String LOG_TAG = "Main Activity Log";

    public final static String EXTRA_DRIVER = "com.mycompany.myfirstapp.Driver";
    public final static String EXTRA_RECORDER = "com.mycompany.myfirstapp.Recorder";
    public final static String EXTRA_CARTYPE = "com.mycompany.myfirstapp.CarType";

    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load survey file
        survey=new Survey();

        setContentView(R.layout.activity_main_menu);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.main_menu_container, MainMenuFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSettingsClick(View view){
        Log.i(LOG_TAG, "Entering Settings fragment");
        //create settings fragment
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_menu_container,SettingsFragment.newInstance());
        transaction.commit();
    }

    public void onResetResultsClick(View view){
        Log.i(LOG_TAG,"deleting files folder and all children");
        String path="/storage/sdcard0/Android/data/com.example.peterk2.driversurvey/files/Test Survey/results.csv";
        String archivePath="/storage/sdcard0/Android/data/com.example.peterk2.driversurvey/files/Test Survey Archive";
        File f = new File(path);
        f.delete();
    }

    public void onSettingsBackClick(View view) {
        Log.i(LOG_TAG,"Go back to main menu screen");
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_menu_container,MainMenuFragment.newInstance());
        transaction.commit();
    }

    public void onSurveyStartClick(View view){
        Log.i(LOG_TAG,"Entering onStartSurveyClick Function");
        if(!survey.loadSurvey(getApplicationContext())){
            TextView v = (TextView)this.findViewById(R.id.main_menu_textView);
            v.setText("Please copy survey into folder.");
        } else {
            //Create a new "GetNameFragment" and replace MainMenuFragment
            //String[] a={"driver 1","driver 2"};
            //String[] b={"car 1","car 2"};
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.main_menu_container, GetNameFragment.newInstance(survey.getDriverArray(),survey.getCarArray()));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void onGetNameClick(View view){
        Log.i(LOG_TAG,"Entering onGetNameClick Function");
        Intent intent = new Intent(this, MainActivity.class);
        Spinner driverSpinner = (Spinner) findViewById(R.id.driverSpinner);
        String driver = driverSpinner.getSelectedItem().toString();
        Spinner recorderSpinner = (Spinner) findViewById(R.id.recorderSpinner);
        String recorder = recorderSpinner.getSelectedItem().toString();
        Spinner carSpinner = (Spinner) findViewById(R.id.carSpinner);
        String carType = carSpinner.getSelectedItem().toString();
        intent.putExtra(EXTRA_DRIVER,driver);
        intent.putExtra(EXTRA_RECORDER,recorder);
        intent.putExtra(EXTRA_CARTYPE,carType);
        startActivity(intent);

        finish();
    }

    @Override
    public void onSendEmailInteraction(String email) {
        Log.i(LOG_TAG,"Sending email to "+email);
        new emailResults().sendEmail(getApplicationContext(),email,"results.csv");
        Log.i(LOG_TAG,"Email Sent");
    }
}