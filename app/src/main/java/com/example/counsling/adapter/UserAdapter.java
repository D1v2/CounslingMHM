package com.example.counsling.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.counsling.R;
import com.example.counsling.activities.MessageActivity;
import com.example.counsling.halper.UserHalper;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context context;
    private List<UserHalper>userHelpers;
    String doctorid="CA1";

    public UserAdapter(Context context, List<UserHalper>userHelpers){
        this.context=context;
        this.userHelpers=userHelpers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserHalper user=userHelpers.get(position);
        holder.username.setText(user.getName());
        holder.usernumber.setText(user.getPhonenumber());
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, MessageActivity.class);
            intent.putExtra("userid",doctorid);
            intent.putExtra("doctorid",user.getUserid());
            intent.putExtra("name",user.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userHelpers.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
           TextView username,usernumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.username);
            usernumber=itemView.findViewById(R.id.usernumber);
        }
    }
}
