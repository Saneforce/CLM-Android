package saneforce.sanclm.Sales_Report.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import saneforce.sanclm.Sales_Report.Adapter.PSalesBrandAdapter;
import saneforce.sanclm.Sales_Report.Interface.OnMultiCheckListener;
import saneforce.sanclm.Sales_Report.Modelclass.MultiCheckDivModel;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesBrandModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;

public class TabBrandWiseReport extends Fragment {
    TextView tv_brand_wise_display_date;
    FrameLayout LayoutFrame_Brand_Wise;
    ImageView btn_brand_wise_div;
    RecyclerView rv_brand_wise;

    public static ArrayList<PSalesBrandModel> PrimarySalesBrandArrayList = new ArrayList<PSalesBrandModel>();

    public PSalesBrandAdapter mPSalesBrandAdapter;

    ArrayList<MultiCheckDivModel> ListSelectedDivision = new ArrayList<MultiCheckDivModel>();
    ArrayList<MultiCheckDivModel> Array_Division_List = new ArrayList<MultiCheckDivModel>();
    ArrayList<MultiCheckDivModel> Division_List = new ArrayList<MultiCheckDivModel>();
    ArrayList<String> Selected_Division_List = new ArrayList<>();

    MultiCheckDivAdaptor multiCheckDivisionAdapt;

    String SelectedDivCode = "";

    //List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear;

    String sf_code,div_Code,db_connPath,sf_name;
    CommonSharedPreference mCommonSharedPreference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_wise, container, false);
        LayoutFrame_Brand_Wise = view.findViewById(R.id.LayoutFrame_Brand_Wise);
        tv_brand_wise_display_date = view.findViewById(R.id.tv_brand_wise_display_date);
        btn_brand_wise_div = view.findViewById(R.id.btn_brand_wise_div);
        rv_brand_wise = view.findViewById(R.id.rv_brand_wise);

        mCommonSharedPreference=new CommonSharedPreference(getContext());
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

        btn_brand_wise_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDivision();
            }
        });

        mPSalesBrandAdapter = new PSalesBrandAdapter(PrimarySalesBrandArrayList, getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_brand_wise.setLayoutManager(mLayoutManager);
        rv_brand_wise.setItemAnimator(new DefaultItemAnimator());
        rv_brand_wise.setAdapter(mPSalesBrandAdapter);

        String TD_Data = "";
        String DisplayReportDate = "";
        if (Dcrdatas.primarysalesselectedtdtag == 0) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_MTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesBrandWiseReportDataAvail_MTD(getActivity())) {
                GetPrimarySalesBrandWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_MTD(getActivity());
                LoadPrimarySalesBrandWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_QTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesBrandWiseReportDataAvail_QTD(getActivity())) {
                GetPrimarySalesBrandWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_QTD(getActivity());
                LoadPrimarySalesBrandWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_YTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesBrandWiseReportDataAvail_YTD(getActivity())) {
                GetPrimarySalesBrandWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_YTD(getActivity());
                LoadPrimarySalesBrandWiseReport(TD_Data);
            }
        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {

            DisplayReportDate = Shared_Common_Pref.getPrimarySalesReportDate_DTD(getActivity());
            if (!Shared_Common_Pref.getPrimarySalesBrandWiseReportDataAvail_DTD(getActivity())) {
                GetPrimarySalesBrandWiseReport();
            } else {
                TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_DTD(getActivity());
                LoadPrimarySalesBrandWiseReport(TD_Data);
            }
        }
        tv_brand_wise_display_date.setText(DisplayReportDate);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void GetPrimarySalesBrandWiseReport() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService =  RetroClient.getClient(db_connPath).create(Api_Interface.class);

