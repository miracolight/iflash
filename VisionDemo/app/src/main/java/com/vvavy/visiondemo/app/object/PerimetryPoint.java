package com.vvavy.visiondemo.app.object;

import android.graphics.Color;
import android.graphics.Point;

import com.vvavy.visiondemo.app.IntensityHandler;

/**
 * Created by qingdi on 3/7/16.
 */
public class PerimetryPoint {
    private Point               point;
    private IntensityHandler    intensityHandler;
    private boolean             visible = false;

    public PerimetryPoint(Point point) {
        this.point = point;
        this.intensityHandler = new IntensityHandler();
    }

    public int getColor() {
        Intensity intensity = intensityHandler.getIntensity();
        int color = Color.argb(intensity.getAlpha(), intensity.getGrey(), intensity.getGrey(), intensity.getGrey());
        return color;
    }

    public Intensity getIntensity() {
        return intensityHandler.getIntensity();
    }

    public void setIntensity(Intensity intensity) {
        intensityHandler.setIntensity(intensity);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Point getPoint() {
        return point;
    }

    public void adjustIntensity() {
        intensityHandler.adjustIntensity(visible);
    }

    public boolean isDone() {
        return intensityHandler.isDone();
    }
}
