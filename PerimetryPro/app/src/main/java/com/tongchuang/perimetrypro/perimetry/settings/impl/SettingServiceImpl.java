package com.tongchuang.perimetrypro.perimetry.settings.impl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tongchuang.perimetrypro.network.VisionRestClient;
import com.tongchuang.perimetrypro.perimetry.pattern.PatternGenerator;
import com.tongchuang.perimetrypro.perimetry.settings.DeviceSettings;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.settings.PatientSettings;
import com.tongchuang.perimetrypro.perimetry.settings.SettingService;
import com.tongchuang.perimetrypro.perimetry.settings.SettingServiceResponseHandler;
import com.tongchuang.perimetrypro.user.UserInfo;

import cz.msebera.android.httpclient.Header;

/**
 * Created by qingdi on 8/9/16.
 */
public class SettingServiceImpl implements SettingService {

    private Gson gson = new Gson();

    private static long REFRESH_INTERVAL = 1000*3600*24; // re-query device settings if the cached value is older than 1 days.
    @Override
    public void loadExamSettings(Activity activity, String deviceId, UserInfo userInfo, final SettingServiceResponseHandler responseHandler) {

        // load deviceSettings
        SharedPreferences sharedPref = activity.getSharedPreferences(SettingService.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        long deviceSettingsLastUpdateDate = sharedPref.getLong(SettingService.PROP_NAME_DEVICE_SETTINGS_LAST_UPDATE_DATE, 0l);

        String valueCached = sharedPref.getString(SettingService.PROP_NAME_DEVICE_SETTINGS, null);
        final DeviceSettings deviceSettingsCached = (valueCached == null)?null:gson.fromJson(valueCached, DeviceSettings.class);

        // re-query device settings from server if the cached value is older than 1 days
        if (deviceSettingsCached == null ||System.currentTimeMillis()-deviceSettingsLastUpdateDate > REFRESH_INTERVAL) {
            String url = "/devices/"+deviceId+"/examsettings?apiKey=rock2016";
            VisionRestClient.get(url, null, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    t.printStackTrace();
                    responseHandler.onDeviceSettingFailure(deviceSettingsCached);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                    DeviceSettings deviceSettings = gson.fromJson(response.toString(), DeviceSettings.class);
                    responseHandler.onDeviceSuccess(deviceSettings);
                }
            });
        } else {
            responseHandler.onDeviceSuccess(deviceSettingsCached);
        }


        if (userInfo.getPatientId() == null) {
            long defaultPatientSettingsLastUpdateDate = sharedPref.getLong(SettingService.PROP_NAME_DEFAULT_PATIENT_SETTINGS_LAST_UPDATE_DATE, 0l);

            valueCached = sharedPref.getString(SettingService.PROP_NAME_DEFAULT_PATIENT_SETTINGS, null);
            final PatientSettings defaultPatientSettingsCached = (valueCached == null)?null:gson.fromJson(valueCached, PatientSettings.class);

            // re-query default patient settings from server if the cached value is older than 1 days
            if (defaultPatientSettingsCached == null ||System.currentTimeMillis()-deviceSettingsLastUpdateDate > REFRESH_INTERVAL) {
                String url = "/defaults/examsettings?apiKey=rock2016";
                VisionRestClient.get(url, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        t.printStackTrace();
                        responseHandler.onPatientSettingFailure(defaultPatientSettingsCached);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                        PatientSettings patientSettings = gson.fromJson(response.toString(), PatientSettings.class);
                        responseHandler.onPatientSettingsSuccess(patientSettings);
                    }
                });
            } else {
                responseHandler.onPatientSettingsSuccess(defaultPatientSettingsCached);
            }

        } else if (userInfo.getPatientId() != null) {
            String url = "/patients/"+userInfo.getPatientId()+"/examsettings?apiKey=rock2016";
            // trigger the network request
            VisionRestClient.get(url, null, new JsonHttpResponseHandler() {
                // define the success and failure callbacks
                // handle the successful response (popular photos JSON)
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    t.printStackTrace();
                    responseHandler.onPatientSettingFailure(null);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                    PatientSettings patientSettings = gson.fromJson(response.toString(), PatientSettings.class);
                    responseHandler.onPatientSettingsSuccess(patientSettings);
                }
            });
        }

    }
}
