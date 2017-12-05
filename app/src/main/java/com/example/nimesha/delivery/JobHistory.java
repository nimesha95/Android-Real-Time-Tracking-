package com.example.nimesha.delivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;

import static com.example.nimesha.delivery.JobListClass.jobList;
import static com.example.nimesha.delivery.JobListHistoryClass.jobHistory;

public class JobHistory extends AppCompatActivity {

    private static final String TAG = "Database1";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("curjobs");
    RadioGroup radiogroup;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_history);

        radiogroup = (RadioGroup) findViewById(R.id.radioGrp);
        // text1 = (TextView) findViewById(R.id.textView1);
        Button SelectBtn = (Button) findViewById(R.id.SelectBtn);
        //Button signoutbtn = (Button) findViewById(R.id.signoutbtn);


        radiogroup.clearCheck();    //this clears the radiogroup everytime datachanges to that it don't duplicate
        radiogroup.removeAllViews();

        prefs = getSharedPreferences("delivery", MODE_PRIVATE);
        String status = prefs.getString("curJob", "no");
        Log.d("statusX", status);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                radiogroup.clearCheck();    //this clears the radiogroup everytime datachanges to that it don't duplicate
                radiogroup.removeAllViews();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                jobList.clear();    //this clears the list.. so it's repopuated again when data changes
                jobHistory.clear();

                for (DataSnapshot jobSnap : dataSnapshot.getChildren()) {
                    String key = jobSnap.getKey();
                    Double lat = jobSnap.child("lat").getValue(Double.TYPE);
                    Double longi = jobSnap.child("long").getValue(Double.TYPE);
                    String custname = (String) jobSnap.child("custName").getValue();
                    String address = (String) jobSnap.child("address").getValue();
                    String itemCatagories = (String) jobSnap.child("itemCatagory").getValue();
                    Long contactNo = (Long) jobSnap.child("contactNo").getValue();
                    Long timestamp = (Long) jobSnap.child("timestamp").getValue();
                    String orderID = (String) jobSnap.child("orderID").getValue();
                    String completed = (String) jobSnap.child("completed").getValue();
                    String compDate = (String) jobSnap.child("comp_date").getValue();
                    Log.d(TAG, key + " " + lat + " " + longi);

                    JobClass job = new JobClass(key, lat, longi, custname, address, itemCatagories, contactNo, timestamp, orderID, compDate);

                    if (completed.equals("yes")) {
                        jobHistory.add(job);
                    } else {
                        jobList.add(job);   //adding job object to a list
                    }

                }

                Collections.sort(jobHistory, new Comparator<JobClass>() {
                    public int compare(JobClass obj1, JobClass obj2) {
                        // ## Ascending order
                        //return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
                        return Long.valueOf(obj1.getTimestamp()).compareTo(obj2.getTimestamp()); // To compare integer values

                    }
                });


                Log.d("arrSize", "" + jobHistory.size());
                for (int i = 0; i < jobHistory.size(); i++) {
                    RadioButton radioButton = new RadioButton(JobHistory.this);
                    radioButton.setLayoutParams
                            (new RadioGroup.LayoutParams
                                    (RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));

                    JobClass jb = jobHistory.get(i);

                    radioButton.setText("Date: " + jb.getCompDate());


                    radioButton.setId(i);

                    //add it to the group.
                    radiogroup.addView(radioButton, i);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        SelectBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        int selectedId = radiogroup.getCheckedRadioButtonId();

                        Intent intent = new Intent(JobHistory.this, HistoryView.class);
                        intent.putExtra("selectedJob", selectedId);
                        startActivity(intent);
                    }
                }
        );
    }
}
