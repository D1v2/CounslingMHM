package com.example.counsling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.counsling.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences sharedPreferences=getSharedPreferences("userlog",MODE_PRIVATE);
        Boolean userlogin=sharedPreferences.getBoolean("userlogin",false);
        SharedPreferences sharedPreferences1=getSharedPreferences("doctorlog",MODE_PRIVATE);
        Boolean doctorlogin=sharedPreferences1.getBoolean("doctorlogin",false);
        Thread thread=new Thread(){
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (userlogin) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else if (doctorlogin) {
                        startActivity(new Intent(getApplicationContext(), UserShowActivity.class));
                        finish();
                    } else  {
                        startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}