package com.tongchuang.perimetrypro.perimetry.stimulus.object;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.util.IntensityUtil;

/**
 * Created by qingdi on 8/8/16.
 */
public class StimulusInstance {
    private String  positionCode;
    private Point   point;
    private Intensity   intensity;

    public StimulusInstance(String positionCode, Point point, Intensity intensity) {
        this.positionCode = positionCode;
        this.point = point;
        this.intensity = intensity;
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public int getBackgroundColor() {
        return IntensityUtil.getBackgroundColor(intensity);
    }

    public int getStimulusColor() {
        return IntensityUtil.getStimulusColor(intensity);
    }


}
