package com.hoc.stockapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity extends AppCompatActivity {

    private TextView textView1, download_app;
    private Button view_stock, edit_stock, add_item, update_design;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference, sRef;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_stock = (Button)findViewById(R.id.view_stock);
        edit_stock = (Button)findViewById(R.id.edit_stock);
        add_item = (Button)findViewById(R.id.add_design);
        update_design = (Button)findViewById(R.id.update_design);
        download_app = (TextView)findViewById(R.id.download_app);


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

        update_design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UpdateDesignInfo.class);
                startActivity(i);
                finish();
            }
        });

        download_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

    }

    private void download() {
        storageReference = firebaseStorage.getInstance().getReference();
        sRef = storageReference.child("HOC_StockApplication_1.0.0.apk");
        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFile(MainActivity.this, "HOC_StockApplication_1.0.0", ".apk", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadManager.enqueue(request);
    }

}