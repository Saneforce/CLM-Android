package saneforce.sanclm.activities;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.listener.OnDrawListener;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.LoadBitmap;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.adapter_class.AdapterPopupSpinnerSelection;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class DumTry extends Activity implements OnChartValueSelectedListener,
        OnDrawListener {
    ProgressBar pb_jan;
    ImageView imgg;
    BarChart mChart,barChart;
    ListView list_view;
    RelativeLayout rl_popup,rl_field;
    Button cancel_btn,sub_btn;
    ImageView filter;
    DrawerLayout drawer_layout;
    ArrayList<LoadBitmap> arr=new ArrayList<>();
    ArrayList<String> aa=new ArrayList<>();
    ArrayList<String> aa1=new ArrayList<>();
    ListView listview;
    boolean update=false;
    ArrayList xVals = new ArrayList();

    ArrayList yVals1 = new ArrayList();
    ArrayList yVals2 = new ArrayList();
    ArrayList yVals3 = new ArrayList();
    ArrayList<SlideDetail> list_dr=new ArrayList<>();
    RecyclerView recycle;
    ArrayList<LoadBitmap> ar=new ArrayList<>();
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView imageView;
    ViewPager viewpager;
    RelativeLayout  rlay_qua,rlay_spec,rlay_cat;
    DataBaseHandler dbh;
    TextView    spin_txt_qua,spin_txt_spec,spin_txt_cat;
    CheckBox    chk_male,chk_fmale;
    Spinner spinner_day,spinner_mnth,spinnerw_day,spinnerw_mnth;
    ArrayList<String>   list_day=new ArrayList<>();
    ArrayList<String>   list_mnth=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dr_profile_layout);
        rlay_qua=(RelativeLayout)findViewById(R.id.rlay_qua);
        spinner_day = (Spinner) findViewById(R.id.spinner_day);
        spinner_mnth = (Spinner) findViewById(R.id.spinner_mnth);
        spinnerw_day = (Spinner) findViewById(R.id.spinnerw_day);
        spinnerw_mnth = (Spinner) findViewById(R.id.spinnerw_mnth);
        rlay_cat=(RelativeLayout)findViewById(R.id.rlay_cat);
        rlay_spec=(RelativeLayout)findViewById(R.id.rlay_spec);
        spin_txt_qua=(TextView) findViewById(R.id.spin_txt_qua);
        spin_txt_spec=(TextView) findViewById(R.id.spin_txt_spec);
        spin_txt_cat=(TextView) findViewById(R.id.spin_txt_cat);
        chk_male=(CheckBox)findViewById(R.id.chk_male);
        chk_fmale=(CheckBox)findViewById(R.id.chk_fmale);
        dbh = new DataBaseHandler(this);
        for(int i=1;i<32;i++){
            list_day.add(String.valueOf(i));
        }
        for(int i=1;i<13;i++){
            list_mnth.add(String.valueOf(i));
        }
        chk_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    chk_fmale.setChecked(false);
            }
        });
        chk_fmale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    chk_male.setChecked(false);
            }
        });
        rlay_qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(1);
            }
        });
        rlay_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(2);
            }
        });
        rlay_spec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(4);
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.textview_differ_size, list_day);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,R.layout.textview_differ_size, list_mnth);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.textview_bg_spinner);
        dataAdapter1.setDropDownViewResource(R.layout.textview_bg_spinner);

        // attaching data adapter to spinner
        spinner_day.setAdapter(dataAdapter);
        spinner_mnth.setAdapter(dataAdapter1);
        spinnerw_day.setAdapter(dataAdapter);
        spinnerw_mnth.setAdapter(dataAdapter1);



      /*  viewpager=(ViewPager)findViewById(R.id.viewpager);
        ArrayList<String>   aa=new ArrayList<>();
        aa.add("hello");
        aa.add("hello");
        aa.add("hello");
        aa.add("hello");
        aa.add("hello");
        aa.add("hello");
        aa.add("hello");
        DemoViewPager   pagerAdapter = new DemoViewPager(aa,DumTry.this);
        viewpager.setAdapter (pagerAdapter);
        viewpager.setOffscreenPageLimit(1);*/

        /*imageView=findViewById(R.id.imageView);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());*/



        /*recycle=(RecyclerView)findViewById(R.id.recycle);
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        ar.add(new LoadBitmap("apple",false));
        ar.add(new LoadBitmap("banana",false));
        ar.add(new LoadBitmap("grape",false));
        ar.add(new LoadBitmap("cherry",false));
        ar.add(new LoadBitmap("orange",false));
        ar.add(new LoadBitmap("dates",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("jackfruit",false));
        ar.add(new LoadBitmap("apple",false));
        ar.add(new LoadBitmap("banana",false));
        ar.add(new LoadBitmap("grape",false));
        ar.add(new LoadBitmap("cherry",false));
        ar.add(new LoadBitmap("orange",false));
        ar.add(new LoadBitmap("dates",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("jackfruit",false));
        ar.add(new LoadBitmap("apple",false));
        ar.add(new LoadBitmap("banana",false));
        ar.add(new LoadBitmap("grape",false));
        ar.add(new LoadBitmap("cherry",false));
        ar.add(new LoadBitmap("orange",false));
        ar.add(new LoadBitmap("dates",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("jackfruit",false));
        ar.add(new LoadBitmap("apple",false));
        ar.add(new LoadBitmap("banana",false));
        ar.add(new LoadBitmap("grape",false));
        ar.add(new LoadBitmap("cherry",false));
        ar.add(new LoadBitmap("orange",false));
        ar.add(new LoadBitmap("dates",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("jackfruit",false));
        ar.add(new LoadBitmap("apple",false));
        ar.add(new LoadBitmap("banana",false));
        ar.add(new LoadBitmap("grape",false));
        ar.add(new LoadBitmap("cherry",false));
        ar.add(new LoadBitmap("orange",false));
        ar.add(new LoadBitmap("dates",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("jackfruit",false));
        ar.add(new LoadBitmap("apple",false));
        ar.add(new LoadBitmap("banana",false));
        ar.add(new LoadBitmap("grape",false));
        ar.add(new LoadBitmap("cherry",false));
        ar.add(new LoadBitmap("orange",false));
        ar.add(new LoadBitmap("dates",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("custedapple",false));
        ar.add(new LoadBitmap("jackfruit",false));
        AdapterRecyle addpt=new AdapterRecyle(DumTry.this,ar);
        recycle.setAdapter(addpt);
        addpt.notifyDataSetChanged();*/

       // mChart=(BarChart)findViewById(R.id.chartConsumptionGraph);
      /*  barChart=(BarChart)findViewById(R.id.chartConsumptionGraph);
        list_view=(ListView)findViewById(R.id.list_view);
        rl_popup=(RelativeLayout)findViewById(R.id.rl_popup);
        rl_field=(RelativeLayout)findViewById(R.id.rl_field);
        rl_popup.setVisibility(View.GONE);
        *//*cancel_btn=(Button)findViewById(R.id.cancel_btn);*//*
        sub_btn=(Button)findViewById(R.id.sub_btn);
        filter=(ImageView)findViewById(R.id.filter);
        drawer_layout=(DrawerLayout)findViewById(R.id.drawer_layout);
        listview=(ListView)findViewById(R.id.listview);

*//*
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.GONE);
            }
        });
*//*
        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.GONE);
                aa.clear();
                mChart=(BarChart)findViewById(R.id.chartConsumptionGraph);
                aa.add("");
                aa.add("Field Work");
                aa.add("Meeting");
                aa.add("Work");
                aa.add("Transit");
                aa.add("");

                float[] valOne = {60, 50, 40, 30};
                float[] valTwo = {20, 30, 10, 20};
                float[] valThree = {10, 40, 30, 60};
               // UpdateViews(valOne,valTwo,valThree);
                getSetValuesForChart();
               *//* for(int i=0;i<arr.size();i++){
                    if(i==0)
                        aa.add("");
                    if(arr.get(i).isCheck()==true){
                        aa.add(arr.get(i).getBrdName());
                    }
                }
                aa.add("");*//*
              *//*  mChart.notifyDataSetChanged();
                mChart.invalidate();*//*

            }
        });
        rl_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.VISIBLE);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer_layout.openDrawer(GravityCompat.START);
               *//* if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                }else{
                    drawer_layout.openDrawer(GravityCompat.START);
                }*//*
            }
        });

        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");
        aa1.add("kite");

       *//* AdapterForReport adpt=new AdapterForReport(DumTry.this,aa1);
        listview.setAdapter(adpt);*//*


       *//* Spinner spinner =(Spinner)findViewById(R.id.spinner);
        Spinner spinner1 =(Spinner)findViewById(R.id.spinner1);
        Spinner spinner2 =(Spinner)findViewById(R.id.spinner2);
        String[] years = {"1996","1997","1998","1998"};
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.custom_spinner,years);
       *//**//* spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_spinner, years));*//**//*
        //aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
        aa.setDropDownViewResource(R.layout.custom_spinner_row_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner1.setAdapter(aa);
        spinner2.setAdapter(aa);
        spinner.setAdapter(aa);*//*
        //  imgg=(ImageView)findViewById(R.id.imgg);
*//*
        Bundle extra=getIntent().getExtras();
        SerialModel mm= (SerialModel) extra.getSerializable("obj");
        Log.v("serial_mode",mm.getName());
        String pathName = "storage/0/emulated/Products/Lexa_22_15_40_1.jpg";
        Drawable d = Drawable.createFromPath(pathName);
        imgg.setBackground(d);*//*


        arr.add(new LoadBitmap ("Field work",false));
        arr.add(new LoadBitmap ("Meeting",false));
        arr.add(new LoadBitmap ("Work",false));
        arr.add(new LoadBitmap ("Reading",false));
        arr.add(new LoadBitmap ("Testing",false));
        arr.add(new LoadBitmap ("Jogging",false));
        arr.add(new LoadBitmap ("Transit",false));

        aa.add("");
        aa.add("Field work");
        aa.add("Meeting");
        aa.add("Work");
        aa.add("Reading");
       *//* aa.add("Testing");
        aa.add("Jogging");
        aa.add("Transit");*//*
        aa.add("");

        *//*float[] valOne = {60, 50, 40, 30, 50,30,5};
        float[] valTwo = {20, 30, 10, 20, 30,20,2};
        float[] valThree = {10, 40, 30, 60, 40,40,2}; *//*
        float[] valOne = {60, 50, 40, 30};
        float[] valTwo = {20, 30, 10, 20};
        float[] valThree = {10, 40, 30, 60};
        getSetValuesForChart();
        //UpdateViews(valOne,valTwo,valThree);

        Adapter_dashboard_menu adpt1=new Adapter_dashboard_menu(DumTry.this,arr,true);
        list_view.setAdapter(adpt1);
        adpt1.notifyDataSetChanged();*/


       /* mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);*/



