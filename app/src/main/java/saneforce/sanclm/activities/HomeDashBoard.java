package saneforce.sanclm.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.zelory.compressor.Compressor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Order_Report.Activity.Target_details;
import saneforce.sanclm.Pojo_Class.TodayCalls;
import saneforce.sanclm.Pojo_Class.TodayTp;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.MissedDate;
import saneforce.sanclm.activities.Model.ModelNavDrawer;
import saneforce.sanclm.activities.Model.ModelWorkType;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.adapter_class.AdapterForCluster;
import saneforce.sanclm.adapter_class.AdapterMissedDate;
import saneforce.sanclm.adapter_class.Custom_Todaycalls_contents;
import saneforce.sanclm.adapter_class.NavigationItemAdapter;
import saneforce.sanclm.adapter_class.TodayCalls_recyclerviewAdapter;
import saneforce.sanclm.adapter_class.ViewPagerAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CheckPermission;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.Decompress;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.applicationCommonFiles.LocationTrack;
import saneforce.sanclm.fragments.AppConfiguration;
import saneforce.sanclm.fragments.CallFragment;
import saneforce.sanclm.fragments.DownloadMasterData;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.fragments.NewCallFragment;
import saneforce.sanclm.fragments.NewTrainingFragment;
import saneforce.sanclm.fragments.TrainingFragment;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DCRCallSelectionFilter;
import saneforce.sanclm.util.NetworkChangeReceiver;
import saneforce.sanclm.util.NetworkUtil;
import saneforce.sanclm.util.UpdateUi;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;
import static saneforce.sanclm.fragments.AppConfiguration.licenceKey;

public class HomeDashBoard extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, LocationListener {
    LinearLayout rl_calls, rl_presentation, rl_reports;
    static TableLayout tb_dwnloadSlides;
    public static DrawerLayout drawer;
    ImageView iv_navdrawer_menu, iv_setting, iv_reload, closePopupBtn, iv_logout, iv_noti;
    CommonUtilsMethods CommonUtilsMethods;
    TextView myplancap,tv_mWorktype, tv_cluster, tv_mworktype_cluster, tv_headquater, tv_userName, tv_worktype, tv_setting;
    public static TextView tv_todaycall_count;
    TextView tv_reload, tv_logout;
    static DataBaseHandler dbh;
    RecyclerView rv_worktype, rv_todayCalls;
    List<String> worktype_arr = new ArrayList<String>();
    HashMap<String, String> mWorktype = new HashMap<String, String>();
    PopupWindow popupWindow;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    NavigationView navigationView;
    JSONObject jsonObject = null;
    String db_connPath, db_slidedwnloadPath, SF_Code, SF_Type;
    DownloadMasters dwnloadMasterData;
    Cursor mCursor;
    Custom_Todaycalls_contents _custom_todaycalls_contents;
    ArrayList<Custom_Todaycalls_contents> TodayCallList;
    TodayCalls_recyclerviewAdapter todayCalls_recyclerviewAdapter;
    static int totalCounts = 0;
    ImageView Close;
    ListView listView;
    public static final int SUB_ACTIVITY_CREATE_USER = 10;
    ArrayList<String> worktypeNm = new ArrayList<String>();
    HashMap<String, String> mWorktypeListID = new HashMap<String, String>();
    ArrayList<ModelWorkType> mWrktypelist = new ArrayList<>();
    ArrayList<String> clusterNm = new ArrayList<String>();
    ArrayList<String> hqNm = new ArrayList<String>();
    HashMap<String, String> mClusterListID = new HashMap<String, String>();
    HashMap<String, String> mHQListID = new HashMap<String, String>();
    String mydaywTypCd, mydayclustrCd, currentDate;
    Dialog dialog;
    static String mydayhqCd, mydayhqname;
    CommonSharedPreference mCommonSharedPreference;
    LinearLayout ll_anim;
    AdapterForCluster clu_adpt;
    Dialog mis_dialog;
    static Context mContext;
    String tpflag = "";

    /**
     * downloading slides constatnts
     */
    private ArrayAdapter<File1> mAdapter;
    private boolean mReceiversRegistered;
    FileOutputStream outputStream;
    static double dtaSize, RDSize;
    static DecimalFormat df;
    static int tszflg = 0;
    static int rszflg = 0;
    static String Size;
    ArrayList<String> tempList = new ArrayList<String>();
    ArrayList<File1> files = new ArrayList<File1>();
    static ProgressBar bar;
    public static final String ID = "id";
    ProgressBar pBar;
    PieChart pieChart;
    int pBarCount = 1;
    boolean MasterList = false;
    TextView pbar_percentage;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String sfcoding, wrkType, cluType;
    String downloadFilepath = "ee";
    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;
    static UpdateUi mUpdateUi;
    boolean submitoffline = false;
    ViewPager viewpage;
    CheckPermission check;
    static UpdateUi callFragmentUi;
    static String GpsNeed = "0";
    boolean displayWrk = false;
    ArrayList<MissedDate> arr;
    RelativeLayout rl_up,l1_app_config;
    LinearLayout layoutBottomSheet, rl_Expenses, rl_act,mainDashbrd,menuDashbrd;
    boolean swipeFun = false;

    BottomSheetBehavior sheetBehavior;

    TextView tv_progress, tv_filesize, tv;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    String wrk_json;
    boolean MissedClicked = false;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Button btn_mydayplan_go;
    TextView tv_clusterView;
    ProgressDialog progressDialog;
    public static int tpCount = 0;
    String quizPAth = "MasterFiles/Options/Files/";
    JSONArray ja1;
    ListView nav_list;
    ArrayList<ModelNavDrawer> arrayNav = new ArrayList<>();
    NavigationItemAdapter navAdpt;
    static public String digital="0";
    String digitalOff="0";

    SharedPreferences licencesharedPreferences;
    String licence;
    String language;
    Context context;
    Resources resources;
    TextView tv_todaycall_head,tv_calls,tv_presentation,tv_reports;
    ProgressDialog progress;
    boolean accessStorageAllowedNew = false;

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(HomeDashBoard.this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(HomeDashBoard.this, "en");
            resources = context.getResources();
        }

        setContentView(R.layout.activity_new_homepage);


//          checkingPermissionsNew();



