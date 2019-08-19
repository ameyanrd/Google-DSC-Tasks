    package com.example.chat_bot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

    public class MainActivity extends AppCompatActivity {

    private EditText etMessage;
    private RecyclerView recyclerView;
    private RelativeLayout addBtn;
    private RecyclerView.Adapter adapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        etMessage = (EditText)findViewById(R.id.etMessage);
        addBtn = (RelativeLayout)findViewById(R.id.addBtn);
        chatMessages = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = etMessage.getText().toString().trim();
                if(!message.equals("")){
                    Log.i("signal","Add Btn pressed!");
                    ChatMessage chatMessage = new ChatMessage(message, "user");
                    chatMessages.add(chatMessage);
                }
                etMessage.setText("");
                Log.i("size",String.valueOf(chatMessages.size()));
                Log.i("message",chatMessages.get(0).getMessage());
                adapter = new MyAdapter(MainActivity.this, chatMessages);
                recyclerView.setAdapter(adapter);
            }
        });


    }
}
