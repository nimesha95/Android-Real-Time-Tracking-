package com.example.nimesha.delivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.nimesha.delivery.R.id.map;
import static com.example.nimesha.delivery.R.id.username;
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
    JSONObject finalAnswer;
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


                finalAnswer = new JSONObject();
                try {
                    //Log.d("stuffhappens",selectedJob.getOrderID());
                    finalAnswer.put("cur_job_id", selectedJob.getOrderID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //selectedJob.getOrderID()
                PostAnotation postAnotation = new PostAnotation();
                String newUrl = "http://pclife.azurewebsites.net/rest_api";
                postAnotation.setUrl(newUrl);
                postAnotation.execute();

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

        finalAnswer = new JSONObject();
        try {
            //Log.d("stuffhappens",selectedJob.getOrderID());
            finalAnswer.put("job_completed", selectedJob.getOrderID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //selectedJob.getOrderID()
        PostAnotation postAnotation = new PostAnotation();
        String newUrl = "http://pclife.azurewebsites.net/rest_api";
        postAnotation.setUrl(newUrl);
        postAnotation.execute();


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        Log.d("umbaa", formattedDate);

        myRef.child(prefs.getString("jobKey", "null")).child("completed").setValue("yes");      //update firebase database as completed
        myRef.child(prefs.getString("jobKey", "null")).child("comp_date").setValue(formattedDate);

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


    public class PostAnotation extends AsyncTask<Void, Void, String> {
        String url;
        String response = null;


        public String setUrl(String url) {
            try {
                this.url = url;
                return "successfully set";


            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... voids) {
            Request requestX = null;

            try {
                requestX = Bridge
                        .post(url)
                        .body(finalAnswer)
                        .request();
            } catch (BridgeException e) {
                e.printStackTrace();
            }

            Response responseX = requestX.response();
            if (responseX.isSuccess()) {
                // Request returned HTTP status 200-300
                responseX.asString();
                //Log.d("stuffhappens", responseX.asString());
            } else {
                // Request returned an HTTP error status
                // Log.d("stuffhappens", responseX.asString());

            }
            return responseX.asString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
            } catch (Exception e) {
                //Log.d("AnswerSent", "failed to sent");
            }

            //Log.d("SendingReport", result);

        }

    }

}
