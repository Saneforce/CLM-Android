package saneforce.sanclm.activities;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

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
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ExpandModel;
import saneforce.sanclm.activities.Model.ModelTpSave;
import saneforce.sanclm.activities.Model.ModelWorkType;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.adapter_class.AdapterExpand;
import saneforce.sanclm.adapter_class.AdapterForSelectionList;
import saneforce.sanclm.adapter_class.AdapterTpViewEntry;
import saneforce.sanclm.adapter_class.CalendarAdapter;
import saneforce.sanclm.adapter_class.MyCustomTPPager;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.GridSelectionList;
import saneforce.sanclm.util.ProductChangeListener;
import saneforce.sanclm.util.SpecialityListener;
import saneforce.sanclm.util.TwoTypeparameter;
import saneforce.sanclm.util.UpdateUi;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class DemoActivity extends AppCompatActivity {
    ViewPager viewpager;
    ListView list_selection,list_view_tp;
    ArrayList<String> array=new ArrayList<>();
    RelativeLayout lay_selection,lay_view_edit;
    Button dr_close,add_session,del_session,btn_send,btn_done,btn_approve,btn_reject;
    ArrayList<ModelTpSave> aa=new ArrayList<>();
    ArrayList<ModelTpSave> viewTp=new ArrayList<>();
    MyCustomTPPager pagerAdapter;
    GridView grid_cal;
    DataBaseHandler dbh;

    String currentMn,currentMnthNum,currentyr;
    JSONArray jsonArray=new JSONArray();
    JSONObject jsonObject=new JSONObject();
    JSONObject jsonObject1=new JSONObject();
    int endDate=0,dumedndate;
    CalendarAdapter calAdapt;
    boolean countPrint=false;
    int startCount=-1,dayCount=0;
    String[] days={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    String day=null;
    ArrayList<String> dayss=new ArrayList<String>();
    ArrayList<Boolean> bookmark=new ArrayList<>();
    String fullDate="",dumDay="",fullDateDum="";
    boolean vvv=false;
    String displayMnth=null;
    TextView txt_mnth,dr_head,txt_right_head;
    ImageView current_mnth,nxt_mnth,back_icon,sync_icon;
    CommonSharedPreference mCommonSharedPreference;
    ArrayList<PopFeed> nameCode=new ArrayList<>();
    ArrayList<ExpandModel> nameCodeExpand=new ArrayList<>();

    String SF_Code,SF_Type;
    String db_connPath,db_slidedwnloadPath;
    String wrkLoader="",hqLoader="",clusterLoader="",drLoader="",chemistLoader="",jointLoader="",hospLoader="";
    String sfname,div;
    String jsondata;
    Api_Interface apiService;
    JSONArray ja=new JSONArray();
    JSONObject jss=null;
    String selectCategory="";
    String work,hq,cluster,joint,dr,chem,hosp;
    static GridSelectionList gridselection;
    ArrayList<String> nameOnly=new ArrayList<>();
    ArrayList<String> nameOnlyIdentify=new ArrayList<>();
    Button btn1,btn2,btn3,btn4,btn_save;
    Button[] btnAry;
    int currentPos=0;
    int tempPos=0;
    boolean checkSession=false;
    String setUpForAdd="4";
    ArrayList<String> finalValue=new ArrayList<>();
    ArrayList<ModelTpSave> values=new ArrayList<>();
    Button btn_edit;
    String clusterBased="",hospitalBased="";
    String filterCatValue="";
    ExpandableListView expand_list;
    String s="";
    String checkField="";
    int count=1;
    int plannedPos=0;
    LinearLayout lin_lay_btn;
    saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods CommonUtilsMethods;

    public static String clusterneed="";
    public static String drNeed="0";
    public static String hospNeed="0";
    public static String chmNeed="0";
    public static String jwNeed="0";
    public static String cltyp="0";
    public static String addsessionNeed="0";
    public static String addSessionCount="3";
    public static boolean val_drneed=true;
    public static boolean val_chneed=true;
    public static boolean val_jwneed=true;

    public static ArrayList<ModelTpSave> list_cluster=new ArrayList<>();
    public static ArrayList<ModelWorkType> list_wrk=new ArrayList<>();
    ArrayList<String> dr_codess=new ArrayList<>();
    String clusterCode="";

    String sss="";
    String status="0",reject="";
    AlertDialog alertDialog=null;
    public static String headquaterValue="";
    JSONArray jsonArrayDum=new JSONArray();
    DownloadMasters dwnloadMasterData;
    static TwoTypeparameter twow;
    ProgressBar marker_progress;
    boolean alertshow=false;
    TextView notifi;
    String language;
    Context context;
    Resources resources;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        commonFun();
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(DemoActivity.this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(DemoActivity.this, "en");
            resources = context.getResources();
        }

        viewpager=(ViewPager)findViewById(R.id.viewpager);
        list_selection=(ListView)findViewById(R.id.list_selection);
        list_view_tp=(ListView)findViewById(R.id.list_view_tp);
        lay_selection=(RelativeLayout)findViewById(R.id.lay_selection);
        lay_view_edit=(RelativeLayout)findViewById(R.id.lay_view_edit);
        lin_lay_btn=(LinearLayout)findViewById(R.id.lin_lay_btn);
        dr_close=(Button)findViewById(R.id.dr_close);
        btn_done=(Button)findViewById(R.id.btn_done);
        btn_save=(Button)findViewById(R.id.btn_save);
        expand_list=(ExpandableListView)findViewById(R.id.expand_list);
        /*add_session=(Button)findViewById(R.id.add_session);
        del_session=(Button)findViewById(R.id.del_session);*/
        btn_edit=(Button)findViewById(R.id.btn_edit);
        btn_send=(Button)findViewById(R.id.btn_send);
        btn_approve=(Button)findViewById(R.id.btn_approve);
        btn_reject=(Button)findViewById(R.id.btn_reject);
        grid_cal=(GridView)findViewById(R.id.grid_cal);
        txt_mnth=(TextView)findViewById(R.id.txt_mnth);
        dr_head=(TextView)findViewById(R.id.dr_head);
        notifi=(TextView)findViewById(R.id.notifi);
        txt_right_head=(TextView)findViewById(R.id.txt_right_head);
        current_mnth=(ImageView)findViewById(R.id.current_mnth);
        back_icon=(ImageView)findViewById(R.id.back_icon);
        sync_icon=(ImageView)findViewById(R.id.sync_icon);
        nxt_mnth=(ImageView)findViewById(R.id.nxt_mnth);
        CommonUtilsMethods = new CommonUtilsMethods(this);
        marker_progress=(ProgressBar)findViewById(R.id.marker_progress);

        /*btn_send.getBackground().setAlpha(225);*/
        /*btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);*/


       /* btnAry=new Button[Integer.parseInt(setUpForAdd)];
        btnAry[0]=btn1;
        btnAry[1]=btn2;
        btnAry[2]=btn3;
        btnAry[3]=btn4;*/


        dbh = new DataBaseHandler(this);
        mCommonSharedPreference = new CommonSharedPreference(this);

        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        SF_Type =  mCommonSharedPreference.getValueFromPreference("sf_type");
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        jsondata = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_LOGIN_RESPONSE);
        if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
            hospNeed="0";
        else
            hospNeed="1";

        int count22= Integer.parseInt(addSessionCount);
        addSessionCount= String.valueOf(count22-1);
        addSession(count22);

       headquaterValue="";

       if(SF_Type.equalsIgnoreCase("2")){
           dbh.open();
             Cursor mCursor=dbh.select_hqlist_manager();
           if(mCursor.getCount()!=0) {
               if (mCursor.moveToFirst()) {
                   do {
                       Log.v("Name_hqlist", mCursor.getString(2));
                       Updatecluster(mCursor.getString(1));

                   } while (mCursor.moveToNext());

               }

           }
           dbh.close();
       }

        try {
            JSONObject  jsonn = new JSONObject(jsondata);
            sfname=jsonn.getString("SF_Name");
            div=jsonn.getString("Division_Code");
            Log.v("name_json",sfname+"code"+div);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(CommonUtilsMethods.isOnline(DemoActivity.this)){
                getTpsetup();
        }
        else
            if(!mCommonSharedPreference.getValueFromPreference("tpsetup").equalsIgnoreCase("null") || !TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("tpsetup")))
            assignTheSetupValue(mCommonSharedPreference.getValueFromPreference("tpsetup"));


        if(SF_Type.equalsIgnoreCase("1"))
            headquaterValue=sfname+"$"+SF_Code+"#";
       // del_session.setVisibility(View.GONE);
        nxt_mnth.setVisibility(View.GONE);

        Log.v("printing_hearrr",headquaterValue+" nice_ii ");
        aa.add(new ModelTpSave("",headquaterValue,"","","","","",""));
        list_cluster.clear();
        list_cluster.add(new ModelTpSave(false));
        pagerAdapter = new MyCustomTPPager(DemoActivity.this,aa);
        viewpager.setAdapter (pagerAdapter);
        viewpager.setOffscreenPageLimit(1);



        /*array.add("hello");
        array.add("hello");
        array.add("hello");*/
        MyCustomTPPager.bindListenerViewpagerPosition(new UpdateUi() {
            @Override
            public void updatingui() {
                viewpager.setCurrentItem(aa.size() - 1, true);
            }
        });
        MyCustomTPPager.bindListenerViewpagerDelPos(new DelUpdateView() {
            @Override
            public void delView() {
                viewpager.setAdapter(null);
                btnAry[currentPos].setVisibility(View.GONE);
                viewpager.setAdapter(pagerAdapter);
                pagerAdapter.notifyDataSetChanged();
                if(aa.size()==1)
                    btnAry[0].setVisibility(View.GONE);
                else
                enableDot(0);
            }
        });
        MyCustomTPPager.bindListenerUpdateUI(new SpecialityListener() {
            @Override
            public void specialityCode(String code) {
               /* if(code.equalsIgnoreCase("0")){
                    //del_session.setVisibility(View.GONE);
                }
                else
                    //del_session.setVisibility(View.VISIBLE);

                if(code.equalsIgnoreCase("3"))
                    //add_session.setVisibility(View.GONE);
                else
                    //add_session.setVisibility(View.VISIBLE);
*/
                enableDot(Integer.parseInt(code));

                Log.v("printing_the_key_val",code);
                currentPos=Integer.parseInt(code);

               /* if(!TextUtils.isEmpty(aa.get(currentPos).getWrk())) {
                    ModelTpSave tp = aa.get(currentPos);
                    work = tp.getWrk();
                    hq = tp.getHq();
                    cluster = tp.getCluster();
                    joint = tp.getJoint();
                    dr = tp.getDr();
                    chem = tp.getChem();
                }*/

               //loadData(aa.get(currentPos).getHq().substring());

            }
        });



        MyCustomTPPager.bindListenerCallSelection(new ProductChangeListener() {
            @Override
            public void checkPosition(int i) {
                if (!txt_right_head.getText().toString().equalsIgnoreCase("Choose the date")) {

                    lay_selection.setVisibility(View.VISIBLE);
                    nameCode.clear();
                    nameOnly.clear();
                    nameCodeExpand.clear();
                    Log.v("printing_hqp", currentPos + " here " + aa.get(currentPos).getWrk());
                    if (!TextUtils.isEmpty(aa.get(currentPos).getWrk())) {
                        ModelTpSave tp = aa.get(currentPos);
                        work = tp.getWrk();
                        hq = tp.getHq();
                        cluster = tp.getCluster();
                        joint = tp.getJoint();
                        dr = tp.getDr();
                        chem = tp.getChem();
                        hosp=tp.getHosp();
                    } else {
                        work = "";
                        hq = "";
                        cluster = "";
                        joint = "";
                        dr = "";
                        chem = "";
                        hosp="";
                    }

                    JSONArray jsonArray = null;
                    try {

                        if (i == 0) {
                            expand_list.setVisibility(View.GONE);
                            list_selection.setVisibility(View.VISIBLE);
                            recurSeparatedPrdCode(aa.get(currentPos).getWrk());
                            jsonArray = new JSONArray(wrkLoader);
                            dr_head.setText("Select Worktype");
                            selectCategory = "w";
                        } else if (i == 1) {
                            if(!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")) {
                                lay_selection.setVisibility(View.GONE);
                                selectCategory = "h";
                            }else{
                                expand_list.setVisibility(View.GONE);
                                list_selection.setVisibility(View.VISIBLE);
                                recurSeparatedPrdCode(aa.get(currentPos).getHq());
                                jsonArray = new JSONArray(hqLoader);
                                dr_head.setText("Select Hq");
                                selectCategory = "h";
                            }
                        } else if (i == 2) {
                            expand_list.setVisibility(View.GONE);
                            list_selection.setVisibility(View.VISIBLE);
                            if(SF_Type.equalsIgnoreCase("2")) {
                                String s=aa.get(currentPos).getHq();
                                Log.v("cluster_appp",s.substring(s.indexOf("$")+1,s.indexOf("#")));
                                managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"c");
                            }
                            recurSeparatedPrdCode(aa.get(currentPos).getCluster());
                            jsonArray = new JSONArray(clusterLoader);
                            dr_head.setText("Select Cluster");
                            selectCategory = "c";
                            clusterBased = "";

                        } else if (i == 3) {
                            expand_list.setVisibility(View.GONE);
                            list_selection.setVisibility(View.VISIBLE);
                            if(SF_Type.equalsIgnoreCase("2")) {
                                String s=aa.get(currentPos).getHq();
                                managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"j");
                            }
                            recurSeparatedPrdCode(aa.get(currentPos).getJoint());
                            jsonArray = new JSONArray(jointLoader);
                            dr_head.setText("Select Jointwork");
                            selectCategory = "j";
                        } else if (i == 4) {
                            expand_list.setVisibility(View.VISIBLE);
                            list_selection.setVisibility(View.GONE);
                            if(SF_Type.equalsIgnoreCase("2")) {
                                String s=aa.get(currentPos).getHq();
                                managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"d");
                            }
                            getAllCluster(aa.get(currentPos).getCluster());
                            Log.v("printing_show_dr", aa.get(currentPos).getDr());
                            plannedPos = currentPos;


                            if(hospNeed.equalsIgnoreCase("0")) {
                                getAllHosp(aa.get(currentPos).getHosp());
                                recurSeparatedPrdCode(aa.get(currentPos).getDr());
                                getDrBasedHosp(hospitalBased, drLoader);
                            }
                            else {
                                recurSeparatedPrdCode(aa.get(currentPos).getDr());
                                filterCategoryExpand(clusterBased, drLoader);
                            }

                            Log.v("printing_nameonlybased", nameOnly.size() + "filter"+filterCatValue);
                            //filterCategory(clusterBased,drLoader);
                            jsonArray = new JSONArray(filterCatValue);
                            dr_head.setText("Select "+mCommonSharedPreference.getValueFromPreference("drcap"));
                            selectCategory = "d";
                        }
                        else if(i==6){
                            expand_list.setVisibility(View.GONE);
                            list_selection.setVisibility(View.VISIBLE);
                            if(SF_Type.equalsIgnoreCase("2")) {
                                String s=aa.get(currentPos).getHq();
                                managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"c");
                            }
                            recurSeparatedPrdCode(aa.get(currentPos).getHosp());
                            jsonArray = new JSONArray(hospLoader);
                            dr_head.setText("Select Hospital");
                            selectCategory = "hos";

                        }
/*
                        else if (i == 6) {
                            expand_list.setVisibility(View.VISIBLE);
                            list_selection.setVisibility(View.GONE);
                            if(SF_Type.equalsIgnoreCase("2")) {
                                String s=aa.get(currentPos).getHq();
                                managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"h");
                            }
                            getAllCluster(aa.get(currentPos).getCluster());
                            Log.v("printing_show_hosi", aa.get(currentPos).getHosp());
                            Log.v("printing_show_hosi11", hospLoader);
                            plannedPos = currentPos;

                            recurSeparatedPrdCode(aa.get(currentPos).getHosp());
                            filterCategoryExpand(clusterBased, hospLoader);
                            Log.v("printing_nameonlyhosi", nameOnly.size() + ""+filterCatValue);
                            //filterCategory(clusterBased,drLoader);
                            jsonArray = new JSONArray(filterCatValue);
                            dr_head.setText("Select Hospital");
                            selectCategory = "hos";
                        }
*/
                        else {
                            expand_list.setVisibility(View.VISIBLE);
                            list_selection.setVisibility(View.GONE);
                            if(SF_Type.equalsIgnoreCase("2")) {
                                String s=aa.get(currentPos).getHq();
                                managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"ch");
                            }
                            getAllCluster(aa.get(currentPos).getCluster());
                            Log.v("printing_show_dr", aa.get(currentPos).getChem());
                            plannedPos = currentPos;
                            if(hospNeed.equalsIgnoreCase("0")) {
                                getAllHosp(aa.get(currentPos).getHosp());
                                recurSeparatedPrdCode(aa.get(currentPos).getChem());
                                getDrBasedHosp(hospitalBased, chemistLoader);
                            }
                            else {
                                recurSeparatedPrdCode(aa.get(currentPos).getChem());
                                filterCategoryExpand(clusterBased, chemistLoader);
                            }
                            jsonArray = new JSONArray(filterCatValue);
                            dr_head.setText("Select "+mCommonSharedPreference.getValueFromPreference("chmcap"));
                            selectCategory = "ch";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("printing_appr_refer",mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")+"hhh"+selectCategory);
                    if(!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")&&
                            (selectCategory.equalsIgnoreCase("h"))) {
                        list_selection.setVisibility(View.GONE);}
                    else {
                        try {

                            if (expand_list.getVisibility() == View.VISIBLE) {
                                Log.v("json_array_here", jsonArray.toString());
                                Log.v("name_Expandedd", nameCodeExpand.size() + " name_only " + nameOnly.size() + " printing_nameonly " + selectCategory);
                                if ((drNeed.equalsIgnoreCase("1") && selectCategory.equalsIgnoreCase("d")) || (chmNeed.equalsIgnoreCase("1") && selectCategory.equalsIgnoreCase("ch") || selectCategory.equalsIgnoreCase("hos"))) {
                                    btn_done.setVisibility(View.INVISIBLE);
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject js = jsonArray.getJSONObject(j);
                                        JSONArray jarr = js.getJSONArray("data");
                                        for (int k = 0; k < jarr.length(); k++) {
                                            JSONObject js1 = jarr.getJSONObject(k);

                                            nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                            Log.v("printing_totla_data_Val", js1.getString("name") + " rem " + js1.getString("code"));
                                        }
                                        nameCodeExpand.add(new ExpandModel(nameOnlyIdentify.get(j), nameCode));
                                        ExpandModel mm = nameCodeExpand.get(nameCodeExpand.size() - 1);
                                        mm.setChildren(nameCode);
                                        nameCode = new ArrayList<>();
                                    }
                                } else {
                                    btn_done.setVisibility(View.VISIBLE);
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject js = jsonArray.getJSONObject(j);
                                        JSONArray jarr = js.getJSONArray("data");
                                        for (int k = 0; k < jarr.length(); k++) {
                                            JSONObject js1 = jarr.getJSONObject(k);
                                            if (nameOnly.size() != 0 && nameOnly.contains(js1.getString("code")))
                                                nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));
                                            else {
                                                nameCode.add(new PopFeed(js1.getString("name"), false, js1.getString("code")));
                                            }
                                            Log.v("printing_totla_data_", js1.getString("name") + " rem " + js1.getString("code"));
                                        }
                                        nameCodeExpand.add(new ExpandModel(nameOnlyIdentify.get(j), nameCode));
                                        ExpandModel mm = nameCodeExpand.get(nameCodeExpand.size() - 1);
                                        mm.setChildren(nameCode);
                                        nameCode = new ArrayList<>();

                                    }
                                }
                                Log.v("name_Expandedd", nameCodeExpand.size() + " time " + nameCodeExpand.get(0).getChildren().size());
                                AdapterExpand adpt = new AdapterExpand(DemoActivity.this, nameCodeExpand, selectCategory);
                                expand_list.setAdapter(adpt);
                                adpt.notifyDataSetChanged();
                            }
/*
                        else    if(selectCategory.equalsIgnoreCase("hos")){
                            btn_done.setVisibility(View.VISIBLE);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject js = jsonArray.getJSONObject(j);
                                JSONArray jarr = js.getJSONArray("data");
                                for (int k = 0; k < jarr.length(); k++) {
                                    JSONObject js1 = jarr.getJSONObject(k);
                                    if (nameOnly.size() != 0 && nameOnly.contains(js1.getString("code")))
                                        nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));
                                    else {
                                        nameCode.add(new PopFeed(js1.getString("name"), false, js1.getString("code")));
                                    }
                                }
                            }
                            Log.v("total_list_namecod11e", nameCode.size() + ""+selectCategory);

                            AdapterForSelectionList adpt = new AdapterForSelectionList(DemoActivity.this, nameCode, selectCategory);
                            list_selection.setAdapter(adpt);
                            adpt.notifyDataSetChanged();

                        }
*/
                            else {
                                Log.v("name_Expandedd_nt", nameCode.size() + "");
                                btn_done.setVisibility(View.VISIBLE);

                                if (jsonArray != null)
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject js = jsonArray.getJSONObject(j);
                                        Log.v("prinatI_yyy", js.getString("code"));
                                        if (nameOnly.size() != 0 && nameOnly.contains(js.getString("code")))
                                            nameCode.add(new PopFeed(js.getString("name"), true, js.getString("code")));
                                        else {
                                            nameCode.add(new PopFeed(js.getString("name"), false, js.getString("code")));
                                        }
                                    }


                                Log.v("total_list_namecod11e", nameCode.size() + "" + selectCategory);

                                AdapterForSelectionList adpt = new AdapterForSelectionList(DemoActivity.this, nameCode, selectCategory);
                                list_selection.setAdapter(adpt);
                                adpt.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                    showToast("Select the date");
            }
        });

        sync_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("printing_the_net",mCommonSharedPreference.getValueFromPreference("net_con"));
                if(CommonUtilsMethods.isOnline(DemoActivity.this))
                    alertOnline();
                else
                    showToast("Check network connection!!");
            }
        });

        dr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_selection.setVisibility(View.GONE);
                expand_list.setVisibility(View.GONE);
                s="";
                checkField="";
                count=1;

            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeDashBoard.tpCount==0) {
                    Intent i = new Intent(DemoActivity.this, HomeDashBoard.class);
                    startActivity(i);
                }
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lay_selection.setVisibility(View.GONE);
                expand_list.setVisibility(View.GONE);
                aa.clear();
                aa.addAll(viewTp);
                if(!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")) {
                    loadData(mCommonSharedPreference.getValueFromPreference("subsf"));
                    btn_save.setVisibility(View.VISIBLE);
                }
              //  Log.v("soln_arr_inside",aa.get(0).getDr());
                if(drNeed.equalsIgnoreCase("1")) {
                    if (SF_Type.equalsIgnoreCase("2") && mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")) {
                        if(!TextUtils.isEmpty(aa.get(currentPos).getHq())) {
                            String s = aa.get(currentPos).getHq();
                            Log.v("headquater_filed", s + " remain");
                            managerLoad(s.substring(s.indexOf("$") + 1, s.indexOf("#")), "d");
                        }

                    }
                    getAllCluster(aa.get(currentPos).getCluster());

                       // Log.v("printing_show_dr", aa.get(currentPos).getDr());
                        recurSeparatedPrdCode(aa.get(currentPos).getDr());
                        filterCategoryExpand(clusterBased, drLoader);
                        //Log.v("printing_nameonlybased", nameOnly.size() + "");
                        //filterCategory(clusterBased,drLoader);
                        try {
                            jsonArray = new JSONArray(filterCatValue);
                            Log.v("hjuikolp", jsonArray.length() + "");
                            String s = "";
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject js = jsonArray.getJSONObject(j);
                                JSONArray jarr = js.getJSONArray("data");
                                for (int k = 0; k < jarr.length(); k++) {
                                    JSONObject js1 = jarr.getJSONObject(k);
                                    s += js1.getString("name") + "$" + js1.getString("code") + "#";
                                    //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                    Log.v("printing_totla_data_Val", js1.getString("name") + " rem " + js1.getString("code"));
                                }

                            }
                            aa.get(currentPos).setDr(s);
                            //twow.update(cc,0);
                        } catch (Exception e) {
                        }

                }

                if(chmNeed.equalsIgnoreCase("1")){
                    if(SF_Type.equalsIgnoreCase("2") && mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")) {
                        if(!TextUtils.isEmpty(aa.get(currentPos).getHq())) {
                            String s = aa.get(currentPos).getHq();
                            managerLoad(s.substring(s.indexOf("$") + 1, s.indexOf("#")), "ch");
                        }
                    }
                    getAllCluster(aa.get(currentPos).getCluster());
                    //if(!aa.get(currentPos).getChem().equalsIgnoreCase("")) {
                        //Log.v("printing_show_dr", aa.get(currentPos).getChem());
                        recurSeparatedPrdCode(aa.get(currentPos).getChem());
                        filterCategoryExpand(clusterBased, chemistLoader);
                        try {
                            jsonArray = new JSONArray(filterCatValue);
                            String s = "";
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject js = jsonArray.getJSONObject(j);
                                JSONArray jarr = js.getJSONArray("data");
                                for (int k = 0; k < jarr.length(); k++) {
                                    JSONObject js1 = jarr.getJSONObject(k);
                                    s += js1.getString("name") + "$" + js1.getString("code") + "#";
                                    //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                    Log.v("printing_totla_data_Val", js1.getString("name") + " rem " + js1.getString("code"));
                                }

                            }
                            aa.get(currentPos).setChem(s);
                        } catch (Exception e) {
                        }
                    /*}
                    else
                        aa.get(currentPos).setChem("");*/
                }

                lay_view_edit.setVisibility(View.GONE);
                pagerAdapter = new MyCustomTPPager(DemoActivity.this,aa);
                viewpager.setAdapter(pagerAdapter);
                work=aa.get(0).getWrk();
                pagerAdapter.notifyDataSetChanged();
            }
        });
       /* expand_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d("onGroupClick:", "worked");
                parent.expandGroup(groupPosition);
                return false;
            }
        });*/
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equalsIgnoreCase("0")||status.equalsIgnoreCase("2")||!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")) {
                    boolean checkFilledValue = true;
                    String fillTheField = "";
                    int pos = 0;
                    if (txt_right_head.getText().toString().equalsIgnoreCase("Choose the date")) {
                        showToast("Select the date");
                        checkSession = true;
                    }
                    else if (!TextUtils.isEmpty(work)) {
                        Log.v("array_size_in", aa.size() + "");
                        for (int i = 0; i < aa.size(); i++) {
                            jss = new JSONObject();
                            ModelTpSave tp = aa.get(i);
                            try {
                                if (!TextUtils.isEmpty(tp.getWrk())) {
                                    for (int k = 0; k < DemoActivity.list_wrk.size(); k++) {

                                        ModelWorkType kk = DemoActivity.list_wrk.get(k);
                                        if (kk.getCode().equalsIgnoreCase(tp.getWrk().substring(tp.getWrk().indexOf("$") + 1, tp.getWrk().length() - 1))) {
                                            Log.v("cluster_need_here", kk.getFlag());
                                            if (kk.getFlag().equalsIgnoreCase("Y")) {

                                                if(tp.getWrk().substring(0,tp.getWrk().indexOf("$")).equalsIgnoreCase("Field Work")) {
                                                    if (!TextUtils.isEmpty(tp.getWrk()) && !TextUtils.isEmpty(tp.getCluster()) && !TextUtils.isEmpty(tp.getDr())) {


                                                    } else {
                                                        pos = i;
                                                        if (TextUtils.isEmpty(tp.getHq()))
                                                            fillTheField = "Head quater";
                                                        else if (TextUtils.isEmpty(tp.getCluster()))
                                                            fillTheField = "Cluster";
                                                        else
                                                            fillTheField = "Doctor";

                                                        if(fillTheField.equalsIgnoreCase("Cluster")){
                                                            if(hospNeed.equalsIgnoreCase("0")&&(!TextUtils.isEmpty(tp.getHosp()))){
                                                                checkFilledValue = true;
                                                            }
                                                            else    if(hospNeed.equalsIgnoreCase("0")){
                                                                checkFilledValue = false;
                                                                fillTheField="hospital";
                                                            }
                                                            else
                                                                checkFilledValue = false;
                                                        }
                                                        else    if(fillTheField.equalsIgnoreCase("Doctor")){
                                                            if(drNeed.equalsIgnoreCase("1"))
                                                                checkFilledValue = true;
                                                        }
                                                        else
                                                        checkFilledValue = false;
                                                    }
                                                }

                                                else{
                                                    if (TextUtils.isEmpty(tp.getCluster())) {
                                                        pos = i;
                                                        if (TextUtils.isEmpty(tp.getHq()))
                                                            fillTheField = "Head quater";
                                                        else
                                                            fillTheField = "Cluster";

                                                        if(fillTheField.equalsIgnoreCase("Cluster")){
                                                            if(hospNeed.equalsIgnoreCase("0")&&(!TextUtils.isEmpty(tp.getHosp()))){
                                                                checkFilledValue = true;
                                                            }
                                                            else    if(hospNeed.equalsIgnoreCase("0")){
                                                                checkFilledValue = false;
                                                                fillTheField="hospital";
                                                            }
                                                            else
                                                                checkFilledValue = false;
                                                        }
                                                        else
                                                        checkFilledValue = false;
                                                    }
                                                }
                                            }
/*
                                            else{
                                                if (TextUtils.isEmpty(tp.getHq())) {
                                                    fillTheField = "Head quater";
                                                    checkFilledValue = false;
                                                }
                                            }
*/

                                        }
                                    }
                                    jss.put("work", tp.getWrk());
                                    jss.put("hq", tp.getHq());
                                    jss.put("cluster", tp.getCluster());
                                    jss.put("joint", tp.getJoint());
                                    jss.put("dr", tp.getDr());
                                    jss.put("chem", tp.getChem());
                                    jss.put("remrk", tp.getRmrk());
                                    jss.put("hos", tp.getHosp());
                                    ja.put(jss);
                                    Log.v("printing_saved", ja.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.v("Printing_saved_value", ja.toString());
                        Log.v("Printing_checkk", checkFilledValue+"");
                        //JSONObject js=new JSONObject();
                        if (checkFilledValue) {
                            try {
                        /*js.put("date",txt_right_head.getText().toString());
                        js.put("datas",ja);
                        Log.v("Printing_saved_value11",js.toString());
                        finalValue.add(js.toString());*/

                                JSONArray jsA = jsonObject1.getJSONArray("TPDatas");
                                JSONArray FillArray = new JSONArray();
                                for (int i = 0; i < jsA.length(); i++) {
                                    JSONObject js = jsA.getJSONObject(i);
                                    if (!TextUtils.isEmpty(js.getString("dayno"))) {
                                        if (day.equalsIgnoreCase(js.getString("dayno"))) {
                                            js.put("DayPlan1", ja);
                                            js.put("sent", "1");
                                        }
                                    }
                                    FillArray.put(js);
                                }
                                JSONObject jj = new JSONObject();
                                jj.put("TPDatas", FillArray);
                                jj.put("DivCode", div);


                                if(!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")){
                                    jj.put("SFName", mCommonSharedPreference.getValueFromPreference("subsfname"));
                                    jj.put("SF", mCommonSharedPreference.getValueFromPreference("subsf"));
                                    jj.put("TPMonth", fullDateDum.substring(5, 7));
                                    jj.put("TPYear", fullDateDum.substring(0, 4));
                                }
                                else {
                                    jj.put("SFName", sfname);
                                    jj.put("SF", SF_Code);
                                    jj.put("TPMonth", fullDate.substring(5, 7));
                                    jj.put("TPYear", fullDate.substring(0, 4));
                                }
                                Log.v("clearCalendar", jj.toString());
                                if(!dbh.checkOpen())
                                    dbh.open();
                                dbh.updateTP(currentMn, jj.toString());
                                calAdapt = new CalendarAdapter(DemoActivity.this, jj.toString());
                                grid_cal.setAdapter(calAdapt);
                                aa.clear();
                                viewpager.setAdapter(null);
                                aa.add(new ModelTpSave("", headquaterValue, "", "", "", "", "",""));
                                list_cluster.clear();
                                list_cluster.add(new ModelTpSave(false));
                                pagerAdapter = new MyCustomTPPager(DemoActivity.this, aa);
                                viewpager.setAdapter(pagerAdapter);
                                pagerAdapter.notifyDataSetChanged();

                                ja = new JSONArray();

                                sendSingleData(day);
                                enableDisableSubmitButton();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            work = "";
                            txt_right_head.setText("Choose the date");
                        } else {
                            viewpager.setCurrentItem(pos);
                            showToast("Select " + fillTheField);
                            jss = new JSONObject();
                            ja = new JSONArray();
                        }
                    } else
                        showToast("Select worktype");
                }
                else
                    showToast("Already sent to approval");
            }


        });

/*
        del_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewpager.setAdapter(null);
                btnAry[currentPos].setVisibility(View.GONE);
                aa.remove(currentPos);
                viewpager.setAdapter(pagerAdapter);
                pagerAdapter.notifyDataSetChanged();
                enableDot(0);
            }
        });
*/

/*
        add_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkSession=true;
                //enableDot(aa.size()-1);
                tempPos=currentPos;
                if(!TextUtils.isEmpty(work)){
                    work=aa.get(currentPos).getWrk();
                    int pos=work.indexOf("$");
                    String pp1=work.substring(0,pos);
                    if(pp1.equalsIgnoreCase("Field Work")) {
                        if (!TextUtils.isEmpty(aa.get(currentPos).getHq()) && !TextUtils.isEmpty(aa.get(currentPos).getCluster()) && (!TextUtils.isEmpty(aa.get(currentPos).getJoint()) || !TextUtils.isEmpty(aa.get(currentPos).getDr())
                                || !TextUtils.isEmpty(aa.get(currentPos).getChem()))) {
                            aa.add(new ModelTpSave("","","","","",""));
                            viewpager.setAdapter(pagerAdapter);
                            pagerAdapter.notifyDataSetChanged();
                            viewpager.setCurrentItem(aa.size() - 1, true);
                            del_session.setVisibility(View.VISIBLE);
                            //saveSessionValue(aa.size()-1);
                            work="";hq="";cluster="";joint="";dr="";chem="";
                        }
                        else
                            showToast("Fill all field " );
                    }
                    else {
                        aa.add(new ModelTpSave("","","","","",""));

                        pagerAdapter.notifyDataSetChanged();
                        viewpager.setCurrentItem(aa.size() - 1, true);
                        del_session.setVisibility(View.VISIBLE);
                        //saveSessionValue(aa.size()-1);
                        work="";hq="";cluster="";joint="";dr="";chem="";

                    }

                }
                else
                    showToast("Select Worktype " );


                Log.v("printing_the_total",aa.get(aa.size()-1).getWrk()+" joint "+(aa.size()-1));
                if(aa.size()==4)
                    add_session.setVisibility(View.GONE);
                else
                    add_session.setVisibility(View.VISIBLE);
               */
/* int pos=work.indexOf("$");
                nameOnly.add(work.substring(0,pos));
                if(TextUtils.isEmpty(work) || TextUtils.isEmpty(hq) || TextUtils.isEmpty(cluster) || TextUtils.isEmpty(joint)
                        || TextUtils.isEmpty(dr) || TextUtils.isEmpty(chem)){
                    Toast.makeText(DemoActivity.this,"Fill all the field",)
                }*//*

            }
        });
*/

        current_mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                marker_progress.setVisibility(View.VISIBLE);
                array.clear();
                jsonArray=new JSONArray();
                jsonObject1=new JSONObject();
                getDateRange(0);
                if(!mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase("null")){
                    if(mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase(txt_mnth.getText().toString())){
                        btn_send.setVisibility(View.INVISIBLE);
                    }
                    else
                        btn_send.setVisibility(View.VISIBLE);
                }

                /* dayCal();*/
                if(!dbh.checkOpen())
                    dbh.open();
                Cursor cur2=dbh.select_tp_list(currentMn);
                if(cur2.getCount()>0){
                    while(cur2.moveToNext()) {
                        Log.v("cur_json_val", cur2.getString(1)+" value "+cur2.getString(2));
                        try {
                            jsonObject1=new JSONObject(cur2.getString(2));
                            if(cur2.getString(  3).equalsIgnoreCase("1") || cur2.getString(  3).equalsIgnoreCase("3")
                                || cur2.getString(  3).equalsIgnoreCase("2")){
                                status=cur2.getString(3);
                                Log.v("printing_Statuis11",cur2.getString(3));
                            }
                            else{
                                status="0";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        calAdapt = new CalendarAdapter(DemoActivity.this, cur2.getString(2));
                        grid_cal.setAdapter(calAdapt);
                        calAdapt.notifyDataSetChanged();
                        if(mCommonSharedPreference.getValueFromPreference("net_con").equalsIgnoreCase("connect"))
                            callOldDatas(currentMn);
                        else
                            marker_progress.setVisibility(View.GONE);
                        enableDisableSubmitButton();

                    }
                }
//                else{
////                    getInsertValue();
////                    loadData(SF_Code);
//                      callOldDatas(currentMn);
//                }
                current_mnth.setVisibility(View.INVISIBLE);
                nxt_mnth.setVisibility(View.VISIBLE);
            }
        });


        nxt_mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                marker_progress.setVisibility(View.VISIBLE);
                jsonArray=new JSONArray();
                array.clear();
                jsonObject1=new JSONObject();
                getDateRange(1);
                if(!mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase("null")){
                    if(mCommonSharedPreference.getValueFromPreference("mnth").equalsIgnoreCase(txt_mnth.getText().toString())){
                        btn_send.setVisibility(View.INVISIBLE);
                    }
                    else
                        btn_send.setVisibility(View.VISIBLE);
                }


                Cursor cur2=dbh.select_tp_list(currentMn);
                if(cur2.getCount()>0){
                    while(cur2.moveToNext()) {
                        Log.v("cur_json_val", cur2.getString(1));
                        try {
                            jsonObject1=new JSONObject(cur2.getString(2));
                            if(cur2.getString(  3).equalsIgnoreCase("1") || cur2.getString(  3).equalsIgnoreCase("3")
                                    || cur2.getString(  3).equalsIgnoreCase("2")){
                                status=cur2.getString(3);
                                Log.v("printing_Statuis22",cur2.getString(3));
                            }
                            else
                                status="0";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        calAdapt = new CalendarAdapter(DemoActivity.this, cur2.getString(2));
                        grid_cal.setAdapter(calAdapt);
                        if(mCommonSharedPreference.getValueFromPreference("net_con").equalsIgnoreCase("connect"))
                            callOldDatas(currentMn);
                        else
                            marker_progress.setVisibility(View.GONE);
                        enableDisableSubmitButton();
                    }
                }
//                else{
////                    getInsertValue();
////                   loadData(SF_Code);
//                  callOldDatas(currentMn);
//                }
                current_mnth.setVisibility(View.VISIBLE);
                nxt_mnth.setVisibility(View.INVISIBLE);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mCommonSharedPreference.getValueFromPreference("net_con").equalsIgnoreCase("connect")) {
                    sendingOfflineTpData(1);
                }
                else
                    showToast("Check Network Connection");
            }
        });

        grid_cal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.v("dayITemClick",CalendarAdapter.days.get(i));
               // if(i>=startCount){
                if(!TextUtils.isEmpty(CalendarAdapter.days.get(i))){
                    txt_right_head.setText(CalendarAdapter.days.get(i)+"-"+txt_mnth.getText().toString());
                    day=CalendarAdapter.days.get(i);
                    //popUpAlert(i,CalendarAdapter.days.get(i));
                   /* if(TextUtils.isEmpty(CalendarAdapter.arr_json.get(i).getCode()) && !TextUtils.isEmpty(aa.get(0).getWrk())){
                        Log.v("printing_all_date","first_here");
                    }*/
                    /*else*/ if(TextUtils.isEmpty(CalendarAdapter.arr_json.get(i).getCode())){
                        Log.v("printing_all_date","second_here");
                        if(!checkSession) {
                            lay_view_edit.setVisibility(View.GONE);
                            lay_selection.setVisibility(View.GONE);
                            aa.clear();
                            aa.add(new ModelTpSave("", headquaterValue, "", "", "", "","",""));
                            pagerAdapter = new MyCustomTPPager(DemoActivity.this, aa);
                            viewpager.setAdapter(pagerAdapter);
                            pagerAdapter.notifyDataSetChanged();
                            checkSession=false;
                           /* btn1.setVisibility(View.GONE);
                            btn2.setVisibility(View.GONE);
                            btn3.setVisibility(View.GONE);
                            btn4.setVisibility(View.GONE);*/
                        }
                        checkSession=false;
                    }
                    else{
                        Log.v("printing_all_date","third_here");
                        Log.v("grid_final_val",CalendarAdapter.arr_json.get(i).getCode());
                        checkSession=false;
                        aa.clear();
                        pagerAdapter.notifyDataSetChanged();
                        try {
                            JSONArray jarray=new JSONArray(CalendarAdapter.arr_json.get(i).getCode());
                            viewTp.clear();
                            list_cluster.clear();
                            lay_view_edit.setVisibility(View.VISIBLE);
                            for(int k=0;k<jarray.length();k++){
                                JSONObject joss=jarray.getJSONObject(k);
                                String  hos="";
                                if(joss.has("hos"))
                                    hos=joss.getString("hos");
                                viewTp.add(new ModelTpSave(joss.getString("work"),joss.getString("hq"),joss.getString("cluster"),joss.getString("joint"),joss.getString("dr"),joss.getString("chem"),joss.getString("remrk"),hos));
                                list_cluster.add(new ModelTpSave(false));
                                Log.v("list_cluster_are",list_cluster.size()+"");
                            }
                            AdapterTpViewEntry adpt=new AdapterTpViewEntry(DemoActivity.this,viewTp);
                            list_view_tp.setAdapter(adpt);
                            adpt.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //aa.add(new ModelTpSave("","","","","",""));
                    }
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommonSharedPreference.setValueToPreference("tp_position",String.valueOf(currentPos));
                s="";
                checkField="";
                count=1;
                JSONObject jsonObj=new JSONObject();
                if(expand_list.getVisibility()==View.VISIBLE){
                    s="";
                    checkField="";
                    count=1;
                    boolean selectionAvail=false;

                    for(int j=0;j<nameCodeExpand.size();j++){
                       /* s="";
                        checkField="";
                        count=1;*/
                       List<PopFeed> mm=nameCodeExpand.get(j).getChildren();
                       if(mm.size()!=0)
                           checkForCheckedItem(mm, j);//{//
                           /*if (mm.contains(new PopFeed(true)))
                               selectionAvail=true;*/


                      // }
                    }

                        expand_list.setVisibility(View.GONE);
                        lay_selection.setVisibility(View.GONE);
                      /*  s="";
                        checkField="";
                        count=1;*/
                   /* }
                    else
                        Toast.makeText(DemoActivity.this,"Atleast select one field",Toast.LENGTH_SHORT).show();*/
                }
                else{
                    //if (nameCode.contains(new PopFeed(true))) {
                        checkForCheckedItem(nameCode, 0);
                        lay_selection.setVisibility(View.GONE);
                        expand_list.setVisibility(View.GONE);

                        /*s="";
                        checkField="";
                        count=1;*/
                    /*}
                    else
                        Toast.makeText(DemoActivity.this,"Atleast select one field",Toast.LENGTH_SHORT).show();*/
                }
/*
                if (nameCode.contains(new PopFeed(true))) {
                    for(int i=0;i<nameCode.size();i++){
                        PopFeed pp=nameCode.get(i);
                        if(pp.isClick()){
                            s+=pp.getTxt()+"$"+pp.getCode()+"#";
                            if(selectCategory.equalsIgnoreCase("c"))
                                clusterBased+=pp.getCode()+",";
                            if(selectCategory.equalsIgnoreCase("j")||selectCategory.equalsIgnoreCase("d")
                        ||selectCategory.equalsIgnoreCase("ch")){
                                checkField= String.valueOf(count);
                                count++;
                            }
                            else
                            checkField=pp.getTxt();
                        }
                    }
                    if(selectCategory.equalsIgnoreCase("w"))
                    {
                        if(!checkField.equalsIgnoreCase("Field Work")){

                            gridselection.selectionList(checkField);
                            gridselection.unselectionList("dd");
                        }
                        else
                            gridselection.selectionList(checkField);
                    }
                    else
                        gridselection.selectionList(checkField);

                    switch (selectCategory){
                        case "w":
                            work=s;
                            break;
                        case "h":
                            hq=s;
                            break;
                        case "c":
                            cluster=s;
                            break;
                        case "j":
                            Log.v("joint_wrk_tp",s);
                            joint=s;
                            break;
                        case "d":
                            dr=s;
                            break;
                        case "ch":
                            chem=s;
                            break;
                    }
                    lay_selection.setVisibility(View.GONE);
                    */
/*if(!TextUtils.isEmpty(aa.get(currentPos).getWrk())){
                        saveSessionValue(currentPos);
                    }*//*

                    Log.v("print_curren_pos",currentPos+"");
                    saveCertainValue(currentPos);
                }
*/
               /* else
                    Toast.makeText(DemoActivity.this,"Kindly select the item",Toast.LENGTH_SHORT).show();*/
/*
               if(drNeed.equalsIgnoreCase("1")) {
                   try {
                       if (SF_Type.equalsIgnoreCase("2")) {
                           String s = aa.get(currentPos).getHq();
                           managerLoad(s.substring(s.indexOf("$") + 1, s.indexOf("#")), "d");
                       }
                       getAllCluster(aa.get(currentPos).getCluster());
                       Log.v("printing_show_dr", aa.get(currentPos).getDr());
                       plannedPos = currentPos;

                       recurSeparatedPrdCode(aa.get(currentPos).getDr());
                       filterCategoryExpand(clusterBased, drLoader);
                       Log.v("printing_nameonlybased", nameOnly.size() + "");
                       //filterCategory(clusterBased,drLoader);
                       jsonArray = new JSONArray(filterCatValue);
                       // dr_head.setText("Select Doctor");
                       Log.v("total_value_dr",jsonArray.length()+"");

                   } catch (Exception e) {
                   }
               }
*/

            }
        });

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendingOfflineTpData(2);
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertForReject(0);

            }
        });
        Log.v("approve_print",mCommonSharedPreference.getValueFromPreference("approve"));
        if(!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")){
           current_mnth.setVisibility(View.INVISIBLE);
           nxt_mnth.setVisibility(View.INVISIBLE);
           btn_save.setVisibility(View.INVISIBLE);
           btn_send.setVisibility(View.GONE);
           btn_approve.setVisibility(View.VISIBLE);
           btn_reject.setVisibility(View.VISIBLE);
           btn_edit.setText("Edit");
            getDateRangeDum(Integer.parseInt(mCommonSharedPreference.getValueFromPreference("tpmonth"))-1,Integer.parseInt(mCommonSharedPreference.getValueFromPreference("tpyear")));
            dayCaldumm();
            Log.v("jsonObject1",jsonObject1.toString());
            loadData(mCommonSharedPreference.getValueFromPreference("subsf"));
            marker_progress.setVisibility(View.VISIBLE);
            retrieveOldDatas(mCommonSharedPreference.getValueFromPreference("approve"),currentMn);
        }
        else {
            getInsertValue();
            loadData(SF_Code);
            callOldDatas(currentMn);
        }
       // getDateRangeDum(3,2020);
        //dayCaldumm();
        commonFun();
    }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
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
                Log.v("cur_json_val_inser", cur.getString(2));
                try {
                    jsonObject1=new JSONObject(cur.getString(2));
                    status=cur.getString(3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                calAdapt = new CalendarAdapter(DemoActivity.this, cur.getString(2));
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
                    Log.v("cur_json_val_else", cur2.getString(2));
                    try {
                        jsonObject1=new JSONObject(cur2.getString(2));
                        status=cur2.getString(3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    calAdapt = new CalendarAdapter(DemoActivity.this, cur2.getString(2));
                    grid_cal.setAdapter(calAdapt);
                }
            }
        }
        enableDisableSubmitButton();

    }


    public void dayCaldumm() {
        try {
            Log.v("print_enddate", dumedndate + "");
            for (int i = 1; i <= dumedndate; i++) {
                if (!countPrint) {
                    for (int j = 0; j < days.length; j++) {
                        if (countPrint)
                            break;
                        if (days[j].equalsIgnoreCase(dumDay)) {
                            array.add(String.valueOf(i));
                            countPrint = true;
                            startCount = j;
                            dayss.add(days[j]);
                            bookmark.add(false);
                            dayCount = j;
                            jsonObject = new JSONObject();
                            jsonObject.put("access", "1");
                            jsonObject.put("dayno", String.valueOf(i));
                            jsonObject.put("TPDt", fullDateDum + String.valueOf(i) + " 00:00:00");
                            //TPDatas

                            jsonArrayDum.put(jsonObject);
                        } else {
                            array.add("");
                            dayss.add(days[j]);
                            bookmark.add(false);
                            dayCount = j;
                            jsonObject = new JSONObject();
                            jsonObject.put("access", "0");
                            jsonObject.put("dayno", "");
                            jsonArrayDum.put(jsonObject);
                        }
                    }
                } else {
                    array.add(String.valueOf(i));
                    jsonObject = new JSONObject();
                    jsonObject.put("access", "1");
                    jsonObject.put("dayno", String.valueOf(i));
                    jsonObject.put("TPDt", fullDateDum + String.valueOf(i) + " 00:00:00");
                    jsonObject.put("sent", "0");

                    jsonArrayDum.put(jsonObject);
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
            jsonObject1.put("TPDatas",jsonArrayDum);
           /* jsonObject1.put("SF",SF_Code);
            jsonObject1.put("SFName",sfname);
            jsonObject1.put("DivCode",div);*/
            jsonObject1.put("TPMonth",fullDateDum.substring(5,7));
            jsonObject1.put("TPYear",fullDateDum.substring(0,4));
            jsonObject1.put("sent","0");
            Log.v("printing_the_json_dum","div"+jsonObject1.toString());
            calAdapt = new CalendarAdapter(DemoActivity.this, jsonObject1.toString());
            grid_cal.setAdapter(calAdapt);
            //calAdapt.notifyDataSetChanged();

        }catch (Exception e){}
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
                    jsonObject.put("sent", "0");

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
            Log.v("printing_the_json_array",jsonArray.toString());
            jsonObject1.put("TPDatas",jsonArray);
            jsonObject1.put("SF",SF_Code);
            jsonObject1.put("SFName",sfname);
            jsonObject1.put("DivCode",div);
            jsonObject1.put("TPMonth",fullDate.substring(5,7));
            jsonObject1.put("TPYear",fullDate.substring(0,4));
            jsonObject1.put("sent","0");
            /*jsonObject1.put("SF",SF_Code);
            jsonObject1.put("SFName",sfname);
            jsonObject1.put("DivCode",div);
            jsonObject1.put("TPMonth",fullDate.substring(5,7));
            jsonObject1.put("TPYear",fullDate.substring(0,4));
            */
        }catch (Exception e){}

        Log.v("calendarActivity", String.valueOf(jsonObject1)+" month "+currentMn);
        dbh.open();
        dbh.insertTP(String.valueOf(jsonObject1),currentMn,status);
        countPrint=false;

        /*if(vvv==true) {
            Log.v("json_objectccc",String.valueOf(jsonObject1));

        }*/
        vvv=true;
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
            Log.v("beginingDatessss", val+" 00:00:00"+val.substring(5,7));
            currentMnthNum=val.substring(5,7);
            currentyr=val.substring(0,4);
                    fullDate=val.substring(0,val.length()-2);
            Log.v("beginingDate", String.valueOf(beg.substring(beg.length()-5))+beg.substring(4,8));
            displayMnth=beg.substring(4,8)+"-"+beg.substring(beg.length()-5);
            currentMn=beg.substring(4,8);
            txt_mnth.setText(displayMnth);
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
    public Pair<Date, Date> getDateRangeDum(int month,int year) {
        Date begining, end;
        {
            Calendar calendar = getCalendarForParticular(month,year);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
            String beg= String.valueOf(begining);
            dumDay=beg.substring(0,3);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String val=sdf.format(begining);
            Log.v("beginingDatessss", val+" 00:00:00"+val.substring(5,7));
            displayMnth=beg.substring(4,8)+"-"+beg.substring(beg.length()-5);
            fullDateDum=val.substring(0,val.length()-2);
            currentMn=beg.substring(4,8);
            txt_mnth.setText(displayMnth);
            /*currentMnthNum=val.substring(5,7);
            currentyr=val.substring(0,4);
                    fullDate=val.substring(0,val.length()-2);
            Log.v("beginingDate", String.valueOf(beg.substring(beg.length()-5))+beg.substring(4,8));
            displayMnth=beg.substring(4,8)+"-"+beg.substring(beg.length()-5);
            currentMn=beg.substring(4,8);
            txt_mnth.setText(displayMnth);*/
        }

        {
            Calendar calendar = getCalendarForParticular(month,year);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
            String beg= String.valueOf(end);
            dumedndate= Integer.parseInt(beg.substring(8,10));
            Log.v("endddate_dum", String.valueOf(end));
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
    private static Calendar getCalendarForParticular(int x,int year) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, x);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
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


    public void loadDataApprove(String SF_Code){
        JSONArray jsonArrayData=new JSONArray();

        dbh.open();
        Cursor mCursor = dbh.select_TPworktypeList(SF_Code);

        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                list_wrk.add(new ModelWorkType(mCursor.getString(2),mCursor.getString(1),mCursor.getString(4)));
                JSONObject jsonObjectData=new JSONObject();
                try {
                    jsonObjectData.put("name",mCursor.getString(2));
                    jsonObjectData.put("code",mCursor.getString(1));
                    jsonArrayData.put(jsonObjectData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        wrkLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_typedemo",wrkLoader);
    }
    public void loadData(String SF_Code){
        JSONArray jsonArrayData=new JSONArray();

        dbh.open();
        Cursor mCursor = dbh.select_TPworktypeList(SF_Code);

        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                list_wrk.add(new ModelWorkType(mCursor.getString(2),mCursor.getString(1),mCursor.getString(4)));
                JSONObject jsonObjectData=new JSONObject();
                try {
                    jsonObjectData.put("name",mCursor.getString(2));
                    jsonObjectData.put("code",mCursor.getString(1));
                    jsonArrayData.put(jsonObjectData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        wrkLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_type",wrkLoader);

        mCursor = dbh.select_ClusterList(SF_Code);
        // mClusterListID.clear();
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                //mClusterListID.put(mCursor.getString(1), mCursor.getString(2));
               /* if(clN!=null && clN.contains(mCursor.getString(2))){
                    clusterNm.add(new PopFeed(mCursor.getString(2),true));
                }
                else{
                    clusterNm.add(new PopFeed(mCursor.getString(2),false));}
                clusterCode.add(mCursor.getString(1));*/
                JSONObject jsonObjectData=new JSONObject();
                try {
                    jsonObjectData.put("name",mCursor.getString(2));
                    jsonObjectData.put("code",mCursor.getString(1));
                    jsonArrayData.put(jsonObjectData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        clusterLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_typedemoo",clusterLoader);

        if(SF_Type.equalsIgnoreCase("2"))
            mCursor=dbh.select_hqlist_manager();
        else
            mCursor=dbh.select_hqlist(SF_Code);

            if(mCursor.getCount()!=0) {
            if (mCursor.moveToFirst()) {
                do {
                    Log.v("Name_hqlist", mCursor.getString(2));

                   /* if(hqN!=null && hqN.contains(mCursor.getString(2))){
                        hqname.add(new PopFeed(mCursor.getString(2),true));
                    }
                    else
                        hqname.add(new PopFeed(mCursor.getString(2),false));
                    hq_code.add(mCursor.getString(1));*/

                    JSONObject jsonObjectData=new JSONObject();
                    try {
                        jsonObjectData.put("name",mCursor.getString(2));
                        jsonObjectData.put("code",mCursor.getString(1));
                        jsonArrayData.put(jsonObjectData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (mCursor.moveToNext());

            }

        }
        hqLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_type",hqLoader);

        mCursor = dbh.select_ClusterList(SF_Code);
        // mClusterListID.clear();
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                //mClusterListID.put(mCursor.getString(1), mCursor.getString(2));
               /* if(clN!=null && clN.contains(mCursor.getString(2))){
                    clusterNm.add(new PopFeed(mCursor.getString(2),true));
                }
                else{
                    clusterNm.add(new PopFeed(mCursor.getString(2),false));}
                clusterCode.add(mCursor.getString(1));*/
                JSONObject jsonObjectData=new JSONObject();
                try {
                    jsonObjectData.put("name",mCursor.getString(2));
                    jsonObjectData.put("code",mCursor.getString(1));
                    jsonArrayData.put(jsonObjectData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            }
        clusterLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_type",clusterLoader);

        mCursor = dbh.select_dr_sfcode(SF_Code);
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                JSONObject jsonObjectData=new JSONObject();
                try {
                    jsonObjectData.put("name",mCursor.getString(2));
                    jsonObjectData.put("code",mCursor.getString(1));
                    jsonObjectData.put("clcode",mCursor.getString(5));
                    jsonObjectData.put("hcode",mCursor.getString(25));
                    jsonArrayData.put(jsonObjectData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        drLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_type",drLoader);

        mCursor = dbh.select_joint_list();
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                if(!SF_Code.equalsIgnoreCase(mCursor.getString(0))) {
                    JSONObject jsonObjectData = new JSONObject();
                    try {
                        jsonObjectData.put("name", mCursor.getString(0));
                        jsonObjectData.put("code", mCursor.getString(1));
                        jsonArrayData.put(jsonObjectData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        jointLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_type",jointLoader);

       //select_chem_sfcode
        mCursor = dbh.select_chem_sfcode(SF_Code);
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                JSONObject jsonObjectData=new JSONObject();
                try {
                    jsonObjectData.put("name",mCursor.getString(2));
                    jsonObjectData.put("code",mCursor.getString(1));
                    jsonObjectData.put("clcode",mCursor.getString(4));
                    jsonObjectData.put("hcode",mCursor.getString(13));
                    jsonArrayData.put(jsonObjectData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        chemistLoader=jsonArrayData.toString();
        jsonArrayData=new JSONArray();
        Log.v("printing_wrk_type",chemistLoader+"hospneed"+hospNeed);

        if(hospNeed.equalsIgnoreCase("0")){
            mCursor = dbh.select_hospitalist(SF_Code);
            if (mCursor.getCount() > 0) {
                while (mCursor.moveToNext()) {
                    JSONObject jsonObjectData=new JSONObject();
                    try {
                        jsonObjectData.put("name",mCursor.getString(3));
                        jsonObjectData.put("code",mCursor.getString(4));
                        jsonObjectData.put("clcode",mCursor.getString(1));
                        jsonArrayData.put(jsonObjectData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            hospLoader=jsonArrayData.toString();
            jsonArrayData=new JSONArray();
            Log.v("printing_hsp_type",hospLoader);
        }
    }

    public static void bindListenerHideUI(GridSelectionList grid){
        gridselection=grid;
    }

    public String recurSeparateProduct(String s){
        if(TextUtils.isEmpty(s))
            return "";
        int pos=s.indexOf("$");
        nameOnly.add(s.substring(0,pos));
        Log.v("printing_all_fieldd",s.substring(0,pos));
        pos=s.indexOf("#");
        if(pos==s.length()-1)
            return "";
        else {
            s = s.substring(pos+1);
            return recurSeparateProduct(s);
        }
    }

    public String recurSeparatedPrdCode(String s){
        Log.v("printing_name_prd",s);

        if(TextUtils.isEmpty(s))
            return "";
        int pos=s.indexOf("$");
        int pos1=s.indexOf("#");
        nameOnly.add(s.substring(pos+1,pos1));
        nameOnlyIdentify.add(s.substring(0,pos));
        Log.v("printing_all_fieldd",s.substring(pos+1,pos1)+" names "+" name_cluster "+s.substring(0,pos));
        if(pos1==s.length()-1)
            return "";
        else {
            s = s.substring(pos1+1);
            return recurSeparatedPrdCode(s);
        }
    }
    public void enableDot(int pos){
        Log.v("printing_the_pos",pos+"");
        try {
            if (btnAry != null) {
                for (int i = 0; i < btnAry.length; i++) {
                    btnAry[i].setVisibility(View.GONE);
                }
            }
            if (aa.size() != 1) {
                for (int i = 0; i < aa.size(); i++) {
                    btnAry[i].setVisibility(View.VISIBLE);
                    if (i == pos) {
                        Log.v("printing_the_pos_11", pos + "");
                        setColoredDotRed(btnAry[i]);
                    } else {
                        setColoredDot(btnAry[i]);
                    }

                }
            }
        }catch (Exception e){}
    }

    public void setColoredDot(Button btn){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackgroundDrawable(ContextCompat.getDrawable(DemoActivity.this, R.drawable.shape_plain_grey_circle_tp) );
        } else {
            btn.setBackground(ContextCompat.getDrawable(DemoActivity.this, R.drawable.shape_plain_grey_circle_tp));
        }
    }
    public void setColoredDotRed(Button btn){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackgroundDrawable(ContextCompat.getDrawable(DemoActivity.this, R.drawable.shape_plain_red_circle_tp) );
        } else {
            btn.setBackground(ContextCompat.getDrawable(DemoActivity.this, R.drawable.shape_plain_red_circle_tp));
        }
    }
    public void showToast(String msg){
        if(msg.equalsIgnoreCase("Doctor"))
            msg=mCommonSharedPreference.getValueFromPreference("drcap");

        try {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            try {
                Toast.makeText(DemoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }catch (Exception e1){
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveSessionValue(int pos){
       /* try {
            jss=new JSONObject();
            jss.put("work",work);
            jss.put("hq",hq);
            jss.put("cluster",cluster);
            jss.put("joint",joint);
            jss.put("dr",dr);
            jss.put("chem",chem);


        } catch (JSONException e) {
            e.printStackTrace();
        }*/
       Log.v("printing_pos_save", String.valueOf(pos));
       ModelTpSave tp=new ModelTpSave(work,hq,cluster,joint,dr,chem,"");
       aa.set(pos,tp);
       pagerAdapter.notifyDataSetChanged();

    }

    public void saveCertainValue(int pos) {
        ModelTpSave tp;
        if(selectCategory.equalsIgnoreCase("d")||selectCategory.equalsIgnoreCase("ch")) {
            tp = aa.get(plannedPos);
            Log.v("position_certain", plannedPos + "");
        }
        else
             tp = aa.get(pos);
        Log.v("position_certain", pos + "");

        switch (selectCategory) {
            case "w":
                tp.setWrk(work);
                break;
            case "h":
                tp.setHq(hq);
                break;
            case "c":
                Log.v("show_the_dr_val12",dr);
                tp.setCluster(cluster);
                Log.v("show_the_dr_val22",dr);
                break;
            case "j":
                tp.setJoint(joint);
                break;
            case "d":
                Log.v("show_the_dr_val",dr);
                tp.setDr(dr);
                break;
            case "ch":
                tp.setChem(chem);
                break;
            case "hos":
                tp.setHosp(hosp);
                break;
        }
        Log.v("printing_total_val22", tp.getWrk() + "hq" + tp.getHq() + "cluster" + tp.getCluster() + "joint" + tp.getJoint()
                + "dr " + tp.getDr() + "chem " + tp.getChem());
        viewpager.setAdapter(null);
        viewpager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        viewpager.setCurrentItem(Integer.parseInt(mCommonSharedPreference.getValueFromPreference("tp_position")), true);
    }

    public interface DelUpdateView{

        void delView();
    }

    public void filterCategory(String codes,String dr){
        filterCatValue="";
        String[] clus=clusterBased.split(",");
        ArrayList<String> a1=new ArrayList<>(Arrays.asList(clus));
        Log.v("selected_codeing",a1.toString());
        try {
        JSONArray jj=new JSONArray(dr);
            JSONArray jjj=new JSONArray();

            for(int j=0;j<jj.length();j++){

                JSONObject js=jj.getJSONObject(j);
            Log.v("shift_value_inside11",js.toString());
                if(a1.contains(js.getString("clcode"))){
                    Log.v("shift_value_inside",js.toString());
                    jjj.put(js);

                }

        }

        Log.v("printing_final_drr",jjj.toString());
        filterCatValue=jjj.toString();
        Log.v("dr_load_finding",filterCatValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*for(int i=0;i<=a1.size()-1;i++){
            Log.v("printing_savedone",clus[i]);
        }*/
    }
    public String   getDrBasedHosp(String   hos,String  dr) {
        filterCatValue="";
        Log.v("filtering_expand",hospitalBased);
        String[] clus=hospitalBased.split(",");
        ArrayList<String> a1=new ArrayList<>(Arrays.asList(clus));
        Log.v("print_hos_code",dr);
       /* Log.v("hosital_value_check", hos);
        Log.v("hos_substring", hos.substring(hos.indexOf("$") + 1, hos.indexOf("#")));
        Log.v("dr_value_check", dr);*/

        JSONArray jjj=new JSONArray();
        JSONArray jj_dr=new JSONArray();
        JSONObject jclust=new JSONObject();

        try {
            JSONArray   jj=new JSONArray(dr);

            for(int k=0;k<a1.size();k++){
                jclust=new JSONObject();
                jj_dr=new JSONArray();
                jclust.put("clust",a1.get(k));
                for(int j=0;j<jj.length();j++) {
                    JSONObject js=jj.getJSONObject(j);
                    if(a1.get(k).equalsIgnoreCase(js.getString("hcode"))){
                        Log.v("showing_det",js.getString("clcode")+" drc "+js.getString("name"));
                        jj_dr.put(js);
                    }
                }
                Log.v("each_array_print",jj_dr.length()+"");
                jclust.put("data",jj_dr);
                jjj.put(jclust);
            }
            Log.v("printing_final_hos",jjj.toString());
            filterCatValue=jjj.toString();
            Log.v("drhos_load_finding",filterCatValue);
    }catch(Exception    e){
            Log.v("drhos_load_finding",e.getMessage());
        }
        return  "";
    }

    public void filterCategoryExpand(String codes,String dr){
        filterCatValue="";
        Log.v("filtering_expand",clusterBased);
        String[] clus=clusterBased.split(",");
        ArrayList<String> a1=new ArrayList<>(Arrays.asList(clus));
        Log.v("selected_codeing",a1.toString());
        try {
            JSONArray jj=new JSONArray(dr);
            JSONArray jjj=new JSONArray();
            JSONArray jj_dr=new JSONArray();
            JSONObject jclust=new JSONObject();

            for(int k=0;k<a1.size();k++){
                jclust=new JSONObject();
                jj_dr=new JSONArray();
                jclust.put("clust",a1.get(k));
                for(int j=0;j<jj.length();j++) {
                    JSONObject js=jj.getJSONObject(j);
                if(a1.get(k).equalsIgnoreCase(js.getString("clcode"))){
                    Log.v("showing_det",js.getString("clcode")+" drc "+js.getString("name"));
                    jj_dr.put(js);
                }
                }
                Log.v("each_array_print",jj_dr.length()+"");
                jclust.put("data",jj_dr);
                jjj.put(jclust);
            }
            /*for(int j=0;j<jj.length();j++){

                JSONObject js=jj.getJSONObject(j);
                Log.v("shift_value_inside11",js.toString());
                if(a1.contains(js.getString("clcode"))){
                    Log.v("shift_value_inside",js.toString());
                    jjj.put(js);

                }

            }
*/
            Log.v("printing_final_drr",jjj.toString());
            filterCatValue=jjj.toString();
            Log.v("dr_load_finding",filterCatValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*for(int i=0;i<=a1.size()-1;i++){
            Log.v("printing_savedone",clus[i]);
        }*/
    }

    public void getAllCluster(String list){
        clusterBased="";
        String s="";
        String checkField="";
        int count=1;
        nameOnly.clear();
        nameOnlyIdentify.clear();
        recurSeparatedPrdCode(list);
        for(int k=0;k<nameOnly.size();k++){
            clusterBased+=nameOnly.get(k)+",";
        }
        nameCode.clear();
        nameOnly.clear();
        Log.v("printing_cluster_Test",list+" cluster "+drLoader);
/*
        if (list.contains(new PopFeed(true))) {
            for(int i=0;i<list.size();i++) {
                PopFeed pp = list.get(i);
                if (pp.isClick()) {
                    s += pp.getTxt() + "$" + pp.getCode() + "#";
                    if (selectCategory.equalsIgnoreCase("c"))
                        clusterBased += pp.getCode() + ",";
                }
            }
        }
*/
    }


    public void getAllHosp(String list){
        hospitalBased="";
        String s="";
        String checkField="";
        int count=1;
        nameOnly.clear();
        nameOnlyIdentify.clear();
        recurSeparatedPrdCode(list);
        for(int k=0;k<nameOnly.size();k++){
            hospitalBased+=nameOnly.get(k)+",";
        }
        nameCode.clear();
        nameOnly.clear();
        Log.v("printing_cluster_hosp",list+" hospital "+hospitalBased);
/*
        if (list.contains(new PopFeed(true))) {
            for(int i=0;i<list.size();i++) {
                PopFeed pp = list.get(i);
                if (pp.isClick()) {
                    s += pp.getTxt() + "$" + pp.getCode() + "#";
                    if (selectCategory.equalsIgnoreCase("c"))
                        clusterBased += pp.getCode() + ",";
                }
            }
        }
*/
    }




    public void checkForCheckedItem(List<PopFeed> list, int x){
        boolean check=false;
        if(x==0){
           s="";
            checkField="";
             count=1;
             check=false;
        }


        if (list.contains(new PopFeed(true))) {
            for(int i=0;i<list.size();i++){
                PopFeed pp=list.get(i);
                if(pp.isClick()){
                    Log.v("check_dr_names",pp.getTxt());
                    s+=pp.getTxt()+"$"+pp.getCode()+"#";
                    if(selectCategory.equalsIgnoreCase("c"))
                        clusterBased+=pp.getCode()+",";
                    if(selectCategory.equalsIgnoreCase("j")||selectCategory.equalsIgnoreCase("d")
                            ||selectCategory.equalsIgnoreCase("ch")){
                        checkField= String.valueOf(count);
                        count++;
                    }
                    else
                        checkField=pp.getTxt();
                }
            }
            if(selectCategory.equalsIgnoreCase("w"))
            {
                list_cluster.add(new ModelTpSave(false));
                if(!checkField.equalsIgnoreCase("Field Work")){

                    gridselection.selectionList(checkField);
                    gridselection.unselectionList("dd");
                }
                else
                    gridselection.selectionList(checkField);
            }
            else
                gridselection.selectionList(checkField);
Log.v("showing_the_count",checkField+" categ "+selectCategory);
            switch (selectCategory){
                case "w":
                    work=s;
                    break;
                case "h":
                    hq=s;
                    if(SF_Type.equalsIgnoreCase("2")) {
                       Log.v("printing_hq_code",s.substring(s.indexOf("$") + 1, s.indexOf("#")));
                        Updatecluster(s.substring(s.indexOf("$") + 1, s.indexOf("#")));
                    }
                    break;
                case "c":
                    cluster=s;
                    int hospLen=0;
                    String  hosValue="";
                    Log.v("printing_dr_here",cluster);
                    if(hospNeed.equalsIgnoreCase("0")){
                        if(SF_Type.equalsIgnoreCase("2")) {
                            String s=aa.get(currentPos).getHq();
                            managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"ch");
                        }
                        getAllCluster(cluster);
                        Log.v("printing_show_hos", aa.get(currentPos).getHosp());
                        recurSeparatedPrdCode(aa.get(currentPos).getHosp());
                        filterCategoryExpand(clusterBased, hospLoader);
                        Log.v("printing_nameonlyhos", nameOnly.size() + "");
                        //filterCategory(clusterBased,drLoader);
                        try {
                            jsonArray = new JSONArray(filterCatValue);
                            hospLen=jsonArray.length();
                                    Log.v("hjuikolphos", jsonArray.length() + "");
                            String s="";
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject js = jsonArray.getJSONObject(j);
                                JSONArray jarr = js.getJSONArray("data");
                                for (int k = 0; k < jarr.length(); k++) {
                                    JSONObject js1 = jarr.getJSONObject(k);
                                    s+=js1.getString("name")+"$"+js1.getString("code")+"#";
                                    //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                    Log.v("printing_totla_data_hos", js1.getString("name") + " rem " + js1.getString("code"));
                                }

                            }
                            hosValue=s;
                            aa.get(currentPos).setHosp(s);
                            //twow.update(cc,0);
                        }catch (Exception e){}
                    }
                    if(drNeed.equalsIgnoreCase("0")){
                        val_drneed=false;
                        getneedfield(0);
                        sss="";
                        getDr(aa.get(currentPos).getDr(),0,0);
                        Log.v("printing_the_ss_value",sss+" currentpos "+currentPos+" em[pty "+val_drneed);
                        aa.get(currentPos).setDr(sss);
                    }
                    else{
                        if(SF_Type.equalsIgnoreCase("2")) {
                            String s=aa.get(currentPos).getHq();
                            managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"d");
                        }
                        getAllCluster(cluster);
                        Log.v("printing_show_dr", aa.get(currentPos).getDr());
                        recurSeparatedPrdCode(aa.get(currentPos).getDr());
                        filterCategoryExpand(clusterBased, drLoader);
                        Log.v("printing_nameonlybased", nameOnly.size() + "");
                        //filterCategory(clusterBased,drLoader);
                        if(hospNeed.equalsIgnoreCase("0")   && hospLen!=0){
                            getAllHosp(hosValue);
                            getDrBasedHosp(hospitalBased,filterCatValue);
                        }
                        try {
                            jsonArray = new JSONArray(filterCatValue);
                            Log.v("hjuikolp", jsonArray.length() + "");
                           String s="";
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject js = jsonArray.getJSONObject(j);
                                JSONArray jarr = js.getJSONArray("data");
                                for (int k = 0; k < jarr.length(); k++) {
                                    JSONObject js1 = jarr.getJSONObject(k);
                                    s+=js1.getString("name")+"$"+js1.getString("code")+"#";
                                    //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                    Log.v("printing_totla_data_Val", js1.getString("name") + " rem " + js1.getString("code"));
                                }

                            }
                            aa.get(currentPos).setDr(s);
                            //twow.update(cc,0);
                        }catch (Exception e){}
                    }
                    if(chmNeed.equalsIgnoreCase("0")){
                        getneedfield(1);
                        sss="";
                        getDr(aa.get(currentPos).getChem(),0,1);
                        Log.v("printing_the_ss_value",sss+" currentpos "+currentPos+" em[pty "+val_drneed);
                        aa.get(currentPos).setChem(sss);
                    }
                    else{
                        if(SF_Type.equalsIgnoreCase("2")) {
                            String s=aa.get(currentPos).getHq();
                            managerLoad(s.substring(s.indexOf("$")+1,s.indexOf("#")),"ch");
                        }
                        getAllCluster(cluster);
                        Log.v("printing_show_dr", aa.get(currentPos).getChem());
                        recurSeparatedPrdCode(aa.get(currentPos).getChem());
                        filterCategoryExpand(clusterBased, chemistLoader);
                        try {
                            jsonArray = new JSONArray(filterCatValue);
                            String s="";
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject js = jsonArray.getJSONObject(j);
                                JSONArray jarr = js.getJSONArray("data");
                                for (int k = 0; k < jarr.length(); k++) {
                                    JSONObject js1 = jarr.getJSONObject(k);
                                    s+=js1.getString("name")+"$"+js1.getString("code")+"#";
                                    //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                    Log.v("printing_totla_data_Val", js1.getString("name") + " rem " + js1.getString("code"));
                                }

                            }
                            aa.get(currentPos).setChem(s);
                        }catch (Exception e){}
                    }

                    if(jwNeed.equalsIgnoreCase("0")){
                        getneedfield(2);
                    }
                    break;
                case "j":
                    Log.v("joint_wrk_tp",s);
                    joint=s;
                    break;
                case "d":
                    dr=s;
                    Log.v("filling_dr_value",dr+" ss "+s);
                    break;
                case "ch":
                    chem=s;
                    break;
                case "hos":
                    hosp=s;
                    getAllHosp(s);
                    getDrBasedHosp(hospitalBased,drLoader);
                    try {
                        jsonArray = new JSONArray(filterCatValue);
                        Log.v("hjuikolp", jsonArray.length() + "");
                        String s="";
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject js = jsonArray.getJSONObject(j);
                            JSONArray jarr = js.getJSONArray("data");
                            for (int k = 0; k < jarr.length(); k++) {
                                JSONObject js1 = jarr.getJSONObject(k);
                                s+=js1.getString("name")+"$"+js1.getString("code")+"#";
                                //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                Log.v("printing_totla_hos_Val", js1.getString("name") + " rem " + js1.getString("code"));
                            }

                        }
                        aa.get(currentPos).setDr(s);



                        //twow.update(cc,0);
                    }catch (Exception e){}
                    getDrBasedHosp(hospitalBased,chemistLoader);
                    try {
                        jsonArray = new JSONArray(filterCatValue);
                        String s="";
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject js = jsonArray.getJSONObject(j);
                            JSONArray jarr = js.getJSONArray("data");
                            for (int k = 0; k < jarr.length(); k++) {
                                JSONObject js1 = jarr.getJSONObject(k);
                                s+=js1.getString("name")+"$"+js1.getString("code")+"#";
                                //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                Log.v("printing_totla_chm_Val", js1.getString("name") + " rem " + js1.getString("code"));
                            }

                        }
                        aa.get(currentPos).setChem(s);
                    }catch (Exception e){}


/*
                    try {
                        jsonArray = new JSONArray(filterCatValue);
                        Log.v("hjuikolp", jsonArray.length() + "");
                        String s="";
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject js = jsonArray.getJSONObject(j);
                            JSONArray jarr = js.getJSONArray("data");
                            for (int k = 0; k < jarr.length(); k++) {
                                JSONObject js1 = jarr.getJSONObject(k);
                                s+=js1.getString("name")+"$"+js1.getString("code")+"#";
                                //nameCode.add(new PopFeed(js1.getString("name"), true, js1.getString("code")));

                                Log.v("printing_totla_data_Val", js1.getString("name") + " rem " + js1.getString("code"));
                            }

                        }
                        aa.get(currentPos).setDr(s);
                        //twow.update(cc,0);
                    }catch (Exception e){}
*/
                    break;
            }
            lay_selection.setVisibility(View.GONE);
                    /*if(!TextUtils.isEmpty(aa.get(currentPos).getWrk())){
                        saveSessionValue(currentPos);
                    }*/
            Log.v("print_curren_pos",currentPos+"");
            saveCertainValue(currentPos);
        }
        else{
            switch (selectCategory){

                case "c":
                    cluster="";

                    if(drNeed.equalsIgnoreCase("0")){
                        val_drneed=false;
                        getneedfield(0);
                        sss="";
                        getDr(aa.get(currentPos).getDr(),0,currentPos);
                        Log.v("printing_the_ss_value",sss+" currentpos "+currentPos+" em[pty "+val_drneed);
                        aa.get(currentPos).setDr(sss);
                    }
                    else
                        aa.get(currentPos).setDr("");
                    if(chmNeed.equalsIgnoreCase("0")){
                        getneedfield(1);
                        sss="";
                        getDr(aa.get(currentPos).getChem(),0,1);
                        Log.v("printing_the_ss_value",sss+" currentpos "+currentPos+" em[pty "+val_drneed);
                        aa.get(currentPos).setChem(sss);
                    }
                    else
                        aa.get(currentPos).setChem("");

                    break;
                case "j":
                    Log.v("joint_wrk_tp",s);
                    joint="";
                    break;
                case "d":
                    dr="";
                    Log.v("filling_dr_value",dr+" ss "+s);
                    break;
                case "ch":
                    chem="";
                    break;
                case "hos":
                    hosp="";
                    break;
            }

            lay_selection.setVisibility(View.GONE);
                    /*if(!TextUtils.isEmpty(aa.get(currentPos).getWrk())){
                        saveSessionValue(currentPos);
                    }*/
                    if(TextUtils.isEmpty(s)) {
                        Log.v("print_curren_pos", currentPos + "");
                        saveCertainValue(currentPos);
                    }

        }
            Log.v("showing_ttal_exp","nt avail");

    }


    public String getDr(String s,int x,int posv){


        Log.v("clusterbase",clusterBased);
        String[] sp=clusterBased.split(",");
        ArrayList<String> code=new ArrayList<>(Arrays.asList(sp));
        if(TextUtils.isEmpty(s))
            return "";
        int pos=s.indexOf("$");
        int pos1=s.indexOf("#");
        dbh.open();
        Cursor cu;
        if(posv==0)
         cu=dbh.getDrCluster(s.substring(pos+1,pos1));
        else
         cu=dbh.getChmCluster(s.substring(pos+1,pos1));
        while(cu.moveToNext()){
            String clustercode="";
            if(posv==0)
                clustercode=cu.getString(5);
            else
                clustercode=cu.getString(4);

            Log.v("database_helper",clustercode+"get_code_plus"+code.toString());
            if(code.contains(clustercode)){
                Log.v("database_helper123",clustercode+"get_code_plus"+code.toString());
                sss+=s.substring(0,pos1+1);
                //array.get(posv).setDr(sss);
            }
            else{
               // array.get(posv).setDr(sss);
                //Log.v("list_not_in_",sss+" s_value "+s);
                //notifyDataSetChanged();
            }

        }
        if(pos1==s.length()-1)
            return "";
        else {
            s = s.substring(pos1+1);
            return getDr(s,++x,posv);
        }
    }


    @Override
    public void onBackPressed() {
       /* Intent i=new Intent(DemoActivity.this,HomeDashBoard.class);
        startActivity(i);*/
    }

    public void getTpsetup(){
        JSONObject jj = null;
        try{
             jj=new JSONObject();
            jj.put("SF",SF_Code);
            div=div.replace(",","");
            jj.put("div",div);
            Call<ResponseBody> drDetail = apiService.getTpSetup(jj.toString());
            drDetail.enqueue(new Callback<ResponseBody>() {
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
                            Log.v("demo_getsetup", is.toString());
                            mCommonSharedPreference.setValueToPreference("tpsetup",is.toString());
                            assignTheSetupValue(is.toString());

                        } catch (Exception e) {
                            Log.v("addsessioncoutexc",e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch (Exception e){}

    }

    public void assignTheSetupValue(String is){
        try{
            JSONArray jarray=new JSONArray(is.toString());
            JSONObject json=jarray.getJSONObject(0);
            if(json.getString("AddsessionNeed").equalsIgnoreCase("1")){
                // add_session.setVisibility(View.INVISIBLE);
                addsessionNeed=json.getString("AddsessionNeed");
            }
            else{
                addsessionNeed=json.getString("AddsessionNeed");
                //add_session.setVisibility(View.VISIBLE);
                Log.v("addsessioncout",json.getString("AddsessionCount"));

                int count= Integer.parseInt(json.getString("AddsessionCount"));
                addSessionCount= String.valueOf(count-1);
                addSession(count);
                               /* btnAry=new Button[count];
                                for(int k=0;k<count;k++){
                                    Button btnTag = new Button(DemoActivity.this);
                                    btnTag.setTag(k);
                                    btnTag.setBackgroundResource(R.drawable.submit_circle_bg);
                                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(5, 5);
                                    params.rightMargin=3;
                                    btnTag.setVisibility(View.GONE);
                                    btnTag.setLayoutParams(params);
                                    lin_lay_btn.addView(btnTag);
                                    btnAry[k]=btnTag;
                                }*/

            }
            clusterneed=json.getString("ClusterNeed");
            drNeed=json.getString("DrNeed");
            if(json.has("hospitalNeed"))
                hospNeed=json.getString("hospitalNeed");
            else
                hospNeed="1";
            drNeed=json.getString("DrNeed");
            chmNeed=json.getString("ChmNeed");
            jwNeed=json.getString("JWNeed");
            cltyp=json.getString("clustertype");
            Log.v("here_demo_cluster",json.getString("ClusterNeed"));

        }catch (Exception e){}
    }
    public void getneedfield(int x){

      //  getAllCluster(aa.get(currentPos).getCluster());
       // Log.v("printing_show_dr",aa.get(currentPos).getDr());
        plannedPos=currentPos;
        String fieldValue;
        String loadervalue;
        if(x==0){
            fieldValue=aa.get(currentPos).getDr();
            loadervalue=drLoader;
        }
        else if(x==1){
            fieldValue=aa.get(currentPos).getChem();
            loadervalue=chemistLoader;
        }
        else{
            fieldValue=aa.get(currentPos).getJoint();
            loadervalue=jointLoader;
        }
        recurSeparatedPrdCode(fieldValue);
        filterCategoryExpand(clusterBased,loadervalue);
        try {
            JSONArray jjarray=new JSONArray(filterCatValue);
            for(int j=0;j<jjarray.length();j++){
                JSONObject js=jjarray.getJSONObject(j);
                JSONArray jarr=js.getJSONArray("data");
                if(jarr.length()!=0){
                    if(x==0)
                    val_drneed=true;
                    else if(x==1)
                        val_chneed=true;
                    else
                        val_jwneed=true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String splitMultipleValues(String value){
        String[] val=value.split("#");
        String clustername="",clustercode="";

            for (int k = 0; k < val.length; k++) {
                String cluster = val[k];
                clustername += cluster.substring(0, cluster.indexOf("$")) + ",";
                clustercode += cluster.substring(cluster.indexOf("$") + 1) + ",";
            }

        return clustername+"$"+clustercode;
    }

    public String getNames(String s){

        return s.substring(0,s.indexOf("$"));
    }

    public String getCode(String s){

            return s.substring(s.indexOf("$")+1);
    }

    public boolean checkForEmptyString(String s){
        if(TextUtils.isEmpty(s))
            return false;
        else return true;
    }

    public void sendSingleData(String dayno){
        try {
            JSONObject justobj=new JSONObject();
            JSONArray jsA=jsonObject1.getJSONArray("TPDatas");
            Log.v("printing_tp_val",jsA.toString());

            for(int i=0;i<jsA.length();i++){
                JSONObject jjson=jsA.getJSONObject(i);
                if(!TextUtils.isEmpty(jjson.getString("dayno"))) {
                    if (jjson.getString("dayno").equalsIgnoreCase(dayno)) {
                        if (jjson.has("DayPlan1")) {
                            JSONArray jsonDay = new JSONArray();
                            JSONArray jj = jjson.getJSONArray("DayPlan1");
                            for (int j = 0; j < jj.length(); j++) {
                                JSONObject js = jj.getJSONObject(j);
                                JSONObject newjs = new JSONObject();
                                String clustername = "";
                                String clustercode = "";
                                if (checkForEmptyString(js.getString("work"))) {
                                    String val_cluster = splitMultipleValues(js.getString("work"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                }
                                newjs.put("WTNm", clustername);
                                newjs.put("WTCd", clustercode.substring(0,clustercode.indexOf(",")));
                                clustername = "";
                                clustercode = "";
                                if (checkForEmptyString(js.getString("hq"))) {
                                    String val_cluster = splitMultipleValues(js.getString("hq"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                }
                                newjs.put("HQNm", clustername);
                                newjs.put("HQCd", clustercode);
                                clustername = "";
                                clustercode = "";
                                if (checkForEmptyString(js.getString("cluster"))) {
                                    String val_cluster = splitMultipleValues(js.getString("cluster"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                }
                                newjs.put("TerrNm", clustername);
                                newjs.put("TerrCd", clustercode);
                                clustername = "";
                                clustercode = "";
                                if (checkForEmptyString(js.getString("dr"))) {
                                    String val_cluster = splitMultipleValues(js.getString("dr"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                }
                                newjs.put("DRNm", clustername);
                                newjs.put("DRCd", clustercode);
                                clustername = "";
                                clustercode = "";
                                if (checkForEmptyString(js.getString("joint"))) {
                                    String val_cluster = splitMultipleValues(js.getString("joint"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                }
                                newjs.put("JWNm", clustername);
                                newjs.put("JWCd", clustercode);
                                clustername = "";
                                clustercode = "";
                                if (checkForEmptyString(js.getString("chem"))) {
                                    String val_cluster = splitMultipleValues(js.getString("chem"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                }
                                newjs.put("CHNm", clustername);
                                newjs.put("CHCd", clustercode);
                                clustername = "";
                                clustercode = "";
                                if (checkForEmptyString(js.getString("hos"))) {
                                    String val_cluster = splitMultipleValues(js.getString("hos"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                    Log.v("printing_total_hos",separateCodeandName(clustername).length+"    code"+separateCodeandName(clustercode).length);
                                }
                                newjs.put("hosNm", clustername);
                                newjs.put("hosCd", clustercode);
                                clustername = "";
                                clustercode = "";

                                    newjs.put("DayRmk", js.getString("remrk"));

                                jsonDay.put(newjs);

                                clustername = "";
                                clustercode = "";
                                Log.v("value_string_of", clustername + " cluster_code " + clustercode);
                            }

                            justobj.put("dayno",dayno);
                            justobj.put("DayPlan", jsonDay);
                            justobj.put("TPDt", jjson.getString("TPDt"));
                            JSONArray justArray=new JSONArray();
                            justArray.put(justobj);
                            JSONObject jssobj=new JSONObject();
                            jssobj.put("TPDatas",justArray);
                            jssobj.put("TPYear", jsonObject1.getString("TPYear"));
                            jssobj.put("TPMonth", jsonObject1.getString("TPMonth"));
                            jssobj.put("SF",jsonObject1.getString("SF"));
                            jssobj.put("SFName",jsonObject1.getString("SFName"));
                            jssobj.put("DivCode",div);

                            Log.v("printined_single_save",jssobj.toString());
                            saveSingleDay(jssobj.toString(),0);

                        }
                        i=jsA.length();
                    }
                }
            }
            Log.v("printing_dayplan",jsA.toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void saveSingleDay(String json, final int x){
        Call<ResponseBody> drDetail;
        if(x==0)
        drDetail = apiService.svDayTp(json);
        else if(x==1)
            drDetail = apiService.svNewTp(json);
        else
            drDetail = apiService.svTpApproval(json);

        drDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    notifi.setText("");
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("day_save", is.toString());
                        JSONObject js=new JSONObject(is.toString());
                        if(js.getString("success").equalsIgnoreCase("true")) {
                            marker_progress.setVisibility(View.GONE);

                            if(x==1) {
                                showToast("Successfully sent !!");
                                btn_send.setVisibility(View.INVISIBLE);
                                btn_save.setVisibility(View.INVISIBLE);
                            }
                            if(x==2){
                                showToast("Successfully sent !!");
                                Intent i=new Intent(DemoActivity.this,ApprovalActivity.class);
                                startActivity(i);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void rejectApproval(String msg){
        try{
            JSONObject jjs=new JSONObject(mCommonSharedPreference.getValueFromPreference("approve"));
            JSONObject jj=new JSONObject();
            jj.put("SF",jjs.getString("SF"));
            jj.put("TPMonth",jjs.getString("Month"));
            jj.put("TPYear",jjs.getString("Year"));
            jj.put("reason",msg);
            Log.v("printing_reject",jj.toString());
            Call<ResponseBody> reject=apiService.svTpReject(jj.toString());
            reject.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.v("printing_reject11",response.isSuccessful()+"");
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
                            Log.v("day_reject", is.toString());
                            JSONObject js = new JSONObject(is.toString());
                            if(js.getString("success").equalsIgnoreCase("true")) {
                                showToast("Tour plan rejected !!");
                                Intent i=new Intent(DemoActivity.this,ApprovalActivity.class);
                                startActivity(i);
                            }
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
    public String[] separateCodeandName(String value){
        String[] val=value.split(",");
        return val;
    }

    public String attachCodeandName(String[] code,String[] name){
        String s="";
        for(int i=0;i<code.length;i++){
            s=s+name[i]+"$"+code[i]+"#";
        }
        return s;
    }

    public void retrieveOldDatas(String json, final String month){
        Log.v("calendar_date_",json+"   "+jsonObject1.toString());
        Call<ResponseBody> drDetail= apiService.getTP(json);
        drDetail.enqueue(new Callback<ResponseBody>() {
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
                        alertshow=false;
                        Log.v("getting_save", is.toString());
                        JSONArray jsonArray=new JSONArray(is.toString());
                        if(jsonArray.length()==0) {
                            marker_progress.setVisibility(View.GONE);
                           // dbh.deleteTP(month);
                            //dayCal();
                            status = "0";
                            reject="";
                            btn_send.setVisibility(View.VISIBLE);
                            grid_cal.setAlpha(1f);
                            grid_cal.setEnabled(true);
                        }
                        else {

                            JSONObject jjobj = jsonArray.getJSONObject(0);
                            status = jjobj.getString("status");
                            reject = jjobj.getString("reject");
                            grid_cal.setAlpha(1f);
                            grid_cal.setEnabled(true);
                           /* if(!status.equalsIgnoreCase("3")) {
                                if(status.equalsIgnoreCase("2"))
                                    showToast("Tp Rejected");*/
                                JSONArray jjary = jjobj.getJSONArray("TPDatas");
                                for (int i = 0; i < jjary.length(); i++) {
                                    JSONArray saveDayjson = new JSONArray();
                                    JSONObject saveDayjsonobj = new JSONObject();
                                    JSONObject json = jjary.getJSONObject(i);
                                    String dayno = json.getString("dayno");
                                    JSONArray day = json.getJSONArray("DayPlan");
                                    for (int k = 0; k < day.length(); k++) {
                                        saveDayjsonobj = new JSONObject();
                                        JSONObject jj = day.getJSONObject(k);
                                        String[] codearray;
                                        String[] namearray;
                                        String code;
                                        String name;
                                        String finalValue = "";

                                        code = jj.getString("WTCode") + ",";
                                        name = jj.getString("WTName");
                                        Log.v("inside_list11", i + "");
                                        if (!TextUtils.isEmpty(name)) {
                                            Log.v("inside_list22", i + "" + dayno);
                                            codearray = separateCodeandName(code);
                                            namearray = separateCodeandName(name);
                                            finalValue = attachCodeandName(codearray, namearray);
                                            saveDayjsonobj.put("work", finalValue);

                                            codearray = null;
                                            namearray = null;
                                            finalValue = "";

                                            code = jj.getString("ClusterCode");
                                            name = jj.getString("ClusterName");

                                            if (!TextUtils.isEmpty(code)) {
                                                codearray = separateCodeandName(code);
                                                namearray = separateCodeandName(name);
                                                finalValue = attachCodeandName(codearray, namearray);
                                                Log.v("printing_finalval", finalValue);
                                            }
                                            saveDayjsonobj.put("cluster", finalValue);

                                            codearray = null;
                                            namearray = null;
                                            finalValue = "";

                                            code = jj.getString("JWCodes");
                                            name = jj.getString("JWNames");

                                            if (!TextUtils.isEmpty(code)) {
                                                codearray = separateCodeandName(code);
                                                namearray = separateCodeandName(name);
                                                finalValue = attachCodeandName(codearray, namearray);
                                                Log.v("printing_finalval", finalValue);
                                            }
                                            saveDayjsonobj.put("joint", finalValue);

                                            codearray = null;
                                            namearray = null;
                                            finalValue = "";

                                            if(drNeed.equalsIgnoreCase("0")) {
                                                code = jj.getString("Dr_Code");
                                                name = jj.getString("Dr_Name");

                                                if (!TextUtils.isEmpty(code)) {
                                                    codearray = separateCodeandName(code);
                                                    namearray = separateCodeandName(name);
                                                    Log.v("printing_finalnam", codearray.length+"   name"+namearray.length);
                                                    finalValue = attachCodeandName(codearray, namearray);
                                                    Log.v("printing_finalval", finalValue);
                                                }
                                                saveDayjsonobj.put("dr", finalValue);
                                            }
                                            else
                                                saveDayjsonobj.put("dr", "");

                                           /* if (dayno.equalsIgnoreCase("14")) {
                                                Log.v("printing_approval_", jj.getString("Dr_Code") + " finalval " + finalValue);
                                            }*/

                                            codearray = null;
                                            namearray = null;
                                            finalValue = "";
                                            if(chmNeed.equalsIgnoreCase("0")) {
                                                code = jj.getString("Chem_Code");
                                                name = jj.getString("Chem_Name");

                                                if (!TextUtils.isEmpty(code)) {
                                                    codearray = separateCodeandName(code);
                                                    namearray = separateCodeandName(name);
                                                    finalValue = attachCodeandName(codearray, namearray);
                                                    Log.v("printing_finalval", finalValue);
                                                }
                                                saveDayjsonobj.put("chem", finalValue);
                                            }
                                            else
                                                saveDayjsonobj.put("chem", "");
                                            codearray = null;
                                            namearray = null;
                                            finalValue = "";

                                            code = jj.getString("HQCodes");
                                            name = jj.getString("HQNames");

                                            if (!TextUtils.isEmpty(code)) {
                                                codearray = separateCodeandName(code);
                                                namearray = separateCodeandName(name);
                                                finalValue = attachCodeandName(codearray, namearray);
                                                Log.v("printing_finalval", finalValue);
                                            }
                                            saveDayjsonobj.put("hq", finalValue);

                                            codearray = null;
                                            namearray = null;
                                            finalValue = "";

                                            if(jj.has("HOSCode")) {
                                                code = jj.getString("HOSCode");
                                                name = jj.getString("HOSName");

                                                if (!TextUtils.isEmpty(code)) {
                                                    codearray = separateCodeandName(code);
                                                    namearray = separateCodeandName(name);
                                                    Log.v("printing_finalfull", name);
                                                    Log.v("printing_finalnam_hos", codearray.length + "   name" + namearray.length);
                                                    finalValue = attachCodeandName(codearray, namearray);
                                                    Log.v("printing_finalval", finalValue);
                                                }

                                            saveDayjsonobj.put("hos", finalValue);
                                            }
                                            /*else
                                                saveDayjsonobj.put("hos", finalValue);*/
                                            saveDayjsonobj.put("remrk", jj.getString("DayRemarks"));

                                            // Log.v("printing_total_js",saveDayjsonobj.toString());
                                            saveDayjson.put(saveDayjsonobj);
                                            Log.v("printing_total_js", saveDayjson.toString());
                                            Log.v("inside_list33", i + "");
                                            setRetrieveValue(dayno, saveDayjson, jjary.length() - 1, i, month,jjary);
                                        }
                                    }


                                }
                           /* }
                            else {
                                Log.v("printing_tp_status",jsonObject1.toString());
                                dbh.updateTPStatus(month,status);
                                grid_cal.setAlpha(0.5f);
                                grid_cal.setEnabled(false);
                                showToast("Already Approved");
                            }*/


                        }
                    } catch (Exception e) {
                        Log.v("Tour_exception_test",e.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void setRetrieveValue(String dayno,JSONArray dayplan,int total,int current,String month,JSONArray jsAa){
        try {
            JSONArray jsA = jsonObject1.getJSONArray("TPDatas");
            Log.v("thiru",jsA.toString());
            JSONArray FillArray = new JSONArray();
            for (int i = 0; i < jsA.length(); i++) {
                JSONObject js = jsA.getJSONObject(i);
                if (!TextUtils.isEmpty(js.getString("dayno"))) {
                    if (dayno.equalsIgnoreCase(js.getString("dayno"))) {
                        js.put("DayPlan1", dayplan);

                    }
                }
                FillArray.put(js);

            }
            Log.v("thirummmmmm",FillArray.toString());
            JSONObject jj=new JSONObject();
            jj.put("TPDatas",FillArray);
            jsonObject1.put("TPDatas",FillArray);
            Log.v("calendar_date_tp",jsonObject1.toString()+" total ");
            Log.v("total_calendar ",total+" current "+current+status);
           // if(total==current){
                //Log.v("showing_calendare",currentMn+" status "+status);
                if(!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")){
                    jj.put("SF",mCommonSharedPreference.getValueFromPreference("subsf"));
                    jj.put("SFName",mCommonSharedPreference.getValueFromPreference("subsfname"));
                    jj.put("DivCode",div);
                    jj.put("TPMonth",fullDateDum.substring(5,7));
                    jj.put("TPYear",fullDateDum.substring(0,4));
                    Log.v("clearCalendar",jj.toString());
                    jsonObject1=jj;
                    calAdapt = new CalendarAdapter(DemoActivity.this, jj.toString());
                    grid_cal.setAdapter(calAdapt);
                }
                else {
                    jj.put("SF",SF_Code);
                    jj.put("SFName",sfname);
                    jj.put("DivCode",div);
                    jj.put("TPMonth",fullDate.substring(5,7));
                    jj.put("TPYear",fullDate.substring(0,4));
                    Log.v("clearCalendar_non",month+" json "+status+jj.toString());
                    Log.v("clearVASAN_non",jj.toString());

                    if(!dbh.checkOpen())
                        dbh.open();
                    dbh.updateTP(month, jj.toString(), status);
                    calAdapt = new CalendarAdapter(DemoActivity.this, jj.toString());
                    grid_cal.setAdapter(calAdapt);
                    if(current==0 || current==total)
                    enableDisableSubmitButton();
                }
            //enableDisableSubmitButton();
           // }
            marker_progress.setVisibility(View.GONE);
            /*jj.put("SF",SF_Code);
            jj.put("SFName",sfname);
            jj.put("DivCode",div);
            jj.put("TPMonth",fullDate.substring(5,7));
            jj.put("TPYear",fullDate.substring(0,4));
            Log.v("clearCalendar",jj.toString());
            dbh.updateTP(currentMn,jj.toString());
            calAdapt = new CalendarAdapter(DemoActivity.this,jj.toString());
            grid_cal.setAdapter(calAdapt);*/
        }catch (Exception e){

            Log.e("exceptional error",e.getMessage());
        }
    }

    public void enableDisableSubmitButton(){
        boolean check=true;
        Log.v("printing_Statuis33",status);
        grid_cal.setAlpha(1f);
        grid_cal.setEnabled(true);
        if(!mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")){
            loadData(mCommonSharedPreference.getValueFromPreference("subsf"));
        }
        if(status.equalsIgnoreCase("3")){
           /* grid_cal.setAlpha(0.5f);
            grid_cal.setEnabled(false);*/
            notifi.setText("");
            showToast("Already Approved");
            btn_edit.setText("View");
            btn_save.setVisibility(View.INVISIBLE);
            btn_send.setVisibility(View.INVISIBLE);
        }
        else if(status.equalsIgnoreCase("2")) {
            Log.v("printing_reject",reject+"kkk");
            btn_edit.setText("Edit");
            if(!TextUtils.isEmpty(reject)) {
               /* if(!alertshow)
                alertForReject(1);*/
               notifi.setText("Reason for rejected: "+reject);

            }
            btn_send.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            btn_send.getBackground().setAlpha(255);
            btn_send.setEnabled(true);
            showToast("Tour plan got Rejected");

        }
        else if(status.equalsIgnoreCase("0")) {
            btn_send.setVisibility(View.VISIBLE);
            btn_edit.setText("Edit");
            notifi.setText("");
            Log.v("startcount_are",CalendarAdapter.arr_json+"");

            try{
            JSONArray jsA = jsonObject1.getJSONArray("TPDatas");
            Log.v("printing_tp_val", jsA.toString());

            for (int i = 0; i < jsA.length(); i++) {
                JSONObject jjson = jsA.getJSONObject(i);
                if (!TextUtils.isEmpty(jjson.getString("dayno"))) {
                    if(jjson.has("DayPlan1")){
                        Log.v("printing_day_total","here"+jjson.getString("dayno"));
                    }
                    else {
                        Log.v("printing_day_total9","here"+jjson.getString("dayno"));
                        check = false;
                        i = CalendarAdapter.arr_json.size();
                        break;
                    }


                }
            }
            }catch(Exception e){}
/*
            for (int i = 0; i < CalendarAdapter.arr_json.size(); i++) {

                Log.v("startcount_are",startCount+""+CalendarAdapter.arr_json.get(i).getTxt()+"");
                if (i >= startCount) {
                    if (TextUtils.isEmpty(CalendarAdapter.arr_json.get(i).getCode())) {
                        Log.v("check_value",check+""+i);

                        */
/* *//*

                    }
                }
            }
*/
            btn_save.setVisibility(View.VISIBLE);

            Log.v("check_value",check+"");
            if (check) {
                btn_send.setVisibility(View.VISIBLE);
                btn_send.setEnabled(true);
                btn_send.getBackground().setAlpha(255);
            } else {
                btn_send.setVisibility(View.VISIBLE);
                btn_send.setEnabled(false);
                btn_send.getBackground().setAlpha(45);
            }
        }
        else {
            btn_send.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.INVISIBLE);
            notifi.setText("");
        }

    }

    public void callOldDatas(String mnth){
        try{
            JSONObject jso2=new JSONObject();
            jso2.put("SF",SF_Code);
            jso2.put("Month",currentMnthNum);
            jso2.put("Year",currentyr);
            Log.v("retrieve_old",jso2.toString());
            marker_progress.setVisibility(View.VISIBLE);
            Log.v("retrievemnth",mnth);
            retrieveOldDatas(jso2.toString(),mnth);
        }catch (Exception e){}
    }

    public void alertOnline(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Are you sure, You wanted send offline data");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
//                                String syncMonth=month.substring(0,4);
//                               Log.v("checkedValmnth",syncMonth);
//                               dbh.deleteTP(syncMonth);
//                              getInsertValue();
//                               loadData(SF_Code);
 //                              callOldDatas(syncMonth);

                                if(status.equalsIgnoreCase("0"))
                                sendingOfflineTpData(0);

                                else
                                showToast("Already sent to Approval");
                                commonFun();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                commonFun();
            }
        });

         alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void sendingOfflineTpData(int x){
        marker_progress.setVisibility(View.VISIBLE);
        try {
            JSONArray jsA = jsonObject1.getJSONArray("TPDatas");
            Log.v("printing_tp_val", jsA.toString());

            for (int i = 0; i < jsA.length(); i++) {
                JSONObject jjson = jsA.getJSONObject(i);
                Log.v("printing_tp_val1", jjson.toString());
                if (!TextUtils.isEmpty(jjson.getString("dayno"))) {

                    if (jjson.has("DayPlan1")) {
                        JSONArray jsonDay = new JSONArray();
                        JSONArray jj = jjson.getJSONArray("DayPlan1");
                        for (int j = 0; j < jj.length(); j++) {
                            JSONObject js = jj.getJSONObject(j);
                            JSONObject newjs = new JSONObject();
                            String clustername = "";
                            String clustercode = "";
                            if (checkForEmptyString(js.getString("work"))) {
                                String val_cluster = splitMultipleValues(js.getString("work"));
                                clustername = getNames(val_cluster);
                                clustercode = getCode(val_cluster);
                            }
                            newjs.put("WTNm", clustername);
                            newjs.put("WTCd", clustercode.substring(0, clustercode.indexOf(",")));
                            clustername = "";
                            clustercode = "";
                            if (checkForEmptyString(js.getString("hq"))) {
                                String val_cluster = splitMultipleValues(js.getString("hq"));
                                clustername = getNames(val_cluster);
                                clustercode = getCode(val_cluster);
                            }
                            newjs.put("HQNm", clustername);
                            newjs.put("HQCd", clustercode);
                            clustername = "";
                            clustercode = "";
                            if (checkForEmptyString(js.getString("cluster"))) {
                                String val_cluster = splitMultipleValues(js.getString("cluster"));
                                clustername = getNames(val_cluster);
                                clustercode = getCode(val_cluster);
                            }
                            newjs.put("TerrNm", clustername);
                            newjs.put("TerrCd", clustercode);
                            clustername = "";
                            clustercode = "";
                            if (checkForEmptyString(js.getString("dr"))) {
                                String val_cluster = splitMultipleValues(js.getString("dr"));
                                clustername = getNames(val_cluster);
                                clustercode = getCode(val_cluster);
                            }
                            newjs.put("DRNm", clustername);
                            newjs.put("DRCd", clustercode);
                            clustername = "";
                            clustercode = "";
                            if (checkForEmptyString(js.getString("joint"))) {
                                String val_cluster = splitMultipleValues(js.getString("joint"));
                                clustername = getNames(val_cluster);
                                clustercode = getCode(val_cluster);
                            }
                            newjs.put("JWNm", clustername);
                            newjs.put("JWCd", clustercode);
                            clustername = "";
                            clustercode = "";
                            if (checkForEmptyString(js.getString("chem"))) {
                                String val_cluster = splitMultipleValues(js.getString("chem"));
                                clustername = getNames(val_cluster);
                                clustercode = getCode(val_cluster);
                            }
                            newjs.put("CHNm", clustername);
                            newjs.put("CHCd", clustercode);
                            clustername = "";
                            clustercode = "";
                            if(js.has("hos")) {
                                if (checkForEmptyString(js.getString("hos"))) {
                                    String val_cluster = splitMultipleValues(js.getString("hos"));
                                    clustername = getNames(val_cluster);
                                    clustercode = getCode(val_cluster);
                                }
                                newjs.put("hosNm", clustername);
                                newjs.put("hosCd", clustercode);
                            }
                            clustername = "";
                            clustercode = "";
                            if (checkForEmptyString(js.getString("remrk"))) {
                                newjs.put("DayRmk", js.getString("remrk"));
                            }
                            jsonDay.put(newjs);

                            clustername = "";
                            clustercode = "";
                            Log.v("value_string_of", clustername + " cluster_code " + clustercode);
                        }
                        jjson.put("DayPlan", jsonDay);
                    }
                    if(x==0)
                        saveSingleDay(jsonObject1.toString(), 0);
                }
            }
            Log.v("printing_dayplan", jsonObject1.toString());
            //Log.v("printing_dayplan12", jsonObject1.getString("DivCode"));

            if(x!=0)
            saveSingleDay(jsonObject1.toString(), x);
                   /* dbh.open();
                    dbh.update*/

                   /* JSONArray jsAa=jsonObject1.getJSONArray("TPDatas");
                    Log.v("printing_tp_val",jsAa.toString());

                    for(int i=0;i<jsAa.length();i++) {
                        JSONObject jjson = jsAa.getJSONObject(i);
                        Log.v("printing_dayplan_here",jjson.toString());
                    }
*/
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("printing_execption",e.getMessage());
        }

    }
    public void addSession(int count){
        try {
            Log.v("count_value", "here" + count);
            btnAry = new Button[count];
            for (int k = 0; k < count; k++) {
                Button btnTag = new Button(DemoActivity.this);
                btnTag.setTag(k);
                btnTag.setBackgroundResource(R.drawable.submit_circle_bg);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
                params.rightMargin = 3;
                btnTag.setVisibility(View.GONE);
                btnTag.setLayoutParams(params);
                lin_lay_btn.addView(btnTag);
                btnAry[k] = btnTag;
            }
        }catch (Exception e){}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCommonSharedPreference.setValueToPreference("approve","null");
    }

    public void managerLoad(String sf,String x){
        dbh.open();
        Cursor mCursor=null;
        JSONArray jsonArrayData=new JSONArray();
        switch (x){
            case "w":
                 mCursor = dbh.select_TPworktypeList(sf);

                if (mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {

                        JSONObject jsonObjectData=new JSONObject();
                        try {
                            jsonObjectData.put("name",mCursor.getString(2));
                            jsonObjectData.put("code",mCursor.getString(1));
                            jsonArrayData.put(jsonObjectData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                wrkLoader=jsonArrayData.toString();
                jsonArrayData=new JSONArray();
                dbh.close();
                break;
            case "c":
                mCursor = dbh.select_ClusterList(sf);
                // mClusterListID.clear();
                if (mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        //mClusterListID.put(mCursor.getString(1), mCursor.getString(2));
               /* if(clN!=null && clN.contains(mCursor.getString(2))){
                    clusterNm.add(new PopFeed(mCursor.getString(2),true));
                }
                else{
                    clusterNm.add(new PopFeed(mCursor.getString(2),false));}
                clusterCode.add(mCursor.getString(1));*/
                        JSONObject jsonObjectData=new JSONObject();
                        try {
                            jsonObjectData.put("name",mCursor.getString(2));
                            jsonObjectData.put("code",mCursor.getString(1));
                            jsonArrayData.put(jsonObjectData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                clusterLoader=jsonArrayData.toString();
                jsonArrayData=new JSONArray();
                dbh.close();
                break;

            case "d":
                mCursor = dbh.select_dr_sfcode(sf);
                if (mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        JSONObject jsonObjectData=new JSONObject();
                        try {
                            jsonObjectData.put("name",mCursor.getString(2));
                            jsonObjectData.put("code",mCursor.getString(1));
                            jsonObjectData.put("clcode",mCursor.getString(5));
                            jsonObjectData.put("hcode",mCursor.getString(25));
                            jsonArrayData.put(jsonObjectData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                drLoader=jsonArrayData.toString();
                jsonArrayData=new JSONArray();
                dbh.close();
                break;

            case "ch":
                mCursor = dbh.select_chem_sfcode(sf);
                if (mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        JSONObject jsonObjectData=new JSONObject();
                        try {
                            jsonObjectData.put("name",mCursor.getString(2));
                            jsonObjectData.put("code",mCursor.getString(1));
                            jsonObjectData.put("clcode",mCursor.getString(4));
                            jsonObjectData.put("hcode",mCursor.getString(13));
                            jsonArrayData.put(jsonObjectData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                chemistLoader=jsonArrayData.toString();
                jsonArrayData=new JSONArray();
                dbh.close();
                break;

            case "j":
                mCursor = dbh.select_joint_list();
                if (mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        if(!SF_Code.equalsIgnoreCase(mCursor.getString(0))) {
                            JSONObject jsonObjectData = new JSONObject();
                            try {
                                jsonObjectData.put("name", mCursor.getString(0));
                                jsonObjectData.put("code", mCursor.getString(1));
                                jsonArrayData.put(jsonObjectData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                jointLoader=jsonArrayData.toString();
                jsonArrayData=new JSONArray();
                dbh.close();
                break;

            case "h":
                mCursor = dbh.select_hospitalist(sf);
                if (mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        if(!SF_Code.equalsIgnoreCase(mCursor.getString(0))) {
                            JSONObject jsonObjectData = new JSONObject();
                            try {
                                jsonObjectData.put("name", mCursor.getString(3));
                                jsonObjectData.put("code", mCursor.getString(4));
                                jsonArrayData.put(jsonObjectData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                hospLoader=jsonArrayData.toString();
                jsonArrayData=new JSONArray();
                dbh.close();
                break;

        }
    }

    public void Updatecluster(String subsfcode){
        dwnloadMasterData = new DownloadMasters(getApplicationContext(), db_connPath, db_slidedwnloadPath, subsfcode,SF_Code);
        //MasterList=true;
        if(!CommonUtilsMethods.checkForSFcodeBasedValue(subsfcode,1))
        dwnloadMasterData.drList();
        if(!CommonUtilsMethods.checkForSFcodeBasedValue(subsfcode,2))
        dwnloadMasterData.chemsList();
        if(!CommonUtilsMethods.checkForSFcodeBasedValue(subsfcode,0))
        dwnloadMasterData.terrList();
        if(!CommonUtilsMethods.checkForSFcodeBasedValue(subsfcode,3))
        dwnloadMasterData.jointwrkCall();
        if(!CommonUtilsMethods.checkForSFcodeBasedValue(subsfcode,4))
        dwnloadMasterData.wrkkList();
        if(!CommonUtilsMethods.checkForSFcodeBasedValue(subsfcode,5))
        dwnloadMasterData.HosList();


    }


    public void alertForReject(int x){
        final Dialog dialog=new Dialog(DemoActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.row_item_reject_reason);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView txt_title=(TextView)dialog.findViewById(R.id.title);
        Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);
        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);
        RelativeLayout lay_edt=(RelativeLayout)dialog.findViewById(R.id.lay_edt);

        final EditText edt_feed=(EditText)dialog.findViewById(R.id.edt_feed);
        lay_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_feed);
            }
        });
       /* if(x==1) {
            txt_title.setText("Rejection Reason");
            btn_ok.setVisibility(View.GONE);
            btn_cancel.setText("Ok");
            edt_feed.setEnabled(false);
            edt_feed.setText(reject);
            alertshow=true;
            commonFun();
        }*/

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(edt_feed.getText().toString())){
                    rejectApproval(edt_feed.getText().toString());
                }
                else
                    showToast("Message field is empty");

                commonFun();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                alertshow=false;
                commonFun();
            }
        });
    }

    public void commonFun(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public static void bindlistner(TwoTypeparameter ll){
        twow=ll;
    }

    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view ,
                InputMethodManager.SHOW_IMPLICIT);

    }


}
