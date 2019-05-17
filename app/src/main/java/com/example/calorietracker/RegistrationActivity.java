package com.example.calorietracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {
    private EditText vFirstname, vSurname, vEmail, vHeight,vWeight,vGender,vAddress, vPostcode, vSteps, vUsername, vPassword, vDOB;
    private Calendar myCalendar = Calendar.getInstance();
    private String firstname,surname,email,dob,address,postcode,height,weight,steps,username,password,gender,hPassword, levelofactivity;
    private Integer uId;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        vDOB = (EditText) findViewById(R.id.dob);

        vFirstname = (EditText) findViewById(R.id.firstname);
        vSurname = (EditText) findViewById(R.id.surname);
        vEmail = (EditText) findViewById(R.id.email);
        vAddress = (EditText) findViewById(R.id.address);
        vPostcode = (EditText) findViewById(R.id.postcode);
        vHeight = (EditText) findViewById(R.id.height);
        vWeight = (EditText) findViewById(R.id.weight);
        vSteps = (EditText) findViewById(R.id.stepspermile);
        vUsername = (EditText) findViewById(R.id.username);
        vPassword = (EditText) findViewById(R.id.password);

        //final String username = vUsername.getText().toString();

        final DatePickerDialog picker = new DatePickerDialog(RegistrationActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        vDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show();
            }
        });





        spinner = (Spinner) findViewById(R.id.levelofactivity_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.levelofactivity_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        Button mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender = "M";
                    break;
            case R.id.radio_female:
                if (checked)
                    gender = "F";
                    break;
        }
    }

    private void attemptSignUp()
    {
        firstname = vFirstname.getText().toString();
        surname = vSurname.getText().toString();
        email = vEmail.getText().toString();
        dob = vDOB.getText().toString();
        address = vAddress.getText().toString();
        postcode = vPostcode.getText().toString();
        height = vHeight.getText().toString();
        weight = vWeight.getText().toString();
        steps = vSteps.getText().toString();
        username = vUsername.getText().toString();
        password = vPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;
        vFirstname.setError(null);
        vSurname.setError(null);
        vEmail.setError(null);
        vDOB.setError(null);
        vWeight.setError(null);
        vHeight.setError(null);
        vAddress.setError(null);
        vPostcode.setError(null);
        vSteps.setError(null);
        vUsername.setError(null);
        vPassword.setError(null);
        // String password = mPasswordView.getText().toString();

        if (firstname.isEmpty()) {
            vFirstname.setError(getString(R.string.error_field_required));
            focusView = vFirstname;
            cancel = true;
        }
        else if (surname.isEmpty()) {
            vSurname.setError(getString(R.string.error_field_required));
            focusView = vSurname;
            cancel = true;
        }
        else if (email.isEmpty()) {
            vEmail.setError(getString(R.string.error_field_required));
            focusView = vEmail;
            cancel = true;
        }
        else if (dob.isEmpty()) {
            vDOB.setError(getString(R.string.error_field_required));
            focusView = vDOB;
            cancel = true;
        }
        else if (height.isEmpty()) {
            vHeight.setError(getString(R.string.error_field_required));
            focusView = vHeight;
            cancel = true;
        }
        else if (weight.isEmpty()) {
            vWeight.setError(getString(R.string.error_field_required));
            focusView = vWeight;
            cancel = true;
        }
        else if (address.isEmpty()) {
            vAddress.setError(getString(R.string.error_field_required));
            focusView = vAddress;
            cancel = true;
        }
        else if (postcode.isEmpty()) {
            vPostcode.setError(getString(R.string.error_field_required));
            focusView = vPostcode;
            cancel = true;
        }
        else if (steps.isEmpty()) {
            vSteps.setError(getString(R.string.error_field_required));
            focusView = vSteps;
            cancel = true;
        }
        else if (username.isEmpty()) {
            vUsername.setError(getString(R.string.error_field_required));
            focusView = vUsername;
            cancel = true;
        }
        else if (password.isEmpty()) {
            vPassword.setError(getString(R.string.error_field_required));
            focusView = vPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            boolean result = RestClient.verifyUsername(username);
            if(result) {
                vUsername.setError(getString(R.string.error_field_username));
                focusView = vUsername;
                focusView.requestFocus();
            }
            else {

                hPassword = hashingMD5.getMd5Hash(password);
                levelofactivity = spinner.getSelectedItem().toString();

                UserIdAsyncTask obj = new UserIdAsyncTask();
                obj.execute();

                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                Intent intent = new Intent(RegistrationActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private class UserIdAsyncTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            return RestClient.getUserId();
        }
        @Override
        protected void onPostExecute(Integer userid) {
            uId = userid;
            UserPostAsyncTask uPost =new UserPostAsyncTask();
            uPost.execute();
            //Toast.makeText(RegistrationActivity.this,response, Toast.LENGTH_LONG).show();
            //resultTextView.setText(response);
        } }

    private class UserPostAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sourceFormat.parse(dob);
                Users user = new Users(uId, firstname, surname, email, date,
                        Double.parseDouble(height), Double.parseDouble(weight),
                        gender.charAt(0),address,Integer.parseInt(postcode),Integer.parseInt(levelofactivity),Integer.parseInt(steps));
                RestClient.createUser(user);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            //return "User was added";
            return  null;
        }
        @Override
        protected void onPostExecute(Void param) {
            //Toast.makeText(RegistrationActivity.this,response, Toast.LENGTH_LONG).show();
            //resultTextView.setText(response);
            CredentialPostAsyncTask cPost = new CredentialPostAsyncTask();
            cPost.execute();
        }
    }

    private class CredentialPostAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

                //DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date today = new Date();
                DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sourceFormat.parse(dob);
                Users user = new Users(uId, firstname, surname, email, date,
                        Double.parseDouble(height), Double.parseDouble(weight),
                        gender.charAt(0), address, Integer.parseInt(postcode), Integer.parseInt(levelofactivity), Integer.parseInt(steps));
                Credential credential = new Credential(username, hPassword, today, user);
                RestClient.createCredential(credential);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return "User was added";
        }
        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(RegistrationActivity.this,response, Toast.LENGTH_LONG).show();
            //resultTextView.setText(response);
        }
    }


}

