package com.example.counsling.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.counsling.R;
import com.example.counsling.halper.UserHalper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText inputName, inputEmail, inputPhone, inputPassword,inputConfirmPassword;
    TextView buttonSignup, alreadylogin;
    private FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Id connections
        progressBar=findViewById(R.id.progressbar);
        auth = FirebaseAuth.getInstance();
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPhone = findViewById(R.id.inputNumber);
        inputPassword = findViewById(R.id.inputPassword);
        buttonSignup = findViewById(R.id.buttonsignup);
        alreadylogin = findViewById(R.id.alreadylogin);
        inputConfirmPassword=findViewById(R.id.inputConfirmPassword);


        //Button clicklisteners
        alreadylogin.setOnClickListener(v -> onBackPressed());
        buttonSignup.setOnClickListener(v -> {
            if(inputName.getText().toString().trim().isEmpty()){
                inputName.setError("Name is required");
                inputName.requestFocus();
                return;
            }else if(inputEmail.getText().toString().trim().isEmpty()){
                inputEmail.setError("Email is required");
                inputEmail.requestFocus();
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                inputEmail.setError("Provide a valid email");
                inputEmail.requestFocus();
                return;
            } else if (inputPassword.getText().toString().trim().isEmpty()) {
                inputPassword.setError("Password is required");
                inputName.requestFocus();
                return;
            }else if (inputPhone.getText().toString().trim().isEmpty()){
                inputPhone.setError("Phone Number is required");
                inputPhone.requestFocus();
                return;
            } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                inputConfirmPassword.setError("Confirm Password is required");
                inputConfirmPassword.requestFocus();
                return;
            } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                inputConfirmPassword.setError("Password and Confirm Password doesn't same");
                inputConfirmPassword.requestFocus();
                return;
            } else {
                SignUp();
            }
        });
    }

    //Call in button signin
    private void SignUp() {
        buttonSignup.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final String name = inputName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String phone = inputPhone.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();
                UserHalper userHalper = new UserHalper(name, email, phone, password, userid);
                FirebaseDatabase.getInstance().getReference("users")
                        .child(userid).setValue(userHalper)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                buttonSignup.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUpActivity.this, "Signup is successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                finish();
                            } else {
                                buttonSignup.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUpActivity.this, "Please check again !", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                buttonSignup.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivity.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {

        });
    }
}