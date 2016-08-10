package com.tongchuang.perimetrypro.context;

import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.user.UserInfo;

/**
 * Created by qingdi on 8/5/16.
 */
public class GlobalContext {
    private static String deviceId;
    private static UserInfo userInfo;
    private static ExamSettings examSettings;

    public static String getDeviceId() {
        return deviceId;
    }

    public static void setDeviceId(String deviceId) {
        GlobalContext.deviceId = deviceId;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        GlobalContext.userInfo = userInfo;
    }

    public static ExamSettings getExamSettings() {
        return examSettings;
    }

    public static void setExamSettings(ExamSettings examSettings) {
        GlobalContext.examSettings = examSettings;
    }
}
