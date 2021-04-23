package saneforce.sanclm.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.webkit.WebView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import saneforce.sanclm.adapter_class.MyCustomPagerAdapter;

public class HelpWebView extends WebView {

    private GestureDetector gestureDetector;
    private AtomicBoolean mPreventAction = new AtomicBoolean(false);
    private AtomicLong mPreventActionTime = new AtomicLong(0);
    public static MyCustomPagerAdapter.touchGesture touchGestures;
    int doubleCount=0;
    static WebviewTopSwipe WebviewTopSwipe;

    public HelpWebView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public HelpWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public HelpWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public HelpWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int index = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        int pointId = event.getPointerId(index);

        Log.v("pointerId", String.valueOf(pointId));
        int action = event.getActionMasked();

        switch (action) {


            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v("double_finget", "called");
                int count = event.getPointerCount(); // Number of 'fingers' in this time
                if (count == 2) {
                    Log.v("double_fingetssss", "called");
                    if(doubleCount==0){
                doubleCount=1;
                touchGestures.touches();
            }
            else{
                doubleCount=0;
                touchGestures.unTouches();
            }
                }

        }
        // just use one(first) finger, prevent double tap with two and more fingers
        if (pointId == 0){
            gestureDetector.onTouchEvent(event);

            if (mPreventAction.get()){
                if (System.currentTimeMillis() - mPreventActionTime.get() > ViewConfiguration.getDoubleTapTimeout()){
                    mPreventAction.set(false);
                } else {
                    return true;
                }
            }

            return super.onTouchEvent(event);
        } else {

            return true;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        int height = (int) Math.floor(this.getContentHeight() * this.getScale());
        int webViewHeight = this.getMeasuredHeight();
        if(this.getScrollY() + webViewHeight >= height){
            Log.i("webview_end_", "reached");
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.v("double_tap","occur");
            mPreventAction.set(true);
            mPreventActionTime.set(System.currentTimeMillis());
            return true;
        }
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.v("double_tap","occur111");
            mPreventAction.set(true);
            mPreventActionTime.set(System.currentTimeMillis());
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
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
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
            Log.v("webview_top_Fun","are_called");
            WebviewTopSwipe.swipeTop();
        }

        public void onSwipeBottom() {
        }
    }

    public static void tapListener(MyCustomPagerAdapter.touchGesture touchGestures1){
        touchGestures=touchGestures1;

    }
    public static void swipeTopListener(WebviewTopSwipe webviewSwipe){
        WebviewTopSwipe=webviewSwipe;

    }
}