//orange:#f94200   #cb4d68   #8a0f08


       /* String[] labels=new String[aa.size()];
        labels=aa.toArray(labels);
        //String[] labels = {"", "Name1", "Name2", "Name3", "Name4", "Name5","Name6", ""};
        IAxisValueFormatter xAxisFormatter = new LabelFormatter(mChart, labels);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.GREEN);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.GREEN);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(9, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        float[] valOne = {60, 50, 40, 30, 50,30,5};
        float[] valTwo = {20, 30, 10, 20, 30,20,2};
        float[] valThree = {10, 40, 30, 60, 40,40,2};
        //float[] valFour = {40, 10, 60, 30, 10};
      //  float[] valFive = {60, 50, 40, 30, 50};

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ArrayList<BarEntry> barThree = new ArrayList<>();
        //ArrayList<BarEntry> barFour = new ArrayList<>();
      //  ArrayList<BarEntry> barFive = new ArrayList<>();
        for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            barThree.add(new BarEntry(i, valThree[i]));
           // barFour.add(new BarEntry(i, valFour[i]));
           // barFive.add(new BarEntry(i, valFive[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "Total DR");
        //set1.setColor(Color.BLUE);  #FF5733  #D27966   #DE5133    #E85434 #EA8B76 #B9513B      #003B46   #07575B  #66A5AD
        //orga:#fa3e09 #c65066  #8a0f08

        //green_yellow: #86d1d6  #f0d89a  #b9d075

        //
        set1.setColor(Color.parseColor("#1fc6b2"));
        BarDataSet set2 = new BarDataSet(barTwo, "Missed");
        set2.setColor(Color.parseColor("#095586"));
        BarDataSet set3 = new BarDataSet(barThree, "Category");
        set3.setColor(Color.parseColor("#75a1bc"));
//#b9d075
        *//*BarDataSet set4 = new BarDataSet(barFour, "barTwo");
        set4.setColor(Color.GREEN);*//*
       *//* BarDataSet set5 = new BarDataSet(barFive, "barTwo");
        set5.setColor(Color.LTGRAY);*//*

       *//*orange: light=#ff5c33 dark=#ff471a pink=#ff66b3 brown=#990000,#b30000*//*

        set1.setHighlightEnabled(false);
        set1.setDrawValues(true);
        set2.setHighlightEnabled(false);
        set2.setDrawValues(true);
        set3.setHighlightEnabled(false);
        set3.setDrawValues(true);


       *//* Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);*//*
       *//* set4.setHighlightEnabled(false);
        set4.setDrawValues(false);*//*
       *//* set5.setHighlightEnabled(false);
        set5.setDrawValues(false);*//*

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        //dataSets.add(set4);
       // dataSets.add(set5);
        BarData data = new BarData(dataSets);
        float groupSpace = 0.1f;
        float barSpace = 0f;
        float barWidth = 0.3f;

        // (barSpace + barWidth) * 5 + groupSpace = 1
        // multiplied by 5 because there are 5 five bars
        // labels will be centered as long as the equation is satisfied
        data.setBarWidth(barWidth);
        data.setValueFormatter(new LargeValueFormatter());
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(labels.length - 1.1f);
        mChart.setData(data);
        *//*mChart.setScaleEnabled(false);*//*
        mChart.setVisibleXRangeMaximum(4f);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.animateY(2000);


        mChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.v("printing_entry",e.toString());
            }

            @Override
            public void onNothingSelected() {
                Log.v("printing_entry","not_selected");
            }
        });
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

                Log.v("bar_chart_were","tapped"+me.getY());
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

*/
    }

    public void gettingTableValue(int x){
        list_dr.clear();
        dbh.open();
        Cursor cur=null;
        if(x==1)
            cur=dbh.select_quality_list();
        else if(x==2)
            cur=dbh.select_category_list();
        else if(x==3)
            cur=dbh.select_class_list();
        else if(x==4)
            cur=dbh.select_speciality_list();
        else
            cur=dbh.select_ClusterList();

        if(cur.getCount()>0){
            while(cur.moveToNext()){
                list_dr.add(new SlideDetail(cur.getString(2),cur.getString(1)));

            }
        }

        popupSpinner(x);
    }

    public void popupSpinner(final int x){
        final Dialog dialog=new Dialog(this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);

        AdapterPopupSpinnerSelection popupAdapter=new AdapterPopupSpinnerSelection(this,list_dr);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("qualification_select",list_dr.get(position).getInputName());
                if(x==1) {
                    spin_txt_qua.setText(list_dr.get(position).getInputName());

                }
                else if(x==2){
                    spin_txt_cat.setText(list_dr.get(position).getInputName());
                   /* txt_select_category.setText(list_dr.get(position).getInputName());
                    txt_cat=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }
                else if(x==3) {
                    spin_txt_spec.setText(list_dr.get(position).getInputName());
                    /*txt_select_class.setText(list_dr.get(position).getInputName());
                    txt_class=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }
                else if(x==4) {
                    spin_txt_spec.setText(list_dr.get(position).getInputName());
                    /*txt_select_spec.setText(list_dr.get(position).getInputName());
                    txt_spec=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }
                else {
                    /*txt_select_terr.setText(list_dr.get(position).getInputName());
                    txt_terr=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }

                dialog.dismiss();
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            Log.v("scalor_here",mScaleFactor+"");
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }

    private class LabelFormatter implements IAxisValueFormatter {

        String[] labels;
        BarLineChartBase<?> chart;

        LabelFormatter(BarLineChartBase<?> chart, String[] labels) {
            this.chart = chart;
            this.labels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Log.v("printing_here_total",value+"");
            return labels[(int) value];
        }
    }


    public void UpdateViews(float[] valOne,float[] valTwo,float[] valThree){
        Log.v("aa_size_update_view",valOne.length+" list_size "+aa.size());
        String[] labels=new String[aa.size()];
        labels=aa.toArray(labels);
        Log.v("label_length_hre",labels.length+"");
        //String[] labels = {"", "Name1", "Name2", "Name3", "Name4", "Name5","Name6", ""};  9
        IAxisValueFormatter xAxisFormatter = new LabelFormatter(mChart, labels);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setAxisMinimum(1f);
       // xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(labels.length+1, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

       /* float[] valOne = {60, 50, 40, 30, 50,30,5};
        float[] valTwo = {20, 30, 10, 20, 30,20,2};
        float[] valThree = {10, 40, 30, 60, 40,40,2};*/
        //float[] valFour = {40, 10, 60, 30, 10};
        //  float[] valFive = {60, 50, 40, 30, 50};

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ArrayList<BarEntry> barThree = new ArrayList<>();
        //ArrayList<BarEntry> barFour = new ArrayList<>();
        //  ArrayList<BarEntry> barFive = new ArrayList<>();
        for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            barThree.add(new BarEntry(i, valThree[i]));
            // barFour.add(new BarEntry(i, valFour[i]));
            // barFive.add(new BarEntry(i, valFive[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "Total DR");
        //set1.setColor(Color.BLUE);  #FF5733  #D27966   #DE5133    #E85434 #EA8B76 #B9513B      #003B46   #07575B  #66A5AD
        //orga:#fa3e09 #c65066  #8a0f08

        //green_yellow: #86d1d6  #f0d89a  #b9d075

        //
        set1.setColor(Color.parseColor("#1fc6b2"));
        BarDataSet set2 = new BarDataSet(barTwo, "Missed");
        set2.setColor(Color.parseColor("#095586"));
        BarDataSet set3 = new BarDataSet(barThree, "Category");
        set3.setColor(Color.parseColor("#75a1bc"));
//#b9d075
        /*BarDataSet set4 = new BarDataSet(barFour, "barTwo");
        set4.setColor(Color.GREEN);*/
       /* BarDataSet set5 = new BarDataSet(barFive, "barTwo");
        set5.setColor(Color.LTGRAY);*/

        /*orange: light=#ff5c33 dark=#ff471a pink=#ff66b3 brown=#990000,#b30000*/

        set1.setHighlightEnabled(false);
        set1.setDrawValues(true);
        set2.setHighlightEnabled(false);
        set2.setDrawValues(true);
        set3.setHighlightEnabled(false);
        set3.setDrawValues(true);


       /* Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);*/
       /* set4.setHighlightEnabled(false);
        set4.setDrawValues(false);*/
       /* set5.setHighlightEnabled(false);
        set5.setDrawValues(false);*/

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        //dataSets.add(set4);
        // dataSets.add(set5);
        BarData data = new BarData(dataSets);
        float groupSpace = 0.1f;
        float barSpace = 0f;
        float barWidth = 0.3f;

        // (barSpace + barWidth) * 5 + groupSpace = 1
        // multiplied by 5 because there are 5 five bars
        // labels will be centered as long as the equation is satisfied
        data.setBarWidth(barWidth);
        data.setValueFormatter(new LargeValueFormatter());
        // so that the entire chart is shown when scrolled from right to left
        //xAxis.setAxisMaximum(labels.length - 1.1f);
        xAxis.setAxisMaximum(labels.length - 1.1f);
        mChart.setData(data);
        //mChart.setHighlightFullBarEnabled(null);
        mChart.highlightValue(null);
        //mChart.setHighlightPerTapEnabled(false);
        /*mChart.setScaleEnabled(false);*/
        mChart.setVisibleXRangeMaximum(3f);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.animateY(2000);


        mChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.v("printing_entry",e.toString());
            }

            @Override
            public void onNothingSelected() {
                Log.v("printing_entry","not_selected");
                mChart.highlightValue(null);
            }
        });
