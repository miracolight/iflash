package com.vvavy.visiondemo.task;

import android.os.Handler;
import android.view.WindowManager;

import com.loopj.android.http.TextHttpResponseHandler;
import com.vvavy.visiondemo.activity.ExamActivity;
import com.vvavy.visiondemo.service.PerimetryTestService;
import com.vvavy.visiondemo.object.ExamResult;
import com.vvavy.visiondemo.object.PerimetryStimulus;
import com.vvavy.visiondemo.network.VisionRestClient;
import com.vvavy.visiondemo.database.VisionDBSQLiteHelper;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.view.ExamView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by qingdi on 3/7/16.
 */
public class ExamTask implements Runnable {
    public static final int     STIMULATE_INTERVAL = 800;
    private PerimetryTestService exam;
    private Handler             uiHandler;
    private ExamView            examView;
    private ExamActivity        examActivity;
    private PerimetryStimulus   currentStimulus = null;
    private boolean             taskDone;

    public ExamTask(ExamActivity examActivity, PerimetryTestService exam, Handler uiHandler, ExamView examView) {
        this.examActivity = examActivity;
        this.exam = exam;
        this.uiHandler = uiHandler;
        this.examView = examView;
        this.taskDone = false;
    }

    @Override
    public void run() {

        while ((currentStimulus = exam.getCurrentStimulus()) != null) {
            this.examView.setCurrentStimulus(currentStimulus);

            UpdateGUI();

            try {
                Thread.sleep(exam.getPromptTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.examView.setCurrentStimulus(null);
            UpdateGUI();

            try {
                Thread.sleep(STIMULATE_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exam.processStimulus(currentStimulus);
            currentStimulus.setDetected(false);
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
            if (currentStimulus != null) {
                WindowManager.LayoutParams layout = examActivity.getWindow().getAttributes();
                layout.screenBrightness = currentStimulus.getIntensity().getScreenBrightness();
                System.out.println("screenbrightness="+layout.screenBrightness);
                examActivity.getWindow().setAttributes(layout);
            }


            examView.invalidate();
        }
    };

    public void setCurrentStimulusDetected() {
        if (currentStimulus != null) {
            currentStimulus.setDetected(true);
        }
    }

    public void saveResult(VisionDBSQLiteHelper dbHelper) {
        ExamResult result = new ExamResult(exam);
        String deviceId = ActivityUtil.getDeviceID(examActivity);
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
        VisionRestClient.post(examActivity, url, jsonObject, new TextHttpResponseHandler() {
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
}
