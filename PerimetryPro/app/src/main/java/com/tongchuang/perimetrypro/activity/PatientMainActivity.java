package com.tongchuang.perimetrypro.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tongchuang.perimetrypro.R;
import com.tongchuang.perimetrypro.context.GlobalContext;
import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTaskBuilder;
import com.tongchuang.perimetrypro.perimetry.exam.object.ExamResult;
import com.tongchuang.perimetrypro.perimetry.result.ResultService;
import com.tongchuang.perimetrypro.perimetry.result.ResultServiceResponseHandler;
import com.tongchuang.perimetrypro.perimetry.result.impl.ResultServiceImpl;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings.EXAM_FIELD_OPTION;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;
import com.tongchuang.perimetrypro.util.ActivityUtil;
import com.tongchuang.perimetrypro.util.IntensityUtil;
import com.tongchuang.perimetrypro.util.TimeUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class PatientMainActivity extends AppCompatActivity  implements View.OnTouchListener{

    public static final int     EXAM_REQUEST_CODE = 1;

    private ExamTask exam;
    private EXAM_FIELD_OPTION currFieldOption;
    private TextView tvExamLeft;
    private TextView tvExamRight;

    private boolean examStarted = false;
    private ExamResult examResult = null;

    private ResultService   resultSerivce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("PatientMainActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        tvExamLeft = (TextView)findViewById(R.id.tvExamLeft);
        tvExamRight = (TextView)findViewById(R.id.tvExamRight);

        ActivityUtil.hideStatusBar(this);

        ActivityUtil.setScreenBrightness(this, GlobalContext.getExamSettings());

        int bgColor = IntensityUtil.getBackgroundColor(GlobalContext.getExamSettings().getDefaultIntensity());
        findViewById(R.id.frExamLeft).setBackgroundColor(bgColor);
        findViewById(R.id.frExamRight).setBackgroundColor(bgColor);

        EXAM_FIELD_OPTION examFieldOption = GlobalContext.getExamSettings().getExamFieldOption();
        if (examFieldOption == EXAM_FIELD_OPTION.BOTH) {
            currFieldOption = EXAM_FIELD_OPTION.RIGHT;
        } else {
            currFieldOption = examFieldOption;
        }

        prepareForExam();

        examResult = new ExamResult(GlobalContext.getExamSettings());

        resultSerivce = new ResultServiceImpl(this) ;


    }

    private void prepareForExam() {
        if (currFieldOption == EXAM_FIELD_OPTION.RIGHT) {
            tvExamLeft.setVisibility(View.INVISIBLE);
            tvExamRight.setVisibility(View.VISIBLE);
        } else {
            tvExamLeft.setVisibility(View.VISIBLE);
            tvExamRight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
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
    // handle the press of "D" (volume_up) on VR bluetooth controller
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN||keyCode == KeyEvent.KEYCODE_ESCAPE)){
            handleKeyTouchEvent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleKeyTouchEvent() {
        if (!examStarted) {
            startExam();
        }
    }

    private synchronized void startExam() {
        if (!examStarted) {
            try {
                Intent i = new Intent(this, ExamActivity.class);
                exam = ExamTaskBuilder.build(GlobalContext.getExamSettings(),currFieldOption);
                GlobalContext.setExamTask(exam);
                startActivityForResult(i, EXAM_REQUEST_CODE);
                examStarted = true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Error:"+e.getMessage(), Toast.LENGTH_LONG);
                Point pos = currFieldOption==EXAM_FIELD_OPTION.LEFT?GlobalContext.getExamSettings().getLeftFixation():
                        GlobalContext.getExamSettings().getRightFixation();
                toast.setGravity(Gravity.TOP| Gravity.LEFT, pos.x, pos.y);
                toast.show();
            }
        }
    }

    // return to signIn page when patient finishes the exam
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == EXAM_REQUEST_CODE) {
            System.out.println("aimu_log: onActivityResult -- set examStarted=false");
            Toast toast = Toast.makeText(getApplicationContext(),
                    "测试结束!", Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.GREEN);
            Point pos = currFieldOption==EXAM_FIELD_OPTION.LEFT?GlobalContext.getExamSettings().getLeftFixation():
                    GlobalContext.getExamSettings().getRightFixation();
            toast.setGravity(Gravity.TOP| Gravity.LEFT, pos.x, pos.y);
            toast.show();

            if (currFieldOption == EXAM_FIELD_OPTION.RIGHT){
                examResult.addRightResult(exam);
            } else {
                examResult.addLeftResult(exam);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvExamLeft.setVisibility(View.INVISIBLE);
                    tvExamRight.setVisibility(View.INVISIBLE);
                }
            });

            if (currFieldOption == EXAM_FIELD_OPTION.RIGHT && GlobalContext.getExamSettings().getExamFieldOption()==EXAM_FIELD_OPTION.BOTH) {
                // Execute some code after 2 seconds have passed
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currFieldOption = EXAM_FIELD_OPTION.LEFT;
                        prepareForExam();
                        examStarted = false;                            }
                }, TimeUtil.SHORT_DELAY);
            } else {
                onFinish();
            }

        }
    }

    private void onFinish() {
        examResult.setPatientId(GlobalContext.getUserInfo().getPatientId());
        examResult.setTestDeviceId(GlobalContext.getDeviceId());
        resultSerivce.saveResult(examResult,
                new ResultServiceResponseHandler() {
                    @Override
                    public void onFinish() {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PatientMainActivity.this.finish();                           }
                        }, TimeUtil.SHORT_DELAY);
                    }
                });
    }
}
