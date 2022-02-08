package saneforce.sanclm.Sales_Report.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
//import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.R;
import saneforce.sanclm.SFE_report.Activity.SFe_Activity;
import saneforce.sanclm.Sales_Report.anim.ProgressBarAnimation;
import saneforce.sanclm.activities.DashActivity;
import saneforce.sanclm.activities.DayReportsActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.ReportActivity;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_MVD_Report_DTDdata;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_MVD_Report_MTDdata;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_MVD_Report_QTDdata;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_MVD_Report_YTDdata;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_primary_sales_report_dtd;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_primary_sales_report_mtd;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_primary_sales_report_qtd;
import static saneforce.sanclm.Common_Class.Shared_Common_Pref.sp_primary_sales_report_ytd;
import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;


public class Sales_report_new extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_code_LOCATION_PERMISSION = 1;
    ImageView imp_back;
    NavigationView navigationView;
    public static RelativeLayout tool_Rel;
    TableRow tr_week1, tr_week2, tr_week3, tr_week4;
    TextView mTitle,toolbar_sub_title;
    ImageView iv_draw, btn_sync;
    PieChart pieChart_1;
    PieDataSet pieDataSet_1;
    PieData pieData_1;
    TextView TextPieChart_1, tv_progressbar_1, tv_progressbar_2, tv_progressbar_3, tv_progressbar_4, TV_DisplayDate, TV_LastSync,tv_secsale_label;
    ProgressBar progress_1, progress_2, progress_3, progress_4;
    Button btn_mtd, btn_qtd, btn_ytd;
    TextView PrimarySaleAmount, PrimarySaleTarget, PrimarySaleGR, PrimarySaleAch;
    TextView SecSaleAmount, SecSaleTarget;
    TextView RCPAAmount, RCPA_GR;
    TextView PCPMAmount, PCPMTarget;
    TextView MCL_TextPieChart,MVD_TextPieChart;
    PieChart MCL_pieGraph, MVD_pieGraph;
    TextView CHM_TextPieChart;
    PieChart CHM_pieGraph;
    RecyclerView rv_sales_summary;
    ImageView btn_cal, btn_div;
    //List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    Dialog PopUpCalender;
    TextView TV_FromDate, TV_ToDate;
    NumberPicker MonthPicker;
    NumberPicker YearPicker;
    int IsDateFilter = 0;
    String Fmonth, Tmonth, Fyear, Tyear;
    int SelectedTD = 0;
    String mTodayDate = "";
    Animation anim;
    TextView tv_name, tv_details, tv_number, tv_clustercap, notetxt;
    PieDataSet MCL_pieDataSet;
    PieData MCL_pieData;
    PieDataSet CHM_pieDataSet;
    PieData CHM_pieData;
    String TD_StartDate, TD_EndDate;
    LinearLayout checkinlay, checkinemmpty;
    TextView checkintxt,checkouttxt;
    String name = "", detail = "", phonenumber = "";
    Context context;
    Resources resources;
    public LocationManager mLocationManager;
    public Criteria criteria;
    public String bestProvider;
    Location locationGPS;
    SharedPreferences sharedpreferencess;
    public static final String mypreference = "mypref";
    public static final String statusky = "statusKey";
    public static final String Name = "nameKey";
    String work_status,statusflg;
    //sqlLite sqlLite;
    String DisplayLatitude="";
    String DisplayLongitude="";
    String DisplayAddress="";
    Location gps_loc, network_loc, final_loc;
    Dialog popup, subordinatelist ,PopupLocation;
    RelativeLayout parentrel;
    LinearLayout linear1, linear2;
    //GifImageView locationicon;
    TableRow tablecheckin, tablelatitude, tablelongitude, tableaddress;
    TextView labelcheckin, labelcolon1, checkindetail, labellatitude, labelcolon2, latitudedetail, labellongitude, labelcolon3, longitudedetail, labeladdress, labelcolon4, addressdetail;
    ImageView closepopup, closelistbutton;
    TextView call_prttxt;
    String data, a2, popupstatus, a1, clreport, b2, notifyingkey;
    LinearLayout layout_bottom_navigation;
    CardView card_primarysales;
   // CardView Card_MCL_Coverage;
    String FromMonth, FromYear, ToMonth, ToYear;
    LinearLayout adjustll,Card_chm_coverage,Card_MDL_coverage,Card_MCL_Coverage;


    String language;
    CommonSharedPreference mCommonSharedPreference;
    String sf_code,div_Code,db_connPath,sf_name,subsf_code,sf_type;
    public DataBaseHandler dbh;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v= inflater.inflate(R.layout.sales_drawer_design, container, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_drawer_design);

//        toolbar111.setVisibility(GONE);
        imp_back = findViewById(R.id.back_icon);
        mTitle = ( TextView ) findViewById(R.id.toolbar_title);
        toolbar_sub_title = ( TextView ) findViewById(R.id.toolbar_sub_title);
        //layout_bottom_navigation = findViewById(R.id.layout_bottom_navigation);
        Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
        sharedpreferencess = Sales_report_new.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        dbh=new DataBaseHandler(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        subsf_code=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        sf_type =  mCommonSharedPreference.getValueFromPreference("sf_type");

       // Dcrdatas.SelectedSF = "";

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(Sales_report_new.this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(Sales_report_new.this, "en");
            resources = context.getResources();
        }

        CommonUtilsMethods.FullScreencall(Sales_report_new.this);

        //dbh.open();


//        sqlLite = new sqlLite(Sales_report_new.this);
//        Cursor cursor = sqlLite.getAllLoginData();
//        if (cursor.moveToFirst()) {
//            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//        }
//        cursor.close();
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<CustomerMe>>() {
//        }.getType();
//        CustomerMeList = gson.fromJson(curval, type);


//        if (!Shared_Common_Pref.getAutomassyncFromSP(Sales_report_new.this)) {
//            this.changeToolBarText("Master Sync");
//            Shared_Common_Pref.putParentmastersyncToSP(Sales_report_new.this, true);
//            Intent intent = new Intent(Sales_report_new.this, Master_sync1.class);
//            intent.putExtra("MAS_ID", "2");
//            startActivity(intent);
//        }

        Log.d("cust_list", String.valueOf(Dcrdatas.custlist));

//        if (Dcrdatas.custlist == 4){
//
//            if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(Sales_report_new.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_code_LOCATION_PERMISSION);
//            }else {
//                if(Location_sevice.isServiceCreated()){
//
//                }else{
//                    startLocationservice();
//                }
//            }
//
//           toolbar_sub_title.setText(sf_name);
//            imp_back.setVisibility(View.GONE);
//            navigationView = findViewById(R.id.nav_view2);
//            Menu nav_Menu = navigationView.getMenu();
//            //nav_Menu.findItem(R.id.logout).setVisible(true);
//            View headerView = navigationView.getHeaderView(0);
//           // adjustll = headerView.findViewById(R.id.adjustll);
//            final DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout143);
//            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                    Sales_report_new.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//            drawer.setDrawerListener(toggle);
//            toggle.syncState();
//            navigationView.setNavigationItemSelectedListener(this);
//            updatenavigationbarcaption(nav_Menu);
////            BottomNavigationView bottomNavigation = ( BottomNavigationView ) findViewById(R.id.bottom_navigationnew1);
////            BottomNavigationMenuView menuView = ( BottomNavigationMenuView ) bottomNavigation.getChildAt(0);
//
////            if (Shared_Common_Pref.getSp_Target_report_md(Sales_report_new.this).equals("0")) {
////                bottomNavigation.getMenu().findItem(R.id.srhome).setTitle("Home");
////            }
////            bottomNavigation.getMenu().findItem(R.id.btMyActivity).setTitle(resources.getString(R.string.myactivity));
////            bottomNavigation.getMenu().findItem(R.id.btMyResource).setTitle(resources.getString(R.string.my_resources));
////            bottomNavigation.getMenu().findItem(R.id.btMyReports).setTitle(resources.getString(R.string.reports));
//
////            for (int i = 0; i < menuView.getChildCount(); i++) {
////                View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
////                ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
////                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
////                layoutParams.height = ( int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, displayMetrics);
////                layoutParams.width = ( int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, displayMetrics);
////                iconView.setLayoutParams(layoutParams);
////            }
////            bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//            tv_name = ( TextView ) headerView.findViewById(R.id.profilename);
//            tv_details = ( TextView ) headerView.findViewById(R.id.details);
//            tv_number = ( TextView ) headerView.findViewById(R.id.phonenumber);
//            checkinlay = headerView.findViewById(R.id.checkinlay);
//            checkintxt=headerView.findViewById(R.id.check_in);
//            checkouttxt=headerView.findViewById(R.id.check_out);
//            name = Shared_Common_Pref.getnameFromSP(Sales_report_new.this);
//            detail = Shared_Common_Pref.getusernameFromSP(Sales_report_new.this);
//            phonenumber = Shared_Common_Pref.getnameFromSP(Sales_report_new.this);
//            tv_name.setText(name);
//            tv_details.setText(detail);
//            tv_number.setText("");
//
////            adjustll.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Intent i = new Intent(Sales_report_new.this, User_Profile_Update.class);
////                    startActivity(i);
////                }
////            });
//
//            if (Shared_Common_Pref.gettpneedFromSP(this).equals("0")) {
//                nav_Menu.findItem(R.id.tpmain).setVisible(true);
//            } else {
//                nav_Menu.findItem(R.id.tpmain).setVisible(false);
//            }
//
//            if (Shared_Common_Pref.getrcpaFromSP(this).equals("0")) {
//                nav_Menu.findItem(R.id.rcpanew).setVisible(true);
//            } else {
//                nav_Menu.findItem(R.id.rcpanew).setVisible(false);
//            }
//
//            CustomerMeList = gson.fromJson(curval, type);
//            if ((Shared_Common_Pref.getmissedneedFromSP(this).equals("0"))) {
//                nav_Menu.findItem(R.id.missed_date).setVisible(true);
//            } else {
//                nav_Menu.findItem(R.id.missed_date).setVisible(false);
//            }
//
//            Log.d("getDesigCode", CustomerMeList.get(0).getDesigCode());
//            if (!(CustomerMeList.get(0).getDesigCode().equalsIgnoreCase("MR")) && (Shared_Common_Pref.getcmpgnneedFromSP(this).equals("0"))) {
//                nav_Menu.findItem(R.id.camp_appr).setVisible(true);
//            } else {
//                nav_Menu.findItem(R.id.camp_appr).setVisible(false);
//            }
//
//            if ((Shared_Common_Pref.getGeoChkFromSP(this).equals("0")) || (Shared_Common_Pref.getgeofencingneedFromSP(this).equals("1"))) {
//                nav_Menu.findItem(R.id.nearme).setVisible(true);
//            } else {
//                nav_Menu.findItem(R.id.nearme).setVisible(false);
//            }
//
//            if (Shared_Common_Pref.getMis_expense(this).equals("0")) {
//                nav_Menu.findItem(R.id.expense_entry).setVisible(true);
//            } else {
//                nav_Menu.findItem(R.id.expense_entry).setVisible(false);
//            }
//            if (Shared_Common_Pref.getfaq_need(this).equals("0")) {
//                nav_Menu.findItem(R.id.faq).setVisible(true);
//            } else {
//                nav_Menu.findItem(R.id.faq).setVisible(false);
//            }
//
//            if (Shared_Common_Pref.getSp_SrtNdSP(Sales_report_new.this).equals("0")) {
//                checkinlay.setVisibility(View.VISIBLE);
//                check_indata();
//                Log.d("hhhh","jkjkjk");
//            }else {
//                checkinlay.setVisibility(View.INVISIBLE);
//            }
//            checkouttxt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    check_outdata("", "0");
//                }
//            });
//
//        }else {
////            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            layout_bottom_navigation.setVisibility(GONE);
//            toolbar_sub_title.setVisibility(GONE);
//            mTitle.setText("Sales Report");
//        }

        imp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oo =new Intent(Sales_report_new.this, ReportActivity.class);
                startActivity(oo);

//                if (Dcrdatas.tarcount==1){
//                    Dcrdatas.custlist = 2;
//                    Intent oo =new Intent(Sales_report_new.this, Main2Activity.class);
//                    startActivity(oo);
//                    Dcrdatas.tarcount=0;
//                }else {
//                    Dcrdatas.custlist = 3;
//                    Intent oo =new Intent(Sales_report_new.this, Main2Activity.class);
//                    startActivity(oo);
//                }
            }
        });

        PopUpCalender = new Dialog(Sales_report_new.this);
        btn_sync = (ImageView) findViewById(R.id.btn_sync);
        TV_LastSync = (TextView)findViewById(R.id.TV_LastSync);
        pieChart_1 = (PieChart)  findViewById(R.id.pieChart_1);
        TextPieChart_1 = (TextView)  findViewById(R.id.TextPieChart_1);
        progress_1 = (ProgressBar)  findViewById(R.id.progress_1);
        tv_progressbar_1 = (TextView)  findViewById(R.id.tv_progressbar_1);
        progress_2 = (ProgressBar)  findViewById(R.id.progress_2);
        tv_progressbar_2 = (TextView)  findViewById(R.id.tv_progressbar_2);
        progress_3 = (ProgressBar)  findViewById(R.id.progress_3);
        tv_progressbar_3 = (TextView)  findViewById(R.id.tv_progressbar_3);
        progress_4 = (ProgressBar)  findViewById(R.id.progress_4);
        tv_progressbar_4 = (TextView)  findViewById(R.id.tv_progressbar_4);
        TV_DisplayDate = (TextView) findViewById(R.id.TV_DisplayDate);
        btn_mtd = (Button)  findViewById(R.id.btn_mtd);
        btn_qtd = (Button)  findViewById(R.id.btn_qtd);
        btn_ytd = (Button)  findViewById(R.id.btn_ytd);
        btn_cal = (ImageView)  findViewById(R.id.btn_cal);
        btn_div = (ImageView)  findViewById(R.id.btn_div);
        rv_sales_summary = (RecyclerView)  findViewById(R.id.rv_sales_summary);
        PrimarySaleAmount = (TextView)  findViewById(R.id.PrimarySaleAmount);
        PrimarySaleTarget = (TextView)  findViewById(R.id.PrimarySaleTarget);
        PrimarySaleGR = (TextView)  findViewById(R.id.PrimarySaleGR);
        PrimarySaleAch = (TextView)  findViewById(R.id.PrimarySaleAch);
        SecSaleAmount = (TextView)  findViewById(R.id.SecSaleAmount);
        SecSaleTarget = (TextView)  findViewById(R.id.SecSaleTarget);
        tv_secsale_label = (TextView)  findViewById(R.id.tv_secsale_label);
        RCPAAmount = (TextView)  findViewById(R.id.RCPAAmount);
        RCPA_GR = (TextView)  findViewById(R.id.RCPA_GR);
        PCPMAmount = (TextView)  findViewById(R.id.PCPMAmount);
        PCPMTarget = (TextView)  findViewById(R.id.PCPMTarget);
        MCL_TextPieChart = (TextView)  findViewById(R.id.MCL_TextPieChart);
        MCL_pieGraph = (PieChart)  findViewById(R.id.MCL_pieGraph);
        CHM_TextPieChart = (TextView)  findViewById(R.id.CHM_TextPieChart);
        CHM_pieGraph = (PieChart)  findViewById(R.id.CHM_pieGraph);
        MVD_TextPieChart = (TextView)  findViewById(R.id.MVD_TextPieChart);
        MVD_pieGraph = (PieChart)  findViewById(R.id.MVD_pieGraph);

        card_primarysales = findViewById(R.id.card_primarysales);
        Card_MCL_Coverage = (LinearLayout) findViewById(R.id.Card_MCL_Coverage);
        Card_chm_coverage = (LinearLayout) findViewById(R.id.chemist_coverage);
        Card_MDL_coverage = (LinearLayout) findViewById(R.id.MDL_coverage);

        tr_week1 = (TableRow) findViewById(R.id.tr_week1);
        tr_week2 = (TableRow) findViewById(R.id.tr_week2);
        tr_week3 = (TableRow) findViewById(R.id.tr_week3);
        tr_week4 = (TableRow) findViewById(R.id.tr_week4);
        TV_DisplayDate.setText("");
        TV_LastSync.setText("");

        Calendar mCal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mTodayDate = sdf.format(mCal.getTime());
        Dcrdatas.primarysalesselectedtdtag = SelectedTD;

        if (sf_type.equalsIgnoreCase("1")){
            Card_chm_coverage.setVisibility(View.VISIBLE);
            Card_MDL_coverage.setVisibility(GONE);
        }else{
            Card_chm_coverage.setVisibility(GONE);
            Card_MDL_coverage.setVisibility(View.VISIBLE);
        }


        btn_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_mtd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_qtd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_ytd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_mtd.setTextColor(Color.BLACK);
                btn_qtd.setTextColor(Color.BLACK);
                btn_ytd.setTextColor(Color.BLACK);
                ShowCalender();
            }
        });

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim = AnimationUtils.loadAnimation(Sales_report_new.this, R.anim.anim_rotate);
                btn_sync.startAnimation(anim);
                if(isNetworkConnected()) {
                    GetPrimarySalesReportData();
                    if (!sf_type.equalsIgnoreCase("1")) {
                        GetMvdReportData();
                    }
                }else
                {
                    Toast.makeText(Sales_report_new.this,getResources().getString(R.string.offline),Toast.LENGTH_LONG).show();
                }
            }

            });

        btn_mtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_mtd.setBackgroundResource(R.drawable.button_rpt_enable);
                btn_qtd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_ytd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_mtd.setTextColor(Color.WHITE);
                btn_qtd.setTextColor(Color.BLACK);
                btn_ytd.setTextColor(Color.BLACK);
                SelectedTD = 0;
               Dcrdatas.primarysalesselectedtdtag = SelectedTD;
                TV_DisplayDate.setText("");
                if (Shared_Common_Pref.getPrimarySalesReportJsonDataAvailable_MTD(Sales_report_new.this)) {
                    LoadPrimarySalesReportData();
                } else {
                    GetPrimarySalesReportData();
                    TV_LastSync.setText("");
                    if (!sf_type.equalsIgnoreCase("1")){
                        GetMvdReportData();
                    }
                }
            }
        });

        btn_qtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_mtd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_qtd.setBackgroundResource(R.drawable.button_rpt_enable);
                btn_ytd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_mtd.setTextColor(Color.BLACK);
                btn_qtd.setTextColor(Color.WHITE);
                btn_ytd.setTextColor(Color.BLACK);
                SelectedTD = 1;
                Dcrdatas.primarysalesselectedtdtag = SelectedTD;
                TV_DisplayDate.setText("");
                if (Shared_Common_Pref.getPrimarySalesReportJsonDataAvailable_QTD(Sales_report_new.this)) {
                    LoadPrimarySalesReportData();
                } else {
                    GetPrimarySalesReportData();
                    TV_LastSync.setText("");
                    if (!sf_type.equalsIgnoreCase("1")){
                        GetMvdReportData();
                    }
                }
            }
        });

        btn_ytd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_mtd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_qtd.setBackgroundResource(R.drawable.button_rpt_disable);
                btn_ytd.setBackgroundResource(R.drawable.button_rpt_enable);
                btn_mtd.setTextColor(Color.BLACK);
                btn_qtd.setTextColor(Color.BLACK);
                btn_ytd.setTextColor(Color.WHITE);
                SelectedTD = 2;
                Dcrdatas.primarysalesselectedtdtag = SelectedTD;
                TV_DisplayDate.setText("");
                if (Shared_Common_Pref.getPrimarySalesReportJsonDataAvailable_YTD(Sales_report_new.this)) {
                    LoadPrimarySalesReportData();
                } else {
                    GetPrimarySalesReportData();
                    TV_LastSync.setText("");
                    if (!sf_type.equalsIgnoreCase("1")){
                        GetMvdReportData();
                    }
                }
            }
        });

        card_primarysales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToPrimarySaleDetails();
            }
        });

        Card_MCL_Coverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales_report_new.this, DayReportsActivity.class);
                intent.putExtra("value", "2");
                startActivity(intent);