        CommonUtilsMethods = new CommonUtilsMethods(this);
        dbh = new DataBaseHandler(this);
        dialog = new Dialog(HomeDashBoard.this);
        progress = CommonUtilsMethods.createProgressDialog(HomeDashBoard.this);
        progress.show();
        mCommonSharedPreference = new CommonSharedPreference(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mainDashbrd=findViewById(R.id.mainDashbrd);
        menuDashbrd=findViewById(R.id.menuDashbrd);
        layoutBottomSheet=findViewById(R.id.bottom_sheet);

        l1_app_config=findViewById(R.id.l1_app_config);

        tv_todaycall_head=findViewById(R.id.tv_todaycall_head);
        tv_calls=findViewById(R.id.tv_calls);
        tv_presentation=findViewById(R.id.tv_presentation);
        tv_reports=findViewById(R.id.tv_reports);

        tb_dwnloadSlides = (TableLayout) findViewById(R.id.tableLayout1);
     // tb_dwnloadSlides.setVisibility(View.GONE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        if (NetworkUtil.getConnectivityStatus(HomeDashBoard.this) > 0)
            System.out.println("Connect");
        else System.out.println("No connection");

        mainDashbrd.setVisibility(View.VISIBLE);
        menuDashbrd.setVisibility(View.VISIBLE);

        tv_todaycall_head.setText(resources.getString(R.string.todaycall));
//        tv_calls.setVisibility(View.INVISIBLE);
//        tv_calls.setText(resources.getString(R.string.calls));
//        tv_presentation.setText(resources.getString(R.string.presentation));
//        tv_reports.setText(resources.getString(R.string.report));


        digital=mCommonSharedPreference.getValueFromPreference("Digital_offline");
              Intent intent=getIntent();
              digitalOff=intent.getStringExtra("masters");
              //Log.v("dashboard",digitalOff);
              if(digitalOff!=null && !digitalOff.isEmpty() )
              {
                  Log.d("dashboard",digitalOff);

              }else
              {
                  digitalOff="0";
              }

              if (digitalOff.equalsIgnoreCase("0")) {
                  Log.d("dashboard",digitalOff);

        }else
       {
           mainDashbrd.setVisibility(View.GONE);
           menuDashbrd.setVisibility(View.GONE);
           layoutBottomSheet.setVisibility(View.GONE);
           l1_app_config.setVisibility(View.GONE);
       }
        mContext = getBaseContext();
        addLogText(NetworkUtil.getConnectivityStatusString(HomeDashBoard.this));

        licencesharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        licence = licencesharedPreferences.getString(licenceKey, "");
        Log.v("licenceKey:", licence);


        /*      */
        CommonUtilsMethods.FullScreencall(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshPendingFunction();
            }
        }, 200);

    }

    private void hideTP() {
        arrayNav.remove(3);
    }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(HomeDashBoard.this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(HomeDashBoard.this, "en");
            resources = context.getResources();
        }
        CommonUtilsMethods.FullScreencall(this);
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.v("Latitude_inside:" + location.getLatitude(), ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUB_ACTIVITY_CREATE_USER && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {

            }
        }
        if (requestCode == 678) {
            Bundle extra = data.getExtras();
            popupQuiz(extra.getString("value"));
        }
    }

    public void TodayCalls() {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject json = new JSONObject();
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        map.clear();
        map.put("SF", SF_Code);
        try {
            json.put("SF", SF_Code);

            Call<List<TodayCalls>> tdaycalls = apiService.todaycalls(String.valueOf(json));
            tdaycalls.enqueue(Todaycalls);
            map.put("ReqDt", currentDate);
            json.put("ReqDt", currentDate);
            Log.v("printing_request", json.toString());
        } catch (Exception e) {

        }

        if (CommonUtilsMethods.isOnline(HomeDashBoard.this)) {
            Call<List<TodayTp>> tdaytp = apiService.todayTP(String.valueOf(json));
            tdaytp.enqueue(TodayTp);
        } else {
            tv_worktype.setText(resources.getString(R.string.worktype)+" : " + mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME));
            tv_cluster.setText(resources.getString(R.string.worktype)+" : " + mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME));
        }
       /* if(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME)=="null" || mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME).trim().isEmpty()) {


        }
        else{

        }*/

    }


    public Callback<List<TodayCalls>> Todaycalls = new Callback<List<TodayCalls>>() {
        @Override
        public void onResponse(Call<List<TodayCalls>> call, Response<List<TodayCalls>> response) {
            System.out.println("checkUser is sucessfuld :+todaycalls" + response.isSuccessful());

            int poss;

            TodayCallList = new ArrayList<Custom_Todaycalls_contents>();
            TodayCallList.clear();
            dbh.open();
            Cursor cur1 = dbh.select_json_list();
            if (cur1.getCount() > 0) {
                while (cur1.moveToNext()) {
                    Log.v("Cursor_today_Cal", cur1.getString(1));
                    _custom_todaycalls_contents = new Custom_Todaycalls_contents(cur1.getString(4), cur1.getString(2), cur1.getString(1), cur1.getString(3), String.valueOf(cur1.getInt(0)), 0, cur1.getString(5), cur1.getString(6), true);

                    TodayCallList.add(_custom_todaycalls_contents);

                }

                Log.v("todaycalllist__print", String.valueOf(TodayCallList.size()));
                todayCalls_recyclerviewAdapter = new TodayCalls_recyclerviewAdapter(getApplicationContext(), TodayCallList);
                tv_todaycall_count.setText(resources.getString(R.string.total)+" " + TodayCallList.size() + " "+resources.getString(R.string.calls));
                rv_todayCalls.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv_todayCalls.setLayoutManager(mLayoutManager);
                rv_todayCalls.setItemAnimator(new DefaultItemAnimator());
                rv_todayCalls.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                rv_todayCalls.setAdapter(todayCalls_recyclerviewAdapter);

            }
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;

                try {

                    poss = TodayCallList.size();
                    Log.v("Today_Check_response", response.body() + "");

                    List<TodayCalls> todayCalls = response.body();
                    Log.v("printing_getting", String.valueOf(todayCalls.size()) + response.body());
                    for (int i = 0; i < todayCalls.size(); i++) {
                        Log.v("printing_getting_sd", todayCalls.get(i).getADetSLNo());
                        _custom_todaycalls_contents = new Custom_Todaycalls_contents(todayCalls.get(i).getCustCode(), todayCalls.get(i).getCustName(), todayCalls.get(i).getADetSLNo(), todayCalls.get(i).getVstTime(), todayCalls.get(i).getCustType(), todayCalls.get(i).getSynced(), "", "", false);


                        TodayCallList.add(_custom_todaycalls_contents);

                    }
                    Log.v("todaycalllist__print_in", String.valueOf(TodayCallList.size()));
                    todayCalls_recyclerviewAdapter = new TodayCalls_recyclerviewAdapter(getApplicationContext(), TodayCallList);
                    tv_todaycall_count.setText(resources.getString(R.string.total)+" " + todayCalls.size() + " "+resources.getString(R.string.calls));
                    rv_todayCalls.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rv_todayCalls.setLayoutManager(mLayoutManager);
                    rv_todayCalls.setItemAnimator(new DefaultItemAnimator());
                    rv_todayCalls.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    rv_todayCalls.setAdapter(todayCalls_recyclerviewAdapter);

                    if (!submitoffline) {
                        callFragmentUi.updatingui();
                        Log.v("call_fragment_1", "are_called");
                    }
                    //catVisitDetail();


                } catch (Exception e) {

                }

            } else {
                callFragmentUi.updatingui();
                Log.v("call_fragment_12", "are_called");
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
            tv_todaycall_count.setText(resources.getString(R.string.total)+" " + TodayCallList.size() + " "+resources.getString(R.string.calls));
            dbh.close();
        }

        @Override
        public void onFailure(Call<List<TodayCalls>> call, Throwable t) {
            //catVisitDetail();
            Log.v("call_fragment_123", "are_called");
            callFragmentUi.updatingui();
        }
    };

    public Callback<List<TodayTp>> TodayTp = new Callback<List<TodayTp>>() {
        @Override
        public void onResponse(Call<List<TodayTp>> call, Response<List<TodayTp>> response) {
            List<TodayTp> todaytp = null;
            sharedpreferences.edit().remove(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE).commit();
            tv_worktype.setText(resources.getString(R.string.worktype)+" : ");
            tv_cluster.setText(resources.getString(R.string.cluster)+" : ");

//            tv_calls.setText(resources.getString(R.string.calls));
//            tv_presentation.setText(resources.getString(R.string.presentation));
//            tv_reports.setText(resources.getString(R.string.report));

            Log.v("tagg", response.toString());

            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    todaytp = response.body();
                    Log.e("Tday_tp_Calling ", "dp " + todaytp.get(0).getPlNm() + " wrik_type " + todaytp.get(0).getWTNm() + " " + todaytp.get(0).getHQNm() + " " + todaytp.get(0).getFWFlg() + " " + todaytp.get(0).getTpVwFlg());

                    tv_worktype.setText(resources.getString(R.string.worktype)+" : " + todaytp.get(0).getWTNm());
                    tv_cluster.setText(resources.getString(R.string.cluster)+" : " + todaytp.get(0).getPlNm());
                    tpflag = todaytp.get(0).getTpVwFlg();

                    if (todaytp.get(0).getWTNm().contains(",")) {
                        String ss = todaytp.get(0).getWTNm();
                        ss = ss.replace(",", "");
                        // editor.putString(CommonUtils.TAG_WORKTYPE_NAME, "");
                        editor.putString(CommonUtils.TAG_WORKTYPE_NAME, ss);
                    } else
                        // editor.putString(CommonUtils.TAG_WORKTYPE_NAME, "");
                        editor.putString(CommonUtils.TAG_WORKTYPE_NAME, todaytp.get(0).getWTNm());

                    editor.putString(CommonUtils.TAG_WORKTYPE_CODE, todaytp.get(0).getWT());
                    editor.putString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE, todaytp.get(0).getPl());
                    editor.putString(CommonUtils.TAG_SF_CODE, todaytp.get(0).getSFCode());
                    editor.putString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, todaytp.get(0).getPlNm());
                    if (SF_Type.equalsIgnoreCase("2")) {
                        editor.putString(CommonUtils.TAG_SF_HQ, todaytp.get(0).getHQNm());
                        editor.putString("sub_sf_code", todaytp.get(0).getSFMem());
                        Log.v("sf_meme_are", todaytp.get(0).getSFMem() + "jkl");
                    }

                    Log.v("sfCode_check", todaytp.get(0).getSFCode() + "cluster" + todaytp.get(0).getPl());
                    editor.commit();


                   /* for (int i = 0; i < jointwork.size(); i++) {
                        String sfCode = jointwork.get(i).getCode();
                        String Name = jointwork.get(i).getName();
                        String sfName = jointwork.get(i).getSfName();
                        String RptSf = jointwork.get(i).getReportingToSF();
                        String OwnDiv = jointwork.get(i).getOwnDiv();
                        String DivCd = jointwork.get(i).getDivisionCode();
                        String SfStatus = jointwork.get(i).getSFStatus();
                        String ActFlg = jointwork.get(i).getActFlg();
                        String UserNm = jointwork.get(i).getUsrDfdUserName();
                        String SfType = jointwork.get(i).getSfType();
                        String Desig = jointwork.get(i).getDesig();

                        dbh.insert_jointworkManagers(sfCode,Name,sfName,RptSf,OwnDiv,DivCd,SfStatus,ActFlg,UserNm,SfType,Desig);
                    }*/
                    dbh.close();
                } catch (Exception e) {
                }
                if (todaytp.size() == 0) {
                    editor.putString(CommonUtils.TAG_WORKTYPE_NAME, "");
                    editor.putString(CommonUtils.TAG_WORKTYPE_CODE, "");
                    editor.putString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE, "");
                    editor.putString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, "");
                    editor.commit();
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
            String wrkNAm = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, null);
            Log.v("data1", wrkNAm);
            String wrkcluNAm = sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, null);
            if (TextUtils.isEmpty(wrkNAm) || wrkNAm.equalsIgnoreCase("null") || wrkNAm.equalsIgnoreCase("")) {
                Log.v("checkme", "condition is working");
                if (tb_dwnloadSlides.getVisibility() != View.VISIBLE)
                    if (!tpflag.isEmpty() || !tpflag.equals((""))) {
                        if (tpflag.equals("1")) {
                            Worktype();
                        }
                    }
            }
            if (!tpflag.isEmpty() || !tpflag.equals((""))) {
                if (tpflag.equals("1")) {
                    if (tb_dwnloadSlides.getVisibility() != View.VISIBLE) {
                        Worktype();
                    }

                }
            } else {
                if (tb_dwnloadSlides.getVisibility() != View.VISIBLE) {
                    Worktype();
                }
            }
        }

        @Override
        public void onFailure(Call<List<TodayTp>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_calls:
                /*if(mCommonSharedPreference.getValueFromPreference("GpsFilter").equals("1") && mCommonSharedPreference.getValueFromPreference("geoneed").equals("0"))
                CheckLocation();
                else*/
                callDCR();
                break;

            case R.id.rl_presentation:
                CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);

                break;

            case R.id.rl_reports:
                CommonUtilsMethods.CommonIntentwithNEwTask(ReportActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @SuppressLint("Range")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {

            case R.id.iv_navdrawer:
                navAdpt.notifyDataSetChanged();
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                } else {
                    drawer.openDrawer(GravityCompat.START);
                    layoutBottomSheet.setAlpha(5);
                }
                break;

            case R.id.iv_settings:
                layoutBottomSheet.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppConfiguration()).commit();
                break;

            case R.id.tv_setting:
                layoutBottomSheet.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppConfiguration()).commit();
                break;

            case R.id.iv_reload:
                layoutBottomSheet.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DownloadMasterData()).commit();
                break;
            case R.id.iv_noti:
                //Toast.makeText(HomeDashBoard.this,"Under Development",Toast.LENGTH_SHORT).show();
                //getNewNotification(); recent notification
                getNotification();
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DownloadMasterData()).commit();
                break;

            case R.id.tv_reload:
                layoutBottomSheet.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DownloadMasterData()).commit();
                break;

            case R.id.iv_logout:
              /*  Intent intent = new Intent(this, DownloadListViewSubActivity.class);
                startActivityForResult(intent, SUB_ACTIVITY_CREATE_USER);*/
                CommonUtilsMethods.CommonIntentwithNEwTask(LoginActivity.class);
                break;

            case R.id.tv_logout:
              /*  Intent intent = new Intent(this, DownloadListViewSubActivity.class);
                startActivityForResult(intent, SUB_ACTIVITY_CREATE_USER);*/
                CommonUtilsMethods.CommonIntentwithNEwTask(LoginActivity.class);
                break;
            case R.id.iv_close:
                SharedPreferences slide = getSharedPreferences("slide", 0);
                SharedPreferences.Editor edit = slide.edit();
                edit.putString("slide_download", "0");
                edit.commit();
                tb_dwnloadSlides.setVisibility(View.GONE);
                break;
        }
        return false;
    }


    public void popupWorkType(String msg, final String json, final int typ) {
        final Dialog dia = new Dialog(HomeDashBoard.this);
        dia.setContentView(R.layout.popup_wrktyp);
        dia.show();
        TextView txt_msg = (TextView) dia.findViewById(R.id.txt_msg);
        txt_msg.setText(msg);
        Button btn_overwrite = (Button) dia.findViewById(R.id.btn_overwrite);
        Button btn_half = (Button) dia.findViewById(R.id.btn_half);
        Button btn_cancel = (Button) dia.findViewById(R.id.btn_cancel);
        mCommonSharedPreference.setValueToPreference("sub_sf_code", mydayhqCd);
        mCommonSharedPreference.setValueToPreference("tpdate", CommonUtilsMethods.getCurrentInstance());
        editor.commit();
        btn_overwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jso = null;
                try {
                    jso = new JSONObject(json);
                    jso.put("InsMode", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("work_type_overwrite", jso.toString());
                if (typ == 1) {
                    dbh.open();
                    dbh.insert_mydayplan(jso.toString());
                    dbh.close();
                } else {
                    Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
                    Call<ResponseBody> tdayTP = apiService.SVtodayTP(String.valueOf(jso));
                    tdayTP.enqueue(svTodayTP);
                }
                setMDPValue();
                dia.cancel();
            }
        });
        btn_half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jso = null;
                try {
                    jso = new JSONObject(json);
                    jso.put("InsMode", "2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("work_type_overwrite", jso.toString());
                if (typ == 1) {
                    dbh.open();
                    dbh.insert_mydayplan(jso.toString());
                    dbh.close();
                } else {
                    Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
                    Call<ResponseBody> tdayTP = apiService.SVtodayTP(String.valueOf(jso));
                    tdayTP.enqueue(svTodayTP);
                }
                setMDPValue();
                dia.cancel();
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.cancel();
            }
        });
        /* */

    }

    public void popupCluster(ArrayList<PopFeed> array) {
        final Dialog dialog = new Dialog(HomeDashBoard.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_cluster);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        ListView list_cluster = (ListView) dialog.findViewById(R.id.list_cluster);
        clu_adpt = new AdapterForCluster(HomeDashBoard.this, array);
        list_cluster.setAdapter(clu_adpt);
        clu_adpt.notifyDataSetChanged();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
                Log.v("search_view_str", s);
                clu_adpt.getFilter().filter(s);
                return false;
            }
        });

        AdapterForCluster.bindCluster(new DCRCallSelectionFilter() {
            @Override
            public void itemClick(String drname, String drcode) {
                tv_clusterView.setText(drname);
                //rv_cluster.setVisibility(View.INVISIBLE);
                btn_mydayplan_go.setVisibility(View.VISIBLE);

                mydayclustrCd = drcode;
                dialog.dismiss();

            }
        });

    }

    public void Worktype() {

        dialog.setContentView(R.layout.activity_myday_plan);
        TextView tv_worktype = (TextView) dialog.findViewById(R.id.et_mydaypln_worktype);
        TextView tv_headquater = (TextView) dialog.findViewById(R.id.et_mydaypln_HQ);
        tv_clusterView = (TextView) dialog.findViewById(R.id.et_mydaypln_cluster);
        final ImageView Close = (ImageView) dialog.findViewById(R.id.iv_close);
        final EditText et_remark = (EditText) dialog.findViewById(R.id.et_mydaypln_remarks);
        final ListView rv_wtype = (ListView) dialog.findViewById(R.id.rv_worktypelist);
        final ListView rv_cluster = (ListView) dialog.findViewById(R.id.rv_clusterlist);
        final ListView rv_hqlist = (ListView) dialog.findViewById(R.id.rv_hqlist);
        btn_mydayplan_go = (Button) dialog.findViewById(R.id.btn_mydaypln_go);
        ll_anim = (LinearLayout) dialog.findViewById(R.id.ll_anim);

        TextView myplancap = (TextView)dialog.findViewById(R.id.tv_todayplan);
        TextView tv_mWorktype = (TextView)dialog.findViewById(R.id.tv_mydaypln_worktype);
        TextView tv_headquaters = (TextView)dialog.findViewById(R.id.tv_mydaypln_HQ);
        TextView tv_mworktype_cluster = (TextView)dialog.findViewById(R.id.tv_mydaypln_cluster);
        TextView tv_myremarks = (TextView)dialog.findViewById(R.id.tv_remarks);

//        context = LocaleHelper.setLocale(HomeDashBoard.this, language);
//        resources = context.getResources();
//        myplancap.setText(resources.getString(R.string.todayworktype));
//        tv_mWorktype.setText(resources.getString(R.string.worktype));
//        tv_mworktype_cluster.setText(resources.getString(R.string.cluster));
//        tv_headquaters.setText(resources.getString(R.string.headquater));
//        tv_myremarks.setText(resources.getString(R.string.remarks));
//        btn_mydayplan_go.setText(resources.getString(R.string.go));
//        tv_worktype.setText(resources.getString(R.string.select)+""+resources.getString(R.string.worktype));
//        tv_headquater.setText(resources.getString(R.string.select)+""+resources.getString(R.string.headquater));
//        tv_clusterView.setText(resources.getString(R.string.select)+""+resources.getString(R.string.cluster));

        final ArrayList<PopFeed> array_cluster = new ArrayList<>();
        tv_headquater.setText(sharedpreferences.getString(CommonUtils.TAG_SF_HQ, ""));

        Log.d("check_data", sharedpreferences.getString(CommonUtils.TAG_SF_HQ, ""));

        if (!TextUtils.isEmpty(sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, "")) && !displayWrk) {
            dbh.open();
            Cursor currr = dbh.select_worktypeListBasedCode(sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CODE, ""));
            while (currr.moveToNext()) {
                if (!currr.getString(4).equalsIgnoreCase("Y")) {
                    tv_clusterView.setEnabled(false);
                    tv_clusterView.setAlpha(.5f);
                }
            }
            dbh.close();
            tv_worktype.setText(sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, ""));
            tv_clusterView.setText(sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, ""));


            mydaywTypCd = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CODE, "");
            mydayclustrCd = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE, "");
            et_remark.setText(sharedpreferences.getString("remrk", ""));
            mydayhqCd = sharedpreferences.getString("sub_sf_code", "");
            dbh.open();
            if (SF_Type.equalsIgnoreCase("2")) {
                Cursor mCur = dbh.select_doctors_bySf(mydayhqCd, mydayclustrCd);
                if (mCur.getCount() < 0) {
                    Updatecluster();
                }
            }
        }

        dbh.open();
        mCursor = dbh.select_worktypeList(SF_Code);
        mWorktypeListID.clear();
        mWrktypelist.clear();
        worktypeNm.clear();
        Log.d("sfcodes", "" + SF_Code);
        Log.d("checkcurcount", "" + mCursor.getCount());
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                if (displayWrk && mCursor.getString(4).equalsIgnoreCase("Y")) {
                }//other than fieldwork in missed date
                else {
                    mWorktypeListID.put(mCursor.getString(1), mCursor.getString(2));
                    mWrktypelist.add(new ModelWorkType(mCursor.getString(2), mCursor.getString(1), mCursor.getString(4)));
                    worktypeNm.add(mCursor.getString(2));
                    Log.d("display_wrktype", mCursor.getString(4));
                }
            }
        }

        if (SF_Type.equalsIgnoreCase("2"))
            mCursor = dbh.select_ClusterList(mydayhqCd);
        else
            mCursor = dbh.select_ClusterList();
        mClusterListID.clear();
        clusterNm.clear();
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                mClusterListID.put(mCursor.getString(1), mCursor.getString(2));
                clusterNm.add(mCursor.getString(2));
                array_cluster.add(new PopFeed(mCursor.getString(2), mCursor.getString(1)));

            }
        }

        if (SF_Type.equalsIgnoreCase("1")) {
           /* tv_headquater.setText(tv_userName.getText().toString());
            mCommonSharedPreference.setValueToPreference("hq_name",tv_userName.getText().toString());*/
        } else {
                       /* if(!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_HQ)))
            tv_headquater.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_HQ));*/
            mHQListID.clear();
            hqNm.clear();
            mCursor = dbh.select_hqlist_manager();
            if (mCursor.getCount() != 0) {
                if (mCursor.moveToFirst()) {
                    do {
                        Log.v("Name_hqlist", mCursor.getString(2));
                        mHQListID.put(mCursor.getString(1), mCursor.getString(2));
                        hqNm.add(mCursor.getString(2));
                    } while (mCursor.moveToNext());

                }
            }
        }

        tv_worktype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rv_wtype.getVisibility() == View.VISIBLE) {
                    rv_wtype.setVisibility(View.INVISIBLE);
                } else {
                    rv_wtype.setVisibility(View.VISIBLE);
                    rv_wtype.setAdapter(new ArrayAdapter<String>(HomeDashBoard.this, R.layout.listview_items, worktypeNm));
                }
                Log.d("dataawise", worktypeNm.toString() + "-" + worktypeNm.size());
            }
        });

        rv_wtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {



                tv_worktype.setText(parent.getItemAtPosition(position).toString());
                rv_wtype.setVisibility(View.INVISIBLE);

                for (int k = 0; k < mWrktypelist.size(); k++) {
                    ModelWorkType kk = mWrktypelist.get(k);
                    if (kk.getName().equalsIgnoreCase(parent.getItemAtPosition(position).toString())) {
                        mydaywTypCd = kk.getCode();
                        if (kk.getFlag().equalsIgnoreCase("Y")) {
                            tv_clusterView.setEnabled(true);
                            tv_clusterView.setAlpha(1f);
                        } else {
                            tv_clusterView.setEnabled(false);
                            tv_clusterView.setAlpha(.5f);
                        }
                        k = mWrktypelist.size();
                    }
                }
/*
                            for (Map.Entry<String, String> entry : mWorktypeListID.entrySet()) {
                                if (entry.getValue().equals(parent.getItemAtPosition(position).toString())) {
                                    mydaywTypCd = entry.getKey();
                                    if(entry.getValue().equalsIgnoreCase("Field Work")){
                                        tv_cluster.setEnabled(true);
                                        tv_cluster.setAlpha(1f);
                                    }
                                    else{
                                        tv_cluster.setEnabled(false);
                                        tv_cluster.setAlpha(.5f);
                                    }
                                }
                            }
*/
            }
        });

        tv_clusterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv_cluster.getVisibility() == View.VISIBLE) {
                    rv_cluster.setVisibility(View.INVISIBLE);
                    btn_mydayplan_go.setVisibility(View.VISIBLE);
                } else {

                    if (SF_Type.equalsIgnoreCase("2")) {
                        dbh.open();
                        Log.v("culster_name_printing", mydayhqCd + "");
                        mCursor = dbh.select_ClusterList(mydayhqCd);
                        mClusterListID.clear();
                        clusterNm.clear();
                        array_cluster.clear();
                        Log.v("culster_name_printing", mCursor.getCount() + "");
                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                mClusterListID.put(mCursor.getString(1), mCursor.getString(2));
                                clusterNm.add(mCursor.getString(2));
                                array_cluster.add(new PopFeed(mCursor.getString(2), mCursor.getString(1)));

                            }
                        }
                    }
                    //popupCluster(array_cluster);
                    rv_cluster.setVisibility(View.VISIBLE);
                    btn_mydayplan_go.setVisibility(View.INVISIBLE);

                    Log.e("Home_dash_board", String.valueOf(clusterNm));

                    rv_cluster.setAdapter(new ArrayAdapter<String>(HomeDashBoard.this, R.layout.listview_items, clusterNm));
                }
            }
        });

        rv_cluster.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                tv_clusterView.setText(parent.getItemAtPosition(position).toString());
                rv_cluster.setVisibility(View.INVISIBLE);
                btn_mydayplan_go.setVisibility(View.VISIBLE);
                for (Map.Entry<String, String> entry : mClusterListID.entrySet()) {
                    if (entry.getValue().equals(parent.getItemAtPosition(position).toString())) {
                        mydayclustrCd = entry.getKey();
                    }
                }
            }
        });

        tv_headquater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SF_Type.equalsIgnoreCase("2")) {
                    if (rv_hqlist.getVisibility() == View.VISIBLE) {
                        rv_hqlist.setVisibility(View.INVISIBLE);
                    } else {
                        rv_hqlist.setVisibility(View.VISIBLE);
                        rv_hqlist.setAdapter(new ArrayAdapter<String>(HomeDashBoard.this, R.layout.listview_items, hqNm));
                    }
                    tv_headquater.setText(mydayhqname);
                } else {
                    tv_headquater.setText(tv_userName.getText().toString());
                    //    tv_headquater.setText("");
                }
            }

        });

        rv_hqlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                if (rv_cluster.getVisibility() == View.VISIBLE) {
                    rv_cluster.setVisibility(View.INVISIBLE);
                }

                tv_clusterView.setText(resources.getString(R.string.select_cluster));
                tv_headquater.setText(parent.getItemAtPosition(position).toString());
                rv_hqlist.setVisibility(View.INVISIBLE);
                for (Map.Entry<String, String> entry : mHQListID.entrySet()) {
                    if (entry.getValue().equals(parent.getItemAtPosition(position).toString())) {
                        mydayhqCd = entry.getKey();
                        mydayhqname = entry.getValue();
                    }
                }
                Log.v("printing_sF_code", mydayhqCd);

                Updatecluster();
              /*  dwnloadMasterData = new DownloadMasters(getApplicationContext(), db_connPath, db_slidedwnloadPath, mydayhqCd,SF_Code);
                ll_anim.setVisibility(View.VISIBLE);
                MasterList=true;
                dwnloadMasterData.drList();
                dwnloadMasterData.chemsList();
                dwnloadMasterData.unDrList();
                dwnloadMasterData.stckList();
                dwnloadMasterData.terrList();
                dwnloadMasterData.jointwrkCall();
*/

            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
        }

        btn_mydayplan_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("checking_for_enable", tv_clusterView.isEnabled() + "");
                if (tv_worktype.getText().toString().equalsIgnoreCase(resources.getString(R.string.select_worktype))) {
                    Toast.makeText(HomeDashBoard.this, resources.getString(R.string.fill_the)+" "+resources.getString(R.string.worktype1)+" "+resources.getString(R.string.field_), Toast.LENGTH_SHORT).show();
//                } else if (tv_clusterView.getText().toString().equalsIgnoreCase(resources.getString(R.string.select_cluster)) && !displayWrk && tv_clusterView.isEnabled()) {
//                    Toast.makeText(HomeDashBoard.this, resources.getString(R.string.fill_the)+" "+resources.getString(R.string.cluster1)+" "+resources.getString(R.string.field_), Toast.LENGTH_SHORT).show();
                } else if (tv_headquater.getText().toString().equalsIgnoreCase("null") || tv_headquater.getText().toString().equalsIgnoreCase(resources.getString(R.string.select_headquater))) {
                    Toast.makeText(HomeDashBoard.this, resources.getString(R.string.fill_the)+" "+resources.getString(R.string.headquater1)+" "+resources.getString(R.string.field_), Toast.LENGTH_SHORT).show();
                } else {

                    JSONObject json = new JSONObject();

                    try {
                        json.put("SF", SF_Code);
                        if (SF_Type.equalsIgnoreCase("2"))
                            json.put("SFMem", mydayhqCd);
                        else
                            json.put("SFMem", SF_Code);
                        json.put("Pl", mydayclustrCd);
                        if (tv_clusterView.getText().toString().equalsIgnoreCase("SELECT CLUSTER"))
                            json.put("PlNm", "");
                        else
                            json.put("PlNm", tv_clusterView.getText().toString());
                        json.put("WT", mydaywTypCd);
                        json.put("WTNMm", tv_worktype.getText().toString());
                        json.put("Rem", et_remark.getText().toString());
                        json.put("Div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
                        json.put("location", "");
                        json.put("InsMode", "0");
                        json.put("TPDt", CommonUtilsMethods.getCurrentInstance() + " " + CommonUtilsMethods.getCurrentTime());
                    } catch (Exception e) {

                    }
                    if (displayWrk) {//missed date

                        sendMissedDateCall(tv_worktype.getText().toString(), mydaywTypCd, mydayhqCd, mydayhqname);

                    } else {
                        editor.putString(CommonUtils.TAG_WORKTYPE_NAME, tv_worktype.getText().toString());
                        editor.putString(CommonUtils.TAG_WORKTYPE_CODE, mydaywTypCd);
                        editor.putString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE, mydayclustrCd);
                        editor.putString(CommonUtils.TAG_SF_CODE, SF_Code);
                        editor.putString(CommonUtils.TAG_SF_HQ, tv_headquater.getText().toString());
                        editor.putString("remrk", sharedpreferences.getString("remrk", ""));

                        if (SF_Type.equalsIgnoreCase("1")) {
                            mCommonSharedPreference.setValueToPreference("sub_sf_code", SF_Code);
                            mCommonSharedPreference.setValueToPreference("tmp_sub_sf_code", SF_Code);
                            mydayhqCd = SF_Code;
                        } else {
                            mCommonSharedPreference.setValueToPreference("sub_sf_code", mydayhqCd);
                            mCommonSharedPreference.setValueToPreference("tmp_sub_sf_code", mydayhqCd);
                        }
                        if (tv_clusterView.getText().toString().equalsIgnoreCase("SELECT CLUSTER"))
                            editor.putString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, "");
                        else
                            editor.putString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, tv_clusterView.getText().toString());
                        editor.putString("remrk", et_remark.getText().toString());
                        editor.putString(CommonUtils.TAG_TMP_WORKTYPE_NAME, tv_worktype.getText().toString());
                        editor.putString(CommonUtils.TAG_TMP_WORKTYPE_CODE, mydaywTypCd);
                        editor.putString(CommonUtils.TAG_TMP_WORKTYPE_CLUSTER_CODE, mydayclustrCd);
                        editor.putString(CommonUtils.TAG_TMP_SF_CODE, SF_Code);
                        editor.putString(CommonUtils.TAG_TMP_SF_HQ, tv_headquater.getText().toString());
                        editor.putString(CommonUtils.TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME, tv_clusterView.getText().toString());
                    }

                    Log.e("values_myday", json.toString() + "db_connPath " + db_connPath + "subsf" + mCommonSharedPreference.getValueFromPreference("sub_sf_code"));
                    wrk_json = json.toString();
                    if (displayWrk) {
                        editor.commit();
                        mCommonSharedPreference.setValueToPreference("missed", "true");
                                   /* Intent i=new Intent(HomeDashBoard.this, DCRCallSelectionActivity.class);
                                    startActivity(i);
                                    dialog.dismiss();*/
                    } else {
                        if (mCommonSharedPreference.getValueFromPreference("net_con").equalsIgnoreCase("connect")) {
                            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
                            Call<ResponseBody> tdayTP = apiService.SVtodayTP(String.valueOf(json));
                            tdayTP.enqueue(svTodayTP);
                        } else {
                            saveOfflineMDP(json.toString());
                        }
                    }

                }
            }
        });

        Close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /*
     */
    public Callback<ResponseBody> svTodayTP = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            System.out.println("myday_save :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;

                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    Log.v("json_object_wrktype", is.toString());
                    jsonObject = new JSONObject(is.toString());
                    Log.v("json_object_printing", String.valueOf(jsonObject) + "code" + mydayhqCd);
                    if (jsonObject.getString("success").equals("true")) {
                        mCommonSharedPreference.setValueToPreference("sub_sf_code", mydayhqCd);
                        mCommonSharedPreference.setValueToPreference("tpdate", CommonUtilsMethods.getCurrentInstance());

                        //https://stackoverflow.com/questions/31175601/how-can-i-change-default-toast-message-color-and-background-color-in-android
                        /*view.getBackground().setColorFilter(YOUR_BACKGROUND_COLOUR, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(YOUR_TEXT_COLOUR);*/

                        mCommonSharedPreference.setValueToPreference("workType", "true");
                        editor.commit();
                        Log.v("tv_work)tupe", tv_worktype.getText().toString());
                        if (displayWrk) {
                            mCommonSharedPreference.setValueToPreference("missed", "true");
                            Intent i = new Intent(HomeDashBoard.this, DCRCallSelectionActivity.class);
                            startActivity(i);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), jsonObject.getString("Msg"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            setMDPValue();
                           /* tv_worktype.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME));
                            tv_cluster.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME));
                            tv_worktype.setText("Work Type : " + tv_worktype.getText().toString());
                            tv_cluster.setText("Cluster : " + tv_cluster.getText().toString());*/
                        }
                        dialog.dismiss();
                        tpflag = "0";
                    } else {
                        Log.v("work_plan_detail", jsonObject.getString("Msg"));
                        Toast.makeText(getApplicationContext(), jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                        popupWorkType(jsonObject.getString("Msg"), wrk_json, 0);
                    }
               /* try {
                    jsonData = response.body().string();
                    jsonObject = new JSONObject(jsonData);
                    Log.v("json_object_printing", String.valueOf(jsonObject));

                    if (jsonObject.getString("success").equals("true")) {
                        Toast.makeText(getApplicationContext(),"TodayCAlls"+ jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                        mCommonSharedPreference.setValueToPreference("workType","true");
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getApplicationContext(),"TodayCAlls22"+jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();

                    }*/
                    //  Log.e("Tday ","dp "+todaytp.get(0).getPlNm());
                    /*tv_worktype.setText("Work Type : "+todaytp.get(0).getWTNm());
                    tv_cluster.setText("Cluster : "+todaytp.get(0).getPlNm());

                    editor.putString(CommonUtils.TAG_WORKTYPE_NAME, todaytp.get(0).getWTNm());
                    editor.putString(CommonUtils.TAG_WORKTYPE_CODE, todaytp.get(0).getWT());
                    editor.putString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE,todaytp.get(0).getPl());
                    editor.putString(CommonUtils.TAG_SF_CODE, todaytp.get(0).getSFCode());
                    editor.commit();*/

                    // dbh.close();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
            Log.v("printing_finall_sub", mCommonSharedPreference.getValueFromPreference("sub_sf_code"));
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
             Toast.makeText(context, "On Failure " +t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    };


    public void SlidesDownloader(int x) {

        Log.v("CLEAR_DATA","CHECKING_DATA");
        files.clear();

        try {
            dbh.open();

            Cursor cur = dbh.select_slidesUrlPath();
            if (cur.getCount() == 0) {
                Log.v("slide_downloder_1", "are_activated");
            }
            Log.v("slide_downloder_1", "are_activated" + cur.getCount());
            if ((cur.getCount() > 0)) {
                while (cur.moveToNext()) {
                    //if(downloadFilepath.contains(cur.getString(5))){}else{
                    files.add(new File1(db_slidedwnloadPath + cur.getString(5)));
                    Log.v("slide_downloder_123", "are_activated_in" + db_slidedwnloadPath + cur.getString(5));
                    downloadFilepath += cur.getString(5) + ",";
                    // }
                }
                SharedPreferences slide = getSharedPreferences("slide", 0);
                SharedPreferences.Editor edit = slide.edit();
                edit.putString("slide_download", "1");
                edit.commit();
                Log.v("slide_downloder", "are_activated");
                if (x == 0)
                    tb_dwnloadSlides.setVisibility(View.VISIBLE);
                listView.setAdapter(mAdapter = new ArrayAdapter<File1>(this, R.layout.custom_download_row, R.id.tv_setfilename, files) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        Log.e("GetFilesView", String.valueOf(files));
                        View v = super.getView(position, convertView, parent);

                        /*CHECKING*/
                       // tb_dwnloadSlides.setVisibility(View.VISIBLE);


                        updateRow(getItem(position), v, files.get(position).size);
                        return v;
                    }
                });


                //if (savedInstanceState == null) {
                Intent intent = new Intent(this, DownloadingService.class);
                intent.putParcelableArrayListExtra("files", new ArrayList<File1>(files));
                startService(intent);
                //}
                registerReceiver();

            } else {
               //tb_dwnloadSlides.setVisibility(View.GONE);
                if (digitalOff.equalsIgnoreCase("0"))
                {
                    tb_dwnloadSlides.setVisibility(View.GONE);
                }else
                {
                    tb_dwnloadSlides.setVisibility(View.VISIBLE);
                }
            }
            dbh.close();;
        } catch (Exception e) {
            Log.v("Excetion_slipe", e.getMessage());
            dbh.open();
            SlidesDownloader(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("here_printg_destroy", "method_are_called");
        // unregisterReceiver();

    }

    private void registerReceiver() {
        unregisterReceiver();
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(DownloadingService.PROGRESS_UPDATE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadingProgressReceiver, intentToReceiveFilter);
        mReceiversRegistered = true;
    }

    private void unregisterReceiver() {
        if (mReceiversRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadingProgressReceiver);
            mReceiversRegistered = false;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void updateRow(final File1 file, View v, String size) {
       // if (digital.equalsIgnoreCase("1"))  tb_dwnloadSlides.setVisibility(View.VISIBLE);
        if (digitalOff.equalsIgnoreCase("0"))
        {
            Log.d("dashboard",digitalOff);

        }else
        {
            tb_dwnloadSlides.setVisibility(View.VISIBLE);
        }
        Log.v("Update_Slide", String.valueOf(tb_dwnloadSlides.getVisibility()));
        bar = (ProgressBar) v.findViewById(R.id.progressBar);
        bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        bar.setProgress(file.progress);
        String fileName = file.toString().substring(file.toString().lastIndexOf('/') + 1, file.toString().length());
        TextView tv_progress = (TextView) v.findViewById(R.id.tv_progress);
        TextView tv_filesize = (TextView) v.findViewById(R.id.tv_filesize);
        TextView tv = (TextView) v.findViewById(R.id.tv_setfilename);


        tv_progress.setText(" " + String.valueOf(file.progress) + "%");
        tv_filesize.setText(size);
        tv.setText(fileName);

        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(DownloadingService.ACTION_CANCEL_DOWNLOAD);
                // i.putExtra(ID, file.getId());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
            }
        });
    }


    protected void onProgressUpdate(int position, int progress, int recsize, int totsize, String size) {
      //  if (digital.equalsIgnoreCase("1"))  tb_dwnloadSlides.setVisibility(View.VISIBLE);
        if (digitalOff.equalsIgnoreCase("0"))
        {
            Log.d("dashboard",digitalOff);
        }else
        {
            tb_dwnloadSlides.setVisibility(View.VISIBLE);
        }
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        mAdapter.getItem(position).progress = progress > 100 ? 100 : progress;
        mAdapter.getItem(position).recsize = recsize;
        mAdapter.getItem(position).totsize = totsize;
        mAdapter.getItem(position).size = size;
        if (position < first || position > last) {
        } else {
            View convertView = listView.getChildAt(position - first);
            updateRow(mAdapter.getItem(position), convertView, size);
        }
    }

    protected void onProgressUpdateOneShot(int[] positions, int[] progresses, int[] recsize, int[] totsize) {
    //    if (digital.equalsIgnoreCase("1"))  tb_dwnloadSlides.setVisibility(View.VISIBLE);
        if (digitalOff.equalsIgnoreCase("0"))
        {
            Log.d("dashboard",digitalOff);
        }else
        {
            tb_dwnloadSlides.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < positions.length; i++) {
            int position = positions[i];
            int progress = progresses[i];
            int recvsize = recsize[i];
            int totasize = totsize[i];
            onProgressUpdate(position, progress, recvsize, totasize, "0B/0B");
        }
    }

    private final BroadcastReceiver mDownloadingProgressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadingService.PROGRESS_UPDATE_ACTION)) {
                final boolean oneShot = intent.getBooleanExtra("oneshot", false);
                if (oneShot) {
                    final int[] progresses = intent.getIntArrayExtra("progress");
                    final int[] positions = intent.getIntArrayExtra("position");
                    final int[] recsize = intent.getIntArrayExtra("progress");
                    final int[] totsize = intent.getIntArrayExtra("position");
                    onProgressUpdateOneShot(positions, progresses, recsize, totsize);
                } else {
                    final int progress = intent.getIntExtra("progress", -1);
                    final int position = intent.getIntExtra("position", -1);
                    final int recsize = intent.getIntExtra("recsize", -1);
                    final int totsize = intent.getIntExtra("totsize", -1);
                    String size = intent.getStringExtra("size");
                    if (position == -1) {
                        return;
                    }
                    onProgressUpdate(position, progress, recsize, totsize, size);
                }
            }
        }
    };

    public static class DownloadingService extends IntentService {
        public static String PROGRESS_UPDATE_ACTION = DownloadingService.class.getName() + ".progress_update";

        private static final String ACTION_CANCEL_DOWNLOAD = DownloadingService.class.getName() + "action_cancel_download";

        private boolean mIsAlreadyRunning;
        private boolean mReceiversRegistered;

        private ExecutorService mExec;
        private CompletionService<NoResultType> mEcs;
        private LocalBroadcastManager mBroadcastManager;
        private List<DownloadTask> mTasks;

        private static final long INTERVAL_BROADCAST = 300;
        private long mLastUpdate = 0;

        public DownloadingService() {
            super("DownloadingService");
            mExec = Executors.newFixedThreadPool( /* only 5 at a time */3);
            mEcs = new ExecutorCompletionService<NoResultType>(mExec);
            mBroadcastManager = LocalBroadcastManager.getInstance(this);
            dbh.open();
            mTasks = new ArrayList<DownloadingService.DownloadTask>();
        }

        @Override
        public void onCreate() {
            super.onCreate();
            registerReceiver();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //unregisterReceiver();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (mIsAlreadyRunning) {
                publishCurrentProgressOneShot(true);
            }
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            if (mIsAlreadyRunning) {
                return;
            }
            mIsAlreadyRunning = true;

            ArrayList<File1> files = intent.getParcelableArrayListExtra("files");
            final Collection<DownloadTask> tasks = mTasks;
            int index = 0;
            for (File1 file : files) {
                int totsize = file.totsize;
                DownloadTask yt1 = new DownloadTask(index++, file, totsize);
                tasks.add(yt1);
            }

            for (DownloadTask t : tasks) {
                mEcs.submit(t);
            }

            // wait for finish
            int n = tasks.size();
            int dwnloadsize = 0;

            for (int i = 0; i < n; ++i) {
                NoResultType r;
                try {
                    r = mEcs.take().get();
                    if (r != null) {
                        dwnloadsize = dwnloadsize + 1;
                        if (dwnloadsize == n) {

                            closeactivity();

                        }
                        Log.d("TASK_SIZE", "" + dwnloadsize + "TOT SIZE " + n);
                        //11
                    }
                } catch (InterruptedException e) {
                    Log.d("TASK_SIZE_interrupt", "" + e);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.d("TASK_SIZE_excep", "" + e);
                    e.printStackTrace();
                }
            }
            // send a last broadcast
            //publishCurrentProgressOneShot(true);
            mExec.shutdown();


           /* new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tb_dwnloadSlides.setVisibility(View.INVISIBLE);
                }
            });
*/
        }

        private void closeactivity() {
            if (digital.equalsIgnoreCase("1")) {
                Intent i = new Intent(this, DetailingCreationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //i.putExtra("detailing", "1");
                startActivity(i);
            }
            else {
                Intent home = new Intent(this, HomeDashBoard.class);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
            }
        }

        private void publishCurrentProgressOneShot(boolean forced) {
            // if (forced || System.currentTimeMillis() - mLastUpdate > INTERVAL_BROADCAST) {
            mLastUpdate = System.currentTimeMillis();
            final List<DownloadTask> tasks = mTasks;
            int[] positions = new int[tasks.size()];
            int[] progresses = new int[tasks.size()];
            int[] recsize = new int[tasks.size()];
            int[] totsize = new int[tasks.size()];
            String[] sizee = new String[tasks.size()];

            for (int i = 0; i < tasks.size(); i++) {
                DownloadTask t = tasks.get(i);
                // Log.d("POSITION", "POSITION"+t.mPosition +"<>>"+t.mProgress);
                positions[i] = t.mPosition;
                progresses[i] = t.mProgress;
                recsize[i] = t.mrecsize;
                totsize[i] = t.mtotsize;
            }


            Log.e("Download_Task", String.valueOf(tasks.size()));

            publishProgress(positions, progresses, recsize, totsize);
            //}
        }

        private void publishCurrentProgressOneShot() {
            publishCurrentProgressOneShot(false);
        }

        private synchronized void publishProgress(int[] positions, int[] progresses, int[] recsize, int[] totsize) {
            Intent i = new Intent();
            i.setAction(PROGRESS_UPDATE_ACTION);
            i.putExtra("position", positions);
            i.putExtra("progress", progresses);
            i.putExtra("recsize", recsize);
            i.putExtra("totsize", totsize);
            i.putExtra("oneshot", true);

            mBroadcastManager.sendBroadcast(i);
        }

        // following methods can also be used but will cause lots of broadcasts
        private void publishCurrentProgress() {
            final Collection<DownloadTask> tasks = mTasks;
            for (DownloadTask t : tasks) {
                // publishProgress(t.mPosition, t.mProgress,t.mrecsize,t.mtotsize);
            }
        }

        private synchronized void publishProgress(int position, int progress, int recsize, int totsize, String size) {
            Intent i = new Intent();
            i.setAction(PROGRESS_UPDATE_ACTION);
            i.putExtra("progress", progress);
            i.putExtra("position", position);
            i.putExtra("recsize", recsize);
            i.putExtra("totsize", totsize);
            i.putExtra("size", size);
            mBroadcastManager.sendBroadcast(i);
        }

        class DownloadTask implements Callable<NoResultType> {
            private int mPosition;
            private int mProgress;
            private int mrecsize;
            private int mtotsize;
            private boolean mCancelled;
            private final File1 mFile;
            private Random mRand = new Random();
            File compressedImageFile;

            public DownloadTask(int position, File1 file, int totsize) {
                mPosition = position;
                mFile = file;
                mtotsize = totsize;
                Log.v("file_in_post", file + "");
            }

            @Override
            public NoResultType call() throws Exception {

                URL url = new URL(mFile.getUrl());
                // Log.d("FILE ANBME ",mFile.getUrl());
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int count = 0;
                File mydir = getApplicationContext().getDir("private_dir", Context.MODE_PRIVATE);
                //File file = new File(mydir.getAbsoluteFile().toString()+"/Products");
                File file = new File(Environment.getExternalStorageDirectory() + "/Products"); /*Internal Storage*/
                File file11 = new File(Environment.getExternalStorageDirectory() + "/Productsss"); /*Internal Storage*/


                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create IMAGE_DIRECTORY_NAME directory");
                    }
                }
                if (!file11.exists()) {
                    if (!file11.mkdirs()) {
                        Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create IMAGE_DIRECTORY_NAME directory");
                    }
                }
                String fileName = mFile.getUrl().substring(mFile.getUrl().lastIndexOf('/') + 1, mFile.getUrl().length());
                File targetLocation = new File(file.getPath() + File.separator + fileName);
                File targetLocationnn = new File(file11.getPath() + File.separator + fileName);
                InputStream input = new BufferedInputStream(url.openStream());

                int lenghtOfFile = conexion.getContentLength();

                Log.e("length_of_file", String.valueOf(lenghtOfFile));

                String fileType = fileName.toString();
                String ZipFile = fileType.substring(fileType.lastIndexOf(".") + 1);
                byte data[] = new byte[1024];
                int total = 0;
                int Status;

                Log.v("intern_sto_target:", targetLocation + "");
                if (ZipFile.equalsIgnoreCase("zip")) {

                    String zipFile = targetLocation.toString();
                    String unzipLocation = file.getPath() + File.separator;
                    OutputStream output = new FileOutputStream(targetLocation);
                    OutputStream output1 = new FileOutputStream(targetLocationnn);
                    try {
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            Status = (int) ((total * 100) / lenghtOfFile);
                            mProgress += Status;//mRand.nextInt(5);
                            mrecsize += total;
                            mtotsize += lenghtOfFile;
                            df = new DecimalFormat("#.##");
                            //File1 ff=files.get(mPosition);
                            dtaSize = lenghtOfFile;
                            if (dtaSize > 1024) {
                                dtaSize = dtaSize / 1024;
                                tszflg = 1;
                            }
                            if (dtaSize > 1024) {
                                dtaSize = dtaSize / 1024;
                                tszflg = 2;
                            }
                            if (dtaSize > 1024) {
                                dtaSize = dtaSize / 1024;
                                tszflg = 3;
                            }

                            RDSize = total;
                            if (RDSize > 1024) {
                                RDSize = RDSize / 1024;
                                rszflg = 1;
                            }
                            if (RDSize > 1024) {
                                RDSize = RDSize / 1024;
                                rszflg = 2;
                            }
                            if (RDSize > 1024) {
                                RDSize = RDSize / 1024;
                                rszflg = 3;
                            }

                            Size = df.format(RDSize) + " " + ((rszflg == 0) ? "B" : (rszflg == 1) ? "KB" : (rszflg == 2) ? "MB" : "GB") + " / " + df.format(dtaSize) + " " + ((tszflg == 0) ? "B" : (tszflg == 1) ? "KB" : (tszflg == 2) ? "MB" : "GB");

                            // System.out.println("STATUS : "+Status +"LENGTH OF FILE >"+lenghtOfFile +"Total >>"+total);
                            publishProgress(mPosition, Status, total, lenghtOfFile, Size);
                            output.write(data, 0, count);
                            output1.write(data, 0, count);

                        }
                        output.flush();
                        output.close();
                        output1.flush();
                        output1.close();

                        Log.v("unzip_location_are", unzipLocation);
                        Decompress d = new Decompress(zipFile, unzipLocation);
                        d.unzip();
                        File file1 = new File(file.getPath() + File.separator + fileName.toString());
                        Log.v("file_one_print", file1 + "");
                        //boolean deleted = file1.delete();

                        String HTMLPath = targetLocation.toString().replaceAll(".zip", "");
                        Log.v("Presentation_dragss", "adapter_called" + HTMLPath);
                        File f = new File(HTMLPath);
                        File[] files = f.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return filename.contains(".png") || filename.contains(".jpg");
                            }
                        });
                        String urll = "";
                        Log.v("files_length_here", files.length + " are here");
                        if (files.length > 0) urll = files[0].getAbsolutePath();
                        Uri imageUri = Uri.fromFile(new File(urll));

                        Log.v("Presentation_drag_ht", "adapter_called" + imageUri + "url" + urll);
                        //imageUri= Uri.parse("/storage/emulated/0/Products/IndianImmunologicals/preview.png");
                        Log.v("Presentation_drag_ht", "adapter_called" + imageUri);
                        //Log.v("Presentation_drag_ht","adapter_called"+imageUri);


                        String bit = BitMapToString(getResizedBitmap(String.valueOf(imageUri).substring(7), 150, 150));
                        File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(new File(urll));
                        // dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit);
                        dbh.update_product_Content_Url(targetLocation.toString(), fileName, bit, compressedImageFile.toString());
                        Log.v("targetLocation_1", targetLocation.toString() + "compress");
                    } catch (NullPointerException e) {
                        Log.d("TASK_SIZE_dwnd_exe", "" + e + url);
                        e.printStackTrace();
                    } catch (Exception e) {
                        Log.d("TASK_SIZE_dwnd_exec", "" + e);
                        e.printStackTrace();
                    }


                } else {
                    //System.out.println("intetnal storege targetLocation: "+targetLocation.toString());
                    OutputStream output = new FileOutputStream(targetLocation);
                    try {
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            Status = (int) ((total * 100) / lenghtOfFile);
                            // System.out.println("lenghtOfFile>>>/"+lenghtOfFile +"POSITION "+mPosition);
                            mProgress += Status;//mRand.nextInt(5);
                            mrecsize += total;
                            mtotsize += lenghtOfFile;
                            df = new DecimalFormat("#.##");
                            dtaSize = lenghtOfFile;
                            if (dtaSize > 1024) {
                                dtaSize = dtaSize / 1024;
                                tszflg = 1;
                            }
                            if (dtaSize > 1024) {
                                dtaSize = dtaSize / 1024;
                                tszflg = 2;
                            }
                            if (dtaSize > 1024) {
                                dtaSize = dtaSize / 1024;
                                tszflg = 3;
                            }

                            RDSize = total;
                            if (RDSize > 1024) {
                                RDSize = RDSize / 1024;
                                rszflg = 1;
                            }
                            if (RDSize > 1024) {
                                RDSize = RDSize / 1024;
                                rszflg = 2;
                            }
                            if (RDSize > 1024) {
                                RDSize = RDSize / 1024;
                                rszflg = 3;
                            }

                            Size = df.format(RDSize) + " " + ((rszflg == 0) ? "B" : (rszflg == 1) ? "KB" : (rszflg == 2) ? "MB" : "GB") + " / " + df.format(dtaSize) + " " + ((tszflg == 0) ? "B" : (tszflg == 1) ? "KB" : (tszflg == 2) ? "MB" : "GB");
                            publishProgress(mPosition, Status, total, lenghtOfFile, Size);
                            output.write(data, 0, count);
                        }

                        output.flush();
                        output.close();
                        System.out.println("lenghtOfFile>>>/" + fileName);
                        String bit = "";
                        if (ZipFile.equalsIgnoreCase("pdf")) {
                            /*Drawable myDrawable = getApplicationContext().getResources().getDrawable(R.mipmap.pdf_logo);
                            Bitmap bm = ((BitmapDrawable) myDrawable).getBitmap();
                            bit = BitMapToString(scaleDown(bm, 200, true));*/
                            Log.v("printing_target_", targetLocation.toString() + " zip " + ZipFile);
                            Bitmap bm = getResizedBitmapForPdf(getBitmap(new File(targetLocation.toString())), 150, 150);
                            bit = BitMapToString(scaleDown(bm, 200, true));
                        } else if (ZipFile.equalsIgnoreCase("mp4")) {
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(targetLocation.toString());
                            Bitmap bm = mediaMetadataRetriever.getFrameAtTime(5000000);
                            bit = BitMapToString(bm);
                            /*dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit,"empty");*/
                        } else if (!targetLocation.toString().contains("avi")) {
                            bit = BitMapToString(getResizedBitmap(targetLocation.toString(), 150, 150));
                        }

                        if (targetLocation.toString().contains("png") || targetLocation.toString().contains("jpg")) {
                            File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(targetLocation);
                            dbh.update_product_Content_Url(compressedImageFile.toString(), fileName, bit, compressedImageFile.toString());
                            Log.v("compressed_Filesss", compressedImageFile.toString());
                        } else
                            dbh.update_product_Content_Url(targetLocation.toString(), fileName, bit, "empty");

                        Log.v("targetLocation_2", targetLocation.toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dbh.close();
                    }
                }


