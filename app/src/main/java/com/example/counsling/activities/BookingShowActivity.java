package com.example.counsling.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.counsling.R;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import static com.example.counsling.R.layout.activity_booking_show;

public class BookingShowActivity extends AppCompatActivity {
    TextView username;
    TextView usernumber;
    TextView userproblem;
    TextView usershowname;
    TextView callyou;
    ImageView calling;
    ImageView endcalling;
    Intent intent;
    private boolean running;
    LinearLayout mainlayout;
    LinearLayout callinglayout;
    String doctorid = "CA1";
    Chronometer chronometer;
    private long pauseoff;
    private static final String APP_KEY = "98126f9a-d333-45eb-9974-65751c2d23de";
    private static final String APP_SECRET = "oyxXpZb7kUeNrtBe9b62EQ==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";
    SinchClient sinchClient;
    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_booking_show);
        //Id connection
        username = findViewById(R.id.name);
        usernumber = findViewById(R.id.number);
        chronometer = findViewById(R.id.chronometer);
        userproblem = findViewById(R.id.problem);
        calling = findViewById(R.id.calling);
        endcalling = findViewById(R.id.endcall);
        usershowname = findViewById(R.id.usershowname);
        mainlayout = findViewById(R.id.mainlayout);
        callinglayout = findViewById(R.id.callinglayout);
        callyou = findViewById(R.id.callyou);
        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BookingShowActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        intent = getIntent();
        String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        String problem = intent.getStringExtra("problem");
        String id = intent.getStringExtra("id");
        String timeofcall = intent.getStringExtra("callyou");

        sinchClient = Sinch.getSinchClientBuilder()
                .context(getApplicationContext())
                .userId(doctorid)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.setSupportCalling(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.setSupportPushNotifications(true);
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener() {

        });
        sinchClient.start();

        username.setText(name);
        usernumber.setText(number);
        userproblem.setText(problem);
        callyou.setText(timeofcall);
        calling.setOnClickListener(v -> {
            if (call == null) {
                call = sinchClient.getCallClient().callUser(id);
                call.addCallListener(new SinchCallListener());
                mainlayout.setVisibility(View.INVISIBLE);
                callinglayout.setVisibility(View.VISIBLE);
            }
        });

        usershowname.setText(name);
        endcalling.setOnClickListener(v -> {
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseoff = 0;
            call.hangup();
            chronometer.setVisibility(View.INVISIBLE);
            mainlayout.setVisibility(View.VISIBLE);
            callinglayout.setVisibility(View.INVISIBLE);
        });

    }


    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(BookingShowActivity.this, "Call is Ringing...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(BookingShowActivity.this, "Call is Connected...", Toast.LENGTH_SHORT).show();
            chronometer.setVisibility(View.VISIBLE);
            chronometer.start();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseoff);
        }

        @Override
        public void onCallEnded(Call endcall) {
            Toast.makeText(BookingShowActivity.this, "Call is hangup", Toast.LENGTH_SHORT).show();
            call = null;
            mainlayout.setVisibility(View.VISIBLE);
            callinglayout.setVisibility(View.INVISIBLE);
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseoff = 0;
            chronometer.setVisibility(View.INVISIBLE);
            endcall.hangup();

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            //do nothing

        }
    }

    private static class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, final Call incomingcall) {
            //do nothing
        }
    }
}