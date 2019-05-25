package com.hoc.stockapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ProfileFragment extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    private TextView tCompany, tEmail;
    private ImageButton bLogOut;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabae;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private ProgressBar pProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final TextView tCompany = (TextView) view.findViewById(R.id.organization);
        final TextView tEmail = (TextView) view.findViewById(R.id.email);
        bLogOut = (ImageButton) view.findViewById(R.id.logout);
        pProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        pProgressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseUser.getUid()).child("UserInformation");


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pProgressBar.setVisibility(View.GONE);
                String company = (String) dataSnapshot.child("organization").getValue();
                String email = (String) dataSnapshot.child("email").getValue();

                tCompany.setText(company);
                tEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(i);
            }
        });
    }
}
