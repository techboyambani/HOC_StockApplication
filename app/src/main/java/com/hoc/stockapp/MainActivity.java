package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView1;
    private Button view_stock, edit_stock, add_item;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_stock = (Button)findViewById(R.id.view_stock);
        edit_stock = (Button)findViewById(R.id.edit_stock);
        add_item = (Button)findViewById(R.id.add_design);

        view_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i =new Intent(MainActivity.this, ViewStock.class);
                startActivity(i);
                finish();
            }
        });

        edit_stock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, EditStock.class));
            }
        });

        add_item.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,AddItem.class);
                startActivity(i);
                finish();
            }
        });

    }

}