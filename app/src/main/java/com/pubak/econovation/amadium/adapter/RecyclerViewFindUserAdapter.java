package com.pubak.econovation.amadium.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.ChatActivity;
import com.pubak.econovation.amadium.activity.MatchUserActivity;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.utils.LoadImage;
import java.util.List;

public class RecyclerViewFindUserAdapter extends RecyclerView.Adapter<RecyclerViewFindUserAdapter.ViewHolder>{
    private final String TAG = "RecyclerViewFindUserAdapter";
    private List<UserDTO> dataSet;
    private List<String> uidList;

    public RecyclerViewFindUserAdapter(List<UserDTO> dataSet, List<String> uidList) {
        this.dataSet = dataSet;
        this.uidList = uidList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_find_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.userName.setText(dataSet.get(position).getUsername());
        holder.userEmail.setText(dataSet.get(position).getEmail());
        new LoadImage(holder.userImage).execute(dataSet.get(position).getProfileImageUrl());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: dataSet: " + dataSet.get(position));
                Intent intent = new Intent(v.getContext(), MatchUserActivity.class);
                intent.putExtra("uid", uidList.get(position));
                intent.putExtra("email", dataSet.get(position).getEmail());
                intent.putExtra("userName", dataSet.get(position).getUsername());
                intent.putExtra("userSports", "스포츠: " + dataSet.get(position).getSport());
                intent.putExtra("userTier", "티어: " + dataSet.get(position).getTier());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView userEmail;
        private ImageView userImage;
        private View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            userName = (TextView) itemView.findViewById(R.id.user_name_find);
            userEmail = (TextView) itemView.findViewById(R.id.user_email_find);
            userImage = (ImageView) itemView.findViewById(R.id.user_image_find);
        }
    }

    public void filterList(List<UserDTO> filteredUsers) {
        dataSet = filteredUsers;
        notifyDataSetChanged();
    }

}
