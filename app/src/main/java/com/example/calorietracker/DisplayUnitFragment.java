package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DisplayUnitFragment extends Fragment implements View.OnClickListener {
    private View vDisplayUnit;
    private Button bDisplayUnits, bClearUnits;
    private TextView tvDisplayUnits;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set Variables and listeners
        vDisplayUnit = inflater.inflate(R.layout.fragment_display_unit, container, false);
        bDisplayUnits = (Button) vDisplayUnit.findViewById(R.id.b_display_units);
        tvDisplayUnits = (TextView) vDisplayUnit.findViewById(R.id.tv_display_units);
        bDisplayUnits.setOnClickListener(this);
        return vDisplayUnit; }

    @Override
    public void onClick(View v) {
// Get myUnits file
        SharedPreferences spMyUnits = getActivity().getSharedPreferences("myUnits", Context.MODE_PRIVATE);
        String units= spMyUnits.getString("message",null);
        tvDisplayUnits.setText(units);
    } }