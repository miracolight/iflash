package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.app.ExamTask;
import com.vvavy.visiondemo.app.PerimetryExam;
import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.view.ExamView;

public class ExamActivity extends Activity {

    private Config          config;
    private PerimetryExam   exam;

    private ExamTask        examTask;
    private ExamView        examView;

    final private Handler   uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        init();

    }


    private void init() {
        ActivityUtil.hideStatusBar(this);

        config = Config.loadConfig(this);

        PerimetryExam.Builder examBuilder = new PerimetryExam.Builder(config);
        exam = examBuilder.create();

        Bundle param = getIntent().getExtras();
        boolean leftEyeExam = param.getBoolean(PerimetryExam.LEFT_EYE_EXAM, true);
        exam.setLeftEyeExam(leftEyeExam);
        examView = new ExamView(this, exam);
        examView.setBackgroundColor(Color.TRANSPARENT);
        ((FrameLayout) findViewById(R.id.frmRun)).addView(examView);
    }


    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (examTask == null) {
                    examTask = new ExamTask(exam, uiHandler, examView);
                    Thread t = new Thread(examTask);
                    t.start();
                } else if (!examTask.isTaskDone()){
                    examTask.setCurrentPointVisible();
                } else {
                    finish();
                }
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

}
