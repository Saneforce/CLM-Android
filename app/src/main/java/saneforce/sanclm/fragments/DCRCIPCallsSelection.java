package saneforce.sanclm.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.DCRLastVisitDetails;
import saneforce.sanclm.Pojo_Class.MontlyVistDetail;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.DetailingCreationActivity;
import saneforce.sanclm.activities.DynamicActivity;
import saneforce.sanclm.activities.FeedbackActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.MapsActivity;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.activities.Model.SlideDetail;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DCRCIPCallsSelection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DCRCIPCallsSelection extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    ArrayList<Custom_DCR_GV_Dr_Contents> chmList ;
    String SF_Code,mMydayWtypeCd,subSfCode,db_slidedwnloadPath,SF_Type,db_connPath,giftDtls,pdtDtls;
    ImageButton btn_go,btn_Skip;
    List<String> categories = new ArrayList<String>();
    List<String> SF_coding = new ArrayList<String>();
    LineChart lineChart;
    EditText et_companyurl;
    Spinner spinner;
    ImageView fab_btn;
    TextView tv_lastvstDt_set,
            tv_pdts_promoted_set,tv_gifts_set,tv_feedbck_set,tv_rmks_set;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    ProgressDialog progressDialog;
    String selectedProductCode="";
    TextView txt_select_mr,txt_select_category,txt_select_class,txt_select_spec,txt_select_terr;
    String txt_qua,txt_cat,txt_class,txt_spec,txt_terr,div_codee="0";
    ArrayList<SlideDetail> list_dr=new ArrayList<>();
    ArrayList<Custom_DCR_GV_Dr_Contents> drList ;
    int spinnerpostion=0;
    String  addChm="1";
    RelativeLayout  rlay_spin;
    ArrayList<PopFeed>  hospital_array=new ArrayList<>();
    boolean isEmpty=false;
    TextView  spin_txt;
    String gpsNeed,geoFencing;
    double laty=0.0,lngy=0.0,limitKm=0.5;
    TextView txt_tool_header;

    public DCRCIPCallsSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DCRCIPCallsSelection.
     */
    // TODO: Rename and change types and number of parameters
    public static DCRCIPCallsSelection newInstance(String param1, String param2) {
        DCRCIPCallsSelection fragment = new DCRCIPCallsSelection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dcr_cip_calls_selection, container, false);

        dbh = new DataBaseHandler(getContext());
        mCommonSharedPreference = new CommonSharedPreference(getContext());
        commonUtilsMethods = new CommonUtilsMethods(getActivity());

        v = inflater.inflate(R.layout.fragment_dcr_cip_calls_selection, container, false);
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
        btn_go = (ImageButton)  v.findViewById(R.id.btn_go) ;
        txt_tool_header=v.findViewById(R.id.txt_tool_header);
        btn_Skip = (ImageButton)  v.findViewById(R.id.btn_skip) ;
        rlay_spin= (RelativeLayout) v.findViewById(R.id.rlay_spin) ;
        spin_txt = (TextView)  v.findViewById(R.id.spin_txt) ;
        btn_act = (Button)  v.findViewById(R.id.btn_act) ;
        if(mCommonSharedPreference.getValueFromPreference("addAct").equalsIgnoreCase("0"))
            btn_act.setVisibility(View.VISIBLE);
        Log.v("print_detailing",mCommonSharedPreference.getValueFromPreference("Detailing_chem"));
        if(mCommonSharedPreference.getValueFromPreference("Detailing_chem").equalsIgnoreCase("0"))
            btn_Skip.setVisibility(View.VISIBLE);
        else
            btn_Skip.setVisibility(View.INVISIBLE);
        if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
            rlay_spin.setVisibility(View.VISIBLE);
        if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("1")){
            rlay_spin.setVisibility(View.GONE);
        }
