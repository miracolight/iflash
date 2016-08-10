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

        dbHelper = VisionDBSQLiteHelper.getInstance(this);

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() throws Exception {
        ActivityUtil.hideStatusBar(this);

        exam = ExamTaskBuilder.build(GlobalContext.getExamSettings(), Collections.<ExamTaskListener>singletonList(this));

        Intensity defaultIntensity = exam.getDefaultIntensity();
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = defaultIntensity.getScreenBrightness();
        getWindow().setAttributes(layout);

        examView = new ExamView(this);
        examView.setBackgroundColor(IntensityUtil.getBackgroundColor(defaultIntensity));
        ((FrameLayout) findViewById(R.id.frmRun)).addView(examView);
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
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            handleKeyTouchEvent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleKeyTouchEvent() {
        if (!examStarted) {
            startExam();
        } else if (exam.isRunning()){
            exam.onResponse();
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

    public void saveResult(VisionDBSQLiteHelper dbHelper) {
        ExamResult result = new ExamResult(exam);
        String deviceId = GlobalContext.getDeviceId();
        result.setTestDeviceId(deviceId);
        dbHelper.addExamResult(result);
        saveToServer(result, dbHelper);
    }


    public void saveToServer(ExamResult result, final VisionDBSQLiteHelper dbHelper) {
        String url = result.getPatientId()+"/perimetrytests?apiKey=rock2016";


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("patientId", result.getPatientId());
            jsonObject.put("result", result.getResult());
            jsonObject.put("testDate", result.getExamDate());
            jsonObject.put("testDeviceId", result.getTestDeviceId());
            jsonObject.put("origClientTestId", result.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        result.setUploaded("Y");
        final ExamResult f = result;
        VisionRestClient.post(this, url, jsonObject, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                System.out.println("statusCode = " + statusCode);
                System.out.println("res = " + res);
                t.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                System.out.println("in success - uploaded=" + f.getUploaded());
                for (Header h : headers) {
                    if ("Location".equals(h.getName())) {
                        String url = h.getValue();
                        String serverId = url.substring(url.lastIndexOf("/")+1).trim();
                        f.setServerId(Integer.valueOf(serverId));
                        break;
                    }
                }
                dbHelper.updateExamResult(f);
                System.out.println("in success - update done");
            }
        });
    }

    @Override
    public void onStimulusChange() {
        StimulusInstance currentStimulus = exam.getCurrentStimulusInstance();
        if (currentStimulus != null) {
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            layout.screenBrightness = currentStimulus.getIntensity().getScreenBrightness();
            System.out.println("screenbrightness="+layout.screenBrightness);
            getWindow().setAttributes(layout);
        }


        examView.invalidate();
    }

    @Override
    public void onExamTaskDone() {
        examView.invalidate();
        saveResult(dbHelper);
        finish();

    }

    public ExamTask getExam() {
        return exam;
    }

    public int getSecondsToStart() {
        return secondsToStart;
    }
}
