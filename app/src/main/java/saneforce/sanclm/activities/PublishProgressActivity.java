package saneforce.sanclm.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import saneforce.sanclm.R;

public class PublishProgressActivity extends AppCompatActivity {
    ArrayList<String> array=new ArrayList<>();
    LinearLayout lay_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_progress);
        lay_main=(LinearLayout)findViewById(R.id.lay_main);
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.add("hello");
        createDynamicView();
    }

    public void createDynamicView(){
        for(int i=0;i<array.size();i++) {

                final RelativeLayout ll = new RelativeLayout(this);
                ll.setId(i+1);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                layoutParams.setMargins(10,15,0,0);
                TextView txt = new TextView(this);
                txt.setText("FileName");
                txt.setId(1000 + (i+1));
                txt.setTextColor(Color.parseColor("#000000"));
                txt.setLayoutParams(layoutParams);
                ll.addView(txt);

                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                TextView txt1 = new TextView(this);
                layoutParams1.setMargins(0,15,10,0);
                txt1.setText("0 B/0 B");
                txt1.setId(3000 + (i+1));
                txt1.setTextColor(Color.parseColor("#000000"));
                txt1.setLayoutParams(layoutParams1);
                ll.addView(txt1);
                ProgressBar dynamicPbar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
                dynamicPbar.getProgressDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.SRC_OUT);
                dynamicPbar.setProgress(0);
                dynamicPbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                dynamicPbar.setProgress(30);
                dynamicPbar.setId(2000+ (i+1));
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.addRule(RelativeLayout.BELOW, txt.getId());
                layoutParams2.setMargins(10,10,10,0);
                dynamicPbar.setLayoutParams(layoutParams2);
                ll.addView(dynamicPbar);
                lay_main.addView(ll);


        }

    }
}