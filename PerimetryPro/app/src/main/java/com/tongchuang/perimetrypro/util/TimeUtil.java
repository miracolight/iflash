package com.tongchuang.perimetrypro.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

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
