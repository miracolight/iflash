package com.tongchuang.perimetrypro.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.tongchuang.perimetrypro.activity.ExamActivity;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;

/**
 * Created by qingdi on 3/4/16.
 */
public class ExamStandbyView extends View{

    public ExamStandbyView(ExamActivity examActivity) {
        super(examActivity);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundColor(Color.BLACK);
    }

}
