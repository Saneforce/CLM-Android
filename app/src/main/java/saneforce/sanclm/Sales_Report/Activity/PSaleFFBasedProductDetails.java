package saneforce.sanclm.Sales_Report.Activity;

import android.annotation.SuppressLint;
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
import saneforce.sanclm.Sales_Report.Adapter.PSalesFFBasedProductAdapter;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesFFBasedProductModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class PSaleFFBasedProductDetails extends AppCompatActivity {
    LinearLayout SubParentLayout;
    RecyclerView rv_brand;
    ImageView back_btn;
    TextView toolbar_sub_title;


    public static ArrayList<PSalesFFBasedProductModel> FFProductArrayList = new ArrayList<PSalesFFBasedProductModel>();
    public PSalesFFBasedProductAdapter mPSalesFFBasedProductAdapter;

   // List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear, DivCode, HQName, BrandCode, BrandName, SFCode;
   // sqlLite mSqlLite;
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

        DivCode=getIntent().getStringExtra("Div_Code");
        DivCode=DivCode+",";
        HQName=getIntent().getStringExtra("HQ_Name");
        BrandCode=getIntent().getStringExtra("Brand_Code");
        BrandName=getIntent().getStringExtra("Brand_Name");
        SFCode=getIntent().getStringExtra("SF_Code");
        //mSqlLite = new sqlLite(PSaleFFBasedProductDetails.this);

        dbh=new DataBaseHandler(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);

        toolbar_sub_title.setText(HQName+" - "+BrandName);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPSalesFFBasedProductAdapter = new PSalesFFBasedProductAdapter(FFProductArrayList, PSaleFFBasedProductDetails.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PSaleFFBasedProductDetails.this);
        rv_brand.setLayoutManager(mLayoutManager);
        rv_brand.setItemAnimator(new DefaultItemAnimator());
        rv_brand.setAdapter(mPSalesFFBasedProductAdapter);

        dbh.open();

        String FFBrandProd_Data="";
        if( Dcrdatas.primarysalesselectedtdtag==0){
            mCursor = dbh.getFFBasedProdData_MTD(SFCode+"~"+BrandCode);
            if (mCursor.moveToFirst()) {
                FFBrandProd_Data = mCursor.getString(mCursor.getColumnIndex("FFBasedProd_MTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==1){
            mCursor = dbh.getFFBasedProdData_QTD(SFCode+"~"+BrandCode);
            if (mCursor.moveToFirst()) {
                FFBrandProd_Data = mCursor.getString(mCursor.getColumnIndex("FFBasedProd_QTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==2){
            mCursor = dbh.getFFBasedProdData_YTD(SFCode+"~"+BrandCode);
            if (mCursor.moveToFirst()) {
                FFBrandProd_Data = mCursor.getString(mCursor.getColumnIndex("FFBasedProd_YTD_Value"));
            }
        }

        if (((mCursor != null) && (mCursor.getCount() > 0))) {
            LoadFFBasedProdDetails(FFBrandProd_Data);
        } else {
            GetFFBasedProdDetails();
        }
    }
    

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void GetFFBasedProdDetails() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(PSaleFFBasedProductDetails.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService =  RetroClient.getClient(db_connPath).create(Api_Interface.class);

//            sqlLite sqlLite;
//            sqlLite = new sqlLite(PSaleFFBasedProductDetails.this);
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
           // QryParam.put("axn", "get/primary_fieldforce_brand_product");
            QryParam.put("divisionCode", DivCode);
            QryParam.put("sfCode", SFCode);
            QryParam.put("rSF", SFCode);
            //For MTD :
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_MTD(PSaleFFBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_MTD(PSaleFFBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_MTD(PSaleFFBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_MTD(PSaleFFBasedProductDetails.this);
            }
            //For QTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_QTD(PSaleFFBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_QTD(PSaleFFBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_QTD(PSaleFFBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_QTD(PSaleFFBasedProductDetails.this);
            }
            //For YTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_YTD(PSaleFFBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_YTD(PSaleFFBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_YTD(PSaleFFBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_YTD(PSaleFFBasedProductDetails.this);
            }
            //For DTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_DTD(PSaleFFBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_DTD(PSaleFFBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_DTD(PSaleFFBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_DTD(PSaleFFBasedProductDetails.this);
            }
            QryParam.put("fmonth", FromMonth);
            QryParam.put("fyear", FromYear);
            QryParam.put("tomonth", ToMonth);
            QryParam.put("toyear", ToYear);
            QryParam.put("Brand_product", BrandCode);
            Log.e("FFBasedProdDetails_1", QryParam.toString());
            Call<JsonArray> call = apiService.getFFProductDataAsJArray( QryParam);
            Log.d("FFBasedProdDetails_2", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("FFBasedProdDetails_3", response.body() + " " + response.body().toString());
                        Log.d("FFBasedProdDetails_4", response.toString());
                        if (!response.body().toString().isEmpty()) {
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    FFProductArrayList.clear();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        String id = jsonObject.getString("id");
                                        String Division_Name = jsonObject.getString("Division_Name");
                                        String sf_code = jsonObject.getString("sf_code");
                                        String Product_Code = jsonObject.getString("Product_Code");
                                        String Product_Brd_Code = jsonObject.getString("Product_Brd_Code");
                                        String Product_Brd_Name = jsonObject.getString("Product_Brd_Name");
                                        String Product_Name = jsonObject.getString("Product Name");
                                        String Pack = jsonObject.getString("Pack");
                                        String Ach = jsonObject.getString("Ach");
                                        String CSaleVal = jsonObject.getString("CSaleVal");
                                        String Growth = jsonObject.getString("Growth");
                                        String PCPM = jsonObject.getString("PCPM");
                                        String PSaleVal = jsonObject.getString("PSaleVal");
                                        String TargtVal = jsonObject.getString("TargtVal");

                                        PSalesFFBasedProductModel mPSalesFFBasedProductModel = new PSalesFFBasedProductModel(id, Division_Name, sf_code,
                                                Product_Code, Product_Brd_Code, Product_Brd_Name, Product_Name, Pack, Ach, CSaleVal, Growth, PCPM, PSaleVal, TargtVal);
                                        FFProductArrayList.add(mPSalesFFBasedProductModel);
                                        mPSalesFFBasedProductAdapter.notifyDataSetChanged();
                                    }
                                    if( Dcrdatas.primarysalesselectedtdtag==0){
                                        dbh.insertFFBasedProd_MTD(SFCode+"~"+BrandCode, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==1){
                                        dbh.insertFFBasedProd_QTD(SFCode+"~"+BrandCode, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==2){
                                        dbh.insertFFBasedProd_YTD(SFCode+"~"+BrandCode, response.body().toString());
                                    }
                                    dbh.close();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                } else {
                                    FFProductArrayList.clear();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            FFProductArrayList.clear();
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }
                    } else {
                        FFProductArrayList.clear();
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Log.e("FFBasedProdDetails_5", "onFailure : " + t.toString());
                }
            });
        } else {
            Toast.makeText(PSaleFFBasedProductDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadFFBasedProdDetails(String FFBrandProd_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(PSaleFFBasedProductDetails.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        try {
            JSONArray jsonArray = new JSONArray(FFBrandProd_Data);
            if (jsonArray.length() > 0) {
                FFProductArrayList.clear();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String id = jsonObject.getString("id");
                    String Division_Name = jsonObject.getString("Division_Name");
                    String sf_code = jsonObject.getString("sf_code");
                    String Product_Code = jsonObject.getString("Product_Code");
                    String Product_Brd_Code = jsonObject.getString("Product_Brd_Code");
                    String Product_Brd_Name = jsonObject.getString("Product_Brd_Name");
                    String Product_Name = jsonObject.getString("Product Name");
                    String Pack = jsonObject.getString("Pack");
                    String Ach = jsonObject.getString("Ach");
                    String CSaleVal = jsonObject.getString("CSaleVal");
                    String Growth = jsonObject.getString("Growth");
                    String PCPM = jsonObject.getString("PCPM");
                    String PSaleVal = jsonObject.getString("PSaleVal");
                    String TargtVal = jsonObject.getString("TargtVal");

                    PSalesFFBasedProductModel mPSalesFFBasedProductModel = new PSalesFFBasedProductModel(id, Division_Name, sf_code,
                            Product_Code, Product_Brd_Code, Product_Brd_Name, Product_Name, Pack, Ach, CSaleVal, Growth, PCPM, PSaleVal, TargtVal);
                    FFProductArrayList.add(mPSalesFFBasedProductModel);
                    mPSalesFFBasedProductAdapter.notifyDataSetChanged();
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } else {
                FFProductArrayList.clear();
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
