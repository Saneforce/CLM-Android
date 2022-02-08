package saneforce.sanclm.Sales_Report.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Adapter.MultiCheckDivAdaptor;
import saneforce.sanclm.Sales_Report.Adapter.PSalesHQAdapter;
import saneforce.sanclm.Sales_Report.Interface.OnMultiCheckListener;
import saneforce.sanclm.Sales_Report.Modelclass.MultiCheckDivModel;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesHQModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;

public class TabHQWiseReport extends Fragment {
    TextView tv_hq_wise_display_date;
    FrameLayout LayoutFrame_HQ_Wise;
    ImageView btn_hq_wise_div;
    RecyclerView rv_hq_wise;
    Button btn_hq_wise_hq;
    String sf_code,div_Code,db_connPath,sf_name;
    CommonSharedPreference mCommonSharedPreference;

    public static ArrayList<PSalesHQModel> PrimarySalesHQArrayList = new ArrayList<PSalesHQModel>();

    public PSalesHQAdapter mPrimarySalesHQWiseAdapter;

    //    ArrayList<MultiCheckDivModel> ListSelectedDivision = new ArrayList<MultiCheckDivModel>();
//    ArrayList<MultiCheckDivModel> Array_Division_List = new ArrayList<MultiCheckDivModel>();
//    ArrayList<MultiCheckDivModel> Division_List = new ArrayList<MultiCheckDivModel>();
    ArrayList<String> Selected_Division_List = new ArrayList<>();

    ArrayList<MultiCheckDivModel> ListSelectedHQ = new ArrayList<MultiCheckDivModel>();
    ArrayList<MultiCheckDivModel> Array_HQ_List = new ArrayList<MultiCheckDivModel>();
    ArrayList<MultiCheckDivModel> HQ_List = new ArrayList<MultiCheckDivModel>();
    ArrayList<String> Selected_HQ_List = new ArrayList<>();


    String SelectedDivCode = "";
    String SelectedHQ = "";

