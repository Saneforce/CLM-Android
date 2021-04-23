package saneforce.sanclm.util;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;

import saneforce.sanclm.activities.DetailingFullScreenImageViewActivity;
import saneforce.sanclm.activities.PdfViewActivity;
import saneforce.sanclm.adapter_class.MyCustomPagerAdapter;

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;
    static MyCustomPagerAdapter.touchGesture touchgesture;
    Context context;
    boolean ispdf=false;
   static String path;
   ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    private float mScaleFactor1 = 1.f;
    private static int SWIPE_THRESHOLD = 100;
    private static  int SWIPE_VELOCITY_THRESHOLD = 100;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    boolean isZoom=false;
    int normalcount=0;
    static  GridSelectionList   gridSelectionList;

    public OnSwipeTouchListener (Context ctx,String x){
        Log.v("swipe_touch_list","context"+x);
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        mScaleDetector = new ScaleGestureDetector(ctx, new ScaleListener());
    }
    public OnSwipeTouchListener (Context ctx){
        Log.v("swipe_touch_list","context");
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        mScaleDetector = new ScaleGestureDetector(ctx, new ScaleListener());

    }
    public OnSwipeTouchListener (Context ctx,boolean value){
        Log.v("swipe_touch_list","context");
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        mScaleDetector = new ScaleGestureDetector(ctx, new ScaleListener());
        SWIPE_THRESHOLD=-100;
        SWIPE_VELOCITY_THRESHOLD = -1000;

    }

    public OnSwipeTouchListener(Context ctx,boolean isPdf,String path){
        //Log.v("printing_total_path",path);
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        mScaleDetector = new ScaleGestureDetector(ctx, new ScaleListener());
        ispdf=isPdf;
        this.context=ctx;
        if(path!=null) {
            this.path = path;
            Log.v("pdf_paths_swipe", this.path+"posss"+MyCustomPagerAdapter.presentSlidePos);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        int action = event.getActionMasked();
        Log.v("fling_inside_touch","motion_called");
        switch (action) {

                              /*  case MotionEvent.ACTION_DOWN:
                                    Intent i=new Intent(context,PdfViewActivity.class);
                                    i.putExtra("PdfUrl",slideUrl1);
                                    context.startActivity(i);
*/
            case MotionEvent.ACTION_DOWN:
                Log.v("double_fingetddd","are_called");
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                normalcount=1;
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v("double_finget", "called"+event.getPointerCount());

                int count = event.getPointerCount(); // Number of 'fingers' in this time
                if (count == 2) {
                    oldDist = spacing(event);
                    if (oldDist > 5f) {
                        Log.v("double_fingetzz", "called");
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    normalcount=2;
                    Log.v("double_fingetssss", "called"+System.currentTimeMillis());

                    if (MyCustomPagerAdapter.doubleTouchCount == 0) {
                        MyCustomPagerAdapter.doubleTouchCount = 1;
                        if(touchgesture!=null)
                        touchgesture.touches();
                        // rll_overlay.setVisibility(View.VISIBLE);
                    } else {
                        MyCustomPagerAdapter.doubleTouchCount = 0;
                        if(touchgesture!=null)
                        touchgesture.unTouches();
                        //rll_overlay.setVisibility(View.GONE);
                    }
                }
                break;
                case    MotionEvent.ACTION_MOVE:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("action_move","are_called"+normalcount);
                            if(normalcount==2){
                                isZoom=true;
                                MyCustomPagerAdapter.doubleTouchCount = 0;
                                if(touchgesture!=null)
                                    touchgesture.unTouches();
                            }
                        }
                    },400);
                    if (mode == DRAG && isZoom) {
                        // ...
                        /*matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x,
                                event.getY() - start.y);
                        drag(matrix);*/
                    }
/*
                    else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
*/
                    break;

            case    MotionEvent.ACTION_UP:
                normalcount=3;
                        Log.v("double_fingetssss_up", "called"+System.currentTimeMillis());
                        break;



        }
        return true;
    }

    private final class GestureListener extends SimpleOnGestureListener {




        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.v("swipe_gdouble","are_down_gest");
            if(ispdf) {
                Log.d("TEST_er", "onDoubleTap"+MyCustomPagerAdapter.getPAth());
                Intent i = new Intent(context, PdfViewActivity.class);
                i.putExtra("PdfUrl", MyCustomPagerAdapter.getPAth());
                context.startActivity(i);
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.v("swipe_gesture_det","are_down_gest");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            Log.v("fling_inside_","motion_called");

            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                Log.v("fling_inside_","motion_called"+diffY+"diffx"+diffX);

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        Log.v("on_swipe_up","motion_called");
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public void onZoom(float scale){

    }

    public void drag(Matrix matrix){

    }

    public static void bindTouchGesture(MyCustomPagerAdapter.touchGesture touchgesturess){
        touchgesture=touchgesturess;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.v("scale_begin_are","strated");
            return super.onScaleBegin(detector);

        }

      /*  @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            super.onScaleBegin(detector);

            Log.v("scaleFactor_value_111","scale_started"+"");
           *//* if(!isArrow) {
                if (checkSelectedpath(prev_X, prev_Y)) {
                    isDoublePinch = true;
                }
            }*//*
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            Log.v("The_end_scaling","are_enddd");
            //isDoublePinch = false;
        }
*/
      @Override
      public void onScaleEnd(ScaleGestureDetector detector) {
          super.onScaleEnd(detector);

          Log.v("The_end_scaling","are_enddd");
          //isDoublePinch = false;
      }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            Log.v("printing_scalefactor",mScaleFactor+"zoo"+isZoom);
            //mScaleFactor = Math.max(0.3f, Math.min(mScaleFactor, 1.3f));

            if(isZoom) {
                mScaleFactor *= detector.getScaleFactor();
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
                Log.v("printing_scaltrue",mScaleFactor+"");
              //  onZoom(mScaleFactor);
            }
          /*

            if(isDoublePinch) {
                isScale = true;
                // Log.v("scaleFactor_value_11",mScaleFactor+" original "+detector.getScaleFactor());
                checkPresentScale=mScaleFactor;
                // Don't let the object get too small or too large.
                mScaleFactor = Math.max(0.3f, Math.min(mScaleFactor, 1.3f));
                Log.v("scaleFactor_value",checkPresentScale+" yyy "+checkPrevScale);
                storePrevScale=mScaleFactor;*/


              /*  if(mScaleFactor>1){
                   // mScaleFactor1=1.10f;
                }
                else
                    //mScaleFactor1=0.90f;*/
             /*   if(mScaleFactor>0.){
                    mScaleFactor1=1.01f;
                }
                else
                    mScaleFactor1=0.1f;*/
                // mScaleFactor=1.01f;
/*
                Log.v("scaleFactor_value_sss",checkPresentScale+" gh "+mScaleFactor1);
                Matrix scaleMatrix = new Matrix();
                RectF rectF = new RectF();
                shapePath.get(selectedPathIndex).computeBounds(rectF, true);
                scaleMatrix.postScale(mScaleFactor, mScaleFactor, rectF.centerX(), rectF.centerY());
                //scaleMatrix.postTranslate(detector.getFocusX() + detector.getFocusShiftX(), detector.getFocusY() + detector.getFocusShiftY());

                //scaleMatrix.setScale(sX, sY);
                checkPrevScale=checkPresentScale;
                shapePath.get(selectedPathIndex).transform(scaleMatrix);
                invalidate();*/


            //}

            return true;
        }
    }
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    public static  void bindImageVisible(GridSelectionList  gg){
        gridSelectionList=gg;
    }

}
