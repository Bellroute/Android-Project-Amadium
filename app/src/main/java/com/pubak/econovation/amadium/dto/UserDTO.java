package com.pubak.econovation.amadium.dto;

import android.support.annotation.NonNull;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

public class UserDTO implements Comparable<UserDTO>{
    private String username;
    private String profileImageUrl;
    private String email;
    private String sport;
    private String tier;
    private double latitude;
    private double longitude;
    private String winTieLose;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWinTieLose() {
        Log.d(TAG, "getWinTieLose: " + winTieLose);
        return winTieLose;
    }

    public void setWinTieLose(String winTieLose) {
        this.winTieLose = winTieLose;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(@NonNull UserDTO o) {
        if (Integer.parseInt(this.tier) > Integer.parseInt(o.tier)) {
            return 1;
        } else if (Integer.parseInt(this.tier) < Integer.parseInt(o.tier)) {
            return -1;
        } else {
            return 0;
        }
    }
}
