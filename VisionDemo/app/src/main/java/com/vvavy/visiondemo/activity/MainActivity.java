package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vvavy.visiondemo.R;

public class MainActivity extends Activity {

    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvUserName = (TextView)findViewById(R.id.tvUserName);
    }

    public void onConfig(View v) {

        Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
    }

    public void onResult(View v) {

        Intent i = new Intent(this, ResultActivity.class);
        startActivity(i);
    }



    public void onExamLeft(View v) {
        examLeftEye();
    }

    private void examLeftEye() {
        Intent i = new Intent(this, ExamActivity.class);
        Bundle param = new Bundle();
        param.putBoolean(ExamActivity.LEFT_EYE_EXAM, true);
        i.putExtras(param);
        startActivity(i);
    }

    public void onExamRight(View v) {
        examRightEye();
    }

    private void examRightEye() {
        Intent i = new Intent(this, ExamActivity.class);
        Bundle param = new Bundle();
        param.putBoolean(ExamActivity.LEFT_EYE_EXAM, false);
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

    @Override
    // handle the press of "B" on VR bluetooth controller
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent. KEYCODE_ESCAPE)){
            // Exam the left eye when "B" is pressed
            examLeftEye();
            return true;
        } else {
            System.out.println("onKeyDown -- keyCode="+keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

    // handle the press of "A" on VR bluetooth controller
    public boolean onTouchEvent(MotionEvent event){
        System.out.println("onTouchEvent -- "+event.toString());
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // when "A" is pressed, exam the right eye
                examRightEye();
                break;
        }
        return super.onTouchEvent(event) ;
    }


    public void onScan(View view) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            tvUserName.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
