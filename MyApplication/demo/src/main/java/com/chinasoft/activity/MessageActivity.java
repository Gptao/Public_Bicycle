package com.chinasoft.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.demo.R;

public class MessageActivity extends Activity {
private WebView  webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://weibo.com/u/5918987931");
    }
}
