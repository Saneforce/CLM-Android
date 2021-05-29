package saneforce.sanclm.fragments;


import android.Manifest;
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
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.DCRLastVisitDetails;
import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.Pojo_Class.MontlyVistDetail;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.DetailingCreationActivity;
import saneforce.sanclm.activities.DynamicActivity;
import saneforce.sanclm.activities.FeedbackActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.MapsActivity;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.activities.NearTagActivity;
import saneforce.sanclm.adapter_class.AdapterForDynamicSelectionList;
import saneforce.sanclm.adapter_class.Custom_DCR_GV_Dr_Contents;
import saneforce.sanclm.adapter_class.DCR_GV_Selection_adapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.applicationCommonFiles.GPSTrack;
import saneforce.sanclm.applicationCommonFiles.LocationTrack;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DCRCallSelectionFilter;
import saneforce.sanclm.util.LocationUpdate;
import saneforce.sanclm.util.ManagerListLoading;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class DCRDRCallsSelection extends Fragment implements View.OnClickListener{
    DataBaseHandler dbh;
    View v;
    Cursor mCursor;
    CommonSharedPreference mCommonSharedPreference;
    boolean isEmpty=false;
    ImageView iv_back;
    CommonUtilsMethods commonUtilsMethods;
    DCR_GV_Selection_adapter _DCR_GV_Selection_adapter=null;
    ArrayList<Custom_DCR_GV_Dr_Contents> drList ;
    RelativeLayout rl_dcr_precall_analysisMain , dcr_drselection_gridview,rl_drPrecallanalysis,rl_chmPrecallanalysis;
    Button btn_re_select_doctor,btn_act;
    TextView tv_drName,tv_drqual,tv_drdob,tv_drdow,tv_drmob,tv_drcat ,tv_drspecl,tv_drtown,tv_dremail,tv_draddr,tv_lastvstDt_set,
    tv_pdts_promoted_set,tv_gifts_set,tv_feedbck_set,tv_rmks_set;
    String SF_Code,mMydayWtypeCd,db_connPath, pdtDtls ,giftDtls,db_slidedwnloadPath,SF_Type;
    Custom_DCR_GV_Dr_Contents _custom_DCR_GV_Dr_Contents;
    EditText et_searchDr;
    ImageButton ibtn_btn_go,ibtn_skip;
    saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO DetailingTrackerPOJO;
    LineChart lineChart;
    LocationTrack locationTrack;
   static LocationUpdate locationUpdate;
   String subSfCode;
   Spinner spinner,spinner_hos;
    List<String> categories = new ArrayList<String>();
    ArrayList<PopFeed>  hospital_array=new ArrayList<>();
    List<String> SF_coding = new ArrayList<String>();
    LinearLayout ll_anim;
    String selectedProductCode="";
    TextView  spin_txt;
    TextView txt_tool_header;
    public static DCRDRCallsSelection newInstance() {
        DCRDRCallsSelection fragment = new DCRDRCallsSelection();
        return fragment;
    }
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    ProgressDialog progressDialog=null;
    int spinnerChoosingSfCode;
    double laty=0.0,lngy=0.0,limitKm=0.5;
    GPSTrack mGPSTrack;
    String geoFencing="0",gpsNeed="0";
    String drpos="0";
    RelativeLayout  rlay_spin,app_config;

    int currentItem=0;
    String language;
    Context context;
    Resources resources;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){

        }
        else{
            Log.v("is_tat_visible","false");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        FullScreencall();
       Log.v("hello_called","here");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("hello_called_start","here");
        if(!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("map_return_count"))){
            int xx=mCommonSharedPreference.getValueFromPreference("dr_pos",0);
            Custom_DCR_GV_Dr_Contents mm=drList.get(xx);
            int yy= Integer.parseInt(mm.getTag())+1;
            mm.setTag(String.valueOf(yy));
            Log.v("hello_called_start","here"+xx+" tag_val "+yy);
            _DCR_GV_Selection_adapter.notifyDataSetChanged();
            mCommonSharedPreference.setValueToPreference("map_return_count","");
        }
       /* if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("1")){
            if(_DCR_GV_Selection_adapter!=null && dbh!=null){
                dbh.open();
                dbh.updateDrTagCount(drcodes,)
            }
                _DCR_GV_Selection_adapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbh = new DataBaseHandler(getContext());
        mCommonSharedPreference = new CommonSharedPreference(getContext());
        DetailingTrackerPOJO = new DetailingTrackerPOJO();
        commonUtilsMethods = new CommonUtilsMethods(getActivity());
        //locationTrack=new LocationTrack(getActivity());
        v = inflater.inflate(R.layout.activity_dcrdrcalls_selection, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(getActivity(), language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
        }

        rl_dcr_precall_analysisMain = (RelativeLayout) v.findViewById(R.id.rl_dcr_precall_analysis_main) ;
        dcr_drselection_gridview= (RelativeLayout) v.findViewById(R.id.rl_dcr_drgrid_selection) ;
        rlay_spin= (RelativeLayout) v.findViewById(R.id.rlay_spin) ;
        app_config= (RelativeLayout) v.findViewById(R.id.app_config) ;
        rl_drPrecallanalysis = (RelativeLayout) v.findViewById(R.id.rl_dcr_drDetails);
        rl_chmPrecallanalysis = (RelativeLayout) v.findViewById(R.id.rl_dcr_chmDetails);
        btn_re_select_doctor = (Button)  v.findViewById(R.id.btn_reselect) ;
        btn_act = (Button)  v.findViewById(R.id.btn_act) ;
        txt_tool_header=v.findViewById(R.id.txt_tool_header);
        txt_tool_header.setText(mCommonSharedPreference.getValueFromPreference("drcap")+" "+resources.getString(R.string.Selection));
        Log.d("daataselec",mCommonSharedPreference.getValueFromPreference("drcap"));
        //FullScreencall();
        if(mCommonSharedPreference.getValueFromPreference("addAct").equalsIgnoreCase("0"))
            btn_act.setVisibility(View.VISIBLE);
        ibtn_btn_go  = (ImageButton)  v.findViewById(R.id.btn_go) ;
        ibtn_skip  = (ImageButton)  v.findViewById(R.id.btn_skip) ;
        tv_drName = (TextView)  v.findViewById(R.id.tv_dcrsel_drname) ;
        tv_drqual = (TextView)  v.findViewById(R.id.tv_drqual_set) ;
        tv_drdob = (TextView)  v.findViewById(R.id.tv_drdob_set) ;
        tv_drdow = (TextView)  v.findViewById(R.id.tv_dr_wedding_set) ;
        spin_txt = (TextView)  v.findViewById(R.id.spin_txt) ;
        tv_drmob = (TextView)  v.findViewById(R.id.tv_drmobile_set) ;
        tv_drcat = (TextView)  v.findViewById(R.id.tv_drcat_set) ;
        tv_drspecl = (TextView)  v.findViewById(R.id.tv_drspeciality_set) ;
        tv_drtown = (TextView)  v.findViewById(R.id.tv_drterritry_set) ;
        tv_dremail = (TextView)  v.findViewById(R.id.tv_drmail_set) ;
        tv_draddr = (TextView)  v.findViewById(R.id.tv_draddr_set) ;

        tv_lastvstDt_set =(TextView)  v.findViewById(R.id.tv_visit_date_set) ;
        tv_pdts_promoted_set =(TextView)  v.findViewById(R.id.tv_pdtdetailed_set) ;
        tv_gifts_set =(TextView)  v.findViewById(R.id.tv_gifts_set) ;
        tv_feedbck_set =(TextView)  v.findViewById(R.id.tv_feedback_set) ;
        tv_rmks_set=(TextView)  v.findViewById(R.id.tv_remark_set) ;
        spinner = (Spinner) v.findViewById(R.id.spinner);
        //spinner_hos = (Spinner) v.findViewById(R.id.spinner_hos);
        Log.v("string_hospfilter",mCommonSharedPreference.getValueFromPreference("hosp_filter"));
        if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
            rlay_spin.setVisibility(View.VISIBLE);
        et_searchDr = (EditText) v.findViewById(R.id.et_searchDr);
        et_searchDr.setText("");
        et_searchDr.setHint(resources.getString(R.string.search)+" "+mCommonSharedPreference.getValueFromPreference("drcap"));
        lineChart = (LineChart) v.findViewById(R.id.chart);
        tv_pdts_promoted_set.setMovementMethod(new ScrollingMovementMethod());
        dcr_drselection_gridview.setVisibility(View.VISIBLE);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code= mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        mMydayWtypeCd = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE);
        subSfCode=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        db_slidedwnloadPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        SF_Type =  mCommonSharedPreference.getValueFromPreference("sf_type");
        geoFencing =  mCommonSharedPreference.getValueFromPreference("geoneed");
        gpsNeed =  mCommonSharedPreference.getValueFromPreference("GpsFilter");
        limitKm = Double.parseDouble(mCommonSharedPreference.getValueFromPreference("radius"));

        FullScreencall();


        if(SF_Type.equalsIgnoreCase("2")){
            spinner.setVisibility(View.VISIBLE);
        }
        else
            spinner.setVisibility(View.GONE);
        if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
            getHospital(0);

        if(gpsNeed.equals("0")) {
           /* boolean locValue = CurrentLoc();
            laty = mGPSTrack.getLatitude();
            lngy = mGPSTrack.getLongitude();*/
            SharedPreferences shares=getActivity().getSharedPreferences("location",0);
            laty= Double.parseDouble(shares.getString("lat","0.0"));
            lngy= Double.parseDouble(shares.getString("lng","0.0"));
            Log.v("Dr_selection_key",laty+" lngy "+lngy);
        }

      /*  SharedPreferences shares=getActivity().getSharedPreferences("location",0);
        laty= Double.parseDouble(shares.getString("lat",""));
        lngy= Double.parseDouble(shares.getString("lng"," "));
        Log.v("Dr_selection_key",laty+" lngy "+lngy);
*/
        /*boolean locValue=CurrentLoc();
        Log.v("loc_value",locValue+" lat "+mGPSTrack.getLatitude());*/

        dbh.open();
        drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
        drList.clear();
        Log.v("checking_sfcode",SF_Code+"cluster"+mMydayWtypeCd+"subsfcode"+subSfCode);
      /*  if(TextUtils.isEmpty(subSfCode) || subSfCode.equalsIgnoreCase("null"))
        mCursor = dbh.select_doctors_bySf(SF_Code,mMydayWtypeCd);
        else*/
            mCursor = dbh.select_doctors_bySf(subSfCode,mMydayWtypeCd);
        Log.v("cursor_count", String.valueOf(mCursor.getCount()));
        while (mCursor.moveToNext()) {
           /* if(distance(laty,lngy,lt,ln)<limitKm){


            }
           */
            Log.v("Dr_detailing_Print123",mCursor.getString(2)+"comm"+mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0")
            +"fencing"+geoFencing);
           if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
               String yy=mCursor.getString(11);
               if (!TextUtils.isEmpty(mCursor.getString(11))){
                   Log.v("Dr_detailing_Print",mCursor.getString(2));
                   if (distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12))) < limitKm) {
                       _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
                       drList.add(_custom_DCR_GV_Dr_Contents);
                       Log.v("Dr_detailing_figure_dk","lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(2));

                   }
                   else{
                      Log.v("Dr_detailing_figure",distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12)))+"lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(1)+"nn"+mCursor.getString(2));
                   }
           }
           }
           else {
               Log.v("Dr_detailing_Print12366",mCursor.getString(2));
               _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
               drList.add(_custom_DCR_GV_Dr_Contents);
           }
        }


        GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList,"D");
        gridView.setAdapter(_DCR_GV_Selection_adapter);
        categories.clear();
        SF_coding.clear();
        mCursor=dbh.select_hqlist_manager();
        if(mCursor.getCount()!=0) {
            if (mCursor.moveToFirst()) {

                do {
                    Log.v("Name_hqlist", mCursor.getString(2));
                    if(subSfCode.equalsIgnoreCase(mCursor.getString(1))){
                        if(SF_coding.size()!=0){
                            categories.add(0,mCursor.getString(2));
                            SF_coding.add(0,mCursor.getString(1));
                        }
                        else{
                            categories.add(mCursor.getString(2));
                            SF_coding.add(mCursor.getString(1));
                        }
                    }
                    else {
                        categories.add(mCursor.getString(2));
                        SF_coding.add(mCursor.getString(1));
                    }
                } while (mCursor.moveToNext());

            }
        }
        if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("1")){
            rlay_spin.setVisibility(View.INVISIBLE);
        }
       /* final ProgressDialog dialog = new ProgressDialog(getActivity());



        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_progress);*/
       // Log.v("location_lati", String.valueOf(locationTrack.getLatitude()));
        //gridView.setOnItemClickListener(this);
        ibtn_btn_go.setOnClickListener(this);
        ibtn_skip.setOnClickListener(this);

        rlay_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSpinner();
            }
        });

        et_searchDr.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {



            }

            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2,int arg3) {

            }

            @Override
            public void afterTextChanged(Editable cs) {
                Log.v("filter_edt_txt", String.valueOf(cs.length()));
              /*  if(cs.length()==0) {
                    _DCR_GV_Selection_adapter.getFilter().filter(" ");
                    _DCR_GV_Selection_adapter.notifyDataSetChanged();
                }
                else{*/
                    _DCR_GV_Selection_adapter.getFilter().filter(cs);
                    _DCR_GV_Selection_adapter.notifyDataSetChanged();
             //   }

            }
        });


        //onclick the row item after filter
        DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
            @Override
            public void itemClick(String drname, String drcode) {


                if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") &&
                        mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("false") &&
                        mCommonSharedPreference.getValueFromPreference("dr_profile").equalsIgnoreCase("false")){
                    OnItemClick(drname,drcode);
                    et_searchDr.setText("");
                    rlay_spin.setVisibility(View.GONE);

                }
                else if(mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){
                    setValueForFeed(drname,drcode);

                }
                else if(mCommonSharedPreference.getValueFromPreference("dr_profile").equalsIgnoreCase("false")){
                    mCommonSharedPreference.setValueToPreference("Pname",drname);
                    mCommonSharedPreference.setValueToPreference("Pcode",drcode);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.app_config, new DrProfile()).commit();

                }
                else{
           //         Log.v("Printing_near_tag", NearTagActivity.list.get(0).getLat()+" ");
                    if(NearTagActivity.list.contains(new ViewTagModel(drname))){
                        for(int i=0;i<NearTagActivity.list.size();i++){
                            if(NearTagActivity.list.get(i).getName().equalsIgnoreCase(drname)){
                                Intent ii=new Intent(getActivity(), MapsActivity.class);
                                ii.putExtra("drcode",drcode);
                                ii.putExtra("drname",drname);
                                ii.putExtra("cust","D");
                                ii.putExtra("lats",NearTagActivity.list.get(i).getLat());
                                ii.putExtra("lngs",NearTagActivity.list.get(i).getLng());
                                i=NearTagActivity.list.size();
                                startActivity(ii);

                            }
                        }
                    }
                    else{
                        Intent i=new Intent(getActivity(), MapsActivity.class);
                        i.putExtra("drcode",drcode);
                        i.putExtra("drname",drname);
                        i.putExtra("cust","D");
                        i.putExtra("lats","0.0");
                        i.putExtra("lngs","0.0");
                        startActivity(i);
                    }
                }

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.textview_bg_spinner, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.textview_bg_spinner);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
               CommonUtilsMethods.avoidSpinnerDropdownFocus(spinner);
                dbh.open();
                Log.v("printing_item_sele",commonUtilsMethods.isOnline(getActivity())+" ");
                mCursor = dbh.select_doctors_bySf(SF_coding.get(i),mMydayWtypeCd);
                if(drList.size()==0 && mCursor.getCount()==0) {
                    if(commonUtilsMethods.isOnline(getActivity())) {
                        DownloadMasters dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_coding.get(i),SF_Code);
                        if (progressDialog == null) {
                            CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(getActivity());
                            progressDialog = commonUtilsMethods.createProgressDialog(getActivity());
                            progressDialog.show();
                        } else {
                            progressDialog.show();
                        }
                        //ll_anim.setVisibility(View.VISIBLE);
                        subSfCode=SF_coding.get(i);
                        dwnloadMasterData.drList();
                        if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
                        dwnloadMasterData.HosList();

                    }else{
                        Toast.makeText(getActivity(),getResources().getString(R.string.network_req),Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                    drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                    drList.clear();
                    while (mCursor.moveToNext()) {
                        if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
                            String yy=mCursor.getString(11);
                            if (!TextUtils.isEmpty(mCursor.getString(11))){
                                Log.v("Dr_detailing_Print",mCursor.getString(2));
                                if (distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12))) < limitKm) {
                                    _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
                                    drList.add(_custom_DCR_GV_Dr_Contents);
                                    Log.v("Dr_detailing_figure_dk","lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(2));

                                }
                                else{
                                    Log.v("Dr_detailing_figure",distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12)))+"lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(1)+"nn"+mCursor.getString(2));
                                }
                            }
                        }

                        else {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
                            drList.add(_custom_DCR_GV_Dr_Contents);
                        }
                    }

                    GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                    _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList,"D");
                    gridView.setAdapter(_DCR_GV_Selection_adapter);
                    _DCR_GV_Selection_adapter.notifyDataSetChanged();
                }

                spinnerChoosingSfCode=i;

                DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                    @Override
                    public void ListLoading() {
                        dbh.open();
                        drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                        drList.clear();
                        Log.v("checking_sfcode",SF_Code+"cluster"+mMydayWtypeCd);

                        mCursor = dbh.select_doctors_bySf(SF_coding.get(spinnerChoosingSfCode),mMydayWtypeCd);

                        Log.v("cursor_count", String.valueOf(mCursor.getCount()));
                        while (mCursor.moveToNext()) {
                            if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
                                String yy=mCursor.getString(11);
                                if (!TextUtils.isEmpty(mCursor.getString(11))){
                                    Log.v("Dr_detailing_Print",mCursor.getString(2));
                                    if (distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12))) < limitKm) {
                                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
                                        drList.add(_custom_DCR_GV_Dr_Contents);
                                        Log.v("Dr_detailing_figure_dk","lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(2));

                                    }
                                    else{
                                        Log.v("Dr_detailing_figure",distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12)))+"lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(1)+"nn"+mCursor.getString(2));
                                    }
                                }
                            }

                            else {
                                _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5), mCursor.getString(22), mCursor.getString(23),mCursor.getString(8));
                                drList.add(_custom_DCR_GV_Dr_Contents);
                            }
                        }

                        GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList,"D");
                        gridView.setAdapter(_DCR_GV_Selection_adapter);
                        getHospital(1);
                        progressDialog.dismiss();
                    }

                });


