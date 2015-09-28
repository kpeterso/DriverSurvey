package com.example.peterk2.driversurvey;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements LikeDislikeFragment.OnFragmentInteractionListener{
    private static final String LOG_TAG = "Main Activity Log";
    private static final int REQUEST_CODE=1234;
    private static final int LIKE=1;
    private static final int NEUTRAL=0;
    private static final int DISLIKE=-1;

    /**
     * The {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * The {@link Survey} that will host the survey data.
     */
    Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "I'M ALIVE!");
        //create Survey instance
        survey=new Survey();
        survey.loadSurvey(getApplicationContext());

        //Extract data from intent and insert into survey
        Intent intent = getIntent();
        survey.setDriverName(intent.getStringExtra(MainMenuActivity.EXTRA_DRIVER));
        survey.setRecorderName(intent.getStringExtra(MainMenuActivity.EXTRA_RECORDER));
        survey.setCarName(intent.getStringExtra(MainMenuActivity.EXTRA_CARTYPE));

        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Button speakButton = (Button) findViewById(R.id.comment_button);

        //wordsList = (ListView) findViewById(R.id.list);

        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
        Log.i(LOG_TAG, "Exiting onCreate");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_end:
                /*EditText v = (EditText) mViewPager.getFocusedChild().findViewById(R.id.commentText);
                survey.setQuestionComment(mViewPager.getCurrentItem(), v.getText().toString());*/
                //save the current survey state
                survey.saveResults(this);
                //start the main menu activity
                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
                //end this activity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*    //set current question answer to LIKE
    public void onLikeInteraction(View view){
        Log.i(LOG_TAG,"in onLikeInteraction");
        setSurveyAnswer(LIKE);
    }
    //set current question answer to DISLIKE
    public void onDislikeInteraction(View view){
        Log.i(LOG_TAG,"in onDislikeInteraction");
        setSurveyAnswer(DISLIKE);
    }
    //set current question answer to NEUTRAL
    public void onNeutralInteraction(View view) {
        Log.i(LOG_TAG, "in onNeutralInteraction");
        setSurveyAnswer(NEUTRAL);
    }

    public void setSurveyAnswer(int answer){
        Log.i(LOG_TAG, "in onRadioInteraction");
        EditText v = (EditText) findViewById(R.id.commentText);
        survey.setQuestionComment(mViewPager.getCurrentItem(), v.getText().toString());
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        survey.setQuestionAnswer(mViewPager.getCurrentItem(),answer);
    }*/

    /*public void onLeaveCommentClick(View v)
    {
        startVoiceRecognitionActivity();
    }*/

/*    private void startVoiceRecognitionActivity()
    {
        Log.i(LOG_TAG, "Starting Voice Recognition Activity.");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Comment Recognition");
        startActivityForResult(intent, REQUEST_CODE);
    }

    *//**
     * Handle the results from the voice recognition activity.
     *//*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(LOG_TAG, "Attempting Voice Recognition.");
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Log.i(LOG_TAG, "Voice Recognition finished, setting text.");
            //get the commentText textview
            TextView v = (TextView) this.mViewPager.getFocusedChild().findViewById(R.id.commentText);
            //Set Textview to display matches
            Log.i(LOG_TAG, "TextView found, setting text");
            v.setText(v.getText() + " " + matches.get(0));
            Log.i(LOG_TAG, "text set, exiting if statement");
        }
        Log.i(LOG_TAG, "kicking activity to super");
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(LOG_TAG, "function complete");
    }*/

    @Override
    public void setComment(String comment, int questionNumber){
        survey.setQuestionComment(questionNumber, comment);
    }

    @Override
    public void setAnswer(int answer, int questionNumber){
        survey.setQuestionAnswer(questionNumber, answer);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private int numQuestions=survey.getQuestionCount();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int questionNumber) {
            Log.i(LOG_TAG,"Creating new fragment");

            if(mViewPager.getFocusedChild()!=null) {
                EditText v = (EditText) mViewPager.getFocusedChild().findViewById(R.id.commentText);

            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a like_dislike_fragment.
            //NewInstance definition is:
            //newInstance(String question, int answer, String comment, int questionNumber)
            switch (survey.getQuestionType(questionNumber)) {
                case 1:
                    return LikeDislikeFragment.newInstance(survey.getQuestionText(questionNumber), survey.getQuestionAnswer(questionNumber), survey.getQuestionComment(questionNumber), questionNumber, survey.getQuestionCount());
                case 2:
                    return ThumbsFragment.newInstance(survey.getQuestionText(questionNumber), survey.getQuestionAnswer(questionNumber), survey.getQuestionComment(questionNumber), questionNumber, survey.getQuestionCount());
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            Log.i(LOG_TAG,"Getting number of survey question.");
            // retrieve questionCount from survey object
            return numQuestions;
        }
    }

}
