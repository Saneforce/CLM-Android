package saneforce.sanclm.Order_Report.Activity;

import android.app.DatePickerDialog;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.Common_Class.sqlLite;
import saneforce.sanclm.Order_Report.Adapter.TargetAdapter_new;
import saneforce.sanclm.Order_Report.Adapter.TargetAllsec_Adapter;
import saneforce.sanclm.Order_Report.Adapter.Targetadapter_newsecnd;
import saneforce.sanclm.Order_Report.modelclass.Primarysales_class;
import saneforce.sanclm.Order_Report.modelclass.TargetPrimary_Class;
import saneforce.sanclm.Order_Report.modelclass.TargetSecondary_class;
import saneforce.sanclm.Order_Report.modelclass.secondarysales_class;
import saneforce.sanclm.Order_Report.modelclass.targetAll;
import saneforce.sanclm.R;
import saneforce.sanclm.User_login.CustomerMe;
import saneforce.sanclm.activities.DashActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.api_Interface.ApiClient;
import saneforce.sanclm.api_Interface.ApiInterface;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.AppConfig;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;

public class Target_Vs_Secondary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    SharedPreferences sharedpreferencess;
    ImageView iv_draw, tfilter,iv_close;
    TextView mTitle;
    RelativeLayout targetlayout, reportlayout,secondarylayout;
    public static final String Name = "nameKey";
    public static final String mypreference = "mypref";
    DrawerLayout slidemenu;
    ImageView imp_back, filter_btn, imp_back_filter, secondary_sel,primary_sel, custom_datecheck;
    CardView secondary_card,primary_card, mtd_card, qtd_card, ytd_card;
    EditText fromdate_ed, todate_ed;
    Button clear, apply;
    PieChart pieChart;
    Spinner spinner;
    String div_code = "";
    Date formattedDate1;
    Date formattedDate2;
    RecyclerView recyclerView, secondarycycle;
    LinearLayout checklayout_date;
    Spinner division_splitting;
    TargetAdapter_new targetsadt;
    Targetadapter_newsecnd targetadt;
    TargetAllsec_Adapter targetALLAdapter;
    TextView fromdate, todate , txtpri,txtsec;
    CheckBox primebox, scondbox;
    public ArrayList<TargetPrimary_Class> targetdetails;
    public ArrayList<TargetSecondary_class> targetSdetails;
    public  ArrayList<String> primevalue;
    public  ArrayList<String> growthvalue;
    public  ArrayList<String> targetvalue;
    public  ArrayList<String> archeivevalue;

    public ArrayList<targetAll> targetAlls;
    public ArrayList<Primarysales_class> SalesPrimaryDetails;
    public ArrayList<secondarysales_class> SalesSecondaryDetails;
    ArrayList<PieEntry> values;
    String prod,prod_code,division_name,achie,growth;
    Double cnt;
    String Target_Val,Prev_Sale,pc;
    String DataSF = "";
    sqlLite sqlLite1;
    String curval = null;
    Gson gson;
    List<CustomerMe> CustomerMeList;
    ArrayList<String> hq_list1 = new ArrayList<>();
    ArrayList<String> hq_list1id = new ArrayList<>();
    ArrayList<String> Sf_Code = new ArrayList<>();
    ArrayList<String> division_code = new ArrayList<>();
    ArrayList<String> Division_Name = new ArrayList<>();
    ArrayList<String> Target = new ArrayList<>();
    ArrayList<String> Sale = new ArrayList<>();
    ArrayList<String> achiev = new ArrayList<>();
    ArrayList<String> PSale = new ArrayList<>();
    ArrayList<String> Growth = new ArrayList<>();
    ArrayList<String> PC = new ArrayList<>();
    ArrayList<String> div_list1 = new ArrayList<>();
    ArrayList<String> div_list1id = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;
    String fromstrdate = "";
    String tostrdate = "";
    ArrayList<String> toback;
    Spinner divspinner;
    String divison_code = "";
    TextView mtdtext, qtdtext, ytdtext,chrttxt;
    String selected_data = "";
    String selected_data_1 = "";

    ImageView imp_back1, filter_btn1,imp_back_filter1,secondary_sel1,custom_datecheck1,changecheck;
    CardView secondary_card1,mtd_card1,qtd_card1,ytd_card1;
    EditText fromdate_ed1,todate_ed1;
    Button clear1,apply1;
    PieChart pieChart1;
    Spinner spinner1;
    String div_code1 = "";
    Date formattedDate11;
    Date formattedDate21;
    RecyclerView recyclerView1, secondarycycle1;
    TargetAdapter_new targetadt1;
    Targetadapter_newsecnd targetsadt1;
    TextView fromdate1, todate1;
    CheckBox primebox1, scondbox1;
    public ArrayList<TargetPrimary_Class> targetdetails1;
    public ArrayList<TargetSecondary_class> targetSdetails1;
    public ArrayList<Primarysales_class> SalesPrimaryDetails1;
    public ArrayList<secondarysales_class> SalesSecondaryDetails1;
    ArrayList<String> div_list11 = new ArrayList<>();
    ArrayList<String> div_list1id1 = new ArrayList<>();
    ArrayList<PieEntry> values1;
    String prod1;
    double cnt1;
    String DataSF1 = "";
    sqlLite sqlLite11;
    String curval1 = null;
    Gson gson1;
    List<CustomerMe> CustomerMeList1;
    ArrayList<String> hq_list11 = new ArrayList<>();
    ArrayList<String> hq_list1id1 = new ArrayList<>();
    PieData pieData1;
    PieDataSet pieDataSet1;
    String fromstrdate1 = "";
    String tostrdate1 = "";
    DrawerLayout slidemenu1;
    TextView mtdtext1,qtdtext1,ytdtext1;
    String selected_data1="";
    Spinner division_splitting1;
    LinearLayout checklayout_date1;
    String divison_code1="";
    // BarChart chart1;
    float barWidth;
    float barSpace;
    float groupSpace;
    TextView profilename,prmtxt,sectxt;
    ArrayList<String> piedata = new ArrayList<String>();
    String sfdata = "";
    String fromdata = "";
    String todata = "";
    //CardView secondary_card,primary_card;
    CommonSharedPreference mCommonSharedPreference;
    String sf_code,div_Code,sf_name,db_connPath;
    String from_dt,selectdat="",fromdt="",todt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salesxml);
        iv_draw = ( ImageView ) findViewById(R.id.drawericon);
        navigationView = ( NavigationView ) findViewById(R.id.nav_view);
        iv_close=findViewById(R.id.close_img);
//        Toolbar toolbar = ( Toolbar ) findViewById(R.id.ToolbarHome);
        mTitle = findViewById(R.id.toolbar_title);
        chrttxt=findViewById(R.id.charttext);
        View headerView = navigationView.getHeaderView(0);
        //  chart1=findViewById(R.id.barchart);
//        targetlayout = findViewById(R.id.Target_activity);
//        reportlayout = findViewById(R.id.report_activity);
//        secondarylayout=findViewById(R.id.secondary_activity);
        tfilter = findViewById(R.id.camp_filter);
        Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);

        mCommonSharedPreference=new CommonSharedPreference(this);
        sharedpreferencess = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);



        sharedpreferencess = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        Log.d("custlist", "" + Dcrdatas.custlist);

        if (Dcrdatas.custlist == 1) {
            targetlayout.setVisibility(View.VISIBLE);
            reportlayout.setVisibility(View.GONE);
        } else if (Dcrdatas.custlist == 2) {
            targetlayout.setVisibility(View.GONE);
            reportlayout.setVisibility(View.VISIBLE);
            changeToolBarText("Reports");
        }
        final DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                Target_Vs_Secondary.this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        BottomNavigationView bottomNavigation = ( BottomNavigationView ) findViewById(R.id.bottom_navigationnew1);
