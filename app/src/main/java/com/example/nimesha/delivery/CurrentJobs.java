package com.example.nimesha.delivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class CurrentJobs extends AppCompatActivity {

    private static final String TAG = "Database1";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("curjobs");
    TextView text1;
    RadioGroup radiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_jobs_new);

        radiogroup = (RadioGroup) findViewById(R.id.radioGrp);
        // text1 = (TextView) findViewById(R.id.textView1);
        Button naviagteBtn = (Button) findViewById(R.id.naviBtn);
        //Button signoutbtn = (Button) findViewById(R.id.signoutbtn);


        radiogroup.clearCheck();    //this clears the radiogroup everytime datachanges to that it don't duplicate
        radiogroup.removeAllViews();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                long x = dataSnapshot.getChildrenCount();
                jobList.clear();    //this clears the list.. so it's repopuated again when data changes
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

                    Log.d(TAG, key + " " + lat + " " + longi);

                    JobClass job = new JobClass(key, lat, longi, custname, address, itemCatagories, contactNo, timestamp, orderID);

                    JobListClass.jobList.add(job);   //adding job object to a list
/*
                    RadioButton radioButton = new RadioButton(CurrentJobs.this);
                    radioButton.setLayoutParams
                            (new RadioGroup.LayoutParams
                                    (RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));
                    radioButton.setText("Address: " + address);
                    Log.d("count", "" + jobList.get(count));

                    radioButton.setId(count);

                    //add it to the group.
                    radiogroup.addView(radioButton, count);
                    count += 1;
*/
                }

                Collections.sort(jobList, new Comparator<JobClass>() {
                    public int compare(JobClass obj1, JobClass obj2) {
                        // ## Ascending order
                        //return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
                        return Long.valueOf(obj1.getTimestamp()).compareTo(obj2.getTimestamp()); // To compare integer values

                    }
                });


                Log.d("arrSize", "" + jobList.size());
                for (int i = 0; i < jobList.size(); i++) {
                    RadioButton radioButton = new RadioButton(CurrentJobs.this);
                    radioButton.setLayoutParams
                            (new RadioGroup.LayoutParams
                                    (RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));

                    JobClass jb = jobList.get(i);

                    radioButton.setText("Address: " + jb.getAddress());


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


        naviagteBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        int selectedId = radiogroup.getCheckedRadioButtonId();
/*
                        JobClass selectedJob = jobList.get(selectedId);
                        Double lati = selectedJob.getLat();
                        Double longi = selectedJob.getLongi();
                        String selected = selectedJob.getKey();
                        Log.d("jobX", "" + selected + " " + lati + " " + longi);
*/
                        Intent intent = new Intent(CurrentJobs.this, JobInfo.class);
                        intent.putExtra("selectedJob", selectedId);
                        startActivity(intent);
                        //clickaway(lati,longi);
                    }
                }
        );

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void clickaway(Double lati, Double longi) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + lati + "," + longi));
        startActivity(intent);
        Log.d(TAG, lati + " " + longi);
    }

}
