package com.example.nimesha.delivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Testing extends AppCompatActivity {

    public static String s;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("curjobs");

    public static void justFunc() {
        // Log.d("GPSloc","lat: "+LocationService.lat+" long: "+LocationService.lon);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button chk = (Button) findViewById(R.id.stat_btn);

        chk.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        justFunc();
                    }
                }
        );

        s = getIntent().getStringExtra("selectedJob");

        startService(new Intent(this, LocationService.class));

        newMessage messageReceiver = new newMessage();
        registerReceiver(messageReceiver, new IntentFilter("loc"));

        //Log.d("jobX", "selected job: " + s + " curLat" + LocationService.lat + " curLon" + LocationService.lon);
    }

    public class newMessage extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extra = intent.getExtras();
            Double lati = extra.getDouble("lat");
            Double longi = extra.getDouble("lon");
            Log.d("GPSloc", "Got from Intent: " + lati + " " + longi);

            myRef.child(s).child("curLat").setValue(lati);
            myRef.child(s).child("curLong").setValue(longi);
        }
    }
}
