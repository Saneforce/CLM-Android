package saneforce.sanclm.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

public class HelpVideoView extends VideoView {

    int firstTap=0,secondTap=0;
    public HelpVideoView(Context context) {
        super(context);
    }

    public HelpVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelpVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
       // super.performClick();
        Log.v("perform_click","are_called");
        return false;
    }


/*
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();

        Log.v("touching_video", "are_called"+action);
        if(firstTap==0){
            firstTap= (int) System.currentTimeMillis();

        }
        else{
            secondTap=(int) System.currentTimeMillis();
        }


        return false;
        }
*/

    }

