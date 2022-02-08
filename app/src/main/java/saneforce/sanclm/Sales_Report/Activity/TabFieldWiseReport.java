package saneforce.sanclm.Sales_Report.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Adapter.MultiCheckDivAdaptor;
import saneforce.sanclm.Sales_Report.Adapter.MulticheckFieldAdapter;
import saneforce.sanclm.Sales_Report.Adapter.PSalesFieldAdapter;
import saneforce.sanclm.Sales_Report.Interface.OnMultiCheckListener;
import saneforce.sanclm.Sales_Report.Interface.onMultiFiledListener;
import saneforce.sanclm.Sales_Report.Modelclass.Designation_List;
import saneforce.sanclm.Sales_Report.Modelclass.MultiCheckDivModel;
import saneforce.sanclm.Sales_Report.Modelclass.pSalesFieldModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TabFieldWiseReport extends Fragment {
    TextView tv_field_wise_display_date;
    FrameLayout LayoutFrame_Field_Wise;
    ImageView btn_field_wise_div;
    RecyclerView rv_field_wise;
    Button btn_field_wise_hq,btn_clear;
    public static ArrayList<pSalesFieldModel> PrimarySalesFieldArrayList = new ArrayList<pSalesFieldModel>();
    PSalesFieldAdapter mPrimarySalesFieldAdapter;
    MulticheckFieldAdapter multicheckFieldAdapter;
    //List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear;

    ArrayList<MultiCheckDivModel> ListSelectedHQ = new ArrayList<MultiCheckDivModel>();
    ArrayList<MultiCheckDivModel> Array_HQ_List = new ArrayList<MultiCheckDivModel>();
    ArrayList<MultiCheckDivModel> HQ_List = new ArrayList<MultiCheckDivModel>();
    ArrayList<String> Selected_HQ_List = new ArrayList<>();

    ArrayList<Designation_List> ListSelectedField = new ArrayList<Designation_List>();
    ArrayList<Designation_List> Array_Field_List = new ArrayList<Designation_List>();
    ArrayList<Designation_List> Field_List = new ArrayList<Designation_List>();
    ArrayList<String> Selected_Field_List = new ArrayList<>();
//    MultiCheckDivAdaptor multiCheckDivisionAdapt;

    String SelectedFieldCode = "";
    String SelectedField = "";
    String SelectedHQ = "";
    MultiCheckDivAdaptor multiCheckDivisionAdapt;

    String sf_code,div_Code,db_connPath,sf_name;
    CommonSharedPreference mCommonSharedPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_field_wise_report, container, false);
        LayoutFrame_Field_Wise = view.findViewById(R.id.LayoutFrame_HQ_Wise);
        tv_field_wise_display_date = view.findViewById(R.id.tv_field_wise_display_date);
        btn_field_wise_hq = view.findViewById(R.id.btn_field_wise_hq);
        btn_clear = view.findViewById(R.id.btn_clear);
        btn_field_wise_div = view.findViewById(R.id.btn_field_wise_div);
        rv_field_wise = view.findViewById(R.id.rv_field_wise);

        mCommonSharedPreference=new CommonSharedPreference(getContext());
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

