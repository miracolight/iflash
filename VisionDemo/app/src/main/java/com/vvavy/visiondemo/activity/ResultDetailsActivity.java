package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.app.model.ExamResult;
import com.vvavy.visiondemo.client.VisionRestClient;
import com.vvavy.visiondemo.database.VisionDBSQLiteHelper;
import com.vvavy.visiondemo.database.entity.PerimetryTest;
import com.vvavy.visiondemo.util.InternetUtil;
import com.vvavy.visiondemo.view.ExamResultView;
import com.vvavy.visiondemo.view.ExamView;

import cz.msebera.android.httpclient.Header;

public class ResultDetailsActivity extends Activity {

    private VisionDBSQLiteHelper dbHelper;
    private ExamResult  mExamResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        int resultId = getIntent().getIntExtra(PerimetryTest.KEY_TEST_ID, -1);
        if(resultId < 0) {
            finish();
            return;
        }

        dbHelper = VisionDBSQLiteHelper.getInstance(this);


        mExamResult = dbHelper.getExamResult(resultId);
        ExamResultView examResultView = new ExamResultView(this, mExamResult);
        examResultView.setBackgroundColor(Color.BLACK);
        ((FrameLayout) findViewById(R.id.frmResult)).addView(examResultView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result_details, menu);
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


    public void onDelete(View v) {


        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while deleting...");
        progress.show();

        if (mExamResult.getServerId() == null || mExamResult.getServerId()==0) {
            dbHelper.deleteExamResult(mExamResult.getId());
            progress.dismiss();
            finish();
            return;
        }


        if (!InternetUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "No internet connection. ", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            finish();
            return;
        }



        String url = "/perimetrytests/"+mExamResult.getServerId().toString()+"?apiKey=rock2016";
        // trigger the network request
        VisionRestClient.delete(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                System.out.println("statusCode = " + statusCode);
                System.out.println("res = " + res);
                progress.dismiss();
                Toast.makeText(ResultDetailsActivity.this, "Deletion failed:" + t.toString(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                System.out.println("in success - deleted=" + mExamResult.getServerId());
                progress.dismiss();
                dbHelper.deleteExamResult(mExamResult.getId());
                finish();
                System.out.println("in success - update done");
            }
        });

        /*
        SparseBooleanArray checkedItemPositions = mList.getCheckedItemPositions();
        int itemCount = mList.getCount();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                mAdapter.remove(mData.get(i));
            }
        }
        checkedItemPositions.clear();
        mAdapter.notifyDataSetChanged();
        */
    }
}
