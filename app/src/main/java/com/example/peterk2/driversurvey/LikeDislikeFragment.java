package com.example.peterk2.driversurvey;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikeDislikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikeDislikeFragment extends Fragment {
    private static final String LOG_TAG = "LikeDislikeFragment Log";
    private static final int REQUEST_CODE=1234;

    //initialize fragment listener
    private OnFragmentInteractionListener mListener;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String currentQuestionString = "currentQuestionString";
    private static final String currentAnswerNumber = "currentAnswerNumber";
    private static final String currentCommentString = "currentCommentString";
    private static final String currentQuestionNumber = "currentQuestionNumber";
    private static final String totalQuestionsNumber = "totalQuestionNumber";

    private String mQuestionString;
    private int mAnswerNumber=2;
    private String mCommentString;
    private int mQuestionNumber;
    private int mTotalQuestionsNumber;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question Survey question string.
     * @param answer Survey answer integer.
     * @param comment Survey comment string.
     * @param questionNumber survey question integer.
     * @return A new instance of fragment like_dislike_fragment.
     */
    public static LikeDislikeFragment newInstance(String question, int answer, String comment, int questionNumber, int totalQuestions) {
        LikeDislikeFragment fragment = new LikeDislikeFragment();
        Bundle args = new Bundle();
        args.putString(currentQuestionString, question);
        args.putInt(currentAnswerNumber, answer);
        args.putString(currentCommentString, comment);
        args.putInt(currentQuestionNumber, questionNumber);
        args.putInt(totalQuestionsNumber, totalQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    public LikeDislikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "LikeDislikeFragment Created.");
        if (getArguments() != null) {
            mQuestionString = getArguments().getString(currentQuestionString);
            mAnswerNumber = getArguments().getInt(currentAnswerNumber);
            mCommentString = getArguments().getString(currentCommentString);
            mQuestionNumber = getArguments().getInt(currentQuestionNumber);
            mTotalQuestionsNumber = getArguments().getInt(totalQuestionsNumber);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_like_dislike, container, false);


        View.OnClickListener buttonListener=new View.OnClickListener() {
            public void onClick(View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                Log.i(LOG_TAG,"Comment Voice Recognition Button Pushed");
                startVoiceRecognitionActivity();
            }
        };
        Button addCommentButton = (Button) v.findViewById(R.id.comment_button);
        addCommentButton.setOnClickListener(buttonListener);

        Log.i(LOG_TAG,"Creating LikeDislikeFragment view.");
        // Inflate the layout for this fragment

        //set Question Number text at top of page
        View tv1 = v.findViewById(R.id.questionNumberTextView);
        ((TextView)tv1).setText(Integer.toString(mQuestionNumber+1));

        //set the Question text at the top of the page
        View tv2 = v.findViewById(R.id.questionTextView);
        ((TextView)tv2).setText(mQuestionString);

        //set the number of questions at the top of the page
        View tv3 = v.findViewById(R.id.totalQuestionNumberTextView);
        ((TextView)tv3).setText("of "+ Integer.toString(mTotalQuestionsNumber));

        //Set up Like Dislike Neutral Radiogroup
        RadioGroup r=(RadioGroup)v.findViewById(R.id.like_dislike_group);
        //add listener to radiogroup to set answer when new button is selected
        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int answer;
                // checkedId is the RadioButton selected
                Log.i(LOG_TAG,"Changing Radiogroup Answer.");
                switch (checkedId) {
                    case R.id.dislike_button:
                        answer = -1;
                        break;
                    case R.id.like_button:
                        answer = 1;
                        break;
                    case R.id.neutral_button:
                        answer = 0;
                        break;
                    default:
                        answer = 2;
                }
                mListener.setAnswer(answer, mQuestionNumber);
            }
        });
        //if an answer is selected, select that button
        if (mAnswerNumber == 1) {
            r.check(R.id.like_button);
        } else if (mAnswerNumber == 0) {
            r.check(R.id.neutral_button);
        } else if (mAnswerNumber == -1) {
            r.check(R.id.dislike_button);
        }

        EditText comment = (EditText)v.findViewById(R.id.commentText);
        comment.setText(mCommentString);
        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Log.i(LOG_TAG,"Saving current text into survey: " + getCurrentComment());
                    mListener.setComment(getCurrentComment(), mQuestionNumber);
                }
            }
        });
        comment.setText(mCommentString);

        return v;
    }

    private String getCurrentComment() {
        String comment;
        if (this.getView().findViewById(R.id.commentText)!=null) {
            EditText c = (EditText) this.getView().findViewById(R.id.commentText);
            comment=c.getText().toString();
        } else {
            comment="";
        }
        return comment;
    }

    private void startVoiceRecognitionActivity()
    {
        Log.i(LOG_TAG, "Starting Voice Recognition Activity.");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Comment Recognition");
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(LOG_TAG, "Attempting Voice Recognition.");
        if (requestCode == REQUEST_CODE && resultCode == MainActivity.RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Log.i(LOG_TAG, "Voice Recognition finished, setting text.");
            //get the commentText textview
            EditText v = (EditText) this.getView().findViewById(R.id.commentText);
            //Set Textview to display matches
            Log.i(LOG_TAG, "EditText found, setting text");
            v.setText(v.getText().toString() + " " + matches.get(0));
            mListener.setComment(v.getText().toString(), mQuestionNumber);
            Log.i(LOG_TAG, "EditText set to " + v.getText().toString());
            Log.i(LOG_TAG, "text set, exiting if statement");
        }

        Log.i(LOG_TAG, "kicking activity to super");
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(LOG_TAG, "function complete");
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
        public void setComment(String comment, int questionNumber);
        public void setAnswer(int answer, int questionNumber);
    }
}