//        sqlLite sqlLite;
//        sqlLite = new sqlLite(getActivity());
//        Cursor cursor = sqlLite.getAllLoginData();
//        if (cursor.moveToFirst()) {
//            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//        }
//        cursor.close();
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<CustomerMe>>() {
//        }.getType();
//        CustomerMeList = gson.fromJson(curval, type);
//        Log.d("ss_dashboard_1", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode() + " " + CustomerMeList.get(0).getSFName());

//        parseFieldforceJsonData();
        parseDivisionJsonData();

        btn_field_wise_hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShowAlertField();
                ShowAlertHQ();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data();
            }
        });

        mPrimarySalesFieldAdapter = new PSalesFieldAdapter(getActivity(),PrimarySalesFieldArrayList,sf_code,TabFieldWiseReport.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_field_wise.setLayoutManager(mLayoutManager);
        rv_field_wise.setItemAnimator(new DefaultItemAnimator());
        rv_field_wise.setAdapter(mPrimarySalesFieldAdapter);
//        GetPrimarySalesFieldWiseReport();
        load_data();
        return view;
    }

    public void  load_data(){
        String TD_Data = "";
        String DisplayReportDate = "";
        Dcrdatas.SelectedSF="";
        if (Dcrdatas.primarysalesselectedtdtag == 0) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_MTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesFieldWiseReportDataAvail_MTD(getActivity())) {
                GetPrimarySalesFieldWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_MTD(getActivity());
                LoadPrimarySalesFieldWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_QTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesFieldWiseReportDataAvail_QTD(getActivity())) {
                GetPrimarySalesFieldWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_QTD(getActivity());
                LoadPrimarySalesFieldWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_YTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesFieldWiseReportDataAvail_YTD(getActivity())) {
                GetPrimarySalesFieldWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_YTD(getActivity());
                LoadPrimarySalesFieldWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
            GetPrimarySalesFieldWiseReport();
            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_DTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesFieldWiseReportDataAvail_DTD(getActivity())) {
                GetPrimarySalesFieldWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_DTD(getActivity());
                LoadPrimarySalesFieldWiseReport(TD_Data);
            }
        }
        tv_field_wise_display_date.setText(DisplayReportDate);
    }

    public void ShowAlertHQ() {
        parseDivisionJsonData();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.saledd, null);
        LinearLayout linearLayout = ( LinearLayout ) dialogView.findViewById(R.id.alloption);
        final EditText editText = ( EditText ) dialogView.findViewById(R.id.search_multiple);
        TextView tv = ( TextView ) dialogView.findViewById(R.id.tv_searchheader_mul);
        CheckBox check = dialogView.findViewById(R.id.checkBox_mulcheck);
        final RecyclerView rv_list = ( RecyclerView ) dialogView.findViewById(R.id.dailogrv_list_mul);
        linearLayout.setVisibility(View.VISIBLE);
        dialogBuilder.setView(dialogView);
        editText.setHint("Search" + " " + "Headquarters");
        tv.setText("Select" + " " + "Headquarters");
        if (Dcrdatas.IsSelectAll == 1) {
            Dcrdatas.isselected = true;
            check.setChecked(true);
        } else {
            Dcrdatas.isselected = false;
            check.setChecked(false);
        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dcrdatas.productFrom = "1";
                if (Dcrdatas.checkin == 1) {
                    Dcrdatas.isselected = true;
                    Dcrdatas.checkin = 0;
                } else {
                    Dcrdatas.isselected = false;
                    Dcrdatas.checkin = 1;
                }
                multiCheckDivisionAdapt.notifyDataSetChanged();
            }
        });
        multiCheckDivisionAdapt = new MultiCheckDivAdaptor(HQ_List, check, rv_list, getActivity(), new OnMultiCheckListener() {
            @Override
            public void OnMultiCheckDivListener_Add(MultiCheckDivModel classGroup) {
                if (classGroup != null) {
                    ListSelectedHQ.add(classGroup);
                }
            }

            @Override
            public void OnMultiCheckDivListener_Remove(MultiCheckDivModel classGroup) {
                if (classGroup != null) {
                    ListSelectedHQ.remove(classGroup);
                }
            }
        });

        dialogBuilder.setNegativeButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ListSelectedHQ.size() > 0) {
                    String SelectHQ = "";
                    editText.setText("");
                    for (int i = 0; i < HQ_List.size(); i++) {
                        if (HQ_List.get(i).isChecked()) {
                            if (SelectHQ.equals("")) {
                                SelectHQ = HQ_List.get(i).getStringID();
                            } else {
                                SelectHQ = SelectHQ + "," + HQ_List.get(i).getStringID();
                            }
                        }
                    }
                    SelectedHQ = SelectHQ + ",";
                    Dcrdatas.primarysaleshqselectedHQ = SelectedHQ;
                    Dcrdatas.primarysaleshqselectedHQtag = 1;
                    FetchHQWisePrimarySalesHQWiseReport();
                }
                Dcrdatas.checkin=1;
                Dcrdatas.isselected=false;
                editText.setText("");
                dialog.dismiss();
            }
        });
        LinearLayoutManager LayoutManagerpocsale = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(LayoutManagerpocsale);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.setAdapter(multiCheckDivisionAdapt);
        final AlertDialog alertDialog = dialogBuilder.create();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text1 = editText.getText().toString().toLowerCase(Locale.getDefault());
                FilterHQ(text1);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        alertDialog.show();
    }

    public void FilterHQ(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        HQ_List.clear();
        if (charText.length() == 0) {
            HQ_List.addAll(Array_HQ_List);
        } else {
            for (int i = 0; i < Array_HQ_List.size(); i++) {
                final String text = Array_HQ_List.get(i).getStringName().toLowerCase();
                if (text.contains(charText)) {
                    HQ_List.add(Array_HQ_List.get(i));
                }
            }
        }
        multiCheckDivisionAdapt.notifyDataSetChanged();
    }


    public void ShowAlertField() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.saledd, null);
        LinearLayout linearLayout = ( LinearLayout ) dialogView.findViewById(R.id.alloption);
        final EditText editText = ( EditText ) dialogView.findViewById(R.id.search_multiple);
        TextView tv = ( TextView ) dialogView.findViewById(R.id.tv_searchheader_mul);
        CheckBox check = dialogView.findViewById(R.id.checkBox_mulcheck);
        final RecyclerView rv_list = ( RecyclerView ) dialogView.findViewById(R.id.dailogrv_list_mul);
        linearLayout.setVisibility(View.VISIBLE);
        dialogBuilder.setView(dialogView);
        editText.setHint("Search" + " " + "FieldForce");
        tv.setText("Select" + " " + "FieldForce");
