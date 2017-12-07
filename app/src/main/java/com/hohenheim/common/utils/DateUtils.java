package com.hohenheim.common.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by com.hohenheim on 2017/10/2.
 */

public class DateUtils {

    public static final String YEAR_TO_DATE = "yyyy-MM-dd";
    public static final String HOUR_TO_SECOND = "HH:mm:ss";

    public static String getYearToDate() {
        return new SimpleDateFormat(YEAR_TO_DATE, Locale.CHINA).format(System.currentTimeMillis());
    }

    public static String getHourToSecond() {
        return new SimpleDateFormat(HOUR_TO_SECOND, Locale.CHINA).format(System.currentTimeMillis());
    }
}
