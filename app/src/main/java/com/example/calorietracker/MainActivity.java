package com.example.calorietracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

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
                SearchAsyncImageTask searchAsyncImageTask = new SearchAsyncImageTask();
                searchAsyncImageTask.execute(keyword);
               // new DownloadImageFromInternet((ImageView) findViewById(R.id.image_view)).execute(keyword);

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

    private class SearchAsyncImageTask extends AsyncTask<String, Void, String> {

        ImageView imageView;
        @Override
        protected String doInBackground(String... params) {
            String result = SearchGoogleAPI.search(params[0], new String[]{"num", "searchType"}, new String[]{"1", "image"});
            String imageURL = SearchGoogleAPI.getImageLink(result);
            return imageURL;
        }

        @Override
        protected void onPostExecute(String imageURL) {
            imageView = (ImageView) findViewById(R.id.image_view);
            //TextView tv = (TextView) findViewById(R.id.tvResultImgURL);
            //tv.setText(SearchGoogleAPI.getImageLink(result));
            //Picasso p = Picasso.with(getBaseContext());
            Picasso.with(getBaseContext()).load(imageURL).into(imageView);

        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String result = SearchGoogleAPI.search(urls[0], new String[]{"num", "searchType","enableImageSearch"}, new String[]{"1", "image","true"});
            String imageURL = SearchGoogleAPI.getImageLink(result);
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }


    }
}