//        if (Dcrdatas.IsSelectAll == 1) {
//            Dcrdatas.isselected = true;
//            check.setChecked(true);
//        } else {
//            Dcrdatas.isselected = false;
//            check.setChecked(false);
//        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Dcrdatas.productFrom = "1";
//                if (Dcrdatas.checkin == 1) {
//                    Dcrdatas.isselected = true;
//                    Dcrdatas.checkin = 0;
//                } else {
//                    Dcrdatas.isselected = false;
//                    Dcrdatas.checkin = 1;
//                }
//                multicheckFieldAdapter.notifyDataSetChanged();
            }
        });
        multicheckFieldAdapter = new MulticheckFieldAdapter(Field_List,getActivity(), new onMultiFiledListener() {
            @Override
            public void OnMultiCheckDivListener_Add(Designation_List classGroup) {
                if (classGroup != null) {
                    ListSelectedField.add(classGroup);
                }
            }

            @Override
            public void OnMultiCheckDivListener_Remove(Designation_List classGroup) {
                if (classGroup != null) {
                    ListSelectedField.remove(classGroup);
                }
            }
        });

        dialogBuilder.setNegativeButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ListSelectedField.size() > 0) {
                    String SelectHQ = "";
                    for (int i = 0; i < Field_List.size(); i++) {
                        if (Field_List.get(i).isChecked()) {

                        }
                    }
                    SelectedField = SelectHQ + ",";
                    Dcrdatas.primarysaleshqselectedHQ = SelectedField;
                    Dcrdatas.primarysaleshqselectedHQtag = 1;
                    FetchHQWisePrimarySalesHQWiseReport();
                }
                Dcrdatas.checkin=1;
                Dcrdatas.isselected=false;
                dialog.dismiss();
            }
        });
        LinearLayoutManager LayoutManagerpocsale = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(LayoutManagerpocsale);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.setAdapter(multicheckFieldAdapter);
        final AlertDialog alertDialog = dialogBuilder.create();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text1 = editText.getText().toString().toLowerCase(Locale.getDefault());
                FilterField(text1);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        alertDialog.show();
    }

    public void FilterField(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Field_List.clear();
        if (charText.length() == 0) {
            Field_List.addAll(Array_Field_List);
        } else {
            for (int i = 0; i < Array_Field_List.size(); i++) {
                final String text = Array_Field_List.get(i).getDesignation_Name().toLowerCase();
                if (text.contains(charText)) {
                    Field_List.add(Array_Field_List.get(i));
                }
            }
        }
        mPrimarySalesFieldAdapter.notifyDataSetChanged();
    }

    private void LoadPrimarySalesFieldWiseReport(String TD_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        if (!TD_Data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(TD_Data);
                if (jsonArray.length() > 0) {
                    PrimarySalesFieldArrayList.clear();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String SF_Code = jsonObject.getString("sf_code");
                        String Reporting_to = jsonObject.getString("Reporting_To");
                        if ((sf_code.equalsIgnoreCase(SF_Code)) || (sf_code.equalsIgnoreCase(Reporting_to))){
                                String sl_no = jsonObject.getString("sno");
                                String Division_Code = jsonObject.getString("Division_Code");
                                String Division_Name = jsonObject.getString("Division_Name");
                                String SF_Code1 = jsonObject.getString("sf_code");
                                String SF_Name = jsonObject.getString("sf_name");
                                String Designation = jsonObject.getString("sf_Designation_Short_Name");
                                String Sf_hq = jsonObject.getString("sf_hq");
                                String SF_type = jsonObject.getString("sf_type");
                                String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                                String SF_act_Flag = jsonObject.getString("sf_TP_Active_Flag");
                                String Reporting_to1 = jsonObject.getString("Reporting_To");
                                String Reporting_to_name = jsonObject.getString("Reporting_To_Name");
                                String Reporting_to_desig = jsonObject.getString("Reporting_To_Desig");
                                String Reporting_hq = jsonObject.getString("Reporting_To_HQ");
                                String primary = jsonObject.getString("Sl_Primary");
                                String Target = jsonObject.getString("Sl_Target");
                                String Growth = jsonObject.getString("Sl_growth");
                                String Achie = jsonObject.getString("Sl_Achieve");
                                String selectedValue = jsonObject.getString("selectedValue");
                                String Pcpm = jsonObject.getString("Sl_PCPM");

                                pSalesFieldModel mPrimarySalesDetailModel = new pSalesFieldModel(sl_no, Division_Code, Division_Name,SF_Code1,SF_Name,Designation,
                                        Sf_hq,SF_type,SF_Cat_Code,SF_act_Flag,Reporting_to1,Reporting_to_name,Reporting_to_desig,Reporting_hq,primary,Target,Growth,
                                        Achie,selectedValue,Pcpm);
                                PrimarySalesFieldArrayList.add(mPrimarySalesDetailModel);
                                mPrimarySalesFieldAdapter.notifyDataSetChanged();
                        }
                    }
                    Log.d("ListCount_1", String.valueOf(PrimarySalesFieldArrayList.size()));
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } else {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    private void GetPrimarySalesFieldWiseReport() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            String divcode = "";
            if (div_Code.contains(",")) {
                divcode = div_Code.replace(div_Code.substring(div_Code.length() - 1), "");
            } else {
                divcode = div_Code;
            }

            Map<String, String> QryParam = new HashMap<>();
           // QryParam.put("axn", "get/primary_fieldforce");
            QryParam.put("divisionCode", divcode);
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF",sf_code);
            //For MTD :
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_MTD(getActivity());
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_MTD(getActivity());
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_MTD(getActivity());
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_MTD(getActivity());
            }
            //For QTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_QTD(getActivity());
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_QTD(getActivity());
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_QTD(getActivity());
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_QTD(getActivity());
            }
            //For YTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_YTD(getActivity());
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_YTD(getActivity());
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_YTD(getActivity());
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_YTD(getActivity());
            }
            //For DTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_DTD(getActivity());
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_DTD(getActivity());
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_DTD(getActivity());
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_DTD(getActivity());
            }
            QryParam.put("fmonth", FromMonth);
            QryParam.put("fyear", FromYear);
            QryParam.put("tomonth", ToMonth);
            QryParam.put("toyear", ToYear);
            Log.e("Primary_Sales_HQ_1", QryParam.toString());

            Call<JsonArray> call = apiService.getFieldwiseDataAsJArray( QryParam);

            Log.d("Primary_Sales_HQ_2", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("Primary_Sales_HQ_3", response.body() + " " + response.body().toString());
                        Log.d("Primary_Sales_HQ_4", response.toString());
                        if (!response.body().toString().isEmpty()) {
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    PrimarySalesFieldArrayList.clear();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        String SF_Code = jsonObject.getString("sf_code");
                                        String Reporting_to = jsonObject.getString("Reporting_To");
                                        if ((sf_code.equalsIgnoreCase(SF_Code)) || (sf_code.equalsIgnoreCase(Reporting_to))) {
                                            String sl_no = jsonObject.getString("sno");
                                            String Division_Code = jsonObject.getString("Division_Code");
                                            String Division_Name = jsonObject.getString("Division_Name");
                                            String SF_Code1 = jsonObject.getString("sf_code");
                                            String SF_Name = jsonObject.getString("sf_name");
                                            String Designation = jsonObject.getString("sf_Designation_Short_Name");
                                            String Sf_hq = jsonObject.getString("sf_hq");
                                            String SF_type = jsonObject.getString("sf_type");
                                            String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                                            String SF_act_Flag = jsonObject.getString("sf_TP_Active_Flag");
                                            String Reporting_to1 = jsonObject.getString("Reporting_To");
                                            String Reporting_to_name = jsonObject.getString("Reporting_To_Name");
                                            String Reporting_to_desig = jsonObject.getString("Reporting_To_Desig");
                                            String Reporting_hq = jsonObject.getString("Reporting_To_HQ");
                                            String primary = jsonObject.getString("Sl_Primary");
                                            String Target = jsonObject.getString("Sl_Target");
                                            String Growth = jsonObject.getString("Sl_growth");
                                            String Achie = jsonObject.getString("Sl_Achieve");
                                            String selectedValue = jsonObject.getString("selectedValue");
                                            String Pcpm = jsonObject.getString("Sl_PCPM");

                                            pSalesFieldModel mPrimarySalesDetailModel = new pSalesFieldModel(sl_no, Division_Code, Division_Name, SF_Code1, SF_Name, Designation,
                                                    Sf_hq, SF_type, SF_Cat_Code, SF_act_Flag, Reporting_to1, Reporting_to_name, Reporting_to_desig, Reporting_hq, primary, Target, Growth,
                                                    Achie, selectedValue, Pcpm);
                                            PrimarySalesFieldArrayList.add(mPrimarySalesDetailModel);
                                            mPrimarySalesFieldAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    if (Dcrdatas.primarysalesselectedtdtag == 0) {
                                        Shared_Common_Pref.putPrimarySalesFieldReport_MTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                                        Shared_Common_Pref.putPrimarySalesFieldReport_QTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                                        Shared_Common_Pref.putPrimarySalesFieldReport_YTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                                        Shared_Common_Pref.putPrimarySalesFieldReport_DTD(getActivity(), response.body().toString(), true);
                                    }
//                                    parseFieldforceJsonData();
                                    parseDivisionJsonData();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                } else {
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }
                    } else {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Log.e("Primary_Sales_HQ_5 TAG", "onFailure : " + t.toString());
                    Toast.makeText(getActivity(), "Something went wrong  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseDivisionJsonData() {
        try {
            String jsonResponse = "";
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_MTD(getActivity());
            } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_QTD(getActivity());
            } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
               jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_YTD(getActivity());
            } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_DTD(getActivity());
            }
            String Div="";
            if (div_Code.contains(",")) {
                Div = div_Code.replace(",", "");
            } else {
                Div = div_Code;
            }
            if (!jsonResponse.equalsIgnoreCase("")){
                JSONArray jsonArray = new JSONArray(jsonResponse);
                HQ_List.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                    String SF_Cat_Code = jsonObject1.getString("SF_Cat_Code");
//                    String Division_Code = jsonObject1.getString("Division_Code");
                    String sf_code = jsonObject1.getString("sf_code");
                    if (!HQ_List.contains(sf_code)) {
                        String sf_name = jsonObject1.getString("sf_name");
                        String sf_Designation_Short_Name = jsonObject1.getString("sf_Designation_Short_Name");
                        String sf_hq = jsonObject1.getString("sf_hq");
                        String  sfhq_data = sf_name + " - " + sf_Designation_Short_Name + " - " + sf_hq;

                        MultiCheckDivModel mul = new MultiCheckDivModel(sf_code, sfhq_data, false);
                        HQ_List.add(mul);
                    }
                }

                Collections.sort(HQ_List, new Comparator<MultiCheckDivModel>() {
                    @Override
                    public int compare(MultiCheckDivModel lhs, MultiCheckDivModel rhs) {
                        return lhs.getStringName().compareTo(rhs.getStringName());
                    }
                });

                Log.d("ListCount_3", String.valueOf(HQ_List.size()));
                Array_HQ_List.clear();
                Array_HQ_List.addAll(HQ_List);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void parseFieldforceJsonData() {
//        try {
//            String jsonResponse = "";
//            if (Dcrdatas.primarysalesselectedtdtag == 0) {
//                jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_MTD(getActivity());
//            } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
//                jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_QTD(getActivity());
//            } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
//                jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_YTD(getActivity());
//            } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
//                jsonResponse = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_DTD(getActivity());
//            }
//            String Div;
//            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
//                Div = CustomerMeList.get(0).getDivisionCode().replace(",", "");
//            } else {
//                Div = CustomerMeList.get(0).getDivisionCode();
//            }
//            if (!jsonResponse.equalsIgnoreCase("")) {
//                JSONArray jsonArray = new JSONArray(jsonResponse);
//                Field_List.clear();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                    String designation = jsonObject1.getString("sf_Designation_Short_Name");
//
//                    Designation_List designationList =new Designation_List(designation,false);
//                    Field_List.add(designationList);
//                }
//                Log.d("ListCount_3", String.valueOf(Field_List.size()));
//                Array_Field_List.clear();
//                Array_Field_List.addAll(Field_List);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = ( ConnectivityManager ) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private void FetchHQWisePrimarySalesHQWiseReport() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        String TD_Data = "";
        if (Dcrdatas.primarysalesselectedtdtag == 0) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_MTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_QTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_YTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_DTD(getActivity());
        }
        Selected_HQ_List.clear();
        String HQCodesDetails[] = Dcrdatas.primarysaleshqselectedHQ.split(",");
        for (int i = 0; i < HQCodesDetails.length; i++) {
            Selected_HQ_List.add(HQCodesDetails[i]);
        }

        if (!TD_Data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(TD_Data);
                if (jsonArray.length() > 0) {
                    PrimarySalesFieldArrayList.clear();
//                    mPrimarySalesFieldAdapter.notifyDataSetChanged();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                        String SF_Code = jsonObject.getString("sf_code");
                        if (Selected_HQ_List.contains(SF_Code)) {
                            String sl_no = jsonObject.getString("sno");
                            String Division_Code = jsonObject.getString("Division_Code");
                            String Division_Name = jsonObject.getString("Division_Name");
                            String SF_Code1 = jsonObject.getString("sf_code");
                            String SF_Name = jsonObject.getString("sf_name");
                            String Designation = jsonObject.getString("sf_Designation_Short_Name");
                            String Sf_hq = jsonObject.getString("sf_hq");
                            String SF_type = jsonObject.getString("sf_type");
                            String SF_act_Flag = jsonObject.getString("sf_TP_Active_Flag");
                            String Reporting_to = jsonObject.getString("Reporting_To");
                            String Reporting_to_name = jsonObject.getString("Reporting_To_Name");
                            String Reporting_to_desig = jsonObject.getString("Reporting_To_Desig");
                            String Reporting_hq = jsonObject.getString("Reporting_To_HQ");
                            String primary = jsonObject.getString("Sl_Primary");
                            String Target = jsonObject.getString("Sl_Target");
                            String Growth = jsonObject.getString("Sl_growth");
                            String Achie = jsonObject.getString("Sl_Achieve");
                            String selectedValue = jsonObject.getString("selectedValue");
                            String Pcpm = jsonObject.getString("Sl_PCPM");

                            pSalesFieldModel mPrimarySalesDetailModel = new pSalesFieldModel(sl_no, Division_Code, Division_Name, SF_Code1, SF_Name, Designation,
                                    Sf_hq, SF_type, SF_Cat_Code, SF_act_Flag, Reporting_to, Reporting_to_name, Reporting_to_desig, Reporting_hq, primary, Target, Growth,
                                    Achie, selectedValue, Pcpm);
                            PrimarySalesFieldArrayList.add(mPrimarySalesDetailModel);
                            mPrimarySalesFieldAdapter.notifyDataSetChanged();
                        }
                    }
                    Log.d("ListCount_1", String.valueOf(PrimarySalesFieldArrayList.size()));
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } else {
                    PrimarySalesFieldArrayList.clear();
                    mPrimarySalesFieldAdapter.notifyDataSetChanged();
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            PrimarySalesFieldArrayList.clear();
            mPrimarySalesFieldAdapter.notifyDataSetChanged();
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    public void subordinatelist(String sf_code){
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        String TD_Data = "";
        if (Dcrdatas.primarysalesselectedtdtag == 0) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_MTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_QTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_YTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
            TD_Data = Shared_Common_Pref.getPrimarySalesFieldWiseReportData_DTD(getActivity());
        }

        if (!TD_Data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(TD_Data);
                if (jsonArray.length() > 0) {
                    PrimarySalesFieldArrayList.clear();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String SF_Code = jsonObject.getString("sf_code");
                        String Reporting_to = jsonObject.getString("Reporting_To");
                        if ((sf_code.equalsIgnoreCase(SF_Code)) || (sf_code.equalsIgnoreCase(Reporting_to))){
                            String sl_no = jsonObject.getString("sno");
                            String Division_Code = jsonObject.getString("Division_Code");
                            String Division_Name = jsonObject.getString("Division_Name");
                            String SF_Code1 = jsonObject.getString("sf_code");
                            String SF_Name = jsonObject.getString("sf_name");
                            String Designation = jsonObject.getString("sf_Designation_Short_Name");
                            String Sf_hq = jsonObject.getString("sf_hq");
                            String SF_type = jsonObject.getString("sf_type");
                            String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                            String SF_act_Flag = jsonObject.getString("sf_TP_Active_Flag");
                            String Reporting_to1 = jsonObject.getString("Reporting_To");
                            String Reporting_to_name = jsonObject.getString("Reporting_To_Name");
                            String Reporting_to_desig = jsonObject.getString("Reporting_To_Desig");
                            String Reporting_hq = jsonObject.getString("Reporting_To_HQ");
                            String primary = jsonObject.getString("Sl_Primary");
                            String Target = jsonObject.getString("Sl_Target");
                            String Growth = jsonObject.getString("Sl_growth");
                            String Achie = jsonObject.getString("Sl_Achieve");
                            String selectedValue = jsonObject.getString("selectedValue");
                            String Pcpm = jsonObject.getString("Sl_PCPM");

                            pSalesFieldModel mPrimarySalesDetailModel = new pSalesFieldModel(sl_no, Division_Code, Division_Name,SF_Code1,SF_Name,Designation,
                                    Sf_hq,SF_type,SF_Cat_Code,SF_act_Flag,Reporting_to1,Reporting_to_name,Reporting_to_desig,Reporting_hq,primary,Target,Growth,
                                    Achie,selectedValue,Pcpm);
                            PrimarySalesFieldArrayList.add(mPrimarySalesDetailModel);
                            mPrimarySalesFieldAdapter.notifyDataSetChanged();
                        }
                    }
                    Log.d("ListCount_1", String.valueOf(PrimarySalesFieldArrayList.size()));
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } else {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

}