    //List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear;
     MultiCheckDivAdaptor multiCheckDivisionAdapt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hq_wise, container, false);
        LayoutFrame_HQ_Wise = view.findViewById(R.id.LayoutFrame_HQ_Wise);
        tv_hq_wise_display_date = view.findViewById(R.id.tv_hq_wise_display_date);
        btn_hq_wise_hq = view.findViewById(R.id.btn_hq_wise_hq);
        btn_hq_wise_div = view.findViewById(R.id.btn_hq_wise_div);
        rv_hq_wise = view.findViewById(R.id.rv_hq_wise);

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

        btn_hq_wise_hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertHQ();
            }
        });

        btn_hq_wise_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShowAlertDivision();
            }
        });

        parseDivisionJsonData();

        mPrimarySalesHQWiseAdapter = new PSalesHQAdapter(PrimarySalesHQArrayList, getActivity(), TabHQWiseReport.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_hq_wise.setLayoutManager(mLayoutManager);
        rv_hq_wise.setItemAnimator(new DefaultItemAnimator());
        rv_hq_wise.setAdapter(mPrimarySalesHQWiseAdapter);

        String TD_Data = "";
        String DisplayReportDate = "";
        if (Dcrdatas.primarysalesselectedtdtag == 0) {
            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_MTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesHQWiseReportDataAvail_MTD(getActivity())) {
                GetPrimarySalesHQWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_MTD(getActivity());
                LoadPrimarySalesHQWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_QTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesHQWiseReportDataAvail_QTD(getActivity())) {
                GetPrimarySalesHQWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_QTD(getActivity());
                LoadPrimarySalesHQWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_YTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesHQWiseReportDataAvail_YTD(getActivity())) {
                GetPrimarySalesHQWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_YTD(getActivity());
                LoadPrimarySalesHQWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_DTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesHQWiseReportDataAvail_DTD(getActivity())) {
                GetPrimarySalesHQWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_DTD(getActivity());
                LoadPrimarySalesHQWiseReport(TD_Data);
            }
        }
        tv_hq_wise_display_date.setText(DisplayReportDate);
        return view;
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = ( ConnectivityManager ) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void GetPrimarySalesHQWiseReport() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService =  RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Map<String, String> QryParam = new HashMap<>();
           // QryParam.put("axn", "get/primary_HQ");
            QryParam.put("divisionCode", div_Code+",");
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
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

            Call<JsonArray> call = apiService.getHqwiseDataAsJArray( QryParam);
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
                                    PrimarySalesHQArrayList.clear();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        String sl_no = jsonObject.getString("sl_no");
                                        String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                                        String HQ_Name = jsonObject.getString("HQ_Name");
                                        String Division_Code = jsonObject.getString("Division_Code");
                                        String Division_Name = jsonObject.getString("Division_Name");
                                        String Primary = jsonObject.getString("Primary");
                                        String PrevPrimary = jsonObject.getString("PrevPrimary");
                                        String Target = jsonObject.getString("Target");
                                        String Achie = jsonObject.getString("Achie");
                                        String Growth = jsonObject.getString("Growth");

                                        PSalesHQModel mPrimarySalesDetailModel = new PSalesHQModel(sl_no, SF_Cat_Code, HQ_Name,
                                                Division_Code, Division_Name, Primary, PrevPrimary, Target, Achie, Growth);
                                        PrimarySalesHQArrayList.add(mPrimarySalesDetailModel);
                                        mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
                                    }
                                    if (Dcrdatas.primarysalesselectedtdtag == 0) {
                                        Shared_Common_Pref.putPrimarySalesHQWiseReport_MTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                                        Shared_Common_Pref.putPrimarySalesHQWiseReport_QTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                                        Shared_Common_Pref.putPrimarySalesHQWiseReport_YTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                                       Shared_Common_Pref.putPrimarySalesHQWiseReport_DTD(getActivity(), response.body().toString(), true);
                                    }
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

    private void LoadPrimarySalesHQWiseReport(String TD_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        if (!TD_Data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(TD_Data);
                if (jsonArray.length() > 0) {
                    PrimarySalesHQArrayList.clear();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String sl_no = jsonObject.getString("sl_no");
                        String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                        String HQ_Name = jsonObject.getString("HQ_Name");
                        String Division_Code = jsonObject.getString("Division_Code");
                        String Division_Name = jsonObject.getString("Division_Name");
                        String Primary = jsonObject.getString("Primary");
                        String PrevPrimary = jsonObject.getString("PrevPrimary");
                        String Target = jsonObject.getString("Target");
                        String Achie = jsonObject.getString("Achie");
                        String Growth = jsonObject.getString("Growth");

                        PSalesHQModel mPrimarySalesDetailModel = new PSalesHQModel(sl_no, SF_Cat_Code, HQ_Name,
                                Division_Code, Division_Name, Primary, PrevPrimary, Target, Achie, Growth);
                        PrimarySalesHQArrayList.add(mPrimarySalesDetailModel);
                        mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
                    }
                    Log.d("ListCount_1", String.valueOf(PrimarySalesHQArrayList.size()));
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

    private void parseDivisionJsonData() {
        try {
            String jsonResponse = "";
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                jsonResponse = Shared_Common_Pref.getPrimarySalesHQWiseReportData_MTD(getActivity());
            } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                jsonResponse = Shared_Common_Pref.getPrimarySalesHQWiseReportData_QTD(getActivity());
            } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                jsonResponse = Shared_Common_Pref.getPrimarySalesHQWiseReportData_YTD(getActivity());
            } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                jsonResponse = Shared_Common_Pref.getPrimarySalesHQWiseReportData_DTD(getActivity());
            }
            String Div;
            if (div_Code.contains(",")) {
                Div =div_Code.replace(",", "");
            } else {
                Div = div_Code;
            }
            if (!jsonResponse.equalsIgnoreCase("")){
                JSONArray jsonArray = new JSONArray(jsonResponse);
                HQ_List.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String SF_Cat_Code = jsonObject1.getString("SF_Cat_Code");
                    String Division_Code = jsonObject1.getString("Division_Code");
                    if ((Division_Code.equals(Div)) && (!HQ_List.contains(SF_Cat_Code))) {
                        String HQ_Name = jsonObject1.getString("HQ_Name");

                        MultiCheckDivModel mul = new MultiCheckDivModel(SF_Cat_Code, HQ_Name, false);
                        HQ_List.add(mul);
                    }
                }
                Log.d("ListCount_3", String.valueOf(HQ_List.size()));
                Array_HQ_List.clear();
                Array_HQ_List.addAll(HQ_List);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void ShowAlertDivision() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.multiple_openmodal, null);
