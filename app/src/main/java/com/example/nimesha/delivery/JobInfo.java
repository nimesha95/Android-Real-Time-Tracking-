package com.example.nimesha.delivery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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

    JobClass selectedJob;

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
    }
}
