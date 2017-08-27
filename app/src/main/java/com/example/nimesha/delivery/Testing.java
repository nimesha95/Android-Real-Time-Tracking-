package com.example.nimesha.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import static com.example.nimesha.delivery.Curjob_fragment.myRef;

public class Testing extends AppCompatActivity {

    public static String s;
    Double lat;
    Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        s = getIntent().getStringExtra("selectedJob");


        startService(new Intent(this, LocationService.class));

        Log.d("jobX", "selected job: " + s + " curLat" + lat + " curLon" + lon);
        myRef.child(s).child("curLat").setValue(lat);
        myRef.child(s).child("curLong").setValue(lon);


    }


}
