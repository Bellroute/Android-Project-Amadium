package com.pubak.econovation.amadium.utils;

public class DataParser {
    public static String parseWin(String tierData) {
        return splitData(tierData)[0];
    }

    public static String parseTie(String tierData) {
        return splitData(tierData)[1];
    }

    public static String parseLose(String tierData) {
        return splitData(tierData)[2];
    }

    private static String[] splitData(String tierData) {
        return tierData.split("/");
    }
}
