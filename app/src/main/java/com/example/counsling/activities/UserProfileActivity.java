package com.example.counsling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.counsling.R;

public class UserProfileActivity extends AppCompatActivity {
    TextView name,email,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name=findViewById(R.id.name);
        number=findViewById(R.id.number);
        email=findViewById(R.id.email);
        Intent intent=getIntent();
        String name1=intent.getStringExtra("name");
        String email1=intent.getStringExtra("email");
        String number1=intent.getStringExtra("number");

        name.setText(name1);
        email.setText(email1);
        number.setText(number1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}