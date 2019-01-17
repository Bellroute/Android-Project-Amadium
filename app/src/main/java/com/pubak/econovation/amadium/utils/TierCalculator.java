package com.pubak.econovation.amadium.utils;

public class TierCalculator {
    private String[] data;
    private int win;
    private int tie;
    private int lose;

    public void setData(String data) {
        this.data = data.split("/");
        parseWin();
        parseTie();
        parseLose();
    }

    private void parseWin() {
        win = Integer.parseInt(data[0]);
    }

    private void parseTie() {
        tie = Integer.parseInt(data[1]);
    }

    private void parseLose() {
        lose = Integer.parseInt(data[2]);
    }

    public String getTier() {
        return String.valueOf((win - lose) / 3);
    }

    public int getWin() {
        return win;
    }

    public int getTie() {
        return tie;
    }

    public int getLose() {
        return lose;
    }

    public void win() {
        win += 1;
    }

    public void tie() {
        tie += 1;
    }

    public void lose() {
        lose += 1;
    }

    public String getWinTieLose() {
        return win + "/" + tie + "/" + lose;
    }
}
