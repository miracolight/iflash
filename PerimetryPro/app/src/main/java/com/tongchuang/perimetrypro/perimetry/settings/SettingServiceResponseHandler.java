package com.tongchuang.perimetrypro.perimetry.settings;

import com.tongchuang.perimetrypro.user.UserInfo;

/**
 * Created by qingdi on 8/9/16.
 */
public interface SettingServiceResponseHandler {

    public void onDeviceSettingFailure(DeviceSettings deviceSettingsCached);

    public void onDeviceSuccess(DeviceSettings deviceSettings);

    void onPatientSettingFailure(PatientSettings defaultPatientSettingsCached);

    void onPatientSettingsSuccess(PatientSettings defaultPatientSettingsCached);
}
