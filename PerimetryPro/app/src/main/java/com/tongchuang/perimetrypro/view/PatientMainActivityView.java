package com.tongchuang.perimetrypro.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;


import com.tongchuang.perimetrypro.activity.PatientMainActivity;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings.EXAM_FIELD_OPTION;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;

/**
 * Created by qingdi on 3/4/16.
 */
public class PatientMainActivityView extends View{


    private Paint paint = new Paint();

    private ExamSettings examSettings;
    private EXAM_FIELD_OPTION fieldOption;

    private static String INFO_MESSAGE = "请调节聚焦旋钮\n轻松看清本段文字后";
    private static String INFO_MESSAGE_LEFT = "点击手中按钮开始测试左眼";
    private static String INFO_MESSAGE_RIGHT = "点击手中按钮开始测试右眼";

    public PatientMainActivityView(PatientMainActivity activity, ExamSettings examSettings, EXAM_FIELD_OPTION fieldOption) {
        super(activity);
        this.examSettings = examSettings;
        this.fieldOption = fieldOption;
    }

    public EXAM_FIELD_OPTION getFieldOption() {
        return fieldOption;
    }

    public void setFieldOption(EXAM_FIELD_OPTION fieldOption) {
        this.fieldOption = fieldOption;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (fieldOption == null) {
            return;
        }
        Point fixation = fieldOption==EXAM_FIELD_OPTION.LEFT?examSettings.getLeftFixation():examSettings.getRightFixation();
        paint.setColor(Color.RED);
        canvas.drawCircle(fixation.x, fixation.y, examSettings.getFixationRadius(), paint);

        final float testTextSize = 30f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(INFO_MESSAGE, 0, INFO_MESSAGE.length(), bounds);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        canvas.drawText(INFO_MESSAGE, fixation.x, fixation.y-(int)(bounds.height()*1.5), paint);

        String message = fieldOption==EXAM_FIELD_OPTION.LEFT?INFO_MESSAGE_LEFT:INFO_MESSAGE_RIGHT;
        canvas.drawText(message, fixation.x, fixation.y+(int)(bounds.height()*1.5)+examSettings.getFixationRadius()*2, paint);
    }

}
