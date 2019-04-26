package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoc.stockapp.Model.DesignInformation;

import java.util.ArrayList;


public class AddItem extends AppCompatActivity {
    Spinner sp1,sp2;
    Button addItem, backHome;
    private EditText item_name,quantity;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef, db;
    String selectedItemText1, selectedItemText2;

    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_add_item);

        sp1 = (Spinner)findViewById(R.id.sp1);
        sp2 = (Spinner)findViewById(R.id.sp2);
        item_name = findViewById(R.id.item_name);
        quantity = findViewById(R.id.quantity);
        addItem = (Button)findViewById(R.id.submit);
        backHome = (Button)findViewById(R.id.back_home);

        db=FirebaseDatabase.getInstance().getReference().child("companies");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> companies = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String company = (String) ds.getValue();
                    companies.add(company);
                }
                ArrayAdapter<String> comp = new ArrayAdapter<String>(AddItem.this,android.R.layout.simple_spinner_dropdown_item,companies);
                comp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp1.setAdapter(comp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText1 = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        db=FirebaseDatabase.getInstance().getReference().child("size");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> size = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String sizes = (String) ds.getValue();
                    size.add(sizes);
                }
                ArrayAdapter<String> s = new ArrayAdapter<String>(AddItem.this,android.R.layout.simple_spinner_dropdown_item,size);
                s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp2.setAdapter(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText2 = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = item_name.getText().toString().trim();
                final String quan = quantity.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter Design Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(quan)) {
                    Toast.makeText(getApplicationContext(), "Enter Quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(selectedItemText1)) {
                    Toast.makeText(getApplicationContext(), "Please select company", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(selectedItemText2)) {
                    Toast.makeText(getApplicationContext(), "Please select size", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();
                myRef.child("designs").child(name).setValue(new DesignInformation(name, quan, selectedItemText1, selectedItemText2));

                Intent i = new Intent(AddItem.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddItem.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
