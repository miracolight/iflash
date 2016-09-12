package com.tongchuang.perimetrypro.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tongchuang.perimetrypro.R;
import com.tongchuang.perimetrypro.context.GlobalContext;
import com.tongchuang.perimetrypro.perimetry.settings.DeviceSettings;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.settings.PatientSettings;
import com.tongchuang.perimetrypro.perimetry.settings.SettingService;
import com.tongchuang.perimetrypro.perimetry.settings.SettingServiceResponseHandler;
import com.tongchuang.perimetrypro.perimetry.settings.impl.SettingServiceImpl;
import com.tongchuang.perimetrypro.user.UserInfo;
import com.tongchuang.perimetrypro.user.UserService;
import com.tongchuang.perimetrypro.user.UserServiceResponseHandler;
import com.tongchuang.perimetrypro.user.impl.UserServiceImpl;
import com.tongchuang.perimetrypro.util.ActivityUtil;
import com.tongchuang.perimetrypro.util.TimeUtil;

import java.util.concurrent.TimeUnit;

public class SigninActivity extends AppCompatActivity {

    public static final int     PATIENT_MAIN_REQUEST_CODE = 1;
    private UserService userService = new UserServiceImpl();
    private SettingService settingService = new SettingServiceImpl();
    private boolean scannerOn = false;
    private boolean processingBarcode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("in onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        GlobalContext.setDeviceId(ActivityUtil.getDeviceID(this));
    }


    @Override
    protected void onResume() {
        System.out.println("in onResume...");
        super.onResume();

        if (!scannerOn && !processingBarcode) {
            initScan();
        }
    }

    // go to the scan mode
    public synchronized void initScan() {
        if (!scannerOn && !processingBarcode) {
            scannerOn = true;
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    // process scan result
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("SigninActivity onActivityResult");
        if (requestCode == PATIENT_MAIN_REQUEST_CODE) {
            System.out.println("SigninActivity onActivityResult before killProcess");
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {//back from scanning barcode
            scannerOn = false;
            processingBarcode = true;
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            if (scanningResult != null) {
                String barCode = scanningResult.getContents();
                System.out.println("barcode: "+barCode);
                if (barCode !=null) {
                    loadUserSettings(barCode);
                    return;
                }
            }
            onSigninFailure();
        }

    }

    private void loadUserSettings(String barCode) {
        userService.getUserInfo(barCode, new UserServiceResponseHandler() {
            @Override
            public void onFailure() {
                onSigninFailure();
            }

            @Override
            public void onSuccess(UserInfo userInfo) {
                GlobalContext.setUserInfo(userInfo);
                loadExamSettings(userInfo);
            }
        });
    }

    private void loadExamSettings(UserInfo userInfo) {

        settingService.loadExamSettings(this, GlobalContext.getDeviceId(), userInfo,
                new SettingServiceResponseHandler() {
                    DeviceSettings deviceSettings = null;
                    PatientSettings patientSettings = null;
                    boolean deviceDone;
                    boolean patientDone;

                    @Override
                    public void onDeviceSettingFailure(DeviceSettings deviceSettings) {
                        deviceDone = true;
                        this.deviceSettings = deviceSettings;
                        onFinish();
                    }

                    @Override
                    public void onDeviceSuccess(DeviceSettings deviceSettings) {
                        deviceDone = true;
                        this.deviceSettings = deviceSettings;
                        onFinish();
                    }

                    @Override
                    public void onPatientSettingFailure(PatientSettings patientSettings) {
                        patientDone = true;
                        this.patientSettings = patientSettings;
                        onFinish();
                    }

                    @Override
                    public void onPatientSettingsSuccess(PatientSettings patientSettings) {
                        patientDone = true;
                        this.patientSettings = patientSettings;
                        onFinish();
                    }

                    private synchronized void onFinish() {
                        if (deviceDone && patientDone) {
                            if (deviceSettings == null || patientSettings == null){
                                onSigninFailure();
                            } else {
                                processingBarcode = false;
                                GlobalContext.setExamSettings(new ExamSettings(deviceSettings, patientSettings));
                                if (GlobalContext.getUserInfo().getPatientId() != null) {
                                    Intent i = new Intent(SigninActivity.this, PatientMainActivity.class);
                                    startActivityForResult(i, PATIENT_MAIN_REQUEST_CODE);
                                } else {
                                    Intent i = new Intent(SigninActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }

                        }
                    }
                });
    }

    private void onSigninFailure() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "请扫用户二维码登录!", Toast.LENGTH_SHORT);
        toast.show();
        processingBarcode = false;
        if (!scannerOn && !processingBarcode) {
            initScan();
        }
    }

}