//        BottomNavigationMenuView menuView = ( BottomNavigationMenuView ) bottomNavigation.getChildAt(0);
//
//        for (int i = 0; i < menuView.getChildCount(); i++) {
//            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
//            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
//            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//            layoutParams.height = ( int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, displayMetrics);
//            layoutParams.width = ( int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, displayMetrics);
//            iconView.setLayoutParams(layoutParams);
//        }
//        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        iv_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.LEFT);
                onBackPressed();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imp_back = findViewById(R.id.back_btn_drpt);
        spinner = findViewById(R.id.report_spin1);
        recyclerView = findViewById(R.id.primaryDetails);
        fromdate = findViewById(R.id.current_date);
        todate = findViewById(R.id.to_date);
        pieChart = findViewById(R.id.piechart);
        filter_btn = findViewById(R.id.camp_filter);
        sqlLite1 = new sqlLite(Target_Vs_Secondary.this);
        slidemenu = ( DrawerLayout ) findViewById(R.id.drawer_layout);
        //  secondary_card = ( CardView ) findViewById(R.id.seconday_crd);
        primary_card = ( CardView ) findViewById(R.id.prm);
        secondary_card = ( CardView ) findViewById(R.id.sec);
        mtd_card = ( CardView ) findViewById(R.id.mtd);
        qtd_card = ( CardView ) findViewById(R.id.qtd);
        ytd_card = ( CardView ) findViewById(R.id.ytd);
        mtdtext = ( TextView ) findViewById(R.id.mtd_txt);
        qtdtext = ( TextView ) findViewById(R.id.qtd_txt);
        ytdtext = ( TextView ) findViewById(R.id.ytd_text);
        fromdate_ed = ( EditText ) findViewById(R.id.fromdate_ed);
        todate_ed = ( EditText ) findViewById(R.id.todate_ed);
        division_splitting = ( Spinner ) findViewById(R.id.divisionselection_ed);
        clear = ( Button ) findViewById(R.id.filter_clear_1);
        apply = ( Button ) findViewById(R.id.filter_submit_1);
        //  secondary_sel = ( ImageView ) findViewById(R.id.second_check);
        primary_sel = ( ImageView ) findViewById(R.id.primer_check);
        checklayout_date = ( LinearLayout ) findViewById(R.id.customcheck_layout);
        custom_datecheck = ( ImageView ) findViewById(R.id.custom_check);
        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(
                Target_Vs_Secondary.this, slidemenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        slidemenu.setDrawerListener(toggle1);
        toggle1.syncState();
        targetdetails = new ArrayList<>();
        targetSdetails = new ArrayList<>();
        targetAlls = new ArrayList<>();
        SalesPrimaryDetails = new ArrayList<>();
        SalesSecondaryDetails = new ArrayList<>();
        toback = new ArrayList<String>();
        values = new ArrayList<PieEntry>();
        String date_n = new SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(new Date());
        fromdate.setText(date_n);
        Dcrdatas.date_fromdetails = fromdate.getText().toString();
        todate.setText("Till Date");
        todate.setVisibility(View.VISIBLE);
        Dcrdatas.till_date = todate.getText().toString();
        Dcrdatas.date_todetails = todate.getText().toString();


        selectdat=getIntent().getStringExtra("selected");
//        Log.d("santhosh",selectdat);
        if (selectdat.equals("Customdate")){
            fromdt=getIntent().getStringExtra("from");
            todt=getIntent().getStringExtra("to");
            Log.d("santhosh",fromdt+"--"+todt);
        }

        ///////////////////secondary-------------------
        imp_back1 = findViewById(R.id.back_btn_drpt);
        spinner1 = findViewById(R.id.report_spin1);
        recyclerView1 = findViewById(R.id.primaryDetails);
        fromdate1 =findViewById(R.id.current_date);
        todate1 =findViewById(R.id.to_date);
        pieChart1 = findViewById(R.id.piechart);
        sqlLite11 = new sqlLite(Target_Vs_Secondary.this);
        filter_btn1 = findViewById(R.id.camp_filter);
        slidemenu1 = (DrawerLayout) findViewById(R.id.drawer_layout);
        //  secondary_card1 = (CardView)findViewById(R.id.seconday_crd);
        mtd_card1 = (CardView)findViewById(R.id.mtd);
        qtd_card1 = (CardView)findViewById(R.id.qtd);
        ytd_card1= (CardView)findViewById(R.id.ytd);
        mtdtext1 = (TextView)findViewById(R.id.mtd_txt);
        qtdtext1 = (TextView)findViewById(R.id.qtd_txt);
        ytdtext1 = (TextView)findViewById(R.id.ytd_text);
        fromdate_ed1 = (EditText)findViewById(R.id.fromdate_ed);
        todate_ed1 = (EditText)findViewById(R.id.todate_ed);
//        division_splitting1 = (Spinner) findViewById(R.id.divisionselection_ed);
        clear1 = (Button)findViewById(R.id.filter_clear_1);
        apply1= (Button)findViewById(R.id.filter_submit_1);
        //  secondary_sel1 = (ImageView)findViewById(R.id.second_check);
        checklayout_date1 = (LinearLayout)findViewById(R.id.customcheck_layout);
        custom_datecheck1 = (ImageView)findViewById(R.id.custom_check);
        //   changecheck = (ImageView)findViewById(R.id.primary1_check);
        profilename = (TextView)headerView.findViewById(R.id.profilename);

        prmtxt = (TextView)findViewById(R.id.prm_txt);
        sectxt = (TextView)findViewById(R.id.sec_txt);

        targetdetails1 = new ArrayList<>();
        targetSdetails1 = new ArrayList<>();
        SalesPrimaryDetails1 = new ArrayList<>();
        SalesSecondaryDetails1 = new ArrayList<>();

        primevalue = new ArrayList<>();
        targetvalue = new ArrayList<>();
        growthvalue = new ArrayList<>();
        archeivevalue = new ArrayList<>();

        values1 = new ArrayList<PieEntry>();

        String date_n1 = new SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(new Date());
        fromdate1.setText(date_n1);
        todate1.setText("Till Date");
        todate1.setVisibility(View.VISIBLE);
        barWidth = 0.3f;
        barSpace = 0.05f;
        groupSpace = 0.16f;

        //divisiondata();

        secondary_card.setCardBackgroundColor(Color.RED);
        sectxt.setTextColor(Color.WHITE);
        // selected_data="secondary"
        Log.d("selected_data",selected_data);

        imp_back_filter = findViewById(R.id.back_btn_filter);
        imp_back_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidemenu.closeDrawer(Gravity.RIGHT);

            }
        });

        // profilename.setText("NAME");

        fromdate_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Target_Vs_Secondary.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromdate_ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                Dcrdatas.from_date = fromdate_ed.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        todate_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(getApplicationContext(),fromdate_ed.getText().toString(),Toast.LENGTH_SHORT).show();
                if (fromdate_ed.getText().toString().equals("")) {
                    Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.selctdate), Toast.LENGTH_LONG).show();
                } else {
                    //Log.d("fromdate",fromdate_ed.getText().toString());
//                    String [] frommedate = fromdate_ed.getText().toString().split("-");
//                    int fyear = Integer.parseInt(frommedate[0]);
//                    int fmonth = Integer.parseInt(frommedate[1]);
//                    int f1day = Integer.parseInt(frommedate[2]);

                    final Calendar c = Calendar.getInstance();

                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog1 = new DatePickerDialog(Target_Vs_Secondary.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    todate_ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    Dcrdatas.to_date = todate_ed.getText().toString();
                                    custom_datecheck.setImageResource(R.drawable.checkbox_marked26);
                                    custom_datecheck.setTag("check");
                                    mtd_card.setCardBackgroundColor(Color.WHITE);
                                    qtd_card.setCardBackgroundColor(Color.WHITE);
                                    ytd_card.setCardBackgroundColor(Color.WHITE);
                                    mtdtext.setTextColor(Color.RED);
                                    qtdtext.setTextColor(Color.RED);
                                    ytdtext.setTextColor(Color.RED);
                                    selected_data ="Customdate";
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog1.show();
                }
            }
        });

        secondary_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                secondary_card.setCardBackgroundColor(Color.RED);
                sectxt.setTextColor(Color.WHITE);
                primary_card.setCardBackgroundColor(Color.WHITE);
                prmtxt.setTextColor(Color.RED);
                selected_data_1="secondary";
                mtd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.RED);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                qtdtext.setTextColor(Color.RED);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                ytdtext.setTextColor(Color.RED);
                fromdate_ed.setText("");
                todate_ed.setText("");
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
            }
        });

        primary_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondary_card.setCardBackgroundColor(Color.WHITE);
                sectxt.setTextColor(Color.RED);
                primary_card.setCardBackgroundColor(Color.RED);
                prmtxt.setTextColor(Color.WHITE);
                selected_data_1="primary";
                mtd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.RED);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                qtdtext.setTextColor(Color.RED);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                ytdtext.setTextColor(Color.RED);
                fromdate_ed.setText("");
                todate_ed.setText("");
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
            }
        });

        checklayout_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checktag = custom_datecheck.getTag().toString();
                if (checktag.equalsIgnoreCase("uncheck")) {
                    custom_datecheck.setImageResource(R.drawable.checkbox_marked26);
                    custom_datecheck.setTag("check");
                    mtd_card.setCardBackgroundColor(Color.WHITE);
                    qtd_card.setCardBackgroundColor(Color.WHITE);
                    ytd_card.setCardBackgroundColor(Color.WHITE);
                    mtdtext.setTextColor(Color.RED);
                    qtdtext.setTextColor(Color.RED);
                    ytdtext.setTextColor(Color.RED);
                    selected_data="Customdate";
                } else {
                    custom_datecheck.setImageResource(R.drawable.checkbox);
                    custom_datecheck.setTag("uncheck");
                    todate_ed.setText("");
                    fromdate_ed.setText("");
                }
            }
        });

        mtd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.RED);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.WHITE);
                qtdtext.setTextColor(Color.RED);
                ytdtext.setTextColor(Color.RED);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data = "MTD";
            }
        });

        qtd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.WHITE);
                qtd_card.setCardBackgroundColor(Color.RED);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.RED);
                qtdtext.setTextColor(Color.WHITE);
                ytdtext.setTextColor(Color.RED);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data = "QTD";
            }
        });

        ytd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.WHITE);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                ytd_card.setCardBackgroundColor(Color.RED);
                mtdtext.setTextColor(Color.RED);
                qtdtext.setTextColor(Color.RED);
                ytdtext.setTextColor(Color.WHITE);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data = "YTD";
            }
        });

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v);
                slidemenu.openDrawer(Gravity.RIGHT);
            }
        });

        division_splitting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (div_list1.get(position).equalsIgnoreCase("All")) {
                    divison_code = "";
                    for (int i = 0; i < div_list1id.size(); i++) {
                        if (i == 0) {
                            // divison_code = div_list1id.get(position);
                        } else {
                            divison_code = divison_code + div_list1id.get(i);
                        }

                    }
                } else {
                    divison_code = div_list1id.get(position);
                }
                Log.d("11111", "one");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // MTDprimeproduct();
        //Orderproduct_PiechartMTD();
        Orderproduct_YTDprimary_l();
        Orderproduct_PiechartYTD();

//        MTDprimeproduct_admin();
//        Orderproduct_AllchartMTD();
//        Orderproduct_YTDprimary();
//        Orderproduct_PiechartMTD();
        mtd_card.setCardBackgroundColor(Color.RED);
        mtdtext.setTextColor(Color.WHITE);
        selected_data = "MTD";
        toback.clear();
        //toback.add("Admin");

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChart.clear();
                Log.d("rateess",selected_data);
                if((!fromdate_ed.getText().toString().equalsIgnoreCase(""))&& !todate_ed.getText().toString().equalsIgnoreCase(""))
                {
                    selected_data="Customdate";
                }
                if (selected_data_1.equalsIgnoreCase("primary")) {
//                    getFragmentManager().beginTransaction().replace(R.id.DCRMain_Frame, new Target_Vs_Secondary()).commit();
                    Intent oo = new Intent(Target_Vs_Secondary.this, DashActivity.class);
                    startActivity(oo);
//                    targetlayout.setVisibility(View.GONE);
//                    secondarylayout.setVisibility(View.VISIBLE);
//                    MTDsecondproduct();
//                    Orderproduct_PiechartSecondaryMTD();
//                    Toast.makeText(getApplicationContext(),"Under Construction",Toast.LENGTH_SHORT).show();
//                    mtd_card1.setCardBackgroundColor(Color.RED);
//                    mtdtext1.setTextColor(Color.WHITE);
                }

                else if (selected_data.equalsIgnoreCase("MTD")) {


                    MTDprimeproduct();
                    Orderproduct_PiechartMTD();


                    //  primartext.setText("Primary");
                } else if (selected_data.equalsIgnoreCase("QTD")) {

                    Orderproduct_QTDprimary_1();
                    Orderproduct_PiechartQTD();


                } else if (selected_data.equalsIgnoreCase("YTD")) {


                    Orderproduct_YTDprimary_l();
                    Orderproduct_PiechartYTD();


                }
                else if (selected_data.equalsIgnoreCase("Customdate")) {

                    if (fromdate_ed.getText().toString().equalsIgnoreCase("") || todate_ed.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.sclt_frm_to), Toast.LENGTH_SHORT).show();
                    } else {

                        allproduct();
                        Orderproduct_Piechart();


                    }
                    Log.d("xyz","custom_date");
                    selected_data = "customdate";
                }
                else {
                    Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.sclt_option), Toast.LENGTH_SHORT).show();
                }
                slidemenu.closeDrawer(Gravity.RIGHT);
            }
        });

