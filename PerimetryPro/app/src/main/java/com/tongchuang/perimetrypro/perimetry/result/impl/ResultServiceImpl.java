package com.tongchuang.perimetrypro.perimetry.result.impl;

import android.content.Context;

import com.loopj.android.http.TextHttpResponseHandler;
import com.tongchuang.perimetrypro.activity.PatientMainActivity;
import com.tongchuang.perimetrypro.context.GlobalContext;
import com.tongchuang.perimetrypro.database.VisionDBSQLiteHelper;
import com.tongchuang.perimetrypro.network.VisionRestClient;
import com.tongchuang.perimetrypro.perimetry.exam.object.ExamResult;
import com.tongchuang.perimetrypro.perimetry.result.ResultService;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by qingdi on 8/22/16.
 */
public class ResultServiceImpl implements ResultService {
    private VisionDBSQLiteHelper    dbHelper;
    private Context                 context;

    public ResultServiceImpl(Context context) {
        this.context = context;
        dbHelper = VisionDBSQLiteHelper.getInstance(context);
    }

    @Override
    public void saveResult(ExamResult result) {
        dbHelper.addExamResult(result);
        saveToServer(result, dbHelper);
    }


    public void saveToServer(ExamResult result, final VisionDBSQLiteHelper dbHelper) {
        String url = result.getPatientId()+"/perimetrytests?apiKey=rock2016";


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("patientId", result.getPatientId());
            jsonObject.put("result", result.toJSon());
            jsonObject.put("testDate", result.getExamDate());
            jsonObject.put("testDeviceId", result.getTestDeviceId());
            jsonObject.put("origClientTestId", result.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        result.setUploaded("Y");
        final ExamResult f = result;
        VisionRestClient.post(context, url, jsonObject, new TextHttpResponseHandler() {
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
