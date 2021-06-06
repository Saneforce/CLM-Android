package saneforce.sanclm.activities;

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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.adapter_class.Custom_DCR_GV_Dr_Contents;
import saneforce.sanclm.adapter_class.DCR_GV_Selection_adapter;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.DCRDRCallsSelection;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DCRCallSelectionFilter;


public class ProfilingActivity extends AppCompatActivity
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
    RelativeLayout rl_dcr_precall_analysisMain , dcr_drselection_gridview;
    String SF_Code,mMydayWtypeCd,subSfCode;
    String selectedProductCode="";
    TextView tv_drName;
    Button btn_re_select_doctor;
    EditText et_searchDr;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilings);
        dbh = new DataBaseHandler(getApplicationContext());
        commonUtilsMethods = new CommonUtilsMethods(ProfilingActivity.this);
        mCommonSharedPreference = new CommonSharedPreference(getApplicationContext());
        SF_Code= mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        mMydayWtypeCd = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE);
        subSfCode=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        GridView gridView = (GridView) findViewById(R.id.gridview_dcrselect);

        rl_dcr_precall_analysisMain = (RelativeLayout) findViewById(R.id.rl_dcr_precall_analysis_main) ;
        dcr_drselection_gridview= (RelativeLayout) findViewById(R.id.rl_dcr_drgrid_selection) ;
        btn_re_select_doctor = (Button) findViewById(R.id.btn_reselect) ;
        tv_drName = (TextView)  findViewById(R.id.tv_drName) ;
        et_searchDr = (EditText) findViewById(R.id.et_searchDr);
        et_searchDr.setText("");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        dcr_drselection_gridview.setVisibility(View.VISIBLE);
        spinner=(AppCompatSpinner) findViewById(R.id.spinner);

        List<String> selectPosition = new ArrayList<>();
      //  selectPosition.add(0, "Select a player from the list");
        selectPosition.add("Listed Dr.");
        selectPosition.add("Chemist");
        selectPosition.add("Stockist");
        selectPosition.add("Unlisted Dr.");
        dcrdrCallsSelection=new DCRDRCallsSelection();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, selectPosition);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

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
                    }

                    }

