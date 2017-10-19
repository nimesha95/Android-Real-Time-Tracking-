package com.example.nimesha.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "signin";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText pass = (EditText) findViewById(R.id.password);


        Button signinBtn = (Button) findViewById(R.id.signinbtn);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //goes to second activity if user is already logged in
                    // Intent intent = new Intent(MainActivity.this, TextRating.class);
                    Intent intent = new Intent(MainActivity.this, decision_point.class);
                    startActivity(intent);

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        signinBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if (CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
                        {
                            Log.d("internet", "Net available");
                            signin(username.getText().toString(), pass.getText().toString());
                        } else {

                            final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(MainActivity.this);

                            dialogBuilder
                                    .withTitle("No Internet")                                  //.withTitle(null)  no title
                                    .withTitleColor("#FFFFFF")                                  //def
                                    .withDividerColor("#11000000")                              //def
                                    .withMessage("Please Connect to the Internet")                     //.withMessage(null)  no Msg
                                    .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                                    .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
                                    .withButton1Text("Ok")
                                    .isCancelableOnTouchOutside(true)//def gone
                                    .setButton1Click(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    public void signin(String email, String password) {
        if (email.length() == 0 || password.length() == 0) {
            Log.w(TAG, "signInWithEmail:failure No inputs");
            Toast.makeText(MainActivity.this, "Enter valid email/password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String newEmail = email + "@pclife.com";
        final String emailx = email;    //to give access to the shared pref
        mAuth.signInWithEmailAndPassword(newEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(MainActivity.this, decision_point.class);
                            startActivity(intent);

                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getEmail());


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {

                        }
                    }
                });
    }
}
