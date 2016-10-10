package com.tongchuang.perimetrypro.perimetry.settings;

import android.app.Activity;

import com.tongchuang.perimetrypro.user.UserInfo;
import com.tongchuang.perimetrypro.user.UserServiceResponseHandler;

/**
 * Created by qingdi on 8/5/16.
 */
public interface SettingService {
    public static final String SHARED_PREF_NAME = "PERIMETRY_SETTINGS";
    public static final String  PROP_NAME_DEVICE_ID = "DEVICE_ID";
    public static final String  PROP_NAME_DEVICE_SETTINGS = "DEVICE_SETTINGS";
    public static final String  PROP_NAME_DEVICE_SETTINGS_LAST_UPDATE_DATE = "DEVICE_SETTINGS_LAST_UPDATE_DATE";
    public static final String  PROP_NAME_DEFAULT_PATIENT_SETTINGS = "DEFAULT_PATIENT_SETTINGS";
    public static final String  PROP_NAME_DEFAULT_PATIENT_SETTINGS_LAST_UPDATE_DATE = "DEFAULT_PATIENT_SETTINGS_LAST_UPDATE_DATE";
    public void loadExamSettings(Activity activity, String deviceId, UserInfo userInfo, SettingServiceResponseHandler responseHandler);
}
