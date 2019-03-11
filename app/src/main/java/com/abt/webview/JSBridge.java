package com.abt.webview;

import android.webkit.JavascriptInterface;

public class JSBridge {

    public JSBridge() {

    }

    @JavascriptInterface
    public String getStr() {
        return "str";
    }

    // Android4.2版本以上，本地方法要加上注解@JavascriptInterface，否则会找不到方法。
    private Object getJSBridge() {
        Object insertObj = new Object(){
            @JavascriptInterface
            public String foo(){
                return "foo";
            }

            @JavascriptInterface
            public String foo2(final String param){
                return "foo2:" + param;
            }

        };
        return insertObj;
    }
}
