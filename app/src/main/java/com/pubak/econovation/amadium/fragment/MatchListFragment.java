package com.pubak.econovation.amadium.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.MainActivity;
import com.pubak.econovation.amadium.adapter.RecyclerViewAdapter;
import com.pubak.econovation.amadium.dto.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchListFragment extends Fragment {
    private static final String TAG = "MatchListFragment";
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<UserDTO> userDTOList;
    private List<String> uidList;
    private List<String> chatUidList;
    private Map<String, List<String>> chatUidListPerUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        firebaseDatabase = firebaseDatabase.getInstance();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_match_list);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        uidList = new ArrayList<>();
        userDTOList = new ArrayList<>();
        chatUidList = new ArrayList<>();
        chatUidListPerUid = new HashMap<>();
        initDatabase();


        adapter = new RecyclerViewAdapter(this.getActivity(), userDTOList, uidList, chatUidListPerUid);

        mRecyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreateView: " + userDTOList);
        Log.d(TAG, "onCreateView: " + uidList);
        Log.d(TAG, "onCreateView: " + chatUidListPerUid);

        return view;
    }

    private void initDatabaseList(final String uId) {
        firebaseDatabase.getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                if (uId.equals(dataSnapshot.getKey()) && !userDTO.getEmail().equals(MainActivity.getCurrentUser().getEmail())) {
                    userDTOList.add(userDTO);
                    uidList.add(uId);
                }
                adapter.notifyItemInserted(userDTOList.size() - 1);
                Log.d(TAG, "onChildAdded: " + userDTO.getUsername());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                if (uId.equals(dataSnapshot.getKey()) && !userDTO.getEmail().equals(MainActivity.getCurrentUser().getEmail())) {
                    userDTOList.add(userDTO);
                    uidList.add(uId);
                }
                adapter.notifyItemInserted(userDTOList.size() - 1);
                Log.d(TAG, "onChildAdded: " + userDTO.getUsername());
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
        firebaseDatabase.getReference().child("user-messages").child(MainActivity.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "initDatabase: " + dataSnapshot.getValue());
                initDatabaseList(dataSnapshot.getKey());
                initChatDatabase(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "initDatabase: " + dataSnapshot.getValue());
                initDatabaseList(dataSnapshot.getKey());
                initChatDatabase(dataSnapshot.getKey());
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

    private void initChatDatabase(final String uid) {
        Log.d(TAG, "initChatDatabase: a");
        firebaseDatabase.getReference().child("user-messages").child(MainActivity.getCurrentUser().getUid()).child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: a" + dataSnapshot.getKey());
                chatUidList.add(dataSnapshot.getKey());
                chatUidListPerUid.put(uid, chatUidList);
                Log.d(TAG, "initChatDatabase: a" + chatUidListPerUid);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: a" + dataSnapshot.getKey());
                chatUidList.add(dataSnapshot.getKey());
                chatUidListPerUid.put(uid, chatUidList);
                Log.d(TAG, "initChatDatabase: a" + chatUidListPerUid);
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
