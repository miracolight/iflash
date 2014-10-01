package com.codepath.apps.basictwitter.helpers;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by qingdi on 9/26/14.
 */
public class DateHelper {
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    private static final long ONE_DAY = 1000*60*60*24;
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            if ((System.currentTimeMillis() - dateMillis)>ONE_DAY) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                relativeDate = sdf.format(sf.parse(rawJsonDate));
            } else {
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString()
                        .replace(" mins ago", "m").replace(" min ago", "m").replace(" hour ago", "h").replace(" hours ago", "h");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
