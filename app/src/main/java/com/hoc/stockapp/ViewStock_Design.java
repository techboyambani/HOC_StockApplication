package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.hoc.stockapp.ViewStock.EXTRA_NAME;

public class ViewStock_Design extends AppCompatActivity {

    private TextView tName, tQuantity, tCompany, tSize, tDescription, tBatchA, tBatchB, tBatchC, tBatchD;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ImageButton bBack;
    private ProgressBar pProgressBar;

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_view_stock_design);

        Intent viewDesignIntent = getIntent();

        final String name = viewDesignIntent.getStringExtra(EXTRA_NAME);

        tName = (TextView) findViewById(R.id.design_name);
        tQuantity = (TextView) findViewById(R.id.quantity);
        tCompany = (TextView) findViewById(R.id.company);
        tSize = (TextView) findViewById(R.id.size);
        tDescription = (TextView) findViewById(R.id.description);
        tBatchA = (TextView) findViewById(R.id.batch_a);
        tBatchB = (TextView) findViewById(R.id.batch_b);
        tBatchC = (TextView) findViewById(R.id.batch_c);
        tBatchD = (TextView) findViewById(R.id.batch_d);
        pProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        bBack = (ImageButton) findViewById(R.id.back);

        tName.setText(name);

        pProgressBar.setVisibility(View.VISIBLE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseAuth.getUid()).child("StockInformation").child("Design").child(name);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pProgressBar.setVisibility(View.GONE);
                String quantity = (String) dataSnapshot.child("quantity").getValue();
                String description = (String) dataSnapshot.child("description").getValue();
                String company = (String) dataSnapshot.child("company").getValue();
                String size = (String) dataSnapshot.child("size").getValue();
                String batch_a = (String) dataSnapshot.child("batch_a").getValue();
                String batch_b = (String) dataSnapshot.child("batch_b").getValue();
                String batch_c = (String) dataSnapshot.child("batch_c").getValue();
                String batch_d = (String) dataSnapshot.child("batch_d").getValue();

                tQuantity.setText(quantity);
                tDescription.setText(description);
                tCompany.setText(company);
                tSize.setText(size);
                tBatchA.setText(batch_a);
                tBatchB.setText(batch_b);
                tBatchC.setText(batch_c);
                tBatchD.setText(batch_d);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
