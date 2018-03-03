package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nine.finance.R;
import com.nine.finance.view.CommonHeadView;
import com.nine.finance.view.MyWebView;

import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends BaseActivity {

//    1. 启用mixed content
//    在Android5.0中，WebView方面做了些修改，如果你的系统target api为21以上:
//
//    系统默认禁止了mixed content和第三方cookie。可以使用setMixedContentMode() 和 setAcceptThirdPartyCookies()以分别启用。
//    系统现在可以智能选择HTML文档的portion来绘制。这种新特性可以减少内存footprint并改进性能。若要一次性渲染整个HTML文档，可以调用这个方法enableSlowWholeDocumentDraw()
//    如果你的app的target api低于21:系统允许mixed content和第三方cookie，并且总是一次性渲染整个HTML文档。
//    在使用WebView的类中添加如下代码：

    public static void startActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    MyWebView webView;
    CommonHeadView headView;
    WebSettings settings;
    private List<String> loadHistoryUrls=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                if (loadHistoryUrls.size() > 1) {
                    //重新加载之前的页面,这里为了让标题也能正常显示
                    String url = loadHistoryUrls.get(loadHistoryUrls.size() - 2);
                    loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
                    if (loadHistoryUrls.size() > 0) {
                        loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
                    }
                    webView.loadUrl(url);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            if (loadHistoryUrls.size() > 1) {
                //重新加载之前的页面,这里为了让标题也能正常显示
                String url = loadHistoryUrls.get(loadHistoryUrls.size() - 2);
                loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
                if (loadHistoryUrls.size() > 0) {
                    loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
                }
                webView.loadUrl(url);
            }
//            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        headView = (CommonHeadView) findViewById(R.id.head_view);
        webView = (MyWebView) findViewById(R.id.webview);
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
//         android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

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
                loadHistoryUrls.add(url);
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
//        就是自己创建一个栈，也就是list，来动态添加，删除你浏览的网页



        //设置WebChromeClient类
        webView.setWebChromeClient(new WebChromeClient() {

            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
                if (headView != null) {
                    headView.setTitle(title);
                }
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
