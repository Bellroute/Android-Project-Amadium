package com.pubak.econovation.amadium;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<SearchUserListViewItem> searchUserListViewItemsList = new ArrayList<SearchUserListViewItem>();

    public ListViewAdapter() {
    }


    @Override
    public int getCount() {
        return searchUserListViewItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchUserListViewItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_search_user, parent, false);
        }

        ImageView userImageView = (ImageView) convertView.findViewById(R.id.profile_image);
        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name);
        TextView userTierView = (TextView) convertView.findViewById(R.id.user_tier);

        SearchUserListViewItem searchUserListViewItem = searchUserListViewItemsList.get(position);

        userImageView.setImageDrawable(searchUserListViewItem.getUserImage());
        userNameView.setText(searchUserListViewItem.getUserName());
        userTierView.setText(searchUserListViewItem.getUserTier());

        return convertView;
    }

    public void addItem(Drawable userImage, String userName, String userTier) {
        SearchUserListViewItem item = new SearchUserListViewItem();

        item.getUserImage();
        item.getUserName();
        item.getUserTier();

        searchUserListViewItemsList.add(item);
    }
}
