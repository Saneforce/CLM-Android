package saneforce.sanclm.Sales_Report.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Adapter.PSaleDetailRptPagerAdaptor;

public class PSalesDetailsReport extends AppCompatActivity {
    RadioButton Btn_HQwise, Btn_brandwise, Btn_fieldforce;
    PSaleDetailRptPagerAdaptor pagerAdaptor;
    TextView toolbar_sub_title;
    RadioGroup Toggle_Hq_Brand;
    ViewPager ViewPager;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psales_report);

        back_btn = findViewById(R.id.back_btn);
        //toolbar_sub_title = findViewById(R.id.toolbar_sub_title);
        Toggle_Hq_Brand = findViewById(R.id.Toggle_Hq_Brand);
        Btn_HQwise = findViewById(R.id.Btn_HQwise);
        Btn_brandwise = findViewById(R.id.Btn_brandwise);
        Btn_fieldforce = findViewById(R.id.Btn_fieldforcewise);
        ViewPager = findViewById(R.id.ViewPager);

        pagerAdaptor = new PSaleDetailRptPagerAdaptor(getApplicationContext(), getSupportFragmentManager(), 3);
        ViewPager.setAdapter(pagerAdaptor);
        pagerAdaptor.notifyDataSetChanged();
//        ViewPager.setOffscreenPageLimit(pagerAdaptor.getCount() > 2 ? pagerAdaptor.getCount() - 1 : 2);

        highLightCurrentTab(0);
        ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PSalesDetailsReport.this, Sales_report_new.class);
//                startActivity(intent);
                Dcrdatas.SelectedSF = "";
                finish();
            }
        });

        Toggle_Hq_Brand.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Btn_HQwise:
                        highLightCurrentTab(0);
                        ViewPager.setCurrentItem(0);
                        break;
                    case R.id.Btn_brandwise:
                        highLightCurrentTab(1);
                        ViewPager.setCurrentItem(1);
                        break;
                    case R.id.Btn_fieldforcewise:
                        highLightCurrentTab(2);
                        ViewPager.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    private void highLightCurrentTab(int position) {
        if (position == 0) {
            Btn_HQwise.setBackgroundResource(R.drawable.button_rpt_enable);
            Btn_brandwise.setBackgroundResource(R.drawable.button_rpt_disable);
            Btn_fieldforce.setBackgroundResource(R.drawable.button_rpt_disable);
            Btn_HQwise.setTextColor(Color.WHITE);
            Btn_brandwise.setTextColor(Color.BLACK);
            Btn_fieldforce.setTextColor(Color.BLACK);
            Dcrdatas.page_position=1;
        } else if (position == 1){
            Btn_HQwise.setBackgroundResource(R.drawable.button_rpt_disable);
            Btn_brandwise.setBackgroundResource(R.drawable.button_rpt_enable);
            Btn_fieldforce.setBackgroundResource(R.drawable.button_rpt_disable);
            Btn_HQwise.setTextColor(Color.BLACK);
            Btn_brandwise.setTextColor(Color.WHITE);
            Btn_fieldforce.setTextColor(Color.BLACK);
            Dcrdatas.page_position=2;
        } else if (position == 2){
            Btn_HQwise.setBackgroundResource(R.drawable.button_rpt_disable);
            Btn_brandwise.setBackgroundResource(R.drawable.button_rpt_disable);
            Btn_fieldforce.setBackgroundResource(R.drawable.button_rpt_enable);
            Btn_HQwise.setTextColor(Color.BLACK);
            Btn_brandwise.setTextColor(Color.BLACK);
            Btn_fieldforce.setTextColor(Color.WHITE);
            Dcrdatas.page_position=3;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
