package com.tongchuang.perimetrypro.perimetry.stimulus.impl;

import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;

/**
 * Created by qingdi on 10/4/16.
 */
public abstract class AbstractStimulusRunner implements StimulusRunner {

    protected boolean inTrainingMode = false;
    @Override
    public void setForTraining(boolean forTraining) {
        inTrainingMode = true;
    }

}
