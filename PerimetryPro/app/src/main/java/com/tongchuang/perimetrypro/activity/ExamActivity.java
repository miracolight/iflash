package com.tongchuang.perimetrypro.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.loopj.android.http.TextHttpResponseHandler;
import com.tongchuang.perimetrypro.R;
import com.tongchuang.perimetrypro.context.GlobalContext;
import com.tongchuang.perimetrypro.database.VisionDBSQLiteHelper;
import com.tongchuang.perimetrypro.network.VisionRestClient;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTaskBuilder;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTaskListener;
import com.tongchuang.perimetrypro.perimetry.exam.object.ExamResult;
import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;
import com.tongchuang.perimetrypro.util.ActivityUtil;
import com.tongchuang.perimetrypro.util.IntensityUtil;
import com.tongchuang.perimetrypro.view.ExamView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import cz.msebera.android.httpclient.Header;

public class ExamActivity extends AppCompatActivity implements View.OnTouchListener, ExamTaskListener {

    public static final String EXAM_FIELD_OPTION_PROPNAME = "EXAM_FIELD_OPTION_PROPNAME";

    private boolean examStarted = false;
    private static int EXAM_PREPARE_TIME_IN_MS = 4000; // 4000ms
    private int secondsToStart;

    private ExamTask exam;
    private ExamView examView;

    private VisionDBSQLiteHelper    dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);


        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() throws Exception {
        ActivityUtil.hideStatusBar(this);

        exam = GlobalContext.getExamTask();
        exam.setExamTaskListeners(Collections.<ExamTaskListener>singletonList(this));

        ActivityUtil.setScreenBrightness(this, exam.getExamSettings());

        examView = new ExamView(this);
        examView.setBackgroundColor(IntensityUtil.getBackgroundColor(exam.getExamSettings().getDefaultIntensity()));
        ((FrameLayout) findViewById(R.id.frmRun)).addView(examView);

        startExam();
    }


    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handleKeyTouchEvent();
                break;
        }
        return super.onTouchEvent(event) ;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    // handle the press of "D" (volume_up) on VR bluetooth controller
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_ESCAPE
                ||keyCode == KeyEvent.KEYCODE_BUTTON_A||keyCode == KeyEvent.KEYCODE_BUTTON_B)){
            handleKeyTouchEvent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleKeyTouchEvent() {
        if (exam.isRunning()){
            exam.onResponse();
            runOnUiThread(new UIUpdateRunner());
        }
    }

    public synchronized void startExam() {
        if (!examStarted) {
            //http://stackoverflow.com/questions/8857590/android-countdowntimer-skips-last-ontick
            new CountDownTimer(EXAM_PREPARE_TIME_IN_MS, 900) {
                public void onTick(long millisUntilFinished) {
                    secondsToStart = Math.round(millisUntilFinished/1000f)-1;
                    examView.invalidate();
                }

                public void onFinish() {
                    Thread t = new Thread(exam);
                    t.start();
                }
            }.start();
            examStarted = true;
        }
    }

    @Override
    public void onStimulusChange() {
        runOnUiThread(new UIUpdateRunner());

    }

    @Override
    public void onExamTaskDone() {
        finish();
    }

    public ExamTask getExam() {
        return exam;
    }

    public int getSecondsToStart() {
        return secondsToStart;
    }


    public class UIUpdateRunner implements Runnable{
        @Override
        public void run() {
            StimulusInstance currentStimulus = exam.getCurrentStimulusInstance();
            if (currentStimulus != null) {
                WindowManager.LayoutParams layout = getWindow().getAttributes();
                layout.screenBrightness = currentStimulus.getIntensity().getScreenBrightness();
                getWindow().setAttributes(layout);
            }

            examView.invalidate();
        }
    }
}