/*
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

                Log.v("bar_chart_were","tapped"+me.getY());
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
*/


    }

    public void getSetValuesForChart(){
        xVals.clear();
        yVals1.clear();
        yVals2.clear();
        yVals3.clear();

        float barWidth = 0.3f;
        float barSpace = 0f;
        float groupSpace = 0.1f;
        int groupCount = 6;

        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");

        yVals1.add(new BarEntry(1, (float) 1));
        yVals2.add(new BarEntry(1, (float) 2));
        yVals3.add(new BarEntry(1, (float) 3));
        yVals1.add(new BarEntry(2, (float) 3));
        yVals2.add(new BarEntry(2, (float) 4));
        yVals3.add(new BarEntry(2, (float) 5));
        yVals1.add(new BarEntry(3, (float) 5));
        yVals2.add(new BarEntry(3, (float) 6));
        yVals3.add(new BarEntry(3, (float) 7));
        yVals1.add(new BarEntry(4, (float) 7));
        yVals2.add(new BarEntry(4, (float) 8));
        yVals3.add(new BarEntry(4, (float) 9));
        yVals1.add(new BarEntry(5, (float) 9));
        yVals2.add(new BarEntry(5, (float) 10));
        yVals3.add(new BarEntry(5, (float) 11));
        yVals1.add(new BarEntry(6, (float) 11));
        yVals2.add(new BarEntry(6, (float) 12));
        yVals3.add(new BarEntry(6, (float) 11));

        BarDataSet set = new BarDataSet(yVals1, "entied");
        BarDataSet set1 = new BarDataSet(yVals2, "entied23");
        BarDataSet set2 = new BarDataSet(yVals3, "entied2333");
        /*set.setColors(new int[]{ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light), ColorTemplate.getHoloBlue()});
        set1.setColor(Color.RED);
        set2.setColor(Color.GREEN); */
        set.setColors(Color.parseColor("#1fc6b2"));
        set1.setColor(Color.parseColor("#095586"));
        set2.setColor(Color.parseColor("#75a1bc"));

        final BarData data = new BarData(set,set1,set2);
        //data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace);



        XAxis xval = barChart.getXAxis();
        xval.setDrawLabels(true);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        barChart.setVisibleXRangeMaximum(4f);
        barChart.invalidate(); // refresh

        barChart.animateY(1000);
        /*chartContainer.setVisibility(View.VISIBLE);
        chartContainer.removeAllViews();*/

       /* if (barChart.getParent() == null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            barChart.setLayoutParams(layoutParams);
            chartContainer.addView(barChart);
        }*/

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                final String x = barChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), barChart.getXAxis());
                Log.e("chart ", e.toString()+"Data_index "+h.getDataSetIndex()+" after "+x);
            }

            @Override
            public void onNothingSelected() {
                Log.e("chart", "Null");
            }
        });

    }

    public void UpdateBarChart(){

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        final String x = barChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), barChart.getXAxis());
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " +x /*h.getDataSetIndex()*/+" after"+x);
    }

    @Override
    public void onNothingSelected() {
    }

    /** callback for each new entry drawn with the finger */
    @Override
    public void onEntryAdded(Entry entry) {
        Log.i(Chart.LOG_TAG, entry.toString());
    }

    /** callback when a DataSet has been drawn (when lifting the finger) */
    @Override
    public void onDrawFinished(DataSet<?> dataSet) {
        Log.i(Chart.LOG_TAG, "DataSet drawn. " + dataSet.toSimpleString());

        // prepare the legend again
        mChart.getLegendRenderer().computeLegend(mChart.getData());
    }

    @Override
    public void onEntryMoved(Entry entry) {
        Log.i(Chart.LOG_TAG, "Point_moved " + entry.toString());
    }

