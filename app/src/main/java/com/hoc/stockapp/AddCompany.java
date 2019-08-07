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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddCompany extends AppCompatActivity {
    private Button bSubmit;
    private EditText tAddCompany;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef, db, databaseReference;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private ImageButton bBack;

    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_add_company);

        tAddCompany = (EditText) findViewById(R.id.company);
        bSubmit = (Button) findViewById(R.id.submit);
        bBack = (ImageButton) findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference().child("companies");

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = tAddCompany.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter Design Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();

                Query query = myRef.child(mFirebaseUser.getUid()).child("StockInformation").child("Company").child(name).child("name");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot issue : dataSnapshot.getChildren()){
                                Toast.makeText(getApplicationContext(), "Company already exists.", Toast.LENGTH_SHORT).show();
                                Log.d("TAG","Company already exists.");
                            }
                        }
                        else{
                            myRef.child(mFirebaseUser.getUid()).child("StockInformation").child("Company").child(name).child("name").setValue(name);
                            Intent i = new Intent(AddCompany.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
