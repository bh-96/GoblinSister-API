package com.kims.goblinsis.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtil {

    public static Date getFormatDate(String format, Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            String formatDate = sdf.format(date);
            return sdf.parse(formatDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getFormatDateByString(String format, String date) {
        Date d = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            d = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    public static String getFormatStringByDate(String format, Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String beforeOrAfterDaysDate(String format, int day, Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, day);
            date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
