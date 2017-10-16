package com.example.nimesha.delivery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class JobInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);

        int s = getIntent().getIntExtra("selectedJob", 0);
        Log.d("JobInfo", JobListClass.jobList.get(s).getAddress());
    }
}
