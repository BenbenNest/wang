package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.jeremy.wang.R;

public class WebViewActivity extends AppCompatActivity {

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        init();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }


}
