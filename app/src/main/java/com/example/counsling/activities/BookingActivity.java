package com.example.counsling.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.counsling.Messaging.Constants;
import com.example.counsling.R;
import com.example.counsling.fragments.ThanksFragment;
import com.example.counsling.halper.BookingHalper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingActivity extends AppCompatActivity {

    ImageView back;
    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText name;
    EditText number;
    EditText callyou;
    Button book;
    FirebaseAuth auth;
    Spinner spinner;
    CheckBox checkfacebook;
    CheckBox checkvideo;
    String spinnertext;
    ProgressBar progressBar;
    String token;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //Id connected
        checkfacebook = findViewById(R.id.checkbox_facebook);
        checkvideo = findViewById(R.id.checkbox_video);
        spinner = findViewById(R.id.spinner);
        back = findViewById(R.id.back);
        radioGroup = findViewById(R.id.radiogroup);
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        callyou = findViewById(R.id.callyou);
        number = findViewById(R.id.number);
        book = findViewById(R.id.book);
        progressBar = findViewById(R.id.progressbar);

        //Button click listener
        back.setOnClickListener(v -> onBackPressed());
        book.setOnClickListener(v -> {
            if (name.getText().toString().trim().equals("")) {
                name.setError("Please provide your name");
                name.requestFocus();
            } else if (number.getText().toString().trim().equals("")) {
                number.setError("Please provide your number");
                number.requestFocus();
            } else if (number.getText().toString().trim().length() > 11 || number.getText().toString().trim().length() < 10) {
                number.setError("Please enter valid number");
                number.requestFocus();
            } else if (callyou.getText().toString().trim().equals("")) {
                callyou.setError("Please Fill this Field");
                callyou.requestFocus();
            } else {
                saveData();
            }
        });


        //
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                token=snapshot.child(userid).child(Constants.KEY_FCM_TOKEN).getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.issue, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnertext = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    //savedData function
    private void saveData() {
        book.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String fname = name.getText().toString().trim();
        String fnumber = number.getText().toString().trim();
        String callyoutime = callyou.getText().toString().trim();
        String follow = "Follow on Instagarm " + radioButton.getText();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userid = user.getUid();
        BookingHalper bookingHalper = new BookingHalper(fname, fnumber, spinnertext, follow, userid, callyoutime,token);
        FirebaseDatabase.getInstance().getReference("Bookings")
                .child(userid).setValue(bookingHalper).addOnCompleteListener(task -> {
            Fragment fragment;
            fragment = new ThanksFragment();
            loadFragment(fragment);
            book.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(callyou.getWindowToken(), 0);
        }).addOnFailureListener(e ->
                Toast.makeText(BookingActivity.this, "Something is Occured !", Toast.LENGTH_SHORT).show());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.wrapper1, fragment);
        transaction.commit();
    }
}