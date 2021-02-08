package com.example.counsling.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.counsling.Messaging.Constants;
import com.example.counsling.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private String userId;
    ImageView chats, calling, acceptcall, endcall;
    LinearLayout mainlayout, callinglayout;
    Button booking;
    private long pauseoff;
    private boolean running;
    ImageSlider imageSlider;
    String doctorid = "CA1";
    RelativeLayout userprofile;
    Chronometer chronometer;
    TextView connected, income;
    String receivername = "Admin";
    private static final String APP_KEY = "98126f9a-d333-45eb-9974-65751c2d23de";
    private static final String APP_SECRET = "oyxXpZb7kUeNrtBe9b62EQ==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";
    SharedPreferences sharedPreferences;
    Call call;
    SinchClient sinchClient;
    TextView usershowname;
    String username,usernumber,useremail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Take permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        //Id Connected
        chronometer = findViewById(R.id.chronometer);
        income = findViewById(R.id.income);
        mainlayout = findViewById(R.id.mainlayout);
        callinglayout = findViewById(R.id.callinglayout);
        connected = findViewById(R.id.connected);
        navigationView = findViewById(R.id.navmenu);
        chats = findViewById(R.id.chats);
        acceptcall = findViewById(R.id.acceptcall);
        endcall = findViewById(R.id.endcall);
        booking = findViewById(R.id.booking);
        calling = findViewById(R.id.Calling1);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        imageSlider = findViewById(R.id.imageslider);
        usershowname=findViewById(R.id.usernameshow);
        setSupportActionBar(toolbar);

        //sharedpreference
        sharedPreferences = getSharedPreferences("userlog", MODE_PRIVATE);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase.getInstance().getReference("users");
            assert user != null;
            userId = user.getUid();

        //Actions
        booking.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BookingActivity.class)));

        //toolbar
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View view = navigationView.getHeaderView(0);
        usershowname=view.findViewById(R.id.usernameshow);
        userprofile=view.findViewById(R.id.relative_profile);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username=snapshot.child(userId).child("name").getValue(String.class);
                useremail=snapshot.child(userId).child("email").getValue(String.class);
                usernumber=snapshot.child(userId).child("phonenumber").getValue(String.class);
                usershowname.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userprofile.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),UserProfileActivity.class);
            intent.putExtra("name",usernumber);
            intent.putExtra("email",useremail);
            intent.putExtra("number",usernumber);
            startActivity(intent);
        });
        navigationView.setNavigationItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.logout) {
                logout();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuItem.getItemId() == R.id.share) {
                invitefriends();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuItem.getItemId() == R.id.about) {
                Intent intent=new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });


        //Calling
        try {
            sinchClient = Sinch.getSinchClientBuilder()
                    .context(getApplicationContext())
                    .userId(userId)
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
        }catch (Exception e){
            System.out.println(e);
        }

        calling.setOnClickListener(v -> {
            if (call == null) {
                call = sinchClient.getCallClient().callUser(doctorid);
                call.addCallListener(new SinchCallListener());
            }
        });

        //chatting function
        chats.setOnClickListener(v -> {
            Intent intent = new Intent(this, MessageActivity.class);
            intent.putExtra("userid", userId);
            intent.putExtra("doctorid", doctorid);
            intent.putExtra("name", receivername);
            startActivity(intent);
        });

        //Image Load in Image Slider
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/counsling-2eaf0.appspot.com/o/relationship%20issues.png?alt=media&token=e4733e7b-72d2-4406-b5b2-ff1b72261a8e", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/counsling-2eaf0.appspot.com/o/relationship%20issues%20(4).png?alt=media&token=97b49b05-22e8-4112-9615-7ccb14bf532c", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/counsling-2eaf0.appspot.com/o/relationship%20issues%20(3).png?alt=media&token=e6682a1b-acf7-482f-bd5d-064f9529f2dd", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/counsling-2eaf0.appspot.com/o/relationship%20issues%20(2).png?alt=media&token=97bc5c11-7028-4687-940c-d6c53e4417e8", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/counsling-2eaf0.appspot.com/o/relationship%20issues%20(1).png?alt=media&token=058f5c7c-1d03-4731-ab9b-2191fddd08b9", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    sendFCMTokenToDatabase(task.getResult().getToken());
                }
            });

    }

    private void sendFCMTokenToDatabase(String token){
        try {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(userId);
            reference.child(Constants.KEY_FCM_TOKEN).setValue(token);
        }catch (Exception e){

        }

    }

    //LogoutFunction
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users").child(userId).child(Constants.KEY_FCM_TOKEN);
        databaseReference.removeValue();
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getApplicationContext(),MainPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(MainActivity.this, "Call is Ringing...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(MainActivity.this, "Call is Connected...", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCallEnded(Call endcall) {
            Toast.makeText(MainActivity.this, "Call is hangup", Toast.LENGTH_SHORT).show();
            mainlayout.setVisibility(View.VISIBLE);
            callinglayout.setVisibility(View.INVISIBLE);
            call = null;
            endcall.hangup();

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            //do nothing

        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, final Call incomingcall) {
            mainlayout.setVisibility(View.INVISIBLE);
            callinglayout.setVisibility(View.VISIBLE);

            acceptcall.setOnClickListener(v -> {
                call = incomingcall;
                connected.setVisibility(View.VISIBLE);
                income.setVisibility(View.INVISIBLE);
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseoff);
                    chronometer.setVisibility(View.VISIBLE);
                    chronometer.start();
                call.answer();
                call.addCallListener(new SinchCallListener());
            });
            endcall.setOnClickListener(v -> {
                call = incomingcall;
                income.setVisibility(View.VISIBLE);
                connected.setVisibility(View.INVISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseoff = 0;
                chronometer.setVisibility(View.INVISIBLE);
                call.hangup();
                mainlayout.setVisibility(View.VISIBLE);
                callinglayout.setVisibility(View.INVISIBLE);
            });
        }
    }
    private void invitefriends() {
        ApplicationInfo api = getApplicationContext().getApplicationInfo();
        String apkpath = api.sourceDir;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/vnd.android.package-archive");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
        startActivity(Intent.createChooser(intent, "Sharevia"));
    }
}