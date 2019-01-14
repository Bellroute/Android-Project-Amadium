package com.pubak.econovation.amadium.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.utils.LoadImage;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<UserDTO> dataSet;
    private Context context;

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

    public RecyclerViewAdapter(Context context, List<UserDTO> dataSet) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_search_user, parent, false);
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
                String[] items = {"예", "아니오"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("경기를 신청하시겠습니까?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast.makeText(context, "yes", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(context, "no", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
