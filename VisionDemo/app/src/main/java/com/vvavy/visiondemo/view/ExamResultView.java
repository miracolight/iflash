package com.vvavy.visiondemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.vvavy.visiondemo.activity.ResultDetailsActivity;
import com.vvavy.visiondemo.app.exam.PerimetryExam;
import com.vvavy.visiondemo.app.exam.impl.DefaultPerimetryExam;
import com.vvavy.visiondemo.app.model.ExamResult;
import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.app.object.PerimetryStimulus;

/**
 * Created by qingdi on 3/4/16.
 */
public class ExamResultView extends View{

    private ExamResult   examResult;
    private Paint paint = new Paint();



    public ExamResultView(Context context, ExamResult examResult) {
        super(context);

        this.examResult = examResult;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (PerimetryStimulus p : examResult.getPerimetryStimulus()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(DefaultPerimetryExam.RESULT_DISPLAY_SIZE);
            canvas.drawText(Integer.toString(p.getIntensity().getDb())+", ",
                    p.getPoint().x, p.getPoint().y, paint);
        }
    }


}