//https://www.google.com/search?q=all+video+support+player+for+android+github&rlz=1C1CHBF_enIN853IN853&oq=all+video+support+player+for+android+github&aqs=chrome..69i57.33038j0j7&sourceid=chrome&ie=UTF-8
                //publishCurrentProgressOneShot();

                return new NoResultType();
            }

            public int getProgress() {
                return mProgress;
            }

            public int getMrecsize() {
                return mrecsize;
            }

            public int getMtotsize() {
                return mtotsize;
            }

            public int getPosition() {
                return mPosition;
            }

            public void cancel() {
                mCancelled = true;
            }
        }

        public Bitmap getResizedBitmap(String path, int newWidth, int newHeight) {
            int width = 0;
            int height = 0;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(path, bmOptions);
            try {
                width = bm.getWidth();
                height = bm.getHeight();
            } catch (Exception e) {
                Log.v("TASK_SIZE_are_ptint", e.getMessage());
            }

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            if (bm != null && !bm.isRecycled())
                bm.recycle();

            return resizedBitmap;
        }

        private void registerReceiver() {
            unregisterReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(DownloadingService.ACTION_CANCEL_DOWNLOAD);
            LocalBroadcastManager.getInstance(this).registerReceiver(mCommunicationReceiver, filter);
            mReceiversRegistered = true;
        }

        private void unregisterReceiver() {
            if (mReceiversRegistered) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mCommunicationReceiver);
                mReceiversRegistered = false;
            }
        }

        private final BroadcastReceiver mCommunicationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DownloadingService.ACTION_CANCEL_DOWNLOAD)) {
                    final long id = intent.getLongExtra(ID, -1);
                    if (id != -1) {
                        for (DownloadTask task : mTasks) {

                        }
                    }
                }
            }
        };

        class NoResultType {
        }
    }

    public static Bitmap getResizedBitmapForPdf(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static ParcelFileDescriptor openFile1(File file) {
        ParcelFileDescriptor descriptor;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return descriptor;
    }

    public static Bitmap getBitmap(File file) {
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(mContext);
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(openFile1(file));
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static class File1 implements Parcelable {
        public static Object separator;
        private final String url;

        private int progress;
        private int recsize;
        private int totsize;
        private String size;

        public File1(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return url;// + "   " + progress + " %";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
            dest.writeInt(this.progress);
            dest.writeInt(this.recsize);
            dest.writeInt(this.totsize);
        }

        private File1(Parcel in) {
            this.url = in.readString();
            this.progress = in.readInt();
            this.recsize = in.readInt();
            this.totsize = in.readInt();
        }

        public static final Parcelable.Creator<File1> CREATOR = new Parcelable.Creator<File1>() {
            public File1 createFromParcel(Parcel source) {
                return new File1(source);
            }

            public File1[] newArray(int size) {
                return new File1[size];
            }
        };
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }


    public void progressBarAnimation(final int max) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (pBarCount < max) {
                    ++pBarCount;
                    try {
                        Thread.sleep(30);
                        pBar.setProgress(pBarCount);
                        pbar_percentage.setText(max);
                    } catch (Exception e) {

                    }
                }
            }
        }).start();
    }

    public void statusNavigation(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        iv_navdrawer_menu.setEnabled(false);

//        mainDashbrd.setVisibility(View.GONE);
//        menuDashbrd.setVisibility(View.GONE);
//        layoutBottomSheet.setVisibility(View.GONE);
//        l1_app_config.setVisibility(View.GONE);
        //drawer.setVisibility(View.INVISIBLE);
        /* toggle.setDrawerIndicatorEnabled(enabled);*/
    }

    private void hideItem() {
        /*Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_approvals).setVisible(false);*/
        arrayNav.remove(6);

    }

    private void hideNearMe() {
        /*Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_nearme).setVisible(false);*/
        arrayNav.remove(8);

    }



