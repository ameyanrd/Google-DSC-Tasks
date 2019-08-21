package com.example.chat_bot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIDataService;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class MainActivity extends AppCompatActivity implements AIListener{

    private EditText etMessage;
    private RecyclerView recyclerView;
    private RelativeLayout addBtn;
    private RecyclerView.Adapter adapter;
    private List<ChatMessage> chatMessages;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myref;
    private final static String ACCESS_TOKEN="9b345b5caead4dd08c5ae1626f14dd78 ";
    private AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AIConfiguration config = new AIConfiguration(ACCESS_TOKEN, AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(config);
        final AIRequest aiRequest = new AIRequest();

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
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = postSnapShot.getValue(ChatMessage.class);
                    //Log.e("Get Data",chatMessage.getMessage());
                    chatMessages.add(chatMessage);
                }
                adapter = new MyAdapter(MainActivity.this, chatMessages);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });

        recyclerView.setAdapter(adapter);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = etMessage.getText().toString().trim();
                final ChatMessage chatMessage = new ChatMessage(message, "user");
                myref.child("chat").push().setValue(chatMessage);
                chatMessages.add(chatMessage);
                adapter = new MyAdapter(MainActivity.this, chatMessages);
                recyclerView.setAdapter(adapter);
                aiRequest.setQuery(message);
                new AsyncTask<AIRequest,Void, AIResponse>() {

                    @Override
                    protected AIResponse doInBackground(AIRequest... aiRequests) {
                        final AIRequest request = aiRequests[0];
                        try {
                            final AIResponse response = aiDataService.request(aiRequest);
                            return response;
                        } catch (AIServiceException e) {
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(AIResponse response) {
                        if (response != null) {

                            Result result = response.getResult();
                            String reply = result.getFulfillment().getSpeech();
                            ChatMessage chatMessage = new ChatMessage(reply, "bot");
                            myref.child("chat").push().setValue(chatMessage);
                            chatMessages.add(chatMessage);
                            adapter = new MyAdapter(MainActivity.this, chatMessages);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }.execute(aiRequest);

                etMessage.setText("");
                Log.i("size", String.valueOf(chatMessages.size()));
                Log.i("message", chatMessages.get(0).getMessage());

            }
        });

    }

    @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {
        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG);
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