//            sqlLite sqlLite;
//            sqlLite = new sqlLite(getActivity());
//            Cursor cursor = sqlLite.getAllLoginData();
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);

            Map<String, String> QryParam = new HashMap<>();
            //QryParam.put("axn", "get/primary_Brand");
            QryParam.put("divisionCode", div_Code+",");
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
            Log.e("TabBrandWiseReport_1", QryParam.toString());
            Call<JsonArray> call = apiService.getBrandwiseDataAsJArray( QryParam);
            Log.d("TabBrandWiseReport_2", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("TabBrandWiseReport_3", response.body() + " " + response.body().toString());
                        Log.d("TabBrandWiseReport_4", response.toString());
                        if (!response.body().toString().isEmpty()) {
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    PrimarySalesBrandArrayList.clear();
//                                    mPSalesBrandAdapter.notifyDataSetChanged();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        String cnt = jsonObject.getString("cnt");
                                        String prod_code = jsonObject.getString("prod_code");
                                        String prod_name = jsonObject.getString("prod_name");
                                        String Division_Code = jsonObject.getString("Division_Code");
                                        String Division_Name = jsonObject.getString("Division_Name");
                                        String Target_Val = jsonObject.getString("Target_Val");
                                        String Prev_Sale = jsonObject.getString("Prev_Sale");
                                        String achie = jsonObject.getString("achie");
                                        String Growth = jsonObject.getString("Growth");
                                        String PC = jsonObject.getString("PC");
                                        String selectedValue = jsonObject.getString("selectedValue");

                                        PSalesBrandModel mPSalesBrandModel = new PSalesBrandModel(cnt, prod_code, prod_name,
                                                Division_Code, Division_Name, Target_Val, Prev_Sale, achie, Growth, PC, selectedValue);
                                        PrimarySalesBrandArrayList.add(mPSalesBrandModel);
                                        mPSalesBrandAdapter.notifyDataSetChanged();
                                    }
                                    if (Dcrdatas.primarysalesselectedtdtag == 0) {
                                        Shared_Common_Pref.putPrimarySalesBrandWiseReport_MTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                                        Shared_Common_Pref.putPrimarySalesBrandWiseReport_QTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                                        Shared_Common_Pref.putPrimarySalesBrandWiseReport_YTD(getActivity(), response.body().toString(), true);
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                                        Shared_Common_Pref.putPrimarySalesBrandWiseReport_DTD(getActivity(), response.body().toString(), true);
                                    }
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                } else {
                                    PrimarySalesBrandArrayList.clear();
                                    mPSalesBrandAdapter.notifyDataSetChanged();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            PrimarySalesBrandArrayList.clear();
                            mPSalesBrandAdapter.notifyDataSetChanged();
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }
                    } else {
                        PrimarySalesBrandArrayList.clear();
                        mPSalesBrandAdapter.notifyDataSetChanged();
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Log.e("TabBrandWiseReport_5", "onFailure : " + t.toString());
                    Toast.makeText(getActivity(), "Something went wrong  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadPrimarySalesBrandWiseReport(String TD_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        if (!TD_Data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(TD_Data);
                if (jsonArray.length() > 0) {
                    PrimarySalesBrandArrayList.clear();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String cnt = jsonObject.getString("cnt");
                        String prod_code = jsonObject.getString("prod_code");
                        String prod_name = jsonObject.getString("prod_name");
                        String Division_Code = jsonObject.getString("Division_Code");
                        String Division_Name = jsonObject.getString("Division_Name");
                        String Target_Val = jsonObject.getString("Target_Val");
                        String Prev_Sale = jsonObject.getString("Prev_Sale");
                        String achie = jsonObject.getString("achie");
                        String Growth = jsonObject.getString("Growth");
                        String PC = jsonObject.getString("PC");
                        String selectedValue = jsonObject.getString("selectedValue");

                        PSalesBrandModel mPSalesBrandModel = new PSalesBrandModel(cnt, prod_code, prod_name,
                                Division_Code, Division_Name, Target_Val, Prev_Sale, achie, Growth, PC, selectedValue);
                        PrimarySalesBrandArrayList.add(mPSalesBrandModel);
                        mPSalesBrandAdapter.notifyDataSetChanged();
                    }
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } else {
                    PrimarySalesBrandArrayList.clear();
                    mPSalesBrandAdapter.notifyDataSetChanged();
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            PrimarySalesBrandArrayList.clear();
            mPSalesBrandAdapter.notifyDataSetChanged();
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    private void parseDivisionJsonData(String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            Division_List.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.optString("division_code");
                String name = jsonObject1.optString("division_name");
                MultiCheckDivModel mul = new MultiCheckDivModel(id, name, false);
                Division_List.add(mul);
            }
            Array_Division_List.clear();
            Array_Division_List.addAll(Division_List);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ShowAlertDivision() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.multiple_openmodal, null);
        LinearLayout linearLayout = (LinearLayout) dialogView.findViewById(R.id.alloption);
        final EditText editText = (EditText) dialogView.findViewById(R.id.search_multiple);
        TextView tv = (TextView) dialogView.findViewById(R.id.tv_searchheader_mul);
        CheckBox check = dialogView.findViewById(R.id.checkBox_mulcheck);
        final RecyclerView rv_list = (RecyclerView) dialogView.findViewById(R.id.dailogrv_list_mul);
        linearLayout.setVisibility(View.VISIBLE);
        dialogBuilder.setView(dialogView);
        editText.setHint("Search" + " " + "Division");
        tv.setText("Select" + " " + "Division");
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
        multiCheckDivisionAdapt = new MultiCheckDivAdaptor(Division_List, check, rv_list, getActivity(), new OnMultiCheckListener() {
            @Override
            public void OnMultiCheckDivListener_Add(MultiCheckDivModel classGroup) {
                if (classGroup != null) {
                    ListSelectedDivision.add(classGroup);
                }
            }

            @Override
            public void OnMultiCheckDivListener_Remove(MultiCheckDivModel classGroup) {
                if (classGroup != null) {
                    ListSelectedDivision.remove(classGroup);
                }
            }
        });

        dialogBuilder.setNegativeButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ListSelectedDivision.size() > 0) {
                    String SelectDiv = "";
                    for (int i = 0; i < Division_List.size(); i++) {
                        if (Division_List.get(i).isChecked()) {
                            if (SelectDiv.equals("")) {
                                SelectDiv = Division_List.get(i).getStringID();
                            } else {
                                SelectDiv = SelectDiv + "," + Division_List.get(i).getStringID();
                            }
                        }
                    }
                    SelectedDivCode = SelectDiv + ",";
                    Dcrdatas.primarysalesbrandselecteddivcode = SelectedDivCode;
                    Dcrdatas.primarysalesbrandselecteddivtag = 1;
                    FetchDivWisePSaleBrandWiseReport();
                }
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
                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                FilterDivision(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        alertDialog.show();
    }

    public void FilterDivision(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Division_List.clear();
        if (charText.length() == 0) {
            Division_List.addAll(Array_Division_List);
        } else {
            for (int i = 0; i < Array_Division_List.size(); i++) {
                final String text = Array_Division_List.get(i).getStringName().toLowerCase();
                if (text.contains(charText)) {
                    Division_List.add(Array_Division_List.get(i));
                }
            }
        }
        multiCheckDivisionAdapt.notifyDataSetChanged();
    }

    private void FetchDivWisePSaleBrandWiseReport() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        String TD_Data = "";
        if (Dcrdatas.primarysalesselectedtdtag == 0) {
            TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_MTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
            TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_QTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
           TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_YTD(getActivity());
        } else if (Dcrdatas.primarysalesselectedtdtag == 3) {
            TD_Data = Shared_Common_Pref.getPrimarySalesBrandWiseReportData_DTD(getActivity());
        }

        String DivisionCodeDetails[] = Dcrdatas.primarysalesbrandselecteddivcode.split(",");
        for (int i = 0; i < DivisionCodeDetails.length; i++) {
            Selected_Division_List.add(DivisionCodeDetails[i]);
        }

        if (!TD_Data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(TD_Data);
                if (jsonArray.length() > 0) {
                    PrimarySalesBrandArrayList.clear();
//                    mPSalesBrandAdapter.notifyDataSetChanged();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String Division_Code = jsonObject.getString("Division_Code");
                        if (Selected_Division_List.contains(Division_Code)) {
                            String cnt = jsonObject.getString("cnt");
                            String prod_code = jsonObject.getString("prod_code");
                            String prod_name = jsonObject.getString("prod_name");
                            String Division_Name = jsonObject.getString("Division_Name");
                            String Target_Val = jsonObject.getString("Target_Val");
                            String Prev_Sale = jsonObject.getString("Prev_Sale");
                            String achie = jsonObject.getString("achie");
                            String Growth = jsonObject.getString("Growth");
                            String PC = jsonObject.getString("PC");
                            String selectedValue = jsonObject.getString("selectedValue");

                            PSalesBrandModel mPSalesBrandModel = new PSalesBrandModel(cnt, prod_code, prod_name,
                                    Division_Code, Division_Name, Target_Val, Prev_Sale, achie, Growth, PC, selectedValue);
                            PrimarySalesBrandArrayList.add(mPSalesBrandModel);
                            mPSalesBrandAdapter.notifyDataSetChanged();
                        }
                    }
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } else {
                    PrimarySalesBrandArrayList.clear();
                    mPSalesBrandAdapter.notifyDataSetChanged();
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            PrimarySalesBrandArrayList.clear();
            mPSalesBrandAdapter.notifyDataSetChanged();
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }
}
