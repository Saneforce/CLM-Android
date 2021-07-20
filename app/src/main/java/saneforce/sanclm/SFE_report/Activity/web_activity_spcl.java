package saneforce.sanclm.SFE_report.Activity;

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


public class web_activity_spcl extends AppCompatActivity {
    ImageView back;
    WebView dvalue;
    String sfcode,fmonth,fyear,divcode,sfname,Bcode,Bname,divis1,db_connPath,url;
    CommonSharedPreference mCommonSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_spcl);

        mCommonSharedPreference=new CommonSharedPreference(this);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        url="http://sansfa.in/";

        back=findViewById(R.id.back_btn_chepro);
        dvalue=findViewById(R.id.weburl);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sfcode=getIntent().getStringExtra("sfcode");
        fmonth=getIntent().getStringExtra("fmon");
        fyear=getIntent().getStringExtra("fyear");
        divcode=getIntent().getStringExtra("division");
        sfname=getIntent().getStringExtra("sfname");
        Bcode=getIntent().getStringExtra("brandcd");
        Bname=getIntent().getStringExtra("brandnm");

        divis1=divcode.replaceAll(",$", "");

        //        http://www.sansfa.in/MIS%20Reports/Visit_Month_Vertical_Wise_Report_Cat_Zoom.aspx?
//        sfcode=MGR0140
//        &FMnth=1
//        &FYear=2021
//        &TMonth=1
//        &TYear=2021
//        &mode=9
//        &sf_name=R%20B%20SRIDHAAR-GM%20SALES%20&%20MARKETING%20-%20GM%20-%20CHENNAI%20-%20sridhaar7425
//        &Division_code=3
//        &Doc_Special_Code=85
//        &Brand_Name=GYN

        Log.d("loaddata", url +
                "MIS%20Reports/Visit_Month_Vertical_Wise_Report_Cat_Zoom.aspx?" +
                "sfcode="+sfcode+
                "&FMnth=" +fmonth+
                "&FYear=" +fyear+
                "&TMonth=" +fmonth+
                "&TYear=" +fyear+
                "&mode=" +"9"+
                "&sf_name=" +sfname+
                "&Division_code=" +divis1+
                "&Doc_Special_Code=" +Bcode+
                "&Brand_Name="+Bname);

        webpage(url +
                "MIS%20Reports/Visit_Month_Vertical_Wise_Report_Cat_Zoom.aspx?" +
                "sfcode="+sfcode+
                "&FMnth=" +fmonth+
                "&FYear=" +fyear+
                "&TMonth=" +fmonth+
                "&TYear=" +fyear+
                "&mode=" +"9"+
                "&sf_name=" +sfname+
                "&Division_code=" +divis1+
                "&Doc_Special_Code=" +Bcode+
                "&Brand_Name="+Bname);
    }

    public void webpage(String url){
        WebSettings settings = dvalue.getSettings();
        settings.setJavaScriptEnabled(true);
        dvalue.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

//        dvalue.getSettings().setBuiltInZoomControls(true);
//        dvalue.getSettings().setUseWideViewPort(true);
//        dvalue.getSettings().setLoadWithOverviewMode(true);

        ProgressDialog progressDialog = new ProgressDialog(web_activity_spcl.this);
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
                Toast.makeText(web_activity_spcl.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        dvalue.loadUrl(url);
    }
}