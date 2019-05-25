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
import com.hoc.stockapp.Model.DesignInformation;

import java.util.ArrayList;

public class AddCompanyDesign extends AppCompatActivity {
    private ImageButton bBack;
    private Spinner spCompany, spSize;
    private EditText tName, tQuantity;
    private Button bSubmit;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mDatabaseReference2;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String stCompany, stSize;
    private ProgressBar pProgressBar;

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_add_company_design);

        bBack = (ImageButton) findViewById(R.id.back);
        pProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        spCompany = (Spinner) findViewById(R.id.company_spinner);
        spSize = (Spinner) findViewById(R.id.size_spinner);
        tName = (EditText) findViewById(R.id.design_name);
        tQuantity = (EditText) findViewById(R.id.quantity);
        bSubmit = (Button) findViewById(R.id.submit);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        pProgressBar.setVisibility(View.VISIBLE);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseUser.getUid()).child("StockInformation").child("Company");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pProgressBar.setVisibility(View.GONE);
                ArrayList<String> companies = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String company = ds.child("name").getValue(String.class);
                    companies.add(company);
                }
                ArrayAdapter<String> comp = new ArrayAdapter<String>(AddCompanyDesign.this,android.R.layout.simple_spinner_dropdown_item,companies);
                comp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCompany.setAdapter(comp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        spCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stCompany = (String) parent.getItemAtPosition(position);

                pProgressBar.setVisibility(View.VISIBLE);
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseUser.getUid()).child("StockInformation").child("Company").child(stCompany).child("sizes");
                mDatabaseReference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pProgressBar.setVisibility(View.GONE);
                        ArrayList<String> companies = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            String size= ds.child("dimensions").getValue(String.class);
                            companies.add(size);
                        }
                        ArrayAdapter<String> comp = new ArrayAdapter<String>(AddCompanyDesign.this,android.R.layout.simple_spinner_dropdown_item,companies);
                        comp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spSize.setAdapter(comp);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        stSize = (String) parent.getItemAtPosition(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = tName.getText().toString().trim();
                final String quantity = tQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter design name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(quantity)) {
                    Toast.makeText(getApplicationContext(), "Enter design quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(stCompany)) {
                    Toast.makeText(getApplicationContext(), "Please select company.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(stSize)) {
                    Toast.makeText(getApplicationContext(), "Please select size.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference().child(mFirebaseUser.getUid()).child("StockInformation").child("Company").child(stCompany).child("sizes").child(stSize).child("designs");
                mDatabaseReference2 = mFirebaseDatabase.getReference().child(mFirebaseUser.getUid()).child("StockInformation").child("Design");
                Query query = mDatabaseReference;
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
                            mDatabaseReference2.child(name).setValue(new DesignInformation(name, quantity, stCompany, stSize));
                            mDatabaseReference2.child(name).child("description").setValue("-");
                            mDatabaseReference2.child(name).child("batch_a").setValue("0");
                            mDatabaseReference2.child(name).child("batch_b").setValue("0");
                            mDatabaseReference2.child(name).child("batch_c").setValue("0");
                            mDatabaseReference2.child(name).child("batch_d").setValue("0");
                            Intent i = new Intent(AddCompanyDesign.this, MainActivity.class);
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


    }

}
