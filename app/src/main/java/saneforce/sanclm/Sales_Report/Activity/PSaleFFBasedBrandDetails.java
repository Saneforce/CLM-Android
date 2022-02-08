package saneforce.sanclm.Sales_Report.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Adapter.PSalesFFBasedBrandAdapter;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesFFBasedBrandModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class PSaleFFBasedBrandDetails extends AppCompatActivity {
    LinearLayout SubParentLayout;
    RecyclerView rv_brand;
    ImageView back_btn;
    TextView toolbar_sub_title;


    public static ArrayList<PSalesFFBasedBrandModel> FFBandArrayList = new ArrayList<PSalesFFBasedBrandModel>();
    public PSalesFFBasedBrandAdapter mPSalesFFBasedBrandAdapter;

    //List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear, DivCode, HQCode, HQName, SFCode, sf_code;
   // sqlLite mSqlLite;
    Cursor mCursor;

    String div_Code,db_connPath,sf_name;
    CommonSharedPreference mCommonSharedPreference;
    public DataBaseHandler dbh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hq_based_brand);

        SubParentLayout = findViewById(R.id.SubParentLayout);
        toolbar_sub_title = findViewById(R.id.toolbar_sub_title);
        back_btn = findViewById(R.id.back_btn);
        rv_brand = findViewById(R.id.rv_brand);

        DivCode=getIntent().getStringExtra("Div_Code");
        DivCode=DivCode+",";
        HQCode=getIntent().getStringExtra("HQ_Code");
        HQName=getIntent().getStringExtra("HQ_Name");
        SFCode=getIntent().getStringExtra("SF_Code");
        Dcrdatas.FFBrandHQName = HQName;
        //mSqlLite = new sqlLite(PSaleFFBasedBrandDetails.this);

        toolbar_sub_title.setText(HQName);

        dbh=new DataBaseHandler(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPSalesFFBasedBrandAdapter = new PSalesFFBasedBrandAdapter(FFBandArrayList, getApplicationContext(), PSaleFFBasedBrandDetails.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PSaleFFBasedBrandDetails.this);
        rv_brand.setLayoutManager(mLayoutManager);
        rv_brand.setItemAnimator(new DefaultItemAnimator());
        rv_brand.setAdapter(mPSalesFFBasedBrandAdapter);

        dbh.open();
        String FFBrand_Data="";
        if( Dcrdatas.primarysalesselectedtdtag==0){
           mCursor = dbh.getFFBasedBrandData_MTD(SFCode);
            if (mCursor.moveToFirst()) {
                FFBrand_Data = mCursor.getString(mCursor.getColumnIndex("FFBasedBrand_MTD_Value"));
          }
        } else if( Dcrdatas.primarysalesselectedtdtag==1){
            mCursor = dbh.getFFBasedBrandData_QTD(SFCode);
            if (mCursor.moveToFirst()) {
                FFBrand_Data = mCursor.getString(mCursor.getColumnIndex("FFBasedBrand_QTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==2){
            mCursor = dbh.getFFBasedBrandData_YTD(SFCode);
            if (mCursor.moveToFirst()) {
                FFBrand_Data = mCursor.getString(mCursor.getColumnIndex("FFBasedBrand_YTD_Value"));
            }
        }

        if (((mCursor != null) && (mCursor.getCount() > 0))) {
            LoadFFBasedBrandDetails(FFBrand_Data);
        } else {
            GetFFBasedBrandDetails();
       }
    }
    

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void GetFFBasedBrandDetails() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(PSaleFFBasedBrandDetails.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService =  RetroClient.getClient(db_connPath).create(Api_Interface.class);
//            sqlLite sqlLite;
//            sqlLite = new sqlLite(PSaleFFBasedBrandDetails.this);
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
            //QryParam.put("axn", "get/primary_fieldforce_brand");
            QryParam.put("divisionCode", DivCode);
            QryParam.put("sfCode", SFCode);
            QryParam.put("rSF", SFCode);
            //For MTD :
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_MTD(PSaleFFBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_MTD(PSaleFFBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_MTD(PSaleFFBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_MTD(PSaleFFBasedBrandDetails.this);
            }
            //For QTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_QTD(PSaleFFBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_QTD(PSaleFFBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_QTD(PSaleFFBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_QTD(PSaleFFBasedBrandDetails.this);
            }
            //For YTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_YTD(PSaleFFBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_YTD(PSaleFFBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_YTD(PSaleFFBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_YTD(PSaleFFBasedBrandDetails.this);
            }
            //For DTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_DTD(PSaleFFBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_DTD(PSaleFFBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_DTD(PSaleFFBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_DTD(PSaleFFBasedBrandDetails.this);
            }
            QryParam.put("fmonth", FromMonth);
            QryParam.put("fyear", FromYear);
            QryParam.put("tomonth", ToMonth);
            QryParam.put("toyear", ToYear);
            Log.e("FFBasedBrandDetails_1", QryParam.toString());
            Call<JsonArray> call = apiService.getFFBrandDataAsJArray( QryParam);
            Log.d("FFBasedBrandDetails_2", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("FFBasedBrandDetails_3", response.body() + " " + response.body().toString());
                        Log.d("FFBasedBrandDetails_4", response.toString());
                        if (!response.body().toString().isEmpty()) {
                            try {
                          //      dbh.open();
                                String FFBrandData = "";
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    FFBandArrayList.clear();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                       // FFBrandData = FFBrandData + jsonObject.toString() + ",";
                                            sf_code = jsonObject.getString("sf_code");
                                            String Brand_Name = jsonObject.getString("Brand_Name");
                                            String Brand_Code = jsonObject.getString("Brand_Code");
                                            String Division_Name = jsonObject.getString("Division_Name");
                                            String Division_Code = jsonObject.getString("Division_Code");
                                            String Sal_Val = jsonObject.getString("Sal_Val");
                                            String Target_Val = jsonObject.getString("Target_Val");
                                            String Prev_Sale = jsonObject.getString("Prev_Sale");
                                            String achie = jsonObject.getString("achie");
                                            String Growth = jsonObject.getString("Growth");
                                            String PC = jsonObject.getString("PC");
                                            String selectedValue = jsonObject.getString("selectedValue");

                                            PSalesFFBasedBrandModel mPSalesFFBasedBrandModel = new PSalesFFBasedBrandModel(sf_code, Brand_Name, Brand_Code, Division_Name, Division_Code, Sal_Val, Target_Val, Prev_Sale, achie, Growth, PC, selectedValue);
                                            FFBandArrayList.add(mPSalesFFBasedBrandModel);
                                            mPSalesFFBasedBrandAdapter.notifyDataSetChanged();
                                        }

                                    String mData = FFBrandData.replaceAll(",$", "");
                                    if (Dcrdatas.primarysalesselectedtdtag == 0) {
                                        dbh.insertFFBasedBrand_MTD(sf_code,response.body().toString());
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                                        dbh.insertFFBasedBrand_QTD(sf_code,response.body().toString());
                                    } else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                                        dbh.insertFFBasedBrand_YTD(sf_code, response.body().toString());
                                    }
                                    dbh.close();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                } else {
                                    FFBandArrayList.clear();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            FFBandArrayList.clear();
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }
                    } else {
                        FFBandArrayList.clear();
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Log.e("HQBasedBrandDetails", "onFailure : " + t.toString());
                }
            });
        } else {
            Toast.makeText(PSaleFFBasedBrandDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadFFBasedBrandDetails(String FFBrand_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(PSaleFFBasedBrandDetails.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        try {
            JSONArray jsonArray = new JSONArray(FFBrand_Data);
            if (jsonArray.length() > 0) {
                FFBandArrayList.clear();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    sf_code = jsonObject.getString("sf_code");
                    String Brand_Name = jsonObject.getString("Brand_Name");
                    String Brand_Code = jsonObject.getString("Brand_Code");
                    String Division_Name = jsonObject.getString("Division_Name");
                    String Division_Code = jsonObject.getString("Division_Code");
                    String Sal_Val = jsonObject.getString("Sal_Val");
                    String Target_Val = jsonObject.getString("Target_Val");
                    String Prev_Sale = jsonObject.getString("Prev_Sale");
                    String achie = jsonObject.getString("achie");
                    String Growth = jsonObject.getString("Growth");
                    String PC = jsonObject.getString("PC");
                    String selectedValue = jsonObject.getString("selectedValue");

                    PSalesFFBasedBrandModel mPSalesFFBasedBrandModel = new PSalesFFBasedBrandModel(sf_code, Brand_Name, Brand_Code, Division_Name, Division_Code, Sal_Val, Target_Val, Prev_Sale, achie, Growth, PC, selectedValue);
                    FFBandArrayList.add(mPSalesFFBasedBrandModel);
                    mPSalesFFBasedBrandAdapter.notifyDataSetChanged();
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } else {
                FFBandArrayList.clear();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
