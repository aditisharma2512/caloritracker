package com.example.calorietracker;

import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class StepsFragment extends Fragment {

    View vSteps;
    StepsDatabase db = null;

    //for the insert operation
    private EditText steps,time;
    private Button addBtn,updateBtn,deleteBtn,displayAllBtn;
    private SimpleAdapter myListAdapter;
    private TextView tvTime,tvDisplay;

    //List for showing all records
    List<HashMap<String, String>> ListArray;
    private ListView listView;
    String[] colHEAD = new String[]{"TIME", "STEPS"};
    int[] dataCell = new int[]{R.id.tv_time, R.id.tv_steps};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment

        vSteps = inflater.inflate(R.layout.fragment_steps, container, false);

        steps = (EditText) vSteps.findViewById(R.id.steps);
        time = (EditText) vSteps.findViewById(R.id.time);
        addBtn = (Button) vSteps.findViewById(R.id.add);
        deleteBtn = (Button) vSteps.findViewById(R.id.delete);
        updateBtn = (Button) vSteps.findViewById(R.id.update);
        tvTime = (TextView) vSteps.findViewById(R.id.time2);
        tvDisplay = (TextView) vSteps.findViewById(R.id.displayall);
        displayAllBtn = (Button) vSteps.findViewById(R.id.display);


        Calendar calendar = Calendar.getInstance();
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(vSteps.getContext(),  new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String amPm;
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                time.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

            }
        }, currentHour, currentMinute, false);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                timePickerDialog.show();

            }
        });




        listView = (ListView) vSteps.findViewById(R.id.list_view);
        ListArray = new ArrayList<HashMap<String, String>>();
        /*HashMap<String, String> map = new HashMap<String, String>();
        map.put("STEPS","200");
        map.put("TIME","20:00");

        ListArray.add(map);

        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("STEPS","300");
        map2.put("TIME","15:00");

        ListArray.add(map2);

        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("STEPS","400");
        map3.put("TIME","12:00");

        ListArray.add(map3);
*/

        myListAdapter = new SimpleAdapter(vSteps.getContext(),ListArray,R.layout.list_view,colHEAD,dataCell);
        listView.setAdapter(myListAdapter);


        //this creates the database
        db = Room.databaseBuilder(vSteps.getContext(),StepsDatabase.class,"StepsDatabase").fallbackToDestructiveMigration().build();


        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
        //add to database
        addBtn.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View V){


                        String stepsStr = steps.getText().toString();
                        String timeStr = time.getText().toString();

                        InsertDatabase addDatabase = new InsertDatabase();
                        addDatabase.execute(stepsStr,timeStr);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("STEPS",stepsStr);
                        map.put("TIME",timeStr);
                        addMap(map);

                        time.setText("");
                        steps.setText("");

                        steps.clearFocus();
                    }
                });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute();
                updateBtn.setVisibility(Button.GONE);
                deleteBtn.setVisibility(Button.GONE);
                addBtn.setVisibility(Button.VISIBLE);
                tvTime.setVisibility(TextView.GONE);
                time.setVisibility(EditText.VISIBLE);
                steps.clearFocus();
            }
        });

        displayAllBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDatabase deleteDatabase = new DeleteDatabase();
                deleteDatabase.execute();
                updateBtn.setVisibility(Button.GONE);
                deleteBtn.setVisibility(Button.GONE);
                addBtn.setVisibility(Button.VISIBLE);
                tvTime.setVisibility(TextView.GONE);
                time.setVisibility(EditText.VISIBLE);

                steps.clearFocus();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int show = position;
                Object details = listView.getItemAtPosition(position);
                HashMap<String, String> temp = (HashMap<String, String>)details;
                String stepEt = temp.get("STEPS");
                String timeEt = temp.get("TIME");
                tvTime.setVisibility(TextView.VISIBLE);
                time.setVisibility(EditText.GONE);
                updateBtn.setVisibility(Button.VISIBLE);
                deleteBtn.setVisibility(Button.VISIBLE);
                addBtn.setVisibility(Button.GONE);
                steps.setText(stepEt);
                tvTime.setText(timeEt);
                time.setText(timeEt);

            }
        });


        return vSteps;

    }

        private class InsertDatabase extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... params) {

                if (!(params[0].isEmpty() && params[1].isEmpty())) {
                    Steps steps = new Steps(Integer.parseInt(params[0]), params[1]);
                    db.stepsDao().insert(steps);
                    return true;
                }
                else
                    return false;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (!(success)){
                    Toast.makeText(vSteps.getContext(),"Empty or invalid values",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(vSteps.getContext(),"Record Added!",Toast.LENGTH_SHORT).show();

                }
            }
        }

        private class ReadDatabase extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                List<Steps> steps = db.stepsDao().getAll();
                ListArray.clear();
                if (steps.size() >= 1) {

                    for (Steps each : steps) {
                        Integer stepsStr = each.getSteps();
                        String timeStr = each.getTime();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("STEPS", stepsStr.toString());
                        map.put("TIME", timeStr);
                        ListArray.add(map);
                    }

                }
            return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                myListAdapter = new SimpleAdapter(vSteps.getContext(),ListArray,R.layout.list_view,colHEAD,dataCell);
                listView.setAdapter(myListAdapter);


            }
        }

        private class DeleteDatabase extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                Steps item = null;
                String stepsCurrent = steps.getText().toString();
                String timeCurrent = tvTime.getText().toString();
                item = new Steps(Integer.parseInt(stepsCurrent), timeCurrent);
                db.stepsDao().deleteUsers(item);
                return null;
            }

            protected void onPostExecute(Void param) {
                time.setText("");
                steps.setText("");

                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
            }
        }

        private class UpdateDatabase extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                Steps item = null;
                String stepsCurrent = steps.getText().toString();
                String timeCurrent = tvTime.getText().toString();
                item = db.stepsDao().findByTime(timeCurrent);
                item.setSteps(Integer.parseInt(stepsCurrent));
                item.setTime(timeCurrent);
                if (item != null) {

                    db.stepsDao().updateUsers(item);
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void param) {
                time.setText("");
                steps.setText("");
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();

            }
        }


        protected void addMap(HashMap map) {
            ListArray.add(map);
            myListAdapter = new SimpleAdapter(vSteps.getContext(), ListArray, R.layout.list_view, colHEAD, dataCell);
            //adapter.notifyDataSetChanged();
            listView.setAdapter(myListAdapter);

        }


}
