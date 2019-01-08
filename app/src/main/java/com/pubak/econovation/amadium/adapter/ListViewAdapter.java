package com.pubak.econovation.amadium.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pubak.econovation.amadium.ListViewItem.SearchUserListViewItem;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.dto.UserDTO;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<SearchUserListViewItem> searchUserListViewItemList = new ArrayList<>();
    final int CONN_TIME = 5000;
    private String TAG = "ListViewAdapter : ";
    private ImageView userImageView;
    private TextView userNameView;
    private TextView userEmailView;
    private Bitmap myBitmap;

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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_search_user, parent, false);
        }

        userImageView = (ImageView) convertView.findViewById(R.id.user_image);
        userNameView = (TextView) convertView.findViewById(R.id.user_name);
        userEmailView = (TextView) convertView.findViewById(R.id.user_email);

        userImageView.setBackground(new ShapeDrawable(new OvalShape()));
        userImageView.setClipToOutline(true);

        SearchUserListViewItem searchUserListViewItem = searchUserListViewItemList.get(position);


        new LoadImage().execute(searchUserListViewItem.getUserImageUrl());
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

    public void addItemAll(List<UserDTO> userDTOs) {
        SearchUserListViewItem item = new SearchUserListViewItem();

        for (UserDTO userDTO : userDTOs) {
            item.setUserName(userDTO.getUsername());
            item.setUserImageUrl(userDTO.getProfileImageUrl());
            item.setUserEmail(userDTO.getEmail());

            searchUserListViewItemList.add(item);
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
        }

        protected Bitmap doInBackground(String... args) {
            try {
                myBitmap = BitmapFactory
                        .decodeStream((InputStream) new URL(args[0])
                                .getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return myBitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if (image != null) {
                userImageView.setImageBitmap(image);
            } else {
            }
        }
    }
}
