package com.example.calorietracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GoogleParks {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String API_KEY = "AIzaSyC1XyBGa3GqAE_dQJPFlJpDW2V2BSyUMx8";

    public static String search(String latitude, String longitude, String proximity, String place)
    {
        int proximityInMetres = Integer.parseInt(proximity)*1000;
        String radius = Integer.toString(proximityInMetres);
        final String methodPath = "location="+latitude+","+longitude+"&radius="+radius+"&type="+place+"&key="+API_KEY;
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;

    }


    public static List<Park> getPlaces(String result)
    {
        List<Park> park = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            int NumberOfParks = jsonArray.length();

            //park = new Park[NumberOfParks];

            for(int i=0; i<NumberOfParks; i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                Double lat = object.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lng = object.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                String name = object.getString("name");
                Park obj = new Park(lat,lng,name);
                park.add(obj);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return park;
    }


}
