package com.vvavy.measurelab.listener;

import android.widget.SeekBar;

import com.vvavy.measurelab.MainActivity;
import com.vvavy.measurelab.updator.DisplayUpdator;
import com.vvavy.measurelab.updator.StateUpdator;
import com.vvavy.measurelab.updator.impl.AlphaStateUpdator;

/**
 * Created by qingdi on 3/23/16.
 */
public class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
    private StateUpdator stateUpdator;
    private DisplayUpdator displayUpdator;


    public SeekBarListener(StateUpdator stateUpdator, DisplayUpdator displayUpdator) {
        this.stateUpdator = stateUpdator;
        this.displayUpdator = displayUpdator;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        stateUpdator.update(displayUpdator.isBackgroundChecked(), progress);
        displayUpdator.redraw();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