//        LinearLayout linearLayout = (LinearLayout) dialogView.findViewById(R.id.alloption);
//        final EditText editText = (EditText) dialogView.findViewById(R.id.search_multiple);
//        TextView tv = (TextView) dialogView.findViewById(R.id.tv_searchheader_mul);
//        CheckBox check = dialogView.findViewById(R.id.checkBox_mulcheck);
//        final RecyclerView rv_list = (RecyclerView) dialogView.findViewById(R.id.dailogrv_list_mul);
//        linearLayout.setVisibility(View.VISIBLE);
//        dialogBuilder.setView(dialogView);
//        editText.setHint("Search" + " " + "Division");
//        tv.setText("Select" + " " + "Division");
//        if (Dcrdatas.IsSelectAll == 1) {
//            Dcrdatas.isselected = true;
//            check.setChecked(true);
//        } else {
//            Dcrdatas.isselected = false;
//            check.setChecked(false);
//        }
//        check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dcrdatas.IsSelectAll=1;
//                Dcrdatas.productFrom = "1";
//                if (Dcrdatas.checkin == 1) {
//                    Dcrdatas.isselected = true;
//                    Dcrdatas.checkin = 0;
//                } else {
//                    Dcrdatas.isselected = false;
//                    Dcrdatas.checkin = 1;
//                }
//                multiCheckDivisionAdapt.notifyDataSetChanged();
//            }
//        });
//        multiCheckDivisionAdapt = new MultiCheckDivAdaptor(Division_List, check, rv_list, getActivity(), new OnMultiCheckDivListener() {
//            @Override
//            public void OnMultiCheckDivListener_Add(MultiCheckDivModel classGroup) {
//                if (classGroup != null) {
//                    ListSelectedDivision.add(classGroup);
//                }
//            }
//
//            @Override
//            public void OnMultiCheckDivListener_Remove(MultiCheckDivModel classGroup) {
//                if (classGroup != null) {
//                    ListSelectedDivision.remove(classGroup);
//                }
//            }
//        });
//
//        dialogBuilder.setNegativeButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (ListSelectedDivision.size() > 0) {
//                    String SelectDiv = "";
//                    Division_List_For_HQ.clear();
//                    for (int i = 0; i < Division_List.size(); i++) {
//                        if (Division_List.get(i).isChecked()) {
//                            if (SelectDiv.equals("")) {
//                                SelectDiv = Division_List.get(i).getStringID();
//                            } else {
//                                SelectDiv = SelectDiv + "," + Division_List.get(i).getStringID();
//                            }
//                            Division_List_For_HQ.add(Division_List.get(i).getStringID());
//                        }
//                    }
//                    SelectedDivCode = SelectDiv + ",";
//                    Dcrdatas.primarysaleshqselecteddivcode = SelectedDivCode;
//                    Dcrdatas.primarysaleshqselecteddivtag = 1;
//                    if (Dcrdatas.primarysalesselectedtdtag == 0) {
//                        parseHQJsonData(Shared_Common_Pref.getPrimarySalesHQWiseReportData_MTD(getActivity()));
//                    } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
//                        parseHQJsonData(Shared_Common_Pref.getPrimarySalesHQWiseReportData_QTD(getActivity()));
//                    } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
//                        parseHQJsonData(Shared_Common_Pref.getPrimarySalesHQWiseReportData_YTD(getActivity()));
//                    } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
//                        parseHQJsonData(Shared_Common_Pref.getPrimarySalesHQWiseReportData_DTD(getActivity()));
//                    }
//                    FetchDivWisePrimarySalesHQWiseReport();
//                }
//                dialog.dismiss();
//            }
//        });
//        LinearLayoutManager LayoutManagerpocsale = new LinearLayoutManager(getActivity());
//        rv_list.setLayoutManager(LayoutManagerpocsale);
//        rv_list.setItemAnimator(new DefaultItemAnimator());
//        rv_list.setAdapter(multiCheckDivisionAdapt);
//        final AlertDialog alertDialog = dialogBuilder.create();
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
//                FilterDivision(text);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        alertDialog.show();
//    }

//    public void FilterDivision(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        Division_List.clear();
//        if (charText.length() == 0) {
//            Division_List.addAll(Array_Division_List);
//        } else {
//            for (int i = 0; i < Array_Division_List.size(); i++) {
//                final String text = Array_Division_List.get(i).getStringName().toLowerCase();
//                if (text.contains(charText)) {
//                    Division_List.add(Array_Division_List.get(i));
//                }
//            }
//        }
//        multiCheckDivisionAdapt.notifyDataSetChanged();
//    }

