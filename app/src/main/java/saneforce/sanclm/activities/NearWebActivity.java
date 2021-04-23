package saneforce.sanclm.activities;


import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import saneforce.sanclm.R;

public class NearWebActivity extends AppCompatActivity {
    WebView webView;
    String dlat,dlng;
    String slat,slng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_web);
        webView=(WebView)findViewById(R.id.webview);

        Bundle extra=getIntent().getExtras();
        slat=extra.getString("slat");
        slng=extra.getString("slng");
        dlat=extra.getString("dlat");
        dlng=extra.getString("dlng");

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://maps.google.com/maps?" + "saddr="+slat+","+slng+"&daddr="+dlat+","+dlng);


    }
}
