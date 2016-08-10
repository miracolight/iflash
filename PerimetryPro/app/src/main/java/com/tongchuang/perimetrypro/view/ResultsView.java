package com.tongchuang.perimetrypro.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.tongchuang.perimetrypro.perimetry.exam.object.ExamResult;

/**
 * Created by qingdi on 3/4/16.
 */
public class ResultsView extends View{

    /**
     * The scaling factor for a single zoom 'step'.
     */
    private static final float ZOOM_AMOUNT = 0.25f;

    // Viewport extremes. See mCurrentViewport for a discussion of the viewport.
    private static final float AXIS_X_MIN = -1f;
    private static final float AXIS_X_MAX = 1f;
    private static final float AXIS_Y_MIN = -1f;
    private static final float AXIS_Y_MAX = 1f;

    private RectF mCurrentViewport = new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);

    // State objects and values related to gesture tracking.
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetectorCompat mGestureDetector;

    private float   mAxisBaseX = 0f;
    private float   mAxisBaseY = 0f;
    private float   mScaleFactor = 1f;


    private ExamResult examResult;
    private Paint paint = new Paint();



    public ResultsView(Context context, ExamResult examResult) {
        super(context);

        this.examResult = examResult;


        // Sets up interactions
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
    }

    /**
     * The scale listener, used for handling multi-finger scale gestures.
     */
    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        private float beginFocusX, beginFocusY;

        @Override
        public boolean	onScaleBegin(ScaleGestureDetector detector) {
            beginFocusX = detector.getFocusX();
            beginFocusY = detector.getFocusY();
            return true;
        }


        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            //System.out.println("mSaleFactor="+mScaleFactor);
            ViewCompat.postInvalidateOnAnimation(ResultsView.this);

            return true;
        }
    };

    private void resetGesture() {
        mAxisBaseX = 0f;
        mAxisBaseY = 0f;
        mScaleFactor = 1f;
    }

    /**
     * The gesture listener, used for handling simple gestures such as double touches, scrolls,
     * and flings.
     */
    private final GestureDetector.SimpleOnGestureListener mGestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            resetGesture();
            //System.out.println("in onDoubleTap");
            ViewCompat.postInvalidateOnAnimation(ResultsView.this);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mAxisBaseX -= distanceX;
            mAxisBaseY -= distanceY;
            //System.out.println("onScroll mAxisBaseX="+mAxisBaseX+"; mAxisBaseY="+mAxisBaseY);
            ViewCompat.postInvalidateOnAnimation(ResultsView.this);
            return true;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mScaleGestureDetector.onTouchEvent(event);
        retVal = mGestureDetector.onTouchEvent(event) || retVal;
        return retVal || super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        /*
        for (PerimetryStimulus p : examResult.getPerimetryStimulus()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(DefaultPerimetryTestServiceImpl.RESULT_DISPLAY_SIZE);
            canvas.drawText(Integer.toString(p.getIntensity().getDb())+", ",
                    mAxisBaseX+(p.getPoint().x*mScaleFactor), mAxisBaseY+(p.getPoint().y*mScaleFactor), paint);
        }
        */
    }


}