//                finish();
            }
        });

        if (Shared_Common_Pref.getPrimarySalesReportJsonDataAvailable_MTD(Sales_report_new.this)){
            LoadPrimarySalesReportData();
        } else {
           GetPrimarySalesReportData();
                if (!sf_type.equalsIgnoreCase("1")){
                    GetMvdReportData();
                }
       }

//        return v;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtilsMethods.FullScreencall(Sales_report_new.this);
    }

    public void changeToolBarText(String sTitle) {
        Log.d("Title Checking ", sTitle);
        mTitle.setText(sTitle);
    }

//    private void startLocationservice(){
//
//        if (!isLocationserviceRunning()){
//            Intent intent = new Intent(getApplicationContext(), Location_sevice.class);
//            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
//            startService(intent);
//        }
//    }

    private boolean isLocationserviceRunning(){
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null){
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
                if (LocationServices.class.getName().equals(service.service.getClassName())){
                    if (service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public void selected(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

//    public void updatenavigationbarcaption(Menu nav_Menu){
//        nav_Menu.findItem(R.id.home).setTitle(resources.getString(R.string.home));
//        nav_Menu.findItem(R.id.mydayplan).setTitle(resources.getString(R.string.mydayplan));
//        nav_Menu.findItem(R.id.outbox).setTitle(resources.getString(R.string.outbox));
//        nav_Menu.findItem(R.id.dayremarks).setTitle(resources.getString(R.string.dayactivityremark));
//        nav_Menu.findItem(R.id.Leave).setTitle(resources.getString(R.string.leave));
//        nav_Menu.findItem(R.id.tpmain).setTitle(resources.getString(R.string.tourplan));
//        nav_Menu.findItem(R.id.missed_date).setTitle(resources.getString(R.string.missed_date));
//        nav_Menu.findItem(R.id.expense_entry).setTitle(resources.getString(R.string.expense_entry));
//        nav_Menu.findItem(R.id.precall).setTitle(resources.getString(R.string.precall));
//        nav_Menu.findItem(R.id.nearme).setTitle(resources.getString(R.string.nearme));
//        nav_Menu.findItem(R.id.rcpanew).setTitle(resources.getString(R.string.rcpa));
//        nav_Menu.findItem(R.id.mas_sync).setTitle(resources.getString(R.string.master_sync));
//        nav_Menu.findItem(R.id.faq).setTitle(resources.getString(R.string.faq));
//        nav_Menu.findItem(R.id.help).setTitle(resources.getString(R.string.help));
//        nav_Menu.findItem(R.id.logout).setTitle(resources.getString(R.string.Logout));
//
////        textcatwise.setText(resources.getString(R.string.doc_catwise));
//    }

    private void NavigateToPrimarySaleDetails() {
        Dcrdatas.primarysalesselectedtdtag = SelectedTD;
        Intent intent = new Intent(Sales_report_new.this, PSalesDetailsReport.class);
        startActivity(intent);
       // finish();
    }

    public void GetPrimarySalesReportData() {
        final ProgressDialog mProgressDialog = new ProgressDialog(Sales_report_new.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        try {
            apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
//            sqlLite sqlLite;
//            sqlLite = new sqlLite(Sales_report_new.this);
//            Cursor cursor = sqlLite.getAllLoginData();
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            Log.d("ss_dashboard_1", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode() + " " + CustomerMeList.get(0).getSFName());

            Map<String, String> QryParam = new HashMap<>();
            //QryParam.put("axn", "get/primary_ss_dashboard");
            QryParam.put("divisionCode", div_Code+","); 
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
            //For MTD :
            if (SelectedTD == 0) {
                Date mDate = new Date();
                SimpleDateFormat mFormat1_MTD = new SimpleDateFormat("MM");
                SimpleDateFormat mFormat2_MTD = new SimpleDateFormat("MMM");
                SimpleDateFormat mFormat3_MTD = new SimpleDateFormat("yyyy");
                String CurrentMonth1_MTD = mFormat1_MTD.format(mDate);
                String CurrentMonth2_MTD = mFormat2_MTD.format(mDate);
                String CurrentYear_MTD = mFormat3_MTD.format(mDate);

                FromMonth = CurrentMonth1_MTD;
                FromYear = CurrentYear_MTD;
                ToMonth = CurrentMonth1_MTD;
                ToYear = CurrentYear_MTD;

                QryParam.put("fmonth", FromMonth);
                QryParam.put("fyear", FromYear);
                QryParam.put("tomonth", ToMonth);
                QryParam.put("toyear", ToYear);

                TD_StartDate = CurrentMonth2_MTD + " " + CurrentYear_MTD;
                TD_EndDate = CurrentMonth2_MTD + " " + CurrentYear_MTD;

                Dcrdatas.TD_StartDate = TD_StartDate;
               Dcrdatas.TD_EndDate = TD_EndDate;

                Log.e("Sales_Dashboard_MTD", QryParam.toString() + "--" + TD_StartDate + "--" + TD_EndDate);
            }
            //For QTD :
            else if (SelectedTD == 1) {
                try {
                    //From Date Format :
                    Calendar cal1 = Calendar.getInstance();
                    cal1.set(Calendar.DAY_OF_MONTH, 1);
                    cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) / 3 * 3);
                    Date FDate1 = cal1.getTime();

                    SimpleDateFormat SDF_1Format_1_QTD = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat SDF_1Format_2_QTD = new SimpleDateFormat("MM");
                    SimpleDateFormat SDF_1Format_3_QTD = new SimpleDateFormat("MMM");
                    SimpleDateFormat SDF_1Format_4_QTD = new SimpleDateFormat("yyyy");

                    String mStartDate = SDF_1Format_1_QTD.format(FDate1);
                    Date FormattedStartDate = SDF_1Format_1_QTD.parse(mStartDate);

                    String FromMonth_Format1_QTD = SDF_1Format_2_QTD.format(FormattedStartDate);
                    String FromMonth_Format2_QTD = SDF_1Format_3_QTD.format(FormattedStartDate);
                    String FromYear_Format_QTD = SDF_1Format_4_QTD.format(FormattedStartDate);

                    //To Date Format :
                    Date mDate = new Date();
                    SimpleDateFormat SDF_2Format_1_QTD = new SimpleDateFormat("MM");
                    SimpleDateFormat SDF_2Format_2_QTD = new SimpleDateFormat("MMM");
                    SimpleDateFormat SDF_2Format_3_QTD = new SimpleDateFormat("yyyy");

                    String ToMonth_Format1_QTD = SDF_2Format_1_QTD.format(mDate);
                    String ToMonth_Format2_QTD = SDF_2Format_2_QTD.format(mDate);
                    String ToYear_Format_QTD = SDF_2Format_3_QTD.format(mDate);

                    FromMonth = FromMonth_Format1_QTD;
                    FromYear = FromYear_Format_QTD;
                    ToMonth = ToMonth_Format1_QTD;
                    ToYear = ToYear_Format_QTD;

                    QryParam.put("fmonth", FromMonth);
                    QryParam.put("fyear", FromYear);
                    QryParam.put("tomonth", ToMonth);
                    QryParam.put("toyear", ToYear);

                    TD_StartDate = FromMonth_Format2_QTD + " " + FromYear_Format_QTD;
                    TD_EndDate = ToMonth_Format2_QTD + " " + ToYear_Format_QTD;

                   Dcrdatas.TD_StartDate = TD_StartDate;
                   Dcrdatas.TD_EndDate = TD_EndDate;

                    Log.e("Sales_Dashboard_QTD", QryParam.toString() + "--" + TD_StartDate + "--" + TD_EndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //For YTD :
            else if (SelectedTD == 2) {
                try {
                    //From Date Format :
                    int mmCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
                    int mmCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
                    String FinancialYearFrom = "";
                    if (mmCurrentMonth < 4) {
                        FinancialYearFrom = (mmCurrentYear - 1) + "-04-01";
                    } else {
                        FinancialYearFrom = (mmCurrentYear) + "-04-01";
                    }
                    String StartDate = FinancialYearFrom;

                    SimpleDateFormat SDF_1Format_1_YTD = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat SDF_1Format_2_YTD = new SimpleDateFormat("MM");
                    SimpleDateFormat SDF_1Format_3_YTD = new SimpleDateFormat("MMM");
                    SimpleDateFormat SDF_1Format_4_YTD = new SimpleDateFormat("yyyy");

                    Date FormattedStartDate = SDF_1Format_1_YTD.parse(StartDate);

                    String FromMonth_Format1_YTD = SDF_1Format_2_YTD.format(FormattedStartDate);
                    String FromMonth_Format2_YTD = SDF_1Format_3_YTD.format(FormattedStartDate);
                    String FromYear_Format_YTD = SDF_1Format_4_YTD.format(FormattedStartDate);

                    //To Date Format :
                    Date mDate = new Date();
                    SimpleDateFormat SDF_2Format_1_YTD = new SimpleDateFormat("MM");
                    SimpleDateFormat SDF_2Format_2_YTD = new SimpleDateFormat("MMM");
                    SimpleDateFormat SDF_2Format_3_YTD = new SimpleDateFormat("yyyy");

                    String ToMonth_Format1_YTD = SDF_2Format_1_YTD.format(mDate);
                    String ToMonth_Format2_YTD = SDF_2Format_2_YTD.format(mDate);
                    String ToYear_Format_YTD = SDF_2Format_3_YTD.format(mDate);

                    FromMonth = FromMonth_Format1_YTD;
                    FromYear = FromYear_Format_YTD;
                    ToMonth = ToMonth_Format1_YTD;
                    ToYear = ToYear_Format_YTD;

                    QryParam.put("fmonth", FromMonth);
                    QryParam.put("fyear", FromYear);
                    QryParam.put("tomonth", ToMonth);
                    QryParam.put("toyear", ToYear);

                    TD_StartDate = FromMonth_Format2_YTD + " " + FromYear_Format_YTD;
                    TD_EndDate = ToMonth_Format2_YTD + " " + ToYear_Format_YTD;

                   Dcrdatas.TD_StartDate = TD_StartDate;
                  Dcrdatas.TD_EndDate = TD_EndDate;

                    Log.e("Sales_Dashboard_YTD", QryParam.toString() + "--" + TD_StartDate + "--" + TD_EndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (SelectedTD == 3 && IsDateFilter == 1) {
                FromMonth = Fmonth;
                FromYear = Fyear;
                ToMonth = Tmonth;
                ToYear = Tyear;

                QryParam.put("fmonth", FromMonth);
                QryParam.put("fyear", FromYear);
                QryParam.put("tomonth", ToMonth);
                QryParam.put("toyear", ToYear);

                Log.e("Sales_Dashboard_DTD", QryParam.toString());
            }
            Call<JsonArray> call = apiService.getSalesDataAsJArray(QryParam);
            Log.d("ss_dashboard_3", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("ss_dashboard_2", response.body() + " " + response.body().toString());
                        Log.d("ss_dashboard_3", response.toString());
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        if (!response.body().toString().isEmpty()) {
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        String Division_Name = jsonObject.getString("Division_Name");
                                        if (Division_Name.equalsIgnoreCase("Total")) {
                                            String Target = jsonObject.getString("Target");
                                            String Sale = jsonObject.getString("Sale");
                                            String achie = jsonObject.getString("achie");
                                            String PSale = jsonObject.getString("PSale");
                                            String Growth = jsonObject.getString("Growth");
                                            String PC = jsonObject.getString("PC");
                                            String week1 = jsonObject.getString("1 - 7");
                                            String week2 = jsonObject.getString("1 - 14");
                                            String week3 = jsonObject.getString("1 - 21");
                                            String week4 = jsonObject.getString("1 - 31");
                                            String SecSale = jsonObject.getString("SecSale");
                                            String PSecSale = jsonObject.getString("PSecSale");
                                            String PPCPM = jsonObject.getString("PPCPM");
                                            String PCGrowth = jsonObject.getString("PCGrowth");
                                            String Drlst = jsonObject.getString("Drlst");
                                            String Drmet = jsonObject.getString("Drmet");
                                            String DrCov = jsonObject.getString("DrCov");
                                            String ChemLst = jsonObject.getString("ChemLst");
                                            String ChemMet = jsonObject.getString("ChemMet");
                                            String ChemCov = jsonObject.getString("ChemCov");
                                            String Rcpa = jsonObject.getString("Rcpa");
                                            String Rcpa_Growth = jsonObject.getString("Rcpa_Growth");

                                            Calendar mCal = Calendar.getInstance();
                                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
                                            String CurrentDateFormat1 = sdf1.format(mCal.getTime());
                                            String CurrentDateFormat2 = sdf2.format(mCal.getTime());
                                            dbh.open();

                                            if (SelectedTD == 0) {
                                                String MTD_ReportDate = TD_StartDate + " To " + TD_EndDate;
                                                TV_DisplayDate.setText(MTD_ReportDate);
                                                ClearSharedPreference(sp_primary_sales_report_mtd);
                                                dbh.deletePSaleTables_MTD();
                                                Shared_Common_Pref.putPrimarySalesHQWiseReport_MTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesBrandWiseReport_MTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesReportDetails_MTD(Sales_report_new.this, jsonArray.toString(), MTD_ReportDate, CurrentDateFormat1, CurrentDateFormat2, FromMonth, FromYear, ToMonth, ToYear, true);
                                                Shared_Common_Pref.putPrimarySalesFieldReport_MTD(Sales_report_new.this, "", false);
                                            } else if (SelectedTD == 1) {
                                                String QTD_ReportDate = TD_StartDate + " To " + TD_EndDate;
                                                TV_DisplayDate.setText(QTD_ReportDate);
                                                ClearSharedPreference(sp_primary_sales_report_qtd);
                                                dbh.deletePSaleTables_QTD();
                                                Shared_Common_Pref.putPrimarySalesHQWiseReport_QTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesFieldReport_QTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesBrandWiseReport_QTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesReportDetails_QTD(Sales_report_new.this, jsonArray.toString(), QTD_ReportDate, CurrentDateFormat1, CurrentDateFormat2, FromMonth, FromYear, ToMonth, ToYear, true);
                                            } else if (SelectedTD == 2) {
                                                String YTD_ReportDate = TD_StartDate + " To " + TD_EndDate;
                                                TV_DisplayDate.setText(YTD_ReportDate);
                                                ClearSharedPreference(sp_primary_sales_report_ytd);
                                               dbh.deletePSaleTables_YTD();
                                                Shared_Common_Pref.putPrimarySalesHQWiseReport_YTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesFieldReport_YTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesBrandWiseReport_YTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesReportDetails_YTD(Sales_report_new.this, jsonArray.toString(), YTD_ReportDate, CurrentDateFormat1, CurrentDateFormat2, FromMonth, FromYear, ToMonth, ToYear, true);
                                            } else if (SelectedTD == 3) {
                                                String DTD_ReportDate = TV_FromDate.getText().toString() + " To " + TV_ToDate.getText().toString();
                                                TV_DisplayDate.setText(DTD_ReportDate);
                                                ClearSharedPreference(sp_primary_sales_report_dtd);
                                                Shared_Common_Pref.putPrimarySalesHQWiseReport_DTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesFieldReport_DTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesBrandWiseReport_DTD(Sales_report_new.this, "", false);
                                                Shared_Common_Pref.putPrimarySalesReportDate_DTD(Sales_report_new.this, DTD_ReportDate, FromMonth, FromYear, ToMonth, ToYear);
                                            }
                                            TV_LastSync.setText("Last Sync : " + CurrentDateFormat2);
                                            ShowSalesPerformancePieChart(achie, Target, Sale);
                                            ShowPrgressPercent(week1, week2, week3, week4);
                                            ShowSalesSummary(Target, Sale, achie, PSale, Growth, PC, SecSale, PSecSale, PPCPM, PCGrowth, Rcpa, Rcpa_Growth);
                                            ShowSalesSummaryMCLChartChart(Drlst, Drmet, DrCov);
                                            ShowSalesSummaryChemChartChart(ChemLst, ChemMet, ChemCov);
                                        }
                                    }
                                } else {
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                    ShowSalesPerformancePieChart("0", "0", "0");
                                    ShowPrgressPercent("0", "0", "0", "0");
                                    ShowSalesSummary("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                                    ShowSalesSummaryMCLChartChart("0", "0", "0");
                                    ShowSalesSummaryChemChartChart("0", "0", "0");
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        } else {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            ShowSalesPerformancePieChart("0", "0", "0");
                            ShowPrgressPercent("0", "0", "0", "0");
                            ShowSalesSummary("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                            ShowSalesSummaryMCLChartChart("0", "0", "0");
                            ShowSalesSummaryChemChartChart("0", "0", "0");
                        }
                    } else {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        ShowSalesPerformancePieChart("0", "0", "0");
                        ShowPrgressPercent("0", "0", "0", "0");
                        ShowSalesSummary("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                        ShowSalesSummaryMCLChartChart("0", "0", "0");
                        ShowSalesSummaryChemChartChart("0", "0", "0");
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Log.e("LeaveReponse TAG", "onFailure : " + t.toString());
                    Toast.makeText(Sales_report_new.this, "Something went wrong  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Sales_report_new.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void LoadPrimarySalesReportData() {
        final ProgressDialog mProgressDialog = new ProgressDialog(Sales_report_new.this);
        mProgressDialog.setIndeterminate(true);
        //mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        try {
            String TD_Data = "";
            String nDisplayCurrentDate = "";
            String ReportDate = "";
            String MVD_data = "";

            if (SelectedTD == 0) {
                TD_Data = Shared_Common_Pref.getPrimarySalesReportJsonData_MTD(Sales_report_new.this);
                nDisplayCurrentDate = Shared_Common_Pref.getPrimarySalesReportFetchDate2_MTD(Sales_report_new.this);
                ReportDate = Shared_Common_Pref.getPrimarySalesReportDate_MTD(Sales_report_new.this);
                if (!sf_type.equalsIgnoreCase("1")){
                    MVD_data = Shared_Common_Pref.getMVDjsondata_MTD(Sales_report_new.this);
                }
            } else if (SelectedTD == 1) {
                TD_Data = Shared_Common_Pref.getPrimarySalesReportJsonData_QTD(Sales_report_new.this);
                nDisplayCurrentDate = Shared_Common_Pref.getPrimarySalesReportFetchDate2_QTD(Sales_report_new.this);
                ReportDate = Shared_Common_Pref.getPrimarySalesReportDate_QTD(Sales_report_new.this);
                if (!sf_type.equalsIgnoreCase("1")){
                    MVD_data = Shared_Common_Pref.getMVDjsondata_QTD(Sales_report_new.this);
                }
            } else if (SelectedTD == 2) {
                TD_Data = Shared_Common_Pref.getPrimarySalesReportJsonData_YTD(Sales_report_new.this);
                nDisplayCurrentDate = Shared_Common_Pref.getPrimarySalesReportFetchDate2_YTD(Sales_report_new.this);
                ReportDate = Shared_Common_Pref.getPrimarySalesReportDate_YTD(Sales_report_new.this);
                if (!sf_type.equalsIgnoreCase("1")) {
                    MVD_data = Shared_Common_Pref.getMVDjsondata_YTD(Sales_report_new.this);
                }
            }
            TV_LastSync.setText("Last Sync : " + nDisplayCurrentDate);
            TV_DisplayDate.setText(ReportDate);
            JSONArray jsonArray = new JSONArray(TD_Data);
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String Division_Name = jsonObject.getString("Division_Name");
                    if (Division_Name.equalsIgnoreCase("Total")) {
                        String Target = jsonObject.getString("Target");
                        String Sale = jsonObject.getString("Sale");
                        String achie = jsonObject.getString("achie");
                        String PSale = jsonObject.getString("PSale");
                        String Growth = jsonObject.getString("Growth");
                        String PC = jsonObject.getString("PC");
                        String week1 = jsonObject.getString("1 - 7");
                        String week2 = jsonObject.getString("1 - 14");
                        String week3 = jsonObject.getString("1 - 21");
                        String week4 = jsonObject.getString("1 - 31");
                        String SecSale = jsonObject.getString("SecSale");
                        String PSecSale = jsonObject.getString("PSecSale");
                        String PPCPM = jsonObject.getString("PPCPM");
                        String PCGrowth = jsonObject.getString("PCGrowth");
                        String Drlst = jsonObject.getString("Drlst");
                        String Drmet = jsonObject.getString("Drmet");
                        String DrCov = jsonObject.getString("DrCov");
                        String ChemLst = jsonObject.getString("ChemLst");
                        String ChemMet = jsonObject.getString("ChemMet");
                        String ChemCov = jsonObject.getString("ChemCov");
                        String Rcpa = jsonObject.getString("Rcpa");
                        String Rcpa_Growth = jsonObject.getString("Rcpa_Growth");

                        ShowSalesPerformancePieChart(achie, Target, Sale);
                        ShowPrgressPercent(week1, week2, week3, week4);
                        ShowSalesSummary(Target, Sale, achie, PSale, Growth, PC, SecSale, PSecSale, PPCPM, PCGrowth, Rcpa, Rcpa_Growth);
                        ShowSalesSummaryMCLChartChart(Drlst, Drmet, DrCov);
                        ShowSalesSummaryChemChartChart(ChemLst, ChemMet, ChemCov);
                    }
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } else {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                ShowSalesPerformancePieChart("0", "0", "0");
                ShowPrgressPercent("0", "0", "0", "0");
                ShowSalesSummary("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                ShowSalesSummaryMCLChartChart("0", "0", "0");
                ShowSalesSummaryChemChartChart("0", "0", "0");
            }

            if (!sf_type.equalsIgnoreCase("1")){
                MVDLocal_data(MVD_data);
            }

        } catch (JSONException js) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            Toast.makeText(Sales_report_new.this, "Something went wrong  " + js.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void MVDLocal_data(String MVD_data){
        try {
            JSONArray jsonArray = new JSONArray(MVD_data);
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String Sfcode = jsonObject.getString("Sf_Code");
                    String ttl = jsonObject.getString("ttl");
                    String met = jsonObject.getString("met");
                    String seen = jsonObject.getString("seen");
                    String msd = jsonObject.getString("msd");

                    ShowSalesSummaryMVDChartChart(ttl,met,seen,msd);
                }
            } else {
                ShowSalesSummaryMVDChartChart("0","0","0","0");
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }



    public void ShowSalesPerformancePieChart(String achie, String Target, String Sale) {
        ArrayList<Integer> colors = new ArrayList<>();
        String Achievment = achie;
        if (Achievment.contains("-")) {
            Float F1 = Float.parseFloat(Achievment);
            if (F1 > 100) {
                Achievment = "100";
            } else if (F1 < -100) {
                Achievment = "100";
            } else{
                Achievment = Achievment.replace("-", "");
            }
            colors.add(Color.rgb(255, 0, 0));
            colors.add(Color.rgb(229, 231, 233));
        }else {
            Float F1 = Float.parseFloat(Achievment);
            if (F1 > 100) {
                Achievment = "100";
            }
            colors.add(Color.rgb(0, 154, 0));
            colors.add(Color.rgb(229, 231, 233));
        }
        Float countdata1 = Float.parseFloat(Achievment);
        Float countdata2 = Float.parseFloat(achie);
        String centertxt = String.format("%.2f", countdata2);
        Float seconddata = 100 - countdata1;
        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        PieEntry pieEntry = new PieEntry(countdata1, "");
        values.add(pieEntry);
        PieEntry pieEntry1 = new PieEntry(seconddata, "");
        values.add(pieEntry1);

        pieDataSet_1 = new PieDataSet(values, "");
        pieData_1 = new PieData(pieDataSet_1);
        pieData_1.setValueFormatter(new PercentFormatter());
        pieChart_1.setData(pieData_1);
        pieChart_1.setUsePercentValues(true);
        pieChart_1.setDrawHoleEnabled(true);
        pieChart_1.setCenterTextSize(15f);
        pieChart_1.setCenterTextColor(Color.rgb(0, 0, 0));
        pieChart_1.setTransparentCircleRadius(40f);
        pieChart_1.setHoleRadius(72f);
        pieDataSet_1.setColors(colors);
        pieData_1.setValueTextSize(0f);
        pieData_1.setValueTextColor(Color.WHITE);
        pieChart_1.animateXY(1400, 1400);

        pieChart_1.setCenterText(achie + "%");
        pieChart_1.setCenterTextSize(15);
        Description description = pieChart_1.getDescription();
        description.setEnabled(false);
        Legend legend = pieChart_1.getLegend();
        legend.setEnabled(false);
        Double D1 = Double.parseDouble(Target);
        Double D2 = Double.parseDouble(Sale);
        double scale3 = Math.pow(10, 3);

        String mTarget = "₹ " + Math.round(D1 * scale3) / scale3 + "L";
        String mSale = "₹ " + Math.round(D2 * scale3) / scale3 + "L";
        String Total = mSale + "/ " + mTarget;
        TextPieChart_1.setText(Total);
    }

    private void ShowPrgressPercent(String week1, String week2, String week3, String week4) {
        try {
            double mProgress_count_1 = 0.0;
            double mProgress_count_2 = 0.0;
            double mProgress_count_3 = 0.0;
            double mProgress_count_4 = 0.0;
            Double D1 = Double.parseDouble(week1);
            Double D2 = Double.parseDouble(week2);
            Double D3 = Double.parseDouble(week3);
            Double D4 = Double.parseDouble(week4);

            if(SelectedTD == 0){
                Calendar calender = Calendar.getInstance();
                int WeekCount = calender.get(Calendar.WEEK_OF_MONTH);
                Log.d("Current_week", String.valueOf(WeekCount));
                tr_week1.setVisibility(View.GONE);
                tr_week2.setVisibility(View.GONE);
                tr_week3.setVisibility(View.GONE);
                tr_week4.setVisibility(View.GONE);
                if (WeekCount == 1) {
                    tr_week1.setVisibility(View.VISIBLE);
                    mProgress_count_1 = mProgress_count_1 + D1;
                    Double aDouble = new Double(mProgress_count_1);
                    int mProgress_value_1 = aDouble.intValue();
                    progress_1.setProgress(mProgress_value_1);
                    progress_1.setMax(100);
                    tv_progressbar_1.setText(D1 + "%");

                    float F1 = (float) mProgress_count_1;
                    ProgressBarAnimation anim1 = new ProgressBarAnimation(progress_1, 0, F1);
                    anim1.setDuration(3000);
                    progress_1.startAnimation(anim1);
                }
                else if (WeekCount == 2) {
                    tr_week1.setVisibility(View.VISIBLE);
                    tr_week2.setVisibility(View.VISIBLE);
                    mProgress_count_1 = mProgress_count_1 + D1;
                    Double aDouble = new Double(mProgress_count_1);
                    int mProgress_value_1 = aDouble.intValue();
                    progress_1.setProgress(mProgress_value_1);
                    progress_1.setMax(100);
                    tv_progressbar_1.setText(D1 + "%");

                    float F1 = (float) mProgress_count_1;
                    ProgressBarAnimation anim1 = new ProgressBarAnimation(progress_1, 0, F1);
                    anim1.setDuration(3000);
                    progress_1.startAnimation(anim1);

                    mProgress_count_2 = mProgress_count_2 + D2;
                    Double bDouble = new Double(mProgress_count_2);
                    int mProgress_value_2 = bDouble.intValue();
                    progress_2.setProgress(mProgress_value_2);
                    progress_2.setMax(100);
                    tv_progressbar_2.setText(D2 + "%");

                    float F2 = (float) mProgress_count_2;
                    ProgressBarAnimation anim2 = new ProgressBarAnimation(progress_2, 0, F2);
                    anim2.setDuration(3000);
                    progress_2.startAnimation(anim2);
                } else if (WeekCount == 3) {
                    tr_week1.setVisibility(View.VISIBLE);
                    tr_week2.setVisibility(View.VISIBLE);
                    tr_week3.setVisibility(View.VISIBLE);
                    mProgress_count_1 = mProgress_count_1 + D1;
                    Double aDouble = new Double(mProgress_count_1);
                    int mProgress_value_1 = aDouble.intValue();
                    progress_1.setProgress(mProgress_value_1);
                    progress_1.setMax(100);
                    tv_progressbar_1.setText(D1 + "%");

                    float F1 = (float) mProgress_count_1;
                    ProgressBarAnimation anim1 = new ProgressBarAnimation(progress_1, 0, F1);
                    anim1.setDuration(3000);
                    progress_1.startAnimation(anim1);

                    mProgress_count_2 = mProgress_count_2 + D2;
                    Double bDouble = new Double(mProgress_count_2);
                    int mProgress_value_2 = bDouble.intValue();
                    progress_2.setProgress(mProgress_value_2);
                    progress_2.setMax(100);
                    tv_progressbar_2.setText(D2 + "%");

                    float F2 = (float) mProgress_count_2;
                    ProgressBarAnimation anim2 = new ProgressBarAnimation(progress_2, 0, F2);
                    anim2.setDuration(3000);
                    progress_2.startAnimation(anim2);

                    mProgress_count_3 = mProgress_count_3 + D3;
                    Double cDouble = new Double(mProgress_count_3);
                    int mProgress_value_3 = cDouble.intValue();
                    progress_3.setProgress(mProgress_value_3);
                    progress_3.setMax(100);
                    tv_progressbar_3.setText(D3 + "%");

                    float F3 = (float) mProgress_count_3;
                    ProgressBarAnimation anim3 = new ProgressBarAnimation(progress_3, 0, F3);
                    anim3.setDuration(3000);
                    progress_3.startAnimation(anim3);
                }
                else if (WeekCount == 4) {
                    tr_week1.setVisibility(View.VISIBLE);
                    tr_week2.setVisibility(View.VISIBLE);
                    tr_week3.setVisibility(View.VISIBLE);
                    tr_week4.setVisibility(View.VISIBLE);
                    mProgress_count_1 = mProgress_count_1 + D1;
                    Double aDouble = new Double(mProgress_count_1);
                    int mProgress_value_1 = aDouble.intValue();
                    progress_1.setProgress(mProgress_value_1);
                    progress_1.setMax(100);
                    tv_progressbar_1.setText(D1 + "%");

                    float F1 = (float) mProgress_count_1;
                    ProgressBarAnimation anim1 = new ProgressBarAnimation(progress_1, 0, F1);
                    anim1.setDuration(3000);
                    progress_1.startAnimation(anim1);

                    mProgress_count_2 = mProgress_count_2 + D2;
                    Double bDouble = new Double(mProgress_count_2);
                    int mProgress_value_2 = bDouble.intValue();
                    progress_2.setProgress(mProgress_value_2);
                    progress_2.setMax(100);
                    tv_progressbar_2.setText(D2 + "%");

                    float F2 = (float) mProgress_count_2;
                    ProgressBarAnimation anim2 = new ProgressBarAnimation(progress_2, 0, F2);
                    anim2.setDuration(3000);
                    progress_2.startAnimation(anim2);

                    mProgress_count_3 = mProgress_count_3 + D3;
                    Double cDouble = new Double(mProgress_count_3);
                    int mProgress_value_3 = cDouble.intValue();
                    progress_3.setProgress(mProgress_value_3);
                    progress_3.setMax(100);
                    tv_progressbar_3.setText(D3 + "%");

                    float F3 = (float) mProgress_count_3;
                    ProgressBarAnimation anim3 = new ProgressBarAnimation(progress_3, 0, F3);
                    anim3.setDuration(3000);
                    progress_3.startAnimation(anim3);

                    mProgress_count_4 = mProgress_count_4 + D4;
                    Double dDouble = new Double(mProgress_count_4);
                    int mProgress_value_4 = dDouble.intValue();
                    progress_4.setProgress(mProgress_value_4);
                    progress_4.setMax(100);
                    tv_progressbar_4.setText(D4 + "%");

                    float F4 = (float) mProgress_count_4;
                    ProgressBarAnimation anim4 = new ProgressBarAnimation(progress_4, 0, F4);
                    anim4.setDuration(3000);
                    progress_4.startAnimation(anim4);
                }
                else if (WeekCount == 5) {
                    tr_week1.setVisibility(View.VISIBLE);
                    tr_week2.setVisibility(View.VISIBLE);
                    tr_week3.setVisibility(View.VISIBLE);
                    tr_week4.setVisibility(View.VISIBLE);
                    mProgress_count_1 = mProgress_count_1 + D1;
                    Double aDouble = new Double(mProgress_count_1);
                    int mProgress_value_1 = aDouble.intValue();
                    progress_1.setProgress(mProgress_value_1);
                    progress_1.setMax(100);
                    tv_progressbar_1.setText(D1 + "%");

                    float F1 = (float) mProgress_count_1;
                    ProgressBarAnimation anim1 = new ProgressBarAnimation(progress_1, 0, F1);
                    anim1.setDuration(3000);
                    progress_1.startAnimation(anim1);

                    mProgress_count_2 = mProgress_count_2 + D2;
                    Double bDouble = new Double(mProgress_count_2);
                    int mProgress_value_2 = bDouble.intValue();
                    progress_2.setProgress(mProgress_value_2);
                    progress_2.setMax(100);
                    tv_progressbar_2.setText(D2 + "%");

                    float F2 = (float) mProgress_count_2;
                    ProgressBarAnimation anim2 = new ProgressBarAnimation(progress_2, 0, F2);
                    anim2.setDuration(3000);
                    progress_2.startAnimation(anim2);

                    mProgress_count_3 = mProgress_count_3 + D3;
                    Double cDouble = new Double(mProgress_count_3);
                    int mProgress_value_3 = cDouble.intValue();
                    progress_3.setProgress(mProgress_value_3);
                    progress_3.setMax(100);
                    tv_progressbar_3.setText(D3 + "%");

                    float F3 = (float) mProgress_count_3;
                    ProgressBarAnimation anim3 = new ProgressBarAnimation(progress_3, 0, F3);
                    anim3.setDuration(3000);
                    progress_3.startAnimation(anim3);

                    mProgress_count_4 = mProgress_count_4 + D4;
                    Double dDouble = new Double(mProgress_count_4);
                    int mProgress_value_4 = dDouble.intValue();
                    progress_4.setProgress(mProgress_value_4);
                    progress_4.setMax(100);
                    tv_progressbar_4.setText(D4 + "%");

                    float F4 = (float) mProgress_count_4;
                    ProgressBarAnimation anim4 = new ProgressBarAnimation(progress_4, 0, F4);
                    anim4.setDuration(3000);
                    progress_4.startAnimation(anim4);
                }
            }
            else
            {
                tr_week1.setVisibility(View.VISIBLE);
                tr_week2.setVisibility(View.VISIBLE);
                tr_week3.setVisibility(View.VISIBLE);
                tr_week4.setVisibility(View.VISIBLE);

                mProgress_count_1 = mProgress_count_1 + D1;
                Double aDouble = new Double(mProgress_count_1);
                int mProgress_value_1 = aDouble.intValue();
                progress_1.setProgress(mProgress_value_1);
                progress_1.setMax(100);
                tv_progressbar_1.setText(D1 + "%");

                float F1 = (float) mProgress_count_1;
                ProgressBarAnimation anim1 = new ProgressBarAnimation(progress_1, 0, F1);
                anim1.setDuration(3000);
                progress_1.startAnimation(anim1);

                mProgress_count_2 = mProgress_count_2 + D2;
                Double bDouble = new Double(mProgress_count_2);
                int mProgress_value_2 = bDouble.intValue();
                progress_2.setProgress(mProgress_value_2);
                progress_2.setMax(100);
                tv_progressbar_2.setText(D2 + "%");

                float F2 = (float) mProgress_count_2;
                ProgressBarAnimation anim2 = new ProgressBarAnimation(progress_2, 0, F2);
                anim2.setDuration(3000);
                progress_2.startAnimation(anim2);

                mProgress_count_3 = mProgress_count_3 + D3;
                Double cDouble = new Double(mProgress_count_3);
                int mProgress_value_3 = cDouble.intValue();
                progress_3.setProgress(mProgress_value_3);
                progress_3.setMax(100);
                tv_progressbar_3.setText(D3 + "%");

                float F3 = (float) mProgress_count_3;
                ProgressBarAnimation anim3 = new ProgressBarAnimation(progress_3, 0, F3);
                anim3.setDuration(3000);
                progress_3.startAnimation(anim3);

                mProgress_count_4 = mProgress_count_4 + D4;
                Double dDouble = new Double(mProgress_count_4);
                int mProgress_value_4 = dDouble.intValue();
                progress_4.setProgress(mProgress_value_4);
                progress_4.setMax(100);
                tv_progressbar_4.setText(D4 + "%");

                float F4 = (float) mProgress_count_4;
                ProgressBarAnimation anim4 = new ProgressBarAnimation(progress_4, 0, F4);
                anim4.setDuration(3000);
                progress_4.startAnimation(anim4);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowSalesSummary(String Target, String Sale, String achie, String PSale, String Growth, String PC, String SecSale, String PSecSale,
                                 String PPCPM, String PCGrowth, String Rcpa, String Rcpa_Growth) {
        //Card -1
        if (!Sale.equals("0")) {
            Double sales=Double.parseDouble(Sale);
            PrimarySaleAmount.setText("₹ " + String.format("%.3f", sales));
        } else {
            PrimarySaleAmount.setText("₹ 0");
        }
        if (!Target.equals("0")) {
            Double target = Double.parseDouble(Target);
            PrimarySaleTarget.setText("Target " + String.format("%.3f", target) + " L");
        } else {
            PrimarySaleTarget.setText("Target 0");
        }
        if (!Growth.equals("0")) {
            PrimarySaleGR.setText(Growth + "% gr");
        } else {
            PrimarySaleGR.setText("0% gr");
        }
        if (!achie.equals("0")) {
            PrimarySaleAch.setText(achie + "% Ach");
        } else {
            PrimarySaleAch.setText("0% Ach");
        }
        //-------------------------------------------------------------------------------------------------------------//
        //Card -2
        if (SelectedTD == 0) {
            tv_secsale_label.setText("Sec Sales (Prev Month)");
        } else {
            tv_secsale_label.setText("Sec Sales");
        }

        if (!SecSale.equals("0")) {
            Double secsales=Double.parseDouble(SecSale);
            SecSaleAmount.setText("₹ " + String.format("%.3f", secsales));
        } else {
            SecSaleAmount.setText("₹ 0");
        }
        if (!PSecSale.equals("0")) {
            SecSaleTarget.setText(PSecSale + "% of Primary Sales");
        } else {
            SecSaleTarget.setText("0% of Primary Sales");
        }
        //-------------------------------------------------------------------------------------------------------------//
        //Card -3
        if (!Rcpa.equals("0")) {
            Double rcpa = Double.parseDouble(Rcpa);
            RCPAAmount.setText("₹ " + String.format("%.3f", rcpa) + "L");
        } else {
            RCPAAmount.setText("₹ 0" + "L");
        }
        if (!Rcpa_Growth.equals("0")) {
            RCPA_GR.setText(Rcpa_Growth + "% of Primary Sales");
        } else {
            RCPA_GR.setText("0% of Primary Sales");
        }
        if (!PC.equals("0")) {
            Double pc = Double.parseDouble(PC);
            PCPMAmount.setText("₹ " + String.format("%.3f", pc));
        } else {
            PCPMAmount.setText("₹ 0");
        }
//        if (!PC.equals("0")) {
//            PCPMAmount.setText("₹ " + PC);
//        } else {
//            PCPMAmount.setText("₹ 0");
//        }
        if (!PCGrowth.equals("0")) {
            PCPMTarget.setText(PCGrowth + "% of Growth");
        } else {
            PCPMTarget.setText("0% of Growth");
        }
    }

    public void ShowSalesSummaryMCLChartChart(String Drlst, String Drmet, String DrCov) {
        ArrayList<Integer> colors = new ArrayList<>();
        String mDrCov = DrCov;
        if (mDrCov.contains("-")) {
            Float F1 = Float.parseFloat(mDrCov);
            if (F1 > 100) {
                mDrCov = "100";
            } else if (F1 < -100) {
                mDrCov = "100";
            } else {
                mDrCov = mDrCov.replace("-", "");
            }
            colors.add(Color.rgb(255, 0, 0));
            colors.add(Color.rgb(229, 231, 233));
        } else {
            Float F1 = Float.parseFloat(mDrCov);
            if (F1 > 100) {
                mDrCov = "100";
            }
            colors.add(Color.rgb(0, 128, 0));
            colors.add(Color.rgb(229, 231, 233));
        }
        Float countdata1 = Float.parseFloat(mDrCov);
        Float countdata2 = Float.parseFloat(DrCov);
        String centertxt = String.format("%.2f", countdata2);
        Float seconddata = 100 - countdata1;
        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        PieEntry pieEntry = new PieEntry(countdata1, "");
        values.add(pieEntry);
        PieEntry pieEntry1 = new PieEntry(seconddata, "");
        values.add(pieEntry1);

/*ArrayList<Integer> colors = new ArrayList<>();
colors.add(Color.rgb(0, 128, 0));
colors.add(Color.rgb(229, 231, 233));

Float OverallCount = Float.parseFloat(Drlst);
Float CompletedCount = Float.parseFloat(Drmet);
Float mCentreText = Float.parseFloat(DrCov);

String centertxt = String.format("%.2f", mCentreText);
Float seconddata = OverallCount - CompletedCount;
ArrayList<PieEntry> values = new ArrayList<PieEntry>();
PieEntry pieEntry = new PieEntry(CompletedCount, "");
values.add(pieEntry);
PieEntry pieEntry1 = new PieEntry(seconddata, "");
values.add(pieEntry1);*/

        MCL_pieDataSet = new PieDataSet(values, "");
        MCL_pieData = new PieData(MCL_pieDataSet);
        MCL_pieData.setValueFormatter(new PercentFormatter());
        MCL_pieGraph.setData(MCL_pieData);
        MCL_pieGraph.setUsePercentValues(true);
        MCL_pieGraph.setDrawHoleEnabled(true);
        MCL_pieGraph.setCenterTextSize(15f);
        MCL_pieGraph.setCenterTextColor(Color.rgb(0, 0, 0));
        MCL_pieGraph.setTransparentCircleRadius(40f);
        MCL_pieGraph.setHoleRadius(72f);
        MCL_pieDataSet.setColors(colors);
        MCL_pieData.setValueTextSize(0f);
        MCL_pieData.setValueTextColor(Color.WHITE);
        MCL_pieGraph.animateXY(1400, 1400);

        MCL_pieGraph.setCenterText(DrCov + "%");
        MCL_pieGraph.setCenterTextSize(15);
        Description description = MCL_pieGraph.getDescription();
        description.setEnabled(false);
        Legend legend = MCL_pieGraph.getLegend();
        legend.setEnabled(false);
        if (!Drlst.equals("0")) {
            MCL_TextPieChart.setText("Total Doctors : " + Drlst);
        } else {
            MCL_TextPieChart.setText("Total Doctors : 0");
        }
    }

    public void ShowSalesSummaryChemChartChart(String ChemLst, String ChemMet, String ChemCov) {
        ArrayList<Integer> colors = new ArrayList<>();
        String mChemCov = ChemCov;
        if (mChemCov.contains("-")) {
            Float F1 = Float.parseFloat(mChemCov);
            if (F1 > 100) {
                mChemCov = "100";
            } else if (F1 < -100) {
                mChemCov = "100";
            } else {
                mChemCov = mChemCov.replace("-", "");
            }
            colors.add(Color.rgb(255, 0, 0));
            colors.add(Color.rgb(229, 231, 233));
        } else {
            Float F1 = Float.parseFloat(mChemCov);
            if (F1 > 100) {
                mChemCov = "100";
            }
            colors.add(Color.rgb(0, 0, 255));
            colors.add(Color.rgb(229, 231, 233));
        }

        Float OverallCount = Float.parseFloat(ChemLst);
        Float CompletedCount = Float.parseFloat(ChemMet);
        Float mCentreText = Float.parseFloat(mChemCov);

        String centertxt = String.format("%.2f", mCentreText);
        Float seconddata = OverallCount - CompletedCount;
        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        PieEntry pieEntry = new PieEntry(CompletedCount, "");
        values.add(pieEntry);
        PieEntry pieEntry1 = new PieEntry(seconddata, "");
        values.add(pieEntry1);

        CHM_pieDataSet = new PieDataSet(values, "");
        CHM_pieData = new PieData(CHM_pieDataSet);
        CHM_pieData.setValueFormatter(new PercentFormatter());
        CHM_pieGraph.setData(CHM_pieData);
        CHM_pieGraph.setUsePercentValues(true);
        CHM_pieGraph.setDrawHoleEnabled(true);
        CHM_pieGraph.setCenterTextSize(15f);
        CHM_pieGraph.setCenterTextColor(Color.rgb(0, 0, 0));
        CHM_pieGraph.setTransparentCircleRadius(40f);
        CHM_pieGraph.setHoleRadius(72f);
        CHM_pieDataSet.setColors(colors);
        CHM_pieData.setValueTextSize(0f);
        CHM_pieData.setValueTextColor(Color.WHITE);
        CHM_pieGraph.animateXY(1400, 1400);

        CHM_pieGraph.setCenterText(ChemCov + "%");
        CHM_pieGraph.setCenterTextSize(15);
        Description description = CHM_pieGraph.getDescription();
        description.setEnabled(false);
        Legend legend = CHM_pieGraph.getLegend();
        legend.setEnabled(false);
        if (!ChemLst.equals("0")) {
            CHM_TextPieChart.setText("Total Chemist : " + ChemLst);
        } else {
            CHM_TextPieChart.setText("Total Chemist : 0");
        }
    }

    public void ShowCalender() {
        PopUpCalender.setContentView(R.layout.activity_date_selector);
        TV_FromDate = PopUpCalender.findViewById(R.id.TV_FromDate);
        TV_ToDate = PopUpCalender.findViewById(R.id.TV_ToDate);
        ImageView Cal1 = PopUpCalender.findViewById(R.id.btn_cal1);
        ImageView Cal2 = PopUpCalender.findViewById(R.id.btn_cal2);
        Button Ok = PopUpCalender.findViewById(R.id.btn_ok);
        Button Close = PopUpCalender.findViewById(R.id.btn_close);

        TV_FromDate.setTextColor(Color.GRAY);
        TV_ToDate.setTextColor(Color.GRAY);

        TV_FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalenderFromDate();
            }
        });

        TV_ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TV_FromDate.getText().toString().equals("--Select Date--")) {
                    Toast.makeText(Sales_report_new.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    CalenderToDate();
                }
            }
        });

        Cal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalenderFromDate();
            }
        });

        Cal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TV_FromDate.getText().toString().equals("--Select Date--")) {
                    Toast.makeText(Sales_report_new.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    CalenderToDate();
                }
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TV_FromDate.getText().toString().equals("--Select Date--")) {
                    Toast.makeText(Sales_report_new.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else if (TV_ToDate.getText().toString().equals("--Select Date--")) {
                    Toast.makeText(Sales_report_new.this, "Select To Date", Toast.LENGTH_SHORT).show();
                } else {
                    SelectedTD = 3;
                    IsDateFilter = 1;
                    TV_DisplayDate.setText(TV_FromDate.getText().toString() + " To " + TV_ToDate.getText().toString());
                    Dcrdatas.primarysalesselectedtdtag = SelectedTD;
                    PopUpCalender.dismiss();
                    if(isNetworkConnected()) {
                        GetPrimarySalesReportData();
                        if (!sf_type.equalsIgnoreCase("1")) {
                            GetMvdReportData();
                        }
                    }
                    else
                    {
                        Toast.makeText(Sales_report_new.this,getResources().getString(R.string.offline),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpCalender.dismiss();
            }
        });

        WindowManager.LayoutParams lp = PopUpCalender.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        PopUpCalender.getWindow().setAttributes(lp);
        PopUpCalender.setCancelable(false);
        PopUpCalender.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        PopUpCalender.show();
    }

    public void CalenderFromDate() {
        final Calendar c = Calendar.getInstance();
        final int y = c.get(Calendar.YEAR);
        final int m = c.get(Calendar.MONTH) + 1;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sales_report_new.this);
        LayoutInflater factory = LayoutInflater.from(Sales_report_new.this);
        final View vieww = factory.inflate(R.layout.monthyearpicker, null);
        alertDialog.setView(vieww);
        MonthPicker = vieww.findViewById(R.id.picker_month);
        YearPicker = vieww.findViewById(R.id.picker_year);
        MonthPicker.setMaxValue(12);
        MonthPicker.setMinValue(1);
        MonthPicker.setDisplayedValues(new String[]{"Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"});
        YearPicker.setMinValue(2000);
        YearPicker.setMaxValue(2500);
        MonthPicker.setValue(m);
        YearPicker.setValue(y);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String months = String.valueOf(MonthPicker.getValue());
                String years = String.valueOf(YearPicker.getValue());
                Dcrdatas.select_month = months;
                SetSelectedFromDate(months, years);
                int fm = Integer.parseInt(Fmonth);
                int fy = Integer.parseInt(Fyear);
//                if (fy > y) {
//                    TV_FromDate.setTextColor(Color.GRAY);
//                    TV_FromDate.setText("--Select Date--");
//                    Toast.makeText(Sales_report_new.this, "Selected Year Greater than Current Year", Toast.LENGTH_SHORT).show();
//                } else if ((y <= fy) && (fm > m)) {
//                    TV_FromDate.setTextColor(Color.GRAY);
//                    TV_FromDate.setText("--Select Date--");
//                    Toast.makeText(Sales_report_new.this, "Selected Month Greater than Current Month", Toast.LENGTH_SHORT).show();
//                } else {
                    dialog.dismiss();
              //  }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void CalenderToDate() {
        final Calendar c = Calendar.getInstance();
        final int y = c.get(Calendar.YEAR);
        final int m = c.get(Calendar.MONTH) + 1;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sales_report_new.this);
        LayoutInflater factory = LayoutInflater.from(Sales_report_new.this);
        final View vieww = factory.inflate(R.layout.monthyearpicker, null);
        alertDialog.setView(vieww);
        MonthPicker = vieww.findViewById(R.id.picker_month);
        YearPicker = vieww.findViewById(R.id.picker_year);
        MonthPicker.setMaxValue(12);
        MonthPicker.setMinValue(1);
        MonthPicker.setDisplayedValues(new String[]{"Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"});
        YearPicker.setMinValue(2000);
        YearPicker.setMaxValue(2500);
        MonthPicker.setValue(m);
        YearPicker.setValue(y);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String months = String.valueOf(MonthPicker.getValue());
                String years = String.valueOf(YearPicker.getValue());
                Dcrdatas.select_month = months;
                SetSelectedToDate(months, years);
                int fm = Integer.parseInt(Fmonth);
                int tm = Integer.parseInt(Tmonth);
                int fy = Integer.parseInt(Fyear);
                int ty = Integer.parseInt(Tyear);
                if (ty < fy) {
                    TV_ToDate.setTextColor(Color.GRAY);
                    TV_ToDate.setText("--Select Date--");
                    Toast.makeText(Sales_report_new.this, "Selected Year Less than From Year", Toast.LENGTH_SHORT).show();
//                } else if (ty > y) {
//                    TV_ToDate.setTextColor(Color.GRAY);
//                    TV_ToDate.setText("--Select Date--");
//                    Toast.makeText(Sales_report_new.this, "Selected Year Greater than Current Year", Toast.LENGTH_SHORT).show();
                } else if ((ty == fy) && (tm < fm)) {
                    TV_ToDate.setTextColor(Color.GRAY);
                    TV_ToDate.setText("--Select Date--");
                    Toast.makeText(Sales_report_new.this, "Selected Month Less than From Month", Toast.LENGTH_SHORT).show();
//                } else if ((ty == fy) && (fm > m)) {
//                    TV_ToDate.setTextColor(Color.GRAY);
//                    TV_FromDate.setText("--Select Date--");
//                    Toast.makeText(Sales_report_new.this, "Selected Month Greater than Current Month", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public String SetSelectedFromDate(String Str_month, String Str_year) {
        Fmonth = Str_month;
        Fyear = Str_year;
        SimpleDateFormat fmt = new SimpleDateFormat("M");
        Date date = null;
        try {
            date = fmt.parse(Str_month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM");
        TV_FromDate.setText(fmtOut.format(date) + " - " + Str_year);
        TV_FromDate.setTextColor(Color.BLACK);
        return fmtOut.format(date);
    }

    public String SetSelectedToDate(String Str_month, String Str_year) {
        Tmonth = Str_month;
        Tyear = Str_year;
        SimpleDateFormat fmt = new SimpleDateFormat("M");
        Date date = null;
        try {
            date = fmt.parse(Str_month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM");
        TV_ToDate.setText(fmtOut.format(date) + " - " + Str_year);
        TV_ToDate.setTextColor(Color.BLACK);
        return fmtOut.format(date);
    }

//    public void check_indata() {
//        try {
//            String curval = null;
//            List<CustomerMe> CustomerMeList;
//            sqlLite sqlLite1 = new sqlLite(Sales_report_new.this);
//            Cursor cursor = sqlLite1.getAllLoginData();
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
//            Api_Interface apiService = ApiClient.getClient(Sales_report_new.this).create(Api_Interface.class);
//            Map<String, String> QryParam = new HashMap<>();
//            QryParam.put("axn", "checkin_details");
//            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//            QryParam.put("sfCode", Shared_Common_Pref.getsfcodeFromSP(Sales_report_new.this));
//            QryParam.put("rSF", CustomerMeList.get(0).getSFCode());
//            QryParam.put("username", CustomerMeList.get(0).getUsername());
//            Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Sales_report_new.this).getBaseurl(), QryParam);
//            Log.d("edit Request", String.valueOf(QryParam));
//            call.enqueue(new Callback<JsonArray>() {
//                @Override
//                public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
//                    Log.d("block:Code", response.code() + " - " + response.toString());
//                    if (response.code() == 200 || response.code() == 201) {
//                        Log.d("todaycall:Res", response.body().toString());
//                        String resp = response.body().toString();
//                        try {
//                            String dataa = "";
//                            String time = "";
//                            JSONArray jsonArray = new JSONArray(response.body().toString());
//                            for (int k = 0; k < jsonArray.length(); k++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(k);
//                                dataa = jsonObject.optString("status");
//                                time = jsonObject.optString("Start_Time");
//                                Log.d("dataas", dataa);
//                            }
//                            checkintxt.setText(time);
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonArray> call, Throwable t) {
//                    Log.d("mydayplan:Failure", t.toString());
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void check_outdata(String date, String s_val) {
//        Log.d("check_date",date +"*-*"+s_val);
//        LocationManager manager = (LocationManager) Sales_report_new.this.getSystemService( Context.LOCATION_SERVICE );
//        if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER)){
//            if (Shared_Common_Pref.getGeoChkFromSP(Sales_report_new.this).equals("0")) {
//                enableGPS();
////                locationfinder();
//            }
//        }else{
//            if (ContextCompat.checkSelfPermission(Sales_report_new.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) Sales_report_new.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    ActivityCompat.requestPermissions((Activity) Sales_report_new.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                } else {
//                    ActivityCompat.requestPermissions((Activity) Sales_report_new.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                }
//            } else {
//                mLocationManager = (LocationManager) Sales_report_new.this.getSystemService(Sales_report_new.this.LOCATION_SERVICE);
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ( LocationListener ) Sales_report_new.this);
//                mLocationManager = (LocationManager) Sales_report_new.this.getSystemService(Sales_report_new.this.LOCATION_SERVICE);
//                criteria = new Criteria();
//                bestProvider = String.valueOf(mLocationManager.getBestProvider(criteria, true)).toString();
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//                    boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                    if (isGPSEnabled) {
//                        LocationManager locationManager = (LocationManager) Sales_report_new.this.getSystemService(Context.LOCATION_SERVICE);
//                        try {
//                            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        double lat;
//                        double longi;
//                        String addressText = "";
//                        ;
//                        if (gps_loc != null) {
//                            final_loc = gps_loc;
//                            lat = final_loc.getLatitude();
//                            longi = final_loc.getLongitude();
//                        } else {
//                            final_loc = network_loc;
//                            lat = final_loc.getLatitude();
//                            longi = final_loc.getLongitude();
//                        }
//                        try {
//                            Geocoder geocoder = new Geocoder(Sales_report_new.this, Locale.getDefault());
//                            List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
//                            if (addresses != null && addresses.size() > 0) {
//                                addressText = addresses.get(0).getAddressLine(0);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        String latitude = String.valueOf(lat);
//                        String longitude = String.valueOf(longi);
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String currentDateandTime = sdf.format(new Date());
////                        sqlLite sqlLite1;
////                        String curval = null;
////                        List<CustomerMe> CustomerMeList;
////                        sqlLite1 = new sqlLite(Sales_report_new.this);
////                        Cursor cursor = sqlLite1.getAllLoginData();
////                        if (cursor.moveToFirst()) {
////                            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
////                        }
////                        cursor.close();
////                        Gson gson = new Gson();
////                        Type type = new TypeToken<List<CustomerMe>>() {
////                        }.getType();
////                        CustomerMeList = gson.fromJson(curval, type);
//                        JSONObject jobject = new JSONObject();
//                        try {
//                            if (latitude.equals("") || longitude.equals("")) {
//                                jobject.put("lat", "");
//                                jobject.put("long", "");
//                                jobject.put("address", "");
//                            } else {
//                                jobject.put("lat", latitude);
//                                jobject.put("long", longitude);
//                                jobject.put("address", addressText);
//                            }
//                            DisplayLatitude=latitude;
//                            DisplayLongitude=longitude;
//                            DisplayAddress=addressText;
//                            if (s_val.equals("1")){
//                                jobject.put("time", date);
//                            }else {
//                                jobject.put("time", currentDateandTime);
//                            }
//                            JSONObject jobject1 = new JSONObject();
//                            jobject1.put("TP_Attendance", jobject);
//                            JSONArray jsonarray = new JSONArray();
//                            jsonarray.put(jobject1);
//                            Log.d("tp_attendance", jsonarray.toString());
//                            Api_Interface apiService = ApiClient.getClient(Sales_report_new.this).create(Api_Interface.class);
//                            Map<String, String> QryParam = new HashMap<>();
//                            QryParam.put("axn", "dcr/save");
//                            QryParam.put("update", "1");
//                            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//                            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
//                            QryParam.put("username", CustomerMeList.get(0).getUsername());
//                            Call<JsonArray> Callto;
//                            Callto = apiService.getDataAsJArray(AppConfig.getInstance(Sales_report_new.this).getBaseurl(), QryParam, jsonarray.toString());
//                            Log.d("QryParam", QryParam.toString());
//                            if (isNetworkConnected()) {
//                                Callto.enqueue(new Callback<JsonArray>() {
//                                    @Override
//                                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                                        Log.d("Stock_Success", String.valueOf(response.body()));
//                                        Log.d("Stock_successvalue", response.toString());
//                                        if (response.code() == 200 || response.code() == 201) {
//                                            Log.d("Stock_call:Res", response.body().toString());
//                                            if (s_val.equals("1")){
//                                                Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }else {
//                                                String one = "1";
//                                                SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                                editor.putString(Name, one);
//                                                editor.commit();
//                                                if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                    DisplayLocation();
//                                                } else {
//                                                    Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                    startActivity(l);
//                                                    Sales_report_new.this.finish();
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<JsonArray> call, Throwable t) {
//                                        Log.d("Checkin TAG", "onFailure : " + t.toString());
//                                        if (s_val.equals("1")){
//                                            Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                            startActivity(l);
//                                            Sales_report_new.this.finish();
//                                        }else {
//                                            String one = "1";
//                                            SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                            editor.putString(Name, one);
//                                            editor.commit();
//                                            if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                DisplayLocation();
//                                            } else {
//                                                Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }
//                                        }
//                                    }
//                                });
//                            } else {
//                                sqlLite.SaveEntryToLocal("Daycheckoutoffline", jsonarray.toString(), QryParam.toString(), "sending..");
//                                Toast.makeText(Sales_report_new.this, resources.getString(R.string.localysave), Toast.LENGTH_SHORT).show();
//                                if (s_val.equals("1")){
//                                    Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                    startActivity(l);
//                                    Sales_report_new.this.finish();
//                                }else {
//                                    String one = "1";
//                                    SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                    editor.putString(Name, one);
//                                    editor.commit();
//                                    if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                        DisplayLocation();
//                                    } else {
//                                        Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                        startActivity(l);
//                                        Sales_report_new.this.finish();
//                                    }
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String currentDateandTime = sdf.format(new Date());
//                        sqlLite sqlLite1;
//                        String curval = null;
//                        List<CustomerMe> CustomerMeList;
//                        sqlLite1 = new sqlLite(Sales_report_new.this);
//                        Cursor cursor = sqlLite1.getAllLoginData();
//                        if (cursor.moveToFirst()) {
//                            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//                        }
//                        cursor.close();
//                        Gson gson = new Gson();
//                        Type type = new TypeToken<List<CustomerMe>>() {
//                        }.getType();
//                        CustomerMeList = gson.fromJson(curval, type);
//                        JSONObject jobject = new JSONObject();
//                        try {
//                            jobject.put("lat", "");
//                            jobject.put("long", "");
//                            jobject.put("address", "");
//
//                            if (s_val.equals("1")){
//                                jobject.put("time", date);
//                            }else {
//                                jobject.put("time", currentDateandTime);
//                            }
//                            JSONObject jobject1 = new JSONObject();
//                            jobject1.put("TP_Attendance", jobject);
//                            JSONArray jsonarray = new JSONArray();
//                            jsonarray.put(jobject1);
//                            Log.d("tp_attendance", jsonarray.toString());
//
//                            Api_Interface apiService = ApiClient.getClient(Sales_report_new.this).create(Api_Interface.class);
//                            Map<String, String> QryParam = new HashMap<>();
//                            QryParam.put("axn", "dcr/save");
//                            QryParam.put("update", "1");
//                            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//                            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
//                            QryParam.put("username", CustomerMeList.get(0).getUsername());
//                            Call<JsonArray> Callto;
//                            Callto = apiService.getDataAsJArray(AppConfig.getInstance(Sales_report_new.this).getBaseurl(), QryParam, jsonarray.toString());
//                            if (isNetworkConnected()) {
//                                Callto.enqueue(new Callback<JsonArray>() {
//                                    @Override
//                                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//
//                                        Log.d("Stock_Success", String.valueOf(response.body()));
//                                        Log.d("Stock_successvalue", response.toString());
//                                        if (response.code() == 200 || response.code() == 201) {
//                                            Log.d("Stock_call:Res", response.body().toString());
//                                            if (s_val.equals("1")){
//                                                Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }else {
//                                                String one = "1";
//                                                SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                                editor.putString(Name, one);
//                                                editor.commit();
//                                                if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                    DisplayLocation();
//                                                } else {
//                                                    Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                    startActivity(l);
//                                                    Sales_report_new.this.finish();
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<JsonArray> call, Throwable t) {
//                                        Log.d("Checkin TAG", "onFailure : " + t.toString());
//                                        if (s_val.equals("1")){
//                                            Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                            startActivity(l);
//                                            Sales_report_new.this.finish();
//                                        }else {
//                                            String one = "1";
//                                            SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                            editor.putString(Name, one);
//                                            editor.commit();
//                                            if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                DisplayLocation();
//                                            } else {
//                                                Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }
//                                        }
//                                    }
//                                });
//                            } else {
//                                sqlLite.SaveEntryToLocal("Daycheckoutoffline", jsonarray.toString(), QryParam.toString(), "sending..");
//                                Toast.makeText(Sales_report_new.this, resources.getString(R.string.localysave), Toast.LENGTH_SHORT).show();
//                                if (s_val.equals("1")){
//                                    Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                    startActivity(l);
//                                    Sales_report_new.this.finish();
//                                }else {
//                                    String one = "1";
//                                    SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                    editor.putString(Name, one);
//                                    editor.commit();
//                                    if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                        DisplayLocation();
//                                    } else {
//                                        Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                        startActivity(l);
//                                        Sales_report_new.this.finish();
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    locationGPS = mLocationManager.getLastKnownLocation(bestProvider);
//                    if (locationGPS != null) {
//                        Log.d("lattit", String.valueOf(locationGPS.getLatitude()));
//                        double lat = locationGPS.getLatitude();
//                        double longi = locationGPS.getLongitude();
//                        String latitude = String.valueOf(lat);
//                        String longitude = String.valueOf(longi);
//                        String addressText = "";
//                        Log.d("lattit", latitude);
//                        Geocoder geocoder = new Geocoder(Sales_report_new.this, Locale.getDefault());
//                        if (latitude.equals("")) {
//                        } else {
//                            try {
//                                List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
//                                if (addresses.size() == 0) {
//                                    addressText = "";
//                                } else {
//                                    addressText = addresses.get(0).getAddressLine(0);
//                                    DisplayAddress = addressText.toString();
//                                    DisplayLatitude = latitude.toString();
//                                    DisplayLongitude = longitude.toString();
//                                }
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//
//                            }
//                        }
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String currentDateandTime = sdf.format(new Date());
//                        sqlLite sqlLite1;
//                        String curval = null;
//                        List<CustomerMe> CustomerMeList;
//                        sqlLite1 = new sqlLite(Sales_report_new.this);
//                        Cursor cursor = sqlLite1.getAllLoginData();
//                        if (cursor.moveToFirst()) {
//                            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//                        }
//                        cursor.close();
//                        Gson gson = new Gson();
//                        Type type = new TypeToken<List<CustomerMe>>() {
//                        }.getType();
//                        CustomerMeList = gson.fromJson(curval, type);
//                        JSONObject jobject = new JSONObject();
//                        try {
//                            if (latitude.equals("") || longitude.equals("")) {
//                                jobject.put("lat", "");
//                                jobject.put("long", "");
//                                jobject.put("address", "");
//                            } else {
//                                jobject.put("lat", latitude);
//                                jobject.put("long", longitude);
//                                jobject.put("address", addressText);
//                            }
//                            if (s_val.equals("1")){
//                                jobject.put("time", date);
//                            }else {
//                                jobject.put("time", currentDateandTime);
//                            }
//                            JSONObject jobject1 = new JSONObject();
//                            jobject1.put("TP_Attendance", jobject);
//                            JSONArray jsonarray = new JSONArray();
//                            jsonarray.put(jobject1);
//                            Log.d("tp_attendance", jsonarray.toString());
//                            Api_Interface apiService = ApiClient.getClient(Sales_report_new.this).create(Api_Interface.class);
//                            Map<String, String> QryParam = new HashMap<>();
//                            QryParam.put("axn", "dcr/save");
//                            QryParam.put("update", "1");
//                            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//                            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
//                            QryParam.put("username", CustomerMeList.get(0).getUsername());
//                            Call<JsonArray> Callto;
//                            Callto = apiService.getDataAsJArray(AppConfig.getInstance(Sales_report_new.this).getBaseurl(), QryParam, jsonarray.toString());
//                            Log.d("QryParam", QryParam.toString());
//                            if (isNetworkConnected()) {
//                                Callto.enqueue(new Callback<JsonArray>() {
//                                    @Override
//                                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                                        Log.d("Stock_Success", String.valueOf(response.body()));
//                                        Log.d("Stock_successvalue", response.toString());
//                                        if (response.code() == 200 || response.code() == 201) {
//                                            Log.d("Stock_call:Res", response.body().toString());
//                                            if (s_val.equals("1")){
//                                                Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }else {
//                                                String one = "1";
//                                                SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                                editor.putString(Name, one);
//                                                editor.commit();
//                                                if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                    DisplayLocation();
//                                                } else {
//                                                    Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                    startActivity(l);
//                                                    Sales_report_new.this.finish();
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<JsonArray> call, Throwable t) {
//                                        Log.d("Checkin TAG", "onFailure : " + t.toString());
//                                        if (s_val.equals("1")){
//                                            Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                            startActivity(l);
//                                            Sales_report_new.this.finish();
//                                        }else {
//                                            String one = "1";
//                                            SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                            editor.putString(Name, one);
//                                            editor.commit();
//                                            if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                DisplayLocation();
//                                            } else {
//                                                Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }
//                                        }
//                                    }
//                                });
//                            } else {
//                                sqlLite.SaveEntryToLocal("Daycheckoutoffline", jsonarray.toString(), QryParam.toString(), "sending..");
//                                Toast.makeText(Sales_report_new.this,  resources.getString(R.string.localysave), Toast.LENGTH_SHORT).show();
//                                if (s_val.equals("1")){
//                                    Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                    startActivity(l);
//                                    Sales_report_new.this.finish();
//                                }else {
//                                    String one = "1";
//                                    SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                    editor.putString(Name, one);
//                                    editor.commit();
//                                    if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                        DisplayLocation();
//                                    } else {
//                                        Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                        startActivity(l);
//                                        Sales_report_new.this.finish();
//                                    }
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String currentDateandTime = sdf.format(new Date());
//                        sqlLite sqlLite1;
//                        String curval = null;
//                        List<CustomerMe> CustomerMeList;
//                        sqlLite1 = new sqlLite(Sales_report_new.this);
//                        Cursor cursor = sqlLite1.getAllLoginData();
//                        if (cursor.moveToFirst()) {
//                            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//                        }
//                        cursor.close();
//                        Gson gson = new Gson();
//                        Type type = new TypeToken<List<CustomerMe>>() {
//                        }.getType();
//                        CustomerMeList = gson.fromJson(curval, type);
//                        JSONObject jobject = new JSONObject();
//                        try {
//                            jobject.put("lat", "");
//                            jobject.put("long", "");
//                            jobject.put("address", "");
//
//                            if (s_val.equals("1")){
//                                jobject.put("time", date);
//                            }else {
//                                jobject.put("time", currentDateandTime);
//                            }
//                            JSONObject jobject1 = new JSONObject();
//                            jobject1.put("TP_Attendance", jobject);
//                            JSONArray jsonarray = new JSONArray();
//                            jsonarray.put(jobject1);
//                            Log.d("tp_attendance", jsonarray.toString());
//
//                            Api_Interface apiService = ApiClient.getClient(Sales_report_new.this).create(Api_Interface.class);
//                            Map<String, String> QryParam = new HashMap<>();
//                            QryParam.put("axn", "dcr/save");
//                            QryParam.put("update", "1");
//                            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//                            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
//                            QryParam.put("username", CustomerMeList.get(0).getUsername());
//                            Call<JsonArray> Callto;
//                            Callto = apiService.getDataAsJArray(AppConfig.getInstance(Sales_report_new.this).getBaseurl(), QryParam, jsonarray.toString());
//                            if (isNetworkConnected()) {
//                                Callto.enqueue(new Callback<JsonArray>() {
//                                    @Override
//                                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//
//                                        Log.d("Stock_Success", String.valueOf(response.body()));
//                                        Log.d("Stock_successvalue", response.toString());
//                                        if (response.code() == 200 || response.code() == 201) {
//                                            Log.d("Stock_call:Res", response.body().toString());
//                                            if (s_val.equals("1")){
//                                                Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }else {
//                                                String one = "1";
//                                                SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                                editor.putString(Name, one);
//                                                editor.commit();
//                                                if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                    DisplayLocation();
//                                                } else {
//                                                    Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                    startActivity(l);
//                                                    Sales_report_new.this.finish();
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<JsonArray> call, Throwable t) {
//                                        Log.d("Checkin TAG", "onFailure : " + t.toString());
//                                        if (s_val.equals("1")){
//                                            Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                            startActivity(l);
//                                            Sales_report_new.this.finish();
//                                        }else {
//                                            String one = "1";
//                                            SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                            editor.putString(Name, one);
//                                            editor.commit();
//                                            if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                                DisplayLocation();
//                                            } else {
//                                                Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                                startActivity(l);
//                                                Sales_report_new.this.finish();
//                                            }
//                                        }
//                                    }
//                                });
//                            } else {
//                                sqlLite.SaveEntryToLocal("Daycheckoutoffline", jsonarray.toString(), QryParam.toString(), "sending..");
//                                Toast.makeText(Sales_report_new.this, resources.getString(R.string.localysave), Toast.LENGTH_SHORT).show();
//                                if (s_val.equals("1")){
//                                    Intent l = new Intent(Sales_report_new.this , Main2Activity.class);
//                                    startActivity(l);
//                                    Sales_report_new.this.finish();
//                                }else {
//                                    String one = "1";
//                                    SharedPreferences.Editor editor = sharedpreferencess.edit();
//                                    editor.putString(Name, one);
//                                    editor.commit();
//                                    if ((!DisplayAddress.equals("")) && ((!DisplayLatitude.equals("")) || (!DisplayLatitude.equals("0.0"))) && ((!DisplayLongitude.equals("")) || (!DisplayLongitude.equals("0.0")))) {
//                                        DisplayLocation();
//                                    } else {
//                                        Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                                        startActivity(l);
//                                        Sales_report_new.this.finish();
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }

//    public void enableGPS() {
//        final LocationManager manager = ( LocationManager ) Sales_report_new.this.getSystemService(Context.LOCATION_SERVICE);
//        if (isFineLocationPermissionGranted() && (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
//            showSettingsAlert();
//        } else {
//
//        }
//    }

//    public boolean isFineLocationPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (Sales_report_new.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                return true;
//            } else {
//
//                Log.v("Write_Etnl_Permission", "Permission is revoked");
//                ActivityCompat.requestPermissions(Sales_report_new.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL);
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = ( ConnectivityManager ) Sales_report_new.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

//    public void showSettingsAlert() {
//        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Sales_report_new.this);
//        alertDialog.setTitle( resources.getString(R.string.gps_setting));
//        alertDialog.setMessage( resources.getString(R.string.gps_notenable));
//        alertDialog.setPositiveButton( resources.getString(R.string.setting), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        });
//        alertDialog.setNegativeButton( resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        alertDialog.show();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int tagID = 0;
        Dcrdatas dcrval = Dcrdatas.getInstance();
        switch (item.getItemId()) {
            case R.id.home:
                Intent o = new Intent(this.getApplicationContext(), HomeDashBoard.class);
                startActivity(o);
                break;

            case R.id.sfe:
                Intent oo = new Intent(this.getApplicationContext(), SFe_Activity.class);
                startActivity(oo);
                break;
        }
//        if (tagID > 0) {
//            Intent i = new Intent(Sales_report_new.this, DCREntryActivity.class);
//            i.putExtra("tagID", tagID);
//            startActivity(i);
//            Sales_report_new.this.finish();
//        }
        DrawerLayout drawer = ( DrawerLayout )findViewById(R.id.drawer_layout143);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

//    public void checkoutconformation() {
//        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Sales_report_new.this);
//        alertDialog.setTitle(resources.getString(R.string.conform));
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage(resources.getString(R.string.wantcheckout));
//        alertDialog.setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                check_outdata("", "0");
//            }
//        });
//
//        alertDialog.setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String one = "1";
//                Intent l = new Intent(Sales_report_new.this, Login_Activity.class);
//                SharedPreferences.Editor editor = sharedpreferencess.edit();
//                editor.putString(Name, one);
//                editor.commit();
//                startActivity(l);
//                Sales_report_new.this.finish();
//            }
//        });
//
//        alertDialog.show();
//    }

//    public void DisplayLocation(){
//        PopupLocation.setContentView(R.layout.activity_display_location);
//        ImageView ok = PopupLocation.findViewById(R.id.buttonOk);
//        parentrel = (RelativeLayout)PopupLocation.findViewById(R.id.parentrel);
//        linear1 = (LinearLayout)PopupLocation.findViewById(R.id.linear1);
//        locationicon = ( GifImageView )PopupLocation.findViewById(R.id.locationicon);
//        linear2 = (LinearLayout)PopupLocation.findViewById(R.id.linear2);
//        tablecheckin = ( TableRow )PopupLocation.findViewById(R.id.tablecheckin);
//        labelcheckin = (TextView)PopupLocation.findViewById(R.id.labelcheckin);
//        labelcolon1 = (TextView)PopupLocation.findViewById(R.id.labelcolon1);
//        checkindetail = (TextView)PopupLocation.findViewById(R.id.checkindetail);
//        tablelatitude = (TableRow)PopupLocation.findViewById(R.id.tablelatitude);
//        labellatitude = (TextView)PopupLocation.findViewById(R.id.labellatitude);
//        labelcolon2 = (TextView)PopupLocation.findViewById(R.id.labelcolon2);
//        latitudedetail = (TextView)PopupLocation.findViewById(R.id.latitudedetail);
//        tablelongitude = (TableRow)PopupLocation.findViewById(R.id.tablelongitude);
//        labellongitude = (TextView)PopupLocation.findViewById(R.id.labellongitude);
//        labelcolon3 = (TextView)PopupLocation.findViewById(R.id.labelcolon3);
//        longitudedetail = (TextView)PopupLocation.findViewById(R.id.longitudedetail);
//        tableaddress = (TableRow)PopupLocation.findViewById(R.id.tableaddress);
//        labeladdress = (TextView)PopupLocation.findViewById(R.id.labeladdress);
//        labelcolon4 = (TextView)PopupLocation.findViewById(R.id.labelcolon4);
//        addressdetail = (TextView)PopupLocation.findViewById(R.id.addressdetail);
//
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String currentDateandTime = sdf1.format(new Date());
//
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
//        Date date = null;
//        try {
//            date = fmt.parse(currentDateandTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd yyyy hh:mmaa",Locale.getDefault());
//        String formattedDate = sdf2.format(date);
//
//        String Check = "<font color=#cc2311>" + formattedDate + "</font>";
//        String Lat = "<font color=#cc2311>" + DisplayLatitude + "</font>";
//        String Long = "<font color=#cc2311>" + DisplayLongitude + "</font>";
//
//        labelcheckin.setText("Check Out");
//        labellatitude.setText("Latitude");
//        labellongitude.setText("Longitude");
//        labeladdress.setText("Address");
//
//        checkindetail.setText(Html.fromHtml(Check));
//        latitudedetail.setText(Html.fromHtml(Lat));
//        longitudedetail.setText(Html.fromHtml(Long));
//        addressdetail.setText(DisplayAddress);
//
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(Sales_report_new.this,Login_Activity.class);
//                startActivity(i);
//                Sales_report_new.this.finish();
//            }
//        });
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i=new Intent(Sales_report_new.this,Login_Activity.class);
//                startActivity(i);
//                Sales_report_new.this.finish();
//            }
//        }, 3000);
//        anim = AnimationUtils.loadAnimation(Sales_report_new.this,R.anim.anim_rotate);
//        PopupLocation.findViewById(R.id.locationicon).startAnimation(anim);
//        WindowManager.LayoutParams lp = PopupLocation.getWindow().getAttributes();
//        lp.dimAmount=0.0f;
//        PopupLocation.getWindow().setAttributes(lp);
//        PopupLocation.setCancelable(false);
//        PopupLocation.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//        PopupLocation.show();
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
//                case R.id.btMyActivity:
//                    Dcrdatas.custlist = 2;
//                    Intent target = new Intent(Sales_report_new.this, Main2Activity.class);
//                    startActivity(target);
//                    return true;
//                case R.id.btMyResource:
//                    Dcrdatas.custlist = 1;
//                    Intent target1 = new Intent(Sales_report_new.this, Main2Activity.class);
//                    startActivity(target1);
//                    return true;
//                case R.id.btMyReports:
//                    Dcrdatas.custlist = 3;
//                    Intent target2 = new Intent(Sales_report_new.this, Main2Activity.class);
//                    startActivity(target2);
//                    return true;
//                case R.id.srhome:
//                    Dcrdatas.custlist = 4;
////                    mTitle.setText("Home");
//                    Dcrdatas.setTagId(29);
//                    Dcrdatas.tarcount = 1;
//                    Intent target3 = new Intent(Sales_report_new.this, DCREntryActivity.class);
//                    startActivity(target3);
//                    return true;
            }

            return false;
        }
    };

    public void ClearSharedPreference(String KeyValue) {
        SharedPreferences preferences = getSharedPreferences(KeyValue, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

        public void GetMvdReportData() {
            final ProgressDialog mProgressDialog = new ProgressDialog(Sales_report_new.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            try {
                apiService =RetroClient.getClient(db_connPath).create(Api_Interface.class);

//                sqlLite sqlLite;
//                sqlLite = new sqlLite(Sales_report_new.this);
//                Cursor cursor = sqlLite.getAllLoginData();
//                if (cursor.moveToFirst()) {
//                    curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//                }
//                cursor.close();
//                Gson gson = new Gson();
//                Type type = new TypeToken<List<CustomerMe>>() {
//                }.getType();
//                CustomerMeList = gson.fromJson(curval, type);
//                Log.d("ss_dashboard_1", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode() + " " + CustomerMeList.get(0).getSFName());

                Map<String, String> QryParam = new HashMap<>();
               // QryParam.put("axn", "get/MVD_coverage");
                QryParam.put("divisionCode", div_Code+",");
                QryParam.put("sfCode", sf_code);
                QryParam.put("rSF", sf_code);
                //For MTD :
                if (SelectedTD == 0) {
                    Date mDate = new Date();
                    SimpleDateFormat mFormat1_MTD = new SimpleDateFormat("MM");
                    SimpleDateFormat mFormat2_MTD = new SimpleDateFormat("MMM");
                    SimpleDateFormat mFormat3_MTD = new SimpleDateFormat("yyyy");
                    String CurrentMonth1_MTD = mFormat1_MTD.format(mDate);
                    String CurrentMonth2_MTD = mFormat2_MTD.format(mDate);
                    String CurrentYear_MTD = mFormat3_MTD.format(mDate);

                    FromMonth = CurrentMonth1_MTD;
                    FromYear = CurrentYear_MTD;
                    ToMonth = CurrentMonth1_MTD;
                    ToYear = CurrentYear_MTD;

                    QryParam.put("fmonth", FromMonth);
                    QryParam.put("fyear", FromYear);
                    QryParam.put("tomonth", ToMonth);
                    QryParam.put("toyear", ToYear);

                    TD_StartDate = CurrentMonth2_MTD + " " + CurrentYear_MTD;
                    TD_EndDate = CurrentMonth2_MTD + " " + CurrentYear_MTD;

                    Dcrdatas.TD_StartDate = TD_StartDate;
                    Dcrdatas.TD_EndDate = TD_EndDate;

                    Log.e("Sales_Dashboard_MTD", QryParam.toString() + "--" + TD_StartDate + "--" + TD_EndDate);
                }
                //For QTD :
                else if (SelectedTD == 1) {
                    try {
                        //From Date Format :
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(Calendar.DAY_OF_MONTH, 1);
                        cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) / 3 * 3);
                        Date FDate1 = cal1.getTime();

                        SimpleDateFormat SDF_1Format_1_QTD = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat SDF_1Format_2_QTD = new SimpleDateFormat("MM");
                        SimpleDateFormat SDF_1Format_3_QTD = new SimpleDateFormat("MMM");
                        SimpleDateFormat SDF_1Format_4_QTD = new SimpleDateFormat("yyyy");

                        String mStartDate = SDF_1Format_1_QTD.format(FDate1);
                        Date FormattedStartDate = SDF_1Format_1_QTD.parse(mStartDate);

                        String FromMonth_Format1_QTD = SDF_1Format_2_QTD.format(FormattedStartDate);
                        String FromMonth_Format2_QTD = SDF_1Format_3_QTD.format(FormattedStartDate);
                        String FromYear_Format_QTD = SDF_1Format_4_QTD.format(FormattedStartDate);

                        //To Date Format :
                        Date mDate = new Date();
                        SimpleDateFormat SDF_2Format_1_QTD = new SimpleDateFormat("MM");
                        SimpleDateFormat SDF_2Format_2_QTD = new SimpleDateFormat("MMM");
                        SimpleDateFormat SDF_2Format_3_QTD = new SimpleDateFormat("yyyy");

                        String ToMonth_Format1_QTD = SDF_2Format_1_QTD.format(mDate);
                        String ToMonth_Format2_QTD = SDF_2Format_2_QTD.format(mDate);
                        String ToYear_Format_QTD = SDF_2Format_3_QTD.format(mDate);

                        FromMonth = FromMonth_Format1_QTD;
                        FromYear = FromYear_Format_QTD;
                        ToMonth = ToMonth_Format1_QTD;
                        ToYear = ToYear_Format_QTD;

                        QryParam.put("fmonth", FromMonth);
                        QryParam.put("fyear", FromYear);
                        QryParam.put("tomonth", ToMonth);
                        QryParam.put("toyear", ToYear);

                        TD_StartDate = FromMonth_Format2_QTD + " " + FromYear_Format_QTD;
                        TD_EndDate = ToMonth_Format2_QTD + " " + ToYear_Format_QTD;

                        Dcrdatas.TD_StartDate = TD_StartDate;
                        Dcrdatas.TD_EndDate = TD_EndDate;

                        Log.e("Sales_Dashboard_QTD", QryParam.toString() + "--" + TD_StartDate + "--" + TD_EndDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //For YTD :
                else if (SelectedTD == 2) {
                    try {
                        //From Date Format :
                        int mmCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
                        int mmCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
                        String FinancialYearFrom = "";
                        if (mmCurrentMonth < 4) {
                            FinancialYearFrom = (mmCurrentYear - 1) + "-04-01";
                        } else {
                            FinancialYearFrom = (mmCurrentYear) + "-04-01";
                        }
                        String StartDate = FinancialYearFrom;

                        SimpleDateFormat SDF_1Format_1_YTD = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat SDF_1Format_2_YTD = new SimpleDateFormat("MM");
                        SimpleDateFormat SDF_1Format_3_YTD = new SimpleDateFormat("MMM");
                        SimpleDateFormat SDF_1Format_4_YTD = new SimpleDateFormat("yyyy");

                        Date FormattedStartDate = SDF_1Format_1_YTD.parse(StartDate);

                        String FromMonth_Format1_YTD = SDF_1Format_2_YTD.format(FormattedStartDate);
                        String FromMonth_Format2_YTD = SDF_1Format_3_YTD.format(FormattedStartDate);
                        String FromYear_Format_YTD = SDF_1Format_4_YTD.format(FormattedStartDate);

                        //To Date Format :
                        Date mDate = new Date();
                        SimpleDateFormat SDF_2Format_1_YTD = new SimpleDateFormat("MM");
                        SimpleDateFormat SDF_2Format_2_YTD = new SimpleDateFormat("MMM");
                        SimpleDateFormat SDF_2Format_3_YTD = new SimpleDateFormat("yyyy");

                        String ToMonth_Format1_YTD = SDF_2Format_1_YTD.format(mDate);
                        String ToMonth_Format2_YTD = SDF_2Format_2_YTD.format(mDate);
                        String ToYear_Format_YTD = SDF_2Format_3_YTD.format(mDate);

                        FromMonth = FromMonth_Format1_YTD;
                        FromYear = FromYear_Format_YTD;
                        ToMonth = ToMonth_Format1_YTD;
                        ToYear = ToYear_Format_YTD;

                        QryParam.put("fmonth", FromMonth);
                        QryParam.put("fyear", FromYear);
                        QryParam.put("tomonth", ToMonth);
                        QryParam.put("toyear", ToYear);

                        TD_StartDate = FromMonth_Format2_YTD + " " + FromYear_Format_YTD;
                        TD_EndDate = ToMonth_Format2_YTD + " " + ToYear_Format_YTD;

                        Dcrdatas.TD_StartDate = TD_StartDate;
                        Dcrdatas.TD_EndDate = TD_EndDate;

                        Log.e("Sales_Dashboard_YTD", QryParam.toString() + "--" + TD_StartDate + "--" + TD_EndDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (SelectedTD == 3 && IsDateFilter == 1) {
                    FromMonth = Fmonth;
                    FromYear = Fyear;
                    ToMonth = Tmonth;
                    ToYear = Tyear;

                    QryParam.put("fmonth", FromMonth);
                    QryParam.put("fyear", FromYear);
                    QryParam.put("tomonth", ToMonth);
                    QryParam.put("toyear", ToYear);

                    Log.e("Sales_Dashboard_DTD", QryParam.toString());
                }
                Call<JsonArray> call = apiService.getMVDDataAsJArray(QryParam);
                Log.d("ss_dashboard_3", QryParam.toString());
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("ss_dashboard_2",  response.body().toString());
                            Log.d("ss_dashboard_3", response.toString());
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            if (!response.body().toString().isEmpty()) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response.body().toString());
                                    if (jsonArray.length() > 0) {
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                                            String Sfcode = jsonObject.getString("Sf_Code");
                                            String ttl = jsonObject.getString("ttl");
                                            String met = jsonObject.getString("met");
                                            String seen = jsonObject.getString("seen");
                                            String msd = jsonObject.getString("msd");

                                            if (SelectedTD == 0) {
                                                ClearSharedPreference(sp_MVD_Report_MTDdata);
                                                Shared_Common_Pref.putMVDjsondata_MTD(Sales_report_new.this,response.body().toString());
                                            } else if (SelectedTD == 1) {
                                                ClearSharedPreference(sp_MVD_Report_QTDdata);
                                                Shared_Common_Pref.putMVDjsondata_QTD(Sales_report_new.this,response.body().toString());
                                            } else if (SelectedTD == 2) {
                                                ClearSharedPreference(sp_MVD_Report_YTDdata);
                                                Shared_Common_Pref.putMVDjsondata_YTD(Sales_report_new.this,response.body().toString());
                                            } else if (SelectedTD == 3) {
                                                ClearSharedPreference(sp_MVD_Report_DTDdata);
                                                Shared_Common_Pref.putMVDjsondata_DTD(Sales_report_new.this,response.body().toString());
                                            }
                                            ShowSalesSummaryMVDChartChart(ttl,met,seen,msd);
                                        }
                                    } else {
                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();
                                        ShowSalesSummaryMVDChartChart("0","0","0","0");
                                    }
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                            } else {
                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                                ShowSalesSummaryMVDChartChart("0","0","0","0");
                            }
                        } else {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            ShowSalesSummaryMVDChartChart("0","0","0","0");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.e("LeaveReponse TAG", "onFailure : " + t.toString());
                        Toast.makeText(Sales_report_new.this, "Something went wrong  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Sales_report_new.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        public void ShowSalesSummaryMVDChartChart(String ttl, String Drmet, String Drseen, String Drmsd) {

            if (Drmet.equalsIgnoreCase("0") && Drseen.equalsIgnoreCase("0") && Drmet.equalsIgnoreCase("0")){
                MVD_pieGraph.clear();
            } else {
                ArrayList<PieEntry> values = new ArrayList<PieEntry>();
                values.clear();
                MVD_pieGraph.clear();
                Float countdata1 = Float.parseFloat(Drmet);
                Float countdata2 = Float.parseFloat(Drseen);
                Float countdata3 = Float.parseFloat(Drmsd);

                PieEntry pieEntry  = new PieEntry(countdata1, "Met");
                PieEntry pieEntry1 = new PieEntry(countdata2, "Seen");
                PieEntry pieEntry2  = new PieEntry(countdata3, "Missed");
                values.add(pieEntry);
                values.add(pieEntry1);
                values.add(pieEntry2);

//            ArrayList<Integer> colors = new ArrayList<>();
//            colors.add(Color.rgb(0, 102, 0));
//            colors.add(Color.rgb(255, 204, 0));
//            colors.add(Color.rgb(0, 0, 255));

                ArrayList<Integer> colors = new ArrayList<>();
//            colors.add(Color.rgb(213, 0, 0));
//            colors.add(Color.rgb(24, 255, 255));
//            colors.add(Color.rgb(255, 195, 0));
//            colors.add(Color.rgb(0, 200, 83));
//            colors.add(Color.rgb(62, 39, 35));
                colors.add(Color.rgb(124, 179, 66));
                colors.add(Color.rgb(255, 87, 51));
                colors.add(Color.rgb(37, 90, 243));
//            colors.add(Color.rgb(66, 165, 245));
//            colors.add(Color.rgb(13, 71, 161));

                PieDataSet pieDataSet = new PieDataSet(values, "");
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueFormatter(new PercentFormatter());
                MVD_pieGraph.setData(pieData);
                MVD_pieGraph.invalidate();
                MVD_pieGraph.setDrawHoleEnabled(false);
                pieDataSet.setColors(colors);
                pieData.setValueTextSize(13f);
                pieData.setValueTextColor(Color.BLACK);
                MVD_pieGraph.setDescription(null);
                MVD_pieGraph.animateXY(1400, 1400);
                MVD_pieGraph.setUsePercentValues(true);

                pieDataSet.setSliceSpace(1.5f);
                pieDataSet.setValueTextSize(10f);
                pieDataSet.setSelectionShift(10f);
                pieDataSet.setValueLinePart1OffsetPercentage(80.f);
                pieDataSet.setValueLinePart1Length(0.5f);
                pieDataSet.setValueLinePart2Length(0.5f);
                pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
                MVD_pieGraph.spin(500, 0, -360f, Easing.EaseInOutQuad);
                MVD_pieGraph.setEntryLabelColor(Color.RED);
                MVD_pieGraph.setEntryLabelTextSize(10f);
                pieDataSet.setUsingSliceColorAsValueLineColor(true);
                Legend legend = MVD_pieGraph.getLegend();
                legend.setEnabled(false);


                MVD_pieGraph.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        if (e == null) {
                            return;
                        }
                        showchartdetails(ttl,Drmet,Drseen,Drmsd);
//                    for (int i=0;i < values.size(); i++){
//                        if (values.get(i).getY() == e.getY()) {
//
//                        }
//                    }
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });
            }


            if (!ttl.equals("0")) {
                MVD_TextPieChart.setText("Total Doctors : " + ttl);
            } else {
                MVD_TextPieChart.setText("Total Doctors : 0");
            }
        }

        public void showchartdetails(String ttl, String drmet, String drseen, String drmsd){
            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(Sales_report_new.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.mvd_graph_view, null);
            dialogBuilder.setView(dialogView);
            ImageView img = dialogView.findViewById(R.id.txt_img);
            PieChart MVD_pieGraph = dialogView.findViewById(R.id.MVD_pieGraph);
            final android.app.AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(false);

            ArrayList<PieEntry> values = new ArrayList<PieEntry>();
            values.clear();
            MVD_pieGraph.clear();
            Float countdata1 = Float.parseFloat(drmet);
            Float countdata2 = Float.parseFloat(drseen);
            Float countdata3 = Float.parseFloat(drmsd);

            PieEntry pieEntry = new PieEntry(countdata1, "Met");
            PieEntry pieEntry1 = new PieEntry(countdata2, "Seen");
            PieEntry pieEntry2 = new PieEntry(countdata3, "Missed");
            values.add(pieEntry);
            values.add(pieEntry1);
            values.add(pieEntry2);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.rgb(124, 179, 66));
            colors.add(Color.rgb(255, 87, 51));
            colors.add(Color.rgb(37, 90, 243));

            PieDataSet pieDataSet = new PieDataSet(values, "");
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            MVD_pieGraph.setData(pieData);
            MVD_pieGraph.invalidate();
            MVD_pieGraph.setDrawHoleEnabled(false);
            pieDataSet.setColors(colors);
            pieData.setValueTextSize(13f);
            pieData.setValueTextColor(Color.BLACK);
            MVD_pieGraph.setDescription(null);
            MVD_pieGraph.animateXY(1400, 1400);
            MVD_pieGraph.setUsePercentValues(true);

            pieDataSet.setSliceSpace(1.5f);
            pieDataSet.setValueTextSize(10f);
            pieDataSet.setSelectionShift(10f);
            pieDataSet.setValueLinePart1OffsetPercentage(80.f);
            pieDataSet.setValueLinePart1Length(0.5f);
            pieDataSet.setValueLinePart2Length(0.5f);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
            MVD_pieGraph.spin(500, 0, -360f, Easing.EaseInOutQuad);
            MVD_pieGraph.setEntryLabelColor(Color.RED);
            MVD_pieGraph.setEntryLabelTextSize(10f);
            pieDataSet.setUsingSliceColorAsValueLineColor(true);
            Legend legend = MVD_pieGraph.getLegend();
            legend.setEnabled(true);


            MVD_pieGraph.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    if (e == null) {
                        return;
                    }
                    for (int i=0;i < values.size(); i++){
                        if (values.get(i).getY() == e.getY()) {

                        }
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });


            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
            alertDialog.show();
        }



        @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}