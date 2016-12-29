package com.tongchuang.perimetrypro.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;


import com.tongchuang.perimetrypro.activity.PatientMainActivity;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings.EXAM_FIELD_OPTION;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;

/**
 * Created by qingdi on 3/4/16.
 * Updated by Ming on 10/27/16 - graphic alignment
 */
public class PatientMainActivityView extends View{


    private Paint paint = new Paint();

    private ExamSettings examSettings;
    private EXAM_FIELD_OPTION fieldOption;

    //   private static String INFO_MESSAGE = "田";   //"请调节聚焦旋钮\n轻松看清本段文字后";
    //   private static String INFO_MESSAGE_LEFT = "O";  // "点击手中按钮开始测试左眼";
    //   private static String INFO_MESSAGE_RIGHT = "O";    //"点击手中按钮开始测试右眼";

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

        paint.setColor(Color.rgb(200,200,200));   //.WHITE);
        float positionSign = 1;
        if (fieldOption == EXAM_FIELD_OPTION.LEFT) {
            positionSign = -1;
        }
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        RectF C1 = new RectF(fixation.x - 180, fixation.y-180, fixation.x+180, fixation.y+180); // left top right bottom  //circle box
        RectF C2 = new RectF(fixation.x - 120, fixation.y-120, fixation.x+120, fixation.y+120);  //circle box
        RectF C3 = new RectF(fixation.x - 60, fixation.y-60, fixation.x+60, fixation.y+60);  //circle box
        RectF C4 = new RectF(fixation.x +30*positionSign-12, fixation.y-30, fixation.x +30*positionSign+12, fixation.y-13);  //eye
        RectF C5 = new RectF(fixation.x - 72, fixation.y-72, fixation.x+72, fixation.y+72);  //circle box
        RectF Cblind = new RectF(fixation.x +200*positionSign-3, fixation.y+40-3, fixation.x +200*positionSign+3, fixation.y+40+3);
        canvas.drawArc(C1,0, 360, false, paint);
        canvas.drawArc(C2,0, 360, false, paint);
        canvas.drawArc(C3,30, 118, false, paint);
        canvas.drawArc(C3,150, 118, false, paint);
        canvas.drawArc(C3,270, 118, false, paint);
        // paint.setStrokeWidth(6);

        // canvas.drawRect(C5, paint);  //this is the rectangle box

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(0, 127, 0));
        canvas.drawArc(C4, 135, 270, true, paint);  //eye left/right

        paint.setColor(Color.rgb(255, 255, 255));
        canvas.drawCircle(fixation.x + 200*positionSign, fixation.y +40, 3, paint);   //blind spot

        paint.setColor(Color.rgb(255, 165, 0)); //RED);
        canvas.drawCircle(fixation.x, fixation.y, examSettings.getFixationRadius(), paint);
    }

}