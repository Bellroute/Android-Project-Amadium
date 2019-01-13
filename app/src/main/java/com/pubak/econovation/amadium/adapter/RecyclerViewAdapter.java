package com.pubak.econovation.amadium.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.utils.LoadImage;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<UserDTO> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameView;
        public TextView userEmailView;
        public ImageView userImageView;

        public ViewHolder(View view) {
            super(view);
            userImageView = (ImageView) view.findViewById(R.id.user_image);
            userNameView = (TextView) view.findViewById(R.id.user_name);
            userEmailView = (TextView) view.findViewById(R.id.user_email);
        }
    }

    public RecyclerViewAdapter(List<UserDTO> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_search_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.userNameView.setText(dataSet.get(position).getUsername());
        holder.userEmailView.setText(dataSet.get(position).getEmail());

        if (dataSet.get(position).getProfileImageUrl() != null) {
            new LoadImage(holder.userImageView).execute(dataSet.get(position).getProfileImageUrl());
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