//        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
//        recyclerView1.setLayoutManager(LayoutManagerpoc);
//        recyclerView1.setItemAnimator(new DefaultItemAnimator());
//        targetadt1 = new TargetAdapter_new(Target_Vs_Secondary.this, targetdetails, targetSdetails, todate, fromdate, Target_Vs_Secondary.this);
//        recyclerView1.setAdapter(targetadt1);

        fromdate_ed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Target_Vs_Secondary.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromdate_ed.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                Dcrdatas.from_date=fromdate_ed.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        todate_ed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromdate_ed.getText().toString().equals("")) {
                    Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.selctdate), Toast.LENGTH_LONG).show();
                } else {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(Target_Vs_Secondary.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    todate_ed.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                    Dcrdatas.to_date=todate_ed.getText().toString();
                                    custom_datecheck.setImageResource(R.drawable.checkbox_marked26);
                                    custom_datecheck.setTag("check");
                                    mtd_card.setCardBackgroundColor(Color.WHITE);
                                    qtd_card.setCardBackgroundColor(Color.WHITE);
                                    ytd_card.setCardBackgroundColor(Color.WHITE);
                                    mtdtext.setTextColor(Color.RED);
                                    qtdtext.setTextColor(Color.RED);
                                    ytdtext.setTextColor(Color.RED);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

//        secondary_card1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String checktag = secondary_sel.getTag().toString();
//                if(checktag.equalsIgnoreCase("uncheck")){
//                    secondary_sel.setImageResource(R.drawable.checkbox_marked26);
//                    secondary_sel.setTag("check");
//                }else{
//                    secondary_sel.setImageResource(R.drawable.checkbox);
//                    secondary_sel.setTag("uncheck");
//                }
//            }
//        });

        checklayout_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checktag = custom_datecheck.getTag().toString();
                if(checktag.equalsIgnoreCase("uncheck")){
                    custom_datecheck.setImageResource(R.drawable.checkbox_marked26);
                    custom_datecheck.setTag("check");
                    mtd_card.setCardBackgroundColor(Color.WHITE);
                    qtd_card.setCardBackgroundColor(Color.WHITE);
                    ytd_card.setCardBackgroundColor(Color.WHITE);
                    mtdtext.setTextColor(Color.RED);
                    qtdtext.setTextColor(Color.RED);
                    ytdtext.setTextColor(Color.RED);
                }else{
                    custom_datecheck.setImageResource(R.drawable.checkbox);
                    custom_datecheck.setTag("uncheck");
                }
            }
        });

        mtd_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.RED);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.WHITE);
                qtdtext.setTextColor(Color.RED);
                ytdtext.setTextColor(Color.RED);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data ="MTD";
            }
        });
        qtd_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.WHITE);
                qtd_card.setCardBackgroundColor(Color.RED);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.RED);
                qtdtext.setTextColor(Color.WHITE);
                ytdtext.setTextColor(Color.RED);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data ="QTD";
            }
        });
        ytd_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.WHITE);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                ytd_card.setCardBackgroundColor(Color.RED);
                mtdtext.setTextColor(Color.RED);
                qtdtext.setTextColor(Color.RED);
                ytdtext.setTextColor(Color.WHITE);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data ="YTD";
            }
        });


    }

    public void changeToolBarText(String sTitle) {
        Log.d("Title Checking ", sTitle);
        mTitle.setText(sTitle);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int tagID = 0;
        Dcrdatas dcrval = Dcrdatas.getInstance();
        switch (item.getItemId()) {
            case R.id.home:
                Intent o = new Intent(this.getApplicationContext(), Target_Vs_Secondary.class);
                startActivity(o);
                break;

//            case R.id.sfe:
//                Intent oo = new Intent(this.getApplicationContext(), SFE_Activtity.class);
//                startActivity(oo);
//                break;
//
//            case R.id.logout:
//                String one = "1";
//                Intent l = new Intent(this.getApplicationContext(), Login_Activity.class);
//                SharedPreferences.Editor editor = sharedpreferencess.edit();
//                editor.putString(Name, one);
//                editor.commit();
//                startActivity(l);
//                finish();
//                break;
        }

        DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.btMyResource:
                    Dcrdatas.custlist = 1;
                    Log.d("custlist", "" + Dcrdatas.custlist);
                    changeToolBarText("Target VS Sales");
                    targetlayout.setVisibility(View.VISIBLE);
                    reportlayout.setVisibility(View.GONE);
                    tfilter.setVisibility(View.VISIBLE);
                    return true;
                case R.id.btMyReports:
                    Dcrdatas.custlist = 2;
                    Log.d("custlist", "" + Dcrdatas.custlist);
                    changeToolBarText("Reports");
                    targetlayout.setVisibility(View.GONE);
                    reportlayout.setVisibility(View.VISIBLE);
                    tfilter.setVisibility(View.GONE);
                    return true;
            }

            return false;
        }
    };

    private void YTDALLproduct() {
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetALLAdapter = new TargetAllsec_Adapter(Target_Vs_Secondary.this,todate, fromdate,Target_Vs_Secondary.this,targetAlls);
        recyclerView.setAdapter(targetALLAdapter);
        Orderproduct_AllYTD();
    }

    private void Orderproduct_YTDprimary_l(){
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetadt = new Targetadapter_newsecnd(Target_Vs_Secondary.this, targetdetails, targetSdetails, todate, fromdate, Target_Vs_Secondary.this,growthvalue,archeivevalue,targetvalue,primevalue);
        recyclerView.setAdapter(targetadt);
        Orderproduct_AllYTD_1();
    }

    private void MTDprimeproduct() {
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetadt = new Targetadapter_newsecnd(Target_Vs_Secondary.this, targetdetails, targetSdetails, todate, fromdate, Target_Vs_Secondary.this,growthvalue,archeivevalue,targetvalue,primevalue);
        recyclerView.setAdapter(targetadt);
        Orderproduct_MTDprimary();
    }

    private void MTDprimeproduct_admin() {
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetALLAdapter = new TargetAllsec_Adapter(Target_Vs_Secondary.this,todate, fromdate,Target_Vs_Secondary.this,targetAlls);
        recyclerView.setAdapter(targetALLAdapter);
        Orderproduct_AllMTD();
    }

    private void QTDprimeproduct_admin_1() {
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetALLAdapter = new TargetAllsec_Adapter(Target_Vs_Secondary.this,todate, fromdate,Target_Vs_Secondary.this,targetAlls);
        recyclerView.setAdapter(targetALLAdapter);
        QTDprimeproduct_admin();
    }
    public void Orderproduct_QTDprimary_1(){
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetadt = new Targetadapter_newsecnd(Target_Vs_Secondary.this, targetdetails, targetSdetails, todate, fromdate, Target_Vs_Secondary.this,growthvalue,archeivevalue,targetvalue,primevalue);
        recyclerView.setAdapter(targetadt);
        Orderproduct_QTDprimary();
    }

    private void allproduct() {
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetadt = new Targetadapter_newsecnd(Target_Vs_Secondary.this, targetdetails, targetSdetails, todate, fromdate, Target_Vs_Secondary.this,growthvalue,archeivevalue,targetvalue,primevalue);
        recyclerView.setAdapter(targetadt);
        Orderproduct_detail();
    }
    private void allproduct_admin(){
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        targetALLAdapter = new TargetAllsec_Adapter(Target_Vs_Secondary.this,todate, fromdate,Target_Vs_Secondary.this,targetAlls);
        recyclerView.setAdapter(targetALLAdapter);
        Orderproduct_detail_1();
    }

    private void Orderproduct_PiechartMTD() {
        try {

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            String fromstrdate = "";
            String tostrdate = "";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

//            if (!Dcrdatas.from_date.equals("")) {
//                //   fromstrdate= Dcrdatas.from_date;
//                //fromdate.setText(fromstrdate);
//            } else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);

            //    }
//            if (!Dcrdatas.to_date.equals("")){
//             //   tostrdate= Dcrdatas.to_date;
//                //todate.setText(tostrdate);
//            }else {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

//            }

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            //QryParam.put("axn", "get/product_sales_secondary");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", div_Code);
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            // sfdata=toback.get(toback.size()-1);
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            fromdata=startDateStr;
            QryParam.put("from_date", startDateStr);
//            if (todate.getText().toString().equalsIgnoreCase("Till Date")) {
            todata=tostrdate;
            QryParam.put("to_date", tostrdate);
//            } else {
//                QryParam.put("to_date", tostrdate);
//            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getsecDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieprimary(response.body().toString(),div_Code,sf_code,fromdata,todata);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonData_div(String jsonResponse) {
        Log.d("jsonResponse_hq", jsonResponse);
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            div_list1.clear();
            div_list1id.clear();
//            hq_list1.add("Select HeadQuater");
//            hq_list1id.add(Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            div_list1.add("All");
            div_list1id.add("0");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.optString("division_code");
                String name = jsonObject1.optString("division_name");

                div_list1.add(name);
                div_list1id.add(id);
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Target_Vs_Secondary.this, android.R.layout.simple_spinner_dropdown_item, div_list1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            division_splitting.setAdapter(adapter1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Orderproduct_QTDprimary() {
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            String fromstrdate = "";
            String tostrdate = "";
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, 0);
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//            Date monthFirstDay = calendar.getTime();
//            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//            Date monthLastDay = calendar.getTime();
//
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);
//            String endDateStr = dfs.format(monthLastDay);
//
//            Log.e("DateFirstLast",startDateStr+" "+endDateStr);

            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
            Date monthFirstDay = cal.getTime();
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);

            Calendar cals = Calendar.getInstance();
            //cal.setTime(date);
            cals.set(Calendar.DAY_OF_MONTH, 1);
            cals.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
            cals.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = cals.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            final String startDateStr = dfs.format(monthFirstDay);
            final String endDateStr = dfs.format(monthLastDay);


            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                formattedDate2 = dfs.parse(endDateStr);
                fromdate.setText(output.format(formattedDate1));    // format output
                todate.setText(output.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
//            if (!Dcrdatas.to_date.equals("")){
//                tostrdate=Dcrdatas.to_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                tostrdate = df.format(c);
//            }
            Dcrdatas.startdate=startDateStr;
            Dcrdatas.enddate=endDateStr;
            Dcrdatas.division_coderselected = div_Code;
            //Dcrdatas.sfcode_selected = toback.get(toback.size()-1);
            Dcrdatas.sfcode_selected = sf_code;


            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
           // QryParam.put("axn", "get/target_sales_secondary");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", div_Code);
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", endDateStr);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getTargetSecDataAsJArray( QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            Orderproduct_MTDsecondary(startDateStr, endDateStr);
                            // secondaryproduct_detail11(startDateStr, endDateStr);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void QTDprimeproduct_admin(){
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }
            String fromstrdate = "";
            String tostrdate = "";
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, 0);
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//            Date monthFirstDay = calendar.getTime();
//            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//            Date monthLastDay = calendar.getTime();
//
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);
//            String endDateStr = dfs.format(monthLastDay);
//
//            Log.e("DateFirstLast",startDateStr+" "+endDateStr);

            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
            Date monthFirstDay = cal.getTime();
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);

            Calendar cals = Calendar.getInstance();
            //cal.setTime(date);
            cals.set(Calendar.DAY_OF_MONTH, 1);
            cals.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
            cals.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = cals.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);


            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                formattedDate2 = dfs.parse(endDateStr);
                fromdate.setText(output.format(formattedDate1));    // format output
                todate.setText(output.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
//            if (!Dcrdatas.to_date.equals("")){
//                tostrdate=Dcrdatas.to_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                tostrdate = df.format(c);
//            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            QryParam.put("sfCode", "Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", endDateStr);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderYTD(response.body().toString());
                            // secondaryproduct_detail11(startDateStr, endDateStr);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void Orderproduct_PiechartQTD_admin(){
        try {
            sqlLite sqlLite;
            String curval = null;
            final List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }



            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
            Date monthFirstDay = cal.getTime();
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);

            Calendar cals = Calendar.getInstance();
            //cal.setTime(date);
            cals.set(Calendar.DAY_OF_MONTH, 1);
            cals.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
            cals.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = cals.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);


            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                formattedDate2 = dfs.parse(endDateStr);
                fromdate.setText(output.format(formattedDate1));    // format output
                todate.setText(output.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All_brand");
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
            QryParam.put("sfCode","Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            fromdata = startDateStr;
            todata = endDateStr;
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", endDateStr);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonData_adminchart(response.body().toString(), CustomerMeList.get(0).getDivisionCode(),"Admin",fromdata,todata);
//                            secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_PiechartQTD() {
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
            Date monthFirstDay = cal.getTime();
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);

            Calendar cals = Calendar.getInstance();
            //cal.setTime(date);
            cals.set(Calendar.DAY_OF_MONTH, 1);
            cals.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
            cals.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = cals.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);


            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                formattedDate2 = dfs.parse(endDateStr);
                fromdate.setText(output.format(formattedDate1));    // format output
                todate.setText(output.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            }

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
           // QryParam.put("axn", "get/product_sales_secondary");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", div_Code);
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            //sfdata=toback.get(toback.size()-1);
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            fromdata=startDateStr;
            QryParam.put("from_date", startDateStr);
//            if (todate.getText().toString().equalsIgnoreCase("Till Date")) {
            todata=endDateStr;
            QryParam.put("to_date", endDateStr);
//            } else {
//                QryParam.put("to_date", tostrdate);
//            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getsecDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieprimary(response.body().toString(),div_Code,sf_code,fromdata,todata);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void Orderproduct_MTDprimary() {
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            String fromstrdate = "";
            String tostrdate = "";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            final String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));// format output
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("DateFirstLast", startDateStr + " " + endDateStr);

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);



            Dcrdatas.startdate=startDateStr;
            Dcrdatas.enddate=tostrdate;
            Dcrdatas.division_coderselected = div_Code;
            // Dcrdatas.sfcode_selected = toback.get(toback.size()-1);
            Dcrdatas.sfcode_selected = sf_code;

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
           // QryParam.put("axn", "get/target_sales_secondary");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", div_Code);

//            QryParam.put("sfCode", toback.get(toback.size()-1));
//            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));

            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
            QryParam.put("from_date",startDateStr);
            //if (todate.getText().toString().equalsIgnoreCase("Till Date")) {
            QryParam.put("to_date", tostrdate);
