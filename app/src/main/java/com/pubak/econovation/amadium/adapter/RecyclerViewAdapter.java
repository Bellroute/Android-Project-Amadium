package com.pubak.econovation.amadium.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.ChatActivity;
import com.pubak.econovation.amadium.dto.MessagesDTO;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.utils.Converter;
import com.pubak.econovation.amadium.utils.LoadImage;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final String TAG = "RecyclerViewAdapter";
    private List<UserDTO> dataSet;
    private Context context;
    private Map<String, String> lastChatUidMap;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameView;
        public TextView lastChat;
        public ImageView userImageView;
        public View mView;
        public TextView chatTime;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            userImageView = (ImageView) view.findViewById(R.id.user_image);
            userNameView = (TextView) view.findViewById(R.id.user_name);
            lastChat = (TextView) view.findViewById(R.id.last_chat);
            chatTime = (TextView) view.findViewById(R.id.chat_time);
        }
    }

    public RecyclerViewAdapter(Context context, List<UserDTO> dataSet, Map<String, String> lastChatUidMap) {
        this.dataSet = dataSet;
        this.context = context;
        this.lastChatUidMap = lastChatUidMap;
        Log.d(TAG, "RecyclerViewAdapter: " + dataSet);
        Log.d(TAG, "RecyclerViewAdapter: " + lastChatUidMap);
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {

        holder.userNameView.setText(dataSet.get(position).getUsername());

        final String lastChatUid = lastChatUidMap.get(dataSet.get(position).getUid());
        FirebaseDatabase.getInstance().getReference().child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: b" + dataSnapshot.getKey());
                if (lastChatUid.equals(dataSnapshot.getKey())) {
                    Log.d(TAG, "onChildAdded: b");
                    MessagesDTO messagesDTO = dataSnapshot.getValue(MessagesDTO.class);
                    holder.lastChat.setText(messagesDTO.getText());
                    holder.chatTime.setText(Converter.convertTimeStampToString(messagesDTO.getTimestamp()));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: b" + dataSnapshot.getKey());
                if (lastChatUid.equals(dataSnapshot.getKey())) {
                    Log.d(TAG, "onChildAdded: b");
                    MessagesDTO messagesDTO = dataSnapshot.getValue(MessagesDTO.class);
                    holder.lastChat.setText(messagesDTO.getText());
                    holder.chatTime.setText(Converter.convertTimeStampToString(messagesDTO.getTimestamp()));
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

        if (dataSet.get(position).getProfileImageUrl() != null) {
            new LoadImage(holder.userImageView).execute(dataSet.get(position).getProfileImageUrl());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("uid", dataSet.get(position).getUid());
                intent.putExtra("userName", dataSet.get(position).getUsername());
                intent.putExtra("userImageURL", dataSet.get(position).getProfileImageUrl());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
