package com.pubak.econovation.amadium.utils;

import java.text.SimpleDateFormat;

public class Converter {

    public static double getCurrentTimeStamp() {
        double millis = System.currentTimeMillis();
        return millis / 1000;
    }

    public static String convertTimeStampToString(double timeStamp) {
        return new SimpleDateFormat("yyyy.MM.dd hh:mm a").format(timeStamp * 1000);
    }
}
