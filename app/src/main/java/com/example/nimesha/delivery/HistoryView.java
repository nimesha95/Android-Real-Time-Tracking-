package com.example.nimesha.delivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import static com.example.nimesha.delivery.R.id.select_dialog_listview;

public class HistoryView extends AppCompatActivity implements OnMapReadyCallback {

    TextView orderid;
    TextView customername;
    TextView address;
    TextView mobile;
    TextView added;
    TextView remarkes;

    Button backBtn;

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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view);

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
        backBtn = (Button) findViewById(R.id.backBtn);

        int s = getIntent().getIntExtra("selectedJob", 0);

        selectedJob = JobListHistoryClass.jobHistory.get(s);

        orderid.setText(selectedJob.getOrderID());
        customername.setText(selectedJob.getCustname());
        address.setText(selectedJob.getAddress());
        mobile.setText("" + selectedJob.getContactNo());

        added.setText(selectedJob.getCompDate());

        remarkes.setText(selectedJob.getItemCatagories());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryView.this, JobHistory.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}