//            } else {
//                QryParam.put("to_date", todate.getText().toString());
//            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getTargetSecDataAsJArray(QryParam);
                final String finalTostrdate = tostrdate;
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            Orderproduct_MTDsecondary(startDateStr, finalTostrdate);
                            //secondaryproduct_detail11(startDateStr, endDateStr);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_Piechart() {
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            String fromstrdate = "";
            String tostrdate = "";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            if (!Dcrdatas.from_date.equals("")) {
                fromstrdate = Dcrdatas.from_date;
                //fromdate.setText(fromstrdate);
            } else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                fromstrdate = df.format(c);
            }
            if (!Dcrdatas.to_date.equals("")) {
                tostrdate = Dcrdatas.to_date;
                //todate.setText(tostrdate);
            } else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                tostrdate = df.format(c);
            }

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
          //  QryParam.put("axn", "get/product_sales_secondary");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", div_Code);
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            //sfdata=toback.get(toback.size()-1);
            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            fromdata=fromstrdate;
            todata=tostrdate;
            QryParam.put("from_date", fromstrdate);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getsecDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieprimary(response.body().toString(),div_Code,sf_code,fromdata,todata);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public  void Orderproduct_Piechartadmin(){
        try {
            sqlLite sqlLite;
            String curval = null;
            final List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }
            String fromstrdate = "";
            String tostrdate = "";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            if (!Dcrdatas.from_date.equals("")) {
                fromstrdate = Dcrdatas.from_date;
                //fromdate.setText(fromstrdate);
            } else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                fromstrdate = df.format(c);
            }
            if (!Dcrdatas.to_date.equals("")) {
                tostrdate = Dcrdatas.to_date;
                //todate.setText(tostrdate);
            } else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                tostrdate = df.format(c);
            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All_brand");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            QryParam.put("sfCode","Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            fromdata = fromstrdate;
            todata = tostrdate;
            QryParam.put("from_date", fromstrdate);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonData_adminchart(response.body().toString(), CustomerMeList.get(0).getDivisionCode(),"Admin",fromdata,todata);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void Orderproduct_detail() {
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);


            // if (!Dcrdatas.from_date.equals("")) {
            fromstrdate = fromdate_ed.getText().toString();
            //fromdate.setText(fromstrdate);
//            } else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//
//            }
            // if (!Dcrdatas.to_date.equals("")) {
            tostrdate = todate_ed.getText().toString();
            //todate.setText(tostrdate);
//            } else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                tostrdate = df.format(c);
//
//            }

            SimpleDateFormat input = new SimpleDateFormat("yy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = input.parse(fromstrdate);
                formattedDate2 = input.parse(tostrdate);// parse input
                fromdate.setText(output.format(formattedDate1));
                todate.setText(output.format(formattedDate2)); // format output
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Dcrdatas.startdate=fromstrdate;
            Dcrdatas.enddate=tostrdate;
            Dcrdatas.division_coderselected = div_Code;
            //  Dcrdatas.sfcode_selected = toback.get(toback.size()-1);
            Dcrdatas.sfcode_selected =sf_code;


            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
           // QryParam.put("axn", "get/target_sales_secondary");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", div_Code);
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
            QryParam.put("from_date", fromstrdate);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getTargetSecDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            Orderproduct_MTDsecondary(fromstrdate, tostrdate);
                            //secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Orderproduct_detail_1(){
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }


            if (!Dcrdatas.from_date.equals("")) {
                fromstrdate = Dcrdatas.from_date;
                //fromdate.setText(fromstrdate);
            } else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                fromstrdate = df.format(c);

            }
            if (!Dcrdatas.to_date.equals("")) {
                tostrdate = Dcrdatas.to_date;
                //todate.setText(tostrdate);
            } else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                tostrdate = df.format(c);

            }

            SimpleDateFormat input = new SimpleDateFormat("yy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = input.parse(fromstrdate);
                formattedDate2 = input.parse(tostrdate);// parse input
                fromdate.setText(output.format(formattedDate1));
                todate.setText(output.format(formattedDate2)); // format output
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All");
            // if (division_splitting.getVisibility() == View.VISIBLE) {
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
            QryParam.put("sfCode","Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            QryParam.put("from_date", fromstrdate);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderYTD(response.body().toString());
                            //secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void parseJsonDataPieprimary(String monitor,String div,String sfdata,String from,String to) {
        String date = "";
        try {
            try {
                JSONArray js = new JSONArray(monitor);
                Log.e("PieArray", js.toString());
                if (js.length() > 0) {
                    SalesSecondaryDetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        cnt = jsonObject.getDouble("cnt");
                        prod = jsonObject.getString("prod_name");
                        prod_code = jsonObject.getString("prod_code");
                        division_name = jsonObject.getString("Division_Name");
                        Target_Val = jsonObject.getString("Target_Val");
//                        Prev_Sale = jsonObject.getString("Prev_Sale");
                        achie = jsonObject.getString("achie");
                        growth = jsonObject.getString("Growth");
                        pc = jsonObject.getString("PC");

                        secondarysales_class primarysales_class = new secondarysales_class(cnt,prod,prod_code,division_name,Target_Val,achie,growth,pc);
                        SalesSecondaryDetails.add(primarysales_class);
                        targetadt.notifyDataSetChanged();

                        double sum = 0.0; //if you use version earlier than java-8
//double sum = IntStream.of(keyList).sum(); //if you are using java-8
                        for (int j = 0; j < SalesSecondaryDetails.size(); j++) {
                            sum += SalesSecondaryDetails.get(j).getCnt();
                        }
                        Log.d("sumvalue",""+sum);
                        for (int k = 0; k < SalesSecondaryDetails.size(); k++) {
                            System.out.println((SalesSecondaryDetails.get(k).getCnt() / sum) * 100 + "%");

                        }

                        final ArrayList<PieEntry> values = new ArrayList<PieEntry>();
//                        values.add(new PieEntry(35.34f,"Citmax Gold"));
//                        values.add(new PieEntry(11.9f,"Thiolac"));
//                        values.add(new PieEntry(9.78f,"Citmax D"));
//                        values.add(new PieEntry(9.22f,"Win RD"));
//                        values.add(new PieEntry(8.88f,"Tryclo D"));
//                        values.add(new PieEntry(8.62f,"Phofol D"));
//                        values.add(new PieEntry(7.45f,"Win HB"));
//                        values.add(new PieEntry(4.64f,"Gawin Nt"));
//                        values.add(new PieEntry(4.16f,"Protwin"));

                        if (sum==0.0){
                            pieChart.setVisibility(View.GONE);
                            chrttxt.setVisibility(View.VISIBLE);
                            chrttxt.setText("No Data Found");
                        }else {
                            pieChart.setVisibility(View.VISIBLE);
                            chrttxt.setVisibility(View.GONE);
                            piedata.clear();
                            for (int n = 0; n < SalesSecondaryDetails.size(); n++) {
                                double vuu = SalesSecondaryDetails.get(n).getCnt();
                                String name = SalesSecondaryDetails.get(n).getProd_name();
                                String prod_code = SalesSecondaryDetails.get(n).getProd_code();
                                String Target_Val = SalesSecondaryDetails.get(n).getTarget_Val();
                                String pc = SalesSecondaryDetails.get(n).getPC();
                                String Division_Name = SalesSecondaryDetails.get(n).getDivision_Name();
                                String achie = SalesSecondaryDetails.get(n).getAchie();
                                String Growth = SalesSecondaryDetails.get(n).getGrowth();
                                float soldPercentage = ( float ) (vuu * 100 / sum);
                                PieEntry pieEntry = new PieEntry(soldPercentage, name);
                                values.add(pieEntry);
                                piedata.add(prod_code+"~"+Target_Val+"~"+vuu+"~"+pc+"~"+Division_Name+"~"+achie+"~"+Growth+"~"+div+"~"+sfdata+"~"+from+"~"+to);
                            }
                        }

                        ArrayList<Integer> colors = new ArrayList<>();
//                        colors.add(Color.rgb(213, 0, 0));
//                        colors.add(Color.rgb(24, 255, 255));
//                        colors.add(Color.rgb(255, 195, 0));
//                        colors.add(Color.rgb(0, 200, 83));
//                        colors.add(Color.rgb(62, 39, 35));
//                        colors.add(Color.rgb(255, 87, 51));
//                        colors.add(Color.rgb(66, 165, 245));
//                        colors.add(Color.rgb(124, 179, 66));
//                        colors.add(Color.rgb(13, 71, 161));

                        colors.add(Color.rgb(213, 0, 0));
                        colors.add(Color.rgb(66, 165, 245));
                        colors.add(Color.rgb(255, 195, 0));
                        colors.add(Color.rgb(0, 200, 83));
                        colors.add(Color.rgb(62, 39, 35));
                        colors.add(Color.rgb(255, 87, 51));
                        colors.add(Color.rgb(31, 122, 122));
                        colors.add(Color.rgb(124, 179, 66));
                        colors.add(Color.rgb(13, 71, 161));
                        colors.add(Color.rgb(51, 102, 0));


                        pieDataSet = new PieDataSet(values, "");
                        pieData = new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieChart.setData(pieData);
                        pieChart.invalidate();
                        pieChart.setDrawHoleEnabled(false);
                        pieDataSet.setColors(colors);
                        pieData.setValueTextSize(13f);
                        pieData.setValueTextColor(Color.WHITE);
                        pieChart.setDescription(null);
                        pieChart.animateXY(1400, 1400);
                        pieChart.setUsePercentValues(true);

                        pieDataSet.setSliceSpace(1.5f);
                        pieDataSet.setValueTextSize(10f);
                        pieDataSet.setSelectionShift(10f);
                        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
                        pieDataSet.setValueLinePart1Length(0.5f);
                        pieDataSet.setValueLinePart2Length(0.5f);
                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
                        pieChart.spin(500, 0, -360f, Easing.EaseInOutQuad);
                        pieChart.setEntryLabelColor(Color.RED);
                        pieChart.setEntryLabelTextSize(10f);
                        pieDataSet.setUsingSliceColorAsValueLineColor(true);
                        Legend legend = pieChart.getLegend();
                        legend.setEnabled(false);
//                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                        legend.setDrawInside(false);
//                        legend.setXEntrySpace(4f);
//                        legend.setYEntrySpace(0f);
//                        legend.setWordWrapEnabled(true);
                        pieChart.setElevation(10);

                        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, Highlight h) {
                                if (e == null) {
                                    return;
                                }
                                for (int i=0;i < values.size(); i++){
                                    if (values.get(i).getY() == e.getY()) {
                                        showchartdetails(values.get(i).getLabel(),values.get(i).getY(),piedata,i);
                                        Log.d("data_values",""+values.get(i).getLabel()+"--"+values.get(i).getY()+"**"+piedata+"//"+i);
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });
                    }
                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    SalesSecondaryDetails.clear();
                    targetadt.notifyDataSetChanged();
                    Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDataorderDetail(String ordrdcr) {
        String date = "";
        String jsw = "";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);

                if (js.length() > 0) {
                    targetdetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        TargetPrimary_Class targetreport = new TargetPrimary_Class(
                                jsonObject.getString("sno"),
                                jsonObject.getString("sf_code"),
                                jsonObject.getString("sf_name"),
                                jsonObject.getString("hq"),
                                jsonObject.getString("sf_type"),
                                jsonObject.getString("SF_Cat_Code"),
                                jsonObject.getString("sf_TP_Active_Flag"),
                                jsonObject.getString("SF_VacantBlock"),
                                jsonObject.getString("Sl_Achieve"),
                                jsonObject.getString("Sl_growth"),
                                jsonObject.getString("Sl_Primary"),
                                jsonObject.getString("Sl_Target"),
                                jsonObject.getString("div_code"),
                                jsonObject.getString("Sl_PCPM"));
                        targetdetails.add(targetreport);
                        targetadt.notifyDataSetChanged();
                        //                       targetALLAdapter.notifyDataSetChanged();

                        for (int j = 0; j < targetdetails.size(); j++) {
                            Dcrdatas.secondaryvalue = targetdetails.get(j).getSl_Primary();
                        }
                    }

                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    targetdetails.clear();
//                    targetALLAdapter.notifyDataSetChanged();
                    targetadt.notifyDataSetChanged();
                    //targetsadt.notifyDataSetChanged();
                    Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.something_wrong) + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    private void secondaryproduct_detail11(String fromdate1, String todate1) {
//        try {
//            sqlLite sqlLite;
//            String curval = null;
//            List<CustomerMe> CustomerMeList;
//            sqlLite = new sqlLite(Target_Vs_Secondary.this);
//            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
//            Cursor cursor = sqlLite.getAllLoginData();
//
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
//
//
//            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
//                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
//            } else {
//                div_code = CustomerMeList.get(0).getDivisionCode();
//            }
//            String fromstrdate = fromdate1;
//            String tostrdate = todate1;
//
////            if (!Dcrdatas.from_date.equals("")){
////                fromstrdate= Dcrdatas.from_date;
////            }else {
////                Date c = Calendar.getInstance().getTime();
////                System.out.println("Current time => " + c);
////                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
////                fromstrdate = df.format(c);
////            }
////            if (!Dcrdatas.to_date.equals("")){
////                tostrdate= Dcrdatas.to_date;
////            }else {
////                Date c = Calendar.getInstance().getTime();
////                System.out.println("Current time => " + c);
////                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
////                tostrdate = df.format(c);
////            }
//
//            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
//            Log.e(" order request", "order Detail request");
//            Map<String, String> QryParam = new HashMap<>();
//            QryParam.put("axn", "get/target_sales_secondary");
//            if (division_splitting.getVisibility() == View.VISIBLE) {
//                QryParam.put("divisionCode", divison_code);
//            } else {
//                QryParam.put("divisionCode", div_code);
//            }
//            QryParam.put("sfCode", "Admin");
//            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
//            QryParam.put("from_date", fromstrdate);
//            QryParam.put("to_date", tostrdate);
//            Log.e("orderreport_detail", QryParam.toString());
//            try {
//
//                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
//                mProgressDialog.setIndeterminate(false);
//                mProgressDialog.setMessage("Loading...");
//                mProgressDialog.setCancelable(false);
//                mProgressDialog.show();
//                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
//                call.enqueue(new Callback<JsonArray>() {
//
//                    @Override
//                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
//                        if (mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
//                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
//                        if (response.code() == 200 || response.code() == 201) {
//                            Log.d("orderrepor:Res", response.body().toString());
//                            parseJsonDatasecondDetail11(response.body().toString());
//                        } else {
////                            Log.d("expense:Res", "1112222233333444");
//                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//
//                    @Override
//                    public void onFailure(Call<JsonArray> call, Throwable t) {
//                        if (mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
//                        Log.d("expense:Failure", t.toString());
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void parseJsonDatasecondDetail11(String ordrdcr) {
//        String date = "";
//        String jsw = "";
//        try {
//            try {
//                JSONArray js = new JSONArray(ordrdcr);
//
//                if (js.length() > 0) {
//                    targetSdetails.clear();
//                    for (int i = 0; i < js.length(); i++) {
//
//                        JSONObject jsonObject = js.getJSONObject(i);
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//                        TargetSecondary_class targetreport1 = new TargetSecondary_class(
//                                jsonObject.getString("sno"),
//                                jsonObject.getString("sf_code"),
//                                jsonObject.getString("sf_name"),
//                                jsonObject.getString("hq"),
//                                jsonObject.getString("sf_type"),
//                                jsonObject.getString("SF_Cat_Code"),
//                                jsonObject.getString("sf_TP_Active_Flag"),
//                                jsonObject.getString("SF_VacantBlock"),
//                                jsonObject.getString("Sl_Achieve"),
//                                jsonObject.getString("Sl_growth"),
//                                jsonObject.getString("Sl_Primary"),
//                                jsonObject.getString("Sl_Target"));
//                        targetSdetails.add(targetreport1);
//                        // targetsadt.notifyDataSetChanged();
//
//                        for (int j = 0; j < targetSdetails.size(); j++) {
//                            Dcrdatas.secondaryvalue = targetSdetails.get(j).getSl_Secondary();
//                        }
//
//                    }
//                    Log.d("dsize", "" + targetSdetails.size());
//                    //  targetSdetails.remove(0);
//                    Log.d("dsize", "" + targetSdetails.size());
//
//                } else {
////                    bottom_sum.setVisibility(View.GONE);
//                    targetSdetails.clear();
//                    // targetsadt.notifyDataSetChanged();
//                    Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    public void getsubdata(String sf_name,String div_code) {

        String selsfcode = sf_name;
        String division_code = div_code;
        toback.add(selsfcode);
        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(Target_Vs_Secondary.this);
        String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
        Cursor cursor = sqlLite.getAllLoginData();

        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
//        if (CustomerMeList.get(0).getSFCode().equalsIgnoreCase(selsfcode)) {
//            pieChart.clear();
//
//        } else {
//            LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
//            recyclerView.setLayoutManager(LayoutManagerpoc);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            targetadt = new TargetAdapter_new(Target_Vs_Secondary.this, targetdetails, targetSdetails, todate, fromdate, Target_Vs_Secondary.this);
//            recyclerView.setAdapter(targetadt);
        pieChart.clear();


        if (selected_data.equalsIgnoreCase("MTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    MTDprimeproduct_admin();
                    Orderproduct_AllchartMTD();
                }else{
                    MTDprimeproduct();
                    Orderproduct_PiechartMTD();
                }

            }else{
                MTDprimeproduct();
                Orderproduct_PiechartMTD();
            }

            //  primartext.setText("Primary");
        } else if (selected_data.equalsIgnoreCase("QTD")) {
            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    QTDprimeproduct_admin_1();
                    Orderproduct_PiechartQTD_admin();
                }else{
                    Orderproduct_QTDprimary_1();
                    Orderproduct_PiechartQTD();
                }

            }else{
                Orderproduct_QTDprimary_1();
                Orderproduct_PiechartQTD();
            }

        } else if (selected_data.equalsIgnoreCase("YTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    YTDALLproduct();
                    Orderproduct_AllchartYTD();
                }else{
                    Orderproduct_YTDprimary_l();
                    Orderproduct_PiechartYTD();
                }
            }
            else{
                Orderproduct_YTDprimary_l();
                Orderproduct_PiechartYTD();
            }

        }
        else if (selected_data.equalsIgnoreCase("Customdate")) {
            if (fromdate_ed.getText().toString().equalsIgnoreCase("") || todate_ed.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.sclt_frm_to), Toast.LENGTH_SHORT).show();
            } else {
                if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                    if(toback.size()==1){
                        allproduct_admin();
                        Orderproduct_Piechartadmin();
                    }else{
                        allproduct();
                        Orderproduct_Piechart();
                    }

                }
                else{
                    allproduct();
                    Orderproduct_Piechart();
                }

            }

        }

