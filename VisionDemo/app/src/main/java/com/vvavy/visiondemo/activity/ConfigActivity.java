package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.app.PerimetryExam;
import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.app.object.Intensity;
import com.vvavy.visiondemo.app.object.PerimetryPoint;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.view.ConfigView;

public class ConfigActivity extends Activity {

    private Config          config;
    private PerimetryExam   exam;

    private ConfigView      configView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        init();

    }

    private void init() {

        ActivityUtil.hideStatusBar(this);

        config = Config.loadConfig(this);

        PerimetryExam.Builder examBuilder = new PerimetryExam.Builder(config);
        exam = examBuilder.create();
        adjustIntensity();

        configView = new ConfigView(this, exam);
        configView.setBackgroundColor(Color.TRANSPARENT);
        ((FrameLayout) findViewById(R.id.frmPreview)).addView(configView);


        final Point size = config.getDisplaySize(this);
        NumberPicker np = (NumberPicker) findViewById(R.id.npCenterX);
        np.setMinValue(0);
        np.setMaxValue(size.x/2);
        np.setWrapSelectorWheel(false);
        np.setValue((config.getCenterRightX()-config.getCenterLeftX())/2);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exam.setCenterLeftX(size.x/2-newVal);
                exam.setCenterRightX(size.x/2+newVal);
                config.setCenterLeftX(size.x/2-newVal);
                config.setCenterRightX(size.x/2+newVal);
                configView.invalidate();
            }
        });

        np = (NumberPicker) findViewById(R.id.npCenterY);
        np.setMinValue(0);
        np.setMaxValue(size.y);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getCenterY());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exam.setCenterY(newVal);
                config.setCenterY(newVal);
                configView.invalidate();
            }
        });

        np = (NumberPicker) findViewById(R.id.npBrightness);
        np.setMinValue(0);
        np.setMaxValue(Config.MAX_GREY);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getIntensity());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setIntensity(newVal);
                adjustIntensity();
                configView.invalidate();
            }
        });

        np = (NumberPicker) findViewById(R.id.npAlpha);
        np.setMinValue(0);
        np.setMaxValue(Config.MAX_ALPHA);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getAlpha());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setAlpha(newVal);
                adjustIntensity();
                configView.invalidate();
            }
        });

        np = (NumberPicker) findViewById(R.id.npGap);
        np.setMinValue(0);
        np.setMaxValue(Config.MAX_GAP);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getGap());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exam.setGap(newVal);
                config.setGap(newVal);
                configView.invalidate();
            }
        });

        np = (NumberPicker) findViewById(R.id.npRadius);
        np.setMinValue(0);
        np.setMaxValue(Config.MAX_RADIUS);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getRadius());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exam.setRadius(newVal);
                config.setRadius(newVal);
                configView.invalidate();
            }
        });

        np = (NumberPicker) findViewById(R.id.npNumPoints);
        np.setMinValue(0);
        np.setMaxValue(Config.MAX_NUMOFPOINTS);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getNumPoints());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setNumPoints(newVal);
                PerimetryExam e = (new PerimetryExam.Builder(config)).create();
                exam.setPoints(e.getPoints());
                configView.invalidate();
            }
        });
    }

    private void adjustIntensity() {
        for (PerimetryPoint p : exam.getPoints()) {
            p.setIntensity(new Intensity(config.getAlpha(), config.getIntensity()));
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
}
