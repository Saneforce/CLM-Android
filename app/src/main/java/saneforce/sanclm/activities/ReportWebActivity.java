package saneforce.sanclm.activities;

import android.app.ProgressDialog;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import saneforce.sanclm.R;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

public class ReportWebActivity extends AppCompatActivity {

    WebView webView;
    String url;
    ProgressDialog progressDialog=null;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_web);
        webView=(WebView)findViewById(R.id.web_view);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        if (progressDialog == null) {
            CommonUtilsMethods commonUtilsMethods=new CommonUtilsMethods(ReportWebActivity.this);
            progressDialog = commonUtilsMethods.createProgressDialog(ReportWebActivity.this);
            progressDialog.show();
        } else {
            progressDialog.show();
        }

        Bundle extra=getIntent().getExtras();
         url=extra.getString("url",null);
       /* webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);*/

        webView.getSettings().setJavaScriptEnabled(true);


        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        Log.v("url_report",url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.v("report_page","are_finished");
                progressDialog.dismiss();
                //view.loadUrl(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //view.loadUrl(url);
                return false;
            }
        });



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWebActivity.super.onBackPressed();
            }
        });

    }
}
