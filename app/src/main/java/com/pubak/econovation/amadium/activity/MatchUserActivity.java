package com.pubak.econovation.amadium.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.dto.MessagesDTO;
import com.pubak.econovation.amadium.utils.Converter;

public class MatchUserActivity extends AppCompatActivity {
    private final String TAG ="MatchUserActivity";
    private TextView userName;
    private TextView userSports;
    private TextView userTier;
    private TextView match;
    private String uid;
    private String name;
    private String sports;
    private String tier;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_user);
        firebaseDatabase = firebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        name = bundle.getString("userName");
        sports = bundle.getString("userSports");
        tier = bundle.getString("userTier");

        userName = (TextView) findViewById(R.id.textView_user);
        userName.setText(name);

        userSports = (TextView) findViewById(R.id.textView_sports);
        userSports.setText("스포츠: " + sports);

        userTier = (TextView) findViewById(R.id.textView_tier);
        userTier.setText("티어: " + tier);

        match = (TextView) findViewById(R.id.textView_match);
        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDatabase();
                match.setVisibility(v.INVISIBLE);
                Toast.makeText(v.getContext(), "매치리스트를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadDatabase() {
        MessagesDTO messagesDTO = new MessagesDTO();
        messagesDTO.setFromId(MainActivity.getCurrentUser().getUid());
        messagesDTO.setText("대결을 신청합니다");

        Log.d(TAG, "uploadDatabase: parsing timestamp to double" + Converter.getCurrentTimeStamp());
        messagesDTO.setTimestamp(Converter.getCurrentTimeStamp());
        messagesDTO.setTold(uid);

        String pushId = firebaseDatabase.getReference().child("user-messages").push().getKey();
        Log.d(TAG, "uploadDatabase: pushId: " + pushId);
        firebaseDatabase.getReference().child("user-messages").child(MainActivity.getCurrentUser().getUid()).child(uid).child(pushId).setValue(1);
        firebaseDatabase.getReference().child("user-messages").child(uid).child(MainActivity.getCurrentUser().getUid()).child(pushId).setValue(1);
        firebaseDatabase.getReference().child("messages").child(pushId).setValue(messagesDTO);
    }
}
