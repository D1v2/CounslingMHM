package com.example.counsling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.counsling.R;

public class MainPageActivity extends AppCompatActivity {
    LinearLayout user, expert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        user = findViewById(R.id.user);
        expert = findViewById(R.id.expert);
        user.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
        expert.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ExpertLoginActivity.class)));
    }
}