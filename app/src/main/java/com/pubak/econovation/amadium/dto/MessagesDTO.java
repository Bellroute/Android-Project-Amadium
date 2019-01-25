package com.pubak.econovation.amadium.dto;

import android.support.annotation.NonNull;

public class MessagesDTO implements Comparable<MessagesDTO> {
    private String fromId;
    private String text;
    private double timestamp;
    private String told;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getTold() {
        return told;
    }

    public void setTold(String told) {
        this.told = told;
    }

    @Override
    public int compareTo(@NonNull MessagesDTO o) {
            if (this.timestamp > o.timestamp) {
                return 1;
            } else if (this.timestamp < o.timestamp) {
                return -1;
            } else {
                return 0;
            }
    }
}
