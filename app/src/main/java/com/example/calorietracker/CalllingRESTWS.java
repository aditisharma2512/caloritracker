package com.example.calorietracker;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalllingRESTWS extends AppCompatActivity {
    private TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callling_restws);
        Button findAllCoursesBtn = (Button) findViewById(R.id.btnFindAll);
        findAllCoursesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursesAsyncTask getAllCourses = new CoursesAsyncTask();
                getAllCourses.execute();
            } });
    }
    private class CoursesAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground (Void... params){
            return RestClient.findAllCourses(); }
        @Override
        protected void onPostExecute (String courses){
            resultTextView = (TextView) findViewById(R.id.tvResult);
            resultTextView.setText(courses);
        }
    } }

    //http://localhost:8080/CalorieTracker/webresources/calorietrackerpackage.users/findByFirstname/%7Bfirstname%7D