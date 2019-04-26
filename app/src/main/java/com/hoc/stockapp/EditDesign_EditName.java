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
import com.hoc.stockapp.Model.DesignInformation;

import static com.hoc.stockapp.EditStock.EXTRA_COMPANY;
import static com.hoc.stockapp.EditStock.EXTRA_NAME;
import static com.hoc.stockapp.EditStock.EXTRA_QUANTIY;
import static com.hoc.stockapp.EditStock.EXTRA_SIZE;


public class EditDesign_EditName extends AppCompatActivity {

    EditText newName;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    Button submit;

    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_edit_design_name);

        Intent editDesignIntent = getIntent();

        final String name = editDesignIntent.getStringExtra(EXTRA_NAME);
        final String company = editDesignIntent.getStringExtra(EXTRA_COMPANY);
        final String size = editDesignIntent.getStringExtra(EXTRA_SIZE);
        final String quantity = editDesignIntent.getStringExtra(EXTRA_QUANTIY);

        final TextView designName = findViewById(R.id.selected_name);
        final TextView designCompany = findViewById(R.id.selected_company);
        final TextView designSize = findViewById(R.id.selected_size);
        final TextView designQuantity = findViewById(R.id.selected_quantity);
        newName = (EditText)findViewById(R.id.name);
        submit = (Button)findViewById(R.id.submit);

        designName.setText(name);
        designCompany.setText(company);
        designSize.setText(size);
        designQuantity.setText(quantity);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String new_name = newName.getText().toString().trim();
                if (TextUtils.isEmpty(new_name)) {
                    Toast.makeText(getApplicationContext(), "Enter new name", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();
                myRef.child("designs").child(name).removeValue();
                myRef.child("designs").child(new_name).setValue(new DesignInformation(new_name, quantity, company, size));

                Intent i = new Intent(EditDesign_EditName.this, EditStock.class);
                startActivity(i);
                finish();
            }
        });

    }
}
