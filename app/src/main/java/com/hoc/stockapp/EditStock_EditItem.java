package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoc.stockapp.Model.DesignInformation;

import java.util.ArrayList;

import static com.hoc.stockapp.EditStock.EXTRA_COMPANY;
import static com.hoc.stockapp.EditStock.EXTRA_NAME;
import static com.hoc.stockapp.EditStock.EXTRA_QUANTIY;
import static com.hoc.stockapp.EditStock.EXTRA_SIZE;


public class EditStock_EditItem extends AppCompatActivity {

    EditText newQuantity;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    Button submit;

    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_edit_item);

        Intent editDesignIntent = getIntent();

        final String name = editDesignIntent.getStringExtra(EXTRA_NAME);
        String company = editDesignIntent.getStringExtra(EXTRA_COMPANY);
        String size = editDesignIntent.getStringExtra(EXTRA_SIZE);
        String quantity = editDesignIntent.getStringExtra(EXTRA_QUANTIY);

        final TextView designName = findViewById(R.id.selected_name);
        TextView designCompany = findViewById(R.id.selected_company);
        TextView designSize = findViewById(R.id.selected_size);
        TextView designQuantity = findViewById(R.id.selected_quantity);
        newQuantity = (EditText)findViewById(R.id.quantity);
        submit = (Button)findViewById(R.id.submit);

        designName.setText(name);
        designCompany.setText(company);
        designSize.setText(size);
        designQuantity.setText(quantity);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String quan = newQuantity.getText().toString().trim();
                if (TextUtils.isEmpty(quan)) {
                    Toast.makeText(getApplicationContext(), "Enter new Quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();
                myRef.child("designs").child(name).child("quantity").setValue(quan);

                Intent i = new Intent(EditStock_EditItem.this, EditStock.class);
                startActivity(i);
                finish();
            }
        });

    }
}
