package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditStock_Design extends AppCompatActivity {

    private EditText textQuantity, textDescription, textBatchA, textBatchB, textBatchC, textBatchD;
    private TextView textCompany, textSize;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mDatabaseReference2;
    private ImageButton bBack;
    private ProgressBar pProgressBar;
    private Button bSubmit;

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_edit_stock_design);

        Intent viewDesignIntent = getIntent();

        final String name = viewDesignIntent.getStringExtra(ViewStock.EXTRA_NAME);

        textQuantity = (EditText) findViewById(R.id.quantity);
        textCompany = (TextView) findViewById(R.id.textCompany);
        textSize = (TextView) findViewById(R.id.textSize);
        textDescription = (EditText) findViewById(R.id.description);
        textBatchA = (EditText) findViewById(R.id.batch_a);
        textBatchB = (EditText) findViewById(R.id.batch_b);
        textBatchC = (EditText) findViewById(R.id.batch_c);
        textBatchD = (EditText) findViewById(R.id.batch_d);
        pProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        bBack = (ImageButton) findViewById(R.id.back);
        bSubmit = (Button) findViewById(R.id.submit);

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

                textQuantity.setText(quantity);
                textDescription.setText(description);
                textBatchA.setText(batch_a);
                textBatchB.setText(batch_b);
                textBatchC.setText(batch_c);
                textBatchD.setText(batch_d);
                textCompany.setText(company);
                textSize.setText(size);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String quantity = textQuantity.getText().toString().trim();
                final String description = textDescription.getText().toString().trim();
                final String batch_a = textBatchA.getText().toString().trim();
                final String batch_b = textBatchB.getText().toString().trim();
                final String batch_c = textBatchC.getText().toString().trim();
                final String batch_d = textBatchD.getText().toString().trim();

                if (TextUtils.isEmpty(quantity)) {
                    Toast.makeText(getApplicationContext(), "Enter correct quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    textDescription.setText("--");
                    return;
                }
                if (TextUtils.isEmpty(batch_a)) {
                    textBatchA.setText(quantity);
                    return;
                }
                if (TextUtils.isEmpty(batch_b)) {
                    textBatchD.setText("0");
                    return;
                }
                if (TextUtils.isEmpty(batch_c)) {
                    textBatchD.setText("0");
                    return;
                }
                if (TextUtils.isEmpty(batch_d)) {
                    textBatchD.setText("0");
                    return;
                }

                int totalQuan = Integer.parseInt(quantity);
                int batchA = Integer.parseInt(batch_a);
                int batchB = Integer.parseInt(batch_b);
                int batchC = Integer.parseInt(batch_c);
                int batchD = Integer.parseInt(batch_d);

                if((batchA + batchB + batchC + batchD) != totalQuan){
                    Toast.makeText(getApplicationContext(), "Enter correct batch quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference2 = mFirebaseDatabase.getReference().child(mFirebaseUser.getUid()).child("StockInformation").child("Design");
                Query query = mDatabaseReference;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mDatabaseReference2.child(name).child("quantity").setValue(quantity);
                        mDatabaseReference2.child(name).child("description").setValue(description);
                        mDatabaseReference2.child(name).child("batch_a").setValue(batch_a);
                        mDatabaseReference2.child(name).child("batch_b").setValue(batch_b);
                        mDatabaseReference2.child(name).child("batch_c").setValue(batch_c);
                        mDatabaseReference2.child(name).child("batch_d").setValue(batch_d);
                        Intent i = new Intent(EditStock_Design.this, MainActivity.class);
                        startActivity(i);
                        finish();
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
