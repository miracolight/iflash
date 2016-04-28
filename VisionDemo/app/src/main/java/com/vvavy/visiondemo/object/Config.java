package com.vvavy.visiondemo.object;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;

import java.util.Random;

/**
 * Created by qingdi on 3/8/16.
 */
public class Config {

    public static final String SHARED_PREF_NAME = "VISION_DEMO_CONFIG";

    public static final String  PROP_NAME_CENTER_LEFTX = "CENTER_LEFT_X";
    public static final String  PROP_NAME_CENTER_RIGHTX = "CENTER_RIGHT_X";
    public static final String  PROP_NAME_CENTERY = "CENTER_Y";
    public static final String  PROP_NAME_RADIUS = "RADIUS";
    public static final String  PROP_NAME_GAP = "GAP";
    public static final String  PROP_NAME_NUMOFFIXATION = "NUM_OF_FIXATION";
    public static final String  PROP_NAME_PROMPTTIME = "PROMPT_TIME";
    public static final String  PROP_NAME_NUMOFPOINTS = "NUM_OF_POINTS";
    public static final String  PROP_NAME_INITDB = "INIT_DB";

    public static final int     DEFAULT_RADIUS = 5;
    public static final int     DEFAULT_GAP = 20;
    public static final int     DEFAULT_NUMOFFIXATION = 2;  // possible values: 1, 2
    public static final int     DEFAULT_PROMPTTIME = 100; // 100ms
    public static final int     DEFAULT_NUMOFPOINTS = 5;
    public static final int     DEFAULT_INITDB = 10;

    public static final int     MAX_GREY = 0xFF;
    public static final int     MAX_ALPHA = 0xFF;
    public static final int     MAX_RADIUS = 30;
    public static final int     MAX_GAP = 100;
    public static final int     MAX_NUMOFPOINTS = 76;
    public static final int     CENTER_RADIUS = 6;

    private int centerLeftX, centerRightX, centerY;
    private int radius, gap, numPoints, numFixations, promptTime, initDb;


    private Config (int centerLeftX, int centerRightX, int centerY, int numFixations,
                    int promptTime, int radius, int gap, int numPoints, int initDb) {
        this.centerLeftX = centerLeftX;
        this.centerRightX = centerRightX;
        this.centerY = centerY;
        this.numFixations = numFixations;
        this.promptTime = promptTime;
        this.radius = radius;
        this.gap = gap;
        this.numPoints = numPoints;
        this.initDb = initDb;

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

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public int getNumFixations() {
        return numFixations;
    }

    public void setNumFixations(int numFixations) {
        this.numFixations = numFixations;
    }

    public int getPromptTime() {
        return promptTime;
    }

    public void setPromptTime(int promptTime) {
        this.promptTime = promptTime;
    }

    public int getInitDb() {
        return initDb;
    }

    public void setInitDb(int initDb) {
        this.initDb = initDb;
    }

    public static Config loadConfig(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Point size = getDisplaySize(activity);
        Config config = new Config(sharedPref.getInt(PROP_NAME_CENTER_LEFTX, (int)(size.x*0.25)),
                                   sharedPref.getInt(PROP_NAME_CENTER_RIGHTX, (int)(size.x*0.75)),
                                   sharedPref.getInt(PROP_NAME_CENTERY, size.y/2),
                                   sharedPref.getInt(PROP_NAME_NUMOFFIXATION, DEFAULT_NUMOFFIXATION),
                                   sharedPref.getInt(PROP_NAME_PROMPTTIME, DEFAULT_PROMPTTIME),
                                   sharedPref.getInt(PROP_NAME_RADIUS, DEFAULT_RADIUS),
                                   sharedPref.getInt(PROP_NAME_GAP, DEFAULT_GAP),
                                   sharedPref.getInt(PROP_NAME_NUMOFPOINTS, DEFAULT_NUMOFPOINTS),
                                   sharedPref.getInt(PROP_NAME_INITDB, DEFAULT_INITDB));
        return config;
    }

    public static Point getDisplaySize(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        return size;
    }

    public void saveConfig(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PROP_NAME_CENTER_LEFTX, centerLeftX);
        editor.putInt(PROP_NAME_CENTER_RIGHTX, centerRightX);
        editor.putInt(PROP_NAME_CENTERY, centerY);
        editor.putInt(PROP_NAME_NUMOFFIXATION, numFixations);
        editor.putInt(PROP_NAME_PROMPTTIME, promptTime);
        editor.putInt(PROP_NAME_GAP, gap);
        editor.putInt(PROP_NAME_RADIUS, radius);
        editor.putInt(PROP_NAME_NUMOFPOINTS, numPoints);
        editor.putInt(PROP_NAME_INITDB, initDb);
        editor.commit();
    }


    public void reset(Activity activity) {
        Point size = getDisplaySize(activity);
        centerLeftX = (int)(size.x*0.25);
        centerRightX = (int)(size.x*0.75);
        centerY = size.y/2;
        numFixations = DEFAULT_NUMOFFIXATION;
        promptTime = DEFAULT_PROMPTTIME;
        radius = DEFAULT_RADIUS;
        gap = DEFAULT_GAP;
        numPoints = DEFAULT_NUMOFPOINTS;
        initDb = DEFAULT_INITDB;
    }

    public static int getCenterRadius() {
        Random randomGenerator = new Random();
        return CENTER_RADIUS - randomGenerator.nextInt(CENTER_RADIUS/2);
    }
}
