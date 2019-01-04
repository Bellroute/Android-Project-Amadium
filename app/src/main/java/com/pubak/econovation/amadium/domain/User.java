package com.pubak.econovation.amadium.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String userName;
    public String profileUserUrl;
    public String uId;
    public String userId;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String userImageUrl, String uId, String userId) {
        this.userName = userName;
        this.profileUserUrl = userImageUrl;
        this.uId = uId;
        this.userId = userId;
    }
}
