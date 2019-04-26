package com.hoc.stockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hoc.stockapp.Adapter.EditDesign_MyAdapter;
import com.hoc.stockapp.Adapter.EditStock_MyAdapter;
import com.hoc.stockapp.Model.DesignInformation;
import com.hoc.stockapp.ViewHolder.DesignViewHolder;

import java.util.ArrayList;

public class EditDesign extends AppCompatActivity implements EditDesign_MyAdapter.OnDesignClickListener {

    public static final String EXTRA_QUANTIY = "designQuantity";
    public static final String EXTRA_NAME = "designName";
    public static final String EXTRA_COMPANY = "designCompany";
    public static final String EXTRA_SIZE = "designSize";

    Button submit;
    EditText designName;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<DesignInformation, DesignViewHolder> adapter;
    FirebaseRecyclerOptions<DesignInformation> options;
    DatabaseReference databaseReference;
    ArrayList<DesignInformation> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock);

        submit = (Button)findViewById(R.id.submit);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        designName = (EditText)findViewById(R.id.design_name);

        designName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    search(s.toString());
                }
                else{
                    search("");
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference("designs");

        options = new FirebaseRecyclerOptions.Builder<DesignInformation>()
                .setQuery(databaseReference,DesignInformation.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<DesignInformation, DesignViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull DesignViewHolder holder, int position, @NonNull DesignInformation model) {
                holder.name.setText(model.getName());
                holder.company.setText(model.getCompany());
                holder.size.setText(model.getSize());
                holder.quantity.setText(model.getQuantity());
            }

            @NonNull
            @Override
            public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_view_stock_card,parent,false);
                return new DesignViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditDesign.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void search(String s) {
        Query query = databaseReference.orderByChild("name")
                .startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    arrayList.clear();
                    for(DataSnapshot dss: dataSnapshot.getChildren()){
                        final DesignInformation designInformation = dss.getValue(DesignInformation.class);
                        arrayList.add(designInformation);
                    }
                    EditDesign_MyAdapter editDesign_myAdapter = new EditDesign_MyAdapter(getApplicationContext(),arrayList);
                    recyclerView.setAdapter(editDesign_myAdapter);
                    editDesign_myAdapter.notifyDataSetChanged();

                    EditDesign_MyAdapter.setOnDesignClickListener(EditDesign.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDesignClick(int position) {
        Intent editItemIntent = new Intent(this, EditDesign_EditName.class);
        DesignInformation clickedItem = arrayList.get(position);

        editItemIntent.putExtra(EXTRA_NAME, clickedItem.getName());
        editItemIntent.putExtra(EXTRA_COMPANY, clickedItem.getCompany());
        editItemIntent.putExtra(EXTRA_SIZE, clickedItem.getSize());
        editItemIntent.putExtra(EXTRA_QUANTIY, clickedItem.getQuantity());

        startActivity(editItemIntent);
    }
}


