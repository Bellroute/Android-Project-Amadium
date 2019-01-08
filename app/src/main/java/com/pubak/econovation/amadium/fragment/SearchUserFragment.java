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

public class SearchUserFragment extends Fragment {
    private static final String TAG = "SearchUserFragment";
    private FirebaseDatabase firebaseDatabase;
    private UserDTO userDTO;

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        ListViewAdapter adapter = new ListViewAdapter();
        firebaseDatabase = firebaseDatabase.getInstance();

        mListView = (ListView) view.findViewById(R.id.listview_search_user);

        initDatabase(adapter);

        Log.d("SearchUserFragment", "onCreateView: " +adapter.getCount());

        return view;
    }

    private void initDatabase(final ListViewAdapter adapter) {
        firebaseDatabase.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userDTO = snapshot.getValue(UserDTO.class);
                    adapter.addItem(userDTO.getProfileImageUrl(), userDTO.getUsername(), userDTO.getEmail());
                    Log.d(TAG, "onDataChange: ");
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
