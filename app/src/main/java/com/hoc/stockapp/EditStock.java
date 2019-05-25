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
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hoc.stockapp.Adapter.EditStock_MyAdapter;
import com.hoc.stockapp.Model.DesignInformation;
import com.hoc.stockapp.ViewHolder.DesignViewHolder;

import java.util.ArrayList;

public class EditStock extends AppCompatActivity implements EditStock_MyAdapter.OnDesignClickListener {

    private Button bSubmit;
    private EditText tDesignName;
    private RecyclerView rRecyclerView;
    private FirebaseRecyclerAdapter<DesignInformation, DesignViewHolder> mAdapter;
    private FirebaseRecyclerOptions<DesignInformation> mOptions;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ArrayList<DesignInformation> arrayList;
    private ImageButton bBack;
    private ProgressBar pProgressBar;

    public static final String EXTRA_NAME = "designName";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock);

        pProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        bBack = (ImageButton) findViewById(R.id.back);
        bSubmit = (Button)findViewById(R.id.submit);
        arrayList = new ArrayList<>();
        rRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        rRecyclerView.setHasFixedSize(true);
        tDesignName = (EditText)findViewById(R.id.design_name);
        ArrayList<String> companies = new ArrayList<>();

        pProgressBar.setVisibility(View.VISIBLE);
        tDesignName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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
        rRecyclerView.setLayoutManager(gridLayoutManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseUser.getUid()).child("StockInformation").child("Design");

        mOptions = new FirebaseRecyclerOptions.Builder<DesignInformation>()
                .setQuery(mDatabaseReference, DesignInformation.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<DesignInformation, DesignViewHolder>(mOptions) {
            @Override
            protected void onBindViewHolder(@NonNull DesignViewHolder holder, int position, @NonNull DesignInformation model) {
                pProgressBar.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.quantity.setText(model.getQuantity());
            }

            @NonNull
            @Override
            public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_view_stock_card, parent, false);
                return new DesignViewHolder(view);
            }
        };

        mAdapter.startListening();
        rRecyclerView.setAdapter(mAdapter);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void search(String s) {
        Query query = mDatabaseReference.orderByChild("name")
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
                    EditStock_MyAdapter viewStock_myAdapter = new EditStock_MyAdapter(getApplicationContext(),arrayList);
                    rRecyclerView.setAdapter(viewStock_myAdapter);
                    viewStock_myAdapter.notifyDataSetChanged();

                    EditStock_MyAdapter.setOnDesignClickListener(EditStock.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDesignClick(int position) {
        Intent editItemIntent = new Intent(this, EditStock_Design.class);
        DesignInformation clickedItem = arrayList.get(position);

        editItemIntent.putExtra(EXTRA_NAME, clickedItem.getName());

        startActivity(editItemIntent);
    }

}


