package com.pubak.econovation.amadium.dto;

public class UserDTO {
    private String username = "";
    private String profileImageUrl = "";
    private String email = "";
    private String sport = "스포츠 선택 필요";
    private String tier = "플레이를 해야 합니다.";
    private double latitude;
    private double longitude;
    private String winTieLose;

    public String getWinTieLose() {
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
}
