package saneforce.sanclm.activities;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.adapter_class.AdapterPopupSpinnerSelection;
import saneforce.sanclm.adapter_class.CalendarAdapter;
import saneforce.sanclm.adapter_class.PopupAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class CalendarActivity extends AppCompatActivity {

    GridView grid_cal;
    ArrayList<String> array=new ArrayList<String>();
    ArrayList<String> dayss=new ArrayList<String>();
    ArrayList<String> worktypeNm = new ArrayList<>();
    ArrayList<String> worktypeCode = new ArrayList<>();
    ArrayList<String> clusterCode = new ArrayList<>();
    ArrayList<Boolean> bookmark=new ArrayList<>();
    ArrayList<PopFeed> hqname = new ArrayList<PopFeed>();
    ArrayList<PopFeed> clusterNm = new ArrayList<PopFeed>();
    ArrayList<PopFeed> hqNameCode=new ArrayList<>();
    ArrayList<PopFeed> cluserCode=new ArrayList<>();
    ArrayList<PopFeed> wrkSave=new ArrayList<>();
    ArrayList<PopFeed> wrkSave2=new ArrayList<>();
    ArrayList<String> hq_code=new ArrayList<>();
    String day=null;
    int endDate=0;
    String[] days={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    boolean countPrint=false;
    int startCount=-1,dayCount=0;
    TextView nxt_mnth,cur_mmth,dis_mnth;
    CalendarAdapter calAdapt;
    String displayMnth=null;
    ImageView iv_dwnldmaster_back;
    DataBaseHandler dbh;
    int findWrkType=0;
    ListView list_hq,list_cluster;
    TextView txt_wrktype,txt_btm_wrktype;
    CommonSharedPreference mCommonSharedPreference;
    String SF_Code,SF_Type;
    String db_connPath,db_slidedwnloadPath;
    DownloadMasters dwnloadMasterData;
    boolean hq_val=false;
    String posTerry="";
    PopupAdapter popupAdapter = null;
    JSONArray jsonArray=new JSONArray();
    JSONObject jsonObject=new JSONObject();
    JSONObject jsonObject1=new JSONObject();
    String currentMn,nextMn;
    boolean vvv=false;
    Button submit;
    Api_Interface apiService;
    List<String> clN;
    String fullDate="";
    String sfname,div;
    String jsondata;

    @Override
    public void onBackPressed() {
        backActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        grid_cal=(GridView)findViewById(R.id.grid_cal);
        cur_mmth=(TextView)findViewById(R.id.cur_mmth);
        nxt_mnth=(TextView)findViewById(R.id.nxt_mnth);
        dis_mnth=(TextView)findViewById(R.id.dis_mnth);
        submit=(Button)findViewById(R.id.submit);

        iv_dwnldmaster_back=(ImageView)findViewById(R.id.iv_dwnldmaster_back);
        dbh = new DataBaseHandler(this);
        mCommonSharedPreference = new CommonSharedPreference(this);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        SF_Type =  mCommonSharedPreference.getValueFromPreference("sf_type");
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        jsondata = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_LOGIN_RESPONSE);
                 try {
                     JSONObject  jsonn = new JSONObject(jsondata);
                     sfname=jsonn.getString("SF_Name");
                     div=jsonn.getString("Division_Code");
                     Log.v("name_json",sfname+"code"+div);
                 } catch (JSONException e) {
            e.printStackTrace();
        }
       /* getDateRange(1);
        dayCal();
*/
//CaldrData

        if(!mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase("null")){
            if(mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase(dis_mnth.getText().toString())){
                submit.setVisibility(View.INVISIBLE);
            }
        }
        else{
            submit.setVisibility(View.VISIBLE);
        }
        getInsertValue();
        grid_cal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.v("dayITemClick",CalendarAdapter.days.get(i));
                if(i>=startCount){
                    popUpAlert(i,CalendarAdapter.days.get(i));
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cur2=dbh.select_tp_list(currentMn);
                if(cur2.getCount()>0){
                    while(cur2.moveToNext()) {
                        Log.v("cur_json_val", cur2.getString(1));
                        try {
                            jsonObject1=new JSONObject(cur2.getString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                try{
                    JSONArray jsA=jsonObject1.getJSONArray("TPDatas");
                    JSONArray FillArray=new JSONArray();
                    for(int i=0;i<jsA.length();i++){
                        JSONObject js=jsA.getJSONObject(i);
                        if(js.getString("access").equalsIgnoreCase("0")){

                        }
                        else{
                            try {
                                JSONObject jj = js.getJSONObject("DayPlan");
                                Log.v("jsonObjec_gg", String.valueOf(jj.length())+" json_div"+" mmm "+jsonObject1.toString());
                                if (jj.length() != 0) {

                                    if(i==jsA.length()-1){
                                        Call<ResponseBody> sub=apiService.submitTP(jsonObject1.toString());
                                        sub.enqueue(new Callback<ResponseBody>() {
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
                                                    Log.v("printing_tp_value",is.toString());
                                                    JSONObject jj=new JSONObject(is.toString());
                                                    if(jj.getString("success").equalsIgnoreCase("true")){
                                                        Toast.makeText(CalendarActivity.this,"Submitted successfully!!",Toast.LENGTH_SHORT).show();
                                                        mCommonSharedPreference.setValueToPreference("mnth",dis_mnth.getText().toString());
                                                        mCommonSharedPreference.setValueToPreference("flagtp","true");
                                                        CommonUtils.TAG_MNTH=dis_mnth.getText().toString();
                                                        CommonUtils.TAG_FLAG_TP="true";
                                                        submit.setVisibility(View.INVISIBLE);
                                                    }
                                                    else{
                                                        Toast.makeText(CalendarActivity.this,jj.getString("success"),Toast.LENGTH_SHORT).show();
                                                    }
                                                }catch (Exception e){

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                    }
                                }
                                else{
                                    Toast.makeText(CalendarActivity.this,"Few Dates are not completed",Toast.LENGTH_SHORT).show();

                                }
                            }catch (Exception e){
                                Toast.makeText(CalendarActivity.this,"Few Dates are not completed",Toast.LENGTH_SHORT).show();
                                i=jsA.length();
                            }



                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

/*
                if(CalendarAdapter.bookMark.contains(false)){

                    Toast.makeText(CalendarActivity.this,"Few Dates are not completed",Toast.LENGTH_SHORT).show();
                }
*/
                /*else{
                    Cursor cur2=dbh.select_tp_list(currentMn);
                    if(cur2.getCount()>0){
                        while(cur2.moveToNext()) {
                            Log.v("cur_json_val", cur2.getString(1));
                            try {
                                jsonObject1=new JSONObject(cur2.getString(2));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    Call<ResponseBody> sub=apiService.submitTP(jsonObject1.toString());
                    sub.enqueue(new Callback<ResponseBody>() {
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
                                Log.v("printing_tp_value",is.toString());
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }*/
            }
        });
        nxt_mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                jsonArray=new JSONArray();
                array.clear();
                jsonObject1=new JSONObject();
                getDateRange(1);
                if(!mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase("null")){
                    if(mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase(dis_mnth.getText().toString())){
                        submit.setVisibility(View.INVISIBLE);
                    }
                    else
                        submit.setVisibility(View.VISIBLE);
                }


                Cursor cur2=dbh.select_tp_list(currentMn);
                if(cur2.getCount()>0){
                    while(cur2.moveToNext()) {
                        Log.v("cur_json_val", cur2.getString(1));
                        try {
                            jsonObject1=new JSONObject(cur2.getString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        calAdapt = new CalendarAdapter(CalendarActivity.this, cur2.getString(2));
                        grid_cal.setAdapter(calAdapt);
                    }
                }
                cur_mmth.setVisibility(View.VISIBLE);
                nxt_mnth.setVisibility(View.INVISIBLE);
            }
        });
        cur_mmth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                array.clear();
                jsonArray=new JSONArray();
                jsonObject1=new JSONObject();
                getDateRange(0);
                if(!mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase("null")){
                    if(mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase(dis_mnth.getText().toString())){
                        submit.setVisibility(View.INVISIBLE);
                    }
                    else
                        submit.setVisibility(View.VISIBLE);
                }

                /* dayCal();*/
                Cursor cur2=dbh.select_tp_list(currentMn);
                if(cur2.getCount()>0){
                    while(cur2.moveToNext()) {
                        Log.v("cur_json_val", cur2.getString(1));
                        try {
                            jsonObject1=new JSONObject(cur2.getString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        calAdapt = new CalendarAdapter(CalendarActivity.this, cur2.getString(2));
                        grid_cal.setAdapter(calAdapt);
                    }
                }
                cur_mmth.setVisibility(View.INVISIBLE);
                nxt_mnth.setVisibility(View.VISIBLE);
            }
        });

        iv_dwnldmaster_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backActivity();
            }
        });
        commonFun();
    }

    public void dayCal(){
        try {
            Log.v("print_enddate",endDate+"");
            for (int i = 1; i <= endDate; i++) {
                if (!countPrint) {
                    for (int j = 0; j < days.length; j++) {
                        if (countPrint)
                            break;
                        if (days[j].equalsIgnoreCase(day)) {
                            array.add(String.valueOf(i));
                            countPrint = true;
                            startCount = j;
                            dayss.add(days[j]);
                            bookmark.add(false);
                            dayCount = j;
                            jsonObject=new JSONObject();
                            jsonObject.put("access", "1");
                            jsonObject.put("dayno", String.valueOf(i));
                            jsonObject.put("TPDt", fullDate+String.valueOf(i)+" 00:00:00");
                            //TPDatas

                            jsonArray.put(jsonObject);
                        } else {
                            array.add("");
                            dayss.add(days[j]);
                            bookmark.add(false);
                            dayCount = j;
                            jsonObject=new JSONObject();
                            jsonObject.put("access", "0");
                            jsonObject.put("dayno", "");
                            jsonArray.put(jsonObject);
                        }
                    }
                } else {
                    array.add(String.valueOf(i));
                    jsonObject=new JSONObject();
                    jsonObject.put("access", "1");
                    jsonObject.put("dayno", String.valueOf(i));
                    jsonObject.put("TPDt", fullDate+String.valueOf(i)+" 00:00:00");

                    jsonArray.put(jsonObject);
                    ++dayCount;
                    if (dayCount < days.length) {
                        dayss.add(days[dayCount]);
                        bookmark.add(false);

                    } else {
                        dayCount = 0;
                        dayss.add(days[dayCount]);
                        bookmark.add(false);
                    }
                }
            }
            Log.v("printing_the_json_array",sfname+"div"+div);
            jsonObject1.put("TPDatas",jsonArray);
            jsonObject1.put("SF",SF_Code);
            jsonObject1.put("SFName",sfname);
            jsonObject1.put("DivCode",div);
            jsonObject1.put("TPMonth",fullDate.substring(5,7));
            jsonObject1.put("TPYear",fullDate.substring(0,4));
            /*jsonObject1.put("SF",SF_Code);
            jsonObject1.put("SFName",sfname);
            jsonObject1.put("DivCode",div);
            jsonObject1.put("TPMonth",fullDate.substring(5,7));
            jsonObject1.put("TPYear",fullDate.substring(0,4));
            */
        }catch (Exception e){}

        Log.v("calendarActivity", String.valueOf(jsonObject1));
        dbh.open();
        dbh.insertTP(String.valueOf(jsonObject1),currentMn);
        countPrint=false;

        /*if(vvv==true) {
            Log.v("json_objectccc",String.valueOf(jsonObject1));

        }*/
        vvv=true;
    }

    public void popUpAlert(final int i, String day) {

        final Dialog dialog = new Dialog(CalendarActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_tour_plan);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView txt_cal_mnth=(TextView)dialog.findViewById(R.id.txt_cal_mnth);
        TextView txt_day=(TextView)dialog.findViewById(R.id.txt_day);
        final TextView txt_date=(TextView)dialog.findViewById(R.id.txt_date);
        txt_wrktype=(TextView)dialog.findViewById(R.id.txt_wrktype);
        txt_btm_wrktype=(TextView)dialog.findViewById(R.id.txt_btm_wrktype);
        LinearLayout ll_edt_txt=(LinearLayout) dialog.findViewById(R.id.ll_edt_txt);
        LinearLayout wrk_typ_one=(LinearLayout) dialog.findViewById(R.id.wrk_typ_one);
        LinearLayout ll_hq=(LinearLayout) dialog.findViewById(R.id.ll_hq);
        LinearLayout ll_cluster=(LinearLayout) dialog.findViewById(R.id.ll_cluster);
        LinearLayout ll_btm_wrk=(LinearLayout) dialog.findViewById(R.id.ll_btm_wrk);
        LinearLayout ll_lay_list_hq=(LinearLayout) dialog.findViewById(R.id.ll_lay_list_hq);
        RelativeLayout rl_show_wrk_list=(RelativeLayout) dialog.findViewById(R.id.rl_show_wrk_list);
        RelativeLayout rr_show_hq_list=(RelativeLayout) dialog.findViewById(R.id.rr_show_hq_list);
        RelativeLayout rl_cluster=(RelativeLayout) dialog.findViewById(R.id.rl_cluster);
        RelativeLayout rl_list=(RelativeLayout) dialog.findViewById(R.id.rl_list);
        RelativeLayout btm_rr_work_type=(RelativeLayout) dialog.findViewById(R.id.btm_rr_work_type);
        final FrameLayout fl_list=(FrameLayout) dialog.findViewById(R.id.fl_list);
        final EditText edt_txt=(EditText) dialog.findViewById(R.id.edt_txt);
        final ListView rv_worktypelist=(ListView)dialog.findViewById(R.id.rv_worktypelist);
        Button save_btn=(Button)dialog.findViewById(R.id.save_btn);
        list_hq=(ListView)dialog.findViewById(R.id.list_hq);
        list_cluster=(ListView)dialog.findViewById(R.id.list_cluster);
        List<String> hqN = null;
        clN = null;
        if(TextUtils.isEmpty(CalendarAdapter.arr_json.get(i).getCode())){

        }
        else{
            try {
                JSONObject jsonObj=new JSONObject(CalendarAdapter.arr_json.get(i).getCode());
                Log.v("jsonObjDate", String.valueOf(jsonObj));
                hqN = Arrays.asList(jsonObj.getString("HQNames").split(","));
                List<String> hqC= Arrays.asList(jsonObj.getString("HQCodes").split(","));
                clN= Arrays.asList(jsonObj.getString("ClusterName").split(","));
                List<String> clC= Arrays.asList(jsonObj.getString("ClusterCode").split(","));
                hqNameCode.clear();
                cluserCode.clear();
                wrkSave.clear();
                wrkSave2.clear();
                for(int k=0;k<hqN.size();k++){
                    hqNameCode.add(new PopFeed(hqN.get(k),hqC.get(k)));
                }
                for(int k=0;k<clN.size();k++){
                    cluserCode.add(new PopFeed(clN.get(k),clC.get(k)));
                }

                if(cluserCode.size()!=0){
                    Log.v("printing_clustt_size",cluserCode.get(0).getTxt());
                }
                wrkSave.add(new PopFeed(jsonObj.getString("WTName"),jsonObj.getString("WTCode")));
                wrkSave2.add(new PopFeed(jsonObj.getString("WTName2"),jsonObj.getString("WTCode2")));
                list_cluster.setAdapter(new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview_items, clN));
                list_hq.setAdapter(new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview_items, hqN));
                txt_wrktype.setText(jsonObj.getString("WTName"));
                txt_btm_wrktype.setText(jsonObj.getString("WTName2"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSoftKeyboard(edt_txt);
            }
        },100);
*/
        dbh.open();
        Cursor mCursor = dbh.select_TPworktypeList(SF_Code);
        // mWorktypeListID.clear();
        worktypeNm.clear();
        worktypeCode.clear();
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                //  mWorktypeListID.put(mCursor.getString(1), mCursor.getString(2));
                worktypeNm.add(mCursor.getString(2));
                worktypeCode.add(mCursor.getString(1));
                //worktypeNm.add(new PopFeed(mCursor.getString(2),false));
            }
        }
        dbh.open();

        if(SF_Type.equalsIgnoreCase("2"))
            mCursor=dbh.select_hqlist_manager();
        else
            mCursor=dbh.select_hqlist(SF_Code);
        hqname.clear();
        if(mCursor.getCount()!=0) {
            if (mCursor.moveToFirst()) {
                do {
                    Log.v("Name_hqlist", mCursor.getString(2));

                    if(hqN!=null && hqN.contains(mCursor.getString(2))){
                        hqname.add(new PopFeed(mCursor.getString(2),true));
                    }
                    else
                        hqname.add(new PopFeed(mCursor.getString(2),false));
                    hq_code.add(mCursor.getString(1));
                } while (mCursor.moveToNext());

            }
        }

        mCursor = dbh.select_ClusterList(SF_Code);
        // mClusterListID.clear();
        clusterNm.clear();
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                //mClusterListID.put(mCursor.getString(1), mCursor.getString(2));
                if(clN!=null && clN.contains(mCursor.getString(2))){
                    clusterNm.add(new PopFeed(mCursor.getString(2),true));
                }
                else{
                    clusterNm.add(new PopFeed(mCursor.getString(2),false));}
                clusterCode.add(mCursor.getString(1));
            }
        }

        ImageView iv_close_popup=(ImageView)dialog.findViewById(R.id.iv_close_popup);
        txt_cal_mnth.setText(dis_mnth.getText().toString());
        txt_day.setText(day);
        txt_date.setText(day);

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                commonFun();
            }
        });
        ll_edt_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_txt);
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_wrktype.getText().toString().equalsIgnoreCase("field work") || txt_btm_wrktype.getText().toString().equalsIgnoreCase("field work")){
                    if(cluserCode.size()==0 || hqNameCode.size()==0){
                        Toast.makeText(CalendarActivity.this,"Select the cluster and headquater",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(TextUtils.isEmpty(cluserCode.get(0).getTxt())|| TextUtils.isEmpty(hqNameCode.get(0).getTxt())){
                            Toast.makeText(CalendarActivity.this,"Select the cluster and headquater",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.v("printing_clustt_size11", cluserCode.size() + "");
                            saveDate(txt_date.getText().toString());
                            dialog.dismiss();
                            commonFun();
                        }
                    }
                }
                else{
                    if(TextUtils.isEmpty(txt_wrktype.getText().toString())){
                        Toast.makeText(CalendarActivity.this,"Choose work type",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        saveDate(txt_date.getText().toString());
                        dialog.dismiss();
                        commonFun();
                    }
                }


                //  bookmark.set(i,true);
                //calAdapt.notifyDataSetChanged();

            }
        });

        wrk_typ_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWrkType=1;

               /* if(rv_worktypelist.getVisibility()==View.VISIBLE){
                    rv_worktypelist.setVisibility(View.INVISIBLE);
                }
                else{
                    RelativeLayout.LayoutParams lay=new RelativeLayout.LayoutParams(509, 222);
                    // FrameLayout.LayoutParams relativeParams = (FrameLayout.LayoutParams)fl_list.getLayoutParams();
                    lay.setMargins(0, 85, 0, 0);  // left, top, right, bottom
                    rv_worktypelist.setLayoutParams(lay);
                    rv_worktypelist.setVisibility(View.VISIBLE);
                    rv_worktypelist.setAdapter(new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview_items, worktypeNm));
                }*/

                popupWrkType();


            }
        });

        ll_hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWrkType=3;
                hq_val=true;
                popupHqCluster(hqname);
            }
        });
        ll_lay_list_hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWrkType=3;
                hq_val=true;
                popupHqCluster(hqname);
            }
        });
        ll_cluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWrkType=4;
                int counts=0;
                if(SF_Type.equalsIgnoreCase("2")){
                    clusterNm.clear();
                    for(int i=0;i<hqNameCode.size();i++){
                        clusterNm.add(new PopFeed(hqNameCode.get(i).getTxt(),false));
                        clusterCode.add("");
                        posTerry+=counts+",";
                        ++counts;
                        dbh.open();
                        Cursor mCursor1 = dbh.select_ClusterList(hqNameCode.get(i).getCode());
                        // mClusterListID.clear();

                        if (mCursor1.getCount() > 0) {
                            while (mCursor1.moveToNext()) {
                                //mClusterListID.put(mCursor.getString(1), mCursor.getString(2));
                                clusterNm.add(new PopFeed(mCursor1.getString(2),false));
                                clusterCode.add(mCursor1.getString(1));
                                ++counts;
                            }
                        }
                    }
                }
                popupHqCluster(clusterNm);
            }
        });
        ll_btm_wrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWrkType=2;
                popupWrkType();
              /*  if(rv_worktypelist.getVisibility()==View.VISIBLE){
                    rv_worktypelist.setVisibility(View.INVISIBLE);
                }
                else{
                    RelativeLayout.LayoutParams lay=new RelativeLayout.LayoutParams(509, 222);
                    // FrameLayout.LayoutParams relativeParams = (FrameLayout.LayoutParams)fl_list.getLayoutParams();
                    lay.setMargins(0, 395, 0, 0);  // left, top, right, bottom
                    rv_worktypelist.setLayoutParams(lay);
                    rv_worktypelist.setVisibility(View.VISIBLE);
                    rv_worktypelist.setAdapter(new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview_items, worktypeNm));
                }*/
            }
        });


        rv_worktypelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {

                Log.v("getItem_display",String.valueOf(findWrkType));
                rv_worktypelist.setVisibility(View.INVISIBLE);
                if(findWrkType==1){
                    txt_wrktype.setText(parent.getItemAtPosition(position).toString());
                }
                else{
                    txt_btm_wrktype.setText(parent.getItemAtPosition(position).toString());
                }
               /* tv_worktype.setText(parent.getItemAtPosition(position).toString());

                for (Map.Entry<String, String> entry : mWorktypeListID.entrySet()) {
                    if (entry.getValue().equals(parent.getItemAtPosition(position).toString())) {
                        mydaywTypCd = entry.getKey();
                    }
                }*/
            }
        });


    }

    public Pair<Date, Date> getDateRange(int month) {
        Date begining, end;
        {
            Calendar calendar = getCalendarForNow(month);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
            String beg= String.valueOf(begining);
            day=beg.substring(0,3);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String val=sdf.format(begining);
            Log.v("beginingDatessss", val+" 00:00:00"+val.substring(0,val.length()-2));
            fullDate=val.substring(0,val.length()-2);
            Log.v("beginingDate", String.valueOf(beg.substring(beg.length()-5))+beg.substring(4,8));
            displayMnth=beg.substring(4,8)+"-"+beg.substring(beg.length()-5);
            currentMn=beg.substring(4,8);
            dis_mnth.setText(displayMnth);
        }

        {
            Calendar calendar = getCalendarForNow(month);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
            String beg= String.valueOf(end);
            endDate= Integer.parseInt(beg.substring(8,10));
            Log.v("endddate", String.valueOf(end));
        }
        //Log.v(" beginingDate ",begining+" EndDate "+end);
        return new Pair(begining, end);
    }

    private static Calendar getCalendarForNow(int x) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, +x);
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public void hideSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view ,
                InputMethodManager.SHOW_IMPLICIT);

    }

    public void backActivity(){
        Intent i=new Intent(CalendarActivity.this,HomeDashBoard.class);
        startActivity(i);
    }

    public void popupWrkType(){


        final Dialog dialog=new Dialog(CalendarActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);

        AdapterPopupSpinnerSelection popupAdapter=new AdapterPopupSpinnerSelection(getApplicationContext(),worktypeNm,true);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(findWrkType==1){
                    wrkSave.clear();
                    txt_wrktype.setText(worktypeNm.get(i));
                    wrkSave.add(new PopFeed(worktypeNm.get(i),worktypeCode.get(i)));
                }
                else{
                    wrkSave2.clear();
                    txt_btm_wrktype.setText(worktypeNm.get(i));
                    wrkSave2.add(new PopFeed(worktypeNm.get(i),worktypeCode.get(i)));
                }
                dialog.dismiss();
                commonFun();
            }
        });

    }

    public void popupHqCluster(final ArrayList<PopFeed> xx){
        final Dialog dialog=new Dialog(CalendarActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_feedback);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button ok=(Button)dialog.findViewById(R.id.ok);
        final ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup=(ImageView)dialog.findViewById(R.id.iv_close_popup);
        TextView tv_todayplan_popup_head=(TextView)dialog.findViewById(R.id.tv_todayplan_popup_head);
        final SearchView search_view=(SearchView)dialog.findViewById(R.id.search_view);
        if(SF_Type.equalsIgnoreCase("2")){
            if(!hq_val){
                popupAdapter=new PopupAdapter(CalendarActivity.this,xx,posTerry);
            }
            else
                popupAdapter=new PopupAdapter(CalendarActivity.this,xx);
        }
        else
            popupAdapter=new PopupAdapter(CalendarActivity.this,xx);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();
        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                commonFun();
            }
        });

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                popupAdapter.getFilter().filter(s);
                return false;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(findWrkType==3){
                    hqNameCode.clear();
                }
                else{
                    cluserCode.clear();
                }
                ArrayList<String> name=new ArrayList<>();
                for(int i=0;i<xx.size();i++) {
                    PopFeed popFeed = xx.get(i);
                    if (popFeed.isClick()) {
                        name.add(popFeed.getTxt());

                        if(SF_Type.equalsIgnoreCase("2")){

                            if(hq_val){
                                hqNameCode.add(new PopFeed(popFeed.getTxt(),hq_code.get(i)));
                                dwnloadMasterData = new DownloadMasters(getApplicationContext(), db_connPath, db_slidedwnloadPath, hq_code.get(i));
                                dwnloadMasterData.terrList();
                            }
                            else{
                                Log.v("clusterCode",clusterCode.size()+" i_value "+i);
                                cluserCode.add(new PopFeed(popFeed.getTxt(), clusterCode.get(i)));

                            }

                        }
                        else if(findWrkType==3){

                            hqNameCode.add(new PopFeed(popFeed.getTxt(),hq_code.get(i)));
                        }
                        else{
                            cluserCode.add(new PopFeed(popFeed.getTxt(),clusterCode.get(i)));
                        }

                    }

                }
                hq_val=false;
                if(findWrkType==3){
                    list_hq.setAdapter(new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview_items, name));
                }
                else{
                    list_cluster.setAdapter(new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview_items, name));
                }
                //
                dialog.dismiss();
                commonFun();

            }
        });


    }


    public void saveDate(String day){

        try {

            String codes="",names="";
            JSONObject json=new JSONObject();
            for(int k=0;k<wrkSave.size();k++){
                names=wrkSave.get(k).getTxt();
                codes=wrkSave.get(k).getCode();
            }
            json.put("WTName",names);
            json.put("WTCode",codes);
            codes="";names="";
            for(int k=0;k<wrkSave2.size();k++){
                names+=wrkSave2.get(k).getTxt();
                codes+=wrkSave2.get(k).getCode();
            }
            json.put("WTName2",names);
            json.put("WTCode2",codes);
            codes="";names="";
            for(int k=0;k<hqNameCode.size();k++){
                names+=hqNameCode.get(k).getTxt()+",";
                codes+=hqNameCode.get(k).getCode()+",";
            }
            json.put("HQNames",names);
            json.put("HQCodes",codes);
            json.put("ClusterSFs",codes);
            json.put("ClusterSFNms",names);

            codes="";names="";
            for(int k=0;k<cluserCode.size();k++){
                names+=cluserCode.get(k).getTxt()+",";
                codes+=cluserCode.get(k).getCode()+",";
            }
            json.put("ClusterCode",codes);
            json.put("ClusterName",names);
            json.put("DayRemarks","");
            json.put("FWFlg","");
            json.put("FWFlg2","");


            JSONArray jsA=jsonObject1.getJSONArray("TPDatas");
            JSONArray FillArray=new JSONArray();
            for(int i=0;i<jsA.length();i++){
                JSONObject js=jsA.getJSONObject(i);
                if(!TextUtils.isEmpty(js.getString("dayno"))){
                    if(day.equalsIgnoreCase(js.getString("dayno"))){
                        js.put("DayPlan",json);
                    }
                }
                FillArray.put(js);
            }
            JSONObject jj=new JSONObject();
            jj.put("TPDatas",FillArray);
            jj.put("SF",SF_Code);
            jj.put("SFName",sfname);
            jj.put("DivCode",div);
            jj.put("TPMonth",fullDate.substring(5,7));
            jj.put("TPYear",fullDate.substring(0,4));
            Log.v("clearCalendar",jj.getString("DivCode"));
            dbh.updateTP(currentMn,jj.toString());
            calAdapt = new CalendarAdapter(CalendarActivity.this,jj.toString());
            grid_cal.setAdapter(calAdapt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cluserCode.clear();
        hqNameCode.clear();
        wrkSave.clear();
        wrkSave2.clear();
    }

    public void getInsertValue(){
        String nxtmnth;
        dayCal();
        getDateRange(1);
        dbh.open();
        Log.v("printing_current",currentMn);
        Cursor cur=dbh.select_tp_list(currentMn);
        if(cur.getCount()>0){
            while(cur.moveToNext()) {
                Log.v("cur_json_val", cur.getString(1));
                try {
                    jsonObject1=new JSONObject(cur.getString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                calAdapt = new CalendarAdapter(CalendarActivity.this, cur.getString(2));
                grid_cal.setAdapter(calAdapt);
            }
        }
        else{
            Log.v("printing_else_part_","in_Calendare");
            cur.close();
            getDateRange(0);
            Cursor cur1=dbh.select_tp_list(currentMn);
            if(cur1.getCount()>0){

            }
            else{
                cur1.close();
                jsonArray=new JSONArray();
                jsonObject1=new JSONObject();
                dayCal();
            }
            getDateRange(1);
            jsonArray=new JSONArray();
            jsonObject1=new JSONObject();
            dayCal();
            Cursor cur2=dbh.select_tp_list(currentMn);
            if(cur2.getCount()>0){
                while(cur2.moveToNext()) {
                    Log.v("cur_json_val", cur2.getString(2));
                    try {
                        jsonObject1=new JSONObject(cur2.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    calAdapt = new CalendarAdapter(CalendarActivity.this, cur2.getString(2));
                    grid_cal.setAdapter(calAdapt);
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbh.close();

    }

    public void commonFun(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }
}
