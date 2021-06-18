package saneforce.sanclm.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDynamicList;
import saneforce.sanclm.activities.Model.ModelDynamicView;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.adapter_class.AdapterDynamicActivity;
import saneforce.sanclm.adapter_class.AdapterDynamicView;
import saneforce.sanclm.adapter_class.AdapterForDynamicSelectionList;
import saneforce.sanclm.adapter_class.AdapterPopupSpinnerSelection;
import saneforce.sanclm.adapter_class.Custom_DCR_GV_Dr_Contents;
import saneforce.sanclm.adapter_class.DCR_GV_Selection_adapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.ImageFilePath;
import saneforce.sanclm.fragments.DCRDRCallsSelection;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DCRCallSelectionFilter;
import saneforce.sanclm.util.TwoTypeparameter;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;


public class ProfilingActivity extends AppCompatActivity implements View.OnClickListener
{
    AppCompatSpinner spinner;
    DCRDRCallsSelection dcrdrCallsSelection;
    DataBaseHandler dbh;
    Cursor mCursor;
    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods commonUtilsMethods;
    DCR_GV_Selection_adapter _DCR_GV_Selection_adapter=null;
    Custom_DCR_GV_Dr_Contents _custom_DCR_GV_Dr_Contents;
    ArrayList<Custom_DCR_GV_Dr_Contents> drList ;
    ArrayList<Custom_DCR_GV_Dr_Contents> chmList ;
    ArrayList<Custom_DCR_GV_Dr_Contents> stckList ;
    ArrayList<Custom_DCR_GV_Dr_Contents> UndrList ;
    ArrayList<SlideDetail> list_dr;
    ArrayList<ModelDynamicList> array = new ArrayList<>();
    ArrayList<ModelDynamicView> array_view = new ArrayList<>();
    RelativeLayout rl_dcr_precall_analysisMain , dcr_drselection_gridview;
    String SF_Code,mMydayWtypeCd,subSfCode,division_code,cat_code,spec_code,qual_code,class_code,hosp_code,terr_code;
    String selectedProductCode="";
    TextView tv_drName;
    Button btn_re_select_doctor;
    AppCompatEditText et_searchDr,et_dobd,et_dobm,et_doby,et_dowd,et_dowm,et_dowy;
    ImageView iv_back;
    TextView txt_select_qua,txt_select_category,txt_select_type,txt_select_spec;
    AppCompatAutoCompleteTextView at_district,at_addr,at_mobile,at_phone,at_email;
    CheckBox check_male,check_female;
    AdapterDynamicActivity adp;
    ProgressDialog progressDialog = null;
    ListView list;
    Api_Interface api_interface;
    int pos_upload_file = 0;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    TimePickerDialog timePickerDialog;
    boolean isEmpty = false;
    Button btn_Submit;
    String mobile,phone,address,email,dobd,dobm,doby,dowd,dowm,dowy,gender,specName,catName,qualName,className,hospName,district,pinCode,type;
    String db_connPath,target,productRange,listedDrVisit,visitMultiple,listedDrAvgPatients,listedDrclsPatients;
//    String language;
//    Context context;
//    Resources resources;
    AdapterDynamicView adp_view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        language = sharedPreferences.getString(language_string, "");
//        if (!language.equals("")){
//            Log.d("homelang",language);
//            selected(language);
//            context = LocaleHelper.setLocale(ProfilingActivity.this, language);
//            resources = context.getResources();
//        }else {
//            selected("en");
//            context = LocaleHelper.setLocale(ProfilingActivity.this, "en");
//            resources = context.getResources();
//        }


