package com.pubak.econovation.amadium.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.dto.MessagesDTO;

import java.util.List;

public class RecyclerViewChatAdapter extends RecyclerView.Adapter<RecyclerViewChatAdapter.ViewHolder> {
    private final List<MessagesDTO> messagesDTOList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chat;
        public View mView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            chat = (TextView) view.findViewById(R.id.text);
        }
    }

    public RecyclerViewChatAdapter(List<MessagesDTO> messagesDTOList) {
        this.messagesDTOList = messagesDTOList;
    }

    @Override
    public RecyclerViewChatAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewChatAdapter.ViewHolder holder, int position) {
        holder.chat.setText(messagesDTOList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return messagesDTOList.size();
    }
}
