package com.tongchuang.perimetrypro.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
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

    public static final int     EXAM_REQUEST_CODE = 1;
    private UserService         userService = new UserServiceImpl();
    private SettingService      settingService = new SettingServiceImpl();
    private boolean             examStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("in onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (GlobalContext.getUserInfo().getPatientId() != null) {
            findViewById(R.id.imgConfig).setVisibility(View.INVISIBLE);
            findViewById(R.id.imgResult).setVisibility(View.INVISIBLE);
        }
        ((TextView)findViewById(R.id.tvDeviceId)).setText("--"+GlobalContext.getDeviceId());

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
        if (!examStarted) {
            Intent i = new Intent(this, ExamActivity.class);
            GlobalContext.getExamSettings().setExamFieldOption(fieldOption);
            startActivityForResult(i, EXAM_REQUEST_CODE);
            examStarted = true;
        }
    }

    // return to signIn page when patient finishes the exam
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == EXAM_REQUEST_CODE) {
            System.out.println("aimu_log: onActivityResult -- set examStarted=false");
            Toast toast = Toast.makeText(getApplicationContext(),
                    "测试结束!", Toast.LENGTH_LONG);
            toast.show();

            examStarted = false;
            if (GlobalContext.getUserInfo().getPatientId() != null) {
                finish();
            }
        }
    }

}
