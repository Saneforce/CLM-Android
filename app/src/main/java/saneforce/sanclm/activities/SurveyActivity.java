package saneforce.sanclm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.Pojo_Class.SurveyQSlist;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDynamicList;
import saneforce.sanclm.adapter_class.AdapterDynamicActivity;
import saneforce.sanclm.adapter_class.AdapterSurveyQuestionview;
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
import saneforce.sanclm.util.SpecialityListener;
import saneforce.sanclm.util.SpecialityListener1;

public class SurveyActivity extends AppCompatActivity {

    Spinner spinner,spinnercustomer;
    List<String> categories = new ArrayList<String>();
    List<String> SF_coding = new ArrayList<String>();
    Cursor mCursor;
    CommonSharedPreference mCommonSharedPreference;
    DataBaseHandler dbh;
    String subSfCode,SF_Code,division_code,db_connPath, mMydayWtypeCd,db_slidedwnloadPath;
    CommonUtilsMethods commonUtilsMethods;
    ImageView iv_back;
    DCR_GV_Selection_adapter _DCR_GV_Selection_adapter=null;
    Custom_DCR_GV_Dr_Contents _custom_DCR_GV_Dr_Contents;
    ArrayList<Custom_DCR_GV_Dr_Contents> drList=new ArrayList<>() ;
    ArrayList<Custom_DCR_GV_Dr_Contents> chemlist=new ArrayList<>() ;
    RelativeLayout  dcr_drselection_gridview;
    Api_Interface api_interface;
    LinearLayout rl_dcr_presurvey_analysisMain ;
    ProgressDialog progressDialog = null,progress;
    AppCompatEditText et_searchDr;
    TextView et_selectcustomer,cancel_txt,choosensurveytxt;
    String selectedItem="",typee,SF_Type;
    ArrayList<ModelDynamicList> array = new ArrayList<>();
    AdapterDynamicActivity adp;
    ListView dynamiclistview, list_view;
    String sur_code="";
    ArrayList<SurveyQSlist>surveyQSlists=new ArrayList<>();
    String Drcat="",Drspec="",Drcls="",Chemcat="";
    GridView gridView;
    Button save_btn;
    String custcode="";
    int spinnerposition=0;
    DetailingTrackerPOJO detailingTrackerPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        dbh = new DataBaseHandler(SurveyActivity.this);
        mCommonSharedPreference = new CommonSharedPreference(SurveyActivity.this);
        commonUtilsMethods = new CommonUtilsMethods(SurveyActivity.this);
        SF_Code= mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        subSfCode=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        division_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        mMydayWtypeCd = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE);
        db_slidedwnloadPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Type =  mCommonSharedPreference.getValueFromPreference("sf_type");
        Log.v("sftype",SF_Type);
        progressDialog = commonUtilsMethods.createProgressDialog(SurveyActivity.this);
        progressDialog.show();

        progress = new ProgressDialog(this);
        progress.setMessage("loading..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);


        spinner = (Spinner) findViewById(R.id.spinner);
        spinnercustomer=findViewById(R.id.spinnercustomer);
        iv_back=findViewById(R.id.iv_back);
        rl_dcr_presurvey_analysisMain = (LinearLayout) findViewById(R.id.rl_dcr_precall_analysis_main) ;
        dcr_drselection_gridview= (RelativeLayout) findViewById(R.id.rl_dcr_drgrid_selection) ;
        et_searchDr = (AppCompatEditText) findViewById(R.id.et_searchDr);
        et_selectcustomer=(TextView)findViewById(R.id.et_selectcustomer);
        et_searchDr.setText("");
        api_interface = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        gridView = (GridView) findViewById(R.id.gridview_dcrselect);
        list_view = (ListView) findViewById(R.id.list_view);
        cancel_txt=(TextView)findViewById(R.id.cancel_txt);
        choosensurveytxt=findViewById(R.id.txt_choosen_survey);
        dynamiclistview=findViewById(R.id.list);
        save_btn=findViewById(R.id.btn_save);
        detailingTrackerPOJO=new DetailingTrackerPOJO();


        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_dcr_presurvey_analysisMain.setVisibility(View.VISIBLE);
                dcr_drselection_gridview.setVisibility(View.GONE);
            }
        });

