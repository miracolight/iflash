package com.tongchuang.perimetrypro.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static final int LONG_DELAY = 3510; // 3.5 seconds
    public static final int SHORT_DELAY = 2010; // 2 seconds

    /**
     * 将千毫秒格式化为yyyy-MM-dd HH:mm格式
     * 
     * @param millisec
     * @return
     */
    public static String formatDate(long millisec) {
        Date date = new Date(millisec);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        return sdf.format(date);
    }
}
