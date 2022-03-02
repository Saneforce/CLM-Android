package saneforce.sanclm.Sales_Report.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import saneforce.sanclm.Sales_Report.Adapter.PSalesHQBasedBrandAdapter;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesHQBasedBrandModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class PSaleHQBasedBrandDetails extends AppCompatActivity {
    LinearLayout SubParentLayout;
    RecyclerView rv_brand;
    ImageView back_btn;
    TextView toolbar_sub_title;

    public static ArrayList<PSalesHQBasedBrandModel> BandArrayList = new ArrayList<PSalesHQBasedBrandModel>();
    public PSalesHQBasedBrandAdapter mPSalesHQBasedBrandAdapter;

    //List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear, HQCode, HQName;
    String SFCatCode, SF_Cat_Code;
    //sqlLite mSqlLite;
    Cursor mCursor;
    String sf_code,div_Code,db_connPath,sf_name;
    CommonSharedPreference mCommonSharedPreference;
    public DataBaseHandler dbh;

    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hq_based_brand);

        SubParentLayout = findViewById(R.id.SubParentLayout);
        toolbar_sub_title = findViewById(R.id.toolbar_sub_title);
        back_btn = findViewById(R.id.back_btn);
        rv_brand = findViewById(R.id.rv_brand);
       // mSqlLite = new sqlLite(PSaleHQBasedBrandDetails.this);

        SFCatCode=getIntent().getStringExtra("SF_Cat_Code");
        HQCode=getIntent().getStringExtra("HQ_Code");
        HQName=getIntent().getStringExtra("HQ_Name");

        toolbar_sub_title.setText(HQName);

        dbh=new DataBaseHandler(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PSaleHQBasedBrandDetails.this, PSalesDetailsReport.class);