//            Orderproduct_MTDprimary1(selsfcode,division_code);
//            Orderproduct_Piechart(selsfcode,division_code);
        // }
    }

    public void getsubdata1(String sf_name,String division_codee) {
        String selsfcode = sf_name;
        String division_codes = division_codee;
        divison_code1 = division_codes;
        toback.add(selsfcode);
        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(Target_Vs_Secondary.this);
        String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
        Cursor cursor = sqlLite.getAllLoginData();

        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
//        if (CustomerMeList.get(0).getSFCode().equalsIgnoreCase(selsfcode)) {
//
//        } else {
//            LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
//            recyclerView.setLayoutManager(LayoutManagerpoc);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            targetadt = new TargetAdapter_new(Target_Vs_Secondary.this, targetdetails, targetSdetails, todate, fromdate, Target_Vs_Secondary.this);
//            recyclerView.setAdapter(targetadt);
        pieChart.clear();

        if (selected_data.equalsIgnoreCase("MTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    MTDprimeproduct_admin();
                    Orderproduct_AllchartMTD();
                }else{
                    MTDprimeproduct();
                    Orderproduct_PiechartMTD();
                }

            }else{
                MTDprimeproduct();
                Orderproduct_PiechartMTD();
            }

            //  primartext.setText("Primary");
        } else if (selected_data.equalsIgnoreCase("QTD")) {
            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    QTDprimeproduct_admin_1();
                    Orderproduct_PiechartQTD_admin();
                }else{
                    Orderproduct_QTDprimary_1();
                    Orderproduct_PiechartQTD();
                }

            }else{
                Orderproduct_QTDprimary_1();
                Orderproduct_PiechartQTD();
            }

        } else if (selected_data.equalsIgnoreCase("YTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    YTDALLproduct();
                    Orderproduct_AllchartYTD();
                }else{
                    Orderproduct_YTDprimary_l();
                    Orderproduct_PiechartYTD();
                }
            }
            else{
                Orderproduct_YTDprimary_l();
                Orderproduct_PiechartYTD();
            }

        }
        else if (selected_data.equalsIgnoreCase("Customdate")) {
            if (fromdate_ed.getText().toString().equalsIgnoreCase("") || todate_ed.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.sclt_frm_to), Toast.LENGTH_SHORT).show();
            } else {
                if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                    if(toback.size()==1){
                        allproduct_admin();
                        Orderproduct_Piechartadmin();
                    }else{
                        allproduct();
                        Orderproduct_Piechart();
                    }

                }
                else{
                    allproduct();
                    Orderproduct_Piechart();
                }

            }

        }

//            Orderproduct_MTDprimary1_1(selsfcode,division_codes);
//            Orderproduct_Piecharts(selsfcode,division_codes);
        // }

    }


    public void ytdback(String divdata){
        String selsfcode="";
        if ((toback.size()) != 0) {
            selsfcode = toback.get(toback.size() - 1);
            // toback.remove(toback.size() - 1);
        } else {
            selsfcode = CustomerMeList.get(0).getSFCode();
        }

        getbackdatas(selsfcode,divdata);
        pieChart.clear();

    }
    public void getbackdatas(String codeit,String divsion) {

        String selsfcode = codeit;
        String divsion1 = divsion;
        Log.d("tempr",selsfcode+divsion1);
        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(Target_Vs_Secondary.this);
        String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
        Cursor cursor = sqlLite.getAllLoginData();

        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
//        if (CustomerMeList.get(0).getSFCode().equalsIgnoreCase(selsfcode)) {
//
//        } else {
        pieChart.clear();
        if (selected_data.equalsIgnoreCase("MTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    MTDprimeproduct_admin();
                    Orderproduct_AllchartMTD();
                }else{
                    MTDprimeproduct();
                    Orderproduct_PiechartMTD();
                }

            }else{
                MTDprimeproduct();
                Orderproduct_PiechartMTD();
            }
            toback.remove(toback.size() - 1);

            //  primartext.setText("Primary");
        } else if (selected_data.equalsIgnoreCase("QTD")) {
            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    QTDprimeproduct_admin_1();
                    Orderproduct_PiechartQTD_admin();
                }else{
                    Orderproduct_QTDprimary_1();
                    Orderproduct_PiechartQTD();
                }

            }else{
                Orderproduct_QTDprimary_1();
                Orderproduct_PiechartQTD();
            }
            toback.remove(toback.size() - 1);

        } else if (selected_data.equalsIgnoreCase("YTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    YTDALLproduct();
                    Orderproduct_AllchartYTD();
                }else{
                    Orderproduct_YTDprimary_l();
                    Orderproduct_PiechartYTD();
                }
            }
            else{
                Orderproduct_YTDprimary_l();
                Orderproduct_PiechartYTD();
            }
            toback.remove(toback.size() - 1);

        }
        else if (selected_data.equalsIgnoreCase("Customdate")) {
            if (fromdate_ed.getText().toString().equalsIgnoreCase("") || todate_ed.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.sclt_frm_to), Toast.LENGTH_SHORT).show();
            } else {
                if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                    if(toback.size()==1){
                        allproduct_admin();
                        Orderproduct_Piechartadmin();
                    }else{
                        allproduct();
                        Orderproduct_Piechart();
                    }

                }
                else{
                    allproduct();
                    Orderproduct_Piechart();
                }

            }

            toback.remove(toback.size() - 1);
        }
        // }
    }

    public void getbackdata() {
//        String selsfcode = codeit;
//        String divif=div_code;
        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(Target_Vs_Secondary.this);
        String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
        Cursor cursor = sqlLite.getAllLoginData();

        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
//        if (CustomerMeList.get(0).getSFCode().equalsIgnoreCase(selsfcode)) {
//
//        } else {
//            LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
//            recyclerView.setLayoutManager(LayoutManagerpoc);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            targetALLAdapter = new TargetALL_Adapter(Target_Vs_Secondary.this,todate, fromdate,Target_Vs_Secondary.this,targetAlls);
//            recyclerView.setAdapter(targetALLAdapter);

        if (selected_data.equalsIgnoreCase("MTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    MTDprimeproduct_admin();
                    Orderproduct_AllchartMTD();
                }else{
                    MTDprimeproduct();
                    Orderproduct_PiechartMTD();
                }

            }else{
                MTDprimeproduct();
                Orderproduct_PiechartMTD();
            }
            toback.remove(toback.size() - 1);

            //  primartext.setText("Primary");
        } else if (selected_data.equalsIgnoreCase("QTD")) {
            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    QTDprimeproduct_admin_1();
                    Orderproduct_PiechartQTD_admin();
                }else{
                    Orderproduct_QTDprimary_1();
                    Orderproduct_PiechartQTD();
                }

            }else{
                Orderproduct_QTDprimary_1();
                Orderproduct_PiechartQTD();
            }
            toback.remove(toback.size() - 1);

        } else if (selected_data.equalsIgnoreCase("YTD")) {

            if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                if(toback.size()==1){
                    YTDALLproduct();
                    Orderproduct_AllchartYTD();
                }else{
                    Orderproduct_YTDprimary_l();
                    Orderproduct_PiechartYTD();
                }
            }
            else{
                Orderproduct_YTDprimary_l();
                Orderproduct_PiechartYTD();
            }
            toback.remove(toback.size() - 1);

        }
        else if (selected_data.equalsIgnoreCase("Customdate")) {
            if (fromdate_ed.getText().toString().equalsIgnoreCase("") || todate_ed.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.sclt_frm_to), Toast.LENGTH_SHORT).show();
            } else {
                if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
                    if(toback.size()==1){
                        allproduct_admin();
                        Orderproduct_Piechartadmin();
                    }else{
                        allproduct();
                        Orderproduct_Piechart();
                    }
                }
                else{
                    allproduct();
                    Orderproduct_Piechart();
                }

            }

            toback.remove(toback.size() - 1);
        }

