package com.pubak.econovation.amadium.utils;

import android.support.annotation.NonNull;

import com.pubak.econovation.amadium.dto.UserDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingSorter {
    private List<NewUserDTO> newUserDTOList;

    public class NewUserDTO implements Comparable<NewUserDTO> {
        private String username;
        private String profileImageUrl;
        private String email;
        private String sport;
        private String tier;
        private double latitude;
        private double longitude;
        private String winTieLose;
        private String uid;

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

        public String getWinTieLose() {
            return winTieLose;
        }

        public void setWinTieLose(String winTieLose) {
            this.winTieLose = winTieLose;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        @Override
        public int compareTo(@NonNull NewUserDTO o) {
            if (Integer.parseInt(this.tier) > Integer.parseInt(o.tier)) {
                return 1;
            } else if (Integer.parseInt(this.tier) < Integer.parseInt(o.tier)) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public RankingSorter(List<UserDTO> userDTOList, List<String> uidList) {
        newUserDTOList = new ArrayList();

        for (int i = 0; i < userDTOList.size(); i++) {
            NewUserDTO newUserDTO = new NewUserDTO();
            newUserDTO.setUsername(userDTOList.get(i).getUsername());
            newUserDTO.setProfileImageUrl(userDTOList.get(i).getProfileImageUrl());
            newUserDTO.setEmail(userDTOList.get(i).getEmail());
            newUserDTO.setSport(userDTOList.get(i).getSport());
            newUserDTO.setTier(userDTOList.get(i).getTier());
            newUserDTO.setWinTieLose(userDTOList.get(i).getWinTieLose());
            newUserDTO.setLatitude(userDTOList.get(i).getLatitude());
            newUserDTO.setLongitude(userDTOList.get(i).getLongitude());
            newUserDTO.setUid(uidList.get(i));

            newUserDTOList.add(newUserDTO);
        }
    }

    public List<NewUserDTO> sortData() {
        Collections.sort(newUserDTOList);
        return newUserDTOList;
    }
}
