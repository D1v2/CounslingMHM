package com.example.counsling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.counsling.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText inputEmail;
    EditText inputPassword;
    private FirebaseAuth auth;
    private TextView buttonLogin;
    TextView forgetPassword;
    ProgressBar progressBar;
    CheckBox checkBox;
    Boolean isRemendar=false;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //ID connected
        forgetPassword=findViewById(R.id.forgetpassword);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        buttonLogin=findViewById(R.id.buttonsignin);
        checkBox=findViewById(R.id.checkbox);
        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressbar);


        //shared preference
        sharedPreferences=getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        isRemendar=sharedPreferences.getBoolean("Checked",false);
        if(isRemendar){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        //forgetpassword and expert login
        forgetPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),ForgetPasswordActivity.class)));

        //Login button
        findViewById(R.id.buttonsignup).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));
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

    //it function call in login button

    private void signIn() {
        buttonLogin.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String email=inputEmail.getText().toString().trim();
        String password=inputPassword.getText().toString().trim();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                    boolean checked=checkBox.isChecked();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.putBoolean("Checked",checked);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"You login successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
            }else {
                Toast.makeText(SignInActivity.this, "Something is wrong !", Toast.LENGTH_SHORT).show();
                buttonLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(e -> {

        });
    }
}