package com.vvavy.measurelab.updator;

import com.vvavy.measurelab.object.State;

/**
 * Created by qingdi on 3/23/16.
 */
public interface StateUpdator {
    public void update(boolean isBackgroundChecked, int value);

    public void deltaUpdate(boolean backgroundChecked, int delta);
}
