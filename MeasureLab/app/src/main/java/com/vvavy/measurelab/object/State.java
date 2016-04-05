package com.vvavy.measurelab.object;

import android.view.View;

/**
 * Created by qingdi on 3/23/16.
 */
public class State {

    private int         backAlpha = 255;
    private int         backGrey = 0;
    private int         stimulusAlpha = 255;
    private int         stimulusGrey = 255;
    private int         screenBrightness = 1000;

    public int getBackAlpha() {
        return backAlpha;
    }

    public void setBackAlpha(int backAlpha) {
        this.backAlpha = backAlpha;
    }

    public int getBackGrey() {
        return backGrey;
    }

    public void setBackGrey(int backGrey) {
        this.backGrey = backGrey;
    }

    public int getStimulusAlpha() {
        return stimulusAlpha;
    }

    public void setStimulusAlpha(int stimulusAlpha) {
        this.stimulusAlpha = stimulusAlpha;
    }

    public int getStimulusGrey() {
        return stimulusGrey;
    }

    public void setStimulusGrey(int stimulusGrey) {
        this.stimulusGrey = stimulusGrey;
    }

    public int getScreenBrightness() {
        return screenBrightness;
    }

    public void setScreenBrightness(int screenBrightness) {
        this.screenBrightness = screenBrightness;
    }
}
