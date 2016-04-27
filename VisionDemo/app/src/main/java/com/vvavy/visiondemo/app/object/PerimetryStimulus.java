package com.vvavy.visiondemo.app.object;

import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by qingdi on 3/7/16.
 */
public class PerimetryStimulus {
    private Point       point;
    private Intensity   intensity;
    private boolean     detected = false;
    private int         dbStep = 4;
    private boolean     done = false;

    public PerimetryStimulus(Point point, Intensity indensity) {
        this.point = point;
        this.intensity = indensity;
    }

    public Point getPoint() {
        return point;
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public void setIntensity(Intensity intensity) {
        if (intensity == null) {
            System.out.println("am here");
        }
        this.intensity = intensity;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    public int getDbStep() {
        return dbStep;
    }

    public void setDbStep(int dbStep) {
        this.dbStep = dbStep;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public float getScreenBrightness() {
        return intensity.getScreenBrightness();
    }

    public int getStimulusColor() {
        System.out.println("in getStimulusColor: stimulusAlpha="+intensity.getStimulusAlpha()+"; stimulusGreyscale="+intensity.getStimulusGreyscale());
        return Color.argb(intensity.getStimulusAlpha(), intensity.getStimulusGreyscale(), intensity.getStimulusGreyscale(), intensity.getStimulusGreyscale());
    }

    public int getBgColor() {
        System.out.println("in getBgColor: bgAlpha="+intensity.getBgAlpha()+"; bgGreyscale="+intensity.getBgGreyscale());
        return Color.argb(intensity.getBgAlpha(), intensity.getBgGreyscale(), intensity.getBgGreyscale(), intensity.getBgGreyscale());
    }
}
