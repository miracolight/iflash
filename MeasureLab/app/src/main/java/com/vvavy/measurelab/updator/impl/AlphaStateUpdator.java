package com.vvavy.measurelab.updator.impl;

import com.vvavy.measurelab.object.State;
import com.vvavy.measurelab.updator.StateUpdator;

/**
 * Created by qingdi on 3/23/16.
 */
public class AlphaStateUpdator implements StateUpdator {
    private State   state;

    public AlphaStateUpdator(State state) {
        this.state = state;
    }

    @Override
    public void update(boolean isBackgroundChecked, int value) {
        if (isBackgroundChecked) {
            state.setBackAlpha(value);
        } else {
            state.setStimulusAlpha(value);
        }
    }

    public void deltaUpdate(boolean isBackgroundChecked, int delta){
        if (isBackgroundChecked) {
            int newAlpha = state.getBackAlpha()+delta;
            if (newAlpha<=255 && newAlpha>=0) {
                state.setBackAlpha(newAlpha);
            }
        } else {
            int newAlpha = state.getStimulusAlpha()+delta;
            if (newAlpha<=255 && newAlpha>=0) {
                state.setStimulusAlpha(newAlpha);
            }
        }
    }
}
