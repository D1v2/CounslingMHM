package com.example.counsling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counsling.R;
import com.example.counsling.halper.Chats;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterView> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private List<Chats>chat;
    String userid;
    public MessageAdapter(Context context, List<Chats>chats, String userid) {
        this.context=context;
        this.chat=chats;
        this.userid=userid;
    }
    @NonNull
    @Override
    public MessageAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType!=MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapterView(view);
        }else {
            View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapterView(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterView holder, int position) {
        Chats chats=chat.get(position);
        holder.show_message.setText(chats.getMessage());
        if (position==chat.size()-1){
            if (chats.isIsseen()){
                holder.txt_seen.setVisibility(View.VISIBLE);
                holder.txt_seen.setText("Seen");
            }else {
                holder.txt_seen.setVisibility(View.VISIBLE);
                holder.txt_seen.setText("Delivered");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }
    public int getItemCount(){
        return chat.size();
    }

      class MessageAdapterView extends RecyclerView.ViewHolder{
        TextView show_message;
        TextView txt_seen;
        public MessageAdapterView(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            txt_seen=itemView.findViewById(R.id.txt_seen);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(chat.get(position).getSender().equals(userid)){
                return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}