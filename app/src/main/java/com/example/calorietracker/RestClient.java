package com.example.calorietracker;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/CalorieTracker/webresources/";

    public static String connResult(URL url)
    {
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
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace(); } finally {
            conn.disconnect(); }
        return textResult; }

    public static String verifyLogin(String username, String password) {
        final String methodPath = "calorietrackerpackage.credential/login";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        String testResult = "";
        //Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + "/" + username + "/" + password);
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
            JSONObject jsonObject = new JSONObject(testResult);
            result = jsonObject.get("match").toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return result;
    }


    public static String getUserData(String username)
    {
        final String methodPath = "calorietrackerpackage.credential/findByUsername";
        URL url = null;
        String credentialResult = "";
        try {
            url = new URL(BASE_URL + methodPath + "/" + username);
            credentialResult = connResult(url);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return credentialResult;
    }

    public static String getReportData(String reportDate, String userid)
    {
        final String methodPath = "calorietrackerpackage.report/getCalorieReport";
        URL url = null;
        String reportResult = "";
        try
        {
            url = new URL(BASE_URL + methodPath + "/" + userid + "/" + reportDate);
            reportResult = connResult(url);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return reportResult;

    }

}

