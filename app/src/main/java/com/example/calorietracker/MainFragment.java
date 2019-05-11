package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends Fragment {
    View vMain;
    TextView welcomeMessage,welcomeDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {


        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        welcomeMessage = (TextView) vMain.findViewById(R.id.welcomemsg);
        SharedPreferences spMyUnits =
                getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        String user= spMyUnits.getString("firstname",null);
        welcomeMessage.setText("Welcome " +user +"!");

        welcomeDate = (TextView) vMain.findViewById(R.id.tv_datetime) ;

        //String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());

        String time ="";
        Calendar c = Calendar.getInstance();
        if(c.get(Calendar.AM_PM)==0)
            time=" AM";
        else
            time=" PM";

        String date = c.get(Calendar.DAY_OF_MONTH) + " " + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + c.get(Calendar.YEAR) + ", " + c.get(Calendar.HOUR) + " : " +c.get(Calendar.MINUTE) + time;

        //String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        welcomeDate.setText(date);
        return vMain;
    }
}