//    private void FetchDivWisePrimarySalesHQWiseReport() {
//        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setMessage(getResources().getString(R.string.loading));
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//
//        String TD_Data = "";
////        if (Dcrdatas.primarysalesselectedtdtag == 0) {
////            TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_MTD(getActivity());
////        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
////            TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_QTD(getActivity());
////        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
////            TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_YTD(getActivity());
////        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
////            TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_DTD(getActivity());
////        }
//
////        String DivisionCodeDetails[] = Dcrdatas.primarysaleshqselecteddivcode.split(",");
////        for (int i = 0; i < DivisionCodeDetails.length; i++) {
////            Selected_Division_List.add(DivisionCodeDetails[i]);
////        }
//
//        if (!TD_Data.isEmpty()) {
//            try {
//                JSONArray jsonArray = new JSONArray(TD_Data);
//                if (jsonArray.length() > 0) {
//                    PrimarySalesHQArrayList.clear();
//                    for (int j = 0; j < jsonArray.length(); j++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(j);
//                        String Division_Code = jsonObject.getString("Division_Code");
//                        if (Selected_Division_List.contains(Division_Code)) {
//                            String sl_no = jsonObject.getString("sl_no");
//                            String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
//                            String HQ_Name = jsonObject.getString("HQ_Name");
//                            String Division_Name = jsonObject.getString("Division_Name");
//                            String Primary = jsonObject.getString("Primary");
//                            String PrevPrimary = jsonObject.getString("PrevPrimary");
//                            String Target = jsonObject.getString("Target");
//                            String Achie = jsonObject.getString("Achie");
//                            String Growth = jsonObject.getString("Growth");
//                            PSalesHQModel mPrimarySalesDetailModel = new PSalesHQModel(sl_no, SF_Cat_Code, HQ_Name,
//                                    Division_Code, Division_Name, Primary, PrevPrimary, Target, Achie, Growth);
//                            PrimarySalesHQArrayList.add(mPrimarySalesDetailModel);
//                            mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
//                        }
//                    }
//                    Log.d("ListCount_2", String.valueOf(PrimarySalesHQArrayList.size()));
//                    if (mProgressDialog.isShowing())
//                        mProgressDialog.dismiss();
//                } else {
//                    PrimarySalesHQArrayList.clear();
//                    mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
//                    if (mProgressDialog.isShowing())
//                        mProgressDialog.dismiss();
//                }
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//            }
//        } else {
//            PrimarySalesHQArrayList.clear();
//            mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
//            if (mProgressDialog.isShowing())
//                mProgressDialog.dismiss();
//        }
//    }

