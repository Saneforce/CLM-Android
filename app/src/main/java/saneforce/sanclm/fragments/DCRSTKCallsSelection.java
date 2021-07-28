package saneforce.sanclm.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.activities.NearTagActivity;
import saneforce.sanclm.adapter_class.Custom_DCR_GV_Dr_Contents;
import saneforce.sanclm.adapter_class.DCR_GV_Selection_adapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DCRCallSelectionFilter;
import saneforce.sanclm.util.ManagerListLoading;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;


public class DCRSTKCallsSelection extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{
    DataBaseHandler dbh;
    View v;
    Cursor mCursor;
    CommonSharedPreference mCommonSharedPreference;
    ImageView iv_back;
    CommonUtilsMethods commonUtilsMethods;
    DCR_GV_Selection_adapter _DCR_GV_Selection_adapter=null;
    ArrayList<String> al = new ArrayList<String>();
    RelativeLayout rl_dcr_precall_analysisMain , dcr_drselection_gridview,rl_chmPrecallanalysis,rl_drPrecallanalysis;
    Button btn_re_select_doctor,btn_act;
    TextView tv_drName,tv_pdt_promoted,tv_chmcontactPerson,tv_chmPhone,tv_chmmob,tv_chmfax ,tv_chmemail,tv_chmterritory,tv_chmaddr;
    Custom_DCR_GV_Dr_Contents _custom_DCR_GV_Dr_Contents;
    ArrayList<Custom_DCR_GV_Dr_Contents> stckList ;
    String SF_Code,mMydayWtypeCd,SF_Type,db_slidedwnloadPath,subSfCode,db_connPath, pdtDtls ,giftDtls;
    Spinner spinner;
    List<String> categories = new ArrayList<String>();
    List<String> SF_coding = new ArrayList<String>();
    LineChart lineChart;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    TextView tv_lastvstDt_set,
            tv_pdts_promoted_set,tv_gifts_set,tv_feedbck_set,tv_rmks_set;
    ProgressDialog progressDialog;
    ImageButton btn_go,btn_Skip;
    String selectedProductCode="";
    TextView txt_tool_header;
    EditText et_companyurl;
    String language;
    Context context;
    Resources resources;
    String gpsNeed,geoFencing;
    double laty=0.0,lngy=0.0,limitKm=0.5;