/*
    private static Bitmap getThumb(String s)
    {
        Bitmap bmp = null;
        if (bmp != null) {
            bmp.recycle();
            bmp = null;
        }
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(s, bmOptions);
            float scaleWidth = ((float) 150) / bitmap.getWidth();
            float scaleHeight = ((float) 150) / bitmap.getHeight();
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);
            bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();
            paint.setTextSize(24);
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            canvas.drawRoundRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), 15, 15, paint);
            paint.setColor(Color.BLACK);
            //paint.setTextAlign(Paint.Align.CENTER);
            //  canvas.drawText(s, 75, 75, paint);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            if (bitmap != bmp) {
                bitmap.recycle();
                bitmap=null;
            }
        }catch (OutOfMemoryError e){
            // Log.v("out_of_memory_excep",e.getMessage());

        }

        return bmp;
    }
*/

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap getResizedBitmap(String path, int newWidth, int newHeight) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(path, bmOptions);
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        bm.recycle();
        return resizedBitmap;
    }

    public void callSetUps() {
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("SF", SF_Code);
        } catch (Exception e) {

        }
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> setUpDetail = apiService.getSetUp(paramObject.toString());

        setUpDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    String jsonData = null;

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("printing_setup_response", is.toString());
                        JSONArray jsonArray = new JSONArray(is.toString());
                        JSONObject jsonn = jsonArray.getJSONObject(0);

                        if (jsonn.has("LeaveStatus")) {
                            mCommonSharedPreference.setValueToPreference("LeaveStatus", jsonn.getString("LeaveStatus"));
                        } else
                            mCommonSharedPreference.setValueToPreference("LeaveStatus", "1");


                        Log.v("specFilter_json", jsonn.getString("SpecFilter"));
                        mCommonSharedPreference.setValueToPreference("specFilter", jsonn.getString("SpecFilter"));
                        Log.v("GpsFilter", jsonn.getString("GeoNeed"));
                        mCommonSharedPreference.setValueToPreference("GpsFilter", jsonn.getString("GeoNeed"));
                        if (jsonn.getString("DrRxNd").equalsIgnoreCase("1")) {
                            mCommonSharedPreference.setValueToPreference("feed_pob", "D");
                            mCommonSharedPreference.setValueToPreference("DrRxNd", jsonn.getString("DrRxNd"));
                        }
                        else {
                            mCommonSharedPreference.setValueToPreference("feed_pob", " ");
                            mCommonSharedPreference.setValueToPreference("DrRxNd", "");
                        }
                        if (jsonn.getString("ChmRxNd").equalsIgnoreCase("0")) {
                            String chm = mCommonSharedPreference.getValueFromPreference("feed_pob");
                            mCommonSharedPreference.setValueToPreference("feed_pob", chm + "C");
                        }
                        if (jsonn.has("Stk_Pob_Need") && jsonn.getString("Stk_Pob_Need").equalsIgnoreCase("0")) {
                            String chm = mCommonSharedPreference.getValueFromPreference("feed_pob");
                            mCommonSharedPreference.setValueToPreference("feed_pob", chm + "S");
                        }
                        if (jsonn.has("Ul_Pob_Need") && jsonn.getString("Ul_Pob_Need").equalsIgnoreCase("0")) {
                            String chm = mCommonSharedPreference.getValueFromPreference("feed_pob");
                            mCommonSharedPreference.setValueToPreference("feed_pob", chm + "U");
                        }
                       /* else
                            mCommonSharedPreference.setValueToPreference("feed_pob"," ");*/
                        if (jsonn.has("DrRxQCap"))
                            mCommonSharedPreference.setValueToPreference("dpob", jsonn.getString("DrRxQCap"));
                        else
                            mCommonSharedPreference.setValueToPreference("dpob", "");
                        if (jsonn.has("ChmQCap"))
                            mCommonSharedPreference.setValueToPreference("cpob", jsonn.getString("ChmQCap"));
                        else
                            mCommonSharedPreference.setValueToPreference("cpob", "");
                        if (jsonn.has("StkQCap"))
                            mCommonSharedPreference.setValueToPreference("spob", jsonn.getString("StkQCap"));
                        else
                            mCommonSharedPreference.setValueToPreference("spob", "");

                        mCommonSharedPreference.setValueToPreference("chmcap", jsonn.getString("ChmCap"));
                        mCommonSharedPreference.setValueToPreference("drcap", jsonn.getString("DrCap"));
                        mCommonSharedPreference.setValueToPreference("stkcap", jsonn.getString("StkCap"));
                        mCommonSharedPreference.setValueToPreference("ucap", jsonn.getString("NLCap"));

                        if (jsonn.has("ChmNeed"))
                            mCommonSharedPreference.setValueToPreference("chem_need", jsonn.getString("ChmNeed"));
                        else
                            mCommonSharedPreference.setValueToPreference("chem_need", "");

                        if (jsonn.has("StkNeed"))
                            mCommonSharedPreference.setValueToPreference("stk_need", jsonn.getString("StkNeed"));
                        else
                            mCommonSharedPreference.setValueToPreference("stk_need", "");

                        if (jsonn.has("UNLNeed"))
                            mCommonSharedPreference.setValueToPreference("unl_need", jsonn.getString("UNLNeed"));
                        else
                            mCommonSharedPreference.setValueToPreference("unl_need", "");



                        if (jsonn.has("DrRxQMd"))
                            mCommonSharedPreference.setValueToPreference("DrRxQMd", jsonn.getString("DrRxQMd"));
                        else
                            mCommonSharedPreference.setValueToPreference("DrRxQMd", "");

                        if (jsonn.has("DrSmpQMd"))
                            mCommonSharedPreference.setValueToPreference("DrSmpQMd", jsonn.getString("DrRxQMd"));
                        else
                            mCommonSharedPreference.setValueToPreference("DrSmpQMd", "");