//    private void parseHQJsonData(String jsonResponse) {
//        try {
//            JSONArray jsonArray = new JSONArray(jsonResponse);
//            HQ_List.clear();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                String SF_Cat_Code = jsonObject1.getString("SF_Cat_Code");
//                String Division_Code = jsonObject1.getString("Division_Code");
//                if ((Division_List_For_HQ.contains(Division_Code)) && (!HQ_List.contains(SF_Cat_Code))) {
//                    String HQ_Name = jsonObject1.getString("HQ_Name");
//                    MultiCheckDivModel mul = new MultiCheckDivModel(SF_Cat_Code, HQ_Name, false);
//                    HQ_List.add(mul);
//                }
//            }
//            Log.d("ListCount_3", String.valueOf(HQ_List.size()));
//            Array_HQ_List.clear();
//            Array_HQ_List.addAll(HQ_List);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    public void ShowAlertHQ() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.multiple_openmodal, null);
//        LinearLayout linearLayout = (LinearLayout) dialogView.findViewById(R.id.alloption);
//        final EditText editText = (EditText) dialogView.findViewById(R.id.search_multiple);
//        TextView tv = (TextView) dialogView.findViewById(R.id.tv_searchheader_mul);
//        CheckBox check = dialogView.findViewById(R.id.checkBox_mulcheck);
//        final RecyclerView rv_list = (RecyclerView) dialogView.findViewById(R.id.dailogrv_list_mul);
//        linearLayout.setVisibility(View.VISIBLE);
//        dialogBuilder.setView(dialogView);
//        editText.setHint("Search" + " " + "Headquarters");
//        tv.setText("Select" + " " + "Headquarters");
//        check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dcrdatas.productFrom = "1";
//                if (Dcrdatas.checkin == 1) {
//                    Dcrdatas.isselected = true;
//                    Dcrdatas.checkin = 0;
//                } else {
//                    Dcrdatas.isselected = false;
//                    Dcrdatas.checkin = 1;
//                }
////                multiCheckDivisionAdapt.notifyDataSetChanged();
//            }
//        });
//        multiCheckDivisionAdapt = new MultiCheckDivAdaptor(HQ_List, check, rv_list, getActivity(), new OnMultiCheckDivListener() {
//            @Override
//            public void OnMultiCheckDivListener_Add(MultiCheckDivModel classGroup) {
//                if (classGroup != null) {
//                    ListSelectedHQ.add(classGroup);
//                }
//            }
//
//            @Override
//            public void OnMultiCheckDivListener_Remove(MultiCheckDivModel classGroup) {
//                if (classGroup != null) {
//                    ListSelectedHQ.remove(classGroup);
//                }
//            }
//        });
//
//        dialogBuilder.setNegativeButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (ListSelectedHQ.size() > 0) {
//                    String SelectHQ = "";
//                    for (int i = 0; i < HQ_List.size(); i++) {
//                        if (HQ_List.get(i).isChecked()) {
//                            if (SelectHQ.equals("")) {
//                                SelectHQ = HQ_List.get(i).getStringID();
//                            } else {
//                                SelectHQ = SelectHQ + "," + HQ_List.get(i).getStringID();
//                            }
//                        }
//                    }
//                    SelectedHQ = SelectHQ + ",";
//                    Dcrdatas.primarysaleshqselectedHQ = SelectedHQ;
//                    Dcrdatas.primarysaleshqselectedHQtag = 1;
//                    FetchHQWisePrimarySalesHQWiseReport();
//                }
//                dialog.dismiss();
//            }
//        });
//        LinearLayoutManager LayoutManagerpocsale = new LinearLayoutManager(getActivity());
//        rv_list.setLayoutManager(LayoutManagerpocsale);
//        rv_list.setItemAnimator(new DefaultItemAnimator());
//        rv_list.setAdapter(multiCheckDivisionAdapt);
//        final AlertDialog alertDialog = dialogBuilder.create();
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String text1 = editText.getText().toString().toLowerCase(Locale.getDefault());
//                FilterHQ(text1);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        alertDialog.show();
//    }

//    public void FilterHQ(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        HQ_List.clear();
//        if (charText.length() == 0) {
//            HQ_List.addAll(Array_HQ_List);
//        } else {
//            for (int i = 0; i < Array_HQ_List.size(); i++) {
//                final String text = Array_HQ_List.get(i).getStringName().toLowerCase();
//                if (text.contains(charText)) {
//                    HQ_List.add(Array_HQ_List.get(i));
//                }
//            }
//        }
//        multiCheckDivisionAdapt.notifyDataSetChanged();
//    }

    private void FetchHQWisePrimarySalesHQWiseReport() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        String TD_Data = "";
        if (Dcrdatas.primarysalesselectedtdtag == 0) {
            TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_MTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
            TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_QTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
           TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_YTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
            TD_Data = Shared_Common_Pref.getPrimarySalesHQWiseReportData_DTD(getActivity());
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
                    PrimarySalesHQArrayList.clear();
//                    mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                        if (Selected_HQ_List.contains(SF_Cat_Code)) {
                            String sl_no = jsonObject.getString("sl_no");
                            String HQ_Name = jsonObject.getString("HQ_Name");
                            String Division_Code = jsonObject.getString("Division_Code");
                            String Division_Name = jsonObject.getString("Division_Name");
                            String Primary = jsonObject.getString("Primary");
                            String PrevPrimary = jsonObject.getString("PrevPrimary");
                            String Target = jsonObject.getString("Target");
                            String Achie = jsonObject.getString("Achie");
                            String Growth = jsonObject.getString("Growth");
                            PSalesHQModel mPrimarySalesDetailModel = new PSalesHQModel(sl_no, SF_Cat_Code, HQ_Name,
                                    Division_Code, Division_Name, Primary, PrevPrimary, Target, Achie, Growth);
                            PrimarySalesHQArrayList.add(mPrimarySalesDetailModel);
                            mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
                        }
                    }
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } else {
                    PrimarySalesHQArrayList.clear();
                    mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            PrimarySalesHQArrayList.clear();
            mPrimarySalesHQWiseAdapter.notifyDataSetChanged();
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }
}
