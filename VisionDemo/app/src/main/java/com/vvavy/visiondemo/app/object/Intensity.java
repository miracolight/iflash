package com.vvavy.visiondemo.app.object;

import com.vvavy.visiondemo.app.IntensityHandler;

/**
 * Created by qingdi on 3/14/16.
 */
public class Intensity {
    private int     dbInt;
    private double  dbDouble;
    private int     alpha;
    private int     grey;

    public Intensity(int dbInt, double dbDouble, int alpha, int grey) {
        this.dbInt = dbInt;
        this.dbDouble = dbDouble;
        this.alpha = alpha;
        this.grey = grey;
    }

    public Intensity(int alpha, int grey) {
        this.dbDouble = IntensityHandler.calcIntensityDB(alpha, grey);
        this.dbInt = (int)Math.round(dbDouble);
        this.alpha = alpha;
        this.grey = grey;
    }

    public Intensity(Intensity intensity) {
        this.dbInt = intensity.getDbInt();
        this.dbDouble = intensity.getDbDouble();
        this.alpha = intensity.getAlpha();
        this.grey = intensity.getGrey();
    }

    public int getDbInt() {
        return dbInt;
    }

    public void setDbInt(int dbInt) {
        this.dbInt = dbInt;
    }

    public double getDbDouble() {
        return dbDouble;
    }

    public void setDbDouble(double dbDouble) {
        this.dbDouble = dbDouble;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getGrey() {
        return grey;
    }

    public void setGrey(int grey) {
        this.grey = grey;
    }

    @Override
    public String toString() {
        return "Intensity{" +
                "dbInt=" + dbInt +
                ", dbDouble=" + dbDouble +
                ", alpha=" + alpha +
                ", grey=" + grey +
                '}';
    }

}
