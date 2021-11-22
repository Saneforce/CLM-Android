package saneforce.sanclm.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDayReport;
import saneforce.sanclm.activities.Model.ModelMissedReport;
import saneforce.sanclm.activities.Model.ModelVisitControl;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.adapter_class.AdapterForMissedReport;
import saneforce.sanclm.adapter_class.AdapterForReportClass;
import saneforce.sanclm.adapter_class.AdapterForVisitControl;
import saneforce.sanclm.adapter_class.AdapterPopupSpinnerSelection;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class DayReportsActivity extends AppCompatActivity {

    RecyclerView recycle_view;
    AdapterForReportClass adpt;
    AdapterForVisitControl visit_adpt;
    ArrayList<String> aa=new ArrayList<>();
    ArrayList<ModelDayReport> array=new ArrayList<>();
    ArrayList<ModelVisitControl> visit_array=new ArrayList<>();
    ArrayList<ModelMissedReport> miss_array=new ArrayList<>();
    CommonSharedPreference commonSharedPreference;
    Api_Interface apiService;
    String db_connPath;
    BarChart barChart;
    ImageView back_icon,filter;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String value,sf_type;
    RelativeLayout lay_filter,lay_select,lay_month,lay_missed;
    String sf_code,sumdate;
    TextView txt_date,txt_count_dr,txt_count_chm,txt_count_stk,txt_count_ul,txt_hq,txt_count_dr_miss,txt_count_chm_miss,txt_count_stk_miss,txt_count_cip;
    DataBaseHandler dbh;
    ArrayList<SlideDetail> list=new ArrayList<>();
    ListView list_view;
    AdapterPopupSpinnerSelection hq_adpt;
    LinearLayout lay_stk_miss;
    AdapterForMissedReport miss_adpt;
    TextView txt_head_report;
    String[] ar={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    TextView txt_dr,txt_chm,txt_stk,txt_udr,txt_cip;
    LinearLayout lay_ul,lay_dr,lay_chm,lay_stk,lay_cip;
    String language;
    Context context;
    Resources resources;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commonFun();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        commonFun();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_reports);

        SharedPreferences sharedPreferences = DayReportsActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(DayReportsActivity.this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(DayReportsActivity.this, "en");
            resources = context.getResources();
        }


        Bundle extra=getIntent().getExtras();
        value=extra.getString("value");
        recycle_view=(RecyclerView)findViewById(R.id.recycle_view);
        back_icon=(ImageView)findViewById(R.id.back_icon);
        filter=(ImageView)findViewById(R.id.filter);
        lay_filter=(RelativeLayout)findViewById(R.id.lay_filter);
        lay_select=(RelativeLayout)findViewById(R.id.lay_select);
        lay_month=(RelativeLayout)findViewById(R.id.lay_month);

        txt_date=(TextView) findViewById(R.id.txt_wt);
        txt_count_dr=(TextView) findViewById(R.id.txt_count_dr);
        txt_count_chm=(TextView) findViewById(R.id.txt_count_chm);
        txt_count_stk=(TextView) findViewById(R.id.txt_count_stk);
        txt_count_ul=(TextView) findViewById(R.id.txt_count_ul);
        txt_head_report=(TextView)findViewById(R.id.txt_head_report);
        txt_count_dr_miss=(TextView) findViewById(R.id.txt_count_dr_miss);
        txt_count_chm_miss=(TextView) findViewById(R.id.txt_count_chm_miss);
        txt_count_stk_miss=(TextView) findViewById(R.id.txt_count_stk_miss);
        txt_hq=(TextView) findViewById(R.id.txt_hq);
        txt_dr=(TextView) findViewById(R.id.txt_dr);
        txt_chm=(TextView) findViewById(R.id.txt_chm);
        txt_stk=(TextView) findViewById(R.id.txt_stk);
        txt_udr=(TextView) findViewById(R.id.txt_udr);
        list_view=(ListView) findViewById(R.id.list);
        myCalendar = Calendar.getInstance();
        dbh=new DataBaseHandler(this);

        lay_ul=(LinearLayout)findViewById(R.id.lay_ul);
        lay_dr=(LinearLayout)findViewById(R.id.lay_dr);
        lay_chm=(LinearLayout)findViewById(R.id.lay_chm);
        lay_stk=(LinearLayout)findViewById(R.id.lay_stk);


        lay_cip=(LinearLayout)findViewById(R.id.lay_cip);
        txt_cip=(TextView) findViewById(R.id.txt_cip);
        txt_count_cip=(TextView) findViewById(R.id.txt_count_cip);

        commonSharedPreference=new CommonSharedPreference(DayReportsActivity.this);
        db_connPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_type =  commonSharedPreference.getValueFromPreference("sf_type");
        sf_code=commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        aa.add("hello");
        aa.add("hello");
        aa.add("hello");
        aa.add("hello");
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);