//        }
    }




    public void divisiondata() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();
            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }
            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e("Missed report request", "Missed report Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "getdivision_ho_sf");
            QryParam.put("divisionCode", div_code);
            QryParam.put("sfCode", "");
            QryParam.put("Ho_Id", CustomerMeList.get(0).getSFCode());
            Log.e("mreport_detail", QryParam.toString());
            try {
                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("missreport:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("division:Res", response.body().toString());
//                            parseJsonData_div(response.body().toString());

                            String divname = response.body().toString();
                            if ((divname.equals("")) || (divname.isEmpty())) {
                                division_splitting.setVisibility(View.INVISIBLE);
//                                MTDprimeproduct();
//                                Orderproduct_PiechartMTD();
                            } else {
                                division_splitting.setVisibility(View.VISIBLE);
                                parseJsonData_div(divname);
                                if (div_list1id.size() > 1) {
                                    division_splitting.setSelection(1);
                                    divison_code = div_list1id.get(1);
                                } else {
                                    division_splitting.setSelection(0);
                                    divison_code = div_list1id.get(0);
                                }
                            }
                            Log.d("gang", divname);
                        } else {
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /////secondary

//    private void MTDsecondproduct() {
//        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
//        recyclerView.setLayoutManager(LayoutManagerpoc);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        targetsadt = new Targetadapter_newsecnd(Target_Vs_Secondary.this,targetdetails, targetSdetails,todate,fromdate);
//        recyclerView.setAdapter(targetsadt);
//        Orderproduct_MTDsecondary1();
//    }

    private void Orderproduct_MTDsecondary1() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }
            String fromstrdate = "";
            String tostrdate = "";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            Log.e("DateFirstLast", startDateStr + " " + endDateStr);


            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                formattedDate2 = dfs.parse(endDateStr);
                fromdate.setText(output.format(formattedDate1));    // format output
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);
//            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_secondary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", "Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            QryParam.put("from_date", startDateStr);
            // if(todate.getText().toString().equalsIgnoreCase("Till Date")){
            QryParam.put("to_date", tostrdate);
//            }else{
//                QryParam.put("to_date", todate.getText().toString());
//            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDatasecondDetail(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDatasecondDetail(String ordrdcr) {
        String date = "";
        String jsw = "";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);

                if (js.length() > 0) {
                    archeivevalue.clear();
                    growthvalue.clear();
                    primevalue.clear();
                    targetvalue.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        //  TargetSecondary_class targetreport1 = new TargetSecondary_class(
//                                jsonObject.getString("sno"),
//                                jsonObject.getString("sf_code"),
//                                jsonObject.getString("sf_name"),
//                                jsonObject.getString("hq"),
//                                jsonObject.getString("sf_type"),
//                                jsonObject.getString("SF_Cat_Code"),
//                                jsonObject.getString("sf_TP_Active_Flag"),
//                                jsonObject.getString("SF_VacantBlock"),
                        archeivevalue.add(jsonObject.getString("Sl_Achieve"));
                        growthvalue.add(jsonObject.getString("Sl_growth"));
                        primevalue.add(jsonObject.getString("Sl_Primary"));
                        targetvalue.add(jsonObject.getString("Sl_Target"));

                        //  targetSdetails.add(targetreport1);
                        //  targetadt.notifyDataSetChanged();

                    }

                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    targetSdetails.clear();
                    targetadt.notifyDataSetChanged();
                    Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Target_Vs_Secondary.this, getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void parseJsonDataPieSecondaryMTD(String monitor) {
//        String date = "";
//        try {
//            try {
//                JSONArray js = new JSONArray(monitor);
//                Log.e("PieArray", js.toString());
//                if (js.length() > 0) {
//                    SalesSecondaryDetails.clear();
//                    for (int i = 0; i < js.length(); i++) {
//
//                        JSONObject jsonObject = js.getJSONObject(i);
//                        cnt = jsonObject.getDouble("cnt");
//                        prod = jsonObject.getString("prod_name");
//                        secondarysales_class secondarysales_class = new secondarysales_class(cnt, prod);
//                        SalesSecondaryDetails.add(secondarysales_class);
//                        // targetadt.notifyDataSetChanged();
//
//                        double sum = 0.0; //if you use version earlier than java-8
////double sum = IntStream.of(keyList).sum(); //if you are using java-8
//                        for (int j = 0; j < SalesSecondaryDetails.size(); j++) {
//                            sum += SalesSecondaryDetails.get(j).getCnt();
//                        }
//                        for (int k = 0; k < SalesSecondaryDetails.size(); k++) {
//                            System.out.println((SalesSecondaryDetails.get(k).getCnt() / sum) * 100 + "%");
//
//                        }
//
//
//                        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
////                        values.add(new PieEntry(35.34f,"Citmax Gold"));
////                        values.add(new PieEntry(11.9f,"Thiolac"));
////                        values.add(new PieEntry(9.78f,"Citmax D"));
////                        values.add(new PieEntry(9.22f,"Win RD"));
////                        values.add(new PieEntry(8.88f,"Tryclo D"));
////                        values.add(new PieEntry(8.62f,"Phofol D"));
////                        values.add(new PieEntry(7.45f,"Win HB"));
////                        values.add(new PieEntry(4.64f,"Gawin Nt"));
////                        values.add(new PieEntry(4.16f,"Protwin"));
//
//                        for (int n = 0; n < SalesSecondaryDetails.size(); n++) {
//                            double vuu = SalesSecondaryDetails.get(n).getCnt();
//                            String name1 = SalesSecondaryDetails.get(n).getProd_name();
//                            float soldPercentage = ( float ) (vuu * 100 / sum);
//                            PieEntry pieEntry = new PieEntry(soldPercentage, name1);
//                            values.add(pieEntry);
//                        }
//
//                        ArrayList<Integer> colors = new ArrayList<>();
////                        colors.add(Color.rgb(213, 0, 0));
////                        colors.add(Color.rgb(255,69,0));
////                        colors.add(Color.rgb(255,140,0));
////                        colors.add(Color.rgb(0,100,0));
////                        colors.add(Color.rgb(184,134,11));
////                        colors.add(Color.rgb(0,0,128));
////                        colors.add(Color.rgb(30,144,255));
////                        colors.add(Color.rgb(139,69,19));
////                        colors.add(Color.rgb(183,176,253));
//                        colors.add(Color.rgb(0, 0, 128));
//                        colors.add(Color.rgb(213, 0, 0));
//                        colors.add(Color.rgb(184, 134, 11));
//                        colors.add(Color.rgb(255, 69, 0));
//                        colors.add(Color.rgb(30, 144, 255));
//                        colors.add(Color.rgb(0, 100, 0));
//                        colors.add(Color.rgb(255, 140, 0));
//                        colors.add(Color.rgb(183, 176, 253));
//                        colors.add(Color.rgb(139, 69, 19));
//
//                        pieDataSet = new PieDataSet(values, "");
//                        pieData = new PieData(pieDataSet);
//                        pieData.setValueFormatter(new PercentFormatter());
//                        pieChart.setData(pieData);
//                        pieChart.invalidate();
//                        pieChart.setDrawHoleEnabled(false);
//                        pieDataSet.setColors(colors);
//                        pieData.setValueTextSize(13f);
//                        pieData.setValueTextColor(Color.WHITE);
//                        pieChart.setDescription(null);
//                        pieChart.animateXY(1400, 1400);
//                        pieChart.setUsePercentValues(true);
//
//                        pieDataSet.setSliceSpace(1.5f);
//                        pieDataSet.setValueTextSize(10f);
//                        pieDataSet.setSelectionShift(10f);
//                        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
//                        pieDataSet.setValueLinePart1Length(0.5f);
//                        pieDataSet.setValueLinePart2Length(0.5f);
//                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//                        pieDataSet.setValueTextColor(getResources().getColor(R.color.White));
//                        pieChart.spin(500, 0, -360f, Easing.EaseInOutQuad);
//                        pieChart.setEntryLabelColor(Color.RED);
//                        pieChart.setEntryLabelTextSize(10f);
//                        pieDataSet.setUsingSliceColorAsValueLineColor(true);
//                        Legend legend = pieChart.getLegend();
//                        legend.setEnabled(false);
////                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
////                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
////                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
////                        legend.setDrawInside(false);
////                        legend.setXEntrySpace(4f);
////                        legend.setYEntrySpace(0f);
////                        legend.setWordWrapEnabled(true);
//                        pieChart.setElevation(10);
//
//
//                    }
//                } else {
////                    bottom_sum.setVisibility(View.GONE);
//                    SalesSecondaryDetails.clear();
//                    //targetadt.notifyDataSetChanged();
//                    Toast.makeText(Target_Vs_Secondary.this, "No Records found", Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }




//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void parseJsonDataPieSecondary(String monitor) {
//        String date = "";
//        try {
//            try {
//                JSONArray js = new JSONArray(monitor);
//                Log.e("PieArray", js.toString());
//                if (js.length() > 0) {
//                    SalesSecondaryDetails.clear();
//                    for (int i = 0; i < js.length(); i++) {
//
//                        JSONObject jsonObject = js.getJSONObject(i);
//                        cnt = jsonObject.getDouble("cnt");
//                        prod = jsonObject.getString("prod_name");
//                        secondarysales_class secondarysales_class = new secondarysales_class(cnt, prod);
//                        SalesSecondaryDetails.add(secondarysales_class);
//                        // targetadt.notifyDataSetChanged();
//
//                        double sum = 0.0; //if you use version earlier than java-8
////double sum = IntStream.of(keyList).sum(); //if you are using java-8
//                        for (int j = 0; j < SalesSecondaryDetails.size(); j++) {
//                            sum += SalesSecondaryDetails.get(j).getCnt();
//                        }
//                        for (int k = 0; k < SalesSecondaryDetails.size(); k++) {
//                            System.out.println((SalesSecondaryDetails.get(k).getCnt() / sum) * 100 + "%");
//
//                        }
//
//
//                        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
////                        values.add(new PieEntry(35.34f,"Citmax Gold"));
////                        values.add(new PieEntry(11.9f,"Thiolac"));
////                        values.add(new PieEntry(9.78f,"Citmax D"));
////                        values.add(new PieEntry(9.22f,"Win RD"));
////                        values.add(new PieEntry(8.88f,"Tryclo D"));
////                        values.add(new PieEntry(8.62f,"Phofol D"));
////                        values.add(new PieEntry(7.45f,"Win HB"));
////                        values.add(new PieEntry(4.64f,"Gawin Nt"));
////                        values.add(new PieEntry(4.16f,"Protwin"));
//
//                        for (int n = 0; n < SalesSecondaryDetails.size(); n++) {
//                            double vuu = SalesSecondaryDetails.get(n).getCnt();
//                            String name1 = SalesSecondaryDetails.get(n).getProd_name();
//                            float soldPercentage = ( float ) (vuu * 100 / sum);
//                            PieEntry pieEntry = new PieEntry(soldPercentage, name1);
//                            values.add(pieEntry);
//                        }
//
//                        ArrayList<Integer> colors = new ArrayList<>();
////                        colors.add(Color.rgb(213, 0, 0));
////                        colors.add(Color.rgb(255,69,0));
////                        colors.add(Color.rgb(255,140,0));
////                        colors.add(Color.rgb(0,100,0));
////                        colors.add(Color.rgb(184,134,11));
////                        colors.add(Color.rgb(0,0,128));
////                        colors.add(Color.rgb(30,144,255));
////                        colors.add(Color.rgb(139,69,19));
////                        colors.add(Color.rgb(183,176,253));
//                        colors.add(Color.rgb(0, 0, 128));
//                        colors.add(Color.rgb(213, 0, 0));
//                        colors.add(Color.rgb(184, 134, 11));
//                        colors.add(Color.rgb(255, 69, 0));
//                        colors.add(Color.rgb(30, 144, 255));
//                        colors.add(Color.rgb(0, 100, 0));
//                        colors.add(Color.rgb(255, 140, 0));
//                        colors.add(Color.rgb(183, 176, 253));
//                        colors.add(Color.rgb(139, 69, 19));
//
//                        pieDataSet = new PieDataSet(values, "");
//                        pieData = new PieData(pieDataSet);
//                        pieData.setValueFormatter(new PercentFormatter());
//                        pieChart.setData(pieData);
//                        pieChart.setDrawHoleEnabled(false);
//                        pieDataSet.setColors(colors);
//                        pieData.setValueTextSize(13f);
//                        pieData.setValueTextColor(Color.WHITE);
//                        pieChart.setDescription(null);
//                        pieChart.animateXY(1400, 1400);
//                        pieChart.setUsePercentValues(true);
//
//                        pieDataSet.setSliceSpace(1.5f);
//                        pieDataSet.setValueTextSize(10f);
//                        pieDataSet.setSelectionShift(10f);
//                        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
//                        pieDataSet.setValueLinePart1Length(0.5f);
//                        pieDataSet.setValueLinePart2Length(0.5f);
//                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//                        pieDataSet.setValueTextColor(getResources().getColor(R.color.White));
//                        pieChart.spin(500, 0, -360f, Easing.EaseInOutQuad);
//                        pieChart.setEntryLabelColor(Color.RED);
//                        pieChart.setEntryLabelTextSize(10f);
//                        pieDataSet.setUsingSliceColorAsValueLineColor(true);
//                        Legend legend = pieChart.getLegend();
//                        legend.setEnabled(false);
////                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
////                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
////                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
////                        legend.setDrawInside(false);
////                        legend.setXEntrySpace(4f);
////                        legend.setYEntrySpace(0f);
////                        legend.setWordWrapEnabled(true);
//                        pieChart.setElevation(10);
//                        pieChart.invalidate();
//
//
//                    }
//                } else {
////                    bottom_sum.setVisibility(View.GONE);
//                    SalesSecondaryDetails.clear();
//                    //targetadt.notifyDataSetChanged();
//                    Toast.makeText(Target_Vs_Secondary.this, "No Records found", Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void Orderproduct_AllYTD() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }



            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 03);
            int monthee = Calendar.getInstance().get(Calendar.MONTH) + 1;
            Log.d("check_month", "" + monthee);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if ((monthee == 1) || (monthee == 2) || (monthee == 3) || (monthee == 4)) {
                cal.set(Calendar.YEAR, year - 1);
            } else {
                cal.set(Calendar.YEAR, year);
            }
            Date monthFirstDay = cal.getTime();
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate2 = df.parse(tostrdate);                 // parse input
                todate.setText(output1.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All");
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
            QryParam.put("sfCode","Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderYTD(response.body().toString());
//                            secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_AllYTD_1() {
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);



            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 03);
            int monthee = Calendar.getInstance().get(Calendar.MONTH) + 1;
            Log.d("check_month", "" + monthee);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if ((monthee == 1) || (monthee == 2) || (monthee == 3) || (monthee == 4)) {
                cal.set(Calendar.YEAR, year - 1);
            } else {
                cal.set(Calendar.YEAR, year);
            }
            Date monthFirstDay = cal.getTime();
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            final String startDateStr = dfs.format(monthFirstDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate2 = df.parse(tostrdate);                 // parse input
                todate.setText(output1.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }


//            int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
//            int CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
//            String financiyalYearFrom="";
//            String financiyalYearTo="";
//            if (CurrentMonth<4) {
//                financiyalYearFrom=(CurrentYear-1)+"-04-01";
//                financiyalYearTo=(CurrentYear)+"-03-31";
//            } else {
//                financiyalYearFrom=(CurrentYear)+"-04-01";
//                financiyalYearTo=(CurrentYear+1)+"-03-31";
//            }
//
////            Date c = Calendar.getInstance().getTime();
////            System.out.println("Current time => " + c);
//
//            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat output11 = new SimpleDateFormat("MMM yyyy");
//            String startDateStr1 = financiyalYearFrom;
//            formattedDate1 = input.parse(financiyalYearFrom);
//            fromdate.setText(output11.format(formattedDate1));
//
//            SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
//            tostrdate = financiyalYearTo;
//            formattedDate2 = input1.parse(tostrdate);
//            todate.setText(output1.format(formattedDate2));

            Dcrdatas.startdate = startDateStr;
            Dcrdatas.enddate = tostrdate;
            Dcrdatas.division_coderselected = div_Code;
            //Dcrdatas.sfcode_selected = toback.get(toback.size()-1);
            Dcrdatas.sfcode_selected =sf_code;


            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            //QryParam.put("axn", "get/target_sales_secondary");
            QryParam.put("divisionCode", div_Code);
            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getTargetSecDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            Orderproduct_MTDsecondary(startDateStr, tostrdate);
//                            secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_AllMTD(){
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());

            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }

            String fromstrdate = "";
            String tostrdate = "";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));// format output
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("DateFirstLast", startDateStr + " " + endDateStr);


