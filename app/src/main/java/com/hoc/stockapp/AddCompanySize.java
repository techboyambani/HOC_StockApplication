package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCompanySize extends AppCompatActivity {
    private Spinner sSpinner;
    private ProgressBar pProgressBar;
    private EditText tSize1, tSize2;
    private ImageButton bBack;
    private Button bSubmit;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String sCompany;

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_add_company_size);

        bBack = (ImageButton) findViewById(R.id.back);
        pProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        sSpinner = (Spinner) findViewById(R.id.spinner);
        tSize1 = (EditText) findViewById(R.id.size1);
        tSize2 = (EditText) findViewById(R.id.size2);
        bSubmit = (Button) findViewById(R.id.submit);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        pProgressBar.setVisibility(View.VISIBLE);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseUser.getUid()).child("StockInformation").child("Company");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pProgressBar.setVisibility(View.GONE);
                ArrayList<String> companies = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String areaName = ds.child("name").getValue(String.class);
                    companies.add(areaName);
                }
                ArrayAdapter<String> comp = new ArrayAdapter<String>(AddCompanySize.this,android.R.layout.simple_spinner_dropdown_item,companies);
                comp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sSpinner.setAdapter(comp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sCompany = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String size1 = tSize1.getText().toString().trim();
                final String size2 = tSize2.getText().toString().trim();
                final String size = size1 + " x " + size2;
                if (TextUtils.isEmpty(size1)) {
                    Toast.makeText(getApplicationContext(), "Enter horizontal length.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(size2)) {
                    Toast.makeText(getApplicationContext(), "Enter vertical length.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(sCompany)) {
                    Toast.makeText(getApplicationContext(), "Please select company", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference();

                Query query = mDatabaseReference.child(mFirebaseUser.getUid()).child("StockInformation").child("Company").child(sCompany).child("sizes").child(size).child("dimensions");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot issue : dataSnapshot.getChildren()){
                                Toast.makeText(getApplicationContext(), "Size already exists.", Toast.LENGTH_SHORT).show();
                                Log.d("TAG","Size already exists.");
                            }
                        }
                        else{
                            mDatabaseReference.child(mFirebaseUser.getUid()).child("StockInformation").child("Company").child(sCompany).child("sizes").child(size).child("dimensions").setValue(size);
                            Intent i = new Intent(AddCompanySize.this, MainActivity.class);
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
