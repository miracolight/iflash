package com.vvavy.visiondemo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.vvavy.visiondemo.R;
import com.vvavy.visiondemo.app.exam.PerimetryExam;
import com.vvavy.visiondemo.app.exam.impl.DefaultPerimetryExam;
import com.vvavy.visiondemo.app.handler.impl.DefaultIntensityHandler;
import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.view.ConfigView;

public class ConfigActivity extends Activity {

    private Config          config;
    //private PerimetryExam   exam;

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

        //exam = new DefaultPerimetryExam(config);


        configView = new ConfigView(this, new DefaultPerimetryExam(config));
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
                config.setCenterLeftX(size.x / 2 - newVal);
                config.setCenterRightX(size.x / 2 + newVal);
                redraw();
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
                config.setCenterY(newVal);
                redraw();
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
                config.setGap(newVal);
                redraw();
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
                config.setRadius(newVal);
                redraw();
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
                redraw();
            }
        });

        np = (NumberPicker) findViewById(R.id.npNumFixations);
        np.setMinValue(1);
        np.setMaxValue(2);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getNumFixations());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setNumFixations(newVal);
                redraw();
            }
        });

        np = (NumberPicker) findViewById(R.id.npPromptTime);
        np.setMinValue(1);
        np.setMaxValue(2);
        np.setValue(config.getPromptTime()/100);
        np.setDisplayedValues(new String[]{"100", "200"});
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setPromptTime(newVal * 100);
            }
        });

        np = (NumberPicker) findViewById(R.id.npDb);
        np.setMinValue(10);
        np.setMaxValue(40);
        np.setWrapSelectorWheel(false);
        np.setValue(config.getNumPoints());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setInitDb(newVal);
                redraw();
            }
        });
    }

    private void redraw() {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = DefaultIntensityHandler.ALL_INTENSITIES[config.getInitDb()].getScreenBrightness();
        getWindow().setAttributes(layout);
        configView.setExam(new DefaultPerimetryExam(config));
        configView.invalidate();
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
