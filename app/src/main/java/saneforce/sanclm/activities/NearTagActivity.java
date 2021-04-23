package saneforce.sanclm.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.fragments.NearMe;
import saneforce.sanclm.fragments.ViewTag;

public class NearTagActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    String db_connPath;
    String SF_Code;
    CommonSharedPreference mCommonSharedPreference;
    public static ArrayList<ViewTagModel> list=new ArrayList<>();
    RelativeLayout tag,nearme,explore;

    @Override
    public void onBackPressed() {
        Intent ii=new Intent(NearTagActivity.this,HomeDashBoard.class);
        startActivity(ii);
    }
    @Override
    protected void onResume() {
        super.onResume();
        FullScreencall();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_tag);
        FullScreencall();
        mCommonSharedPreference = new CommonSharedPreference(this);
        /*tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Tagging"));
        tabLayout.addTab(tabLayout.newTab().setText("Near Me"));
        tabLayout.addTab(tabLayout.newTab().setText("Explore"));

        viewPager = (ViewPager) findViewById(R.id.pager);*/
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);

        tag=(RelativeLayout)findViewById(R.id.tag);
        nearme=(RelativeLayout)findViewById(R.id.nearme);
       // explore=(RelativeLayout)findViewById(R.id.explore);

        getSupportFragmentManager().beginTransaction().replace(R.id.maps, new ViewTag()).commit();

        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new ViewTag()).commit();
                nearme.setBackground(null);
                //explore.setBackground(null);
                tag.setBackgroundColor(Color.RED);
            }
        });
        nearme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new NearMe()).commit();
                tag.setBackground(null);
//                explore.setBackground(null);
                nearme.setBackgroundColor(Color.RED);
            }
        });
/*
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new ExploreMap()).commit();
                tag.setBackground(null);
                nearme.setBackground(null);
                explore.setBackgroundColor(Color.RED);
            }
        });
*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //Creating our pager adapter
        /*Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);


        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position,positionOffset,true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.v("selected_tab",tab.getPosition()+"print");
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }*/
    }
    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
