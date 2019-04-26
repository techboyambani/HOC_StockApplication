package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class UpdateDesignInfo extends AppCompatActivity {

    private Button edit_name, delete_design, back_home;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_design_information);

        edit_name = (Button)findViewById(R.id.edit_name);
        delete_design = (Button)findViewById(R.id.delete_design);
        back_home = (Button)findViewById(R.id.back_home);

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i =new Intent(UpdateDesignInfo.this, EditDesign.class);
                startActivity(i);
                finish();
            }
        });

        delete_design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateDesignInfo.this, DeleteDesign.class);
                startActivity(i);
                finish();
            }
        });

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateDesignInfo.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}