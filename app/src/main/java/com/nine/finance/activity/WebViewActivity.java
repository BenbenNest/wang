package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nine.finance.R;

public class WebViewActivity extends BaseActivity {


    public static void startActivity(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    WebView webView;

    WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        init();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON); // 设置插件支持
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false); // 设置支持缩放
        settings.setBuiltInZoomControls(false);
        settings.setAllowFileAccess(true); // 启用WebView访问文件数据
        settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置默认缓存模式，根据cache-control决定是否从网络上取数据。
        settings.setDatabaseEnabled(true); // 启用数据库缓存
        settings.setDomStorageEnabled(true); // 启用DOM缓存
        settings.setAppCacheEnabled(true); // 启用应用缓存
        settings.setSavePassword(false); // 关闭webview的自动保存密码
        settings.setAllowContentAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(true); //允许跨域
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);

        //设置不用系统浏览器打开,直接显示在当前Webview
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");
//                beginLoading.setText("开始加载了");

            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
//                endLoading.setText("结束加载了");

            }

        });


        //设置WebChromeClient类
        webView.setWebChromeClient(new WebChromeClient() {

            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
//                mtitle.setText(title);
            }


            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
//                    loading.setText(progress);
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
//                    loading.setText(progress);
                }
            }
        });


    }


}
