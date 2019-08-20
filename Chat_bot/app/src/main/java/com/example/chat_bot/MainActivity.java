    package com.example.chat_bot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

    public class MainActivity extends AppCompatActivity {

    private EditText etMessage;
    private RecyclerView recyclerView;
    private RelativeLayout addBtn;
    private RecyclerView.Adapter adapter;
    private List<ChatMessage> chatMessages;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        etMessage = findViewById(R.id.etMessage);
        addBtn = findViewById(R.id.addBtn);
        chatMessages = new ArrayList<>();
        
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myref = firebaseDatabase.getReference();
        myref.keepSynced(true);

        myref.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    ChatMessage chatMessage = postSnapShot.getValue(ChatMessage.class);
                    //Log.e("Get Data",chatMessage.getMessage());
                    chatMessages.add(chatMessage);
                }
                adapter = new MyAdapter(MainActivity.this, chatMessages);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " , databaseError.getMessage());
            }
        });

        recyclerView.setAdapter(adapter);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = etMessage.getText().toString().trim();

//                if(!message.equals("")){
                    //Log.i("signal","Add Btn pressed!");
                    ChatMessage chatMessage = new ChatMessage(message, "user");
                    myref.child("chat").push().setValue(chatMessage);
                    chatMessages.add(chatMessage);
                    adapter = new MyAdapter(MainActivity.this, chatMessages);
                    recyclerView.setAdapter(adapter);
//                }
                etMessage.setText("");
                Log.i("size",String.valueOf(chatMessages.size()));
                Log.i("message",chatMessages.get(0).getMessage());

            }
        });


    }
}