//        et_selectcustomer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(selectedItem.equalsIgnoreCase(getResources().getString(R.string.selct))){
//                    Toast.makeText(SurveyActivity.this, getResources().getString(R.string.slt_cust_typ), Toast.LENGTH_SHORT).show();
//                }
//                else if(selectedItem.equalsIgnoreCase(getResources().getString(R.string.listed_dr))) {
//                    surveyQSlists.clear();
//                    save_btn.setVisibility(View.GONE);
//                    getDoctorlist();
//                }else if(selectedItem.equalsIgnoreCase(getResources().getString(R.string.chem))) {
//                    surveyQSlists.clear();
//                    save_btn.setVisibility(View.GONE);
//                    getChemistList();
//
//                }
//            }
//        });

        et_selectcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem.equalsIgnoreCase("Select")) {
                    Toast.makeText(SurveyActivity.this, "Select Customer type", Toast.LENGTH_SHORT).show();
                } else if (selectedItem.equalsIgnoreCase(getResources().getString(R.string.listed_dr))) {
                    surveyQSlists.clear();
                    save_btn.setVisibility(View.GONE);
                    chemlist.clear();
                    drList.clear();
                    mCursor = dbh.select_doctors_bySf(SF_coding.get(spinnerposition), mMydayWtypeCd);
                    if (drList.size() == 0 && mCursor.getCount() == 0) {
                        if (commonUtilsMethods.isOnline(SurveyActivity.this)) {
                            DownloadMasters dwnloadMasterData = new DownloadMasters(SurveyActivity.this, db_connPath, db_slidedwnloadPath, SF_coding.get(spinnerposition), SF_Code);
                            if (progressDialog == null) {
                                CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(SurveyActivity.this);
                                progressDialog = commonUtilsMethods.createProgressDialog(SurveyActivity.this);
                                progressDialog.show();
                                //progress.show();
                            } else {
                                progressDialog.show();
                                //progress.show();
                            }
//ll_anim.setVisibility(View.VISIBLE);
                            subSfCode = SF_coding.get(spinnerposition);
                            dwnloadMasterData.drList();

                        } else {
                            Toast.makeText(SurveyActivity.this, getResources().getString(R.string.network_req), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //detailingTrackerPOJO.setTabSelection("0");
                        getDoctorlist();
                    }

                    DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                        @Override
                        public void ListLoading() {
                            //detailingTrackerPOJO.setTabSelection("0");
                            getDoctorlist();

                        }
                    });


                }else if (selectedItem.equalsIgnoreCase(getResources().getString(R.string.chem))) {
                    surveyQSlists.clear();
                    save_btn.setVisibility(View.GONE);
                    mCursor = dbh.select_Chemist_bySf(SF_coding.get(spinnerposition), mMydayWtypeCd);

                    if (chemlist.size() == 0 && mCursor.getCount() == 0) {
                        if (commonUtilsMethods.isOnline(SurveyActivity.this)) {
                            DownloadMasters dwnloadMasterData = new DownloadMasters(SurveyActivity.this, db_connPath, db_slidedwnloadPath, SF_coding.get(spinnerposition), SF_Code);
                            //ll_anim.setVisibility(View.VISIBLE);
                            if (progressDialog == null) {
                                CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(SurveyActivity.this);
                                progressDialog = commonUtilsMethods.createProgressDialog(SurveyActivity.this);
                                progressDialog.show();
                                //progress.show();
                            } else {
                                progressDialog.show();
                                //progress.show();
                            }
                            dwnloadMasterData.chemsList();
                        } else {
                            Toast.makeText(SurveyActivity.this, getResources().getString(R.string.network_req), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                       // detailingTrackerPOJO.setTabSelection("1");
                        getChemistList();
                    }

                    DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                        @Override
                        public void ListLoading() {
                           // detailingTrackerPOJO.setTabSelection("1");
                            getChemistList();
                        }


                    });
                    Log.v("call>>", SF_coding.get(spinnerposition));

                }

            }

        });

        if(SF_Type.equalsIgnoreCase("2")){
            spinner.setVisibility(View.VISIBLE);
        }
        else
            spinner.setVisibility(View.GONE);


        List<String> selectPosition = new ArrayList<>();
        selectPosition.add(getResources().getString(R.string.selct));
        selectPosition.add(/*"Listed Dr."*/ getResources().getString(R.string.listed_dr));
        selectPosition.add(/*"Chemist"*/ getResources().getString(R.string.chem));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, selectPosition);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercustomer.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        categories.clear();
        SF_coding.clear();
        dbh.open();
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SurveyActivity.this,R.layout.textview_bg_spinner, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.textview_bg_spinner);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CommonUtilsMethods.avoidSpinnerDropdownFocus(spinner);
                spinnerposition=position;
                mCommonSharedPreference.setValueToPreference("sub_sf_code", SF_coding.get(spinnerposition));
                mCommonSharedPreference.setValueToPreference("hq_code", SF_coding.get(spinnerposition));
                subSfCode = SF_coding.get(spinnerposition);
                dbh.open();
                drList.clear();
                chemlist.clear();


                if (selectedItem.equalsIgnoreCase(getResources().getString(R.string.listed_dr)) || selectedItem.equalsIgnoreCase(getResources().getString(R.string.chem))) {
                    if (typee.equalsIgnoreCase("D")) {
                        mCursor = dbh.select_doctors_bySf(SF_coding.get(spinnerposition),mMydayWtypeCd);
                        if(drList.size()==0 && mCursor.getCount()==0) {
                            if(commonUtilsMethods.isOnline(SurveyActivity.this)) {
                                DownloadMasters dwnloadMasterData = new DownloadMasters(SurveyActivity.this, db_connPath, db_slidedwnloadPath, SF_coding.get(spinnerposition),SF_Code);
                                if (progressDialog== null) {
                                    CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(SurveyActivity.this);
                                    progressDialog = commonUtilsMethods.createProgressDialog(SurveyActivity.this);
                                    progressDialog.show();
                                   // progress.show();
                                } else {
                                    progressDialog.show();
                                   // progress.show();
                                }
                                subSfCode = SF_coding.get(spinnerposition);
                                dwnloadMasterData.drList();

                            }else{
                                Toast.makeText(SurveyActivity.this,getResources().getString(R.string.network_req),Toast.LENGTH_SHORT).show();
                            }
                        }else {
                           // detailingTrackerPOJO.setTabSelection("0");
                            getDoctorlist();
                        }

                        DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                            @Override
                            public void ListLoading() {
                                //detailingTrackerPOJO.setTabSelection("0");
                                getDoctorlist();

                            }
                        });
                    } else if (typee.equalsIgnoreCase("C")) {
                        mCursor = dbh.select_Chemist_bySf(SF_coding.get(spinnerposition),mMydayWtypeCd);

                        if(chemlist.size()==0 && mCursor.getCount()==0) {
                            if(commonUtilsMethods.isOnline(SurveyActivity.this)) {
                                DownloadMasters dwnloadMasterData = new DownloadMasters(SurveyActivity.this, db_connPath, db_slidedwnloadPath, SF_coding.get(spinnerposition),SF_Code);
                                //ll_anim.setVisibility(View.VISIBLE);
                                if (progressDialog== null) {
                                    CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(SurveyActivity.this);
                                    progressDialog = commonUtilsMethods.createProgressDialog(SurveyActivity.this);
                                    progressDialog.show();
                                    //progress.show();
                                } else {
                                    progressDialog.show();
                                    //progress.show();
                                }
                                dwnloadMasterData.chemsList();
                            }
                            else{
                                Toast.makeText(SurveyActivity.this,getResources().getString(R.string.network_req),Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            //detailingTrackerPOJO.setTabSelection("1");
                            getChemistList();
                        }

                        DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                            @Override
                            public void ListLoading() {
                                //detailingTrackerPOJO.setTabSelection("1");
                                getChemistList();
                            }


                        });
                    }
                }


                // mCursor = dbh.select_doctors_bySf(subSfCode, mMydayWtypeCd);
