package com.pubak.econovation.amadium.activity;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.adapter.RecyclerViewChatAdapter;
import com.pubak.econovation.amadium.dto.MessagesDTO;
import com.pubak.econovation.amadium.utils.Converter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private final String TAG = "ChatActivity";
    private TextView userName;
    private RecyclerView chatRecyclerView;
    private EditText editText;
    private Button sendButton;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private String uid;
    private List<MessagesDTO> messagesDTOList;
    private String chatUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseUser user = MainActivity.getCurrentUser();
        firebaseDatabase = firebaseDatabase.getInstance();

        chatRecyclerView = (RecyclerView) findViewById(R.id.recycler_chat);
        chatRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        chatUserName = bundle.getString("userName");

        messagesDTOList = new ArrayList<>();
        adapter = new RecyclerViewChatAdapter(messagesDTOList);
        initDatabase();

        chatRecyclerView.setAdapter(adapter);

        userName = (TextView) findViewById(R.id.textView_user);
        userName.setText(chatUserName);

        editText = (EditText) findViewById(R.id.editText);
        sendButton = (Button) findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                uploadDatabase(text);
                editText.getText().clear();
            }
        });
    }

    private void uploadDatabase(String text) {
        MessagesDTO messagesDTO = new MessagesDTO();
        messagesDTO.setFromId(MainActivity.getCurrentUser().getUid());
        messagesDTO.setText(text);

        Log.d(TAG, "uploadDatabase: parsing timestamp to double" + Converter.getCurrentTimeStamp());
        messagesDTO.setTimestamp(Converter.getCurrentTimeStamp());
        messagesDTO.setTold(uid);

        String pushId = firebaseDatabase.getReference().child("user-messages").push().getKey();
        Log.d(TAG, "uploadDatabase: pushId: " + pushId);
        firebaseDatabase.getReference().child("user-messages").child(MainActivity.getCurrentUser().getUid()).child(uid).child(pushId).setValue(1);
        firebaseDatabase.getReference().child("user-messages").child(uid).child(MainActivity.getCurrentUser().getUid()).child(pushId).setValue(1);
        firebaseDatabase.getReference().child("messages").child(pushId).setValue(messagesDTO);
    }

    private void initDatabaseList(final String uId) {
        firebaseDatabase.getReference().child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: dataSnapshot : " + dataSnapshot.getValue());
                if (dataSnapshot.getKey().equals(uId)) {
                    MessagesDTO messagesDTO = dataSnapshot.getValue(MessagesDTO.class);
                    messagesDTOList.add(messagesDTO);
                    adapter.notifyItemInserted(messagesDTOList.size() - 1);
                }
                Log.d(TAG, "onChildAdded: " + messagesDTOList.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initDatabase() {
        Log.d(TAG, "initDatabase: user " + MainActivity.getCurrentUser().getUid());
        firebaseDatabase.getReference().child("user-messages").child(MainActivity.getCurrentUser().getUid()).child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "initDatabase: dataSnapshot.getKey() : " + dataSnapshot.getKey());
                Log.d(TAG, "initDatabase: dataSnapshot.getValue() : " + dataSnapshot.getValue());
                initDatabaseList(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}