        setContentView(R.layout.activity_profilings);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dbh = new DataBaseHandler(getApplicationContext());
        commonUtilsMethods = new CommonUtilsMethods(ProfilingActivity.this);
        mCommonSharedPreference = new CommonSharedPreference(getApplicationContext());
        SF_Code= mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        mMydayWtypeCd = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE);
        subSfCode=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        division_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        GridView gridView = (GridView) findViewById(R.id.gridview_dcrselect);
        list_dr =new ArrayList<>();

        rl_dcr_precall_analysisMain = (RelativeLayout) findViewById(R.id.rl_dcr_precall_analysis_main) ;
        dcr_drselection_gridview= (RelativeLayout) findViewById(R.id.rl_dcr_drgrid_selection) ;
        btn_re_select_doctor = (Button) findViewById(R.id.btn_reselect) ;
        tv_drName = (TextView)  findViewById(R.id.tv_drName) ;
        et_searchDr = (AppCompatEditText) findViewById(R.id.et_searchDr);
        et_searchDr.setText("");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        dcr_drselection_gridview.setVisibility(View.VISIBLE);
        btn_Submit=(Button) findViewById(R.id.btn_Submit);
        spinner=(AppCompatSpinner) findViewById(R.id.spinner);
        txt_select_qua=(TextView) findViewById(R.id.txt_select_qua);
        txt_select_category=(TextView) findViewById(R.id.txt_select_category);
        txt_select_type=(TextView) findViewById(R.id.txt_select_type);
        txt_select_spec=(TextView) findViewById(R.id.txt_select_spec);
        et_dobd = (AppCompatEditText) findViewById(R.id.et_dobd);
        et_dobm = (AppCompatEditText) findViewById(R.id.et_dobm);
        et_doby = (AppCompatEditText) findViewById(R.id.et_doby);
        et_dowd = (AppCompatEditText) findViewById(R.id.et_dowd);
        et_dowm = (AppCompatEditText) findViewById(R.id.et_dowm);
        et_dowy = (AppCompatEditText) findViewById(R.id.et_dowy);
        at_district=(AppCompatAutoCompleteTextView) findViewById(R.id.at_district);
        at_addr=(AppCompatAutoCompleteTextView) findViewById(R.id.at_addr);
        at_mobile=(AppCompatAutoCompleteTextView) findViewById(R.id.at_mobile);
        at_phone=(AppCompatAutoCompleteTextView) findViewById(R.id.at_phone);
        at_email=(AppCompatAutoCompleteTextView) findViewById(R.id.at_email);
        check_male=(CheckBox) findViewById(R.id.check_male);
        check_female=(CheckBox) findViewById(R.id.check_female);
        list=(ListView) findViewById(R.id.list);


        api_interface = RetroClient.getClient(db_connPath).create(Api_Interface.class);


        List<String> selectPosition = new ArrayList<>();
        selectPosition.add(/*"Listed Dr."*/ getResources().getString(R.string.listed_dr));
        selectPosition.add(/*"Chemist"*/ getResources().getString(R.string.chem));
        selectPosition.add(/*"Stockist"*/ getResources().getString(R.string.stockist));
        selectPosition.add(/*"Unlisted Dr."*/ getResources().getString(R.string.unlisted_dr));
        dcrdrCallsSelection=new DCRDRCallsSelection();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, selectPosition);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        txt_select_category.setOnClickListener(this);
        txt_select_qua.setOnClickListener(this);
        txt_select_type.setOnClickListener(this);
        txt_select_spec.setOnClickListener(this);
        btn_re_select_doctor.setOnClickListener(this);

        AdapterDynamicView.bindListernerForDateRange(new TwoTypeparameter() {
            @Override
            public void update(int value, int pos) {
                if (value == 5) {
                    pos_upload_file = pos;
                    uploadFile();

                } else if (value > 5 && value < 10) {
                    datePick(pos, value);
                } else
                    timePicker(pos, value);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(ProfilingActivity.this,HomeDashBoard.class);
                startActivity(ii);

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
                _DCR_GV_Selection_adapter.getFilter().filter(cs);
                _DCR_GV_Selection_adapter.notifyDataSetChanged();
            }
        });

        check_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked){

                    check_female.setChecked(false);
                }

            }
        });
        check_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    check_male.setChecked(false);
                }

            }
        });


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commonUtilsMethods.isOnline(ProfilingActivity.this)) {
                    if (validationOfField())
                        saveEntry();
                    else
                        Toast.makeText(ProfilingActivity.this,  getResources().getString(R.string.fillmand), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.nonetwrk), Toast.LENGTH_SHORT).show();


            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("postion_view_id", array_view.get(position).getViewid());
                if (array_view.get(position).getViewid().equalsIgnoreCase("8") ||
                        array_view.get(position).getViewid().equalsIgnoreCase("12") ||
                        array_view.get(position).getViewid().equalsIgnoreCase("13")) {
                    popupSpinner1(0, array_view.get(position).getA_list(), position);
                } else if (array_view.get(position).getViewid().equalsIgnoreCase("9")) {
                    popupSpinner1(1, array_view.get(position).getA_list(), position);
                } else if (array_view.get(position).getViewid().equalsIgnoreCase("6")) {
                    // tp.setVisibility(View.VISIBLE);
                    timePicker(position, 8);
                } else if (array_view.get(position).getViewid().equalsIgnoreCase("4")) {
                    // tp.setVisibility(View.VISIBLE);
                    datePick(position, 8);


                } else if (array_view.get(position).getViewid().equalsIgnoreCase("10"))
                    uploadFile();
               /* else    if(array_view.get(position).getViewid().equalsIgnoreCase("10")){
                   // tp.setVisibility(View.VISIBLE);
                    datePick(position);
                }*/

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Select one from list")){
//                }else {
//                    String item = parent.getItemAtPosition(position).toString();
//
//                }
                switch (position)
                {
                    case 0:
                        et_searchDr.setHint("Search "+mCommonSharedPreference.getValueFromPreference("drcap"));
                        dbh.open();
                        drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                        drList.clear();
                        Log.v("checking_sfcode",SF_Code+"cluster"+mMydayWtypeCd+"subsfcode"+subSfCode);

                        mCursor = dbh.select_doctors_bySf(subSfCode,mMydayWtypeCd);
                        Log.v("cursor_count", String.valueOf(mCursor.getCount()));
                        while (mCursor.moveToNext()) {
                            Log.v("Dr_detailing_Print12366",mCursor.getString(2));
                                _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(6), mCursor.getString(5),mCursor.getString(22),mCursor.getString(23),mCursor.getString(8));
                                drList.add(_custom_DCR_GV_Dr_Contents);
                        }



                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getApplicationContext(),drList,"D");
                        gridView.setAdapter(_DCR_GV_Selection_adapter);

                        DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
                            @Override
                            public void itemClick(String drname, String drcode) {
                                OnItemClick(drname, drcode);
                            }
                        });
                                break;
                    case 1:
                        et_searchDr.setHint("Search "+mCommonSharedPreference.getValueFromPreference("chmcap"));
                        dbh.open();
                        chmList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                        chmList.clear();
                        if(TextUtils.isEmpty(subSfCode) || subSfCode.equalsIgnoreCase("null"))
                            mCursor = dbh.select_Chemist_bySf(SF_Code,mMydayWtypeCd);
                        else
                            mCursor = dbh.select_Chemist_bySf(subSfCode,mMydayWtypeCd);


                        Log.v("chemist_count", String.valueOf(mCursor.getCount()));
                        while (mCursor.moveToNext()) {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2), mCursor.getString(1), mCursor.getString(10), mCursor.getString(9), mCursor.getString(5), mCursor.getString(4), mCursor.getString(11), mCursor.getString(12));
                                chmList.add(_custom_DCR_GV_Dr_Contents);

                        }
                        Log.v("chmList_value",chmList.size()+"");

                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getApplicationContext(),chmList,"C");
                        gridView.setAdapter(_DCR_GV_Selection_adapter);
                        _DCR_GV_Selection_adapter.notifyDataSetChanged();

                                DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
                                    @Override
                                    public void itemClick(String drname, String drcode) {

                                        OnItemClick(drname, drcode);
                                    }
                                });

                        break;
                    case 2:
                        et_searchDr.setHint("Search Listed "+mCommonSharedPreference.getValueFromPreference("stkcap"));
                        dbh.open();
                        stckList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                        stckList.clear();

                        mCursor = dbh.select_Stockist_bySf(SF_Code,mMydayWtypeCd);

                        Log.v("checking_sfcode_stck",SF_Code+"cluster"+mMydayWtypeCd);
                        Log.v("stocklist_count", String.valueOf(mCursor.getCount()));
                        while (mCursor.moveToNext()) {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(4),mCursor.getString(3),mCursor.getString(14),mCursor.getString(15));
                            stckList.add(_custom_DCR_GV_Dr_Contents);
                        }

                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getApplicationContext(),stckList,"S");
                        gridView.setAdapter(_DCR_GV_Selection_adapter);
                                DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
                                    @Override
                                    public void itemClick(String drname, String drcode) {
                                        OnItemClick(drname, drcode);
                                    }
                                });
                        break;
                    case 3:
                        et_searchDr.setHint("Search Listed "+mCommonSharedPreference.getValueFromPreference("ucap"));
                        dbh.open();
                        UndrList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                        UndrList.clear();
                        mCursor = dbh.select_unListeddoctors_bySf(SF_Code,mMydayWtypeCd);
                        while (mCursor.moveToNext()) {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5),mCursor.getString(16),mCursor.getString(17));
                            UndrList.add(_custom_DCR_GV_Dr_Contents);
                        }


                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getApplicationContext(),UndrList,"U");
                        gridView.setAdapter(_DCR_GV_Selection_adapter);

                         DCR_GV_Selection_adapter.bindListner(new DCRCallSelectionFilter() {
                                        @Override
                                        public void itemClick(String drname, String drcode) {
                                            OnItemClick(drname, drcode);
                                        }
                         });
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void selectedPosition(DCRDRCallsSelection dcrdrCallsSelection) {
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout,dcrdrCallsSelection);
//        fragmentTransaction.commit();
    }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

                    public void OnItemClick(String name, final String code) {
                        selectedProductCode = code;
                        tv_drName.setText(name);
                        mCommonSharedPreference.setValueToPreference("drName", name);
                        mCommonSharedPreference.setValueToPreference("drCode", code);
                        rl_dcr_precall_analysisMain.setVisibility(View.VISIBLE);
                        dcr_drselection_gridview.setVisibility(View.GONE);

                        CommonUtils.TAG_CHEM_CODE = code;
                        CommonUtils.TAG_CHEM_NAME = name;
                        CommonUtils.TAG_FEED_SF_CODE = subSfCode;

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("SF", SF_Code);
                            jsonObject.put("CusCode", code);
                            jsonObject.put("typ", "D");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v("drDetails_param", jsonObject.toString());


                        Call<ResponseBody> drDetails = api_interface.getDrDetails(jsonObject.toString());
                       drDetails.enqueue(new Callback<ResponseBody>() {
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
                                       Log.v("drDetails", is.toString());
                                       JSONArray ja = new JSONArray(is.toString());
                                       for (int i = 0; i < ja.length(); i++) {
                                           JSONObject js = ja.getJSONObject(i);

                                           mobile = js.getString("Mobile");
                                           phone = js.getString("Phone");
                                           address = js.getString("Addr1");
                                           email = js.getString("Email");
                                           dobd = js.getString("DOBD");
                                           dobm = js.getString("DOBM");
                                           doby = js.getString("DOBY");
                                           dowd = js.getString("DOWD");
                                           dowm = js.getString("DOWM");
                                           dowy = js.getString("DOWY");
                                           gender=js.getString("Gender");
                                           specName=js.getString("SpecName");
                                           catName=js.getString("CatName");
                                           qualName=js.getString("QualName");
                                           className=js.getString("ClsName");
                                           hospName=js.getString("HospNames");
                                           spec_code=js.getString("SpecCode");
                                           cat_code=js.getString("CatCode");
                                           qual_code=js.getString("QualCode");
                                           class_code=js.getString("ClsCode");
                                           hosp_code=js.getString("HospCodes");
                                           terr_code=js.getString("TerrCode");
                                           class_code=js.getString("ClsCode");
                                           district=js.getString("District");
                                           pinCode=js.getString("PinCode");
                                           type=js.getString("Type");
                                           target=js.getString("Target");
                                           productRange=js.getString("Product_Range");
                                           target=js.getString("Target");
                                           productRange=js.getString("Product_Range");
                                           listedDrVisit=js.getString("ListedDr_Visit_Days");
                                           visitMultiple=js.getString("Visit_Session_Multiple");
                                           listedDrAvgPatients=js.getString("ListedDr_Avg_Patients");
                                           listedDrclsPatients=js.getString("ListedDr_Class_Patients");



                                           txt_select_qua.setText(qualName);
                                           txt_select_spec.setText(specName);
                                           txt_select_category.setText(catName);
                                           et_dobd.setText(dobd);
                                           et_dobm.setText(dobm);
                                           et_doby.setText(doby);
                                           et_dowd.setText(dowd);
                                           et_dowm.setText(dowd);
                                           et_dowy.setText(dowy);
                                           at_district.setText(district);
                                           at_addr.setText(address);
                                           at_mobile.setText(mobile);
                                           at_phone.setText(phone);
                                           at_email.setText(email);


                                           Log.v("drdetails_info", js.toString());

                                       }

                                   } catch (Exception e) {
                                       Log.e("Errorexception",e.getMessage());
                                   }
                               }
                           }

                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable t) {

                           }
                       });

                        callDynamicList();

}


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_reselect:
                rl_dcr_precall_analysisMain.setVisibility(View.GONE);
                dcr_drselection_gridview.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_select_qua:
                gettingTableValue(1);
                break;
            case R.id.txt_select_spec:
                gettingTableValue(2);
                break;
            case R.id.txt_select_category:
                gettingTableValue(3);
                break;

        }
    }

    public void gettingTableValue(int x){
        list_dr.clear();
        dbh.open();
        Cursor cur=null;
        if(x==1)
            cur=dbh.select_quality_list();
        else if(x==2)
            cur=dbh.select_speciality_list();
        else if(x==3)
            cur=dbh.select_category_list();

        if(cur.getCount()>0){
            while(cur.moveToNext()){
                list_dr.add(new SlideDetail(cur.getString(2),cur.getString(1)));

            }
        }

        popupSpinner(x);
    }

    public void popupSpinner(final int x){
        final Dialog dialog=new Dialog(this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        ImageView close_btn = (ImageView)dialog.findViewById(R.id.close_img);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        AdapterPopupSpinnerSelection popupAdapter=new AdapterPopupSpinnerSelection(getApplicationContext(),list_dr);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("qualification_select",list_dr.get(position).getInputName());
                if(x==1) {
                    txt_select_qua.setText(list_dr.get(position).getInputName());

                }
                else if(x==2){
                    txt_select_spec.setText(list_dr.get(position).getInputName());

                }
                else if(x==3) {
                    txt_select_category.setText(list_dr.get(position).getInputName());
                    cat_code =list_dr.get(position).getIqty();
                    Log.v("Cat_code",cat_code);
                    String profileControls= mCommonSharedPreference.getValueFromPreference("Profiledynamic");
                    JSONArray jsonArray= null;
                    try {
                        jsonArray = new JSONArray(profileControls);
                        genAdditionalFields(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                dialog.dismiss();
            }
        });


    }

    public void popupSpinner1(int type, final ArrayList<PopFeed> array_selection, final int pos) {
        final Dialog dialog = new Dialog(ProfilingActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText(array_view.get(pos).getFieldname());
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        if (array_selection.contains(new PopFeed(true))) {
            isEmpty = false;
        } else
            isEmpty = true;

        final AdapterForDynamicSelectionList adapt = new AdapterForDynamicSelectionList(ProfilingActivity.this, array_selection, type);
        popup_list.setAdapter(adapt);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
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
                adapt.getFilter().filter(s);
                return false;
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty) {
                    if (array_selection.contains(new PopFeed(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            PopFeed m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
                dialog.dismiss();
                //commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_selection.contains(new PopFeed(true))) {
                    for (int i = 0; i < array_selection.size(); i++) {
                        PopFeed m = array_selection.get(i);
                        if (m.isClick()) {
                            array_view.get(pos).setValue(m.getTxt());
                            i = array_selection.size();
                            adp_view.notifyDataSetChanged();
                            break;
                        }
                    }

                } else {
                    array_view.get(pos).setValue("");
                    adp_view.notifyDataSetChanged();
                }
               // dialog.dismiss();
                //commonFun();
            }
        });

    }

    public void commonFun() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }


    public void callDynamicList() {
        JSONObject json = new JSONObject();
        //progressDialog = commonUtilsMethods.createProgressDialog(ProfilingActivity.this);
        try {
            json.put("Div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = api_interface.getCustProfCtrls(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
                        Log.v("datawises", response.body().toString());
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
                            array.clear();
                            Log.v("printing_here_dynamic", is.toString());
                            JSONArray jsonArray = new JSONArray(is.toString());
                            mCommonSharedPreference.setValueToPreference("Profiledynamic", is.toString());
                            genAdditionalFields(jsonArray);

                          //  progressDialog.dismiss();



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


public void genAdditionalFields(JSONArray jsonArray){
    array_view.clear();
    JSONObject jsonObject = null;
    String jsonData = null;
    InputStreamReader ip = null;
    StringBuilder is = new StringBuilder();
    String line = null;
    progressDialog = commonUtilsMethods.createProgressDialog(ProfilingActivity.this);
    progressDialog.show();
    try {
        for (int i = 0; i < jsonArray.length(); i++) {
            ArrayList<PopFeed> arr = new ArrayList<>();
            JSONObject json = jsonArray.getJSONObject(i);
           if (json.getString("Cat_code").equalsIgnoreCase(cat_code)) {
                JSONArray jarray = json.getJSONArray("input");
                if (jarray.length() != 0) {
                    for (int m = 0; m < jarray.length(); m++) {
                        JSONObject jjss = jarray.getJSONObject(m);
                        Log.v("json_input_iss", jjss.getString(json.getString("Table_code")));
                        arr.add(new PopFeed(jjss.getString(json.getString("Table_name")), false, jjss.getString(json.getString("Table_code"))));
                    }

                }
                String para = "";
                if (json.getString("Control_Id").equalsIgnoreCase("11"))
                    para = json.getString("Table_code");
                else
                    para = json.getString("Control_Para");
                array_view.add(new ModelDynamicView(json.getString("Control_Id"), "", json.getString("Field_Name"), "", arr, para, json.getString("Sl_no"), json.getString("Activity_SlNo"), "", json.getString("Mandatory"),json.getString("Type")));
            }
       }

        adp_view = new AdapterDynamicView(array_view, ProfilingActivity.this);
        list=(ListView) findViewById(R.id.list);
        list.setAdapter(adp_view);
        adp_view.notifyDataSetChanged();
        progressDialog.dismiss();


    } catch (Exception e) {

        Log.v("Exception",e.getMessage());
    }
}

    /*public void callDynamicViewList(String slno) {
        final JSONObject json = new JSONObject();
        try {
            json.put("slno", slno);
            json.put("SF", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            json.put("div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = api_interface.getDynamicViewTest(json.toString());

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
                            array_view.clear();
                            Log.v("printing_dynamic_view", is.toString());
                            JSONArray jsonArray = new JSONArray(is.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArrayList<PopFeed> arr = new ArrayList<>();
                                JSONObject json = jsonArray.getJSONObject(i);
                                JSONArray jarray = json.getJSONArray("input");
                                if (jarray.length() != 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        JSONObject jjss = jarray.getJSONObject(m);
                                        Log.v("json_input_iss", jjss.getString(json.getString("Table_code")));
                                        arr.add(new PopFeed(jjss.getString(json.getString("Table_name")), false, jjss.getString(json.getString("Table_code"))));
                                    }

                                }
                                String para = "";
                                if (json.getString("Control_Id").equalsIgnoreCase("11"))
                                    para = json.getString("Table_code");
                                else
                                    para = json.getString("Control_Para");
                                array_view.add(new ModelDynamicView(json.getString("Control_Id"), "", json.getString("Field_Name"), "", arr, para, json.getString("Creation_Id"), json.getString("Activity_SlNo"), "", json.getString("Mandatory")));

//                                if(json.getString("Control_Id").equalsIgnoreCase("17"))
//                                {
//                                    para = json.getString("Control_Para");
//                                    array_view.add(new ModelDynamicView(json.getString("Control_Id"), "", json.getString("Field_Name"), "", arr, para, json.getString("Creation_Id"), json.getString("Activity_SlNo"), "", json.getString("Mandatory")));
//                                }
                            }

                            adp_view = new AdapterDynamicView(array_view, ProfilingActivity.this,"","");
                            list.setAdapter(adp_view);
                            adp_view.notifyDataSetChanged();
                            //progressDialog.dismiss();


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

    }*/
    public void uploadFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 7);
    }

    public void datePick(final int pos, final int value) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(ProfilingActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ModelDynamicView mm = array_view.get(pos);
                int mnth = monthOfYear + 1;
                if (mm.getViewid().equalsIgnoreCase("5")) {
                    if (value == 8) {
                        if (TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(dayOfMonth + "-" + mnth + "-" + year);
                        else {
                            String val = dayOfMonth + "-" + mnth + "-" + year;
                            if (dateDifference(val, mm.getTvalue()) < 0)
                                Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.datelesser), Toast.LENGTH_SHORT).show();
                            else
                                mm.setValue(dayOfMonth + "-" + mnth + "-" + year);

                        }
                    } else {
                        if (TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.filldate), Toast.LENGTH_SHORT).show();
                        else {
                            String val = dayOfMonth + "-" + mnth + "-" + year;
                            if (dateDifference(mm.getValue(), val) < 0)
                                Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.togreater), Toast.LENGTH_SHORT).show();
                            else
                                mm.setTvalue(dayOfMonth + "-" + mnth + "-" + year);
                        }


                    }
                } else
                    mm.setValue(dayOfMonth + "-" + mnth + "-" + year);

                adp_view.notifyDataSetChanged();
               // commonFun();

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    public void timePicker(final int pos, final int value) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(ProfilingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                ModelDynamicView mm = array_view.get(pos);
                if (mm.getViewid().equalsIgnoreCase("7")) {
                    if (value == 12) {
                        if (TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(selectedHour + ":" + selectedMinute);
                        else {
                            int thr = spiltTime(mm.getTvalue());
                            int tmin = spiltMin(mm.getTvalue());
                            if (thr == selectedHour) {
                                if (selectedMinute < tmin) {
                                    mm.setValue(selectedHour + ":" + selectedMinute);
                                } else
                                    Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.timelesser), Toast.LENGTH_SHORT).show();
                            } else if (thr > selectedHour) {
                                mm.setValue(selectedHour + ":" + selectedMinute);
                            } else
                                Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.timelesser), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        if (TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.filltime), Toast.LENGTH_SHORT).show();
                        else {
                            int fhr = spiltTime(mm.getValue());
                            int fmin = spiltMin(mm.getValue());
                            if (fhr == selectedHour) {
                                if (selectedMinute > fmin) {
                                    mm.setTvalue(selectedHour + ":" + selectedMinute);
                                } else
                                    Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.totimegreater), Toast.LENGTH_SHORT).show();
                            } else if (fhr < selectedHour) {
                                mm.setTvalue(selectedHour + ":" + selectedMinute);
                            } else
                                Toast.makeText(ProfilingActivity.this,  getResources().getString(R.string.totimegreater), Toast.LENGTH_SHORT).show();

                        }
                    }

                } else
                    mm.setValue(selectedHour + ":" + selectedMinute);


                adp_view.notifyDataSetChanged();
                //commonFun();

            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    public void saveEntry() {
        boolean isEmpty = false;
        int count = 0;
        progressDialog.show();
        if (array_view.contains(new ModelDynamicView("10"))) {
            for (int i = 0; i < array_view.size(); i++) {
                ModelDynamicView mm = array_view.get(i);
                if (mm.getViewid().equalsIgnoreCase("10")) {
                    if (!TextUtils.isEmpty(mm.getValue())) {
                        isEmpty = true;
                        count = count + 1;
                        getMulipart(mm.getValue(), i);
                    }
                }
            }
        } else {
            saveValueEntry();
        }

    }

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field("MR0417");
        CallApiImage(values, imgg, x);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("full_profile", path);
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        Log.v("full_profile", yy + "");
        return yy;
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }

    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = api_interface.uploadimg(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());
                //uploading.setText("Uploading "+String.valueOf(count)+"/"+String.valueOf(count_check));

                try {
                    if (response.isSuccessful()) {


                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            ModelDynamicView mm = array_view.get(x);
                            Log.v("printing_dynamic_cou", adp_view.DynamicCount + "    xxx" + x);
                            mm.setUpload_sv(js.getString("url"));
                            adp_view.notifyDataSetChanged();

                            if (adp_view.DynamicCount == x) {
                                saveValueEntry();
                            }
                        }


                    }


                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }


    public void saveValueEntry() {
        JSONObject finaljs =null;
        JSONArray jsonAddtnlCtrls = new JSONArray();

        if (array_view.size() != 0) {
            for (int i = 0; i < array_view.size(); i++) {
                JSONObject js = null;
                    ModelDynamicView mm = array_view.get(i);

                    try {
                        js = new JSONObject();
                        js.put("sfCode", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                        js.put("DivCode", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
                        js.put("ProfType",mm.getType());
                        js.put("DrCat",cat_code);
                        js.put("CustCode",selectedProductCode);
                        js.put("ctrl_id", mm.getViewid());
                        js.put("creat_id", mm.getCreation_id());

                        if (mm.getViewid().equalsIgnoreCase("8") || mm.getViewid().equalsIgnoreCase("9")
                                || mm.getViewid().equalsIgnoreCase("12") || mm.getViewid().equalsIgnoreCase("13")) {
                            mm.getA_list();
                            if (mm.getA_list().contains(new PopFeed(true))) {
                                String name = "", code = "";
                                for (int k = 0; k < mm.getA_list().size(); k++) {
                                    PopFeed pf = mm.getA_list().get(k);
                                    if (pf.isClick()) {
                                        if (!mm.getViewid().equalsIgnoreCase("9")) {
                                            name = name + "," + pf.getTxt();
                                            code = code + "," + pf.getCode();
                                            k = mm.getA_list().size();
                                            break;
                                        } else {
                                            name = name + "," + pf.getTxt();
                                            code = code + "," + pf.getCode();
                                        }

                                    }
                                }
                                js.put("values", name);
                                js.put("codes", code);
                            }
                        } else {
                            if (mm.getViewid().equalsIgnoreCase("10"))
                                js.put("values", mm.getUpload_sv());
                            else
                                js.put("values", mm.getValue());
                            js.put("codes", "");
                        }
                        if (mm.getViewid().equalsIgnoreCase("17")) {
                            js.put("values", mm.getLatValue());
                            js.put("codes", mm.getLngValue());
                        } else if (mm.getViewid().equalsIgnoreCase("7")) {
                            js.put("values", mm.getValue());
                            js.put("codes", mm.getTvalue());
                        }

                    } catch (Exception e) {
                    }
                jsonAddtnlCtrls.put(js);
            }

            }

        try {
            finaljs=new JSONObject();
            finaljs.put("AdditionalCtrls",jsonAddtnlCtrls);
            finaljs.put("ContactPerson", "");
            finaljs.put("CustCode", selectedProductCode);
            finaljs.put("CustName", tv_drName.getText().toString());
            finaljs.put("DrAdd1", at_addr.getText().toString());
            finaljs.put("DrAdd2", "");
            finaljs.put("DrAdd3", "");
            finaljs.put("DrAdd4", "");
            finaljs.put("DrAdd5", "");
            finaljs.put("DrCat",cat_code);
            finaljs.put("DrClass", "");
            finaljs.put("DrDOBD", et_dobd.getText().toString());
            finaljs.put("DrDOBM", et_dobm.getText().toString());
            finaljs.put("DrDOBY", et_doby.getText().toString());
            finaljs.put("DrDOWD", et_dowd.getText().toString());
            finaljs.put("DrDOWM", et_dowm.getText().toString());
            finaljs.put("DrDOWY", et_dowy.getText().toString());
            finaljs.put("DrEmail",at_email.getText().toString() );
            finaljs.put("DrHosp", "");
            finaljs.put("DrHospNm", "");
            finaljs.put("DrMob",at_mobile.getText().toString() );
            finaljs.put("DrPhone", at_phone.getText().toString());
            finaljs.put("DrQual", qual_code);
            finaljs.put("DrSpec",spec_code );
            finaljs.put("DrTar", target);
            finaljs.put("DrType", type);

            JsonArray jProducts=new JsonArray();
//            for(int i=0;i<jProducts.size();i++)
//            {
//                JSONObject jsonObject=new JSONObject();
//                jsonObject.put("Code", "");
//                jsonObject.put("Name", "");
//            }

            finaljs.put("Products",jProducts);

            finaljs.put("ProfType","D");

            JsonArray jvisitDays=new JsonArray();
            finaljs.put("VisitDays",jvisitDays);
            JsonArray jvstSession=new JsonArray();
            finaljs.put("VstSess",jvstSession);
            JsonArray jvstAvgDay=new JsonArray();
            finaljs.put("vstAvgPDy",jvstAvgDay);
            JsonArray jvstecoPats=new JsonArray();
            finaljs.put("vstEcoPats",jvstecoPats);

        }catch (Exception e)
        {

        }
        Log.v("printing_dr_profile", finaljs.toString());
        callsave(finaljs.toString());
    }

    public void callsave(String json) {

        try {

            Call<ResponseBody> approval = api_interface.svdcrProfile(json);

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
                                commonFun();
                                Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.datasave), Toast.LENGTH_SHORT).show();
                                array_view.clear();
                                adp_view.notifyDataSetChanged();
                            } else {
                                progressDialog.dismiss();
                                commonFun();
                                Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.cannotload), Toast.LENGTH_SHORT).show();
                            }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    ImageFilePath filepath = new ImageFilePath();
                    String fullPath = filepath.getPath(ProfilingActivity.this, uri);
                    Log.v("file_path_are", fullPath + "print");
                    String PathHolder = data.getData().getPath();
                    /*String filePath = getImageFilePath(data);
                    Log.v("file_path_are",filePath);*/
                    if (fullPath == null)
                        Toast.makeText(ProfilingActivity.this, getResources().getString(R.string.filenot_support), Toast.LENGTH_LONG).show();
                    else {
                        ModelDynamicView mm = array_view.get(pos_upload_file);
                        mm.setValue(fullPath);
                        adp_view.notifyDataSetChanged();
                    }

                    commonFun();

                }
                break;

        }
    }


    public boolean validationOfField() {
        boolean val = true;
        for (int i = 0; i < array_view.size(); i++) {
            ModelDynamicView mm = array_view.get(i);
            if (mm.getMandatory().equalsIgnoreCase("1") && (!mm.getViewid().equalsIgnoreCase("22"))) {
                if (mm.getViewid().equalsIgnoreCase("5") || mm.getViewid().equalsIgnoreCase("7")) {
                    if (TextUtils.isEmpty(mm.getTvalue()) || TextUtils.isEmpty(mm.getValue()))
                        return false;
                }else if(mm.getViewid().equalsIgnoreCase("17"))
                {
                    if (TextUtils.isEmpty(mm.getLatValue()) && TextUtils.isEmpty(mm.getLngValue()))
                        return false;
                } else {
                    if (TextUtils.isEmpty(mm.getValue()))
                        return false;
                }
            }
        }
        return val;
    }

    public int spiltTime(String val) {
        String v = val.substring(0, val.indexOf(":"));
        return Integer.parseInt(v);
    }

    public int spiltMin(String val) {
        String v = val.substring(val.indexOf(":") + 1);
        return Integer.parseInt(v);
    }

    public long dateDifference(String d1, String d2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            Date date1 = simpleDateFormat.parse(d1 + " 00:00:00");
            Date date2 = simpleDateFormat.parse(d2 + " 00:00:00");

            long different = date2.getTime() - date1.getTime();
            Log.v("priting_date_diffence", different + "");
            return different;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

