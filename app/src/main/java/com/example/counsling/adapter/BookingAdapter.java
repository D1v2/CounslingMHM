package com.example.counsling.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counsling.Listener.UserListener;
import com.example.counsling.R;
import com.example.counsling.activities.BookingShowActivity;
import com.example.counsling.halper.BookingHalper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BookingAdapter extends FirebaseRecyclerAdapter<BookingHalper, BookingAdapter.BookingView> {

    Context context;
    UserListener userListener;
    public BookingAdapter(@NonNull FirebaseRecyclerOptions<BookingHalper> options, Context context, UserListener userListener) {
        super(options);
        this.context=context;
        this.userListener=userListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookingView holder, int position, @NonNull BookingHalper model) {
        holder.username.setText(model.getUsername());
        holder.usernumber.setText(model.getUsernumber());
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, BookingShowActivity.class);
            intent.putExtra("id",model.getUserid());
            intent.putExtra("token",model.getToken());
            intent.putExtra("name",model.getUsername());
            intent.putExtra("number",model.getUsernumber());
            intent.putExtra("problem",model.getProblem());
            intent.putExtra("callyou",model.getCallyou());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public BookingView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new BookingView(view);
    }

    class BookingView extends RecyclerView.ViewHolder {
        TextView username, usernumber;
        public BookingView(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            usernumber = itemView.findViewById(R.id.usernumber);
        }
    }
}