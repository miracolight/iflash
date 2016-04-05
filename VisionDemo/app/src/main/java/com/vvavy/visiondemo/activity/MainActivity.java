package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.app.PerimetryExam;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onConfig(View v) {

        Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
    }

    public void onExamLeft(View v) {
        Intent i = new Intent(this, ExamActivity.class);
        Bundle param = new Bundle();
        param.putBoolean(PerimetryExam.LEFT_EYE_EXAM, true);
        i.putExtras(param);
        startActivity(i);
    }

    public void onExamRight(View v) {
        Intent i = new Intent(this, ExamActivity.class);
        Bundle param = new Bundle();
        param.putBoolean(PerimetryExam.LEFT_EYE_EXAM, false);
        i.putExtras(param);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
