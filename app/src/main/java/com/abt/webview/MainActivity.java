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
    private Button mBtnBaidu, mBtnPay, mBtnCallJS, mBtnRefresh;
    private WebView mWebView;

    private void initViews() {
        mBtnBaidu = findViewById(R.id.jump_baidu);
        mBtnPay = findViewById(R.id.jump_pay);
        mBtnCallJS = findViewById(R.id.call_js);
        mBtnRefresh = findViewById(R.id.refresh);
        mBtnBaidu.setOnClickListener(this);
        mBtnPay.setOnClickListener(this);
        mBtnCallJS.setOnClickListener(this);
        mBtnRefresh.setOnClickListener(this);
        mWebView = findViewById(R.id.webview);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initWebView();
    }
    
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSBridge(), MallConstant.JS_INTERFACE);
        mWebView.loadUrl("file:///android_asset/index.html");
        //mWebView.loadUrl("https://www.baidu.com");// 需要特殊设置才能显示出来
        //mWebView.loadUrl("http://www.baidu.com");
        //mWebView.loadUrl("www.baidu.com");// 无法加载出来
        
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if(url.startsWith(Constant.ALI_PAYS) || url.startsWith(Constant.ALI_PAY)) {
                    try {
                        MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception e) {
                        noAlipayDialog(MainActivity.this);
                    }
                    return true;
                }
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("file:///android_asset/error.html");
            }
        });
     }
    
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i(TAG, "onReceivedTitle: " + title);
                setTitle(title);
            }
        });
    }

    private void noAlipayDialog(final Activity context) {
        new AlertDialog.Builder(context)
                .setMessage("未检测到支付宝客户端，请安装后重试。")
                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                        context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_baidu:
                mWebView.loadUrl("https://www.baidu.com/");
                break;
            case R.id.jump_pay:
                mWebView.loadUrl("http://192.168.2.28:8088/index1.html");
                break;
            case R.id.call_js:
                callJS();
                break;
            case R.id.refresh:
                mWebView.reload();
                break;
        }
    }

    private void callJS() {
        mWebView.evaluateJavascript("javascript:callJS('js')", new ValueCallback() {
            @Override
            public void onReceiveValue(Object value) {
                if (null != value) {
                    Log.i(TAG, "onReceiveValue: " + value.toString());
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}

