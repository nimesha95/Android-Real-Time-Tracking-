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

    public String s;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("curjobs");

    Button stopBtn;
    Button chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chk = (Button) findViewById(R.id.stat_btn);
        stopBtn = (Button) findViewById(R.id.stop_btn);

        chk.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        StartService();
                    }
                }
        );

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopService();
            }
        });

        s = getIntent().getStringExtra("selectedJob");

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

    public void StartService() {
        startService(new Intent(this, LocationService.class));
        newMessage messageReceiver = new newMessage();
        registerReceiver(messageReceiver, new IntentFilter("loc"));

    }

    public void StopService() {
        stopService(new Intent(this, LocationService.class));
        // Log.d("GPSloc","lat: "+LocationService.lat+" long: "+LocationService.lon);
    }

}
