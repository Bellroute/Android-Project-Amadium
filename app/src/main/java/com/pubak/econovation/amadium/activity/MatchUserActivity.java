package com.pubak.econovation.amadium.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.dto.MessagesDTO;
import com.pubak.econovation.amadium.utils.Converter;
import com.pubak.econovation.amadium.utils.LoadImage;

public class MatchUserActivity extends AppCompatActivity {
    private final String TAG ="MatchUserActivity";
    private TextView userName;
    private TextView userSports;
    private TextView userTier;
    private TextView userWinTieLose;
    private TextView match;
    private ImageView userImage;
    private String email;
    private String name;
    private String sports;
    private String tier;
    private String winTieLose;
    private String userImagePath;
    private FirebaseDatabase firebaseDatabase;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_user);
        firebaseDatabase = FirebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        email = bundle.getString("email");
        name = bundle.getString("userName");
        sports = bundle.getString("userSports");
        tier = bundle.getString("userTier");
        winTieLose = bundle.getString("userWinTieLose");
        userImagePath = bundle.getString("userImage");

        userName = findViewById(R.id.textView_name);
        userName.setText(name);

        userSports = findViewById(R.id.textView_profile_sports);
        userSports.setText(sports);

        userTier = findViewById(R.id.textView_profile_tier);
        userTier.setText(tier);

        userWinTieLose = findViewById(R.id.textView_profile_wintielose);
        userWinTieLose.setText(winTieLose);

        userImage = findViewById(R.id.image_user_profile);
        if (userImagePath != null) {
            new LoadImage(userImage).execute(userImagePath);
        }
        userImage.bringToFront();

        match = findViewById(R.id.textView_match);
        if (email.equals(MainActivity.getCurrentUser().getEmail())) {
            match.setVisibility(View.INVISIBLE);
        }
        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDatabase();
                match.setVisibility(View.INVISIBLE);
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
        firebaseDatabase.getReference().child("resultListener").child(MainActivity.getCurrentUser().getUid()).child(uid).setValue(2);
        firebaseDatabase.getReference().child("resultListener").child(uid).child(MainActivity.getCurrentUser().getUid()).setValue(2);
    }
}