//        txt_tool_header.setText("Listed "+mCommonSharedPreference.getValueFromPreference("chmcap")+" selection");
//        Log.d("daataselec",mCommonSharedPreference.getValueFromPreference("chmcap"));

        tv_chmterritory = (TextView)  v.findViewById(R.id.tv_terri_set) ;
        tv_chmaddr = (TextView)  v.findViewById(R.id.tv_chmaddr_set) ;
        lineChart = (LineChart) v.findViewById(R.id.chart);
        fab_btn=(ImageView) v.findViewById(R.id.fab_btn);

        tv_pdt_promoted = (TextView)  v.findViewById(R.id.tv_pdtdetailed_set);
        et_companyurl=(EditText)v.findViewById(R.id.et_companyurl);
        //et_companyurl.setHint("Search Listed "+mCommonSharedPreference.getValueFromPreference("chmcap"));
        tv_pdt_promoted.setMovementMethod(new ScrollingMovementMethod());
        dcr_drselection_gridview.setVisibility(View.VISIBLE);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        SF_Code= mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        mMydayWtypeCd = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE);
        subSfCode=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        db_slidedwnloadPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        SF_Type =  mCommonSharedPreference.getValueFromPreference("sf_type");
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        addChm=mCommonSharedPreference.getValueFromPreference("addChm");
        geoFencing =  mCommonSharedPreference.getValueFromPreference("chmgeoneed");
        gpsNeed =  mCommonSharedPreference.getValueFromPreference("GpsFilter");
        Log.v("gpsNeed",gpsNeed);
        limitKm = Double.parseDouble(mCommonSharedPreference.getValueFromPreference("radius"));

        tv_lastvstDt_set =(TextView)  v.findViewById(R.id.tv_visit_date_set) ;
        tv_pdts_promoted_set =(TextView)  v.findViewById(R.id.tv_pdtdetailed_set) ;
        tv_gifts_set =(TextView)  v.findViewById(R.id.tv_gifts_set) ;
        tv_feedbck_set =(TextView)  v.findViewById(R.id.tv_feedback_set) ;
        tv_rmks_set=(TextView)  v.findViewById(R.id.tv_remark_set) ;

        //FullScreencall();


        CommonUtils.TAG_DR_SPEC="";

        if(SF_Type.equalsIgnoreCase("2")){
            spinner.setVisibility(View.VISIBLE);
        }
        else
            spinner.setVisibility(View.GONE);

        if(addChm.equalsIgnoreCase("0"))
            fab_btn.setVisibility(View.VISIBLE);


//        if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
//            getHospital(0);


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
        chmList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
        chmList.clear();
        if(TextUtils.isEmpty(subSfCode) || subSfCode.equalsIgnoreCase("null"))
            mCursor = dbh.select_Chemist_bySf(SF_Code,mMydayWtypeCd);
        else
            mCursor = dbh.select_Chemist_bySf(subSfCode,mMydayWtypeCd);


        Log.v("chemist_count", String.valueOf(mCursor.getCount())+" geo_fencing "+geoFencing);
        Log.v("geotag",mCommonSharedPreference.getValueFromPreference("geo_tag"));
//        while (mCursor.moveToNext()) {
//            if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
//                String yy=mCursor.getString(11);
//                if (!TextUtils.isEmpty(mCursor.getString(14))){
//                    Log.v("Dr_detailing_Print",mCursor.getString(2));
//                    if (distance(laty, lngy, Double.parseDouble(mCursor.getString(14)), Double.parseDouble(mCursor.getString(15))) < limitKm) {
//                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(5), mCursor.getString(4), mCursor.getString(11), mCursor.getString(12));
//
//                        Log.v("chemistDetails:",_custom_DCR_GV_Dr_Contents.toString());
//
//                        chmList.add(_custom_DCR_GV_Dr_Contents);
//                        Log.v("Dr_detailing_figure_dk","lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(2));
//
//                    }
//                    else{
//                        Log.v("Dr_detailing_figure",distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12)))+"lat_lng "+laty+" lngy "+lngy+"drnam "+mCursor.getString(1)+"nn"+mCursor.getString(2));
//                    }
//                }
//            }
//            else {
//                _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(5), mCursor.getString(4), mCursor.getString(11), mCursor.getString(12));
//                chmList.add(_custom_DCR_GV_Dr_Contents);
//            }
//        }

        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents("","","","","","","","");
        chmList.add(_custom_DCR_GV_Dr_Contents);
        Log.v("chmList_value",chmList.size()+"");
        GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),chmList,"C");
        gridView.setAdapter(_DCR_GV_Selection_adapter);
        _DCR_GV_Selection_adapter.notifyDataSetChanged();