//                startActivity(intent);
                finish();
            }
        });

        String HQBrand_Data="";
        mPSalesHQBasedBrandAdapter = new PSalesHQBasedBrandAdapter(BandArrayList, PSaleHQBasedBrandDetails.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PSaleHQBasedBrandDetails.this);
        rv_brand.setLayoutManager(mLayoutManager);
        rv_brand.setItemAnimator(new DefaultItemAnimator());
        rv_brand.setAdapter(mPSalesHQBasedBrandAdapter);

        dbh.open();

        if( Dcrdatas.primarysalesselectedtdtag==0){
            mCursor = dbh.getHQBasedBrandData_MTD(SFCatCode);
            if (mCursor.moveToFirst()) {
                HQBrand_Data = mCursor.getString(mCursor.getColumnIndex("HQBasedBrand_MTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==1){
            mCursor = dbh.getHQBasedBrandData_QTD(SFCatCode);
            if (mCursor.moveToFirst()) {
                HQBrand_Data = mCursor.getString(mCursor.getColumnIndex("HQBasedBrand_QTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==2){
            mCursor = dbh.getHQBasedBrandData_YTD(SFCatCode);
            if (mCursor.moveToFirst()) {
                HQBrand_Data = mCursor.getString(mCursor.getColumnIndex("HQBasedBrand_YTD_Value"));
            }
        }

        if (((mCursor != null) && (mCursor.getCount() > 0))) {
            LoadHQBasedBrandDetails(HQBrand_Data);
        } else {
            GetHQBasedBrandDetails();
        }

    }
    

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void GetHQBasedBrandDetails() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(PSaleHQBasedBrandDetails.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);

//            sqlLite sqlLite;
//            sqlLite = new sqlLite(PSaleHQBasedBrandDetails.this);
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
           // QryParam.put("axn", "get/primary_hq_detail");
            QryParam.put("divisionCode", div_Code);
            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
            //For MTD :
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_MTD(PSaleHQBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_MTD(PSaleHQBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_MTD(PSaleHQBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_MTD(PSaleHQBasedBrandDetails.this);
            }
            //For QTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_QTD(PSaleHQBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_QTD(PSaleHQBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_QTD(PSaleHQBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_QTD(PSaleHQBasedBrandDetails.this);
            }
            //For YTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_YTD(PSaleHQBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_YTD(PSaleHQBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_YTD(PSaleHQBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_YTD(PSaleHQBasedBrandDetails.this);
            }
            //For DTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_DTD(PSaleHQBasedBrandDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_DTD(PSaleHQBasedBrandDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_DTD(PSaleHQBasedBrandDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_DTD(PSaleHQBasedBrandDetails.this);
            }
            QryParam.put("fmonth", FromMonth);
            QryParam.put("fyear", FromYear);
            QryParam.put("tomonth", ToMonth);
            QryParam.put("toyear", ToYear);
            QryParam.put("hqcode", HQCode);
            QryParam.put("hqname", HQName);
            Log.e("HQBasedBrandDetails_1", QryParam.toString());
            Call<JsonArray> call = apiService.getHqBrandDataAsJArray( QryParam);
            Log.d("HQBasedBrandDetails_2", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("HQBasedBrandDetails_3", response.body() + " " + response.body().toString());
                        Log.d("HQBasedBrandDetails_4", response.toString());
                        if (!response.body().toString().isEmpty()) {
                            try {
                              //  dbh.open();
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    BandArrayList.clear();
//                                    mPSalesHQBasedBrandAdapter.notifyDataSetChanged();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                                        String HQ_Name = jsonObject.getString("HQ Name");
                                        String Brand = jsonObject.getString("Brand");
                                        String Brand_Code = jsonObject.getString("Brand_Code");
                                        String Division_Code = jsonObject.getString("Division_Code");
                                        String Division_Name = jsonObject.getString("Division_Name");
                                        String Primary = jsonObject.getString("Primary");
                                        String Target = jsonObject.getString("Target");
                                        String PrevSale = jsonObject.getString("PrevSale");
                                        String achie = jsonObject.getString("achie");
                                        String Growth = jsonObject.getString("Growth");
                                        String PC = jsonObject.getString("PC");

                                        PSalesHQBasedBrandModel mPSalesHQBasedBrandModel = new PSalesHQBasedBrandModel(SF_Cat_Code, HQ_Name, Brand,
                                                Brand_Code, Division_Code, Division_Name, Primary, Target, PrevSale, achie, Growth, PC);
                                        BandArrayList.add(mPSalesHQBasedBrandModel);
                                        mPSalesHQBasedBrandAdapter.notifyDataSetChanged();
                                    }

                                    if( Dcrdatas.primarysalesselectedtdtag==0){
                                        dbh.insertHQBasedBrand_MTD(SF_Cat_Code, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==1){
                                        dbh.insertHQBasedBrand_QTD(SF_Cat_Code, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==2){
                                        dbh.insertHQBasedBrand_YTD(SF_Cat_Code, response.body().toString());
                                    }
                                    dbh.close();

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
                    Log.e("HQBasedBrandDetails", "onFailure : " + t.toString());
                    Toast.makeText(PSaleHQBasedBrandDetails.this, "Something went wrong  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(PSaleHQBasedBrandDetails.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadHQBasedBrandDetails(String HQBrand_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(PSaleHQBasedBrandDetails.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        try {
            JSONArray jsonArray = new JSONArray(HQBrand_Data);
            if (jsonArray.length() > 0) {
                BandArrayList.clear();
//                mPSalesHQBasedBrandAdapter.notifyDataSetChanged();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                    String HQ_Name = jsonObject.getString("HQ Name");
                    String Brand = jsonObject.getString("Brand");
                    String Brand_Code = jsonObject.getString("Brand_Code");
                    String Division_Code = jsonObject.getString("Division_Code");
                    String Division_Name = jsonObject.getString("Division_Name");
                    String Primary = jsonObject.getString("Primary");
                    String Target = jsonObject.getString("Target");
                    String PrevSale = jsonObject.getString("PrevSale");
                    String achie = jsonObject.getString("achie");
                    String Growth = jsonObject.getString("Growth");
                    String PC = jsonObject.getString("PC");
                    PSalesHQBasedBrandModel mPSalesHQBasedBrandModel = new PSalesHQBasedBrandModel(SF_Cat_Code, HQ_Name, Brand,
                            Brand_Code, Division_Code, Division_Name, Primary, Target, PrevSale, achie, Growth, PC);
                    BandArrayList.add(mPSalesHQBasedBrandModel);
                    mPSalesHQBasedBrandAdapter.notifyDataSetChanged();
                }
                Log.d("ListCount_1", String.valueOf(BandArrayList.size()));
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } else {
                BandArrayList.clear();
                mPSalesHQBasedBrandAdapter.notifyDataSetChanged();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
