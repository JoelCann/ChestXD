package com.tech.pcreate.ChestXD;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MoreinfoActivity extends AppCompatActivity {

    String diseasename =  "";

    String url = "";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);

        diseasename = getIntent().getStringExtra( "diseasename");
        webView = findViewById(R.id.webview);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);

        if(diseasename.equals("Atelectasis")){
            url = "https://www.mayoclinic.org/diseases-conditions/atelectasis/symptoms-causes/syc-20369684";
        }
        else if(diseasename.equals("")){
            url = "";
        }
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);
    }
}