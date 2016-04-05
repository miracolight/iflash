package com.vvavy.measurelab.listener;

import android.view.View;
import android.widget.SeekBar;

import com.vvavy.measurelab.updator.DisplayUpdator;
import com.vvavy.measurelab.updator.StateUpdator;

/**
 * Created by qingdi on 3/23/16.
 */
public class ButtonListener implements View.OnClickListener {
    private StateUpdator stateUpdator;
    private DisplayUpdator displayUpdator;
    private int delta;

    public ButtonListener(StateUpdator stateUpdator, DisplayUpdator displayUpdator, int delta) {
        this.stateUpdator = stateUpdator;
        this.displayUpdator = displayUpdator;
        this.delta = delta;
    }

    @Override
    public void onClick(View view) {
        stateUpdator.deltaUpdate(displayUpdator.isBackgroundChecked(), delta);
        displayUpdator.redraw();
    }
}
