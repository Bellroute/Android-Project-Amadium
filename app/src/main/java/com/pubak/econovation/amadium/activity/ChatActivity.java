package com.pubak.econovation.amadium.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.adapter.RecyclerViewChatAdapter;
import com.pubak.econovation.amadium.dto.MessagesDTO;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.utils.Converter;
import com.pubak.econovation.amadium.utils.TierCalculator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private final String TAG = "ChatActivity";
    private final int WIN_CODE = 0;
    private final int TIE_CODE = 1;
    private final int LOSE_CODE = 2;
    private TextView winButton;
    private TextView tieButton;
    private TextView loseButton;
    private TextView userName;
    private RecyclerView chatRecyclerView;
    private EditText editText;
    private Button sendButton;
    private TextView cancelButton;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private String uid;
    private List<MessagesDTO> messagesDTOList;
    private String chatUserName;
    private String userImageURL;
    private Map<String, UserDTO> userDTOs;
    private TextView resultButton;
    private LinearLayout linearLayout;
    private String myUid;
    private float[] myResultValue;
    private float[] userResultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        myUid = MainActivity.getCurrentUser().getUid();
        firebaseDatabase = firebaseDatabase.getInstance();

        chatRecyclerView = (RecyclerView) findViewById(R.id.recycler_chat);
        chatRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);

        final Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        chatUserName = bundle.getString(    "userName");
        userImageURL = bundle.getString("userImageURL");

        myResultValue = new float[1];
        userResultValue = new float[1];
        messagesDTOList = new ArrayList<>();
        adapter = new RecyclerViewChatAdapter(messagesDTOList, userImageURL);
        initDatabase();
        chatUserDatabase();
        loadResultListenerDatabase(myUid, uid);

        chatRecyclerView.setAdapter(adapter);

        userName = (TextView) findViewById(R.id.textView_user);
        userName.setText(chatUserName);

        editText = (EditText) findViewById(R.id.editText);
        sendButton = (Button) findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (text.equals("") || text.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "내용을 입력해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    uploadDatabase(text);
                    editText.getText().clear();
                }
            }
        });

        winButton = (TextView) findViewById(R.id.texView_win);
        tieButton = (TextView) findViewById(R.id.texView_tie);
        loseButton = (TextView) findViewById(R.id.texView_lose);
        linearLayout = (LinearLayout) findViewById(R.id.result);

        resultButton = (TextView) findViewById(R.id.text_result_button);
        cancelButton = (TextView) findViewById(R.id.cancel) ;
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
            }
        });

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotValueSet(myUid)) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ChatActivity.this, "상대방의 응답을 기다리는 중입니다", Toast.LENGTH_LONG).show();
                }
            }
        });

        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResultValue(WIN_CODE);
                sendResultMessage();
            }
        });

        tieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResultValue(TIE_CODE);
                sendResultMessage();
            }
        });

        loseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResultValue(LOSE_CODE);
                sendResultMessage();
            }
        });
    }

    private boolean isNotValueSet(String uId) {
        return getResultListenerDatabase(uId) == 2.0;
    }

    private float getResultListenerDatabase(String uId) {
        if (uId.equals(myUid)) {
            return myResultValue[0];
        } else {
            return userResultValue[0];
        }
    }

    private void loadResultListenerDatabase(final String myUid, final String userUid) {
        firebaseDatabase.getReference().child("resultListener").child(myUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(userUid)) {
                    float result = dataSnapshot.getValue(Float.class);
                    myResultValue[0] = result;
                    Log.d(TAG, "onChildAdded: getValue float: " + dataSnapshot.getKey() + result);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(userUid)) {
                    float result = dataSnapshot.getValue(Float.class);
                    myResultValue[0] = result;
                    Log.d(TAG, "onChildAdded: getValue float: " + dataSnapshot.getKey() + result);
                }
                Log.d(TAG, "onChildChanged: listener");
                if (getResultListenerDatabase(myUid) + getResultListenerDatabase(uid) == 1.0) {
                    startRating();
                }
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

        firebaseDatabase.getReference().child("resultListener").child(userUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(myUid)) {
                    float result = dataSnapshot.getValue(Float.class);
                    userResultValue[0] = result;
                    Log.d(TAG, "onChildAdded: getValue float: " + dataSnapshot.getKey() + result);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(myUid)) {
                    float result = dataSnapshot.getValue(Float.class);
                    userResultValue[0] = result;
                    Log.d(TAG, "onChildAdded: getValue float: " + dataSnapshot.getKey() + result);
                }
                Log.d(TAG, "onChildChanged: listener");
                if (getResultListenerDatabase(myUid) + getResultListenerDatabase(uid) == 1.0) {
                    startRating();
                }
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

    private void sendResultMessage() {
        uploadDatabase(userDTOs.get("myDTO").getUsername() + "님이 결과 입력을 완료했습니다.");
        linearLayout.setVisibility(View.INVISIBLE);
    }

    private void sendResultValue(int resultCode) {

        switch (resultCode) {
            case WIN_CODE:
                firebaseDatabase.getReference().child("resultListener").child(myUid).child(uid).setValue(1);
                break;
            case TIE_CODE:
                firebaseDatabase.getReference().child("resultListener").child(myUid).child(uid).setValue(0.5);
                break;
            case LOSE_CODE:
                firebaseDatabase.getReference().child("resultListener").child(myUid).child(uid).setValue(0);
                break;
        }
    }

    private void startRating() {
        Log.d(TAG, "startRating: ");
        TierCalculator tierCalculator = new TierCalculator();
        TierCalculator userTierCalculator = new TierCalculator();

        tierCalculator.setData(userDTOs.get("myDTO").getWinTieLose());
        userTierCalculator.setData(userDTOs.get("userDTO").getWinTieLose());

        checkWinTieLose(tierCalculator, userTierCalculator);

        Map<String, Object> myUpdates = new HashMap<>();
        Map<String, Object> userUpdates = new HashMap<>();

        myUpdates.put("winTieLose", tierCalculator.getWinTieLose());
        myUpdates.put("tier", tierCalculator.getTier());
        userUpdates.put("winTieLose", userTierCalculator.getWinTieLose());
        userUpdates.put("tier", userTierCalculator.getTier());

        firebaseDatabase.getReference().child("users").child(MainActivity.getCurrentUser().getUid()).updateChildren(myUpdates);
        firebaseDatabase.getReference().child("users").child(uid).updateChildren(userUpdates);

        if (isIncorrectResultValue()) {
            firebaseDatabase.getReference().child("resultListener").child(MainActivity.getCurrentUser().getUid()).child(uid).setValue(2);
            firebaseDatabase.getReference().child("resultListener").child(uid).child(MainActivity.getCurrentUser().getUid()).setValue(2);
            uploadDatabase("입력한 결과가 서로 일치하지 않습니다. 다시 시도하세요");
        } else {
           Toast.makeText(ChatActivity.this, "매치 종료", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isIncorrectResultValue() {
        return !isNotValueSet(myUid) && !isNotValueSet(uid) && getResultListenerDatabase(myUid) + getResultListenerDatabase(uid) != 1.0;
    }

    private void checkWinTieLose(TierCalculator tierCalculator, TierCalculator userTierCalculator) {
        double resultValue = getResultListenerDatabase(myUid);

        if (resultValue == 1) {
            tierCalculator.win();
            userTierCalculator.lose();
        } else if (resultValue == 0) {
            tierCalculator.lose();
            userTierCalculator.win();
        } else {
            tierCalculator.tie();
            userTierCalculator.tie();
        }
    }

    private void chatUserDatabase() {
        userDTOs = new HashMap<>();

        firebaseDatabase.getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                if (dataSnapshot.getKey().equals(MainActivity.getCurrentUser().getUid())) {
                    userDTOs.put("myDTO", userDTO);
                    Log.d(TAG, "onChildAdded: my" + userDTO.getWinTieLose());
                }
                if (dataSnapshot.getKey().equals(uid)) {
                    userDTOs.put("userDTO", userDTO);
                    Log.d(TAG, "onChildAdded: u" + userDTO.getWinTieLose());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                if (dataSnapshot.getKey().equals(MainActivity.getCurrentUser().getUid())) {
                    userDTOs.put("myDTO", userDTO);
                    Log.d(TAG, "onChildAdded: my" + userDTO.getWinTieLose());
                }
                if (dataSnapshot.getKey().equals(uid)) {
                    userDTOs.put("userDTO", userDTO);
                    Log.d(TAG, "onChildAdded: u" + userDTO.getWinTieLose());
                }
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