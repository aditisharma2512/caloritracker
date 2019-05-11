package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterUnitFragment extends Fragment implements View.OnClickListener {
    private View vEnterUnit;
    private EditText etUnitCode, etUnitName;
    private TextView tvFeedback;
    private Button bSubmit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set Variables and listener
        vEnterUnit = inflater.inflate(R.layout.fragment_enter_unit, container, false);
        etUnitCode = (EditText) vEnterUnit.findViewById(R.id.et_unit_code);
        etUnitName = (EditText) vEnterUnit.findViewById(R.id.et_unit_name);
        tvFeedback = (TextView) vEnterUnit.findViewById(R.id.tv_feedback);
        bSubmit = (Button) vEnterUnit.findViewById(R.id.b_submit);
        bSubmit.setOnClickListener(this);
        return vEnterUnit;
    }
    /**
     * When the submit button is clicked
     */
    @Override
    public void onClick(View v) { // Gather user input
        String unitCode = etUnitCode.getText().toString();
        String unitName = etUnitName.getText().toString(); // Validate user input
        if (unitCode.isEmpty())

        {
            etUnitCode.setError("Unit Code is required!");
            return;
        }
        if (unitName.isEmpty())
        {
            etUnitName.setError("Unit Name is required!");
            return;
        }
        // Add created unit to file
        SharedPreferences spMyUnits = getActivity().getSharedPreferences("myUnits", Context.MODE_PRIVATE);
        String myUnit="unitCode: " + unitCode + " " + "unitName: " + unitName;
        SharedPreferences.Editor eMyUnits = spMyUnits.edit();
        eMyUnits.putString("message", myUnit);
        eMyUnits.apply();
// Feedback
        String feedback = unitCode + " " + unitName + " has been recorded."; tvFeedback.setText(feedback);
    }
}