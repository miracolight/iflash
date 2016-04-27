package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.adapter.ResultListAdapter;
import com.vvavy.visiondemo.app.model.ExamResult;
import com.vvavy.visiondemo.client.VisionRestClient;
import com.vvavy.visiondemo.database.VisionDBSQLiteHelper;
import com.vvavy.visiondemo.database.entity.PerimetryTest;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.util.InternetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class ResultActivity extends Activity {

    /** Round List */
    private ListView mList;

    private ResultListAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> mData;

    private VisionDBSQLiteHelper dbHelper = null;

    private Handler uiHandler = new Handler();

    private Runnable uiRunnable = new Runnable() {
        public void run() {
            mAdapter.clear();
            mAdapter.addAll(dbHelper.getResultList());
            mAdapter.notifyDataSetChanged();
        }
    };

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        /*
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshPerimetryTests();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        */
        initViews();
        setListeners();
    }

    private void refreshPerimetryTests()  {
        String deviceId = ActivityUtil.getDeviceID(this);
        final Map<Integer, Integer> serverIds = new HashMap<Integer, Integer>();
        for (ExamResult e : dbHelper.getAllExamResults()) {
            if (e.getServerId()!= null){
                serverIds.put(e.getServerId(), e.getId());
            }
            if (!"Y".equals(e.getUploaded())) {
                String url = e.getPatientId()+"/perimetrytests?apiKey=rock2016";


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("patientId", e.getPatientId());
                    jsonObject.put("result", e.getResult());
                    jsonObject.put("testDate", e.getExamDate());
                    jsonObject.put("testDeviceId", deviceId);
                    jsonObject.put("origClientTestId", e.getId());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    continue;
                }


                e.setUploaded("Y");
                final ExamResult f = e;
                VisionRestClient.post(this, url, jsonObject, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        System.out.println("statusCode = " + statusCode);
                        System.out.println("res = " + res);
                        Toast.makeText(ResultActivity.this, "Upload failed:"+t.toString(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        System.out.println("in success - uploaded=" + f.getUploaded());
                        dbHelper.updateExamResult(f);
                        uiHandler.post(uiRunnable);
                        swipeContainer.setRefreshing(false);
                        System.out.println("in success - update done");
                    }
                });
            }
        }


        // download records
        String url = ExamResult.DUMMY_PATIENT_ID+"/perimetrytests?apiKey=rock2016";
        // trigger the network request
        VisionRestClient.get(url, null, new JsonHttpResponseHandler() {
            // define the success and failure callbacks
            // handle the successful response (popular photos JSON)

            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONArray response) {
                JSONArray photosJSON = null;
                try {
                    System.out.println("response.length="+response.length());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject testJSON = response.getJSONObject(i);
                        ExamResult er = new ExamResult(
                                testJSON.getString("patientId"),
                                testJSON.getLong("testDate"),
                                testJSON.getString("result"),
                                "Y");
                        er.setTestDeviceId(testJSON.getString("testDeviceId"));
                        er.setServerId((int)testJSON.getLong("testId"));
                        if (!serverIds.containsKey(er.getServerId())) {
                            dbHelper.addExamResult(er);
                        } else if ("Y".equals(testJSON.getString("deleted"))){
                            dbHelper.deleteExamResult(serverIds.get(er.getServerId()));
                        }
                    }
                    swipeContainer.setRefreshing(false);
                    uiHandler.post(uiRunnable);
               } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
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

    /** 初始化View */
    private void initViews() {
        mList = (ListView) findViewById(R.id.list);
    }

    /** 准备数据 */
    private void prepareData() {
        dbHelper = VisionDBSQLiteHelper.getInstance(this);
        mData = dbHelper.getResultList();
        mAdapter = new ResultListAdapter(getApplicationContext(), mData);
        mList.setAdapter(mAdapter);
    }

    /** 设置Listener */
    private void setListeners() {
        mList.setOnItemClickListener(mOnItemClickListener);

    }



    /** ListView的item点击的监听 */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> map = mData.get(position);

            Intent i = new Intent();
            i.setClass(ResultActivity.this, ResultDetailsActivity.class);
            i.putExtra(PerimetryTest.KEY_TEST_ID, (Integer) map.get(PerimetryTest.KEY_TEST_ID));
            startActivity(i);
        }

    };


    public void onSync(View v) throws JSONException {

        if (!InternetUtil.isNetworkAvailable(this)) {
            Toast.makeText(ResultActivity.this, "No internet connection. ", Toast.LENGTH_SHORT).show();
            return;
        }


        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while downloading...");
        progress.show();
        List<ExamResult> results = dbHelper.getAllExamResults();
        final Map<Integer, Integer> serverIds = new HashMap<Integer, Integer>();
        final List<ExamResult> toBeUploaded = new ArrayList<ExamResult>();
        for (ExamResult e : results) {
            if (e.getServerId() != null) {
                serverIds.put(e.getServerId(), e.getId());
            } else {
                toBeUploaded.add(e);
            }
        }

        // download records
        String url = ExamResult.DUMMY_PATIENT_ID+"/perimetrytests?apiKey=rock2016";
        // trigger the network request
        VisionRestClient.get(url, null, new JsonHttpResponseHandler() {
            // define the success and failure callbacks
            // handle the successful response (popular photos JSON)
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                System.out.println("statusCode = " + statusCode);
                System.out.println("res = " + res);
                progress.dismiss();
                t.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONArray response) {
                JSONArray photosJSON = null;
                try {
                    System.out.println("response.length="+response.length());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject testJSON = response.getJSONObject(i);
                        ExamResult er = new ExamResult(
                                testJSON.getString("patientId"),
                                testJSON.getLong("testDate"),
                                testJSON.getString("result"),
                                "Y");
                        er.setTestDeviceId(testJSON.getString("testDeviceId"));
                        er.setServerId((int)testJSON.getLong("testId"));
                        if (!serverIds.containsKey(er.getServerId())) {
                            if (!"Y".equals(testJSON.getString("deleted"))) {
                                dbHelper.addExamResult(er);
                            }
                        } else if ("Y".equals(testJSON.getString("deleted"))){
                            dbHelper.deleteExamResult(serverIds.get(er.getServerId()));
                        }
                    }
                    progress.dismiss();
                    uiHandler.post(uiRunnable);
                    progress.setTitle("wait while uploading");
                    for (ExamResult e : toBeUploaded) {
                        progress.show();
                        String url = e.getPatientId()+"/perimetrytests?apiKey=rock2016";


                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("patientId", e.getPatientId());
                            jsonObject.put("result", e.getResult());
                            jsonObject.put("testDate", e.getExamDate());
                            jsonObject.put("testDeviceId", e.getTestDeviceId());
                            jsonObject.put("origClientTestId", e.getId());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            continue;
                        }


                        final ExamResult f = e;
                        VisionRestClient.post(ResultActivity.this, url, jsonObject, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                System.out.println("statusCode = " + statusCode);
                                System.out.println("res = " + res);
                                Toast.makeText(ResultActivity.this, "Upload failed:"+t.toString(), Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                                progress.dismiss();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String response) {
                                System.out.println("in success - uploaded=" + f.getUploaded());
                                for (Header h : headers) {
                                    if ("Location".equals(h.getName())) {
                                        String url = h.getValue();
                                        String serverId = url.substring(url.lastIndexOf("/")).trim();
                                        f.setServerId(Integer.getInteger(serverId));
                                        break;
                                    }
                                }
                                f.setUploaded("Y");
                                dbHelper.updateExamResult(f);
                                uiHandler.post(uiRunnable);
                                progress.dismiss();
                                System.out.println("in success - update done");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
