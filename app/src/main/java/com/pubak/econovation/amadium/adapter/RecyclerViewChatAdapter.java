package com.pubak.econovation.amadium.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.MainActivity;
import com.pubak.econovation.amadium.dto.MessagesDTO;
import com.pubak.econovation.amadium.utils.LoadImage;

import java.util.List;

public class RecyclerViewChatAdapter extends RecyclerView.Adapter<RecyclerViewChatAdapter.ViewHolder> {
    private final String TAG = "RecyclerViewChatAdapter";
    private List<MessagesDTO> messagesDTOList;
    private static final int CHAT_BUBBLE_RIGHT = 1;
    private static final int CHAT_BUBBLE_LEFT = 2;
    private String userImageURL;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chat;
        public View mView;
        public ImageView userImage;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            chat = (TextView) view.findViewById(R.id.text);
            userImage = (ImageView) view.findViewById(R.id.userImage);
        }
    }

    public RecyclerViewChatAdapter(List<MessagesDTO> messagesDTOList, String userImageURL) {
        this.messagesDTOList = messagesDTOList;
        this.userImageURL = userImageURL;
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesDTOList.get(position).getFromId().equals(MainActivity.getCurrentUser().getUid())) {
            return CHAT_BUBBLE_RIGHT;
        } else {
            return CHAT_BUBBLE_LEFT;
        }
    }

    @Override
    public RecyclerViewChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == CHAT_BUBBLE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_left, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewChatAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+ messagesDTOList.get(position).getText());
        holder.chat.setText(messagesDTOList.get(position).getText());
        if (!messagesDTOList.get(position).getFromId().equals(MainActivity.getCurrentUser().getUid())) {
            new LoadImage(holder.userImage).execute(userImageURL);
        }
    }

    @Override
    public int getItemCount() {
        return messagesDTOList.size();
    }
}
