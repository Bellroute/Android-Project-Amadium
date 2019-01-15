package com.pubak.econovation.amadium.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.ChatActivity;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.fragment.MatchListFragment;
import com.pubak.econovation.amadium.utils.LoadImage;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<UserDTO> dataSet;
    private Context context;
    private List<String> uidList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameView;
        public TextView userEmailView;
        public ImageView userImageView;
        public View mView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            userImageView = (ImageView) view.findViewById(R.id.user_image);
            userNameView = (TextView) view.findViewById(R.id.user_name);
            userEmailView = (TextView) view.findViewById(R.id.user_email);
        }
    }

    public RecyclerViewAdapter(Context context, List<UserDTO> dataSet, List<String> uidList) {
        this.dataSet = dataSet;
        this.context = context;
        this.uidList = uidList;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_match_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.userNameView.setText(dataSet.get(position).getUsername());
        holder.userEmailView.setText(dataSet.get(position).getEmail());

        if (dataSet.get(position).getProfileImageUrl() != null) {
            new LoadImage(holder.userImageView).execute(dataSet.get(position).getProfileImageUrl());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("uid", uidList.get(position));
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
