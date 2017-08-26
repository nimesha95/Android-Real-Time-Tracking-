package com.example.nimesha.delivery;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Curjob_fragment extends Fragment {

    private static final String TAG = "Database1";

    TextView text1;
    Double lati;
    Double longi;

    RadioGroup radiogroup;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("curjobs");

    public Curjob_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curjob_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        radiogroup = (RadioGroup) view.findViewById(R.id.radioGrp);
        text1 = (TextView) view.findViewById(R.id.textView1);
        Button naviagteBtn = (Button) view.findViewById(R.id.naviBtn);
        Button signoutbtn = (Button) view.findViewById(R.id.signoutbtn);

        naviagteBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clickaway();
                    }
                }
        );

        signoutbtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Log.d(TAG, "User signed out");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }

    @Override
    public void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                radiogroup.clearCheck();    //this clears the radiogroup everytime datachanges to that it don't duplicate
                radiogroup.removeAllViews();

                final List<String> jobs = new ArrayList<String>(); //get all the children of the curjob table
                int count = 0;
                for (DataSnapshot jobSnap : dataSnapshot.getChildren()) {
                    String key = jobSnap.getKey();
                    Double lat = jobSnap.child("lat").getValue(Double.TYPE);
                    Double longi = jobSnap.child("long").getValue(Double.TYPE);
                    Log.d(TAG, key + " " + lat + " " + longi);

                    RadioButton radioButton = new RadioButton(getActivity());
                    radioButton.setLayoutParams
                            (new RadioGroup.LayoutParams
                                    (RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));
                    radioButton.setText("Radio Button #" + lat);
                    radioButton.setId(count);

                    //add it to the group.
                    radiogroup.addView(radioButton, count);
                    count += 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void clickaway() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + lati + "," + longi));
        startActivity(intent);
        Log.d(TAG, lati + " " + longi);
    }

}

