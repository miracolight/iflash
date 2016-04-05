package com.vvavy.visiondemo.app;

import android.graphics.Point;

import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.app.object.PerimetryPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by qingdi on 3/8/16.
 */
public class PerimetryExam {

    public static final String LEFT_EYE_EXAM = "LEFT_EYE_EXAM";
    public static final int     RESULT_DISPLAY_SIZE = 40;
    private boolean leftEyeExam;
    private int     centerLeftX;
    private int     centerRightX;
    private int     centerY;
    private int     radius;
    private int     gap;

    private List<PerimetryPoint>    points;
    private List<PerimetryPoint>    resultPoints;
    private int     currPointIndex;

    public PerimetryExam(List<PerimetryPoint> points, int centerLeftX, int centerRightX, int centerY, int radius, int gap) {
        this.points = points;
        this.resultPoints = new ArrayList<PerimetryPoint>();
        this.centerLeftX = centerLeftX;
        this.centerRightX = centerRightX;
        this.centerY = centerY;
        this.radius = radius;
        this.gap = gap;
    }

    public boolean isLeftEyeExam() {
        return leftEyeExam;
    }

    public void setLeftEyeExam(boolean leftEyeExam) {
        this.leftEyeExam = leftEyeExam;
    }

    public int getCenterLeftX() {
        return centerLeftX;
    }

    public void setCenterLeftX(int centerLeftX) {
        this.centerLeftX = centerLeftX;
    }

    public int getCenterRightX() {
        return centerRightX;
    }

    public void setCenterRightX(int centerRightX) {
        this.centerRightX = centerRightX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public List<PerimetryPoint> getPoints() {
        return points;
    }

    public void setPoints(List<PerimetryPoint> points) {
        this.points = points;
    }

    public PerimetryPoint getCheckPoint() {
        if (!points.isEmpty()) {
            // find the point
            currPointIndex = getRandomIndex();
            PerimetryPoint point = points.get(currPointIndex);
            return point;
        }
        return null;
    }

    private int getRandomIndex() {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(points.size());
    }

    public void processCheckPoint(PerimetryPoint checkPoint) {
        checkPoint.adjustIntensity();
        if (checkPoint.isDone()) {
            points.remove(currPointIndex);
            resultPoints.add(checkPoint);
        }
        System.out.println("points.size="+points.size());
    }

    public List<PerimetryPoint> getResultPoints() {
        return resultPoints;
    }

    public void setResultPoints(List<PerimetryPoint> resultPoints) {
        this.resultPoints = resultPoints;
    }

    public boolean isDone() {
        return points.isEmpty();
    }

    public static class Builder {

        private Config config;

        public Builder(Config config) {
            this.config = config;
        }

        public PerimetryExam create() {

            List<PerimetryPoint> points = new ArrayList<PerimetryPoint>();

            points.add(new PerimetryPoint(new Point(1, 1)));
            points.add(new PerimetryPoint(new Point(1, 3)));
            points.add(new PerimetryPoint(new Point(1, 5)));
            points.add(new PerimetryPoint(new Point(1, 7)));
            points.add(new PerimetryPoint(new Point(3, 1)));
            points.add(new PerimetryPoint(new Point(3, 3)));
            points.add(new PerimetryPoint(new Point(3, 5)));
            points.add(new PerimetryPoint(new Point(5, 1)));
            points.add(new PerimetryPoint(new Point(5, 3)));
            points.add(new PerimetryPoint(new Point(7, 1)));

            points.add(new PerimetryPoint(new Point(-1, 1)));
            points.add(new PerimetryPoint(new Point(-1, 3)));
            points.add(new PerimetryPoint(new Point(-1, 5)));
            points.add(new PerimetryPoint(new Point(-1, 7)));
            points.add(new PerimetryPoint(new Point(-3, 1)));
            points.add(new PerimetryPoint(new Point(-3, 3)));
            points.add(new PerimetryPoint(new Point(-3, 5)));
            points.add(new PerimetryPoint(new Point(-5, 1)));
            points.add(new PerimetryPoint(new Point(-5, 3)));
            points.add(new PerimetryPoint(new Point(-7, 1)));

            points.add(new PerimetryPoint(new Point(1, -1)));
            points.add(new PerimetryPoint(new Point(1, -3)));
            points.add(new PerimetryPoint(new Point(1, -5)));
            points.add(new PerimetryPoint(new Point(1, -7)));
            points.add(new PerimetryPoint(new Point(3, -1)));
            points.add(new PerimetryPoint(new Point(3, -3)));
            points.add(new PerimetryPoint(new Point(3, -5)));
            points.add(new PerimetryPoint(new Point(5, -1)));
            points.add(new PerimetryPoint(new Point(5, -3)));
            points.add(new PerimetryPoint(new Point(7, -1)));

            points.add(new PerimetryPoint(new Point(-1, -1)));
            points.add(new PerimetryPoint(new Point(-1, -3)));
            points.add(new PerimetryPoint(new Point(-1, -5)));
            points.add(new PerimetryPoint(new Point(-1, -7)));
            points.add(new PerimetryPoint(new Point(-3, -1)));
            points.add(new PerimetryPoint(new Point(-3, -3)));
            points.add(new PerimetryPoint(new Point(-3, -5)));
            points.add(new PerimetryPoint(new Point(-5, -1)));
            points.add(new PerimetryPoint(new Point(-5, -3)));
            points.add(new PerimetryPoint(new Point(-7, -1)));

            Collections.sort(points, new Comparator<PerimetryPoint>() {
                @Override
                public int compare(PerimetryPoint p1, PerimetryPoint p2) {
                    return ((p1.getPoint().x*p1.getPoint().x+p1.getPoint().y*p1.getPoint().y)-
                            (p2.getPoint().x*p2.getPoint().x+p2.getPoint().y*p2.getPoint().y));
                }
            });

            if (config.getNumPoints()<=points.size()) {
                points = points.subList(0, config.getNumPoints());
            }
            PerimetryExam exam = new PerimetryExam(points,
                    config.getCenterLeftX(),
                    config.getCenterRightX(),
                    config.getCenterY(),
                    config.getRadius(),
                    config.getGap());
            return exam;
        }
    }
}
