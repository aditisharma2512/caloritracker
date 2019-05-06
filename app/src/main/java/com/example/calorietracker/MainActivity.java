package com.example.calorietracker;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText=(EditText) findViewById(R.id.editText) ;
        Button btnSearch = (Button) findViewById(R.id.btnFind);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String keyword = editText.getText().toString();
                SearchAsyncTask searchAsyncTask=new SearchAsyncTask();
                searchAsyncTask.execute(keyword);
            }

        }); }
    private class SearchAsyncTask extends AsyncTask<String, Void, String> { @Override
    protected String doInBackground(String... params) {
        return SearchGoogleAPI.search(params[0], new String[]{"num"}, new
                String[]{"1"}); }
        @Override
        protected void onPostExecute(String result) {
            TextView tv= (TextView) findViewById(R.id.tvResult);
            tv.setText(SearchGoogleAPI.getSnippet(result));
        } }
}
