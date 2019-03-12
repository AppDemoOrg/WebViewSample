package com.abt.webview;

import android.webkit.JavascriptInterface;

public class JSBridge {

    private static final String TAG = "JSBridge";

    @JavascriptInterface
    public void setValue(String value) {
        Log.d(TAG, "JS setValue: " + value);
    }

    @JavascriptInterface
    public String getValue() {
        String str = "JS_Value";
        Log.d(TAG, "JS getValue: " +str);
        return str;
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
