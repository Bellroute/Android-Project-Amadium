package com.pubak.econovation.amadium.ListViewItem;

import android.graphics.drawable.Drawable;

public class SearchUserListViewItem {
    private Drawable userImage;
    private String userName;
    private String userLocation;

    public Drawable getUserImage() {
        return userImage;
    }

    public void setUserImage(Drawable userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }
}
