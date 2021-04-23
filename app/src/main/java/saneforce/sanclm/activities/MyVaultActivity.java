package saneforce.sanclm.activities;

import android.database.Cursor;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.listener.OnDrawListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.LoadBitmap;
import saneforce.sanclm.activities.Model.ReportData;
import saneforce.sanclm.adapter_class.AdapterForReport;
import saneforce.sanclm.adapter_class.Adapter_dashboard_menu;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class MyVaultActivity extends AppCompatActivity implements OnChartValueSelectedListener,
        OnDrawListener {
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "June"
    };
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;
    private CombinedChart mChart;
    BarChart barChart;

    ArrayList xVals = new ArrayList();
    ArrayList temp = new ArrayList();

    ArrayList yVals1 = new ArrayList();
    ArrayList yVals2 = new ArrayList();
    ArrayList yVals3 = new ArrayList();

    ArrayList<ReportData> reportList=new ArrayList<>();
    ArrayList<ReportData> reportListTemp=new ArrayList<>();

    ListView list_view;
    RelativeLayout rl_popup,rl_field;
    Button sub_btn;
    ImageView filter;
    DrawerLayout drawer_layout;
    ArrayList<LoadBitmap> arr=new ArrayList<>();
    ArrayList<String> aa=new ArrayList<>();
    ArrayList<String> aa1=new ArrayList<>();
    ListView listview;
    boolean updateBar=false;
    Api_Interface apiService;
    String db_connPath,SF_Code;
    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods mCommonUtilsMethods;

    RelativeLayout rl_yr,rl_mnth,rl_apply_filter;
    String getmnth,yr,mnth,Sf_type;
    boolean isField=false,isMnth=false,isYr=false;
    TextView txt_header;
    EditText edt_search;
    DataBaseHandler dbh;
    ImageView img_close;

    ArrayList<String> selectedNames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vault);
        barChart=(BarChart)findViewById(R.id.chartConsumptionGraph);
        //barChart=(BarChart)findViewById(R.id.bar_chart);

        mCommonSharedPreference = new CommonSharedPreference(this);
        dbh=new DataBaseHandler(MyVaultActivity.this);
        list_view=(ListView)findViewById(R.id.list_view);
        rl_popup=(RelativeLayout)findViewById(R.id.rl_popup);
        rl_field=(RelativeLayout)findViewById(R.id.rl_field);
        rl_mnth=(RelativeLayout)findViewById(R.id.rl_mnth);
        rl_apply_filter=(RelativeLayout)findViewById(R.id.rl_apply_filter);
        rl_popup.setVisibility(View.GONE);
        img_close=(ImageView)findViewById(R.id.img_close);
        sub_btn=(Button)findViewById(R.id.sub_btn);
        filter=(ImageView)findViewById(R.id.filter);
        drawer_layout=(DrawerLayout)findViewById(R.id.drawer_layout);
        listview=(ListView)findViewById(R.id.listview);
        txt_header=(TextView)findViewById(R.id.txt_header);
        edt_search=(EditText)findViewById(R.id.edt_search);

        rl_yr=(RelativeLayout) findViewById(R.id.rl_yr);

        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        Sf_type=mCommonSharedPreference.getValueFromPreference("sf_type");
        mCommonUtilsMethods = new CommonUtilsMethods(this);

        getmnth=mCommonUtilsMethods.getCurrentInstance();
        Log.v("printing_mtnms",getmnth.substring(0,4)+"yera_print"+getmnth.substring(5,7));
         yr=getmnth.substring(0,4);
         mnth=getmnth.substring(5,7);
        getLvReport();
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);

        barChart.setDrawBarShadow(false);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(true);
        barChart.setScaleEnabled(true);
        barChart.setDrawGridBackground(false);
        barChart.setOnChartValueSelectedListener(this);


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.GONE);
            }
        });
        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.GONE);
                selectedNames.clear();
                for(int i=0;i<arr.size();i++){

                    if(arr.get(i).isCheck()==true){
                        // aa.add(arr.get(i).getBrdName());
                       /* xVals.add(arr.get(i).getBrdName());
                           *//* int pos=temp.indexOf(arr.get(i).getBrdName().trim());
                            Log.v("pribting_pos",i+"");*//*
                        yVals3.add(new BarEntry(j, Float.parseFloat(reportList.get(i).getMissed())));
                        yVals2.add(new BarEntry(j, Float.parseFloat(reportList.get(i).getMeet())));
                        yVals1.add(new BarEntry(j, Float.parseFloat(reportList.get(i).getSeen())));*/
                        selectedNames.add(reportList.get(i).getName());
                        //j++;
                    }
                }

                aa.clear();
                barChart=(BarChart)findViewById(R.id.chartConsumptionGraph);
                aa.add("");
                aa.add("Field Work");
                aa.add("Meeting");
                aa.add("Work");
                aa.add("Transit");
                aa.add("");

                updateBar=true;
                float[] valOne = {60, 50, 40, 30};
                float[] valTwo = {20, 30, 10, 20};
                float[] valThree = {10, 40, 30, 60};


            }
        });

        rl_apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.open();
                Cursor cur=dbh.select_report_list_yrmnth(yr,mnth);
                if(cur.getCount()!=0){
                    refreshLocalReport(cur);
                }
                else
                    getLiveReport();

            }
        });
        rl_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.VISIBLE);
                isField=true;
                isMnth=false;
                isYr=false;
                arr.clear();
                txt_header.setText("Select Field Work");
                edt_search.setVisibility(View.VISIBLE);
                for(int i=0;i<reportListTemp.size();i++){
                    arr.add(new LoadBitmap(reportListTemp.get(i).getName(),false));
                }
                Adapter_dashboard_menu adpt1=new Adapter_dashboard_menu(MyVaultActivity.this,arr,true);
                list_view.setAdapter(adpt1);
                adpt1.notifyDataSetChanged();
                sub_btn.setVisibility(View.VISIBLE);
            }
        });
        rl_mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.VISIBLE);
                isMnth=true;
                isField=false;
                isYr=false;
                arr.clear();
                edt_search.setVisibility(View.INVISIBLE);
                txt_header.setText("Select Month");
                arr.add(new LoadBitmap("January",false));
                arr.add(new LoadBitmap("Feburary",false));
                arr.add(new LoadBitmap("March",false));
                arr.add(new LoadBitmap("April",false));
                arr.add(new LoadBitmap("May",false));
                arr.add(new LoadBitmap("June",false));
                arr.add(new LoadBitmap("July",false));
                arr.add(new LoadBitmap("August",false));
                arr.add(new LoadBitmap("September",false));
                arr.add(new LoadBitmap("October",false));
                arr.add(new LoadBitmap("November",false));
                arr.add(new LoadBitmap("December",false));
               /* for(int i=1;i<13;i++){
                    arr.add(new LoadBitmap(String.valueOf(i),false));
                }*/
                Adapter_dashboard_menu adpt1=new Adapter_dashboard_menu(MyVaultActivity.this,arr,false);
                list_view.setAdapter(adpt1);
                adpt1.notifyDataSetChanged();
                sub_btn.setVisibility(View.INVISIBLE);

            }
        });
        rl_yr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_popup.setVisibility(View.VISIBLE);
                isYr=true;
                isField=false;
                isMnth=false;
                arr.clear();
                txt_header.setText("Select Year");
                edt_search.setVisibility(View.INVISIBLE);
                int val= Integer.parseInt(mCommonUtilsMethods.getCurrentInstance().substring(0,4));
                val=val-2;
                for(int i=0;i<3;i++){
                    arr.add(new LoadBitmap(String.valueOf(val),false));
                    val++;
                }
                Adapter_dashboard_menu adpt1=new Adapter_dashboard_menu(MyVaultActivity.this,arr,false);
                list_view.setAdapter(adpt1);
                adpt1.notifyDataSetChanged();
                sub_btn.setVisibility(View.INVISIBLE);
            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("position_filter",position+"");
                rl_popup.setVisibility(View.GONE);
                if(isMnth){
                    int mn=position+1;
                            mnth=String.valueOf(mn);

                }
                else{
                    yr=arr.get(position).getBrdName();
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer_layout.openDrawer(GravityCompat.START);
               /* if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                }else{
                    drawer_layout.openDrawer(GravityCompat.START);
                }*/
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



       /* arr.add(new LoadBitmap ("Field work",false));
        arr.add(new LoadBitmap ("Meeting",false));
        arr.add(new LoadBitmap ("Work",false));
        arr.add(new LoadBitmap ("Reading",false));
        arr.add(new LoadBitmap ("Testing",false));
        arr.add(new LoadBitmap ("Jogging",false));
        arr.add(new LoadBitmap ("Transit",false));*/

       dbh.open();
       Cursor cur=dbh.select_report_list_yrmnth(yr,mnth);
       if(cur.getCount()==0)
        getLiveReport();
       else{
           refreshLocalReport(cur);
       }
       cur.close();
       dbh.close();
        //getSetValuesForChart();
        //UpdateViews(valOne,valTwo,valThree);

       /* Adapter_dashboard_menu adpt1=new Adapter_dashboard_menu(MyVaultActivity.this,arr);
        list_view.setAdapter(adpt1);
        adpt1.notifyDataSetChanged();*/



       /* mChart = (CombinedChart) findViewById(R.id.chart1);
        mChart.getDescription().setText("This is testing Description");
        mChart.setDrawGridBackground(true);
       //mChart.setDrawBarShadow(true);
       // mChart.setHighlightFullBarEnabled(true);
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonths[(int) value % mMonths.length];
            }
        });

        CombinedData data = new CombinedData();

        data.setData( generateLineData());
        data.setData(generateBarData());

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        mChart.setData(data);
        mChart.invalidate();
*/

       //////////////////////

       /* chart.setDescription(null);
        chart.setPinchZoom(true);
        *//*chart.setScaleEnabled(false);*//*
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(true);


        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();

        AddValuesToBARENTRY();

        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "Projects");

        BARDATA = new BarData(Bardataset);

        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
       // BARDATA.setValueFormatter(new LargeValueFormatter());

        chart.setData(BARDATA);

        chart.getData().setHighlightEnabled(true);
        chart.invalidate();


        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        //xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(BarEntryLabels));
//Y-axis
        //chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis=chart.getAxisRight();
        rightAxis.setValueFormatter(new IndexAxisValueFormatter(BarEntryLabels));
        rightAxis.setDrawGridLines(true);
        rightAxis.setSpaceTop(35f);
        rightAxis.setAxisMinimum(0f);

        chart.animateY(3000);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.v("Entry_clicked_","here");
            }

            @Override
            public void onNothingSelected() {
                Log.v("Entry_clicked_","not here");
            }
        });*/

/*
        chart.setOnChartGestureListener(new OnChartGestureListener() {
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

    public void AddValuesToBARENTRY(){

       /* BARENTRY.add(new BarEntry(2f, 0));
        BARENTRY.add(new BarEntry(4f, 1));
        BARENTRY.add(new BarEntry(6f, 2));
        BARENTRY.add(new BarEntry(8f, 3));
        BARENTRY.add(new BarEntry(7f, 4));
        BARENTRY.add(new BarEntry(3f, 5));*/
       /* BARENTRY.add(new BarEntry(0, (float) 0));
        BARENTRY.add(new BarEntry(1, (float)0));
        */
        BARENTRY.add(new BarEntry(1, (float) 6));
        BARENTRY.add(new BarEntry(2, (float) 8));
        BARENTRY.add(new BarEntry(3, (float) 7));
        BARENTRY.add(new BarEntry(4, (float) 3));
        BARENTRY.add(new BarEntry(5, (float) 3));
        BARENTRY.add(new BarEntry(6, (float) 2));


    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("January");
        BarEntryLabels.add("February");
        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");
        BarEntryLabels.add("June");
        BarEntryLabels.add("July");
        BarEntryLabels.add("Aug");
        BarEntryLabels.add("Sep");

    }

    private ArrayList<Entry> getLineEntriesData(ArrayList<Entry> entries){
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 10));
        entries.add(new Entry(3, 8));
        entries.add(new Entry(4, 40));
        entries.add(new Entry(5, 37));

        return entries;
    }

    private ArrayList<BarEntry> getBarEnteries(ArrayList<BarEntry> entries){
        entries.add(new BarEntry(1, 25));
        entries.add(new BarEntry(2, 30));
        entries.add(new BarEntry(3, 38));
        entries.add(new BarEntry(4, 10));
        entries.add(new BarEntry(5, 15));
        return  entries;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = getLineEntriesData(entries);

        LineDataSet set = new LineDataSet(entries, "Line");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = getBarEnteries(entries);

        BarDataSet set1 = new BarDataSet(entries, "Bar");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset


        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);


        return d;
    }

    public void getSetValuesForChart(){


        float barWidth = 0.3f;
        float barSpace = 0f;
        float groupSpace = 0.1f;
        int groupCount = 6;

        groupCount=xVals.size();

      /*  xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        if(!updateBar){
            xVals.add("Jun");
            groupCount=5;
        }*/


      /*  yVals1.add(new BarEntry(1, (float) 1));
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
        if(!updateBar) {
            yVals1.add(new BarEntry(6, (float) 11));
            yVals2.add(new BarEntry(6, (float) 12));
            yVals3.add(new BarEntry(6, (float) 11));
        }*/

        Log.v("printing_offline_report",yVals1.size()+"toal__"+xVals);

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

       /* barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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
*/
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        final String x = barChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), barChart.getXAxis());
        Log.i("VAL_SELECTEDmy",
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

    public void refreshLocalReport(Cursor cur){
        xVals.clear();
        yVals1.clear();
        yVals2.clear();
        yVals3.clear();
        reportList.clear();
        reportListTemp.clear();
        int j=1;
        while(cur.moveToNext()){
            Log.v("cursor_val_report",cur.getString(1));
            xVals.add(cur.getString(1));
            yVals2.add(new BarEntry(j, Float.parseFloat(cur.getString(2))));
            yVals3.add(new BarEntry(j, Float.parseFloat(cur.getString(3))));
            yVals1.add(new BarEntry(j, Float.parseFloat(cur.getString(4))));
            reportListTemp.add(new ReportData(cur.getString(1),cur.getString(7),cur.getString(5),cur.getString(4),cur.getString(2),cur.getString(3),cur.getString(6)));
            j++;
        }
        cur.close();
        dbh.close();
        filterSelectedList();

        AdapterForReport adpt=new AdapterForReport(MyVaultActivity.this,reportList);
        listview.setAdapter(adpt);
        getSetValuesForChart();

    }
    public void getLiveReport(){
        xVals.clear();
        yVals1.clear();
        yVals2.clear();
        yVals3.clear();
        reportList.clear();
        reportListTemp.clear();


        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("SF","MGR0148");
            jsonObject.put("div","19");
            jsonObject.put("mnth",mnth);
            jsonObject.put("yr",yr);
            Log.v("print_report_dt",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> noti = apiService.getReport(jsonObject.toString());
        noti.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip=null;
                StringBuilder is=new StringBuilder();
                String line=null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    Log.v("print_report_dt",is.toString());
                    String met,miss,seen;

                    JSONArray jj=new JSONArray(is.toString());

                    for(int i=0;i<jj.length();i++){
                        JSONObject js=jj.getJSONObject(i);
                        xVals.add(js.getString("FieldForce Name"));
                        if(js.getString("Doctors_Met").equalsIgnoreCase("-")) {
                            yVals2.add(new BarEntry(i + 1, (float) 0));
                            met="0";
                        }
                            else{
                            yVals2.add(new BarEntry(i+1, Float.parseFloat(js.getString("Doctors_Met"))));
                            met=js.getString("Doctors_Met");}

                        if(js.getString("Listed_Drs_Missed").equalsIgnoreCase("-")){
                            yVals3.add(new BarEntry(i+1, (float) 0));
                            miss="0";
                        }
                            else{
                            yVals3.add(new BarEntry(i+1, Float.parseFloat(js.getString("Listed_Drs_Missed"))));
                            miss=js.getString("Listed_Drs_Missed");}

                        if(js.getString("Doctors_Calls_Seen").equalsIgnoreCase("-")){
                            yVals1.add(new BarEntry(i+1, (float) 0));
                            seen="0";
                        }
                            else{
                            yVals1.add(new BarEntry(i+1, Float.parseFloat(js.getString("Doctors_Calls_Seen"))));
                            seen=js.getString("Doctors_Calls_Seen");
                            }
                      /*  yVals2.add(js.getString("Listed_Drs_Missed"));
                        yVals3.add(js.getString("Doctors_Calls_Seen"));*/
                      dbh.open();
                      dbh.insert_report_master(js.getString("FieldForce Name"),met,miss,seen,yr,mnth,js.getString("Total_Listed_Drs"));
                      dbh.close();
                      //reportList.add(new ReportData(js.getString("FieldForce Name"),mnth,yr,seen,met,miss,js.getString("Total_Listed_Drs")));
                      reportListTemp.add(new ReportData(js.getString("FieldForce Name"),mnth,yr,seen,met,miss,js.getString("Total_Listed_Drs")));
                    }

                    Log.v("print_local_store",xVals.toString());
                    filterSelectedList();
                    getSetValuesForChart();
                    AdapterForReport adpt=new AdapterForReport(MyVaultActivity.this,reportList);
                    listview.setAdapter(adpt);
                   /* JSONArray jj=new JSONArray(is.toString());
                    for(int i=0;i<jj.length();i++){
                        JSONObject js=jj.getJSONObject(i);
                        arr.add(new MissedDate(js.getString("DMon")+" "+js.getString("DYr"),js.getString("DDate"),js.getString("DDay"),js.getString("id")));
                    }
                    AdapterMissedDate adp=new AdapterMissedDate(HomeDashBoard.this,arr);
                    missed_list.setAdapter(adp);
                    adp.notifyDataSetChanged();
*/
                   /* ArrayList<String> msg=new ArrayList<>();

                    JSONArray jsonArray=new JSONArray(is.toString());
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jj=jsonArray.getJSONObject(i);
                        msg.add(jj.getString("msg"));
                    }
                    alertDialoggg(msg);*/


                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    public void filterSelectedList(){
        if(selectedNames.size()!=0){
            xVals.clear();
            yVals1.clear();
            yVals2.clear();
            yVals3.clear();
            int j=1;
            Log.v("selected_items_are",selectedNames.size()+"");
            for(int i=0;i<selectedNames.size();i++){


                    for(int j1=0;j1<reportListTemp.size();j1++){
                        if(reportListTemp.get(j1).getName().equalsIgnoreCase(selectedNames.get(i))){
                            xVals.add(reportListTemp.get(i).getName());

                            yVals3.add(new BarEntry(j, Float.parseFloat(reportListTemp.get(j1).getMissed())));
                            yVals2.add(new BarEntry(j, Float.parseFloat(reportListTemp.get(j1).getMeet())));
                            yVals1.add(new BarEntry(j, Float.parseFloat(reportListTemp.get(j1).getSeen())));
                            // pos.add(i);
                            j++;
                            reportList.add(reportListTemp.get(i));
                        }


                }


            }

            selectedNames.clear();
        }
        else{
            reportList.addAll(reportListTemp);
        }

    }

    public void getLvReport(){
        getmnth.substring(0,4);
        Log.v("printing_report","cursoring"+"");
        dbh.open();
        Cursor cu=dbh.select_getreport("2019");
        Log.v("printing_report",cu.getCount()+"");
    }

}
