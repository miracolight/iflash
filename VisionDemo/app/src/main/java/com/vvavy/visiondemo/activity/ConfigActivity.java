package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.network.VisionRestClient;
import com.vvavy.visiondemo.object.ExamResult;
import com.vvavy.visiondemo.service.PerimetryTestService;
import com.vvavy.visiondemo.service.impl.DefaultIntensityServiceImpl;
import com.vvavy.visiondemo.service.impl.DefaultPerimetryTestServiceImpl;
import com.vvavy.visiondemo.object.Config;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.util.InternetUtil;
import com.vvavy.visiondemo.view.ConfigView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ConfigActivity extends Activity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    private Config          config;
    //private PerimetryExam   exam;

    private ConfigView      configView;

    private SeekBar         skbConfigValue;
    private TextView        tvConfigValue;
    private Point           screenDisplaySize;
    private String          currConfigOption;
    private TextView        tvDbValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        init();

    }

    private void init() {

        ActivityUtil.hideStatusBar(this);

        config = Config.loadConfig(this);

        //exam = new DefaultPerimetryExam(config);


        configView = new ConfigView(this, new DefaultPerimetryTestServiceImpl(config));
        configView.setBackgroundColor(Color.TRANSPARENT);
        ((FrameLayout) findViewById(R.id.frmPreview)).addView(configView);


        screenDisplaySize = config.getDisplaySize(this);

        Spinner spinner = (Spinner) findViewById(R.id.spnConfigOptions);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.config_options_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition("CenterX");
        spinner.setSelection(spinnerPosition);


        skbConfigValue = (SeekBar)findViewById(R.id.skbConfigValue);
        skbConfigValue.setProgress((config.getCenterRightX()-config.getCenterLeftX())/2);
        skbConfigValue.setMax(screenDisplaySize.x/2);
        skbConfigValue.setOnSeekBarChangeListener(this);


        tvConfigValue = (TextView)findViewById(R.id.tvValue);
        tvConfigValue.setText(Integer.toString(skbConfigValue.getProgress()));

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgNumFixation);
        if (config.getNumFixations()==1) {
            radioGroup.check(R.id.rbF1);
        } else {
            radioGroup.check(R.id.rbF2);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbF1) {
                    config.setNumFixations(1);
                } else {
                    config.setNumFixations(2);
                }
                redraw();
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.rgDisplay);
        if (config.getPromptTime()==100) {
            radioGroup.check(R.id.rb100ms);
        } else {
            radioGroup.check(R.id.rb200ms);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb100ms) {
                    config.setPromptTime(100);
                } else {
                    config.setPromptTime(200);
                }
            }
        });

        tvDbValue = (TextView)findViewById(R.id.tvDbValue);
        tvDbValue.setText(configView.getCurrentDBValue());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void redraw() {
        WindowManager.LayoutParams layout = getWindow().getAttributes();

        PerimetryTestService newTest = new DefaultPerimetryTestServiceImpl(config);
        layout.screenBrightness = newTest.getIntensity(config.getInitDb()).getScreenBrightness();
        getWindow().setAttributes(layout);
        configView.setExam(new DefaultPerimetryTestServiceImpl(config));
        configView.invalidate();

        tvDbValue.setText(config.getCalibrationCode()+": "+configView.getCurrentDBValue());
     //   resetSetting();
    }



    private void resetSetting() {
        if ("CenterX".equals(currConfigOption)) {
            skbConfigValue.setProgress((config.getCenterRightX()-config.getCenterLeftX())/2);
            skbConfigValue.setMax(screenDisplaySize.x/2);
        } else if ("CenterY".equals(currConfigOption)) {
            skbConfigValue.setProgress(config.getCenterY());
            skbConfigValue.setMax(screenDisplaySize.y);
        } else if ("Radius".equals(currConfigOption)) {
            skbConfigValue.setProgress(config.getRadius());
            skbConfigValue.setMax(config.MAX_RADIUS);
        } else if ("Gap".equals(currConfigOption)) {
            skbConfigValue.setProgress(config.getGap());
            skbConfigValue.setMax(config.MAX_GAP);
        } else if ("NumOfStimulus".equals(currConfigOption)) {
            skbConfigValue.setProgress(config.getNumPoints());
            skbConfigValue.setMax(Config.MAX_NUMOFPOINTS);
        } else if ("InitDB".equals(currConfigOption)) {
            skbConfigValue.setProgress(config.getInitDb()-10);
            skbConfigValue.setMax(30);
        }
        if ("InitDB".equals(currConfigOption)) {
            tvConfigValue.setText(Integer.toString(skbConfigValue.getProgress()+10));
        } else {
            tvConfigValue.setText(Integer.toString(skbConfigValue.getProgress()));
        }

    }

    public void onSave(View v) {
        config.saveConfig(this);
        finish();
    }

    public void onReset(View v) {
        config.reset(this);
        config.saveConfig(this);

        finish();
        startActivity(getIntent());
    }

    public void onPlus(View v) {
        onButtonUpdate(1);
    }

    public void onMinus(View v) {
        onButtonUpdate(-1);
    }

    private void onButtonUpdate(int delta) {
        int currValue = Integer.parseInt(tvConfigValue.getText().toString());
        int newValue = currValue+delta;
        if (newValue<0 || ("InitDB".equals(currConfigOption) && newValue<10)) {
            return;
        }
        if ("CenterX".equals(currConfigOption)) {
            if (newValue <= screenDisplaySize.x/2){
                config.setCenterLeftX(screenDisplaySize.x/2-newValue);
                config.setCenterRightX(screenDisplaySize.x/2+newValue);
                tvConfigValue.setText(Integer.toString(newValue));
            }
        } else if ("CenterY".equals(currConfigOption)) {
            if (newValue<=screenDisplaySize.y) {
                config.setCenterY(newValue);
                tvConfigValue.setText(Integer.toString(newValue));
            }
        } else if ("Radius".equals(currConfigOption)) {
            if (newValue <= Config.MAX_RADIUS) {
                config.setRadius(newValue);
                tvConfigValue.setText(Integer.toString(newValue));
            }
        } else if ("Gap".equals(currConfigOption)) {
            if (newValue <= Config.MAX_GAP) {
                config.setGap(newValue);
                tvConfigValue.setText(Integer.toString(newValue));
            }
        } else if ("NumOfStimulus".equals(currConfigOption)) {
            if (newValue <= Config.MAX_NUMOFPOINTS){
                config.setNumPoints(newValue);
                tvConfigValue.setText(Integer.toString(newValue));
            }
        } else if ("InitDB".equals(currConfigOption)) {
            if (newValue <=40) {
                config.setInitDb(newValue);
                tvConfigValue.setText(Integer.toString(newValue));
            }
        }
        redraw();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preview, menu);
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        currConfigOption = (String)parent.getItemAtPosition(pos);
        System.out.println("onItemSelected: currConfigOption=" + currConfigOption);
        resetSetting();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        tvConfigValue.setText(Integer.toString(progress));
        if ("CenterX".equals(currConfigOption)) {
            config.setCenterLeftX(screenDisplaySize.x / 2 - progress);
            config.setCenterRightX(screenDisplaySize.x / 2 + progress);
        } else if ("CenterY".equals(currConfigOption)) {
            config.setCenterY(progress);
        } else if ("Radius".equals(currConfigOption)) {
            config.setRadius(progress);
        } else if ("Gap".equals(currConfigOption)) {
            config.setGap(progress);
        } else if ("NumOfStimulus".equals(currConfigOption)) {
            config.setNumPoints(progress);
        } else if ("InitDB".equals(currConfigOption)) {
            config.setInitDb(progress+10);
            tvConfigValue.setText(Integer.toString(progress+10));
        }
        redraw();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onRefreshCalibration(View v) {
        if (!InternetUtil.isNetworkAvailable(this)) {
            Toast.makeText(ConfigActivity.this, "No internet connection. ", Toast.LENGTH_SHORT).show();
            return;
        }


        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while downloading...");
        progress.show();

        String url = "calibrations/latest?apiKey=rock2016";
        // trigger the network request
        VisionRestClient.get(url, null, new JsonHttpResponseHandler() {
            // define the success and failure callbacks
            // handle the successful response (popular photos JSON)
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                System.out.println("statusCode = " + statusCode);
                progress.dismiss();
                t.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    String code = response.getString("code");
                    long lastUpdateDate = response.getLong("lastUpdateDate");
                    String result = response.getString("result");
                    config.setCalibrationCode(code+"-"+(lastUpdateDate/1000)%1000);
                    config.setCalibrationResult(result);
                    config.saveCalibrationConfig(ConfigActivity.this);
                    progress.dismiss();
                    redraw();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