//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate2 = df.parse(tostrdate);                 // parse input
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All");
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
            QryParam.put("sfCode","Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderYTD(response.body().toString());
//                            secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDataorderYTD(String ordrdcr) {
        try {
            try {
                Log.d("orderprd",ordrdcr);
                JSONArray js = new JSONArray(ordrdcr);
//                Sf_Code.clear();
//                division_code.clear();
//                Division_Name.clear();
//                Target.clear();
//                Sale.clear();
//                achiev.clear();
//                PSale.clear();
//                Growth.clear();
//                PC.clear();
//                if (js.length() > 0) {
//                    for (int i = 0; i < js.length(); i++) {
//                        JSONObject jsonObject1 = js.getJSONObject(i);
//                        String id = jsonObject1.optString("Sf_Code");
//                        String division = jsonObject1.optString("division_code");
//                        String divname = jsonObject1.optString("Division_Name");
//                        String target = jsonObject1.optString("Target");
//                        String sale = jsonObject1.optString("Sale");
//                        String achie = jsonObject1.optString("achie");
//                        String psale = jsonObject1.optString("PSale");
//                        String growth = jsonObject1.optString("Growth");
//                        String pc = jsonObject1.optString("PC");
//                        Sf_Code.add(id);
//                        division_code.add(division);
//                        Division_Name.add(divname);
//                        Target.add(target);
//                        Sale.add(sale);
//                        achiev.add(achie);
//                        PSale.add(psale);
//                        Growth.add(growth);
//                        PC.add(pc);
//                    }
//                    LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
//                    recyclerView.setLayoutManager(LayoutManagerpoc);
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    targetALLAdapter = new TargetALL_Adapter(Target_Vs_Secondary.this,todate, fromdate,Target_Vs_Secondary.this,Sf_Code,division_code,Division_Name,Target,Sale,achiev,PSale,Growth,PC);
//                    recyclerView.setAdapter(targetALLAdapter);
//                }
                if (js.length() > 0) {
                    targetAlls.clear();
                    for (int i = 0; i < js.length(); i++) {
                        JSONObject jsonObject = js.getJSONObject(i);
                        targetAll target1 = new targetAll(
                                jsonObject.getString("Sf_Code"),
                                jsonObject.getString("division_code"),
                                jsonObject.getString("Division_Name"),
                                jsonObject.getString("Target"),
                                jsonObject.getString("Sale"),
                                jsonObject.getString("achie"),
                                jsonObject.getString("PSale"),
                                jsonObject.getString("Growth"),
                                jsonObject.getString("PC"));
                        targetAlls.add(target1);
                        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(Target_Vs_Secondary.this);
                        recyclerView.setLayoutManager(LayoutManagerpoc);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        targetALLAdapter = new TargetAllsec_Adapter(Target_Vs_Secondary.this,todate, fromdate,Target_Vs_Secondary.this,targetAlls);
                        recyclerView.setAdapter(targetALLAdapter);
                        targetALLAdapter.notifyDataSetChanged();

                    }

                } else {
                    targetAlls.clear();
                    targetALLAdapter.notifyDataSetChanged();
                    Toast.makeText(Target_Vs_Secondary.this, "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_AllchartYTD() {
        try {
            sqlLite sqlLite;
            String curval = null;
            final List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }



            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 03);
            int monthee = Calendar.getInstance().get(Calendar.MONTH) + 1;
            Log.d("check_month", "" + monthee);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if ((monthee == 1) || (monthee == 2) || (monthee == 3) || (monthee == 4)) {
                cal.set(Calendar.YEAR, year - 1);
            } else {
                cal.set(Calendar.YEAR, year);
            }
            Date monthFirstDay = cal.getTime();
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate2 = df.parse(tostrdate);                 // parse input
                todate.setText(output1.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All_brand");
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
            QryParam.put("sfCode","Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            fromdata = startDateStr;
            todata = tostrdate;
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonData_adminchart(response.body().toString(),CustomerMeList.get(0).getDivisionCode(),"Admin",fromdata,todata);
//                            secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void Orderproduct_PiechartYTD(){
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);




            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 03);
            int monthee = Calendar.getInstance().get(Calendar.MONTH) + 1;
            Log.d("check_month", "" + monthee);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if ((monthee == 1) || (monthee == 2) || (monthee == 3) || (monthee == 4)) {
                cal.set(Calendar.YEAR, year - 1);
            } else {
                cal.set(Calendar.YEAR, year);
            }
            Date monthFirstDay = cal.getTime();
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate2 = df.parse(tostrdate);                 // parse input
                todate.setText(output1.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }



//            int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
//            int CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
//            String financiyalYearFrom="";
//            String financiyalYearTo="";
//            if (CurrentMonth<4) {
//                financiyalYearFrom=(CurrentYear-1)+"-04-01";
//                financiyalYearTo=(CurrentYear)+"-03-31";
//            } else {
//                financiyalYearFrom=(CurrentYear)+"-04-01";
//                financiyalYearTo=(CurrentYear+1)+"-03-31";
//            }
//
////            Date c = Calendar.getInstance().getTime();
////            System.out.println("Current time => " + c);
//
//            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat output11 = new SimpleDateFormat("MMM yyyy");
//            String startDateStr1 = financiyalYearFrom;
//            formattedDate1 = input.parse(financiyalYearFrom);
//            fromdate.setText(output11.format(formattedDate1));
//
//            SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
//            tostrdate = financiyalYearTo;
//            formattedDate2 = input1.parse(tostrdate);
//            todate.setText(output1.format(formattedDate2));

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
          //  QryParam.put("axn", "get/product_sales_secondary");
            QryParam.put("divisionCode",div_Code);
            //sfdata = toback.get(toback.size()-1);
            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
            fromdata = startDateStr;
            todata = tostrdate;
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getsecDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonData_adminchart(response.body().toString(),div_Code,sf_code,fromdata,todata);
//                            secondaryproduct_detail11(fromstrdate, tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void Orderproduct_AllchartMTD() {
        try {
            sqlLite sqlLite;
            String curval = null;
            final List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Target_Vs_Secondary.this);
            String username = Shared_Common_Pref.getusernameFromSP(Target_Vs_Secondary.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }



            String fromstrdate = "";
            String tostrdate = "";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));// format output
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("DateFirstLast", startDateStr + " " + endDateStr);

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate2 = df.parse(tostrdate);                 // parse input
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ApiInterface apiService = ApiClient.getClient(Target_Vs_Secondary.this).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Secondary_YTD_All_brand");
            QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
            QryParam.put("sfCode","Admin");
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(Target_Vs_Secondary.this));
            fromdata = startDateStr;
            todata = tostrdate;
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Target_Vs_Secondary.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonData_adminchart(response.body().toString(),CustomerMeList.get(0).getDivisionCode(),"Admin",fromdata,todata);
//                            secondaryproduct_detail11(fromstrdate, tostrdate);
                        }
                        else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void parseJsonData_adminchart(String monitor,String div,String sfdata,String from,String to) {
        String date = "";
        try {
            try {
                JSONArray js = new JSONArray(monitor);
                Log.e("PieArray", js.toString());
                if (js.length() > 0) {
                    SalesSecondaryDetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        cnt = jsonObject.getDouble("cnt");
                        prod = jsonObject.getString("prod_name");
                        prod_code = jsonObject.getString("prod_code");
                        division_name = jsonObject.getString("Division_Name");
                        Target_Val = jsonObject.getString("Target_Val");
//                        Prev_Sale = jsonObject.getString("Prev_Sale");
                        achie = jsonObject.getString("achie");
                        growth = jsonObject.getString("Growth");
                        pc = jsonObject.getString("PC");

                        secondarysales_class primarysales_class = new secondarysales_class(cnt,prod,prod_code,division_name,Target_Val,achie,growth,pc);
                        SalesSecondaryDetails.add(primarysales_class);
//                        targetadt.notifyDataSetChanged();

                        double sum = 0.0; //if you use version earlier than java-8
//double sum = IntStream.of(keyList).sum(); //if you are using java-8
                        for (int j = 0; j < SalesSecondaryDetails.size(); j++) {

                            sum += SalesSecondaryDetails.get(j).getCnt();
                        }
                        Log.d("sumvalue",""+sum);
                        for (int k = 0; k < SalesSecondaryDetails.size(); k++) {
                            System.out.println((SalesSecondaryDetails.get(k).getCnt() / sum) * 100 + "%");

                        }

                        final ArrayList<PieEntry> values = new ArrayList<PieEntry>();
//                        values.add(new PieEntry(35.34f,"Citmax Gold"));
//                        values.add(new PieEntry(11.9f,"Thiolac"));
//                        values.add(new PieEntry(9.78f,"Citmax D"));
//                        values.add(new PieEntry(9.22f,"Win RD"));
//                        values.add(new PieEntry(8.88f,"Tryclo D"));
//                        values.add(new PieEntry(8.62f,"Phofol D"));
//                        values.add(new PieEntry(7.45f,"Win HB"));
//                        values.add(new PieEntry(4.64f,"Gawin Nt"));
//                        values.add(new PieEntry(4.16f,"Protwin"));

                        if (sum==0.0){
                            pieChart.setVisibility(View.GONE);
                            chrttxt.setVisibility(View.VISIBLE);
                            chrttxt.setText("No Data Found");
                        }else {
                            pieChart.setVisibility(View.VISIBLE);
                            chrttxt.setVisibility(View.GONE);
                            piedata.clear();
                            for (int n = 0; n < SalesSecondaryDetails.size(); n++) {
                                double vuu = SalesSecondaryDetails.get(n).getCnt();
                                String name = SalesSecondaryDetails.get(n).getProd_name();
                                String prod_code = SalesSecondaryDetails.get(n).getProd_code();
                                String Target_Val = SalesSecondaryDetails.get(n).getTarget_Val();
                                String pc = SalesSecondaryDetails.get(n).getPC();
                                String Division_Name = SalesSecondaryDetails.get(n).getDivision_Name();
                                String achie = SalesSecondaryDetails.get(n).getAchie();
                                String Growth = SalesSecondaryDetails.get(n).getGrowth();
                                float soldPercentage = ( float ) (vuu * 100 / sum);
                                PieEntry pieEntry = new PieEntry(soldPercentage, name);
                                values.add(pieEntry);
                                piedata.add(prod_code+"~"+Target_Val+"~"+vuu+"~"+pc+"~"+Division_Name+"~"+achie+"~"+Growth+"~"+div+"~"+sfdata+"~"+from+"~"+to);
                            }
                        }

                        ArrayList<Integer> colors = new ArrayList<>();
//                        colors.add(Color.rgb(213, 0, 0));
//                        colors.add(Color.rgb(24, 255, 255));
//                        colors.add(Color.rgb(255, 195, 0));
//                        colors.add(Color.rgb(0, 200, 83));
//                        colors.add(Color.rgb(62, 39, 35));
//                        colors.add(Color.rgb(255, 87, 51));
//                        colors.add(Color.rgb(66, 165, 245));
//                        colors.add(Color.rgb(124, 179, 66));
//                        colors.add(Color.rgb(13, 71, 161));

                        colors.add(Color.rgb(213, 0, 0));
                        colors.add(Color.rgb(66, 165, 245));
                        colors.add(Color.rgb(255, 195, 0));
                        colors.add(Color.rgb(0, 200, 83));
                        colors.add(Color.rgb(62, 39, 35));
                        colors.add(Color.rgb(255, 87, 51));
                        colors.add(Color.rgb(31, 122, 122));
                        colors.add(Color.rgb(124, 179, 66));
                        colors.add(Color.rgb(13, 71, 161));
                        colors.add(Color.rgb(51, 102, 0));

                        pieDataSet = new PieDataSet(values, "");
                        pieData = new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieChart.setData(pieData);
                        pieChart.invalidate();
                        pieChart.setDrawHoleEnabled(false);
                        pieDataSet.setColors(colors);
                        pieData.setValueTextSize(13f);
                        pieData.setValueTextColor(Color.WHITE);
                        pieChart.setDescription(null);
                        pieChart.animateXY(1400, 1400);
                        pieChart.setUsePercentValues(true);

                        pieDataSet.setSliceSpace(1.5f);
                        pieDataSet.setValueTextSize(10f);
                        pieDataSet.setSelectionShift(10f);
                        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
                        pieDataSet.setValueLinePart1Length(0.5f);
                        pieDataSet.setValueLinePart2Length(0.5f);
                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
                        pieChart.spin(500, 0, -360f, Easing.EaseInOutQuad);
                        pieChart.setEntryLabelColor(Color.RED);
                        pieChart.setEntryLabelTextSize(10f);
                        pieDataSet.setUsingSliceColorAsValueLineColor(true);
                        Legend legend = pieChart.getLegend();
                        legend.setEnabled(false);
//                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                        legend.setDrawInside(false);
//                        legend.setXEntrySpace(4f);
//                        legend.setYEntrySpace(0f);
//                        legend.setWordWrapEnabled(true);
                        pieChart.setElevation(10);

                        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, Highlight h) {
                                if (e == null) {
                                    return;
                                }
                                for (int i=0;i < values.size(); i++){
                                    if (values.get(i).getY() == e.getY()) {
                                        showchartdetails(values.get(i).getLabel(),values.get(i).getY(),piedata,i);
                                        Log.d("data_values",""+values.get(i).getLabel()+"--"+values.get(i).getY()+"**"+piedata+"//"+i);
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });
                    }
                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    SalesSecondaryDetails.clear();
                    targetALLAdapter.notifyDataSetChanged();
                    pieChart.setVisibility(View.GONE);
                    chrttxt.setVisibility(View.VISIBLE);
                    chrttxt.setText("No Data Found");
                    // Toast.makeText(Target_Vs_Secondary.this, "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                pieChart.setVisibility(View.GONE);
                chrttxt.setVisibility(View.VISIBLE);
                chrttxt.setText("No Data Found");
                //Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent ii=new Intent(this, HomeDashBoard.class);
        startActivity(ii);
    }
    private void Orderproduct_MTDsecondary(String fdate,String tdate) {
        try {

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            String fromstrdate = fdate;
            String tostrdate = tdate;


            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            //QryParam.put("axn", "get/target_sales_primary");
            //if(division_splitting.getVisibility() == View.VISIBLE){
            QryParam.put("divisionCode", div_Code);

            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
            QryParam.put("from_date", fromstrdate);
            // if(todate.getText().toString().equalsIgnoreCase("Till Date")){
            QryParam.put("to_date", tostrdate);
//            }else{
//                QryParam.put("to_date", todate.getText().toString());
//            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_Vs_Secondary.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getTargetDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDatasecondDetail(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Target_Vs_Secondary.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(Target_Vs_Secondary.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showchartdetails(final String label, float y, ArrayList<String> piedata, int position){

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(Target_Vs_Secondary.this);
        LayoutInflater inflater = Target_Vs_Secondary.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogue, null);
        dialogBuilder.setView(dialogView);
        TextView prdname = dialogView.findViewById(R.id.prd_name);
        TextView prdtarget = dialogView.findViewById(R.id.prd_Target);
        TextView prdcnt = dialogView.findViewById(R.id.prd_Sales);
        TextView prdarcheive = dialogView.findViewById(R.id.prd_Archeive);
        TextView prdgrowth = dialogView.findViewById(R.id.prd_growth);
        TextView prdpcm = dialogView.findViewById(R.id.prd_pcpm);
        TextView prddname = dialogView.findViewById(R.id.prd_dname);
        TextView infobut = dialogView.findViewById(R.id.info_but);
        TextView ok =dialogView.findViewById(R.id.okbut);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        prdname.setText(label);
        Log.d("position", String.valueOf(position));
        final String[] separateddoc =piedata.get(position).split("~");
        prdtarget.setText(separateddoc[1]);
        prdcnt.setText(separateddoc[2]);
        prdarcheive.setText(separateddoc[5]);
        prdgrowth.setText(separateddoc[6]);
        prdpcm.setText(separateddoc[3]);
        prddname.setText(separateddoc[4]);

        Log.d("webdata",separateddoc[7]+"/*/"+separateddoc[8]+"**"+separateddoc[9]+"**"+separateddoc[10]);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("MM");
        DateFormat outputyear = new SimpleDateFormat("yyyy");
        String inputDateStr=separateddoc[9];
        String inputDateStr1=separateddoc[10];

        Date date = null;
        Date date1 = null;
        try {
            date = inputFormat.parse(inputDateStr);
            date1 = inputFormat.parse(inputDateStr1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String outputDateStr = outputFormat.format(date);
        final String outputDateyear = outputyear.format(date);

        final String outputDateStr1 = outputFormat.format(date1);
        final String outputDateyear1 = outputyear.format(date1);

        Log.d("webdata",outputDateStr+"/*/"+outputDateyear+"**"+outputDateStr1+"**"+outputDateyear1);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        infobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oo = new Intent(Target_Vs_Secondary.this, Web_activity_secondary.class);
                oo.putExtra("sfcode",separateddoc[8]);
                oo.putExtra("fmon",outputDateStr);
                oo.putExtra("fyear",outputDateyear);
                oo.putExtra("tmon",outputDateStr1);
                oo.putExtra("tyear",outputDateyear1);
                oo.putExtra("Dcode",separateddoc[7]);
                oo.putExtra("Brandcd",separateddoc[0]);
                oo.putExtra("Brandname",label);
                oo.putExtra("sfname",sf_name);
                startActivity(oo);
            }
        });
        alertDialog.show();
    }
}
