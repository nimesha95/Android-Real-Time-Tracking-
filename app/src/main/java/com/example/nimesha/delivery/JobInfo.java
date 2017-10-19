package com.example.nimesha.delivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static com.example.nimesha.delivery.R.id.map;
import static java.sql.Types.TIMESTAMP;

public class JobInfo extends AppCompatActivity implements OnMapReadyCallback {

    TextView orderid;
    TextView customername;
    TextView address;
    TextView mobile;
    TextView added;
    TextView remarkes;

    Button getNavi;

    ScrollView mainScrollView;

    SharedPreferences prefs;

    JobClass selectedJob;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("curjobs");

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng pos = new LatLng(selectedJob.getLat(), selectedJob.getLongi());
        googleMap.addMarker(new MarkerOptions().position(pos)
                .title("Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);

        mainScrollView = (ScrollView) findViewById(R.id.scrollviewInfo);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);



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

        prefs = getSharedPreferences("delivery", MODE_PRIVATE);
        String status = prefs.getString("curJob", "no");
        if (status.equals("yes")) {
            NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(JobInfo.this);

            dialogBuilder
                    .withTitle("current JOB")                                  //.withTitle(null)  no title
                    .withTitleColor("#FFFFFF")                                  //def
                    .withDividerColor("#11000000")                              //def
                    .withMessage("Is the delivery complete?")                     //.withMessage(null)  no Msg
                    .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                    .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
                    .isCancelableOnTouchOutside(false)
                    .withButton1Text("YES")                                      //def gone
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JobCompletion();
                        }
                    })
                    .show();
        }

        remarkes.setText(selectedJob.getItemCatagories());

        getNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartService();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("curJob", "yes");
                editor.putString("jobKey", selectedJob.getKey());
                editor.commit();

                OpenMaps(selectedJob.getLat(), selectedJob.getLongi());
                //Intent intent = new Intent(JobInfo.this, Testing.class);
                //intent.putExtra("selectedJob", selectedJob.getKey());
                //startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        prefs = getSharedPreferences("delivery", MODE_PRIVATE);
        String status = prefs.getString("curJob", "no");
        if (status.equals("yes")) {
            NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(JobInfo.this);

            dialogBuilder
                    .withTitle("current JOB")                                  //.withTitle(null)  no title
                    .withTitleColor("#FFFFFF")                                  //def
                    .withDividerColor("#11000000")                              //def
                    .withMessage("Is this delivery complete?")                     //.withMessage(null)  no Msg
                    .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                    .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
                    .isCancelableOnTouchOutside(false)
                    .withButton1Text("YES")                                      //def gone
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JobCompletion();
                        }
                    })
                    .show();
        }

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

    public void JobCompletion() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("curJob", "no");
        editor.commit();

        myRef.child(prefs.getString("jobKey", "null")).child("completed").setValue("yes");      //update firebase database as completed

        StopService();      //stops the location background service

        Intent intent = new Intent(JobInfo.this, CurrentJobs.class);
        startActivity(intent);
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
