package com.example.counsling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.counsling.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExpertLoginActivity extends AppCompatActivity {

    private EditText inputEmail;
    EditText inputPassword;
    private TextView buttonLogin;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_login);

        //Id connection
        progressBar = findViewById(R.id.progressbar);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.signin);

        //Login as expert
        buttonLogin.setOnClickListener(v -> {
            if (inputEmail.getText().toString().trim().isEmpty()) {
                inputEmail.setError("Email is required");
                inputEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                inputEmail.setError("Enter Valid Email");
                inputEmail.requestFocus();
            } else if (inputPassword.getText().toString().trim().isEmpty()) {
                inputPassword.setError("Enter Password");
                inputPassword.requestFocus();
            } else {
                signIn();
            }
        });
    }

    private void signIn() {
        buttonLogin.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Doctor")
                .whereEqualTo("doctoremail", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        buttonLogin.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        SharedPreferences sharedPreferences1=getSharedPreferences("doctorlog",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences1.edit();
                        editor.putBoolean("doctorlogin",true);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), UserShowActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(ExpertLoginActivity.this, "You are unable to login", Toast.LENGTH_SHORT).show();
                        buttonLogin.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}