package saneforce.sanclm.adapter_class;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import saneforce.sanclm.R;

public class DemoViewPager  extends PagerAdapter {
    LayoutInflater layoutInflater;
    //
    ArrayList<String> array=new ArrayList<>();
    Context context;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    ImageView imageView,imageView1;
    Object objectsd;
    RelativeLayout          rlay;

    public DemoViewPager(ArrayList<String> array, Context context) {
        this.array = array;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View    itemView = layoutInflater.inflate(R.layout.dummy_row, container, false);
         imageView=itemView.findViewById(R.id.imageView);
         imageView1=itemView.findViewById(R.id.imageView1);
        rlay=itemView.findViewById(R.id.rlay);
        container.addView (itemView);
        return itemView;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            Log.v("mScaleFactor",mScaleFactor+"");
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            Log.v("scalor_here",mScaleFactor+"");
            if(objectsd!=null) {
                rlay= (RelativeLayout) objectsd;
                rlay.setScaleX(mScaleFactor);
                rlay.setScaleY(mScaleFactor);
            }
            return true;
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.v("postion_pager", String.valueOf(position)+"object_view"+object);
        container.removeView((RelativeLayout) object);
    }
    @Override
    public void setPrimaryItem (ViewGroup container, int position, Object object)
    {
        View    view= (RelativeLayout) object;
        objectsd=object;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });


    }
}
