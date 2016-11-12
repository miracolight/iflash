package com.tongchuang.perimetrypro.perimetry.stimulus;

import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;

/**
 * Created by qingdi on 8/6/16.
 */
public interface StimulusSelector {
    public StimulusRunner getNextStimulus();
    public void updateCurrentProgress(ExamTask examTask);

}