//                        if (jsonn.has("DrRxNd"))
//                            mCommonSharedPreference.setValueToPreference("DrRxNd", jsonn.getString("DrRxNd"));
//                        else
//                            mCommonSharedPreference.setValueToPreference("DrRxNd", "");
                        //Rcpa Needed

                        if (jsonn.has("RcpaNd"))
                            mCommonSharedPreference.setValueToPreference("RcpaNd", jsonn.getString("RcpaNd"));
                        else
                            mCommonSharedPreference.setValueToPreference("RcpaNd", "");

                        if (jsonn.has("DrInpMd"))
                            mCommonSharedPreference.setValueToPreference("DrInpMd", jsonn.getString("DrInpMd"));
                        else
                            mCommonSharedPreference.setValueToPreference("DrInpMd", "");


                        if (jsonn.has("cipcap"))
                            mCommonSharedPreference.setValueToPreference("cipcap", jsonn.getString("CIP_Caption"));
                        else
                            mCommonSharedPreference.setValueToPreference("cipcap", "");

                        if (jsonn.has("cip_need"))
                            mCommonSharedPreference.setValueToPreference("cip_need", jsonn.getString("cip_need"));
                        else
                            mCommonSharedPreference.setValueToPreference("cip_need", "");

                        if (jsonn.has("CIP_ENeed"))
                            mCommonSharedPreference.setValueToPreference("cip_det", jsonn.getString("CIP_ENeed"));
                        else
                            mCommonSharedPreference.setValueToPreference("cip_det", "");


                        GpsNeed = jsonn.getString("GeoNeed");

                        //availableAdudit Needed
                        if(jsonn.has("NActivityNeed")) {
                            mCommonSharedPreference.setValueToPreference("ActivityNeeded", jsonn.getString("NActivityNeed"));
                        }else {
                            mCommonSharedPreference.setValueToPreference("ActivityNeeded", "");
                        }
                        //availableAdudit Needed
                        if(jsonn.has("AvailableAduitNeeded")) {
                            mCommonSharedPreference.setValueToPreference("AvailableAduitNeeded", jsonn.getString("AvailableAduitNeeded"));
                        }else {
                            mCommonSharedPreference.setValueToPreference("AvailableAduitNeeded", "");
                        }

                        Log.v("specFilter_json", mCommonSharedPreference.getValueFromPreference("specFilter"));
                        if (GpsNeed.equalsIgnoreCase("0") &&
                                mCommonSharedPreference.getValueFromPreference("track_loc").equalsIgnoreCase("1")) {
                            LocationTrack tt = new LocationTrack(HomeDashBoard.this, SF_Code);
                            /*callLocation();
                             */

                        }

                        //DashboardSetup
                        if(jsonn.has("Dashboard1")){
                            if(jsonn.getString("Dashboard1").contains("MTD")) {
                                mCommonSharedPreference.setValueToPreference("Dashboard1", "Newscreen1");
                            }else{
                                mCommonSharedPreference.setValueToPreference("Dashboard1", "Oldscreen1");

                            }

                        }else{
                            mCommonSharedPreference.setValueToPreference("Dashboard1", "");
                        }

                        if(jsonn.has("Dashboard2")){
                            if(jsonn.getString("Dashboard2").contains("VSD")) {
                                mCommonSharedPreference.setValueToPreference("Dashboard2", "Newscreen2");
                            }else{
                                mCommonSharedPreference.setValueToPreference("Dashboard2", "Oldscreen2");

                            }

                        }else{
                            mCommonSharedPreference.setValueToPreference("Dashboard2", "");
                        }

                        if(jsonn.has("Detailing_chem")){
                            mCommonSharedPreference.setValueToPreference("Detailing_chem",jsonn.getString("Detailing_chem"));
                        }
                        else
                            mCommonSharedPreference.setValueToPreference("Detailing_chem","");
                        if(jsonn.has("Detailing_stk")){
                                mCommonSharedPreference.setValueToPreference("Detailing_stk",jsonn.getString("Detailing_stk"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("Detailing_stk","");
                            if(jsonn.has("Detailing_undr")){
                                mCommonSharedPreference.setValueToPreference("Detailing_undr",jsonn.getString("Detailing_undr"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("Detailing_undr","");


                        if(jsonn.has("Tp_Based")){
                            mCommonSharedPreference.setValueToPreference("Tp_Based",jsonn.getString("Tp_Based"));
                        }
                        else
                            mCommonSharedPreference.setValueToPreference("Tp_Based","");



                        getTodayCalls();
                    } catch (Exception e) {
                    }
                } else {
                    getTodayCalls();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getTodayCalls();
            }
        });
    }

    public void deleteFeedInstruction() {
        mCommonSharedPreference.clearFeedShare();
        dbh.open();
        dbh.deleteFeed();
        dbh.delete_groupName("customised");
        dbh.close();
        mCommonSharedPreference.setValueToPreference("jsonarray", "");
        mCommonSharedPreference.setValueToPreference("slide_feed", "[]");
    }

    public void callLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void CheckLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeDashBoard.this);
            alertDialog.setTitle(getResources().getString(R.string.enable_location));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getResources().getString(R.string.alert_location));
            alertDialog.setPositiveButton(getResources().getString(R.string.location_setting), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                }
            });
           /* alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });*/
            AlertDialog alert = alertDialog.create();
            alert.show();


        }
        check = new CheckPermission(HomeDashBoard.this);
        check.startLocationButtonClick();
        Log.v("check_location_val", check.locationenable + "");
        if (check.locationenable) {
            callDCR();
        }


    }

    public static void addLogText(String log) {
        //log_network.setText(String.format("%s\n%s", log_network.getText().toString(), log));
        Log.v("printing_network_sta", log);
        if (!TextUtils.isEmpty(log)) {
            if (!log.equalsIgnoreCase("NOT_CONNECT")) {
                Log.v("wifi_are_connec", "here");
                if (mUpdateUi != null)
                    mUpdateUi.updatingui();
                /* Activity.getInstance().sendingTp();*/
            } else {
                Log.v("wifi_are_connec_not", "here");
            }
        }
    }

    public void sendingTp() {

        try {

            dbh.open();
            Cursor cur22 = dbh.select_MDP();
            Cursor cur1 = dbh.select_json_list();

            Log.v("sending_tp_check", cur22.getCount() + "");
            if (cur22.getCount() > 0) {

                cur22.moveToFirst();
                Log.v("cur_value_send", cur22.getString(1) + " sec " + cur22.getString(0));
                sendMDP(cur22.getString(1), cur22.getString(0));


            } else {
                if (cur1.getCount() > 0) {
                    cur1.moveToFirst();
                    submitoffline = true;
                    if (cur1.getString(2).indexOf("_") != -1) {
                        Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                        finalSubmission(cur1.getString(1), cur1.getInt(0));
                    }


                } else {
                    submitoffline = false;
                }
            }


            mCommonSharedPreference.setValueToPreference("check_online", "true");
            /*    if (cur1.getCount() > 0) {
                    while (cur1.moveToNext()) {
                        submitoffline=true;
                        if (cur1.getString(2).indexOf("_") != -1) {
                            Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                            finalSubmission(cur1.getString(1), cur1.getInt(0));
                        }
                    }

                }
                else{
                    submitoffline=false;
                }*/

            Cursor cur2 = dbh.select_tracking();
            if (cur2.getCount() > 0) {
                while (cur2.moveToNext()) {
                    svTrack(cur2.getString(1), cur2.getString(2), cur2.getString(3), cur2.getString(4));
                }
                dbh.delete_track();
            }

        } catch (Exception e) {
            Log.v("printing_excep", e.getMessage());
        }
    }

    public void finalSubmissionForMissed(final String json) {
        Api_Interface api_interface = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Log.v("printing_miss_1", "here" + json);
        Call<ResponseBody> query = api_interface.savemisEntry(json);
        Log.v("printing_miss_11", "here");
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("printing_response", response.body() + "");
                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("final_wrking", is.toString() + " final_Val " + json);
                    JSONObject js = new JSONObject(is.toString());
                    if (js.getString("success").equals("true")) {
                        mis_dialog.dismiss();
                        Toast.makeText(HomeDashBoard.this, resources.getString(R.string.submitsuccess), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void finalSubmission(final String val, final int id) {
        Api_Interface api_interface = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> query = api_interface.finalSubmit(val);
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("final_submit_working", is.toString() + " final_Val " + val);
                    JSONObject js = new JSONObject(is.toString());
                    if (js.getString("success").equals("true")) {
                        Log.v("final_submit_working", "success");
                        dbh.open();
                        dbh.delete_json(id);
                        Cursor cur1 = dbh.select_json_list();
                        if (cur1.getCount() > 0) {
                            cur1.moveToFirst();
                            if (cur1.getString(2).indexOf("_") != -1) {
                                Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                                finalSubmission(cur1.getString(1), cur1.getInt(0));
                            }

                        }

                        TodayCalls();

                        // Toast.makeText(FeedbackActivity.this, "Data Submitted Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(FeedbackActivity.this, js.getString("Msg"), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void bindupdateui(UpdateUi updateUi) {
        mUpdateUi = updateUi;
    }

    public void getTodayCalls() {
        if (!CommonUtilsMethods.isOnline(HomeDashBoard.this)) {
            TodayCallList = new ArrayList<Custom_Todaycalls_contents>();
            TodayCallList.clear();
            setMDPValue();
            dbh.open();
            Cursor cur1 = dbh.select_json_list();
            if (cur1.getCount() > 0) {
                while (cur1.moveToNext()) {
                    Log.v("Cursor_today_Cal", cur1.getString(1));
                    _custom_todaycalls_contents = new Custom_Todaycalls_contents(cur1.getString(4), cur1.getString(2), cur1.getString(1), cur1.getString(3), String.valueOf(cur1.getInt(0)), 0, cur1.getString(5), cur1.getString(6), true);

                    if (!cur1.getString(2).equals("DocName")) {
                        TodayCallList.add(_custom_todaycalls_contents);
                    }
                }

                Log.v("todaycalllist__print", String.valueOf(TodayCallList.size()));
                todayCalls_recyclerviewAdapter = new TodayCalls_recyclerviewAdapter(getApplicationContext(), TodayCallList);
                tv_todaycall_count.setText(resources.getString(R.string.total)+" "+ TodayCallList.size() + " "+resources.getString(R.string.calls));
                rv_todayCalls.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv_todayCalls.setLayoutManager(mLayoutManager);
                rv_todayCalls.setItemAnimator(new DefaultItemAnimator());
                rv_todayCalls.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                rv_todayCalls.setAdapter(todayCalls_recyclerviewAdapter);
            }
        } else
            TodayCalls();
    }

    public void alertDialoggg(ArrayList<String> list) {
        LayoutInflater lInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup_view = lInflater.inflate(R.layout.popup_notification, null);
        final PopupWindow popup = new PopupWindow(popup_view, 500, 400, true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable());
        popup.showAsDropDown(iv_noti, -225, 0);
        ListView listView = (ListView) popup_view.findViewById(R.id.list); //response:
        Log.v("total_list_size", list.size() + " msgg " + list.get(0));
        ArrayAdapter adap = new ArrayAdapter<String>(HomeDashBoard.this, R.layout.listview_items, list);
        listView.setAdapter(adap);
        adap.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popup.dismiss();
                alertMsgNotification(parent.getItemAtPosition(position).toString());
            }
        });


        /*[
    {
        "id": 64,
        "msg": "testing",
        "Flag": "0"
    }
]*/
    }

    public void getNewNotification() {
        Intent i = new Intent(HomeDashBoard.this, NotificationActivity.class);
        startActivity(i);
    }

    public void getNotification() {
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SF", SF_Code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> noti = apiService.getnotification(jsonObject.toString());
        noti.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    Log.v("notification_printt", is.toString());
                    ArrayList<String> msg = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(is.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jj = jsonArray.getJSONObject(i);
                        msg.add(jj.getString("msg"));
                    }
                    alertDialoggg(msg);
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void alertMsgNotification(String msg) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        // builder = new AlertDialog.Builder(HomeDashBoard.this, R.style.MyDialogTheme);
        builder.setMessage("Message").setTitle("Notification");

        //Setting message manually and performing action on button click
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Notification");
        alert.show();

    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new CallFragment(), "");
       // adapter.addFrag(new TrainingFragment(), "");
        adapter.addFrag(new NewTrainingFragment(), "");
        adapter.addFrag(new NewCallFragment(), "");

        viewPager.setAdapter(adapter);
        progress.dismiss();
    }

    public void callDCR() {
        String wrkNAm = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, null);
        String wrkcluNAm = sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, null);
        if (TextUtils.isEmpty(wrkNAm) || wrkNAm.equalsIgnoreCase("null")) {
            Toast.makeText(HomeDashBoard.this, resources.getString(R.string.dplan_need), Toast.LENGTH_SHORT).show();
            if (!tpflag.isEmpty() || !tpflag.equals("")) {
                if (tpflag.equals("1")) {
                    if (tb_dwnloadSlides.getVisibility() != View.VISIBLE) {
                        Worktype();
                    }

                }
            } else {
                if (tb_dwnloadSlides.getVisibility() != View.VISIBLE) {
                    Worktype();
                }
            }
        } else {
            //if (sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, "").equalsIgnoreCase("Field Work")) {
                CommonUtilsMethods.CommonIntentwithNEwTask(DCRCallSelectionActivity.class);
            //}
        }

