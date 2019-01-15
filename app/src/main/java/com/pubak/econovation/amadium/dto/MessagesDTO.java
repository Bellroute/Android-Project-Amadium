package com.pubak.econovation.amadium.dto;

public class MessagesDTO {
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
}