//        if(commonSharedPreference.getValueFromPreference("cip_need").equals("0"))
//        {
//            txt_chm.setText(commonSharedPreference.getValueFromPreference("cipcap"));
//        }else {
//
//            txt_chm.setText(commonSharedPreference.getValueFromPreference("chmcap"));
//        }
        txt_dr.setText(commonSharedPreference.getValueFromPreference("drcap"));
        txt_chm.setText(commonSharedPreference.getValueFromPreference("chmcap"));
        txt_stk.setText(commonSharedPreference.getValueFromPreference("stkcap"));
        txt_udr.setText(commonSharedPreference.getValueFromPreference("ucap"));

        txt_cip.setText(commonSharedPreference.getValueFromPreference("cipcap"));

        if(commonSharedPreference.getValueFromPreference("cip_need").equals("0"))
            lay_cip.setVisibility(View.VISIBLE);

        if(commonSharedPreference.getValueFromPreference("chem_need").equals("1"))
            lay_chm.setVisibility(View.GONE);

/*
        if(sf_type.equalsIgnoreCase("2")){
            dbh.open();
            Cursor mCursor=dbh.select_hqlist_manager();
            if(mCursor.getCount()!=0) {
                if (mCursor.moveToFirst()) {
                    do {
                        Log.v("Name_hqlist", mCursor.getString(2)+" djkdj "+mCursor.getString(1));

                       // Updatecluster(mCursor.getString(1));

                    } while (mCursor.moveToNext());

                }

            }
            dbh.close();
        }
*/

         date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                commonFun();
                Log.d("SELDATE", String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth));
                String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
                if(value.equalsIgnoreCase("1"))
                    callApi(date);
                else if(value.equalsIgnoreCase("2"))
                    callApiVisit(String.valueOf(monthOfYear + 1),String.valueOf(year));
                else if(value.equalsIgnoreCase("3")) {
                    int x=monthOfYear ;
                    txt_date.setText(ar[x]+"-"+String.valueOf(year));
                    callApiMonth(sf_code, date);
                }
                else
                    callApiMissed(date);
               // updateLabel(date);
            }

        };

        if(value.equalsIgnoreCase("1")) {
            lay_filter.setVisibility(View.GONE);
            adpt = new AdapterForReportClass(this, array, 1);
            RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
            recycle_view.setLayoutManager(layout);
            recycle_view.setItemAnimator(new DefaultItemAnimator());
            recycle_view.setAdapter(adpt);
            txt_head_report.setText(resources.getString(R.string.dayreport));
            callApi(CommonUtilsMethods.getCurrentInstance());
        }
        else if(value.equalsIgnoreCase("2")){
            lay_filter.setVisibility(View.GONE);
            String date_=CommonUtilsMethods.getCurrentInstance();
            String month=date_.substring(5,7);
            String yr=date_.substring(0,4);
            Log.v("printing_date_in",date_+" mn "+month+" yr "+yr);
            visit_adpt=new AdapterForVisitControl(this,visit_array);
            RecyclerView.LayoutManager layout=new LinearLayoutManager(getApplicationContext());
            recycle_view.setLayoutManager(layout);
            recycle_view.setAdapter(visit_adpt);
            txt_head_report.setText(resources.getString(R.string.visitcontrol));
            callApiVisit(month,yr);

        }
        else if(value.equalsIgnoreCase("3")){
            lay_filter.setVisibility(View.VISIBLE);
            lay_month.setVisibility(View.VISIBLE);

            if(sf_type.equalsIgnoreCase("2")) {
                list.clear();
                list.add(new SlideDetail(commonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME),sf_code));
                txt_hq.setText(commonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME));
                lay_select.setVisibility(View.VISIBLE);
                dbh.open();
                Cursor mCursor=dbh.select_hqlistmgr_manager();
                if(mCursor.getCount()!=0) {
                    if (mCursor.moveToFirst()) {
                        do {
                            Log.v("Name_hqlist", mCursor.getString(2)+" djkdj "+mCursor.getString(1));
                            if(!(sf_code.equals(mCursor.getString(1))))
                                list.add(new SlideDetail(mCursor.getString(2),mCursor.getString(1)));
//                            {
//
//                            }else
//                             {
//                            list.add(new SlideDetail(mCursor.getString(2),mCursor.getString(1)));
//                             }


                        } while (mCursor.moveToNext());

                    }
                    hq_adpt=new AdapterPopupSpinnerSelection(DayReportsActivity.this,list);
                    list_view.setAdapter(hq_adpt);
                    hq_adpt.notifyDataSetChanged();
                }
                dbh.close();
            }
            else
                lay_select.setVisibility(View.GONE);

            adpt = new AdapterForReportClass(this, array, 1);
            RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
            recycle_view.setLayoutManager(layout);
            recycle_view.setItemAnimator(new DefaultItemAnimator());
            recycle_view.setAdapter(adpt);
            txt_head_report.setText(resources.getString(R.string.monthlyreport));
            callApiMonth(commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE),CommonUtilsMethods.getCurrentInstance());

        }
        else{
            lay_filter.setVisibility(View.GONE);
            miss_adpt = new AdapterForMissedReport(this, miss_array);
            RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
            recycle_view.setLayoutManager(layout);
            recycle_view.setItemAnimator(new DefaultItemAnimator());
            recycle_view.setAdapter(miss_adpt);
            txt_head_report.setText(resources.getString(R.string.missedreport));
            callApiMissed(CommonUtilsMethods.getCurrentInstance());
        }

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DayReportsActivity.this, android.R.style.Theme_Holo_Dialog, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                Log.d("month",myCalendar.get(Calendar.MONTH));
                System.out.println("selection time time => " + myCalendar.get(Calendar.MONTH));
            }
        });
        lay_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_view.setVisibility(View.VISIBLE);
            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SlideDetail mm=list.get(i);
                txt_hq.setText(mm.getInputName());
                list_view.setVisibility(View.GONE);
                commonFun();
                callApiMonth(mm.getIqty(),CommonUtilsMethods.getCurrentInstance());
            }
        });
        //barReport();
        commonFun();
    }

    public void callApi(String date){
        try{
            JSONObject json=new JSONObject();
            json.put("rptDt",date);
            json.put("rSF",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            json.put("sfCode",commonSharedPreference.getValueFromPreference("sub_sf_code"));
            json.put("divisionCode",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));

            Log.v("showing_day_",json.toString());//{"rptDt":"2020-09-14","rSF":"MGR0205","sfCode":"MR0633","divisionCode":"3"}

            Call<ResponseBody> report=apiService.getDayRpt(json.toString());
            report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("day_report", is.toString());
                            array.clear();
                            JSONArray jsonA=new JSONArray(is.toString());
                            for(int i=0;i<jsonA.length();i++){
                                JSONObject json=jsonA.getJSONObject(i);
                                array.add(new ModelDayReport(json.getString("Adate"),json.getString("SF_Name"),json.getString("wtype"),
                                        json.getString("TerrWrk"),json.getString("HalfDay_FW_Type"),json.getString("remarks"),json.getString("Udr"),
                                        json.getString("Drs"),json.getString("Stk"),json.getString("Chm"),json.getString("ACode"),json.getString("Cip")));
                            }
                            adpt.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){
            Log.v("DayReports Exception",e.getMessage());}
    }
    public void callApiMissed(String date){
        try{
            JSONObject json=new JSONObject();
            json.put("rptDt",date);
            json.put("rSF",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            json.put("sfCode",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            json.put("divisionCode",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));

            Log.v("showing_day_",json.toString());

            Call<ResponseBody> report=apiService.getMissedRpt(json.toString());
            report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("missed_report", is.toString());
                            miss_array.clear();
                            JSONArray jsonA=new JSONArray(is.toString());
                            for(int i=0;i<jsonA.length();i++){
                                JSONObject json=jsonA.getJSONObject(i);
                                miss_array.add(new ModelMissedReport(json.getString("Name"),json.getString("sf_code"),json.getString("MRptdate"),
                                        json.getString("Mdate"),json.getString("Cluster"),json.getString("Dcnt"),json.getString("Dmet"),
                                        json.getString("Dmis")));
                            }
                            miss_adpt.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}
    }
    public void callApiMonth(String sfcode,String date){
        try{
            JSONObject json=new JSONObject();
            json.put("rptDt",date);
            json.put("rptSF",sfcode);
            json.put("sfCode",commonSharedPreference.getValueFromPreference("sub_sf_code"));
            json.put("divisionCode",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            Log.v("printing_db_mn",json.toString());//{"rptDt":"2020-09-14","rptSF":"MGR0205","sfCode":"MR0633","divisionCode":"3"}


            Call<ResponseBody> report=apiService.getMonthSum(json.toString());
            report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("month_report", is.toString());
                            array.clear();
                            int dr=0,ch=0,st=0,ul=0,cip=0;
                            JSONArray jsonA=new JSONArray(is.toString());
                            for(int i=0;i<jsonA.length();i++){
                                JSONObject json=jsonA.getJSONObject(i);
                                sumdate=json.getString("Sumdate");
                                txt_date.setText(sumdate);
                                array.add(new ModelDayReport("",json.getString("Adate"),json.getString("wtype"),
                                        json.getString("TerrWrk"),json.getString("HalfDay_FW_Type"),json.getString("Remarks"),json.getString("Udr"),
                                        json.getString("Drs"),json.getString("Stk"),json.getString("Chm"),json.getString("ACode"),json.getString("Cip")));
                                dr=dr+countValues(json.getString("Drs"));
                                ch=ch+countValues(json.getString("Chm"));
                                st=st+countValues(json.getString("Stk"));
                                ul=ul+countValues(json.getString("Udr"));
                                cip=cip+countValues(json.getString("Cip"));
                            }
                            txt_count_ul.setText(String.valueOf(ul));
                            txt_count_dr.setText(String.valueOf(dr));
                            txt_count_chm.setText(String.valueOf(ch));
                            txt_count_stk.setText(String.valueOf(st));
                            txt_count_cip.setText(String.valueOf(cip));
                            Log.v("showing_mnth",array.size()+"");
                            adpt.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.v("printed_sum",e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}
    }
    public void callApiVisit(String mn,String yr){
        try{
            JSONObject json=new JSONObject();

            json.put("SF",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            json.put("month",mn);
            json.put("year",yr);
            json.put("div",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));

            Call<ResponseBody> report=apiService.getVisitMonitor(json.toString());
            report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("visit_report", is.toString());
                            visit_array.clear();
                            JSONArray jsonA=new JSONArray(is.toString());
                            for(int i=0;i<jsonA.length();i++){
                                JSONObject json=jsonA.getJSONObject(i);
                                visit_array.add(new ModelVisitControl(json.getString("FieldForce Name"),json.getString("vmonth"),json.getString("HQ"),getValues(json.getString("Total_Listed_Drs")),
                                        json.getString("Listed_Drs_Missed"),getValues(json.getString("Doctors_Met")),getValues(json.getString("Doctors_Calls_Seen")),getValues(json.getString("No_Of_Field_Wrk_Days")),
                                        getValues(json.getString("Call_Average")),json.getString("Coverage_Per")));
                            }
                            visit_adpt.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}
    }

    public String getValues(String val){
        if(val.equalsIgnoreCase("-"))
            return "0";
        else
            return val;
    }

    public int countValues(String val){
        int a=Integer.parseInt(val);
        return a;
    }
    public void commonFun(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }
    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}