//            if(wttpeFlag.equals("0"))
//            {
//                Toast.makeText(this,"Submit yesterday plan",Toast.LENGTH_LONG).show();
//            }else
//            {
//                Worktype();
//            }


    }

    public static void updateCallUI(UpdateUi ui) {
        callFragmentUi = ui;
    }

    public boolean fullCheckPermission() {
        check = new CheckPermission(HomeDashBoard.this);
        check.startLocationButtonClick();
        Log.v("check_location_val", check.locationenable + "");
        return check.locationenable;
    }

    public void missedDate() {
        mis_dialog = new Dialog(HomeDashBoard.this, R.style.AlertDialogCustom);
        mis_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mis_dialog.setContentView(R.layout.popup_missed_date);
        mis_dialog.setCanceledOnTouchOutside(false);
        mis_dialog.show();
        final ListView missed_list = (ListView) mis_dialog.findViewById(R.id.missed_list);
        ImageView close_img = (ImageView) mis_dialog.findViewById(R.id.close_img);

        arr = new ArrayList<>();

        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mis_dialog.dismiss();
            }
        });

        final AdapterMissedDate adp = new AdapterMissedDate(this, arr);
        missed_list.setAdapter(adp);
        adp.notifyDataSetChanged();

        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SF", SF_Code);
            Log.v("json_missed", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> noti = apiService.getMissedDate(jsonObject.toString());
        noti.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    Log.v("missed_printt", is.toString());
                    JSONArray jj = new JSONArray(is.toString());
                    for (int i = 0; i < jj.length(); i++) {
                        JSONObject js = jj.getJSONObject(i);
                        arr.add(new MissedDate(js.getString("DMon") + " " + js.getString("DYr"), js.getString("DDate"), js.getString("DDay"), js.getString("id")));
                    }
                    Log.v("missed>>>", arr.toString());
                    AdapterMissedDate adp = new AdapterMissedDate(HomeDashBoard.this, arr);
                    missed_list.setAdapter(adp);
                    adp.notifyDataSetChanged();

                   /* ArrayList<String> msg=new ArrayList<>();

                    JSONArray jsonArray=new JSONArray(is.toString());
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jj=jsonArray.getJSONObject(i);
                        msg.add(jj.getString("msg"));
                    }
                    alertDialoggg(msg);*/
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void svTrack(String lat, String lng, String acc, String date) {

        JSONObject json = new JSONObject();
        JSONArray jsonA = new JSONArray();
        try {
            json.put("SF", SF_Code);
            json.put("latitude", lat);
            json.put("longitude", lng);
            json.put("theAccuracy", acc);
            json.put("date", date);
            Log.v("print_json_bb", json.toString() + " db_path " + db_connPath);
            jsonA.put(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> taggedDr = apiService.saveTrack(jsonA.toString());

        taggedDr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.v("printing_res_track", response.body().byteStream() + "");
                    JSONObject jsonObject = null;
                    String jsonData = null;

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                    } catch (Exception e) {

                    }
                    Log.v("printing_trackeeed_22", is.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    class BackgroundProcess extends AsyncTask<Void, Void, Void> {
        File1 file;
        View v;

        public BackgroundProcess(File1 file, View view) {
            this.file = file;
            v = view;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // updateRow(file, v);
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.v("printing_req", "are_Called");

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                            Log.v("request_permi", "are_aquired");
                                           /* callLocation();
                                            LocationTrack tt = new LocationTrack(HomeDashBoard.this, SF_Code);*/

                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.v("printing_req11", "are_Called");
                    callLocation();
                    LocationTrack tt = new LocationTrack(HomeDashBoard.this, SF_Code);
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeDashBoard.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void refreshPendingFunction() {


        viewpage = (ViewPager) findViewById(R.id.viewpage);
        addTabs(viewpage);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_navdrawer_menu = (ImageView) findViewById(R.id.iv_navdrawer);
        rl_calls = (LinearLayout) findViewById(R.id.rl_calls);
        rl_Expenses = (LinearLayout) findViewById(R.id.rl_Expenses);
        rl_act = (LinearLayout) findViewById(R.id.rl_act);
        rl_act.setVisibility(View.INVISIBLE);
        tb_dwnloadSlides = (TableLayout) findViewById(R.id.tableLayout1);
        rl_presentation = (LinearLayout) findViewById(R.id.rl_presentation);
        rl_reports = (LinearLayout) findViewById(R.id.rl_reports);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        myplancap = (TextView)findViewById(R.id.tv_todayplan);
        tv_mWorktype = (TextView) findViewById(R.id.tv_mydaypln_worktype);
        tv_headquater = (TextView) findViewById(R.id.tv_mydaypln_HQ);
        tv_userName = (TextView) findViewById(R.id.tv_username);
        tv_worktype = (TextView) findViewById(R.id.tv_worktype);
        tv_cluster = (TextView) findViewById(R.id.tv_cluster);
        pbar_percentage = (TextView) findViewById(R.id.pbar_percentage);
        rv_todayCalls = (RecyclerView) findViewById(R.id.rv_todaycalls);
        tv_todaycall_count = (TextView) findViewById(R.id.tv_todaycall_count);
        Close = (ImageView) findViewById(R.id.iv_close);
        listView = (ListView) findViewById(R.id.listView_slides);
        tb_dwnloadSlides.setVisibility(View.GONE);
        tv_mworktype_cluster = (TextView) findViewById(R.id.tv_mydaypln_cluster);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_reload = (TextView) findViewById(R.id.tv_reload);
        tv_logout = (TextView) findViewById(R.id.tv_logout);

        iv_setting = (ImageView) findViewById(R.id.iv_settings);
        iv_reload = (ImageView) findViewById(R.id.iv_reload);
        iv_noti = (ImageView) findViewById(R.id.iv_noti);
        iv_logout = (ImageView) findViewById(R.id.iv_logout);
        rl_up = (RelativeLayout) findViewById(R.id.rl_up);
        pBar = (ProgressBar) findViewById(R.id.pBar);
        nav_list = findViewById(R.id.nav_list);
        tv_calls=findViewById(R.id.tv_calls);
        tv_presentation=findViewById(R.id.tv_presentation);
        tv_reports=findViewById(R.id.tv_reports);

//        tv_calls.setText(resources.getString(R.string.calls));
//        tv_presentation.setText(resources.getString(R.string.presentation));
//        tv_reports.setText(resources.getString(R.string.report));

        addNavItem();
        //hideNearMe();
      //  hideTP();


//        iv_reload.setVisibility(View.GONE);
//        if (digital.equalsIgnoreCase("1")) {
//            iv_reload.setVisibility(View.VISIBLE);
//        }
        layoutBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setHideable(false);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        submitoffline = false;
        sharedpreferences = getSharedPreferences(getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        setSharedPreference();




        String jsondata = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_LOGIN_RESPONSE);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        db_slidedwnloadPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        SF_Type = mCommonSharedPreference.getValueFromPreference("sf_type");
        db_slidedwnloadPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        mCommonSharedPreference.setValueToPreference("cat", "D");
        mCommonSharedPreference.setValueToPreference("switch_fragment", "false");
        mCommonSharedPreference.setValueToPreference("map_return_count", "");
        mCommonSharedPreference.setValueToPreference("hos_code", "");
        mCommonSharedPreference.setValueToPreference("hos_name", "");
        mCommonSharedPreference.setValueToPreference("cust_act", "");
        mCommonSharedPreference.setValueToPreference("dr_profile", "false");
        mCommonSharedPreference.setValueToPreference("present", "no");

        mCommonSharedPreference.setValueToPreference("dashboard", "true");

        if (SF_Type.equalsIgnoreCase("1")) {
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_SF_HQ, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME));
            mCommonSharedPreference.setValueToPreference("sub_sf_code", SF_Code);

        } else {
            //UpdateWrktype();
        }
        if (mCommonSharedPreference.getValueFromPreference("ActivityNeeded").equalsIgnoreCase("1"))
            rl_act.setVisibility(View.VISIBLE);
        else
            rl_act.setVisibility(View.INVISIBLE);

        if (mCommonSharedPreference.getValueFromPreference("GpsFilter").equalsIgnoreCase("0")) {
            permissionsToRequest = findUnAskedPermissions(permissions);
            //get the permissions we have asked for before but are not granted..
            //we will store this in a global list to access later.

            if (permissionsToRequest.size() > 0) {
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (permissionsToRequest.size() > 0) {
                    requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                }
            } else {
                //startLocationButtonClick();
                if (mCommonSharedPreference.getValueFromPreference("track_loc").equalsIgnoreCase("0")) {
                    LocationTrack tt = new LocationTrack(HomeDashBoard.this, SF_Code);
                }
            }
        } else
           // hideNearMe();
       /* Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_quiz).setVisible(false);*/

        mCommonSharedPreference.setValueToPreference("missed", "false");
        mCommonSharedPreference.setValueToPreference("missed_array", "[]");
        mCommonSharedPreference.setValueToPreference("miss_select", "0");
        mCommonSharedPreference.setValueToPreference("display_brand", getResources().getString(R.string.brandmatrix));

        try {
            jsonObject = new JSONObject(jsondata);
            SF_Code = jsonObject.getString("SF_Code");
            tv_userName.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME));
            //tv_worktype.setText("Work Type : "+jsonObject.getString("SF_Password"));

            rl_calls.setOnClickListener(this);
            rl_presentation.setOnClickListener(this);
            iv_navdrawer_menu.setOnTouchListener(this);
            iv_setting.setOnTouchListener(this);
            iv_logout.setOnTouchListener(this);
            iv_reload.setOnTouchListener(this);
            iv_noti.setOnTouchListener(this);
            Close.setOnTouchListener(this);
            tv_setting.setOnTouchListener(this);
            tv_reload.setOnTouchListener(this);
            tv_logout.setOnTouchListener(this);
            rl_reports.setOnClickListener(this);
            displayWrk = false;

            currentDate = CommonUtilsMethods.getCurrentInstance();

            Log.v("current_datee_home ", currentDate + " preference_date " + mCommonSharedPreference.getValueFromPreference("tpdate"));

            if (mCommonSharedPreference.getValueFromPreference("tpdate").equals("null") || mCommonSharedPreference.getValueFromPreference("tpdate").isEmpty()) {

            } else {
                if (mCommonSharedPreference.getValueFromPreference("tpdate").equals(currentDate)) {
                    Log.v("current_datee_home1 ", currentDate + " preference_date " + mCommonSharedPreference.getValueFromPreference("tpdate"));

                } else {
                    Log.v("current_datee_home2 ", currentDate + " preference_date " + mCommonSharedPreference.getValueFromPreference("tpdate"));

                    mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_NAME, "");
                }
            }

            currentDate = currentDate + " 00:00:00";

            //navigationView.setNavigationItemSelectedListener(this);
            mCommonSharedPreference.setValueToPreference("geo_tag", "0");
            if (mCommonSharedPreference.getValueFromPreference("sf_type").equals("1")) {
               // hideItem();
            }
            mCommonSharedPreference.setValueToPreference("visit_dr", "false");
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        deleteFeedInstruction();
        dbh.open();
        mCursor = dbh.select_worktypeList(SF_Code);
        totalCounts = mCursor.getCount();
        DownloadMasters.bindList(new DownloadMasters.CallSlideDownloader() {
            @Override
            public void callDownload() {

                if (!MasterList) {
                    MasterList = true;
//                    SharedPreferences myPrefs;
//                    myPrefs = getSharedPreferences("slide", MODE_WORLD_READABLE);
//                    String StoredValue = "";
//                    StoredValue=myPrefs.getString("slide_download", "");
//                    if(StoredValue.equals("1")){
//
//                    }else{
                    SlidesDownloader(0);
                    //  }

                } else
                    ll_anim.setVisibility(View.GONE);
            }
        });


        callSetUps();
        //tpValidate();
        if (tpCount == 1) {
            Intent i = new Intent(HomeDashBoard.this, DemoActivity.class);
            startActivity(i);
        }

        if (totalCounts > 0) {
            String val = mCommonSharedPreference.getValueFromPreference("workType");
            SharedPreferences sharedpreferences = getSharedPreferences(getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
            String txtt = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, null);

        } else {
            Log.v("download_master_Are", "called_here" + db_connPath + " SF_code " + SF_Code);
            dwnloadMasterData = new DownloadMasters(getApplicationContext(), db_connPath, db_slidedwnloadPath, SF_Code);
            if (!db_connPath.equalsIgnoreCase("null")) {
                dwnloadMasterData.slideListCheck();
            } else {

            }

        }
        tv_worktype.setText(resources.getString(R.string.worktype)+" : ");
        tv_cluster.setText(resources.getString(R.string.cluster)+" : ");
        Log.v("intener_value_checking", mCommonSharedPreference.getValueFromPreference("check_online"));


        HomeDashBoard.bindupdateui(new UpdateUi() {
            @Override
            public void updatingui() {
                mCommonSharedPreference.setValueToPreference("net_con", "connect");
                sendingTp();
            }
        });
        dbh.delete_group_mapping();

        AdapterMissedDate.bindWorkListener(new UpdateUi() {
            @Override
            public void updatingui() {
                displayWrk = true;
                Worktype();
            }
        });

        nav_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (arrayNav.get(i).getText().equals(resources.getString(R.string.change_cluster))){
                    displayWrk = false;
                    setSharedPreference();
                    Worktype();

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.calls))){
                    String wrkNAm = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, null);
                    Log.v("testing", wrkNAm);
                    String wrkcluNAm = sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, null);
                    if (TextUtils.isEmpty(wrkNAm) || wrkNAm.equalsIgnoreCase("null")) {
                        Toast.makeText(HomeDashBoard.this, resources.getString(R.string.dplan_need), Toast.LENGTH_SHORT).show();
                        Worktype();
                    }
