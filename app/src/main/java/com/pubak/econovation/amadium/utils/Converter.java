package com.pubak.econovation.amadium.utils;

public class Converter {

    public static double getCurrentTimeStamp() {
        double millis = System.currentTimeMillis();
        return millis / 1000;
    }
}
