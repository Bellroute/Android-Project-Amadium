package com.pubak.econovation.amadium.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pubak.econovation.amadium.ListViewItem.SearchUserListViewItem;
import com.pubak.econovation.amadium.R;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
    private ArrayList<SearchUserListViewItem> searchUserListViewItemList = new ArrayList<>();

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
        TextView userEmailView = (TextView) convertView.findViewById(R.id.user_email);

        SearchUserListViewItem searchUserListViewItem = searchUserListViewItemList.get(position);


        try {
            URL url = new URL(searchUserListViewItem.getUserImageUrl());
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            userImageView.setImageBitmap(bm);
        } catch (Exception e) {
        }


        userNameView.setText(searchUserListViewItem.getUserName());
        userEmailView.setText(searchUserListViewItem.getUserEmail());

        return convertView;
    }

    public void addItem(String imageUrl, String name, String email) {
        SearchUserListViewItem item = new SearchUserListViewItem();

        item.setUserName(name);
        item.setUserImageUrl(imageUrl);
        item.setUserEmail(email);

        searchUserListViewItemList.add(item);
    }
}