//
//                if (selectedItem.equalsIgnoreCase(getResources().getString(R.string.listed_dr)) || selectedItem.equalsIgnoreCase(getResources().getString(R.string.chem))) {
//                    if (typee.equalsIgnoreCase("D")) {
//                        mCursor = dbh.select_doctors_bySf(subSfCode, mMydayWtypeCd);
//                        if (drList.size() == 0 && mCursor.getCount() == 0 ) {
//                    if (commonUtilsMethods.isOnline(SurveyActivity.this)) {
//                        DownloadMasters dwnloadMasterData = new DownloadMasters(SurveyActivity.this, db_connPath, db_slidedwnloadPath, subSfCode, SF_Code);
//                        if (progressDialog == null) {
//                            progressDialog.show();
//                        } else {
//                            progressDialog.show();
//                        }
//                        dwnloadMasterData.drList();
//
//                    } else {
//                        Toast.makeText(SurveyActivity.this, getResources().getString(R.string.network_req), Toast.LENGTH_SHORT).show();
//                    }
//                }  if (selectedItem.equalsIgnoreCase(getResources().getString(R.string.listed_dr)) || selectedItem.equalsIgnoreCase(getResources().getString(R.string.chem))) {
//                            if (typee.equalsIgnoreCase("D")) {
//                                getDoctorlist();
//                            } else if (typee.equalsIgnoreCase("C")) {
//                                getChemistList();
//                                Log.v("call>>", SF_coding.get(position));
//
//                            }
//                        }
//                    } else if (typee.equalsIgnoreCase("C")) {
//                        mCursor = dbh.select_Chemist_bySf(subSfCode, mMydayWtypeCd);
//                        if (chemlist.size() == 0 && mCursor.getCount() == 0) {
//                    if (commonUtilsMethods.isOnline(SurveyActivity.this)) {
//                        DownloadMasters dwnloadMasterData = new DownloadMasters(SurveyActivity.this, db_connPath, db_slidedwnloadPath, subSfCode, SF_Code);
//                        if (progressDialog == null) {
//                            progressDialog.show();
//                        } else {
//                            progressDialog.show();
//                        }
//
//                        dwnloadMasterData.chemsList();
//                    } else {
//                        Toast.makeText(SurveyActivity.this, getResources().getString(R.string.network_req), Toast.LENGTH_SHORT).show();
//                    }
//                }else
//              {
//                        if (selectedItem.equalsIgnoreCase(getResources().getString(R.string.listed_dr)) || selectedItem.equalsIgnoreCase(getResources().getString(R.string.chem))) {
//                            if (typee.equalsIgnoreCase("D")) {
//                                getDoctorlist();
//                            } else if (typee.equalsIgnoreCase("C")) {
//                                getChemistList();
//                                Log.v("call>>", SF_coding.get(position));
//
//                            }
//                        }
//              }
//                        }
//                    }


