package com.pubak.econovation.amadium.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.adapter.RecyclerViewFindUserAdapter;
import com.pubak.econovation.amadium.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class FindUserActivity extends AppCompatActivity {
    private final String TAG = "FindUserActivity";
    private RecyclerView recyclerView;
    private EditText editTextFind;
    private TextView textViewCancel;
    private List<UserDTO> userDTOList;
    private RecyclerViewFindUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        initDatabase();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_find_user);
        editTextFind = (EditText) findViewById(R.id.editText_find);
        textViewCancel = (TextView) findViewById(R.id.textView_cancel);

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userDTOList = new ArrayList<>();
        adapter = new RecyclerViewFindUserAdapter(userDTOList);

        recyclerView.setAdapter(adapter);

        editTextFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        List<UserDTO> filteredUsers = new ArrayList<>();

        //looping through existing elements
        for (UserDTO userDTO : userDTOList) {
            //if the existing elements contains the search input
            if (userDTO.getEmail().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filteredUsers.add(userDTO);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filteredUsers);
    }

    private void initDatabase() {
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
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
