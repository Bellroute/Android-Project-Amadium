package com.pubak.econovation.amadium.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.pubak.econovation.amadium.adapter.RecyclerViewRankListAdapter;
import com.pubak.econovation.amadium.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class RankListFragment extends Fragment {
    private final String TAG = "RankListFragment";
    private RecyclerView recyclerView;
    private RecyclerViewRankListAdapter adapter;
    private List<UserDTO> userDTOList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_list, container, false);
        userDTOList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_rank_list);

        initDatabase();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerViewRankListAdapter(userDTOList);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void initDatabase() {
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                userDTO.setUid(dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded: " + userDTO.getUsername());
                userDTOList.add(userDTO);
                adapter.notifyItemInserted(userDTOList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                Log.d(TAG, "onChildAdded: " + userDTO.getUsername());
                userDTOList.add(userDTO);
                adapter.notifyItemInserted(userDTOList.size() - 1);
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
