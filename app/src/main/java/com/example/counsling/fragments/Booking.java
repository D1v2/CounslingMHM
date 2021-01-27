package com.example.counsling.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counsling.R;
import com.example.counsling.adapter.BookingAdapter;
import com.example.counsling.halper.BookingHalper;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Booking extends Fragment {
    RecyclerView recyclerview;
    BookingAdapter bookingAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerview=view.findViewById(R.id.recyclerview_booking);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<BookingHalper> options =
                new FirebaseRecyclerOptions.Builder<BookingHalper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bookings"), BookingHalper.class)
                        .build();
        bookingAdapter=new BookingAdapter(options,getContext());
        recyclerview.setAdapter(bookingAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        bookingAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        bookingAdapter.stopListening();
    }
}