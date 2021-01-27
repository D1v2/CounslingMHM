package com.example.counsling.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.counsling.R;
import com.example.counsling.adapter.MessageAdapter;
import com.example.counsling.halper.Chats;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    String userid;
    String doctorid;
    TextView username;
    String receivername;
    EditText txt_send;
    ImageView btn_send;
    ImageView back;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    DatabaseReference reference;
    Intent intent;
    List<Chats> chat;
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //get data for chatting
        intent = getIntent();
        userid = intent.getStringExtra("userid");
        doctorid = intent.getStringExtra("doctorid");
        receivername = intent.getStringExtra("name");

        //Id conntection
        username = findViewById(R.id.username);
        txt_send = findViewById(R.id.text_send);
        btn_send = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recyclerview);
        username.setText(receivername);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
        recyclerView.setHasFixedSize(true);

        //set layout in recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        btn_send.setOnClickListener(v -> {
            String msg = txt_send.getText().toString();
            if (!msg.equals("")) {
                sendMesssage(userid, doctorid, msg);
            } else {
                Toast.makeText(getApplicationContext(), "Please write a message", Toast.LENGTH_SHORT).show();
            }
            txt_send.setText("");
        });
        readMessage(userid, doctorid);
        seenMessage(userid, doctorid);
    }

    private void seenMessage(String userid, String doctorid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);
                    assert chats != null;
                    if (chats.getReceiver().equals(userid) && chats.getSender().equals(doctorid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //do nothing
            }
        });

    }

    private void sendMesssage(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

    }
    private void readMessage(String userid1, String doctorid1) {
        chat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chats chats = dataSnapshot.getValue(Chats.class);
                    assert chats != null;
                    if (chats.getReceiver().equals(userid1) && chats.getSender().equals(doctorid1) ||
                            chats.getReceiver().equals(doctorid1) && chats.getSender().equals(userid1)) {
                        chat.add(chats);
                    }
                    messageAdapter = new MessageAdapter(getApplicationContext(), chat, userid);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //do nothing
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }
}