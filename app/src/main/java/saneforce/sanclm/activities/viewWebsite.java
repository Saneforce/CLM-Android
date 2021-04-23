package saneforce.sanclm.activities;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import saneforce.sanclm.R;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class viewWebsite extends AppCompatActivity {

    Cursor mCursor;
    DataBaseHandler dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_website);

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.setCancelable(false);
        WebView webView = findViewById(R.id.wbSite);
//        webView.requestFocus();
//        webView.getSettings().setLightTouchEnabled(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setGeolocationEnabled(true);
//        webView.setSoundEffectsEnabled(true);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        webView.getSettings().setUseWideViewPort(true);

        //webView.getSettings().setAppCacheEnabled(true);
        //webView.getSettings().setDomStorageEnabled(true);
//
//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress < 100) {
//                    progressDialog.show();
//                }
//                if (progress == 100) {
//                    progressDialog.dismiss();
//                }
//            }
//        });

        dbh = new DataBaseHandler(this);
        dbh.open();
        mCursor = dbh.select_urlconfiguration();
        Log.v("value_cursor", String.valueOf(mCursor.getCount()));
        String db_pathUrl="";
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                db_pathUrl = mCursor.getString(0) + mCursor.getString(1).replace("/iOSServer/db.php?axn=","");
            }
            String uName = "ANIRBAN4919";
            String uPwd = "TABLETS123";
            String uSgn = "Sign In";
            try {
                String postData = "__EVENTTARGET=" +
                        "&__EVENTARGUMENT=" +
                        "&__VIEWSTATE=/wEPDwULLTEwODkyMTk4MjZkZPfH24xQaJQRCphY0FDwA9tELfmlQwa6Y98JehBXmCbG" +
                        "&__VIEWSTATEGENERATOR: 90059987" +
                        "&__EVENTVALIDATION: /wEdAAQe0G+w9ViPV7EBqjBsbUaZY3plgk0YBAefRz3MyBlTcBd3BKAySfBGtMhMF1x/bI2inihG6d/Xh3PZm3b5AoMQCEN3mgvupIMy9cdEg/TcjpocHqi+VghlOSDbduDU0Ms=" +
                        "&txtUserName=" + URLEncoder.encode(uName, "UTF-8") +
                        "&txtPassWord=" + URLEncoder.encode(uPwd, "UTF-8") +
                        "&btnLogin=" + URLEncoder.encode(uSgn, "UTF-8");
               // webView.postUrl(db_pathUrl, postData.getBytes());
                webView.loadUrl("sansfa.in");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }
}