package com.vvavy.visiondemo.app;

import android.os.Handler;

import com.vvavy.visiondemo.app.object.PerimetryPoint;
import com.vvavy.visiondemo.view.ExamView;

/**
 * Created by qingdi on 3/7/16.
 */
public class ExamTask implements Runnable {
    private PerimetryExam   exam;
    private Handler         uiHandler;
    private ExamView        examView;
    private PerimetryPoint  checkPoint = null;
    private boolean         taskDone;

    public ExamTask(PerimetryExam exam, Handler uiHandler, ExamView examView) {
        this.exam = exam;
        this.uiHandler = uiHandler;
        this.examView = examView;
        this.taskDone = false;
    }

    @Override
    public void run() {

        while ((checkPoint = exam.getCheckPoint()) != null) {
            this.examView.setCheckPoint(checkPoint);
            UpdateGUI();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.examView.setCheckPoint(null);
            UpdateGUI();

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exam.processCheckPoint(checkPoint);
            checkPoint.setVisible(false);
        }

        UpdateGUI();

        taskDone = true;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    private void UpdateGUI() {
        uiHandler.post(uiRunnable);
    }

    final Runnable uiRunnable = new Runnable() {
        public void run() {
            examView.invalidate();
        }
    };

    public void setCurrentPointVisible() {
        if (checkPoint != null) {
            checkPoint.setVisible(true);
        }
    }
}
