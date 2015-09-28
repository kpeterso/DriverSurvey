package com.example.peterk2.driversurvey;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by peterk2 on 2/9/2015.
 */
public class Survey{
    private static final String LOG_TAG = "SurveyLog";
    //class that parses survey document (hopefully a CSV)
    //and directs fragment and question display

    private String surveyName;
    private String[] driverArray;
    private String[] carArray;
    private String driverName;
    private String recorderName;
    private String carName;

    private surveyQuestion[] questionArray = new surveyQuestion[100];
    private int questionCount = 0;

    public Survey(){
        //Empty Constructor
    }

    //Survey-level wrapper for question type getter
    public int getQuestionType(int questionNumber){ return questionArray[questionNumber].getQuestionType(); }

    //Survey driverArray getter
    public String[] getDriverArray() { return driverArray; }

    //Survey carArray getter
    public String[] getCarArray() { return carArray; }

    //Survey-level wrapper for SurveyQuestion currentQuestion getter
    public String getQuestionText(int questionNumber) { return questionArray[questionNumber].getQuestionText(); }

    //Survey-level wrapper for SurveyQuestion currentQuestion Answer getter
    public int getQuestionAnswer(int questionNumber) { return questionArray[questionNumber].getAnswer(); }

    //Survey-level wrapper for SurveyQuestion currentQuestion Answer setter
    public void setQuestionAnswer(int questionNumber,int answer) { questionArray[questionNumber].setAnswer(answer); }

    //Survey-level wrapper for SurveyQuestion answer existence check
    public boolean hasAnswer(int questionNumber) { return questionArray[questionNumber].hasAnswer(); }

    //Survey-level wrapper for SurveyQuestion comment existence check
    public boolean hasComment(int i) { return questionArray[i].hasComment(); }

    //Survey-level wrapper for SurveyQuestion comment getter
    public String getQuestionComment(int questionNumber) { return questionArray[questionNumber].getComment(); }

    //Survey-level wrapper for SurveyQuestion comment getter
    public void appendQuestionComment(int questionNumber, String comment) { questionArray[questionNumber].appendComment(comment); }

    //Survey-level wrapper for SurveyQuestion comment setter
    public void setQuestionComment(int questionNumber, String comment) { questionArray[questionNumber].setComment(comment); }

    //Survey surveyName setter
    public String getSurveyName() { return surveyName; }

    //Survey questionCount getter
    public int getQuestionCount() {return questionCount; }

    //Survey userName getter
    public String getUserName() { return driverName; }

    //Survey userName setter
    public void setDriverName(String name) { driverName = name; }

    //Survey userName getter
    public String getDriverName() { return driverName; }

    //Survey userName setter
    public void setRecorderName(String name) { recorderName = name; }

    //Survey userName getter
    public String getRecorderName() { return recorderName; }

    //Survey carName setter
    public void setCarName(String name) { carName = name; }

    //Survey carName getter
    public String getCarName() { return carName; }

    //Survey questions ArrayList getter
    public ArrayList<String> getSurveyQuestionList() {
        ArrayList<String> questionList = new ArrayList<String>();
        for(int i=0;i<questionCount;i++){
            questionList.add(Integer.toString(i+1)+". "+questionArray[i].getQuestionText());
        }
        return questionList;
    }

    public boolean loadSurvey(Context context) {
        //load survey file into array
        //CSV input Layout:
        //SurveyName
        //list of drivers separated by commas
        //list of cars separated by commas
        //question number, questionText
        //String fpath=getSurveyStorageDir(context,"Test Survey").getAbsolutePath()+"/test.csv";
        //File f = new File(fpath);
        try {
            InputStream in = context.getResources().openRawResource(context.getResources().getIdentifier("surveyquestions",
                    "raw", context.getPackageName()));
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(in));
            String inputString;
            //input the name of the survey from the survey csv
            surveyName = inputReader.readLine();
            //import an array of drivers from 2nd line of survey
            driverArray = inputReader.readLine().split(",");
            //import an array of cars from 3rd line of survey
            carArray = inputReader.readLine().split(",");
            //import question types and questions
            String temp[];
            while ((inputString = inputReader.readLine()) != null) {
                Log.i(LOG_TAG,"The survey question being imported: "+ inputString);
                temp=inputString.split(",");
                questionArray[questionCount]=new surveyQuestion(Integer.parseInt(temp[0]),temp[1]);
                questionCount++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void saveResults(Context context){
        File f;
        BufferedWriter buf;
        if(isExternalStorageWritable()){
            try{
                String path=getSurveyStorageDir(context,surveyName).getAbsolutePath()+"/results.csv";
                Log.i(LOG_TAG,"The file is at: " + path);
                f=new File(path);
                //if file doesn't exist, create file
                if(!(f.exists())) {f.createNewFile();}
                //only append data to file
                buf = new BufferedWriter(new FileWriter(f,true));
                //write data about user and survey
                buf.write(surveyName+","+carName + ","+driverName+","+recorderName);
                buf.newLine();
                //begin writing survey results
                for(int i=0; i<questionCount; i++){
                    int answer = questionArray[i].getAnswer();
                    String comment = questionArray[i].getComment();
                    Log.i(LOG_TAG,"writing answer + comment "+i+","+comment);
                    buf.write(answer+ "," + comment);
                    buf.newLine();
                }
                buf.flush();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public File getSurveyStorageDir(Context context, String fileName) {
        // Get the directory for the app's private survey directory.
        File file = new File(context.getExternalFilesDir(null), fileName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    public class surveyQuestion{
        //A class that stores a single survey question.

        public int getQuestionType() { return questionType; }
        public void setQuestionType(int i) { questionType=i; }

        public String getQuestionText() {
            return questionText;
        }

        public int getAnswer() {
            return answer;
        }

        public void setAnswer(int i){
            answer = i;
            hasAnswer=true;
        }

        public boolean hasAnswer() { return hasAnswer; }

        public void appendComment(String s){
            comment=comment+" "+s;
            hasComment=true;
        }

        public void setComment(String s) {
            comment=s;
            hasComment=true;
        }

        public String getComment() { return comment; }

        public boolean hasComment() { return hasComment; }

        /*private boolean verifyType(String type){
            //validates if question being generated is the correct type
            String YesNo="YesNo";
            return type.equals(YesNo);
        }*/

        private int questionType = 0;
        private String questionText;
        private String comment = "";
        private int answer = 2;

        private boolean hasAnswer;
        private boolean hasComment;

        public surveyQuestion(int questionType, String questionText){
            this.questionType = questionType;
            this.questionText = questionText;
            this.hasAnswer=false;
            this.hasComment=false;
        }
    }
}
