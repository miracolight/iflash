package com.tongchuang.perimetrypro.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
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

public class MainActivity extends AppCompatActivity {

    private UserService         userService = new UserServiceImpl();
    private SettingService      settingService = new SettingServiceImpl();
    private ProgressDialog      progressDialog;
    private boolean             readyForExam = false;
    private boolean             examStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("in onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        GlobalContext.setDeviceId(ActivityUtil.getDeviceID(this));
        initScan();
    }

    @Override
    protected void onStart() {
        System.out.println("in onStart...");
        super.onStart();
        initScan();
    }

    @Override
    protected void onResume() {
        System.out.println("in onResume...");
        super.onResume();
        initScan();
    }

    // go to the scan mode
    public void initScan() {
        readyForExam = false;
        examStarted = false;
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    // process scan result
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        progressDialog.setMessage("用户登录...");
        progressDialog.show();

        if (scanningResult != null) {
            String barCode = scanningResult.getContents();

            userService.getUserInfo(barCode, new UserServiceResponseHandler() {
                @Override
                public void onFailure() {
                    onSetupFailure();
                }

                @Override
                public void onSuccess(UserInfo userInfo) {
                    progressDialog.hide();
                    GlobalContext.setUserInfo(userInfo);
                    loadExamSettings(userInfo);
                }
            });
        } else {
            onSetupFailure();
        }

    }

    private void loadExamSettings(UserInfo userInfo) {
        progressDialog.setMessage("系统准备中...");
        progressDialog.show();


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
                            progressDialog.hide();
                            if (deviceSettings == null || patientSettings == null){
                                onSetupFailure();
                            } else {
                                GlobalContext.setExamSettings(new ExamSettings(deviceSettings, patientSettings));
                                readyForExam = true;
                            }

                        }
                    }
                });
    }

    private void onSetupFailure() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "发生错误!", Toast.LENGTH_SHORT);
        toast.show();
        initScan();
    }

    // go to Configuration page
    public void onConfig(View v) {

        Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
    }

    // go to Results page
    public void onResults(View v) {

        Intent i = new Intent(this, ResultsActivity.class);
        startActivity(i);
    }


    // exam left eye
    public void onExamLeft(View v) {
        startExam(ExamSettings.EXAM_FIELD_OPTION.LEFT);
    }


    // handle the press of "B" on VR bluetooth controller
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent. KEYCODE_ESCAPE)){
            // Exam the left eye when "B" is pressed
            startExam(ExamSettings.EXAM_FIELD_OPTION.LEFT);
            return true;
        } else {
            System.out.println("onKeyDown -- keyCode="+keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

    // exam right eye
    public void onExamRight(View v) {
        startExam(ExamSettings.EXAM_FIELD_OPTION.RIGHT);
    }

    // handle the press of "A" on VR bluetooth controller
    public boolean onTouchEvent(MotionEvent event){
        System.out.println("onTouchEvent -- "+event.toString());
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // when "A" is pressed, exam the right eye
                startExam(ExamSettings.EXAM_FIELD_OPTION.RIGHT);
                break;
        }
        return super.onTouchEvent(event) ;
    }

    private synchronized void startExam(ExamSettings.EXAM_FIELD_OPTION fieldOption) {
        if (!examStarted && readyForExam) {
            Intent i = new Intent(this, ExamActivity.class);
            GlobalContext.getExamSettings().setExamFieldOption(fieldOption);
            startActivity(i);
            examStarted = true;
        }
    }

}
