package com.pubak.econovation.amadium;

import android.graphics.drawable.Drawable;

public class SearchUserListViewItem {
    private Drawable userImage;
    private String userName;
    private String userTier;

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

    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }
}
