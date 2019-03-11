package com.abt.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WebView mWebView;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSBridge(), "JSBridge");

        //mWebView.loadUrl("https://www.baidu.com");// 需要特殊设置才能显示出来
        mWebView.loadUrl("http://www.baidu.com");
        //mWebView.loadUrl("www.baidu.com");// 无法加载出来

        //mWebView.reload();// 刷新页面

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i(TAG, "onReceivedTitle: "+title);// 页面的标题
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            // 不用跳转到第三方浏览器，直接在WebView上加载
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 自定义跳转
                if (request.getUrl().getPath().endsWith("AboutActivity")) {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    return true;
                }

                view.loadUrl(String.valueOf(request));
                return super.shouldOverrideUrlLoading(view, request);
            }

            // 处理页面加载错误
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("file:///android_asset/error.html");
            }
        });

        // 异步执行JS代码，并获取返回值
        mWebView.evaluateJavascript("javascript: 方法名('参数,需要转为字符串')", new ValueCallback() {
            @Override
            public void onReceiveValue(Object value) {
                // 这里的value即为对应JS方法的返回值
            }
        });


    }

}