//                DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
//                    @Override
//                    public void ListLoading() {
//                        if (selectedItem.equalsIgnoreCase(getResources().getString(R.string.listed_dr)) || selectedItem.equalsIgnoreCase(getResources().getString(R.string.chem))) {
//                            if (typee.equalsIgnoreCase("D")) {
//                                getDoctorlist();
//                            } else if (typee.equalsIgnoreCase("C")) {
//                                getChemistList();
//                                Log.v("call>>", SF_coding.get(position));
//
//                            }
//                        }
//                    }
//                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SurveyActivity.this,HomeDashBoard.class);
                startActivity(intent);
            }
        });
        spinnercustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                typee = "";
                surveyQSlists.clear();
                save_btn.setVisibility(View.GONE);
                dynamiclistview.setVisibility(View.GONE);
                if(!selectedItem.equalsIgnoreCase(getResources().getString(R.string.selct))&&sur_code.equalsIgnoreCase("")||sur_code.equalsIgnoreCase("null")){
                    Toast.makeText(SurveyActivity.this, getResources().getString(R.string.slt_survey), Toast.LENGTH_SHORT).show();
                    spinnercustomer.setSelection(0);
                }else{

                    switch (position) {

                        case 0:
                            Drcat="";
                            Drspec="";
                            Chemcat="";
                            break;

                        case 1:
                            typee="D";
                            et_selectcustomer.setText(getResources().getString(R.string.slct_lst_dr));
                            try {
                                JSONObject jsonObject = new JSONObject(mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));
                                Log.v("qn>>",mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));

                                JSONArray jsonArray=jsonObject.getJSONArray("survey_for");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    if(jsonObject1.getString("Stype").contains("D")){
                                        Drcat=jsonObject1.getString("DrCat");
                                        Drspec=jsonObject1.getString("DrSpl");
                                        Drcls=jsonObject1.getString("DrCls");
                                        Log.v("qn>>",jsonObject1.getString("Qanswer"));

                                    }
                                    Log.v("qn>>1", String.valueOf(surveyQSlists.size()));

                                }

                            }catch(Exception e){

                            }

                            break;
                        case 2:
                            typee="C";
                            et_selectcustomer.setText(getResources().getString(R.string.slct_chem));
                            try {
                                JSONObject jsonObject = new JSONObject(mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));
                                Log.v("qn>>",mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));

                                JSONArray jsonArray=jsonObject.getJSONArray("survey_for");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    if(jsonObject1.getString("Stype").contains("C")){
                                        Chemcat= jsonObject1.getString("ChmCat");

                                        Log.v("qn>>",jsonObject1.getString("Qanswer"));

                                    }
                                    Log.v("qn>>1", String.valueOf(surveyQSlists.size()));

                                }

                            }catch(Exception e){

                            }
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                if(!cs.equals(null)) {
                    _DCR_GV_Selection_adapter.getFilter().filter(cs);
                    _DCR_GV_Selection_adapter.notifyDataSetChanged();
                }
            }
        });

        getSurveyJSON();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonUtilsMethods.isOnline(SurveyActivity.this)) {
                    if (validationOfField())
                        saveEntry();
                    else
                        Toast.makeText(SurveyActivity.this, getResources().getString(R.string.fillmand), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(SurveyActivity.this, getResources().getString(R.string.nonetwrk), Toast.LENGTH_SHORT).show();




            }
        });

    }

    private void saveEntry() {
        if(surveyQSlists.size()>0) {
            JSONArray ja = new JSONArray();
            JSONObject js = null;
            JSONObject finaljs = new JSONObject();
            try {
                finaljs.put("SF",SF_Code);
                finaljs.put("Div",division_code);
                for(int i=0;i<surveyQSlists.size();i++){
                    js=new JSONObject();
                    js.put("CustType",typee);
                    js.put("CustCode",custcode);
                    js.put("SurveyDate",CommonUtilsMethods.getCurrentDataTime());
                    js.put("Survey_Id",sur_code);
                    js.put("Question_Id",surveyQSlists.get(i).getId());
                    if(surveyQSlists.get(i).getQc_id().equals("4")){
                        String answers="";

                            String  cvc = surveyQSlists.get(i).getAnswer();
                            String [] answer  = cvc.split(",");

                        for (String s : answer) {
                            String[] answer1 = s.split("~");
                            Log.v("cvcv11", s);
                            if (surveyQSlists.get(i).getId().equals(answer1[1])) {
                                answers += answer1[0] + ",";
                                Log.v("cvcv", answer1[0]);
                            }
                        }

                        js.put("Answer",answers);
                    }
                    else{
                        js.put("Answer",surveyQSlists.get(i).getAnswer());
                    }

                    ja.put(js);

                }
                finaljs.put("val", ja);
                Log.v("printing_json_dynamic", finaljs.toString());

                saveSurvey(finaljs);

            } catch (Exception e) {

            }
        }

    }

    private void saveSurvey(JSONObject finaljs) {
        progressDialog.show();

        Call<ResponseBody> approval = api_interface.saveSurveylist(finaljs.toString());

        approval.enqueue(new Callback<ResponseBody>() {
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
                        Log.v("printing_here_dynamic", is.toString());
                        JSONObject js = new JSONObject(is.toString());
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            progressDialog.dismiss();
                            Toast.makeText(SurveyActivity.this, getResources().getString(R.string.datasave), Toast.LENGTH_SHORT).show();
                            dynamiclistview.setVisibility(View.GONE);
                            spinnercustomer.setSelection(0);
                            et_selectcustomer.setText(getResources().getString(R.string.slct_cust));
                            choosensurveytxt.setText("");
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SurveyActivity.this, getResources().getString(R.string.cannotload), Toast.LENGTH_SHORT).show();
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

    private boolean validationOfField() {
        boolean val = true;
        for (int i=0;i<surveyQSlists.size();i++) {
            SurveyQSlist mm =surveyQSlists.get(i);
            Log.v("ans1>>",mm.getAnswer());

            if(mm.getMandatory().equalsIgnoreCase("0")){
                if(mm.getAnswer().equalsIgnoreCase("")){
                    val  =false;
                }else{
                    val=true;
                }
            }else{
                val=true;
            }
        }
        return val;

    }

    private void getChemistList() {
        chemlist.clear();
        et_searchDr.setHint(getResources().getString(R.string.search_chm));
        cancel_txt.setText(getResources().getString(R.string.slct_chem));
        rl_dcr_presurvey_analysisMain.setVisibility(View.GONE);
        dcr_drselection_gridview.setVisibility(View.VISIBLE);
        String[] chemcatarrray = Chemcat.split(",");
        String strchem = "";
        for (int i = 0; i < chemcatarrray.length; i++) {
            strchem += chemcatarrray[i] + ";";
        }
        dbh.open();

//        if (TextUtils.isEmpty(subSfCode) || subSfCode.equalsIgnoreCase("null"))
//            mCursor = dbh.select_Chemist_bySf(SF_Code, mMydayWtypeCd);
//        else
            mCursor = dbh.select_Chemist_bySf(subSfCode, mMydayWtypeCd);
        while (mCursor.moveToNext()) {
            if (chemcatarrray.length > 0) {
                if ((";" + strchem).indexOf(";" + mCursor.getString(16) + ";") > -1) {
                    Log.v("chemCn",mCursor.getString(17));
                    _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(5), mCursor.getString(4), mCursor.getString(11), mCursor.getString(12),mCursor.getString(17));
                    chemlist.add(_custom_DCR_GV_Dr_Contents);
                    Collections.sort(chemlist, Custom_DCR_GV_Dr_Contents.StuNameComparator);

                }
            }

            Log.v("chmList_value", chemlist.size() + "");
            _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getApplicationContext(), chemlist, "C");
            gridView.setAdapter(_DCR_GV_Selection_adapter);
            _DCR_GV_Selection_adapter.notifyDataSetChanged();
            progressDialog.dismiss();
            //progress.dismiss();

            DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
                @Override
                public void itemClick(String drname, String drcode) {
                    typee = "C";
                    OnItemClick(drname, drcode, typee);
                }
            });


        }
    }

    private void getDoctorlist() {
        drList.clear();
        et_searchDr.setHint(getResources().getString(R.string.search_lst_dr));
        cancel_txt.setText(getResources().getString(R.string.slct_lst_dr));

        rl_dcr_presurvey_analysisMain.setVisibility(View.GONE);
        dcr_drselection_gridview.setVisibility(View.VISIBLE);
        String[] drcatarrray = Drcat.split(",");
        String[] drspecarray = Drspec.split(",");
        String[] drclsarray = Drcls.split(",");
        String sStr="",sStr1="";

        for (String s : drcatarrray) {
            if(s.isEmpty())
            {
                Log.v("empty", String.valueOf(s.length()));
            }else {
                sStr += s + ";";
            }
            Log.v("drcode>>", sStr);
        }
        for (String s : drspecarray) {
            if(s.isEmpty())
            {
                Log.v("empty", String.valueOf(s.length()));
            }else {
                sStr1 += s + ";";
            }
            Log.v("drcode>>", sStr1);
        }
        dbh.open();
//        if (TextUtils.isEmpty(subSfCode) || subSfCode.equalsIgnoreCase("null")){
//            mCursor = dbh.select_doctors_bySf(SF_Code, mMydayWtypeCd);
//
//        }else
            mCursor = dbh.select_doctors_bySf(subSfCode, mMydayWtypeCd);
        if(mCursor.getCount()!=0) {
            while (mCursor.moveToNext()) {
                if (sStr.length() == 0 && sStr1.length() >0) {
                    if ((";" + sStr1).indexOf(";" + mCursor.getString(8) + ";") > -1) {
                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5), mCursor.getString(22), mCursor.getString(23), mCursor.getString(8));
                        drList.add(_custom_DCR_GV_Dr_Contents);
                    }
                } else if (sStr1.length() == 0 && sStr.length() >0) {
                    if ((";" + sStr).indexOf(";" + mCursor.getString(7) + ";") > -1) {
                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5), mCursor.getString(22), mCursor.getString(23), mCursor.getString(8));
                        drList.add(_custom_DCR_GV_Dr_Contents);
                    }
                }
                else if (sStr.length() > 0 && sStr1.length() >0) {
                    if ((";" + sStr).indexOf(";" + mCursor.getString(7) + ";") > -1 && (";" + sStr1).indexOf(";" + mCursor.getString(8) + ";") > -1) {

                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5), mCursor.getString(22), mCursor.getString(23), mCursor.getString(8));
                        drList.add(_custom_DCR_GV_Dr_Contents);
                        Collections.sort(drList, Custom_DCR_GV_Dr_Contents.StuNameComparator);

                        Log.v("drlist>>", mCursor.getString(2) + " " + mCursor.getString(1));
                    }
                }
            }
        }
        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getApplicationContext(), drList, "D");
        gridView.setAdapter(_DCR_GV_Selection_adapter);
        _DCR_GV_Selection_adapter.notifyDataSetChanged();
        if(progressDialog!=null) progressDialog.dismiss();
        //progress.dismiss();

        DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
            @Override
            public void itemClick(String drname, String drcode) {
                typee = "D";
                OnItemClick(drname, drcode, typee);
            }
        });
    }

    private void getQuestions(String sur_code) {
        try {
            JSONArray jsonArray = new JSONArray(mCommonSharedPreference.getValueFromPreference("surveyjson"));
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if(sur_code.equalsIgnoreCase(jsonObject.getString("id"))){
                    mCommonSharedPreference.setValueToPreference("surveyjsonobject",jsonObject.toString());
                    Log.v("sur>>",jsonObject.toString());
                }
            }

        }catch(Exception e){

        }
    }
    private void getSurveyJSON() {
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        JSONObject jj=new JSONObject();
        try {
            jj.put("SF", SF_Code);
            jj.put("Div", division_code);

            Log.v("printing_reject", jj.toString());
            Call<ResponseBody> surveylist = apiService.getSurveylist(jj.toString());
            surveylist.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.v("printing_reject11",response.isSuccessful()+"");
                    if (response.isSuccessful()) {
                        array.clear();
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                       // String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

//                            while ((line = bf.readLine()) != null) {
//                                is.append(line);
//                            }
                            for ( String line; (line = bf.readLine()) != null; ) {
                                is.append(line);
                            }

                            mCommonSharedPreference.setValueToPreference("surveyjson",is.toString());

                            JSONArray jsonArray = new JSONArray(is.toString());
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                Log.v("dsss",json.getString("name"));
                                array.add(new ModelDynamicList(json.getString("name"), json.getString("id"), false, json.getString("from_date"), json.getString("to_date")));
                            }
                            adp = new AdapterDynamicActivity(SurveyActivity.this, array,"survey");
                            list_view.setAdapter(adp);
                            adp.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }catch (Exception e){
                            progressDialog.dismiss();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
        }catch(Exception e){

        }

        AdapterDynamicActivity.bindDynamicListListner1(new SpecialityListener1() {
            @Override
            public void specialityCode(String code,String name) {
                sur_code=code;
                spinnercustomer.setSelection(0);
                et_selectcustomer.setText("");
                getQuestions(sur_code);
                choosensurveytxt.setText(name);
                surveyQSlists.clear();
                save_btn.setVisibility(View.GONE);

            }
        });
    }

    private void OnItemClick(String drname, String drcode, String typeee) {
        rl_dcr_presurvey_analysisMain.setVisibility(View.VISIBLE);
        dynamiclistview.setVisibility(View.VISIBLE);
        dcr_drselection_gridview.setVisibility(View.GONE);
        et_selectcustomer.setText(drname);
        dynamiclistview.setVisibility(View.VISIBLE);
        custcode=drcode;

        if(SF_Type.equalsIgnoreCase("2")){
            spinner.setVisibility(View.VISIBLE);
        }
        else
            spinner.setVisibility(View.GONE);
        if(typee.equalsIgnoreCase("D")){
            try {
                JSONObject jsonObject = new JSONObject(mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));
                Log.v("qn>>",mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));

                JSONArray jsonArray=jsonObject.getJSONArray("survey_for");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    if(jsonObject1.getString("Stype").contains("D")){

                        surveyQSlists.add(new SurveyQSlist(jsonObject1.getString("id"),jsonObject1.getString("Survey"),
                                jsonObject1.getString("DrCat"),jsonObject1.getString("DrSpl"),jsonObject1.getString("DrCls"),jsonObject1.getString("HosCls"),
                                jsonObject1.getString("ChmCat"),jsonObject1.getString("Stkstate"),jsonObject1.getString("StkHQ"),jsonObject1.getString("Stype"),
                                jsonObject1.getString("Qc_id"),jsonObject1.getString("Qtype"),jsonObject1.getString("Qlength"),jsonObject1.getString("Mandatory"),jsonObject1.getString("Qname"),
                                jsonObject1.getString("Qanswer"),jsonObject1.getString("Active_Flag"),""));

                        Log.v("qn>>",jsonObject1.getString("Qanswer"));
                    }
                    Log.v("qn>>1", String.valueOf(surveyQSlists.size()));

                }

            }catch(Exception e){

            }

        }else if(typee.equalsIgnoreCase("C")){
            try {
                JSONObject jsonObject = new JSONObject(mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));
                Log.v("qn>>",mCommonSharedPreference.getValueFromPreference("surveyjsonobject"));

                JSONArray jsonArray=jsonObject.getJSONArray("survey_for");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    if(jsonObject1.getString("Stype").contains("C")){
                        surveyQSlists.add(new SurveyQSlist(jsonObject1.getString("id"),jsonObject1.getString("Survey"),
                                jsonObject1.getString("DrCat"),jsonObject1.getString("DrSpl"),jsonObject1.getString("DrCls"),jsonObject1.getString("HosCls"),
                                jsonObject1.getString("ChmCat"),jsonObject1.getString("Stkstate"),jsonObject1.getString("StkHQ"),jsonObject1.getString("Stype"),
                                jsonObject1.getString("Qc_id"),jsonObject1.getString("Qtype"),jsonObject1.getString("Qlength"),
                                jsonObject1.getString("Mandatory"),jsonObject1.getString("Qname"),
                                jsonObject1.getString("Qanswer"),jsonObject1.getString("Active_Flag"),""));
                        Log.v("qn>>",jsonObject1.getString("Qanswer"));
                    }
                    Log.v("qn>>1", String.valueOf(surveyQSlists.size()));
                }
            }catch(Exception e){

            }
        }
        if(!surveyQSlists.isEmpty()) {
            save_btn.setVisibility(View.VISIBLE);
        }else
        {
            save_btn.setVisibility(View.GONE);
        }

        AdapterSurveyQuestionview adapterSurveyQuestionview=new AdapterSurveyQuestionview(this,surveyQSlists);
        dynamiclistview.setAdapter(adapterSurveyQuestionview);
        adapterSurveyQuestionview.notifyDataSetChanged();
    }
}