package com.pubak.econovation.amadium.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.adapter.ListViewAdapter;
import com.pubak.econovation.amadium.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class SearchUserFragment extends Fragment {

    private static final String TAG = "SearchUserFragment";
    private FirebaseDatabase firebaseDatabase;
    private List<UserDTO> userDTOs;
    private UserDTO userDTO;

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        firebaseDatabase = firebaseDatabase.getInstance();

        ListViewAdapter adapter;

        adapter = new ListViewAdapter();

        userDTOs = new ArrayList<>();

        mListView = (ListView) view.findViewById(R.id.listview_search_user);

        //mListView.setAdapter(adapter);
        initDatabase(adapter);

        Log.d("SearchUserFragment", "onCreateView: " +adapter.getCount());

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//            }
//        }, 2000);
//
//        System.out.println(userDTOs.size());
//        for (int i = 0; i < userDTOs.size(); i++) {
//            adapter.addItem(userDTOs.get(i).getProfileImageUrl(), userDTOs.get(i).getUsername(), userDTOs.get(i).getEmail());
//        }

        // 더미 데이터
//        adapter.addItem(null, "user1", "user1@gamil.com");
//        adapter.addItem(null, "user2", "user1@gamil.com");
//        adapter.addItem(null, "user3", "user1@gamil.com");
//        adapter.addItem(null, "user4", "user1@gamil.com");

        return view;
    }

    private void initDatabase(final ListViewAdapter adapter) {
        firebaseDatabase.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    adapter.addItem(snapshot.getValue(UserDTO.class).getProfileImageUrl(), snapshot.getValue(UserDTO.class).getUsername(), snapshot.getValue(UserDTO.class).getEmail());
                    userDTO = snapshot.getValue(UserDTO.class);
                    System.out.println("통과");
                    //adapter.addItem(userDTO.getProfileImageUrl(), userDTO.getUsername(), userDTO.getEmail());

                    Log.d(TAG, "onDataChange: ");
                    System.out.println(userDTO.getUsername());
                }


                mListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", "DB 접근 실패 in AdminPostFragment" + databaseError.getMessage());
            }
        });
    }
}
