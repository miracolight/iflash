package com.tongchuang.perimetrypro.perimetry.stimulus.impl;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.tongchuang.perimetrypro.R;

/**
 * Created by qingdi on 10/17/16.
 */
public class FalseNegativePostProcessor {
    private SoundPool   soundPool;
    private int         sourceId;
    public FalseNegativePostProcessor(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
        sourceId = soundPool.load(context, R.raw.warning, 0);
    }

    public void postProcess(boolean stimulusDetected) {
        if (!stimulusDetected) {
            int playId = soundPool.play(sourceId, 2, 2, 0, 0, 1);
            System.out.println("aimu_log: soundPool playId="+playId);
        }
    }
}
