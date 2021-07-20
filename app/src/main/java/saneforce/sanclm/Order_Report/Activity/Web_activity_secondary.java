package saneforce.sanclm.Order_Report.Activity;



import android.app.ProgressDialog;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import saneforce.sanclm.R;
import saneforce.sanclm.api_Interface.AppConfig;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;


public class Web_activity_secondary extends AppCompatActivity {
    ImageView back;
    WebView dvalue;
    String sfcode,fmonth,fyear,tmonth,tyear,divcode,Bcode,Bname,sfname,db_connPath,url;
    CommonSharedPreference mCommonSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_secondary);

        mCommonSharedPreference=new CommonSharedPreference(this);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);

        url="http://sansfa.in";


        back=findViewById(R.id.back_btn_chepro);
        dvalue=findViewById(R.id.weburl);

        sfcode=getIntent().getStringExtra("sfcode");
        fmonth=getIntent().getStringExtra("fmon");
        fyear=getIntent().getStringExtra("fyear");
        tmonth=getIntent().getStringExtra("tmon");
        tyear=getIntent().getStringExtra("tyear");
        divcode=getIntent().getStringExtra("Dcode");
        Bcode=getIntent().getStringExtra("Brandcd");
        Bname=getIntent().getStringExtra("Brandname");
        sfname=getIntent().getStringExtra("sfname");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d("loaddata", AppConfig.getWeburl() +
                "/MasterFiles/AnalysisReports/rpt_Primary_Sale_StockistWise_Product.aspx?" +
                "sf_code="+sfcode+
                "&Frm_Month=" +fmonth+
                "&Frm_year=" +fyear+
                "&To_year=" +tyear+
                "&To_Month=" +tmonth+
                "&div_Code=" +divcode+
                "&Brand_Code=" +Bcode+
                "&Brand_Name=" +Bname+
                "&sf_name=" +sfname+
                "&selectedValue="+"5"+
                "&Div_New="+"undefined");

        // http://sansfa.in/MasterFiles/AnalysisReports/rpt_Primary_Sale_StockistWise_Product.aspx?sf_code=5,2,4,3,&Frm_Month=12&Frm_year=2020&To_year=2020&To_Month=12&div_Code=ALL&Brand_Code=40&Brand_Name=BIF%20GRP&sf_name=MANISH%20SHARMA%20-%20CSMO&selectedValue=5&Div_New=ALL



        webpage(url +
                "/MasterFiles/AnalysisReports/rpt_Secondary_StkProd_Bill_View.aspx?" +
                "sf_code="+sfcode+
                "&Frm_Month=" +fmonth+
                "&Frm_year=" +fyear+
                "&To_year=" +tyear+
                "&To_Month=" +tmonth+
                "&div_Code=" +divcode+
                "&Brand_Code=" +Bcode+
                "&Brand_Name=" +Bname+
                "&sf_name=" +sfname+
                "&Mode="+"5" +
                "&Div_New="+"undefined");

    }
    public void webpage(String url){
        WebSettings settings = dvalue.getSettings();
        settings.setJavaScriptEnabled(true);
        dvalue.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

//        dvalue.getSettings().setBuiltInZoomControls(true);
//        dvalue.getSettings().setUseWideViewPort(true);
//        dvalue.getSettings().setLoadWithOverviewMode(true);

        final ProgressDialog progressDialog = new ProgressDialog(Web_activity_secondary.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        dvalue.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(Web_activity_secondary.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        dvalue.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}