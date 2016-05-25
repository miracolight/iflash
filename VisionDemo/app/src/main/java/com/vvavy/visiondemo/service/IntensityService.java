package com.vvavy.visiondemo.service;

import com.vvavy.visiondemo.object.Intensity;
import com.vvavy.visiondemo.object.PerimetryStimulus;

/**
 * Created by qingdi on 4/6/16.
 */
public interface IntensityService {

    public void adjustIntensity(PerimetryStimulus checkPoint);

    public Intensity getIntensity(int db);
}
