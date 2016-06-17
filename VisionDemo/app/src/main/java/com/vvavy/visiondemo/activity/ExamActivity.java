package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.service.PerimetryTestService;
import com.vvavy.visiondemo.service.impl.DefaultIntensityServiceImpl;
import com.vvavy.visiondemo.service.impl.DefaultPerimetryTestServiceImpl;
import com.vvavy.visiondemo.object.Intensity;
import com.vvavy.visiondemo.task.ExamTask;
import com.vvavy.visiondemo.object.Config;
import com.vvavy.visiondemo.database.VisionDBSQLiteHelper;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.view.ExamView;

import java.util.Locale;

public class ExamActivity extends Activity implements View.OnTouchListener {

    public static final String LEFT_EYE_EXAM = "LEFT_EYE_EXAM";


    private Config          config;
    private PerimetryTestService exam;

    private ExamTask        examTask;
    private ExamView        examView;

    final private Handler   uiHandler = new Handler();

    private TextToSpeech    tts;

    private VisionDBSQLiteHelper    dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        // temp test
        dbHelper = VisionDBSQLiteHelper.getInstance(this);

        init();

    }


    private void init() {
        ActivityUtil.hideStatusBar(this);

        config = Config.loadConfig(this);

        Bundle param = getIntent().getExtras();
        boolean leftEyeExam = param.getBoolean(LEFT_EYE_EXAM, true);

        exam = new DefaultPerimetryTestServiceImpl(config, leftEyeExam? PerimetryTestService.ExamType.LEFT: PerimetryTestService.ExamType.RIGHT);

        Intensity initIntensity = exam.getIntensity(config.getInitDb());
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = initIntensity.getScreenBrightness();
        getWindow().setAttributes(layout);

        examView = new ExamView(this, exam);
        examView.setBackgroundColor(
                Color.argb(initIntensity.getBgAlpha(),
                        initIntensity.getBgGreyscale(), initIntensity.getBgGreyscale(), initIntensity.getBgGreyscale()));
        ((FrameLayout) findViewById(R.id.frmRun)).addView(examView);

        tts =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.run, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (examTask == null) {
            //http://stackoverflow.com/questions/8857590/android-countdowntimer-skips-last-ontick
            new CountDownTimer(4000, 900) {
                public void onTick(long millisUntilFinished) {
                    examView.setSecondsToStart(Math.round(millisUntilFinished/1000f)-1);
                    examView.invalidate();
                }

                public void onFinish() {
                    examView.setSecondsToStart(0);
                    examTask = new ExamTask(ExamActivity.this, exam, uiHandler, examView);
                    Thread t = new Thread(examTask);
                    t.start();
                }
            }.start();
        } else if (!examTask.isTaskDone()){
            examTask.setCurrentStimulusDetected();
        } else {
            examTask.saveResult(dbHelper);
            finish();
        }
    }
}
