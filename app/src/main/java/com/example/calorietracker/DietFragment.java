package com.example.calorietracker;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DietFragment extends Fragment {

    View vDiet;
    private String foodData, categorySelected;
    Button searchBtn;
    TextView tvFat, tvCalorie,tvResult;
    ImageView imageView;
    EditText etSearch;
    Spinner categorySpinner, foodSpinner;
    List<String> category;
    ArrayAdapter<String> adapterC, adapterF;
    Map<String, List<String>> foodInCategory = new HashMap<String, List<String>>();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment

        vDiet = inflater.inflate(R.layout.fragment_diet, container, false);

        categorySpinner = (Spinner) vDiet.findViewById(R.id.category_spinner);
        foodSpinner = (Spinner) vDiet.findViewById(R.id.food_spinner);
        tvCalorie = (TextView) vDiet.findViewById(R.id.tv_calorie);
        tvFat = (TextView) vDiet.findViewById(R.id.tv_fat);
        searchBtn = (Button) vDiet.findViewById(R.id.searchBtn);
        etSearch = (EditText) vDiet.findViewById(R.id.et_search);
        imageView = (ImageView) vDiet.findViewById(R.id.searchImage);
        tvResult = (TextView) vDiet.findViewById(R.id.tvResult);


        FoodData foodData = new FoodData();
        foodData.execute();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = (String) categorySpinner.getItemAtPosition(position);
                setFoodItems(categorySelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String foodSelected = (String) foodSpinner.getItemAtPosition(position);
                GetFoodDetails obj = new GetFoodDetails();
                obj.execute(categorySelected, foodSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Button btnSearch = (Button) vDiet.findViewById(R.id.btnFind);
        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String keyword = etSearch.getText().toString();
                SearchAsyncTask searchAsyncTask=new SearchAsyncTask();
                searchAsyncTask.execute(keyword);
                SearchAsyncImageTask searchAsyncImageTask = new SearchAsyncImageTask();
                searchAsyncImageTask.execute(keyword);

            }

        });


        return vDiet;
    }

    private class FoodData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                foodData = RestClient.getFoodData();
                if (!(foodData.equals("[]"))) {
                    Set<String> categorySet = new HashSet<>();
                    JSONArray jsonArray = new JSONArray(foodData);
                    for (int i = 0, size = jsonArray.length(); i < size; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String category = jsonObject.getString("category");
                        String foodItem = jsonObject.getString("foodname");
                        if (foodInCategory.containsKey(category)) {
                            foodInCategory.get(category).add(foodItem);
                        } else {
                            List<String> foodItemList = new ArrayList<>();
                            foodItemList.add(foodItem);
                            foodInCategory.put(category, foodItemList);
                        }
                        int j = foodInCategory.size();
                        System.out.print(j);
                        categorySet.add(category);

                    }
                    List<String> other = new ArrayList<>();
                    foodInCategory.put("Other", other);
                    //int i = jsonArray.length();
                    //System.out.print(i);
                    category = new ArrayList<String>(categorySet);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                adapterC = new ArrayAdapter<String>(vDiet.getContext(), R.layout.spinner_ui, category);
                adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapterC);
                Toast.makeText(vDiet.getContext(), "Food Data Generated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(vDiet.getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class GetFoodDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = RestClient.getFoodDetails(params[0], params[1]);
            System.out.print(result);
            return result;

        }

        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                String calorie = jsonArray.getJSONObject(0).getString("calorieamount");
                String fat = jsonArray.getJSONObject(0).getString("fat");

                //System.out.print(calorie);
                //System.out.print(fat);
                tvCalorie.setText(calorie);
                tvFat.setText(fat);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, String> { @Override
    protected String doInBackground(String... params) {
        return SearchGoogleAPI.search(params[0], new String[]{"num"}, new
                String[]{"1"}); }
        @Override
        protected void onPostExecute(String result) {
        String snippet = SearchGoogleAPI.getSnippet(result);
            tvResult.setText(snippet);
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
            //imageView = (ImageView) findViewById(R.id.image_view);
            //TextView tv = (TextView) findViewById(R.id.tvResultImgURL);
            //tv.setText(SearchGoogleAPI.getImageLink(result));
            try {
                Picasso p = Picasso.with(getActivity().getBaseContext());
                imageView = (ImageView) vDiet.findViewById(R.id.searchImage);
                p.load(imageURL).into(imageView);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

        protected void setFoodItems(String categorySelected) {
            List<String> foodItemsList = foodInCategory.get(categorySelected);
            adapterF = new ArrayAdapter<String>(vDiet.getContext(), R.layout.spinner_ui, foodItemsList);
            adapterF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            foodSpinner.setAdapter(adapterF);


        }

}