    public static DCRSTKCallsSelection newInstance() {
        DCRSTKCallsSelection fragment = new DCRSTKCallsSelection();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.v("hello_called_start","here");
        if(!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("map_return_count"))){
            Log.v("hello_called_start","here");
            int xx=mCommonSharedPreference.getValueFromPreference("dr_pos",0);
            Custom_DCR_GV_Dr_Contents mm=stckList.get(xx);
            int yy= Integer.parseInt(mm.getTag())+1;
            mm.setTag(String.valueOf(yy));
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
    public void onResume() {
        super.onResume();
        FullScreencall();
        Log.v("hello_called","here");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbh = new DataBaseHandler(getContext());
        commonUtilsMethods = new CommonUtilsMethods(getActivity());
        mCommonSharedPreference = new CommonSharedPreference(getContext());

        v = inflater.inflate(R.layout.activity_dcrstkcalls_selection, container, false);

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
        rl_drPrecallanalysis = (RelativeLayout) v.findViewById(R.id.rl_dcr_drDetails);
        rl_chmPrecallanalysis = (RelativeLayout) v.findViewById(R.id.rl_dcr_chmDetails);
        btn_re_select_doctor = (Button)  v.findViewById(R.id.btn_reselect) ;
        tv_drName = (TextView)  v.findViewById(R.id.tv_dcrsel_drname) ;
        tv_chmcontactPerson = (TextView)  v.findViewById(R.id.tv_chmcontactperson_set) ;
        tv_chmPhone = (TextView)  v.findViewById(R.id.tv_chmPhone_set) ;
        tv_chmmob = (TextView)  v.findViewById(R.id.tv_chmmobile_set) ;
        tv_chmfax = (TextView)  v.findViewById(R.id.tv_chmfax_set) ;
        tv_chmemail = (TextView)  v.findViewById(R.id.tv_chmmail_set) ;
        tv_chmterritory = (TextView)  v.findViewById(R.id.tv_terri_set) ;
        tv_chmaddr = (TextView)  v.findViewById(R.id.tv_chmaddr_set) ;
        spinner = (Spinner) v.findViewById(R.id.spinner);
        tv_pdt_promoted = (TextView)  v.findViewById(R.id.tv_pdtdetailed_set);
        lineChart = (LineChart) v.findViewById(R.id.chart);
        tv_pdt_promoted.setMovementMethod(new ScrollingMovementMethod());
        dcr_drselection_gridview.setVisibility(View.VISIBLE);
        tv_lastvstDt_set =(TextView)  v.findViewById(R.id.tv_visit_date_set) ;
        tv_pdts_promoted_set =(TextView)  v.findViewById(R.id.tv_pdtdetailed_set) ;
        tv_gifts_set =(TextView)  v.findViewById(R.id.tv_gifts_set) ;
        tv_feedbck_set =(TextView)  v.findViewById(R.id.tv_feedback_set) ;
        tv_rmks_set=(TextView)  v.findViewById(R.id.tv_remark_set) ;
        btn_go = (ImageButton)  v.findViewById(R.id.btn_go) ;
        btn_Skip = (ImageButton)  v.findViewById(R.id.btn_skip) ;
        btn_act = (Button)  v.findViewById(R.id.btn_act) ;
        txt_tool_header=v.findViewById(R.id.txt_tool_header);
        et_companyurl=v.findViewById(R.id.et_companyurl);
        txt_tool_header.setText(resources.getString(R.string.listed)+" "+mCommonSharedPreference.getValueFromPreference("stkcap")+" "+resources.getString(R.string.Selection));
        Log.d("daataselec",mCommonSharedPreference.getValueFromPreference("stkcap"));

        et_companyurl.setHint(resources.getString(R.string.search)+" "+resources.getString(R.string.listed)+mCommonSharedPreference.getValueFromPreference("stkcap"));
        if(mCommonSharedPreference.getValueFromPreference("addAct").equalsIgnoreCase("0"))
            btn_act.setVisibility(View.VISIBLE);
        if(mCommonSharedPreference.getValueFromPreference("Detailing_stk").equalsIgnoreCase("0"))
            btn_Skip.setVisibility(View.VISIBLE);
        else
            btn_Skip.setVisibility(View.INVISIBLE);
        SF_Code= mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        mMydayWtypeCd = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE);
        subSfCode=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        db_slidedwnloadPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Type =  mCommonSharedPreference.getValueFromPreference("sf_type");
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        geoFencing =  mCommonSharedPreference.getValueFromPreference("stkgeoneed");
        gpsNeed =  mCommonSharedPreference.getValueFromPreference("GpsFilter");
        Log.v("gpsNeed",gpsNeed);
        limitKm = Double.parseDouble(mCommonSharedPreference.getValueFromPreference("radius"));



        FullScreencall();

        CommonUtils.TAG_DR_SPEC="";

        if(SF_Type.equalsIgnoreCase("2")){
            spinner.setVisibility(View.VISIBLE);
        }
        else
            spinner.setVisibility(View.GONE);
        if(gpsNeed.equals("0")) {
/* boolean locValue = CurrentLoc();
 laty = mGPSTrack.getLatitude();
 lngy = mGPSTrack.getLongitude();*/
            SharedPreferences shares=getActivity().getSharedPreferences("location",0);
            laty= Double.parseDouble(shares.getString("lat","0.0"));
            lngy= Double.parseDouble(shares.getString("lng","0.0"));
            Log.v("Dr_selection_key",laty+" lngy "+lngy);
        }

        dbh.open();
        stckList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
        stckList.clear();
        if(TextUtils.isEmpty(subSfCode) || subSfCode.equalsIgnoreCase("null"))
            mCursor = dbh.select_Stockist_bySf(SF_Code,mMydayWtypeCd);
        else
            mCursor = dbh.select_Stockist_bySf(subSfCode,mMydayWtypeCd);
        //mCursor = dbh.select_Stockist_bySf("MR4077","Trivandrum");
        Log.v("checking_sfcode_stck",SF_Code+"cluster"+mMydayWtypeCd);
        Log.v("stocklist_count", String.valueOf(mCursor.getCount()));
        while (mCursor.moveToNext()) {
//            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(4),mCursor.getString(3),mCursor.getString(14),mCursor.getString(15));
//            stckList.add(_custom_DCR_GV_Dr_Contents);

            if (mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
                if (mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")) {
                    _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));
                    stckList.add(_custom_DCR_GV_Dr_Contents);
                } else {
                    String yy = mCursor.getString(11);
                    if (!TextUtils.isEmpty(mCursor.getString(16))) {
                        Log.v("Dr_detailing_Print", mCursor.getString(2));
                        if (distance(laty, lngy, Double.parseDouble(mCursor.getString(16)), Double.parseDouble(mCursor.getString(17))) < limitKm) {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));

                            Log.v("chemistDetails:", _custom_DCR_GV_Dr_Contents.toString());

                            stckList.add(_custom_DCR_GV_Dr_Contents);
                            Log.v("Dr_detailing_figure_dk", "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(2));

                        } else {
                            Log.v("Dr_detailing_figure", distance(laty, lngy, Double.parseDouble(mCursor.getString(16)), Double.parseDouble(mCursor.getString(17))) + "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(1) + "nn" + mCursor.getString(2));
                        }
                    }
                }
            }
            else {
                _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));
                stckList.add(_custom_DCR_GV_Dr_Contents);
            }
        }

        GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),stckList,"S");
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
        gridView.setOnItemClickListener(this);
        btn_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SharedPreferences shares=getActivity().getSharedPreferences("location",0);
                    String lat=shares.getString("lat","");
                    String lng=shares.getString("lng","");
                    Log.v("example_code",CommonUtils.TAG_DOCTOR_NAME);
                    JSONObject  jj=new JSONObject();
                    jj.put("typ","3");
                    jj.put("cust",CommonUtils.TAG_STOCK_CODE);
                    jj.put("custname",CommonUtils.TAG_STOCK_NAME);
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

        btn_re_select_doctor.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        iv_back = (ImageView)   v.findViewById(R.id.iv_back);
        iv_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("1")){
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
                    Intent LoginAc = new Intent(getActivity(),HomeDashBoard.class);
                    startActivity(LoginAc);

                }

                return false;
            }
        });

        DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
            @Override
            public void itemClick(String drname, String drcode) {
               /* if(mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){

                }
                else
                OnItemClick(drname,drcode);*/

                if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") &&
                        mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("false")){
                    OnItemClick(drname,drcode);
                }

                else if(mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){
                    setValueForFeed(drname,drcode);
                }
                else{
                    if(NearTagActivity.list.contains(new ViewTagModel(drname))){
                        for(int i=0;i<NearTagActivity.list.size();i++){
                            if(NearTagActivity.list.get(i).getName().equalsIgnoreCase(drname)){

                                Intent ii=new Intent(getActivity(), MapsActivity.class);
                                ii.putExtra("drcode",drcode);
                                ii.putExtra("drname",drname);
                                ii.putExtra("cust","S");
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
                        i.putExtra("cust","S");
                        i.putExtra("lats","0.0");
                        i.putExtra("lngs","0.0");
                        startActivity(i);
                    }

                  /*  Intent i=new Intent(getActivity(), MapsActivity.class);
                    i.putExtra("drcode",drcode);
                    i.putExtra("drname",drname);
                    i.putExtra("cust","S");
                    startActivity(i);*/
                }
                //et_searchDr.setText("");
            }
        });

        et_companyurl.addTextChangedListener(new TextWatcher() {

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
                if (mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true"))
                mCommonSharedPreference.setValueToPreference("sub_sf_code",SF_coding.get(i));
                mCursor = dbh.select_Stockist_bySf(SF_coding.get(i),mMydayWtypeCd);

                if(stckList.size()==0 && mCursor.getCount()==0) {
                    if(commonUtilsMethods.isOnline(getActivity())) {
                        DownloadMasters dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_coding.get(i),SF_Code);
                        //ll_anim.setVisibility(View.VISIBLE);
                        if (progressDialog == null) {
                            CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(getActivity());
                            progressDialog = commonUtilsMethods.createProgressDialog(getActivity());
                            progressDialog.show();
                        } else {
                            progressDialog.show();
                        }
                        dwnloadMasterData.stckList();
                    }
                    else{
                        Toast.makeText(getActivity(),getResources().getString(R.string.network_req),Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    stckList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                    stckList.clear();
                    while (mCursor.moveToNext()) {
//                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(4),mCursor.getString(3),mCursor.getString(14),mCursor.getString(15));
//                        stckList.add(_custom_DCR_GV_Dr_Contents);
                        if (mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
                            if (mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")) {
                                _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));
                                stckList.add(_custom_DCR_GV_Dr_Contents);
                            } else {
                                String yy = mCursor.getString(11);
                                if (!TextUtils.isEmpty(mCursor.getString(16))) {
                                    Log.v("Dr_detailing_Print", mCursor.getString(2));
                                    if (distance(laty, lngy, Double.parseDouble(mCursor.getString(16)), Double.parseDouble(mCursor.getString(17))) < limitKm) {
                                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));

                                        Log.v("chemistDetails:", _custom_DCR_GV_Dr_Contents.toString());

                                        stckList.add(_custom_DCR_GV_Dr_Contents);
                                        Log.v("Dr_detailing_figure_dk", "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(2));

                                    } else {
                                       Log.v("Dr_detailing_figure", distance(laty, lngy, Double.parseDouble(mCursor.getString(14)), Double.parseDouble(mCursor.getString(15))) + "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(1) + "nn" + mCursor.getString(2));
                                    }
                                }
                            }
                        }
                        else {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));
                            stckList.add(_custom_DCR_GV_Dr_Contents);
                        }
                    }

                    GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                    _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),stckList,"S");
                    gridView.setAdapter(_DCR_GV_Selection_adapter);
                    _DCR_GV_Selection_adapter.notifyDataSetChanged();
                    dbh.close();
                }

                //dwnloadMasterData.jointwrkCall();
               DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                   @Override
                   public void ListLoading() {
                       dbh.open();
                       stckList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                       stckList.clear();

                       mCursor = dbh.select_Stockist_bySf(SF_coding.get(i),mMydayWtypeCd);
                       //mCursor = dbh.select_Stockist_bySf("MR4077","Trivandrum");
                       Log.v("checking_sfcode_stck",SF_Code+"cluster"+mMydayWtypeCd);
                       Log.v("stocklist_count", String.valueOf(mCursor.getCount()));
                       while (mCursor.moveToNext()) {
                           if (mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
                               if (mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")) {
                                   _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));
                                   stckList.add(_custom_DCR_GV_Dr_Contents);
                               } else {
                                   String yy = mCursor.getString(11);
                                   if (!TextUtils.isEmpty(mCursor.getString(16))) {
                                       Log.v("Dr_detailing_Print", mCursor.getString(2));
                                       if (distance(laty, lngy, Double.parseDouble(mCursor.getString(16)), Double.parseDouble(mCursor.getString(17))) < limitKm) {
                                           _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));

                                           Log.v("chemistDetails:", _custom_DCR_GV_Dr_Contents.toString());

                                           stckList.add(_custom_DCR_GV_Dr_Contents);
                                           Log.v("Dr_detailing_figure_dk", "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(2));

                                       } else {
                                           Log.v("Dr_detailing_figure", distance(laty, lngy, Double.parseDouble(mCursor.getString(16)), Double.parseDouble(mCursor.getString(17))) + "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(1) + "nn" + mCursor.getString(2));
                                       }
                                   }
                               }
                           }
                           else {
                               _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(4), mCursor.getString(3), mCursor.getString(14), mCursor.getString(15));
                               stckList.add(_custom_DCR_GV_Dr_Contents);
                           }
                       }

                       GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                       _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),stckList,"S");
                       gridView.setAdapter(_DCR_GV_Selection_adapter);
                       dbh.close();
                       progressDialog.dismiss();
                   }
               });
                DownloadMasters dwnloadMasterData1 = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_coding.get(i),SF_Code);
                dwnloadMasterData1.jointtList();
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

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.isDrawTopYLabelEntryEnabled();
        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisMaxValue(6);


        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateY(2000);
        lineChart.animateX(2000);
        if(mCommonSharedPreference.getValueFromPreference("visit_dr").equalsIgnoreCase("true")){
            btn_re_select_doctor.setVisibility(View.INVISIBLE);
            btn_go.setVisibility(View.INVISIBLE);
            String cname=mCommonSharedPreference.getValueFromPreference("drnames");
            String ccode=mCommonSharedPreference.getValueFromPreference("drcodes");
            OnItemClick(cname,ccode);
            mCommonSharedPreference.setValueToPreference("visit_dr","false");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Custom_DCR_GV_Dr_Contents custom_dcr_gv_dr_contents  =  stckList.get(position);
        tv_drName.setText(custom_dcr_gv_dr_contents.getmDoctorName());
        rl_drPrecallanalysis.setVisibility(View.GONE);
        rl_chmPrecallanalysis.setVisibility(View.VISIBLE);
        rl_dcr_precall_analysisMain.setVisibility(View.VISIBLE);
        dcr_drselection_gridview.setVisibility(View.GONE);



        try{
            dbh.open();

            Cursor mCursor = dbh.select_StockistDetails(custom_dcr_gv_dr_contents.getmDoctorcode());
            while (mCursor.moveToNext()){

                tv_chmcontactPerson.setText(mCursor.getString(12));
                tv_chmPhone.setText(mCursor.getString(6));
                tv_chmmob.setText(mCursor.getString(7));
                tv_chmfax.setText("");
                tv_chmemail.setText(mCursor.getString(8));
                tv_chmterritory.setText(mCursor.getString(3));
                tv_chmaddr.setText(mCursor.getString(5));
            }

        }catch(Exception e){

        }finally {
            dbh.close();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_reselect:
                rl_dcr_precall_analysisMain.setVisibility(View.GONE);
                dcr_drselection_gridview.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_go:
                if(tv_drName.getText().toString().equalsIgnoreCase("DocName")) {
                    Log.v("DocName",tv_drName.toString());
                    Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.invalid_cus_sclt),Toast.LENGTH_LONG).show();
                    return ;
                }else if(mCommonSharedPreference.getValueFromPreference("Detailing_stk").equalsIgnoreCase("0")){
                    commonUtilsMethods.CommonIntentwithNEwTask(DetailingCreationActivity.class);
                    mCommonSharedPreference.setValueToPreference("detail_","stk");
                }
                else {
                    if(tv_drName.getText().toString().equalsIgnoreCase("DocName")) {
                        Log.v("DocName",tv_drName.toString());
                        Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.invalid_cus_sclt),Toast.LENGTH_LONG).show();
                        return ;
                    }else {
                        Intent i = new Intent(getActivity(), FeedbackActivity.class);
                        i.putExtra("feedpage", "stock");
                        i.putExtra("customer", tv_drName.getText().toString());
                        startActivity(i);
                    }
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
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    public void setValueForFeed(String name,String code){
        mCommonSharedPreference.setValueToPreference("drName",name);
        mCommonSharedPreference.setValueToPreference("drCode",code);
        DetailingTrackerPOJO.setmDoctorCode(code);

        String sfMCode;
        CommonUtils.TAG_STOCK_CODE=code;
        CommonUtils.TAG_STOCK_NAME=name;
        /*if(SF_Type.equalsIgnoreCase("2")){
            sfMCode=subSfCode;
        }
        else{
            sfMCode=SF_Code;
        }*/
        CommonUtils.TAG_FEED_SF_CODE=subSfCode;

        try{
            dbh.open();

            Cursor mCursor = dbh.select_StockistDetails(code);
            while (mCursor.moveToNext()){
                Log.v("Printing_code22",mCursor.getString(12));
               /* tv_chmcontactPerson.setText(mCursor.getString(12));
                tv_chmPhone.setText(mCursor.getString(6));
                tv_chmmob.setText(mCursor.getString(7));
                tv_chmfax.setText("");
                tv_chmemail.setText(mCursor.getString(8));
                tv_chmterritory.setText(mCursor.getString(3));
                tv_chmaddr.setText(mCursor.getString(5));*/
            }

        }catch(Exception e){

        }finally {
            dbh.close();
        }

    }

    public void OnItemClick(String name, final String code){
        Log.v("Printing_code",code);
        selectedProductCode=code;
        mCommonSharedPreference.setValueToPreference("drName",name);
        mCommonSharedPreference.setValueToPreference("drCode",code);
        tv_drName.setText(name);
        DetailingTrackerPOJO.setmDoctorCode(code);
        rl_drPrecallanalysis.setVisibility(View.VISIBLE);
        rl_chmPrecallanalysis.setVisibility(View.GONE);
        rl_dcr_precall_analysisMain.setVisibility(View.VISIBLE);
        dcr_drselection_gridview.setVisibility(View.GONE);
        String sfMCode;
        CommonUtils.TAG_STOCK_CODE=code;
        CommonUtils.TAG_STOCK_NAME=name;
      /*  if(SF_Type.equalsIgnoreCase("2")){
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
        map.put("typ", "S");
        map.put("CusCode", code);
        JSONObject json=new JSONObject();
        try{
            json.put("SF", subSfCode);
            json.put("typ", "S");
            json.put("CusCode", code);

        }catch (Exception e){

        }

       /* Call<List<MontlyVistDetail>> visitDetail = apiService.getPreCall(json.toString());
        visitDetail.enqueue(new Callback<List<MontlyVistDetail>>() {
            @Override
            public void onResponse(Call<List<MontlyVistDetail>> call, Response<List<MontlyVistDetail>> response) {
                if(response.isSuccessful()){
                    List<MontlyVistDetail> details=response.body();
                    Log.v("precallAnalysis", String.valueOf(details.size()));
                    entries.clear();
                    labels.clear();
                    for(int i=0;i<details.size();i++){
                        entries.add(new Entry(Float.parseFloat(details.get(i).getCount()), i));
                        labels.add(details.get(i).getMon());
                    }

                    if(entries.size()!=0) {
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

                        LineDataSet dataset = new LineDataSet(entries, "of  Calls");
                        dataset.setDrawCubic(true);
                        dataset.setDrawValues(false);

                        *//* dataset.enableDashedLine(10f, 5f, 0f);*//*

                        lineChart.getLegend().setEnabled(false);
                        lineChart.setDescription("");
                        LineData data = new LineData(labels, dataset);
                        lineChart.setData(data);
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();

                    }
                }
            }
            @Override
            public void onFailure(Call<List<MontlyVistDetail>> call, Throwable t) {

            }
        });*/

        Call<List<MontlyVistDetail>> visitDetail = apiService.getPreCall(json.toString());
        visitDetail.enqueue(new Callback<List<MontlyVistDetail>>() {
            @Override
            public void onResponse(Call<List<MontlyVistDetail>> call, Response<List<MontlyVistDetail>> response) {
                if(response.isSuccessful()){
                    List<MontlyVistDetail> details=response.body();
                    Log.v("precallAnalysis", String.valueOf(details.size()));
                    entries.clear();
                    labels.clear();
                    ArrayList xVals = new ArrayList();
                    ArrayList yVals = new ArrayList();
                    dbh.open();
                    Cursor cur=dbh.select_precall_graph_list(code);
                    if(cur.getCount()!=0)
                        dbh.delete_precall(code);
                    for(int i=0;i<details.size();i++){
                        Log.v("getEntry_value_for_ch",details.get(i).getCount());

                        entries.add(new Entry( i,Float.parseFloat(details.get(i).getCount())));
                        //labels.add(details.get(i).getMon());
                        xVals.add(details.get(i).getMon());
                        yVals.add(String.valueOf(i));
                        dbh.insert_precall_graph(details.get(i).getCount(),details.get(i).getMon(),String.valueOf(i),code);
                    }
                    dbh.close();



                    if (details.size() == 0) {
                        graphDataEmpty();
                    }
                    else
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
            @Override
            public void onFailure(Call<List<MontlyVistDetail>> call, Throwable t) {

            }
        });

        Call<List<DCRLastVisitDetails>> dcrlastcallDtls = apiService.dcrLastcallDtls(String.valueOf(json));
        dcrlastcallDtls.enqueue(DcrlastcallDtls);

        // Toast.makeText(getActivity(), ""+code, Toast.LENGTH_SHORT).show();

        try{
            dbh.open();

            Cursor mCursor = dbh.select_StockistDetails(code);
            while (mCursor.moveToNext()){
                Log.v("Printing_code22",mCursor.getString(12));
                tv_chmcontactPerson.setText(mCursor.getString(12));
                tv_chmPhone.setText(mCursor.getString(6));
                tv_chmmob.setText(mCursor.getString(7));
                tv_chmfax.setText("");
                tv_chmemail.setText(mCursor.getString(8));
                tv_chmterritory.setText(mCursor.getString(3));
                tv_chmaddr.setText(mCursor.getString(5));
            }

        }catch(Exception e){

        }finally {
            dbh.close();
        }
    }
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

                    tv_lastvstDt_set.setText(dcrLastvstDtls.get(0).getVstDate().getDate());
                    tv_pdts_promoted_set.setText(pdtDtls);
                    tv_gifts_set.setText(giftDtls);
                    tv_feedbck_set.setText(dcrLastvstDtls.get(0).getFeedbk());
                    tv_rmks_set.setText(dcrLastvstDtls.get(0).getRemks());
                    dbh.open();
                    dbh.insert_precall_data(dcrLastvstDtls.get(0).getVstDate().getDate(),pdtDtls,giftDtls,dcrLastvstDtls.get(0).getFeedbk(),dcrLastvstDtls.get(0).getRemks(),selectedProductCode);
                    dbh.close();


                }catch (Exception e) {
                }
            } else {
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
    private void setData11(ArrayList xVals,ArrayList yVals,ArrayList<Entry> values) {
//https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/LineChartActivity1.java
        //ArrayList<Entry> values = new ArrayList<>();

       /* for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val));
        }
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
        yVals.add("5");*/


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
//Y-axis
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
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setCircleHoleColor(Color.TRANSPARENT);

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

}

