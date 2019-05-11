package com.example.calorietracker;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportFragment extends Fragment {
    View vReport;
    private float[] yData = {25.3f, 10.6f, 66.76f};
    private String[] xData = {"consumed", "burned", "remaining"};
    private EditText dateView;
    private Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener mDateSetListener;
    PieChart pieChart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_report);

        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        dateView = (EditText) vReport.findViewById(R.id.reportDate);


        final DatePickerDialog picker = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show();
            }
        });

        //Description desc = (Description)"Calorie Report";
        pieChart = (PieChart) vReport.findViewById(R.id.idPieChart) ;
        //pieChart.setDescription();
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        
        addDataSet();



        GetData obj = new GetData();
        obj.execute();

        return vReport;
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i=0; i<yData.length; i++)
        {
            yEntrys.add(new PieEntry(yData[i], i));
        }

        for(int i=0; i<xData.length; i++)
        {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys,"Employee Sales");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }


    public class GetData extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String date[] = dateView.getText().toString().split("/");
                String dateStr = date[2] + "-" + date[1] + "-" + date[0];
                SharedPreferences sharedPref = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
                String userid = sharedPref.getString("userid", null);
                String reportResult = RestClient.getReportData(dateStr, userid);
                JSONObject jsonobject = new JSONObject(reportResult);
                float totalCalorieConsumed = Float.parseFloat(jsonobject.getString("total calories consumed"));
                float totalCalorieBurned = Float.parseFloat(jsonobject.getString("total calories burned"));
                float remainingCalorie = Float.parseFloat(jsonobject.getString("remaining calorie"));
               //yData = new float[]{totalCalorieConsumed,totalCalorieBurned,remainingCalorie};

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


return true;


        }

        protected void onPostExecute(final Boolean success) {
            if(success)
            {
               // addDataSet();
            }
        }

    }
}
