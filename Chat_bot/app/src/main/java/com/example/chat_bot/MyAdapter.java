package com.example.chat_bot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessages;


    public MyAdapter(Context context, List<ChatMessage> userProfiles) {
        this.context = context;
        this.chatMessages = userProfiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        ChatMessage chatMessage = chatMessages.get(i);

        if (chatMessage.getUser().equals("user")) {

            viewHolder.rightText.setText(chatMessage.getMessage());
            viewHolder.rightText.setVisibility(View.VISIBLE);
            viewHolder.leftText.setVisibility(View.GONE);
        }
        else {
            viewHolder.leftText.setText(chatMessage.getMessage());
            viewHolder.rightText.setVisibility(View.GONE);
            viewHolder.leftText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView leftText,rightText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftText = itemView.findViewById(R.id.leftText);
            rightText = itemView.findViewById(R.id.rightText);

        }
    }
}