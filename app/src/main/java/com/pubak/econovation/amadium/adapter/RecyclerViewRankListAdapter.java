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
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.MatchUserActivity;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.utils.LoadImage;

import java.util.Collections;
import java.util.List;

public class RecyclerViewRankListAdapter extends RecyclerView.Adapter<RecyclerViewRankListAdapter.ViewHolder> {
    private final String TAG = "RecyclerViewFindUserAdapter";
    private List<UserDTO> userDTOList;

    public RecyclerViewRankListAdapter(List<UserDTO> userDTOList) {
        this.userDTOList = userDTOList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rank_list, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Collections.sort(userDTOList);
        Collections.reverse(userDTOList);
        Log.d(TAG, "onBindViewHolder: " + userDTOList.get(position).getUsername());
        holder.userName.setText(userDTOList.get(position).getUsername());
        holder.userTier.setText("티어: " + userDTOList.get(position).getTier());
        holder.rank.setText(String.valueOf(position + 1));
        new LoadImage(holder.userImage).execute(userDTOList.get(position).getProfileImageUrl());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: dataSet: " + userDTOList.get(position));
                Intent intent = new Intent(v.getContext(), MatchUserActivity.class);
                intent.putExtra("uid", userDTOList.get(position).getUid());
                intent.putExtra("email", userDTOList.get(position).getEmail());
                intent.putExtra("userName", userDTOList.get(position).getUsername());
                intent.putExtra("userSports", "스포츠: " + userDTOList.get(position).getSport());
                intent.putExtra("userTier", "티어: " + userDTOList.get(position).getTier());
                intent.putExtra("userWinTieLose", "전적(승/무/패): " + userDTOList.get(position).getWinTieLose());
                intent.putExtra("userImage", "전적(승/무/패): " + userDTOList.get(position).getProfileImageUrl());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDTOList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView userTier;
        private ImageView userImage;
        private TextView rank;
        private View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            userName = (TextView) itemView.findViewById(R.id.user_name_find);
            userTier = (TextView) itemView.findViewById(R.id.user_email_find);
            userImage = (ImageView) itemView.findViewById(R.id.user_image_find);
            rank = (TextView) itemView.findViewById(R.id.rank);

        }
    }
}

