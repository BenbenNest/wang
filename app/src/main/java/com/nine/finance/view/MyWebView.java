package com.nine.finance.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by changqing on 2018/2/25.
 */

public class MyWebView extends WebView {

    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        WebSettings settings = getSettings();
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
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        //设置不用系统浏览器打开,直接显示在当前Webview
        setWebViewClient(new WebViewClient() {

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

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                // handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
                // handleMessage(Message msg);// 进行其他处理
            }

//            注：在重写WebViewClient的onReceivedSslError方法时，注意一定要去除onReceivedSslError方法的super.onReceivedSslError(view, handler, error);，否则设置无效。


        });

//        WebChromeClient的bug，按返回键的时候，是不会执行onReceivedTitle这个方法的，所以返回的时候title就不会动态的改变了，后来百度到有方法可以让他动态的改变，那就是自己维护，何为自己维护呢：
//
//        就是自己创建一个栈，也就是list，来动态添加，删除你浏览的网页


        //设置WebChromeClient类
        setWebChromeClient(new WebChromeClient() {

            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
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
