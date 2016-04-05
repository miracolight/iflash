package com.vvavy.measurelab.updator.impl;

import com.vvavy.measurelab.object.State;
import com.vvavy.measurelab.updator.StateUpdator;

/**
 * Created by qingdi on 3/23/16.
 */
public class GreyStateUpdator implements StateUpdator {
    private State   state;

    public GreyStateUpdator(State state) {
        this.state = state;
    }

    @Override
    public void update(boolean isBackgroundChecked, int value) {
        if (isBackgroundChecked) {
            state.setBackGrey(value);
        } else {
            state.setStimulusGrey(value);
        }
    }

    public void deltaUpdate(boolean isBackgroundChecked, int delta){
        if (isBackgroundChecked) {
            int newGrey = state.getBackGrey()+delta;
            if (newGrey<=255 && newGrey>=0) {
                state.setBackGrey(newGrey);
            }
        } else {
            int newGrey = state.getStimulusGrey()+delta;
            if (newGrey<=255 && newGrey>=0) {
                state.setStimulusGrey(newGrey);
            }
        }
    }
}
