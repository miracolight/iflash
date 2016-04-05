package com.vvavy.measurelab.updator.impl;

import com.vvavy.measurelab.object.State;
import com.vvavy.measurelab.updator.StateUpdator;

/**
 * Created by qingdi on 3/23/16.
 */
public class ScreenBrightUpdator implements StateUpdator {
    private State   state;

    public ScreenBrightUpdator(State state) {
        this.state = state;
    }

    @Override
    public void update(boolean isBackgroundChecked, int value) {
        state.setScreenBrightness(value);
    }

    public void deltaUpdate(boolean isBackgroundChecked, int delta){
        int newBright = state.getScreenBrightness()+delta;
        if (newBright<=1000 && newBright>=0) {
            state.setScreenBrightness(newBright);
        }
    }
}
