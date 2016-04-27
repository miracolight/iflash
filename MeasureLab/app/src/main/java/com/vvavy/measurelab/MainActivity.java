package com.vvavy.measurelab;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vvavy.measurelab.listener.ButtonListener;
import com.vvavy.measurelab.listener.SeekBarListener;
import com.vvavy.measurelab.object.State;
import com.vvavy.measurelab.updator.DisplayUpdator;
import com.vvavy.measurelab.updator.impl.AlphaStateUpdator;
import com.vvavy.measurelab.updator.impl.GreyStateUpdator;
import com.vvavy.measurelab.updator.impl.ScreenBrightUpdator;
import com.vvavy.measurelab.view.CustomBackgroundView;

import java.lang.reflect.Field;


public class MainActivity extends Activity implements DisplayUpdator {

    private View        backView;


    //private View        stimulusView;

    private boolean     backgroundChecked = true;
    private SeekBar     alphaSeekBar;
    private SeekBar     greySeekBar;
    private SeekBar     screenBrightSeekBar;

    private TextView    tvAlphaValue;
    private TextView    tvGreyValue;
    private TextView    tvScreenBrightValue;

    private State       state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        state = new State();

        backView = new CustomBackgroundView(this, state);
        ((FrameLayout)findViewById(R.id.rlBackground)).addView(backView);

        //stimulusView = findViewById(R.id.rlStimulus);

        tvAlphaValue = (TextView) findViewById(R.id.tvAlphaValue);
        alphaSeekBar = (SeekBar) findViewById(R.id.sbAlphaValue);
        alphaSeekBar.setOnSeekBarChangeListener(new SeekBarListener(new AlphaStateUpdator(state), this));


        greySeekBar = (SeekBar) findViewById(R.id.sbGreyValue);
        greySeekBar.setOnSeekBarChangeListener(new SeekBarListener(new GreyStateUpdator(state), this));
        tvGreyValue = (TextView) findViewById(R.id.tvGreyValue);

        screenBrightSeekBar = (SeekBar) findViewById(R.id.sbScreenValue);
        screenBrightSeekBar.setOnSeekBarChangeListener(new SeekBarListener(new ScreenBrightUpdator(state), this));
        tvScreenBrightValue = (TextView) findViewById(R.id.tvScreenValue);


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgToggle);
        radioGroup.check(R.id.rbBackground);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbBackground) {
                    backgroundChecked = true;
                } else {
                    backgroundChecked = false;
                }
                resetSetting();
            }
        });

        Button btn = (Button)findViewById(R.id.btnAlphaPlus);
        btn.setOnClickListener(new ButtonListener(new AlphaStateUpdator(state), this, 1));

        btn = (Button)findViewById(R.id.btnAlphaMinus);
        btn.setOnClickListener(new ButtonListener(new AlphaStateUpdator(state), this, -1));

        btn = (Button)findViewById(R.id.btnGreyPlus);
        btn.setOnClickListener(new ButtonListener(new GreyStateUpdator(state), this, 1));

        btn = (Button)findViewById(R.id.btnGreyMinus);
        btn.setOnClickListener(new ButtonListener(new GreyStateUpdator(state), this, -1));

        btn = (Button)findViewById(R.id.btnScreenPlus);
        btn.setOnClickListener(new ButtonListener(new ScreenBrightUpdator(state), this, 1));

        btn = (Button)findViewById(R.id.btnScreenMinus);
        btn.setOnClickListener(new ButtonListener(new ScreenBrightUpdator(state), this, -1));

        redraw();
    }

    @Override
    public boolean isBackgroundChecked() {
        return backgroundChecked;
    }

    public void redraw() {
        // set screen brightness
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = state.getScreenBrightness()/1000f;
        getWindow().setAttributes(layout);

        // set background intensity
        //backView.setBackgroundColor(Color.argb(state.getBackAlpha(), state.getBackGrey(), state.getBackGrey(), state.getBackGrey()));
        backView.invalidate();

        // set simulus intensity
        //stimulusView.setBackgroundColor(Color.argb(state.getStimulusAlpha(), state.getStimulusGrey(), state.getStimulusGrey(), state.getStimulusGrey()));

        resetSetting();
    }



    private void resetSetting() {
        int alphaValue = backgroundChecked?state.getBackAlpha():state.getStimulusAlpha();
        alphaSeekBar.setProgress(alphaValue);
        tvAlphaValue.setText(Integer.toString(alphaValue));

        int greyValue = backgroundChecked?state.getBackGrey():state.getStimulusGrey();
        greySeekBar.setProgress(greyValue);
        tvGreyValue.setText(Integer.toString(greyValue));

        screenBrightSeekBar.setProgress(state.getScreenBrightness());
        tvScreenBrightValue.setText(Integer.toString(state.getScreenBrightness()));
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return false;
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