/*
                if(TextUtils.isEmpty(wrkcluNAm)|| TextUtils.isEmpty(wrkNAm) || wrkNAm.equalsIgnoreCase("null")){
                    Toast.makeText(HomeDashBoard.this," My Day plan is Needed ",Toast.LENGTH_SHORT).show();
                    Worktype();
                }
*/
                    else {
                       // if (sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, "").equalsIgnoreCase("Field Work"))
                            CommonUtilsMethods.CommonIntentwithNEwTask(DCRCallSelectionActivity.class);
                    }

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.create_presentation))){

                    CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.tour_plan))){

                    CommonUtilsMethods.CommonIntentwithNEwTask(DemoActivity.class);

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.missed_date_entry))){

                    dbh.open();
                    Cursor cur = dbh.select_worktypeList(SF_Code, "Field Work");
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {
                            Log.v("sfcode_count", "Hello");
                            CommonUtils.wrktype_code = cur.getString(1);
                        }
                    }
                    cur.close();
                    dbh.close();
                    MissedClicked = true;
                    String wrkNAm1 = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, null);
                    String wrkcluNAm1 = sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, null);
                    /*if (TextUtils.isEmpty(wrkcluNAm1) || TextUtils.isEmpty(wrkNAm1) || wrkNAm1.equalsIgnoreCase("null")) {
                        Toast.makeText(HomeDashBoard.this, " My Day plan is Needed ", Toast.LENGTH_SHORT).show();

                    }
                    else*/
                    missedDate();

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.leave_application))){

                    if (licence.equals("iil4420")) {
                        CommonUtilsMethods.CommonIntentwithNEwTask(LeaveActivity1.class);
                    } else {
                        CommonUtilsMethods.CommonIntentwithNEwTask(LeaveActivity.class);
                    }

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.approvals))){

                    CommonUtilsMethods.CommonIntentwithNEwTask(ApprovalActivity.class);

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.report))){

                    CommonUtilsMethods.CommonIntentwithNEwTask(ReportActivity.class);

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.near_me))){

                    Log.v("printing_geoneed_val", GpsNeed);
                    //GpsNeed
                    // if(fullCheckPermission() && CommonUtilsMethods.isOnline(HomeDashBoard.this)) {
                    //GpsNeed.equalsIgnoreCase("0") &&
                    if (CommonUtilsMethods.isOnline(HomeDashBoard.this)) {
                        CommonUtilsMethods.CommonIntentwithNEwTask(NearTagActivity.class);
                    } else {
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.offline), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(),"Gps Required For this Option " +GpsNeed,Toast.LENGTH_SHORT).show();
                    }

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.detailing_report))){

                    CommonUtilsMethods.CommonIntentwithNEwTask(ReportOfDetailing.class);

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.logout))){

                    CommonUtilsMethods.CommonIntentwithNEwTask(LoginActivity.class);

                }else if (arrayNav.get(i).getText().equals(resources.getString(R.string.Dashboard))){

                    CommonUtilsMethods.CommonIntentwithNEwTask(DashActivity.class);

                }
                else if (arrayNav.get(i).getText().equals(resources.getString(R.string.activity))){
                    Intent i1 = new Intent(HomeDashBoard.this, DynamicActivity.class);
                    startActivity(i1);

                }

//                switch (arrayNav.get(i).getText()) {
//                    case "Change Cluster"  :
//                        displayWrk = false;
//                        setSharedPreference();
//                        Worktype();
//                        break;
//                    case "Calls":
//                        String wrkNAm = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, null);
//                        Log.v("testing", wrkNAm);
//                        String wrkcluNAm = sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, null);
//                        if (TextUtils.isEmpty(wrkNAm) || wrkNAm.equalsIgnoreCase("null")) {
//                            Toast.makeText(HomeDashBoard.this, " My Day plan is Needed ", Toast.LENGTH_SHORT).show();
//                            Worktype();
//                        }
///*
//                if(TextUtils.isEmpty(wrkcluNAm)|| TextUtils.isEmpty(wrkNAm) || wrkNAm.equalsIgnoreCase("null")){
//                    Toast.makeText(HomeDashBoard.this," My Day plan is Needed ",Toast.LENGTH_SHORT).show();
//                    Worktype();
//                }
//*/
//                        else {
//                            if (sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, "").equalsIgnoreCase("Field Work"))
//                                CommonUtilsMethods.CommonIntentwithNEwTask(DCRCallSelectionActivity.class);
//                        }
//                        break;
//                    case "Create Presentation":
//                        CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);
//                        break;
///*case R.id.nav_dr_profile:
//                        mCommonSharedPreference.setValueToPreference("dr_profile","true");
//                    CommonUtilsMethods.CommonIntentwithNEwTask(DRProfileActivity.class);
//
//                    break;*/
//                    case "Tour Plan":
//                        CommonUtilsMethods.CommonIntentwithNEwTask(DemoActivity.class);
//                        break;
//                    case "Missed Date Entry":
//                        //
//                        dbh.open();
//                        Cursor cur = dbh.select_worktypeList(SF_Code, "Field Work");
//                        if (cur.getCount() > 0) {
//                            while (cur.moveToNext()) {
//                                Log.v("sfcode_count", "Hello");
//                                CommonUtils.wrktype_code = cur.getString(1);
//                            }
//                        }
//                        cur.close();
//                        dbh.close();
//                        MissedClicked = true;
//                        String wrkNAm1 = sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME, null);
//                        String wrkcluNAm1 = sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, null);
//                    /*if (TextUtils.isEmpty(wrkcluNAm1) || TextUtils.isEmpty(wrkNAm1) || wrkNAm1.equalsIgnoreCase("null")) {
//                        Toast.makeText(HomeDashBoard.this, " My Day plan is Needed ", Toast.LENGTH_SHORT).show();
//
//                    }
//                    else*/
//                        missedDate();
//                        break;
//                    case "Leave Application":
//                        if (licence.equals("iil4420")) {
//                            CommonUtilsMethods.CommonIntentwithNEwTask(LeaveActivity1.class);
//                        } else {
//                            CommonUtilsMethods.CommonIntentwithNEwTask(LeaveActivity.class);
//                        }
//                        break;
//                    case "Detailing Report":
//                        CommonUtilsMethods.CommonIntentwithNEwTask(ReportOfDetailing.class);
//                        break;
//                    case "Approvals":
//                        CommonUtilsMethods.CommonIntentwithNEwTask(ApprovalActivity.class);
//                        break;
//                    case "Reports":
//                        CommonUtilsMethods.CommonIntentwithNEwTask(ReportActivity.class);
//                   /* Intent i=new Intent(HomeDashBoard.this,MyVaultActivity.class);
//                    startActivity(i);*/
//                        break;
//                    case "Quiz":
//                        if (progressDialog == null) {
//                            CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(HomeDashBoard.this);
//                            progressDialog = commonUtilsMethods.createProgressDialog(HomeDashBoard.this);
//                            progressDialog.show();
//                        } else {
//                            progressDialog.show();
//                        }
//                        getQuiz();
//                        break;
//                    case "Near Me":
//                        Log.v("printing_geoneed_val", GpsNeed);
//                        //GpsNeed
//                        // if(fullCheckPermission() && CommonUtilsMethods.isOnline(HomeDashBoard.this)) {
//                        //GpsNeed.equalsIgnoreCase("0") &&
//                        if (CommonUtilsMethods.isOnline(HomeDashBoard.this)) {
//                            CommonUtilsMethods.CommonIntentwithNEwTask(NearTagActivity.class);
//                        } else {
//                            Toast.makeText(getApplicationContext(), "You are in OFF - Line ", Toast.LENGTH_SHORT).show();
//                            //Toast.makeText(getApplicationContext(),"Gps Required For this Option " +GpsNeed,Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    case "Logout":
//                        CommonUtilsMethods.CommonIntentwithNEwTask(LoginActivity.class);
//                        break;
//
//                    case "Dashboard":
//                        CommonUtilsMethods.CommonIntentwithNEwTask(DashActivity.class);
//                        break;
//                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        rl_Expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeDashBoard.this, ExpenseActivity.class);
                startActivity(i);
            }
        });
        rl_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeDashBoard.this, DynamicActivity.class);
                startActivity(i);
            }
        });


        progressBarAnimation(60);

/*
        layoutBottomSheet.setOnTouchListener(new OnSwipeTouchListener(HomeDashBoard.this){
            public void onSwipeTop() {
                Log.v("swiping_top","are_clciek"+swipeFun);
                if(swipeFun) {
                    //this one for swipe up
                    CoordinatorLayout.LayoutParams lay = new CoordinatorLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    layoutBottomSheet.setLayoutParams(lay);
                }
                else{

                    sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
                    // sheetBehavior.setHideable(false);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
                }
                //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashBoardMenu()).commit();
                //popupScribbling(2,slideDescribe.get(presentSlidePos).getSlideUrl());
            }

            public void onSwipeBottom(){
                Log.v("swiping_top","are_clciek_bottom");
                CoordinatorLayout.LayoutParams lay=new CoordinatorLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lay.setBehavior(new BottomSheetBehavior(layoutBottomSheet.getContext(), null));
                layoutBottomSheet.setLayoutParams(lay);
                sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
                sheetBehavior.setHideable(false);
                */
