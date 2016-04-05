package com.vvavy.visiondemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.vvavy.visiondemo.app.PerimetryExam;
import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.app.object.PerimetryPoint;

/**
 * Created by qingdi on 3/4/16.
 */
public class ExamView extends View{


    private Paint paint = new Paint();


    private PerimetryPoint checkPoint;

    private PerimetryExam       exam;



    public ExamView(Context context, PerimetryExam exam) {
        super(context);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        this.exam = exam;
    }
    public void setCheckPoint(PerimetryPoint checkPoint) {
        this.checkPoint = checkPoint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // smooths
        // opacity
        //p.setAlpha(0x80); //
        if (exam.isDone()) {
            //show result;
            for (PerimetryPoint p : exam.getResultPoints()) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(PerimetryExam.RESULT_DISPLAY_SIZE);
                canvas.drawText(p.getIntensity().getDbInt()+"db",
                        (exam.isLeftEyeExam()?exam.getCenterLeftX():exam.getCenterRightX())+p.getPoint().x*exam.getGap(),
                        exam.getCenterY()+p.getPoint().y*exam.getGap(), paint);
            }
            return;
        }
        if (checkPoint != null) {
            paint.setColor(checkPoint.getColor());
            Point p = checkPoint.getPoint();
            canvas.drawCircle((exam.isLeftEyeExam()?exam.getCenterLeftX():exam.getCenterRightX())+p.x*exam.getGap(),
                    exam.getCenterY()+p.y*exam.getGap(), exam.getRadius(), paint);
        }
        paint.setColor(Color.RED);
        canvas.drawCircle(exam.getCenterLeftX(), exam.getCenterY(), Config.CENTER_RADIUS, paint);
        canvas.drawCircle(exam.getCenterRightX(), exam.getCenterY(), Config.CENTER_RADIUS, paint);
    }


}
