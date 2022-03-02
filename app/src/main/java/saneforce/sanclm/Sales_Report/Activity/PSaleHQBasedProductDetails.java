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
import saneforce.sanclm.Sales_Report.Adapter.PSalesHQBasedProductAdapter;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesHQBasedProductModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class PSaleHQBasedProductDetails extends AppCompatActivity {
    LinearLayout SubParentLayout;
    RecyclerView rv_brand;
    ImageView back_btn;
    TextView toolbar_sub_title;

    public static ArrayList<PSalesHQBasedProductModel> ProductArrayList = new ArrayList<PSalesHQBasedProductModel>();
    public PSalesHQBasedProductAdapter mPSalesHQBasedProductAdapter;

    //List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear, DivCode, HQCode, HQName, BrandCode, BrandName, Product_Brd_Code;
    //sqlLite mSqlLite;
    Cursor mCursor;

    CommonSharedPreference mCommonSharedPreference;
    String sf_code,div_Code,db_connPath,sf_name,subsf_code;
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
        HQCode=getIntent().getStringExtra("HQ_Code");
        HQName=getIntent().getStringExtra("HQ_Name");
        BrandCode=getIntent().getStringExtra("Brand_Code");
        BrandName=getIntent().getStringExtra("Brand_Name");
        //mSqlLite = new sqlLite(PSaleHQBasedProductDetails.this);

        toolbar_sub_title.setText(HQName+" - "+BrandName);

        dbh=new DataBaseHandler(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

                back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPSalesHQBasedProductAdapter = new PSalesHQBasedProductAdapter(ProductArrayList, PSaleHQBasedProductDetails.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PSaleHQBasedProductDetails.this);
        rv_brand.setLayoutManager(mLayoutManager);
        rv_brand.setItemAnimator(new DefaultItemAnimator());
        rv_brand.setAdapter(mPSalesHQBasedProductAdapter);

        dbh.open();
        String HQBrandProd_Data="";
        if( Dcrdatas.primarysalesselectedtdtag==0){
            mCursor = dbh.getHQBasedProdData_MTD(BrandCode);
            if (mCursor.moveToFirst()) {
                HQBrandProd_Data = mCursor.getString(mCursor.getColumnIndex("HQBasedProd_MTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==1){
            mCursor = dbh.getHQBasedProdData_QTD(BrandCode);
            if (mCursor.moveToFirst()) {
                HQBrandProd_Data = mCursor.getString(mCursor.getColumnIndex("HQBasedProd_QTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==2){
            mCursor = dbh.getHQBasedProdData_YTD(BrandCode);
            if (mCursor.moveToFirst()) {
                HQBrandProd_Data = mCursor.getString(mCursor.getColumnIndex("HQBasedProd_YTD_Value"));
            }
        }

        if (((mCursor != null) && (mCursor.getCount() > 0))) {
            LoadHQBasedProdDetails(HQBrandProd_Data);
        } else {
            GetHQBasedProdDetails();
        }
    }
    

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void GetHQBasedProdDetails() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(PSaleHQBasedProductDetails.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService =  RetroClient.getClient(db_connPath).create(Api_Interface.class);

//            sqlLite sqlLite;
//            sqlLite = new sqlLite(PSaleHQBasedProductDetails.this);
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
           // QryParam.put("axn", "get/primary_Hq_Brand_product");
            QryParam.put("divisionCode", DivCode);
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
            //For MTD :
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_MTD(PSaleHQBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_MTD(PSaleHQBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_MTD(PSaleHQBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_MTD(PSaleHQBasedProductDetails.this);
            }
            //For QTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_QTD(PSaleHQBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_QTD(PSaleHQBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_QTD(PSaleHQBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_QTD(PSaleHQBasedProductDetails.this);
            }
            //For YTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_YTD(PSaleHQBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_YTD(PSaleHQBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_YTD(PSaleHQBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_YTD(PSaleHQBasedProductDetails.this);
            }
            //For DTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_DTD(PSaleHQBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_DTD(PSaleHQBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_DTD(PSaleHQBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_DTD(PSaleHQBasedProductDetails.this);
            }
            QryParam.put("fmonth", FromMonth);
            QryParam.put("fyear", FromYear);
            QryParam.put("tomonth", ToMonth);
            QryParam.put("toyear", ToYear);
            QryParam.put("hq_cat_id", HQCode);
            QryParam.put("hq_name", HQName);
            QryParam.put("Brand_product", BrandCode);
            Log.e("HQBasedProdDetails_1", QryParam.toString());
            Call<JsonArray> call = apiService.getHqProductDataAsJArray(QryParam);
            Log.d("HQBasedProdDetails_2", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("HQBasedProdDetails_3", response.body() + " " + response.body().toString());
                        Log.d("HQBasedProdDetails_4", response.toString());
                        if (!response.body().toString().isEmpty()) {
                            try {
                                //dbh.open();
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    ProductArrayList.clear();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        String id = jsonObject.getString("id");
                                        String Division_Name = jsonObject.getString("Division_Name");
                                        String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                                        String HQ_Name = jsonObject.getString("HQ Name");
                                        String Product_Code = jsonObject.getString("Product_Code");
                                        Product_Brd_Code = jsonObject.getString("Product_Brd_Code");
                                        String Product_Brd_Name = jsonObject.getString("Product_Brd_Name");
                                        String Product_Name = jsonObject.getString("Product Name");
                                        String Pack = jsonObject.getString("Pack");
                                        String Ach = jsonObject.getString("Ach");
                                        String CSaleVal = jsonObject.getString("CSaleVal");
                                        String Growth = jsonObject.getString("Growth");
                                        String PCPM = jsonObject.getString("PCPM");
                                        String PSaleVal = jsonObject.getString("PSaleVal");
                                        String TargtVal = jsonObject.getString("TargtVal");

                                        PSalesHQBasedProductModel mPSalesHQBasedProductModel = new PSalesHQBasedProductModel(id,Division_Name,SF_Cat_Code,
                                                HQ_Name,Product_Code,Product_Brd_Code,Product_Brd_Name,Product_Name,Pack,Ach,CSaleVal,Growth,PCPM,PSaleVal,TargtVal);
                                        ProductArrayList.add(mPSalesHQBasedProductModel);
                                        mPSalesHQBasedProductAdapter.notifyDataSetChanged();
                                    }
                                    if( Dcrdatas.primarysalesselectedtdtag==0){
                                        dbh.insertHQBasedProd_MTD(Product_Brd_Code, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==1){
                                        dbh.insertHQBasedProd_QTD(Product_Brd_Code, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==2){
                                        dbh.insertHQBasedProd_YTD(Product_Brd_Code, response.body().toString());
                                    }
                                    dbh.close();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                } else {
                                    ProductArrayList.clear();
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ProductArrayList.clear();
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }
                    } else {
                        ProductArrayList.clear();
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
            Toast.makeText(PSaleHQBasedProductDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadHQBasedProdDetails(String HQBrand_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(PSaleHQBasedProductDetails.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        try {
            JSONArray jsonArray = new JSONArray(HQBrand_Data);
            if (jsonArray.length() > 0) {
                ProductArrayList.clear();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String id = jsonObject.getString("id");
                    String Division_Name = jsonObject.getString("Division_Name");
                    String SF_Cat_Code = jsonObject.getString("SF_Cat_Code");
                    String HQ_Name = jsonObject.getString("HQ Name");
                    String Product_Code = jsonObject.getString("Product_Code");
                    Product_Brd_Code = jsonObject.getString("Product_Brd_Code");
                    String Product_Brd_Name = jsonObject.getString("Product_Brd_Name");
                    String Product_Name = jsonObject.getString("Product Name");
                    String Pack = jsonObject.getString("Pack");
                    String Ach = jsonObject.getString("Ach");
                    String CSaleVal = jsonObject.getString("CSaleVal");
                    String Growth = jsonObject.getString("Growth");
                    String PCPM = jsonObject.getString("PCPM");
                    String PSaleVal = jsonObject.getString("PSaleVal");
                    String TargtVal = jsonObject.getString("TargtVal");

                    PSalesHQBasedProductModel mPSalesHQBasedProductModel = new PSalesHQBasedProductModel(id,Division_Name,SF_Cat_Code,
                            HQ_Name,Product_Code,Product_Brd_Code,Product_Brd_Name,Product_Name,Pack,Ach,CSaleVal,Growth,PCPM,PSaleVal,TargtVal);
                    ProductArrayList.add(mPSalesHQBasedProductModel);
                    mPSalesHQBasedProductAdapter.notifyDataSetChanged();
                }
                Log.d("ListCount_1", String.valueOf(ProductArrayList.size()));
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } else {
                ProductArrayList.clear();
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
