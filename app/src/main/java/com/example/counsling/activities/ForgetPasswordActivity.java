package com.example.counsling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.counsling.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailEdittext;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //Id connected
        emailEdittext=findViewById(R.id.forgetemail);
        Button resetpassword = findViewById(R.id.resetpassword);
        auth=FirebaseAuth.getInstance();

        //resetbutton
        resetpassword.setOnClickListener(v -> resetPassword());
    }

    //resetpassword function
    private void resetPassword() {
        String email = emailEdittext.getText().toString().trim();
        if (email.isEmpty()) {
            emailEdittext.setError("Email is required!");
            emailEdittext.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdittext.setError("Please provide valid email!");
            emailEdittext.requestFocus();
        } else {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Check your email to reset your password", Toast.LENGTH_LONG).show();
                    emailEdittext.setText("");
                }else {
                    Toast.makeText(getApplicationContext(), "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}