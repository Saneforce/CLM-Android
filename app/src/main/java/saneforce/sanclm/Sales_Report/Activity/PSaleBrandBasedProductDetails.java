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
import saneforce.sanclm.Sales_Report.Adapter.PSalesBrandBasedProductAdapter;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesBrandBasedProductModel;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class PSaleBrandBasedProductDetails extends AppCompatActivity {
    LinearLayout SubParentLayout;
    RecyclerView rv_brand;
    ImageView back_btn;
    TextView toolbar_sub_title;
    String sf_code,div_Code,db_connPath,sf_name;
    CommonSharedPreference mCommonSharedPreference;

    public static ArrayList<PSalesBrandBasedProductModel> BrandArrayList = new ArrayList<PSalesBrandBasedProductModel>();
    public PSalesBrandBasedProductAdapter mPSalesBrandBasedProductAdapter;

  //  List<CustomerMe> CustomerMeList;
    Api_Interface apiService;
    String curval = null;
    String FromMonth, FromYear, ToMonth, ToYear, DivCode, BrandCode, BrandName, Product_Brd_Code;
    //sqlLite mSqlLite;
    Cursor mCursor;
    public DataBaseHandler dbh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hq_based_brand);

        SubParentLayout = findViewById(R.id.SubParentLayout);
        toolbar_sub_title = findViewById(R.id.toolbar_sub_title);
        back_btn = findViewById(R.id.back_btn);
        rv_brand = findViewById(R.id.rv_brand);
       // mSqlLite = new sqlLite(PSaleBrandBasedProductDetails.this);

        DivCode=getIntent().getStringExtra("Div_Code");
        DivCode=DivCode+",";
        BrandCode=getIntent().getStringExtra("Brand_Code");
        BrandName=getIntent().getStringExtra("Brand_Name");

        dbh=new DataBaseHandler(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

        toolbar_sub_title.setText(BrandName);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPSalesBrandBasedProductAdapter = new PSalesBrandBasedProductAdapter(BrandArrayList, PSaleBrandBasedProductDetails.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PSaleBrandBasedProductDetails.this);
        rv_brand.setLayoutManager(mLayoutManager);
        rv_brand.setItemAnimator(new DefaultItemAnimator());
        rv_brand.setAdapter(mPSalesBrandBasedProductAdapter);

        dbh.open();

        String Brand_Based_Brand_Data="";
        if( Dcrdatas.primarysalesselectedtdtag==0){
            mCursor = dbh.getBrandBasedBrandData_MTD(BrandCode);
            if (mCursor.moveToFirst()) {
                Brand_Based_Brand_Data = mCursor.getString(mCursor.getColumnIndex("BrandBasedBrand_MTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==1){
            mCursor = dbh.getBrandBasedBrandData_QTD(BrandCode);
            if (mCursor.moveToFirst()) {
                Brand_Based_Brand_Data = mCursor.getString(mCursor.getColumnIndex("BrandBasedBrand_QTD_Value"));
            }
        } else if( Dcrdatas.primarysalesselectedtdtag==2){
            mCursor = dbh.getBrandBasedBrandData_YTD(BrandCode);
            if (mCursor.moveToFirst()) {
                Brand_Based_Brand_Data = mCursor.getString(mCursor.getColumnIndex("BrandBasedBrand_YTD_Value"));
            }
        }

        if (((mCursor != null) && (mCursor.getCount() > 0))) {
            LoadBrandBasedBrandDetails(Brand_Based_Brand_Data);
        } else {
            GetBrandBasedBrandDetails();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void GetBrandBasedBrandDetails() {
        if (isNetworkConnected()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(PSaleBrandBasedProductDetails.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            apiService =  RetroClient.getClient(db_connPath).create(Api_Interface.class);

//            sqlLite sqlLite;
//            sqlLite = new sqlLite(PSaleBrandBasedProductDetails.this);
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
            //QryParam.put("axn", "get/primary_Brand_product");
            QryParam.put("divisionCode", div_Code+",");
            QryParam.put("sfCode", sf_code);
            QryParam.put("rSF", sf_code);
            //For MTD :
            if (Dcrdatas.primarysalesselectedtdtag == 0) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_MTD(PSaleBrandBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_MTD(PSaleBrandBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_MTD(PSaleBrandBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_MTD(PSaleBrandBasedProductDetails.this);
            }
            //For QTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 1) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_QTD(PSaleBrandBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_QTD(PSaleBrandBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_QTD(PSaleBrandBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_QTD(PSaleBrandBasedProductDetails.this);
            }
            //For YTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 2) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_YTD(PSaleBrandBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_YTD(PSaleBrandBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_YTD(PSaleBrandBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_YTD(PSaleBrandBasedProductDetails.this);
            }
            //For DTD :
            else if (Dcrdatas.primarysalesselectedtdtag == 3) {
                FromMonth = Shared_Common_Pref.getPrimarySalesReportFromMonth_DTD(PSaleBrandBasedProductDetails.this);
                FromYear = Shared_Common_Pref.getPrimarySalesReportFromYear_DTD(PSaleBrandBasedProductDetails.this);
                ToMonth = Shared_Common_Pref.getPrimarySalesReportToMonth_DTD(PSaleBrandBasedProductDetails.this);
                ToYear = Shared_Common_Pref.getPrimarySalesReportToYear_DTD(PSaleBrandBasedProductDetails.this);
            }
            QryParam.put("Brand_product", BrandCode);
            QryParam.put("fmonth", FromMonth);
            QryParam.put("fyear", FromYear);
            QryParam.put("tomonth", ToMonth);
            QryParam.put("toyear", ToYear);
            Log.e("BrandBasedBrandDetail_1", QryParam.toString());
            Call<JsonArray> call = apiService.getBrandProductDataAsJArray( QryParam);
            Log.d("BrandBasedBrandDetail_2", QryParam.toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200 || response.code() == 201) {
                        Log.d("BrandBasedBrandDetail_3", response.body() + " " + response.body().toString());
                        Log.d("BrandBasedBrandDetail_4", response.toString());
                        if (!response.body().toString().isEmpty()) {
                            try {
                              //  dbh.open();
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                if (jsonArray.length() > 0) {
                                    BrandArrayList.clear();
//                                    mPSalesBrandBasedProductAdapter.notifyDataSetChanged();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        String Division_Name = jsonObject.getString("Division_Name");
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

                                        PSalesBrandBasedProductModel mPSalesBrandBasedProductModel = new PSalesBrandBasedProductModel(Division_Name, Product_Code, Product_Brd_Code, Product_Brd_Name, Product_Name, Pack, Ach, CSaleVal, Growth, PCPM, PSaleVal, TargtVal);
                                        BrandArrayList.add(mPSalesBrandBasedProductModel);
                                        mPSalesBrandBasedProductAdapter.notifyDataSetChanged();
                                    }
                                    if( Dcrdatas.primarysalesselectedtdtag==0){
                                       dbh.insertBrandBasedBrand_MTD(Product_Brd_Code, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==1){
                                        dbh.insertBrandBasedBrand_QTD(Product_Brd_Code, response.body().toString());
                                    } else if( Dcrdatas.primarysalesselectedtdtag==2){
                                        dbh.insertBrandBasedBrand_YTD(Product_Brd_Code, response.body().toString());
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
                    Log.e("BrandBasedBrandDetail_5", "onFailure : " + t.toString());
                }
            });
        } else {
            Toast.makeText(PSaleBrandBasedProductDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadBrandBasedBrandDetails(String Brand_Based_Brand_Data) {
        final ProgressDialog mProgressDialog = new ProgressDialog(PSaleBrandBasedProductDetails.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        try {
            JSONArray jsonArray = new JSONArray(Brand_Based_Brand_Data);
            if (jsonArray.length() > 0) {
                BrandArrayList.clear();
//                mPSalesBrandBasedProductAdapter.notifyDataSetChanged();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String Division_Name = jsonObject.getString("Division_Name");
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

                    PSalesBrandBasedProductModel mPSalesBrandBasedProductModel = new PSalesBrandBasedProductModel(Division_Name, Product_Code, Product_Brd_Code, Product_Brd_Name, Product_Name, Pack, Ach, CSaleVal, Growth, PCPM, PSaleVal, TargtVal);
                    BrandArrayList.add(mPSalesBrandBasedProductModel);
                    mPSalesBrandBasedProductAdapter.notifyDataSetChanged();
                }
                Log.d("ListCount_1", String.valueOf(BrandArrayList.size()));
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } else {
                BrandArrayList.clear();
                mPSalesBrandBasedProductAdapter.notifyDataSetChanged();
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
