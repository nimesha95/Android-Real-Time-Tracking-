package com.example.nimesha.delivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static java.sql.Types.TIMESTAMP;

public class JobInfo extends AppCompatActivity {

    TextView orderid;
    TextView customername;
    TextView address;
    TextView mobile;
    TextView added;
    TextView remarkes;

    Button getNavi;

    JobClass selectedJob;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("curjobs");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);

        orderid = (TextView) findViewById(R.id.orderid);
        customername = (TextView) findViewById(R.id.customername);
        address = (TextView) findViewById(R.id.address);
        mobile = (TextView) findViewById(R.id.mobile);
        added = (TextView) findViewById(R.id.added);
        remarkes = (TextView) findViewById(R.id.remarkes);
        getNavi = (Button) findViewById(R.id.getNavi);

        int s = getIntent().getIntExtra("selectedJob", 0);

        selectedJob = JobListClass.jobList.get(s);

        orderid.setText(selectedJob.getOrderID());
        customername.setText(selectedJob.getCustname());
        address.setText(selectedJob.getAddress());
        mobile.setText("" + selectedJob.getContactNo());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(selectedJob.getTimestamp()));
        added.setText(dateString);

        remarkes.setText(selectedJob.getItemCatagories());

        getNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartService();
                OpenMaps(selectedJob.getLat(), selectedJob.getLongi());
                //Intent intent = new Intent(JobInfo.this, Testing.class);
                //intent.putExtra("selectedJob", selectedJob.getKey());
                //startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        newMessage messageReceiver = new newMessage();
        registerReceiver(messageReceiver, new IntentFilter("loc"));     //keep listning when app is paused
    }

    public class newMessage extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extra = intent.getExtras();
            Double lati = extra.getDouble("lat");
            Double longi = extra.getDouble("lon");
            Log.d("GPSloc", "Got from Intent: " + lati + " " + longi);

            myRef.child(selectedJob.getKey()).child("curLat").setValue(lati);
            myRef.child(selectedJob.getKey()).child("curLong").setValue(longi);
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

    public void OpenMaps(Double lati, Double longi) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + lati + "," + longi));
        startActivity(intent);
    }
}
