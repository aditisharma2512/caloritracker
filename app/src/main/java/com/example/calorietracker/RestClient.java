package com.example.calorietracker;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class RestClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/CalorieTracker/webresources/";

    public static String connResultGet(URL url) {
        HttpURLConnection conn = null;
        String testResult = "";

        try {

            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input steream and store it as string
            while (inStream.hasNextLine()) {
                testResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return testResult;

    }


    public static void connResultPost(URL url, String postJsonStr) {
        HttpURLConnection conn = null;
        try {
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(postJsonStr.getBytes().length); //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(postJsonStr);
            out.close();
            Log.i("error", new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

    }


    public static String findAllCourses() {
        final String methodPath = "calorietrackerpackage.users/findByFirstname/John";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input steream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String verifyLogin(String username, String password) {
        final String methodPath = "calorietrackerpackage.credential/login";
        //initialise
        URL url = null;
        String result = "";
        String testResult = "";
        //Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + "/" + username + "/" + password);
            testResult = connResultGet(url);
            JSONObject jsonObject = new JSONObject(testResult);
            result = jsonObject.get("match").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getUserData(String username) {
        final String methodPath = "calorietrackerpackage.credential/findByUsername";
        URL url = null;
        String credentialResult = "";
        try {
            url = new URL(BASE_URL + methodPath + "/" + username);
            credentialResult = connResultGet(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return credentialResult;
    }

    public static String getReportData(String reportDate, String userid) {
        final String methodPath = "calorietrackerpackage.report/getCalorieReport";
        URL url = null;
        String reportResult = "";
        try {
            url = new URL(BASE_URL + methodPath + "/" + userid + "/" + reportDate);
            reportResult = connResultGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportResult;

    }

    public static boolean verifyUsername(String username) {
        final String methodPath = "calorietrackerpackage.credential/findByUsername";
        URL url = null;
        boolean match = false;
        String result = "";
        try {
            url = new URL(BASE_URL + methodPath + "/" + username);
            result = connResultGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((result.equals(""))) {
            return match;
        } else {
            match = true;
            return match;
        }
    }


    public static String getFoodData() {
        final String methodPath = "calorietrackerpackage.food";
        URL url = null;
        String result = "";
        try {
            url = new URL(BASE_URL + methodPath);
            result = connResultGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getFoodDetails(String category, String foodname) {
        final String methodPath = "calorietrackerpackage.food/findByFoodNameANDCategory";
        URL url = null;
        String result = "";
        try {
            url = new URL(BASE_URL + methodPath + "/" + foodname + "/" + category);
            result = connResultGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static Integer getUserId()
    {
        Integer userId = 1;
        final String methodPath = "calorietrackerpackage.users/";
        String result = "";
        URL url = null;
        try {

            url = new URL(BASE_URL + methodPath);
            result = connResultGet(url);
            JSONArray jsonArray = new JSONArray(result);
            List<Integer> UserIds = new ArrayList<>();
            for(int i=0; i<jsonArray.length();i++)
            {
                Integer id = jsonArray.getJSONObject(i).getInt("userid");
                UserIds.add(id);
            }

            userId = Collections.max(UserIds) + 1;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return userId;

    }

    public static void createUser(Users user) {

        final String methodPath = "calorietrackerpackage.users/";
        URL url = null;
        try {
            //Gson gson = new Gson();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUsersJson = gson.toJson(user);
            url = new URL(BASE_URL + methodPath);

            connResultPost(url,stringUsersJson);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void createCredential(Credential credential) {

        final String methodPath = "calorietrackerpackage.credential/";
        URL url = null;
        try {
            //Gson gson = new Gson();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUsersJson = gson.toJson(credential);
            url = new URL(BASE_URL + methodPath);

            connResultPost(url,stringUsersJson);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

