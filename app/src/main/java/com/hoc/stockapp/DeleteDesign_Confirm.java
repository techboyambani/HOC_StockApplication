package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.hoc.stockapp.EditStock.EXTRA_COMPANY;
import static com.hoc.stockapp.EditStock.EXTRA_NAME;
import static com.hoc.stockapp.EditStock.EXTRA_QUANTIY;
import static com.hoc.stockapp.EditStock.EXTRA_SIZE;


public class DeleteDesign_Confirm extends AppCompatActivity {

    EditText newName;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    Button delete, back_home;

    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_delete_design_confirm);

        Intent editDesignIntent = getIntent();

        final String name = editDesignIntent.getStringExtra(EXTRA_NAME);
        String company = editDesignIntent.getStringExtra(EXTRA_COMPANY);
        String size = editDesignIntent.getStringExtra(EXTRA_SIZE);
        String quantity = editDesignIntent.getStringExtra(EXTRA_QUANTIY);

        final TextView designName = findViewById(R.id.selected_name);
        TextView designCompany = findViewById(R.id.selected_company);
        TextView designSize = findViewById(R.id.selected_size);
        TextView designQuantity = findViewById(R.id.selected_quantity);
        delete = (Button)findViewById(R.id.delete);
        back_home = (Button)findViewById(R.id.back_home);

        designName.setText(name);
        designCompany.setText(company);
        designSize.setText(size);
        designQuantity.setText(quantity);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();
                myRef.child("designs").child(name).removeValue();

                Intent i = new Intent(DeleteDesign_Confirm.this, DeleteDesign.class);
                startActivity(i);
                finish();
            }
        });

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeleteDesign_Confirm.this, DeleteDesign.class);
                startActivity(i);
                finish();
            }
        });

    }
}