//        categories.clear();
//        SF_coding.clear();
//        mCursor=dbh.select_hqlist_manager();
//        if(mCursor.getCount()!=0) {
//            if (mCursor.moveToFirst()) {
//                do {
//                    Log.v("Name_hqlist", mCursor.getString(2));
//                    if(subSfCode.equalsIgnoreCase(mCursor.getString(1))){
//                        if(SF_coding.size()!=0){
//                            categories.add(0,mCursor.getString(2));
//                            SF_coding.add(0,mCursor.getString(1));
//                        }
//                        else{
//                            categories.add(mCursor.getString(2));
//                            SF_coding.add(mCursor.getString(1));
//                        }
//                    }
//                    else {
//                        categories.add(mCursor.getString(2));
//                        SF_coding.add(mCursor.getString(1));
//                    }
//                } while (mCursor.moveToNext());
//
//            }
//        }
        fab_btn.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        btn_re_select_doctor.setOnClickListener(this);

        btn_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SharedPreferences shares=getActivity().getSharedPreferences("location",0);
                    String lat=shares.getString("lat","");
                    String lng=shares.getString("lng","");
                    Log.v("example_code",CommonUtils.TAG_DOCTOR_NAME);
                    JSONObject jj=new JSONObject();
                    jj.put("typ","2");
                    jj.put("cust",CommonUtils.TAG_CHEM_CODE);
                    jj.put("custname",CommonUtils.TAG_CHEM_NAME);
                    jj.put("lat",lat);
                    jj.put("lng",lng);
                    jj.put("DataSF",subSfCode);
                    jj.put("wt",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CODE));
                    jj.put("pl",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE));
                    mCommonSharedPreference.setValueToPreference("cust_act",jj.toString());
                    Intent ii=new Intent(getActivity(), DynamicActivity.class);
                    startActivity(ii);
                }catch (Exception   e){
                }
            }
        });

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
                    Intent LoginAc = new Intent(getActivity(), HomeDashBoard.class);
                    startActivity(LoginAc);

                }

                return false;
            }
        });

        if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") &&
                mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("false")){

        }
        else
            fab_btn.setVisibility(View.GONE);
        btn_go.setOnClickListener(this);
        DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
            @Override
            public void itemClick(String drname, String drcode) {

//                if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") &&
//                        mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("false")){
//                    OnItemClick(drname,drcode);
//                    rlay_spin.setVisibility(View.GONE);
//                }
//
//                else if(mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){
//                    setValueForFeed(drname,drcode);
//                }
//                else{
//
//                    if(NearTagActivity.list.contains(new ViewTagModel(drname))){
//                        for(int i=0;i<NearTagActivity.list.size();i++){
//                            if(NearTagActivity.list.get(i).getName().equalsIgnoreCase(drname)){
//
//                                Intent ii=new Intent(getActivity(), MapsActivity.class);
//                                ii.putExtra("drcode",drcode);
//                                ii.putExtra("drname",drname);
//                                ii.putExtra("cust","C");
//                                ii.putExtra("lats",NearTagActivity.list.get(i).getLat());
//                                ii.putExtra("lngs",NearTagActivity.list.get(i).getLng());
//                                i=NearTagActivity.list.size();
//                                startActivity(ii);
//
//                            }
//                        }
//                    }
//                    else{
//                        Intent i=new Intent(getActivity(), MapsActivity.class);
//                        i.putExtra("drcode",drcode);
//                        i.putExtra("drname",drname);
//                        i.putExtra("cust","C");
//                        i.putExtra("lats","0.0");
//                        i.putExtra("lngs","0.0");
//                        startActivity(i);
//                    }
//
//                }
               OnItemClick(drname,drcode);

            }
        });
        btn_Skip.setOnClickListener(this);

        rlay_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  popupSpinner();
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

        if(mCommonSharedPreference.getValueFromPreference("visit_dr").equalsIgnoreCase("true")){
            btn_re_select_doctor.setVisibility(View.INVISIBLE);
            btn_go.setVisibility(View.INVISIBLE);
            String cname=mCommonSharedPreference.getValueFromPreference("drnames");
            String ccode=mCommonSharedPreference.getValueFromPreference("drcodes");
          //  OnItemClick(cname,ccode);
            mCommonSharedPreference.setValueToPreference("visit_dr","false");
        }
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.textview_bg_spinner, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.textview_bg_spinner);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
//                CommonUtilsMethods.avoidSpinnerDropdownFocus(spinner);
//                spinnerpostion=i;
//                dbh.open();
//                mCursor = dbh.select_Chemist_bySf(SF_coding.get(i),mMydayWtypeCd);
//
//                if(chmList.size()==0 && mCursor.getCount()==0) {
//                    if(commonUtilsMethods.isOnline(getActivity())) {
//                        DownloadMasters dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_coding.get(i),SF_Code);
//                        //ll_anim.setVisibility(View.VISIBLE);
//                        if (progressDialog == null) {
//                            CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(getActivity());
//                            progressDialog = commonUtilsMethods.createProgressDialog(getActivity());
//                            progressDialog.show();
//                        } else {
//                            progressDialog.show();
//                        }
//                        dwnloadMasterData.chemsList();
//                    }
//                    else{
//                        Toast.makeText(getActivity(),"Network required to get detail",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    chmList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
//                    chmList.clear();
//                    while (mCursor.moveToNext()) {
//                        if (mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("0") && geoFencing.equalsIgnoreCase("1")) {
//                            String yy = mCursor.getString(11);
//                            if (!TextUtils.isEmpty(mCursor.getString(14))) {
//                                Log.v("Dr_detailing_Print", mCursor.getString(2));
//                                if (distance(laty, lngy, Double.parseDouble(mCursor.getString(14)), Double.parseDouble(mCursor.getString(15))) < limitKm) {
//                                    _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(5), mCursor.getString(4), mCursor.getString(11), mCursor.getString(12));
//                                    chmList.add(_custom_DCR_GV_Dr_Contents);
//                                    Log.v("Dr_detailing_figure_dk", "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(2));
//
//                                } else {
//                                    Log.v("Dr_detailing_figure", distance(laty, lngy, Double.parseDouble(mCursor.getString(11)), Double.parseDouble(mCursor.getString(12))) + "lat_lng " + laty + " lngy " + lngy + "drnam " + mCursor.getString(1) + "nn" + mCursor.getString(2));
//                                }
//                            }
//                        }
//                        else {
//                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(5), mCursor.getString(4), mCursor.getString(11), mCursor.getString(12));
//                            chmList.add(_custom_DCR_GV_Dr_Contents);
//                        }
//                    }
////                    while (mCursor.moveToNext()) {
////                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(5),mCursor.getString(4),mCursor.getString(11),mCursor.getString(12));
////                        chmList.add(_custom_DCR_GV_Dr_Contents);
////                    }
//
//                    GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
//                    _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),chmList,"C");
//                    gridView.setAdapter(_DCR_GV_Selection_adapter);
//                    _DCR_GV_Selection_adapter.notifyDataSetChanged();
//
//                }
//
//                DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
//                    @Override
//                    public void ListLoading() {
//                        dbh.open();
//                        chmList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
//                        chmList.clear();
//
//                        mCursor = dbh.select_Chemist_bySf(SF_coding.get(i),mMydayWtypeCd);
//
//                        Log.v("chemist_count", String.valueOf(mCursor.getCount()));
//                        while (mCursor.moveToNext()) {
//                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(5),mCursor.getString(4),mCursor.getString(11),mCursor.getString(12));
//                            chmList.add(_custom_DCR_GV_Dr_Contents);
//                        }
//
//                        GridView gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
//                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),chmList,"C");
//                        gridView.setAdapter(_DCR_GV_Selection_adapter);
//                       // getHospital(1);
//                        progressDialog.dismiss();
//                    }
//                });
//
//                //dwnloadMasterData.jointwrkCall();
//
//                // mCommonSharedPreference.setValueToPreference("sub_sf_code",SF_coding.get(i));
//           /*   DownloadMasters  dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_coding.get(i));
//                ll_anim.setVisibility(View.VISIBLE);
//                dwnloadMasterData.drList();
//                dwnloadMasterData.chemsList();
//                dwnloadMasterData.unDrList();
//                dwnloadMasterData.stckList();
//                dwnloadMasterData.terrList();
//                //dwnloadMasterData.jointwrkCall();
//
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                Fragment frag = new DCRDRCallsSelection();
//                ft.replace(R.id.app_config, frag);
//                ft.commit();
//                ll_anim.setVisibility(View.INVISIBLE);*/
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.isDrawTopYLabelEntryEnabled();
        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisMaxValue(6);


        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateY(2000);
        lineChart.animateX(2000);

        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_reselect:
                rl_dcr_precall_analysisMain.setVisibility(View.GONE);
                dcr_drselection_gridview.setVisibility(View.VISIBLE);
                if(mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
                    rlay_spin.setVisibility(View.VISIBLE);
                else
                    rlay_spin.setVisibility(View.GONE);
                break;

            case R.id.fab_btn:
                //popupAddDoctr();
                break;

            case R.id.btn_go:
                if(tv_drName.getText().toString().equalsIgnoreCase("DocName")) {
                    Log.v("DocName",tv_drName.toString());
                    Toast.makeText(getActivity().getApplicationContext(),"Invalid Customer Selection",Toast.LENGTH_LONG).show();
                    return ;
                }else if(mCommonSharedPreference.getValueFromPreference("Detailing_chem").equalsIgnoreCase("0")){
                    commonUtilsMethods.CommonIntentwithNEwTask(DetailingCreationActivity.class);
                    mCommonSharedPreference.setValueToPreference("detail_","chm");
                }
                else {
                    if(tv_drName.getText().toString().equalsIgnoreCase("DocName")) {
                        Log.v("DocName",tv_drName.toString());
                        Toast.makeText(getActivity().getApplicationContext(),"Invalid Customer Selection",Toast.LENGTH_LONG).show();
                        return ;
                    }else {
                        Intent i = new Intent(getActivity(), FeedbackActivity.class);
                        i.putExtra("feedpage", "chemist");
                        i.putExtra("customer", tv_drName.getText().toString());
                        startActivity(i);
                    }
                }
                break;

            case R.id.btn_skip:
                if(tv_drName.getText().toString().equalsIgnoreCase("DocName")) {
                    Log.v("DocName",tv_drName.toString());
                    Toast.makeText(getActivity().getApplicationContext(),"Invalid Customer Selection",Toast.LENGTH_LONG).show();
                    return ;
                }else {
                    Intent i = new Intent(getActivity(), FeedbackActivity.class);
                    i.putExtra("feedpage", "chemist");
                    i.putExtra("customer", tv_drName.getText().toString());
                    startActivity(i);
                }
                break;
        }
    }


    public void OnItemClick(String name, final String code){
//        selectedProductCode=code;
//        tv_drName.setText(name);
//        mCommonSharedPreference.setValueToPreference("drName",name);
//        mCommonSharedPreference.setValueToPreference("drCode",code);
//        Log.v("Chemist_details3333","are_called_here");
//        rl_drPrecallanalysis.setVisibility(View.GONE);
//        rl_chmPrecallanalysis.setVisibility(View.VISIBLE);
//        rl_dcr_precall_analysisMain.setVisibility(View.VISIBLE);
//        dcr_drselection_gridview.setVisibility(View.GONE);
//        String sfMCode;
//        CommonUtils.TAG_CHEM_CODE=code;
//        CommonUtils.TAG_CHEM_NAME=name;
//      /*  if(SF_Type.equalsIgnoreCase("2")){
//            sfMCode=subSfCode;
//        }
//        else{
//            sfMCode=SF_Code;
//        }*/
//        CommonUtils.TAG_FEED_SF_CODE=subSfCode;
//
//        dbh.open();
//        Cursor cur=dbh.select_precall_graph_list(code);
//        int i=0;
//        if(cur.getCount()!=0) {
//            entries.clear();
//            labels.clear();
//            ArrayList xVals = new ArrayList();
//            ArrayList yVals = new ArrayList();
//            while (cur.moveToNext()) {
//                entries.add(new Entry(i, Float.parseFloat(cur.getString(1))));
//                //labels.add(details.get(i).getMon());
//                xVals.add(cur.getString(2));
//                yVals.add(cur.getString(3));
//                i++;
//            }
//            setData11(xVals, yVals, entries);
//
//            Cursor c1=dbh.select_precall_data_list(code);
//            if(c1.getCount()!=0){
//                while (c1.moveToNext()) {
//                    tv_lastvstDt_set.setText(c1.getString(1));
//                    tv_pdts_promoted_set.setText(c1.getString(2));
//                    tv_gifts_set.setText(c1.getString(3));
//                    tv_feedbck_set.setText(c1.getString(4));
//                    tv_rmks_set.setText(c1.getString(5));
//                }
//            }
//        }
//        else
//          //  graphDataEmpty();
//
//        dbh.close();
//
//        HashMap<String, String> map = new HashMap<String, String>();
//        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
//        map.clear();
//        map.put("SF", SF_Code);
//        map.put("typ", "C");
//        map.put("CusCode", code);
//        JSONObject json=new JSONObject();
//        try{
//            json.put("SF", subSfCode);
//            json.put("typ", "C");
//            json.put("CusCode", code);
//
//        }catch (Exception e){
//
//        }
//
//        Call<List<MontlyVistDetail>> visitDetail = apiService.getPreCall(json.toString());
//        visitDetail.enqueue(new Callback<List<MontlyVistDetail>>() {
//            @Override
//            public void onResponse(Call<List<MontlyVistDetail>> call, Response<List<MontlyVistDetail>> response) {
//                if(response.isSuccessful()){
//                    List<MontlyVistDetail> details=response.body();
//                    Log.v("precallAnalysis", String.valueOf(details.size()));
//                    entries.clear();
//                    labels.clear();
//                    ArrayList xVals = new ArrayList();
//                    ArrayList yVals = new ArrayList();
//                    dbh.open();
//                    Cursor cur=dbh.select_precall_graph_list(code);
//                    if(cur.getCount()!=0)
//                        dbh.delete_precall(code);
//                    for(int i=0;i<details.size();i++){
//                        Log.v("getEntry_value_for_ch",details.get(i).getCount());
//
//                        entries.add(new Entry( i,Float.parseFloat(details.get(i).getCount())));
//                        //labels.add(details.get(i).getMon());
//                        xVals.add(details.get(i).getMon());
//                        yVals.add(String.valueOf(i));
//                        dbh.insert_precall_graph(details.get(i).getCount(),details.get(i).getMon(),String.valueOf(i),code);
//                    }
//                    dbh.close();
//
//
//                    if (details.size() == 0) {
//                        graphDataEmpty();
//                    }
//                    else
//                       setData11(xVals, yVals, entries);
//                      /*  XAxis xl = lineChart.getXAxis();
//                        xl.setTextColor(Color.GREEN);
//                        xl.setDrawGridLines(false);
//                        xl.setDrawLabels(true);
//                        xl.setAvoidFirstLastClipping(true);
//                        xl.setSpaceBetweenLabels(1);
//                        xl.setEnabled(true);
//
//       *//* YAxis leftAxis1 = lineChart.getAxisLeft();
//        leftAxis1.setTextColor(Color.WHITE);
//        leftAxis1.setSpaceBe
//        leftAxis1.setDrawGridLines(true);*//*
//
//                        LineDataSet dataset = new LineDataSet(entries, "of  Calls");
//                        dataset.setDrawCubic(true);
//                        dataset.setDrawValues(false);
//
//                        *//* dataset.enableDashedLine(10f, 5f, 0f);*//*
//
//                        lineChart.getLegend().setEnabled(false);
//                        lineChart.setDescription("");
//                        LineData data = new LineData(labels, dataset);
//                        lineChart.setData(data);
//                        lineChart.notifyDataSetChanged();
//                        lineChart.invalidate();*/
//
//                }
//            }
//            @Override
//            public void onFailure(Call<List<MontlyVistDetail>> call, Throwable t) {
//
//            }
//        });
//        Call<List<DCRLastVisitDetails>> dcrlastcallDtls = apiService.dcrLastcallDtls(String.valueOf(json));
//        dcrlastcallDtls.enqueue(DcrlastcallDtls);
//        try{
//            dbh.open();
//
//            Cursor mCursor = dbh.select_ChemistDetails(code);
//            while (mCursor.moveToNext()){
//
//                tv_chmcontactPerson.setText(mCursor.getString(10));
//                tv_chmPhone.setText(mCursor.getString(6));
//                tv_chmmob.setText(mCursor.getString(7));
//                tv_chmfax.setText(mCursor.getString(8));
//                tv_chmemail.setText(mCursor.getString(9));
//                tv_chmterritory.setText(mCursor.getString(5));
//                tv_chmaddr.setText(mCursor.getString(3));
//            }
//
//        }catch(Exception e){
//
//        }finally {
//            dbh.close();
//        }


        mCommonSharedPreference.setValueToPreference("drName",name);
        tv_drName.setText(name);
      //  DetailingTrackerPOJO.setmDoctorCode(code);
        rl_drPrecallanalysis.setVisibility(View.VISIBLE);
        rl_chmPrecallanalysis.setVisibility(View.GONE);
        rl_dcr_precall_analysisMain.setVisibility(View.VISIBLE);
        dcr_drselection_gridview.setVisibility(View.GONE);


    }
}