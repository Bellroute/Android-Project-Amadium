package com.pubak.econovation.amadium.adapter;

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

public class RecyclerViewFindUserAdapter extends RecyclerView.Adapter<RecyclerViewFindUserAdapter.ViewHolder>{
    private List<UserDTO> dataSet;

    public RecyclerViewFindUserAdapter(List<UserDTO> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_find_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(dataSet.get(position).getUsername());
        holder.userEmail.setText(dataSet.get(position).getEmail());
        new LoadImage(holder.userImage).execute(dataSet.get(position).getProfileImageUrl());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView userEmail;
        private ImageView userImage;

        ViewHolder(View itemView) {
            super(itemView);

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