/*
    public class AdapterRecyle extends RecyclerView.Adapter<AdapterRecyle.MyViewHolder>{
        Context context;
        ArrayList<LoadBitmap> arr=new ArrayList<>();

        public AdapterRecyle(Context context,ArrayList<LoadBitmap> arr){
            this.context=context;
            this.arr=arr;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflates=LayoutInflater.from(context).inflate(R.layout.dummy_row,parent,false);
            return new MyViewHolder(inflates);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final LoadBitmap bb=arr.get(position);
            holder.setIsRecyclable(false);
            holder.txt.setText(bb.getBrdName());
            holder.check.setChecked(bb.isCheck());

            for(int i=0;i<arr.size();i++){
                Log.v("position_select",i+" checked "+arr.get(i).isCheck());
            }
            holder.edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    bb.setCheck(true);
                    holder.check.setChecked(true);

                }
            });

            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        bb.setCheck(true);
                    else
                        bb.setCheck(false);


                    for(int i=0;i<arr.size();i++){
                        Log.v("position_select11",i+" checked "+arr.get(i).isCheck());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            CheckBox check;
            EditText edt;
            TextView txt;

            public MyViewHolder(View itemView) {
                super(itemView);
                check=(CheckBox)itemView.findViewById(R.id.check);
                edt=(EditText) itemView.findViewById(R.id.edt);
                txt=(TextView) itemView.findViewById(R.id.txt);
            }
        }
    }
*/
}
