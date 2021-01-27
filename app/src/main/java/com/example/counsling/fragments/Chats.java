package com.example.counsling.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.counsling.R;
import com.example.counsling.adapter.UserAdapter;
import com.example.counsling.halper.UserHalper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chats extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserHalper> userHelpers;
    public Chats(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView=view.findViewById(R.id.recyclerview_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userHelpers=new ArrayList<>();
        readUser();
        return view;
    }
    private void readUser() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userHelpers.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    UserHalper user1 = dataSnapshot.getValue(UserHalper.class);
                    userHelpers.add(user1);
                }
                userAdapter = new UserAdapter(getContext(),userHelpers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //no to do
            }
        });
    }
}