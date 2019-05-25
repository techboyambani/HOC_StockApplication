package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hoc.stockapp.Model.UserInformation;

public class SignUp extends AppCompatActivity {

    private EditText tEmail, tOrganization, tPassword;
    private Button bSubmit;
    private String userID;
    private TextView tLogIn;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseDatabase mDatabaseReference;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tEmail = (EditText) findViewById(R.id.phone);
        tOrganization = (EditText) findViewById(R.id.organization);
        tPassword = (EditText) findViewById(R.id.password);
        bSubmit = (Button) findViewById(R.id.submit);
        tLogIn = (TextView) findViewById(R.id.login);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = tEmail.getText().toString().trim();
                final String organization = tOrganization.getText().toString().trim();
                final String password = tPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(organization)) {
                    Toast.makeText(getApplicationContext(), "Enter organization", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Toast.makeText(SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mFirebaseDatabase = FirebaseDatabase.getInstance();
                            mDatabaseReference = mFirebaseDatabase.getInstance();
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();

                            UserInformation userInformation = new UserInformation(email, organization, password);

                            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = mFirebaseDatabase.getReference();
                            myRef.child(userID).child("UserInformation").setValue(userInformation);

                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });

        tLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });
    }
}
