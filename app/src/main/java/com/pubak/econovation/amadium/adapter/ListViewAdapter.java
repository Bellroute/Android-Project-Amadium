package com.pubak.econovation.amadium.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pubak.econovation.amadium.ListViewItem.SearchUserListViewItem;
import com.pubak.econovation.amadium.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
    private ArrayList<SearchUserListViewItem> searchUserListViewItemList = new ArrayList<SearchUserListViewItem>();

    public ListViewAdapter() {

    }

    @Override
    public int getCount() {
        return searchUserListViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchUserListViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_search_user, parent, false);
        }

        ImageView userImageView = (ImageView) convertView.findViewById(R.id.user_image);
        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name);
        TextView userLocationView = (TextView) convertView.findViewById(R.id.user_location);

        SearchUserListViewItem searchUserListViewItem = searchUserListViewItemList.get(position);

        userImageView.setImageDrawable(searchUserListViewItem.getUserImage());
        userNameView.setText(searchUserListViewItem.getUserName());
        userLocationView.setText(searchUserListViewItem.getUserLocation());

        return convertView;
    }

    public void addItem(Drawable image, String name, String location) {
        SearchUserListViewItem item = new SearchUserListViewItem();

        item.setUserName(name);
        item.setUserImage(image);
        item.setUserLocation(location);

        searchUserListViewItemList.add(item);
    }
}