//                spinner.setSelection(i);
//                switch (i){
//                    case 0:
//                        Log.v("Spinner Selected:",adapterView.getSelectedItem().toString());
//                        FullScreencall();
//                        break;
//                    case 1:
//                        Log.v("Spinner Selected:",adapterView.getSelectedItem().toString());
//                        FullScreencall();
//                        break;
//                }


                //dwnloadMasterData.jointwrkCall();


               // mCommonSharedPreference.setValueToPreference("sub_sf_code",SF_coding.get(i));
           /*   DownloadMasters  dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_coding.get(i));
                ll_anim.setVisibility(View.VISIBLE);
                dwnloadMasterData.drList();
                dwnloadMasterData.chemsList();
                dwnloadMasterData.unDrList();
                dwnloadMasterData.stckList();
                dwnloadMasterData.terrList();
                //dwnloadMasterData.jointwrkCall();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag = new DCRDRCallsSelection();
                ft.replace(R.id.app_config, frag);
                ft.commit();
                ll_anim.setVisibility(View.INVISIBLE);*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*  spinner_hos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                dbh.open();

                        mCursor = dbh.select_doctors_bySfAndHos(SF_coding.get(spinnerChoosingSfCode),mMydayWtypeCd,hos_code.get(i));
                    drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                    drList.clear();
                    Log.v("printing_doc_hos",mCursor.getCount()+"");
                    while (mCursor.moveToNext()) {
                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5),mCursor.getString(22),mCursor.getString(23));
                        drList.add(_custom_DCR_GV_Dr_Contents);

                }

                GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList,"D");
                gridView.setAdapter(_DCR_GV_Selection_adapter);
                _DCR_GV_Selection_adapter.notifyDataSetChanged();

                //dwnloadMasterData.jointwrkCall();


               // mCommonSharedPreference.setValueToPreference("sub_sf_code",SF_coding.get(i));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.isDrawTopYLabelEntryEnabled();
        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisMaxValue(6);


        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateY(2000);
        lineChart.animateX(2000);

       /* ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(0, 1));
        entries.add(new Entry(0, 2));
        entries.add(new Entry(0, 3));
        entries.add(new Entry(0, 4));
        entries.add(new Entry(0, 5));
        entries.add(new Entry(0, 6));
        entries.add(new Entry(0, 7));
        entries.add(new Entry(0, 8));
        entries.add(new Entry(0, 9));
        entries.add(new Entry(0, 10));
        entries.add(new Entry(0, 11));

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Dec");

        XAxis xl = lineChart.getXAxis();
        xl.setTextColor(Color.GREEN);
        xl.setDrawGridLines(false);
        xl.setDrawLabels(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(1);
        xl.setEnabled(true);

       *//* YAxis leftAxis1 = lineChart.getAxisLeft();
        leftAxis1.setTextColor(Color.WHITE);
        leftAxis1.setSpaceBe
        leftAxis1.setDrawGridLines(true);*//*

        LineDataSet dataset = new LineDataSet(entries,"of  Calls");
        dataset.setDrawCubic(true);
        dataset.setDrawValues(false);

        *//* dataset.enableDashedLine(10f, 5f, 0f);*//*

        lineChart.getLegend().setEnabled(false);
        lineChart.setDescription("");
        LineData data = new LineData(labels, dataset);
        lineChart.setData(data);*/

        btn_re_select_doctor.setOnClickListener(this);

        iv_back = (ImageView)   v.findViewById(R.id.iv_back);
        /*iv_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });*/

        btn_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SharedPreferences shares=getActivity().getSharedPreferences("location",0);
                    String lat=shares.getString("lat","");
                    String lng=shares.getString("lng","");
                    Log.v("example_code",CommonUtils.TAG_DOCTOR_NAME);
                    JSONObject jj=new JSONObject();
                    jj.put("typ","1");
                    jj.put("cust",CommonUtils.TAG_DOCTOR_CODE);
                    jj.put("custname",CommonUtils.TAG_DOCTOR_NAME);
                    jj.put("lat",lat);
                    jj.put("lng",lng);
                    jj.put("DataSF",subSfCode);
                    jj.put("wt",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CODE));
                    jj.put("pl",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE));
                    mCommonSharedPreference.setValueToPreference("cust_act",jj.toString());
                    Intent  ii=new Intent(getActivity(), DynamicActivity.class);
                    startActivity(ii);
                }catch (Exception   e){
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonFun();
                if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("1")){
                    if(mCommonSharedPreference.getValueFromPreference("visit_dr").equalsIgnoreCase("true")){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new NearMe()).commit();
                    }
                    else
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();
                }
                else{
                    if(mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){
                        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_NAME));
                        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_CODE, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_CODE));
                        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE,mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_CLUSTER_CODE));
                        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_SF_CODE, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_SF_CODE));
                        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_SF_HQ, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_SF_HQ));
                        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME));
                        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME));
                        mCommonSharedPreference.setValueToPreference("sub_sf_code",mCommonSharedPreference.getValueFromPreference("tmp_sub_sf_code"));

                        Log.v("getting_sf_code",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE)+" sf_sf "+"");

                    }
                    Intent LoginAc = new Intent(getActivity(), HomeDashBoard.class);
                    startActivity(LoginAc);

                }

            }
        });
        if(mCommonSharedPreference.getValueFromPreference("visit_dr").equalsIgnoreCase("true")){
            btn_re_select_doctor.setVisibility(View.INVISIBLE);
            ibtn_btn_go.setVisibility(View.INVISIBLE);
            ibtn_skip.setVisibility(View.INVISIBLE);
            String cname=mCommonSharedPreference.getValueFromPreference("drnames");
            String ccode=mCommonSharedPreference.getValueFromPreference("drcodes");
            OnItemClick(cname,ccode);

        }
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        return v;
    }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    /*  @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Custom_DCR_GV_Dr_Contents custom_dcr_gv_dr_contents  =  drList.get(position);
          tv_drName.setText(custom_dcr_gv_dr_contents.getmDoctorName());
          DetailingTrackerPOJO.setmDoctorCode(custom_dcr_gv_dr_contents.getmDoctorcode());
          rl_drPrecallanalysis.setVisibility(View.VISIBLE);
          rl_chmPrecallanalysis.setVisibility(View.GONE);
          rl_dcr_precall_analysisMain.setVisibility(View.VISIBLE);
          dcr_drselection_gridview.setVisibility(View.GONE);

          HashMap<String, String> map = new HashMap<String, String>();
          Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
          map.clear();
          map.put("SF", SF_Code);
          map.put("typ", "D");
          map.put("CusCode", custom_dcr_gv_dr_contents.getmDoctorcode());

          Call<List<DCRLastVisitDetails>> dcrlastcallDtls = apiService.dcrLastcallDtls(map);
          dcrlastcallDtls.enqueue(DcrlastcallDtls);

          Toast.makeText(getActivity(), ""+custom_dcr_gv_dr_contents.getmDoctorcode(), Toast.LENGTH_SHORT).show();

          try{
              dbh.open();
              Cursor mCursor = dbh.select_doctorDetails(custom_dcr_gv_dr_contents.getmDoctorcode());
              while (mCursor.moveToNext()){
                  tv_drqual.setText(mCursor.getString(14));
                  tv_drdob.setText(mCursor.getString(3));
                  tv_drdow.setText(mCursor.getString(4));
                  tv_drmob.setText(mCursor.getString(16));
                  tv_drcat.setText(mCursor.getString(9));
                  tv_drspecl.setText(mCursor.getString(10));
                  tv_drtown.setText(mCursor.getString(6));
                  tv_dremail.setText(mCursor.getString(15));
                  tv_draddr.setText(mCursor.getString(18));
                  DetailingTrackerPOJO.setmDoctorSpeciality(mCursor.getString(8));

              }

          }catch(Exception e){

          }finally {
              dbh.close();
          }

      }*/
    public Callback<List<DCRLastVisitDetails>> DcrlastcallDtls = new Callback<List<DCRLastVisitDetails>>() {
        @Override
        public void onResponse(Call<List<DCRLastVisitDetails>> call, Response<List<DCRLastVisitDetails>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    List<DCRLastVisitDetails> dcrLastvstDtls = response.body();
                    pdtDtls = dcrLastvstDtls.get(0).getProdSamp().replace("#"," , ");
                    pdtDtls = pdtDtls.replace("~","-");
                    pdtDtls = pdtDtls.replace("$", "-");
                    pdtDtls = pdtDtls.replace("^", "-");
                    giftDtls = dcrLastvstDtls.get(0).getInputs();

                    tv_lastvstDt_set.setText(" : "+dcrLastvstDtls.get(0).getVstDate().getDate());
                    tv_pdts_promoted_set.setText(" : "+pdtDtls);
                    tv_gifts_set.setText(" : "+giftDtls);
                    tv_feedbck_set.setText(" : "+dcrLastvstDtls.get(0).getFeedbk());
                    tv_rmks_set.setText(" : "+dcrLastvstDtls.get(0).getRemks());
                    dbh.open();
                    dbh.insert_precall_data(dcrLastvstDtls.get(0).getVstDate().getDate(),pdtDtls,giftDtls,dcrLastvstDtls.get(0).getFeedbk(),dcrLastvstDtls.get(0).getRemks(),selectedProductCode);
                    dbh.close();

                }
                catch (Exception e) {
                }
            }
            else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }
        @Override
        public void onFailure(Call<List<DCRLastVisitDetails>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_reselect:
                rl_dcr_precall_analysisMain.setVisibility(View.GONE);
                dcr_drselection_gridview.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
                    rlay_spin.setVisibility(View.VISIBLE);
                else
                rlay_spin.setVisibility(View.GONE);
                break;

            case R.id.btn_go:

                if(tv_drName.getText().toString().equalsIgnoreCase("DocName")) {
                    Log.v("DocName",tv_drName.toString());
                    Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.invalid_cus_sclt),Toast.LENGTH_LONG).show();
                    return ;
                }else {

                    commonUtilsMethods.CommonIntentwithNEwTask(DetailingCreationActivity.class);
                    mCommonSharedPreference.setValueToPreference("detail_", "dr");
                }
                break;

                case R.id.btn_skip:

                    if(tv_drName.getText().toString().equalsIgnoreCase("DocName")) {
                        Log.v("DocName",tv_drName.toString());
                        Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.invalid_cus_sclt),Toast.LENGTH_LONG).show();
                        return ;
                    }else {

                        Intent i = new Intent(getActivity(), FeedbackActivity.class);
                        i.putExtra("feedpage", "dr");
                        startActivity(i);
                    }
                break;
        }
    }
    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = getActivity().getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void setValueForFeed(String name,String code){
        mCommonSharedPreference.setValueToPreference("drName",name);
        mCommonSharedPreference.setValueToPreference("drCode",code);
        DetailingTrackerPOJO.setmDoctorCode(code);
        String sfMCode;
        CommonUtils.TAG_DOCTOR_CODE=code;
        CommonUtils.TAG_DOCTOR_NAME=name;
       /* if(SF_Type.equalsIgnoreCase("2")){
            sfMCode=subSfCode;
        }
        else{
            sfMCode=SF_Code;
        }*/
        CommonUtils.TAG_FEED_SF_CODE=subSfCode;

        try{
            dbh.open();
            Cursor mCursor = dbh.select_doctorDetails(code);
            while (mCursor.moveToNext()){
                tv_drqual.setText(mCursor.getString(14));
                tv_drdob.setText(mCursor.getString(3));
                tv_drdow.setText(mCursor.getString(4));
                tv_drmob.setText(mCursor.getString(16));
                tv_drcat.setText(mCursor.getString(9));
                tv_drspecl.setText(mCursor.getString(10));
                tv_drtown.setText(mCursor.getString(6));
                tv_dremail.setText(mCursor.getString(15));
                tv_draddr.setText(mCursor.getString(18));
                CommonUtils.TAG_DR_SPEC=mCursor.getString(8);
                CommonUtils.TAG_DR_PRODCT_CODE=mCursor.getString(21);
                DetailingTrackerPOJO.setmDoctorSpeciality(mCursor.getString(8));
                Log.v("doctor_spec_det",mCursor.getString(20)+" new_product "+mCursor.getString(21)+" drcode "+code);

            }

        }catch(Exception e){

        }finally {
            dbh.close();
        }

    }

    public void OnItemClick(String name, final String code){
        selectedProductCode=code;
        mCommonSharedPreference.setValueToPreference("drName",name);
        mCommonSharedPreference.setValueToPreference("drCode",code);
        tv_drName.setText(name);



        DetailingTrackerPOJO.setmDoctorCode(code);
        rl_drPrecallanalysis.setVisibility(View.VISIBLE);
        rl_chmPrecallanalysis.setVisibility(View.GONE);
        rl_dcr_precall_analysisMain.setVisibility(View.VISIBLE);
        dcr_drselection_gridview.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        String sfMCode;
        CommonUtils.TAG_DOCTOR_CODE=code;
        CommonUtils.TAG_DOCTOR_NAME=name;
       /* if(SF_Type.equalsIgnoreCase("2")){
            sfMCode=subSfCode;
        }
        else{
            sfMCode=SF_Code;
        }*/
        CommonUtils.TAG_FEED_SF_CODE=subSfCode;


            dbh.open();
            Cursor cur=dbh.select_precall_graph_list(code);
            int i=0;
            if(cur.getCount()!=0) {
                entries.clear();
                labels.clear();
                ArrayList xVals = new ArrayList();
                ArrayList yVals = new ArrayList();
                while (cur.moveToNext()) {
                    entries.add(new Entry(i, Float.parseFloat(cur.getString(1))));
                    //labels.add(details.get(i).getMon());
                    xVals.add(cur.getString(2));
                    yVals.add(cur.getString(3));
                    i++;
                }
                setData11(xVals, yVals, entries);

                Cursor c1=dbh.select_precall_data_list(code);
                if(c1.getCount()!=0){
                    while (c1.moveToNext()) {
                        tv_lastvstDt_set.setText(c1.getString(1));
                        tv_pdts_promoted_set.setText(c1.getString(2));
                        tv_gifts_set.setText(c1.getString(3));
                        tv_feedbck_set.setText(c1.getString(4));
                        tv_rmks_set.setText(c1.getString(5));
                    }
                }
            }
            else
                graphDataEmpty();

            dbh.close();

            HashMap<String, String> map = new HashMap<String, String>();
            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            map.clear();
            map.put("SF", SF_Code);
            map.put("typ", "D");
            map.put("CusCode", code);
            JSONObject json = new JSONObject();
            try {

                json.put("SF", subSfCode);
                json.put("typ", "D");
                json.put("CusCode", code);

            } catch (Exception e) {

            }
            Log.v("line_graph_codeee", json.toString());

            Call<List<MontlyVistDetail>> visitDetail = apiService.getPreCall(json.toString());
            visitDetail.enqueue(new Callback<List<MontlyVistDetail>>() {
                @Override
                public void onResponse(Call<List<MontlyVistDetail>> call, Response<List<MontlyVistDetail>> response) {
                    if (response.isSuccessful()) {
                        List<MontlyVistDetail> details = response.body();
                        if(details.size()!=0) {
                            Log.v("precallAnalysis", String.valueOf(details.size())+" value ");
                            entries.clear();
                            labels.clear();
                            ArrayList xVals = new ArrayList();
                            ArrayList yVals = new ArrayList();
                            dbh.open();
                            Cursor cur = dbh.select_precall_graph_list(code);
                            if (cur.getCount() != 0)
                                dbh.delete_precall(code);
                            for (int i = 0; i < details.size(); i++) {
                                Log.v("getEntry_value_for_ch", details.get(i).getCount());

                                entries.add(new Entry(i, Float.parseFloat(details.get(i).getCount())));
                                //labels.add(details.get(i).getMon());
                                xVals.add(details.get(i).getMon());
                                yVals.add(String.valueOf(i));

                                dbh.insert_precall_graph(details.get(i).getCount(), details.get(i).getMon(), String.valueOf(i), code);
                            }

                            dbh.close();
                            if (details.size() == 0) {
                                graphDataEmpty();
                            } else
                                setData11(xVals, yVals, entries);

                      /*  XAxis xl = lineChart.getXAxis();
                        xl.setTextColor(Color.GREEN);
                        xl.setDrawGridLines(false);
                        xl.setDrawLabels(true);
                        xl.setAvoidFirstLastClipping(true);
                        xl.setSpaceBetweenLabels(1);
                        xl.setEnabled(true);

       *//* YAxis leftAxis1 = lineChart.getAxisLeft();
        leftAxis1.setTextColor(Color.WHITE);
        leftAxis1.setSpaceBe
        leftAxis1.setDrawGridLines(true);*//*

                        LineDataSet dataset = new LineDataSet(entries, "of  Calls");
                        dataset.setDrawCubic(true);
                        dataset.setDrawValues(false);

                        *//* dataset.enableDashedLine(10f, 5f, 0f);*//*

                        lineChart.getLegend().setEnabled(false);
                        lineChart.setDescription("");
                        LineData data = new LineData(labels, dataset);
                        lineChart.setData(data);
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();*/
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<MontlyVistDetail>> call, Throwable t) {

                }
            });

            Call<List<DCRLastVisitDetails>> dcrlastcallDtls = apiService.dcrLastcallDtls(String.valueOf(json));
            dcrlastcallDtls.enqueue(DcrlastcallDtls);


       // Toast.makeText(getActivity(), ""+code, Toast.LENGTH_SHORT).show();

        try{
            dbh.open();
            Cursor mCursor = dbh.select_doctorDetails(code);
            while (mCursor.moveToNext()){
                tv_drqual.setText(mCursor.getString(14));
                tv_drdob.setText(mCursor.getString(3));
                tv_drdow.setText(mCursor.getString(4));
                tv_drmob.setText(mCursor.getString(16));
                tv_drcat.setText(mCursor.getString(9));
                tv_drspecl.setText(mCursor.getString(10));
                tv_drtown.setText(mCursor.getString(6));
                tv_dremail.setText(mCursor.getString(15));
                tv_draddr.setText(mCursor.getString(18));
                CommonUtils.TAG_DR_SPEC=mCursor.getString(8);
                CommonUtils.TAG_DR_PRODCT_CODE=mCursor.getString(21);
                DetailingTrackerPOJO.setmDoctorSpeciality(mCursor.getString(8));
                DetailingTrackerPOJO.setmDoctorTheraptic(mCursor.getString(8));
                Log.v("doctor_spec_det",mCursor.getString(20)+" new_product "+mCursor.getString(21)+" drcode "+code);

            }

        }catch(Exception e){

        }finally {
            dbh.close();
        }
    }

    public static void bindLocListener(LocationUpdate locationUpdates){
        locationUpdate=locationUpdates;
    }


    public boolean CurrentLoc(){

        mGPSTrack=new GPSTrack(getActivity());
        if(mGPSTrack.getLatitude()==0.0){
            CheckLocation();
            CurrentLoc();
        }
        return true;
    }

    public void CheckLocation(){
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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
        }catch (Exception e){
            Toast toast=Toast.makeText(getActivity(), getResources().getString(R.string.loction_detcted), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }




/*
    private void setData11(ArrayList xVals,ArrayList yVals,ArrayList<Entry> values) {
//https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/LineChartActivity1.java
        //ArrayList<Entry> values = new ArrayList<>();

       */
/* for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val));
        }
*//*

       */
/* ArrayList xVals = new ArrayList();
        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");

        ArrayList yVals = new ArrayList();
        yVals.add("0");
        yVals.add("1");
        yVals.add("2");
        yVals.add("3");
        yVals.add("4");
        yVals.add("5");*//*



        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(11);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setVisibleXRangeMaximum(6);//Y-axis
        lineChart.setVisibleXRangeMinimum(6);
        lineChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(0f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setCenterAxisLabels(false);
        leftAxis.setAxisMaximum(6);
        //leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setValueFormatter(new IndexAxisValueFormatter(yVals));
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);


        values.add(new Entry(0, 1));
        values.add(new Entry(1, 2));
        values.add(new Entry(2, 1));
        values.add(new Entry(3, 2));
        values.add(new Entry(4, 4));
        values.add(new Entry(5, 2));
        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            // set1.setValues(values);
            set1.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // draw dashed line
            // set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLUE);
            set1.setCircleColorHole(Color.WHITE);

            // line thickness and point size
            set1.setLineWidth(1f);


            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(true);



            // customize legend entry
            set1.setFormLineWidth(1f);
            //   set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            // set1.enableDashedHighlightLine(10f, 5f, 0f);


            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return lineChart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
               */
/* Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);*//*

                set1.setFillColor(Color.WHITE);
            } else {
                set1.setFillColor(Color.WHITE);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }
    }
*/

    private void setData11(ArrayList xVals,ArrayList yVals,ArrayList<Entry> values) {
//https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/LineChartActivity1.java
      /*  ArrayList<Entry> values = new ArrayList<>();

       *//* for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val));
        }
*//*
        ArrayList xVals = new ArrayList();
        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");
        xVals.add("Jul");
        xVals.add("Aug");
        xVals.add("Sep");
        xVals.add("Oct");

        ArrayList yVals = new ArrayList();
        yVals.add("0");
        yVals.add("1");
        yVals.add("2");
        yVals.add("3");
        yVals.add("4");
        yVals.add("5");
        yVals.add("6");
        yVals.add("7");
        yVals.add("8");
        yVals.add("9");*/



        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(12);
        xAxis.setTextColor(Color.GREEN);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setVisibleXRangeMaximum(6);//Y-axis
        lineChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(0f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setCenterAxisLabels(false);
        leftAxis.setAxisMaximum(6);
        //leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setValueFormatter(new IndexAxisValueFormatter(yVals));
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);


        /*values.add(new Entry(0, 1));
        values.add(new Entry(1, 2));
        values.add(new Entry(2, 1));
        values.add(new Entry(3, 2));
        values.add(new Entry(4, 4));
        values.add(new Entry(5, 0));
        values.add(new Entry(6, 0));
        values.add(new Entry(7, 0));
        values.add(new Entry(8, 0));
        values.add(new Entry(9, 0));*/
        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            // set1.setValues(values);
            set1.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // draw dashed line
            // set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLUE);
            set1.setCircleHoleColor(Color.WHITE);

            // line thickness and point size
            set1.setLineWidth(1f);

            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(true);
            set1.setCircleHoleColor(Color.TRANSPARENT);


            // customize legend entry
            set1.setFormLineWidth(1f);
            //   set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            // set1.enableDashedHighlightLine(10f, 5f, 0f);


            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return lineChart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
               /* Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);*/
                set1.setFillColor(Color.WHITE);
            } else {
                set1.setFillColor(Color.WHITE);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }
    }
    public void graphDataEmpty(){
        Log.v("graph_empty","here");
        entries.clear();
        labels.clear();
        ArrayList xVals = new ArrayList();
        ArrayList yVals = new ArrayList();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(0, 1));
        entries.add(new Entry(0, 2));
        entries.add(new Entry(0, 3));
        entries.add(new Entry(0, 4));
        entries.add(new Entry(0, 5));
        entries.add(new Entry(0, 6));
        entries.add(new Entry(0, 7));
        entries.add(new Entry(0, 8));
        entries.add(new Entry(0, 9));
        entries.add(new Entry(0, 10));
        entries.add(new Entry(0, 11));

                       /* labels.add("Jan");
                        labels.add("Feb");
                        labels.add("Mar");
                        labels.add("Apr");
                        labels.add("May");
                        labels.add("Jun");
                        labels.add("Jul");
                        labels.add("Aug");
                        labels.add("Sep");
                        labels.add("Oct");
                        labels.add("Nov");
                        labels.add("Dec");*/

        yVals.add(String.valueOf(0));
        yVals.add(String.valueOf(1));
        yVals.add(String.valueOf(2));
        yVals.add(String.valueOf(3));
        yVals.add(String.valueOf(4));
        yVals.add(String.valueOf(5));
        yVals.add(String.valueOf(6));
        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");
        xVals.add("Jul");
        xVals.add("Aug");
        xVals.add("Sep");
        xVals.add("Oct");
        xVals.add("Nov");
        xVals.add("Dec");

        setData11(xVals, yVals, entries);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commonFun();

    }
    public void commonFun(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void popupSpinner(){
        final Dialog dialog=new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_feedback);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        TextView    tv_todayplan_popup_head=(TextView)dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText(getResources().getString(R.string.selctlist));
        ImageView   iv_close_popup=(ImageView)dialog.findViewById(R.id.iv_close_popup);
        Button   ok=(Button)dialog.findViewById(R.id.ok);

        if (hospital_array.contains(new PopFeed(true))) {
            isEmpty=false;
        }
        else
            isEmpty=true;

        final AdapterForDynamicSelectionList adapt=new AdapterForDynamicSelectionList(getActivity(),hospital_array,0);
        popup_list.setAdapter(adapt);
        final SearchView search_view=(SearchView)dialog.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE));
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
                Log.v("search_view_str",s);
                adapt.getFilter().filter(s);
                return false;
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty) {
                    if (hospital_array.contains(new PopFeed(true))) {
                        for (int i = 0; i < hospital_array.size(); i++) {
                            PopFeed m = hospital_array.get(i);
                            m.setClick(false);
                        }
                    }
                }
                commonFun();
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                if (hospital_array.contains(new PopFeed(true))) {
                    for (int i = 0; i < hospital_array.size(); i++) {
                        PopFeed m = hospital_array.get(i);
                        if(m.isClick()){
                            spin_txt.setText(m.getTxt());
                            dbh.open();
                            String  sff;
                            if(SF_Type.equalsIgnoreCase("2"))
                                sff= SF_coding.get(spinnerChoosingSfCode);
                            else
                                sff= SF_Code;
                            mCommonSharedPreference.setValueToPreference("hos_code",m.getCode());
                            mCommonSharedPreference.setValueToPreference("hos_name",m.getTxt());
                            mCursor = dbh.select_doctors_bySfAndHos(sff,mMydayWtypeCd,m.getCode());
                            drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                            drList.clear();
                            Log.v("printing_doc_hos",mCursor.getCount()+"");
                            while (mCursor.moveToNext()) {

                                if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
                                    String yy=mCursor.getString(11);
                                    if (!TextUtils.isEmpty(mCursor.getString(11))){
                                        Log.v("Dr_detailing_Print",mCursor.getString(2));
                                        if (distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12))) < limitKm) {
                                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
                                            drList.add(_custom_DCR_GV_Dr_Contents);
                                            Log.v("Dr_detailing_figure_dk","lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(2));

                                        }
                                        else{
                                            Log.v("Dr_detailing_figure",distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12)))+"lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(1)+"nn"+mCursor.getString(2));
                                        }
                                    }
                                }
                                else {
                                    Log.v("Dr_detailing_Print12366",mCursor.getString(2));
                                    _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
                                    drList.add(_custom_DCR_GV_Dr_Contents);
                                }


                            }

                            GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                            _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList,"D");
                            gridView.setAdapter(_DCR_GV_Selection_adapter);
                            _DCR_GV_Selection_adapter.notifyDataSetChanged();
                            commonFun();
                            break;
                        }
                    }

                }
                else {
                    spin_txt.setText(getResources().getString(R.string.sclt_hospital));

                }
                dialog.dismiss();
            }
        });

    }

    public void getHospital(int x){
        Log.v("Name_hqlist", subSfCode+"");
        dbh.open();
        if(x==0)
        mCursor=dbh.select_hospitalist(subSfCode,mMydayWtypeCd);
        else
        mCursor=dbh.select_hospitalist(subSfCode);
        hospital_array.clear();
        if(mCursor.getCount()!=0) {
            if (mCursor.moveToFirst()) {
                do {
                    Log.v("Name_hqlist", mCursor.getString(2));
                    hospital_array.add(new PopFeed(mCursor.getString(3),false,mCursor.getString(4)));

                } while (mCursor.moveToNext());

            }
        }


    }
}
