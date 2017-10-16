package com.example.nimesha.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class decision_point extends AppCompatActivity {

    Button signoutBtn;
    Button CurJobBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision_point);

        signoutBtn = (Button) findViewById(R.id.button2);
        CurJobBtn = (Button) findViewById(R.id.CurJobBtn);

        CurJobBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(decision_point.this, CurrentJobs.class);
                        startActivity(intent);

                    }
                }
        );


        signoutBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(decision_point.this, MainActivity.class);
                        startActivity(intent);

                    }
                }
        );
    }
}
