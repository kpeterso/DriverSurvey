package com.example.peterk2.driversurvey;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThumbsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThumbsFragment extends Fragment {
    private static final String LOG_TAG = "ThumbFragment Log";
    // the fragment initialization parameters
    private static final String currentQuestionString = "currentQuestionString";
    private static final String currentAnswerNumber = "currentAnswerNumber";
    private static final String currentCommentString = "currentCommentString";
    private static final String currentQuestionNumber = "currentQuestionNumber";
    private static final String totalQuestionsNumber = "totalQuestionNumber";

    // parameters used in Bundle
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
     * @return A new instance of fragment ThumbsFragment.
     */
    public static ThumbsFragment newInstance(String question, int answer, String comment, int questionNumber, int totalQuestions) {
        ThumbsFragment fragment = new ThumbsFragment();
        Bundle args = new Bundle();
        args.putString(currentQuestionString, question);
        args.putInt(currentAnswerNumber, answer);
        args.putString(currentCommentString, comment);
        args.putInt(currentQuestionNumber, questionNumber);
        args.putInt(totalQuestionsNumber, totalQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    public ThumbsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "ThumbsFragment Created.");
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
        // Inflate the layout for this fragment
        Log.i(LOG_TAG,"Creating ThumbFragment view.");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_like_dislike, container, false);

        //set Question Number text at top of page
        View tv1 = v.findViewById(R.id.questionNumberTextView);
        ((TextView)tv1).setText(Integer.toString(mQuestionNumber+1));

        //set the Question text at the top of the page
        View tv2 = v.findViewById(R.id.questionTextView);
        ((TextView)tv2).setText(mQuestionString);

        //set the number of questions at the top of the page
        View tv3 = v.findViewById(R.id.totalQuestionNumberTextView);
        ((TextView)tv3).setText("of "+ Integer.toString(mTotalQuestionsNumber));

        //set Radiogroup button if answer was previously selected
        RadioGroup r=(RadioGroup)v.findViewById(R.id.like_dislike_group);

        //if an answer is selected, select that button
        if (mAnswerNumber == 1) {
            r.check(R.id.like_button);
        } else if (mAnswerNumber == -1) {
            r.check(R.id.dislike_button);
        }

        EditText comment = (EditText)v.findViewById(R.id.commentText);
        comment.setText(mCommentString);

        return v;
    }


}
