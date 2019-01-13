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
import java.util.List;

public class SearchUserFragment extends Fragment {
    private static final String TAG = "SearchUserFragment";
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<UserDTO> userDTOList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        firebaseDatabase = firebaseDatabase.getInstance();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_search_user);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        userDTOList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(userDTOList);
        initDatabase();
        mRecyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreateView: ");

        return view;
    }

    private void initDatabase() {
        firebaseDatabase.getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                if (!userDTO.getEmail().equals(MainActivity.getCurrentUser().getEmail())) {
                    userDTOList.add(userDTO);
                }
                Log.d(TAG, "onChildAdded: " + userDTO.getUsername());
                adapter.notifyItemInserted(userDTOList.size() - 1);
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