/*CoordinatorLayout.LayoutParams lay=new CoordinatorLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutBottomSheet.setLayoutParams(lay);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                swipeFun=false;*//*

                swipeFun=true;
                layoutBottomSheet.post(new Runnable() {
                    @Override
                    public void run() {
                        sheetBehavior.setPeekHeight(129);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        });
*/


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.v("bottom_sheet_nh", "are)one_here");
                        // btnBottomSheet.setText("Close Sheet");
                        swipeFun = true;
                        Log.v("bottom_sheet_nh", "are)one_here" + swipeFun);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        Log.v("bottom_sheet_nh12", "are)one_here");
                        //btnBottomSheet.setText("Expand Sheet");
                        swipeFun = false;
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.v("bottom_sheet_nh13", "are)one_here");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.v("bottom_sheet_nh14", "are)one_here");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.v("bottom_sheet_slide", "are_dragged_here");
            }
        });

        NewCallFragment.bindUpdateViewList(new UpdateUi() {
            @Override
            public void updatingui() {
                if (mCommonSharedPreference.getValueFromPreference("addAct").equalsIgnoreCase("0"))
                    rl_act.setVisibility(View.VISIBLE);
                else
                    rl_act.setVisibility(View.INVISIBLE);

            }
        });

    }

    public void saveOfflineMDP(String json) {
        if (sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CODE, "").equalsIgnoreCase("null") ||
                TextUtils.isEmpty(sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CODE, ""))) {
            dbh.open();
            dbh.insert_mydayplan(json);
            dbh.close();
            mCommonSharedPreference.setValueToPreference("sub_sf_code", mydayhqCd);
            mCommonSharedPreference.setValueToPreference("tpdate", CommonUtilsMethods.getCurrentInstance());


            //https://stackoverflow.com/questions/31175601/how-can-i-change-default-toast-message-color-and-background-color-in-android
                        /*view.getBackground().setColorFilter(YOUR_BACKGROUND_COLOUR, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(YOUR_TEXT_COLOUR);*/

            mCommonSharedPreference.setValueToPreference("workType", "true");
            editor.commit();
            setMDPValue();
        } else {
            popupWorkType("Already my day plan submitted !! ", json, 1);

        }
        dialog.dismiss();
    }

    public void setMDPValue() {
        tv_worktype.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME));
        tv_cluster.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME));
        tv_worktype.setText(resources.getString(R.string.worktype)+" : " + tv_worktype.getText().toString());
        tv_cluster.setText(resources.getString(R.string.cluster)+" : " + tv_cluster.getText().toString());
    }

    public String sendMDP(String jso, final String id) {
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);

        Call<ResponseBody> tdayTP = apiService.SVtodayTP(jso);
        tdayTP.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("final_mdp_work", is.toString());
                    dbh.open();
                    dbh.delete_MDP(id);
                    Cursor cur2 = dbh.select_MDP();
                    if (cur2.getCount() > 0) {
                        cur2.moveToFirst();
                        sendMDP(cur2.getString(1), cur2.getString(0));
                    } else {
                        Cursor cur1 = dbh.select_json_list();
                        if (cur1.getCount() > 0) {
                            cur1.moveToFirst();
                            if (cur1.getString(2).indexOf("_") != -1) {
                                Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                                finalSubmission(cur1.getString(1), cur1.getInt(0));
                            }

                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        return "";
    }

    public void missedWorkType() {
        dialog.setContentView(R.layout.activity_myday_plan);
        dialog.show();
        final TextView tv_worktype = (TextView) dialog.findViewById(R.id.et_mydaypln_worktype);
        final TextView tv_headquater = (TextView) dialog.findViewById(R.id.et_mydaypln_HQ);
        final TextView tv_cluster = (TextView) dialog.findViewById(R.id.et_mydaypln_cluster);
        final ImageView Close = (ImageView) dialog.findViewById(R.id.iv_close);
        final EditText et_remark = (EditText) dialog.findViewById(R.id.et_mydaypln_remarks);
        final ListView rv_wtype = (ListView) dialog.findViewById(R.id.rv_worktypelist);
        final ListView rv_cluster = (ListView) dialog.findViewById(R.id.rv_clusterlist);
        final ListView rv_hqlist = (ListView) dialog.findViewById(R.id.rv_hqlist);
        final Button btn_mydayplan_go = (Button) dialog.findViewById(R.id.btn_mydaypln_go);
        ll_anim = (LinearLayout) dialog.findViewById(R.id.ll_anim);

        dbh.open();
        mCursor = dbh.select_worktypeList(SF_Code);
    }

    public void sendMissedDateCall(String wtname, String wcode, String hcode, String hname) {
        JSONObject json = new JSONObject();
        JSONArray jArray = new JSONArray();
        try {
            json.put("JWWrk", jArray);
            json.put("Inputs", jArray);
            json.put("Products", jArray);
            json.put("AdCuss", jArray);
            json.put("CateCode", "");
            json.put("CusType", "");
            json.put("CustName", "");
            if (SF_Type.equalsIgnoreCase("2")) {
                json.put("DataSF", hcode);
                json.put("SFName", hname);
            } else {
                json.put("DataSF", SF_Code);
                json.put("SFName", tv_userName.getText().toString());
            }
            json.put("DivCode", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            json.put("Entry_location", "");
            json.put("Sf_Code", SF_Code);
            json.put("SF", SF_Code);
            json.put("Div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            json.put("AppUserSF", SF_Code);
            json.put("SpecCode", "");
            json.put("mappedProds", "");
            json.put("mode", "0");
            json.put("WT", wcode);
            json.put("WTNm", wtname);
            json.put("ModTime", CommonUtilsMethods.getCurrentInstance() + " " + CommonUtilsMethods.getCurrentTime());
            json.put("ReqDt", CommonUtilsMethods.getCurrentInstance() + " " + CommonUtilsMethods.getCurrentTime());
            json.put("vstTime", CommonUtilsMethods.getCurrentInstance() + " " + CommonUtilsMethods.getCurrentTime());
            json.put("Remks", "");
            json.put("Pl", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE));
            json.put("amc", "");
            json.put("sign_path", "");
            JSONArray jj = new JSONArray();
            jj.put(json);
            JSONObject jsonCheck = new JSONObject();
            jsonCheck.put("EData", jj);
            jsonCheck.put("SF", SF_Code);
            jsonCheck.put("SF_Code", SF_Code);
            jsonCheck.put("Div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION) + ",");
            jsonCheck.put("ReqDt", CommonUtilsMethods.getCurrentInstance());
            jsonCheck.put("EDt", mCommonSharedPreference.getValueFromPreference("mis_date"));
            Log.v("Printing_missed_Dat", jsonCheck.toString());
            finalSubmissionForMissed(jsonCheck.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Updatecluster() {
        dwnloadMasterData = new DownloadMasters(getApplicationContext(), db_connPath, db_slidedwnloadPath, mydayhqCd, SF_Code);
        ll_anim.setVisibility(View.VISIBLE);
        MasterList = true;
        dwnloadMasterData.drList();
        dwnloadMasterData.chemsList();
        dwnloadMasterData.unDrList();
        dwnloadMasterData.stckList();
        dwnloadMasterData.terrList();
        dwnloadMasterData.jointwrkCall();
        if (mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
            dwnloadMasterData.HosList();

    }


    public void setSharedPreference() {
        if (MissedClicked) {

            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_NAME));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_CODE, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_CODE));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_CLUSTER_CODE));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_SF_CODE, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_SF_CODE));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_SF_HQ, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_SF_HQ));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME));
            mCommonSharedPreference.setValueToPreference("sub_sf_code", mCommonSharedPreference.getValueFromPreference("tmp_sub_sf_code"));
            MissedClicked = false;
        }
    }

    public void getQuiz() {

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            paramObject.put("SF", SF_Code);
            paramObject.put("sfcode", mCommonSharedPreference.getValueFromPreference("sub_sf_code"));

            Log.v("json_object_custom", paramObject.toString());
            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> reports = apiService.getQuiz(paramObject.toString());
            reports.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("json_object_quiz", is.toString());
                            mCommonSharedPreference.setValueToPreference("quizdata", is.toString());
                            progressDialog.dismiss();


                            JSONObject ja = new JSONObject(is.toString());
                            if (ja.getString("success").equalsIgnoreCase("false"))
                                Toast.makeText(HomeDashBoard.this, ja.getString("msg"), Toast.LENGTH_SHORT).show();
                            else
                                popupQuiz("");


                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }

    public void popupQuiz(String s) {
        final Dialog dialog = new Dialog(HomeDashBoard.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_quiz);
        dialog.show();
        int attempt = 0;
        Button btn_start = dialog.findViewById(R.id.btn_start);
        final Button btn_dwn = dialog.findViewById(R.id.btn_dwn);
        TextView txt_atmpt = dialog.findViewById(R.id.txt_atmpt);
        TextView txt_time = dialog.findViewById(R.id.txt_time);
        TextView txt_ques = dialog.findViewById(R.id.txt_ques);
        TextView txt_result = dialog.findViewById(R.id.txt_result);

        try {
            JSONObject jj = new JSONObject(mCommonSharedPreference.getValueFromPreference("quizdata").toString());
            ja1 = jj.getJSONArray("quiztitle");
            JSONArray ja = jj.getJSONArray("processUser");
            JSONArray ques = jj.getJSONArray("questions");
            JSONObject jjs = ja.getJSONObject(0);
            attempt = Integer.parseInt(jjs.getString("NoOfAttempts")) - 1;
            txt_atmpt.setText("Attempt : " + jjs.getString("NoOfAttempts"));
            txt_time.setText("Time Limit : " + jjs.getString("timelimit"));
            txt_ques.setText("Questions : " + ques.length());


        } catch (Exception e) {
        }
        if (!TextUtils.isEmpty(s)) {
            txt_result.setText(s);
            try {
                JSONObject jj = new JSONObject(mCommonSharedPreference.getValueFromPreference("quizdata").toString());
                JSONArray ja = jj.getJSONArray("processUser");
                JSONObject jjs = ja.getJSONObject(0);
                jjs.put("NoOfAttempts", attempt);
                mCommonSharedPreference.setValueToPreference("quizdata", jj.toString());
            } catch (Exception e) {
            }
            txt_atmpt.setText("Attempt : " + attempt);
            if (attempt == 0) {
                btn_start.setVisibility(View.GONE);
            }
        }
        // Log.v("print_true_false",fileExist("/storage/emulated/0/Quiz/survey.png")+"");
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent ii = new Intent(HomeDashBoard.this, QuizActivity.class);
                startActivityForResult(ii, 678);

            }
        });
        btn_dwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject jjj = ja1.getJSONObject(0);
                    if (TextUtils.isEmpty(jjj.getString("FileName"))) {
                        Toast.makeText(HomeDashBoard.this, resources.getString(R.string.filenotavailable),
                                Toast.LENGTH_LONG).show();
                    } else {
                        if (!fileExist("/storage/emulated/0/Quiz/" + jjj.getString("FileName"))) {
                            //btn_dwn.setEnabled(false);
                            Log.v("Printing_except_intt", db_connPath + quizPAth + jjj.getString("FileName") + " what " + db_connPath.substring(0, db_connPath.length() - 10));

                            // down(db_connPath.substring(0, db_connPath.length() - 10) + quizPAth + jjj.getString("FileName"), jjj.getString("FileName"));


                            new downloadAsync(db_connPath.substring(0, db_connPath.length() - 10) + quizPAth + jjj.getString("FileName"), jjj.getString("FileName")).execute();
                        } else {
                            File ff = new File("/storage/emulated/0/Quiz/" + jjj.getString("FileName"));
                            openFile(ff);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("Printing_except_intt", e.getLocalizedMessage() + "");
                }

            }
        });

    }

    public void tpValidate() {
        try {
            JSONObject jj = new JSONObject();

            jj.put("div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            jj.put("SF", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> reports = apiService.getTpValidate(jj.toString());
            reports.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("json_object_validate", is.toString());
                            JSONArray ja = new JSONArray(is.toString());
                            JSONObject js = ja.getJSONObject(0);
                            if (js.getString("GetCount").equalsIgnoreCase("1")) {
                                tpCount = 1;
                            } else {
                                tpCount = 0;
                            }
                            if (tpCount == 1) {
                                Intent i = new Intent(HomeDashBoard.this, DemoActivity.class);
                                startActivity(i);
                            }

                            // JSONArray   ja=new JSONArray(is.toString());


                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }

    }

    class downloadAsync extends AsyncTask<Void, String, File> {
        String urll = null, filename;

        public downloadAsync(String url, String filename) {
            this.urll = url;
            this.filename = filename;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(HomeDashBoard.this, resources.getString(R.string.filedownloading),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(File s) {
            super.onPostExecute(s);
            Toast.makeText(HomeDashBoard.this, resources.getString(R.string.downldsuccess),
                    Toast.LENGTH_SHORT).show();

            openFile(s);
        }


        @Override
        protected File doInBackground(Void... voids) {
            File targetLocation = null;
            try {
                Log.v("downlioad_url", urll);
                URL url = new URL(urll);
                URLConnection c = url.openConnection();
                c.connect();

                File file = new File(Environment.getExternalStorageDirectory() + "/Quiz"); /*Internal Storage*/

                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create IMAGE_DIRECTORY_NAME directory");
                    }
                }
                targetLocation = new File(file.getPath() + File.separator + filename);
                // File targetLocationnn = new File(file11.getPath() + File.separator+ fileName);
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(targetLocation);

                Log.v("LOG_TAG", "PATH: " + targetLocation);

                byte[] buffer = new byte[4096];
                int len1 = 0;

                while ((len1 = input.read(buffer)) != -1) {
                    output.write(buffer, 0, len1);
                    publishProgress("A new file is downloading");
                }

                output.close();
                input.close();


            } catch (IOException e) {
                Log.v("Printing_except_intt", e.getMessage());
                e.printStackTrace();
            }

            return targetLocation;
        }


    }

    public boolean fileExist(String fname) {
        File file = new File(fname);
        return file.exists();
    }

    private void openFile(File url) {

        try {

            // Uri uri = Uri.fromFile(url);
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")) {
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, resources.getString(R.string.nofile), Toast.LENGTH_SHORT).show();
        }
    }

    public void addNavItem() {

        arrayNav.clear();

        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_clu_mnu, /*"Change Cluster"*/resources.getString(R.string.change_cluster)));
        arrayNav.add(new ModelNavDrawer(R.mipmap.calls, /*"Calls"*/resources.getString(R.string.calls)));
        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_presentation, /*"Create Presentation"*/resources.getString(R.string.create_presentation)));
//        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_tourplan, /*"Tour Plan"*/resources.getString(R.string.tour_plan)));
        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_tourplan, /*"Missed Date Entry"*/resources.getString(R.string.missed_date_entry)));
        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_leaveappln, /*"Leave Application"*/resources.getString(R.string.leave_application)));
        if (mCommonSharedPreference.getValueFromPreference("sf_type").equals("2")) {
            arrayNav.add(new ModelNavDrawer(R.mipmap.nav_tourplan, /*"Approvals"*/resources.getString(R.string.approvals)));

        }


            arrayNav.add(new ModelNavDrawer(R.mipmap.nav_reports, /*"Reports"*/resources.getString(R.string.report)));
        // arrayNav.add(new ModelNavDrawer(R.mipmap.nav_reports,"Quiz"));
        if (mCommonSharedPreference.getValueFromPreference("GpsFilter").equalsIgnoreCase("0")) {
            arrayNav.add(new ModelNavDrawer(R.mipmap.nav_reports, /*"Near Me"*/resources.getString(R.string.near_me)));

        }

        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_reports, /*"Detailing Report"*/resources.getString(R.string.detailing_report)));
        arrayNav.add(new ModelNavDrawer(R.drawable.activity_img, /*"Logout"*/resources.getString(R.string.activity)));

        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_logout, /*"Logout"*/resources.getString(R.string.logout)));
        //arrayNav.add(new ModelNavDrawer(R.mipmap.nav_reports, /*"Dashboard"*/resources.getString(R.string.Dashboard)));
        // arrayNav.add(new Mod        arrayNav.add(new ModelNavDrawer(R.mipmap.nav_logout, /*"Logout"*/resources.getString(R.string.logout)));elNavDrawer(R.mipmap.nav_reports, "Target Vs Sales"));

        navAdpt = new NavigationItemAdapter(arrayNav, HomeDashBoard.this);
        nav_list.setAdapter(navAdpt);
    }

    public void checkingPermissionsNew(){
        if (ContextCompat.checkSelfPermission(HomeDashBoard.this, MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.v("permission_check","are_not_granted");
            // Permission is not granted
            if (ContextCompat.checkSelfPermission(HomeDashBoard.this,
                    MANAGE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeDashBoard.this,
                        MANAGE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    Log.v("accessPermission","went_request");
                    ActivityCompat.requestPermissions(HomeDashBoard.this,
                            new String[]{MANAGE_EXTERNAL_STORAGE},
                            12);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                accessStorageAllowedNew=true;

            }
        }else {
            // Permission has already been granted
            accessStorageAllowedNew=true;

        }
    }

}
