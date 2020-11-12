package com.js.library.widget.webview;

import android.webkit.WebViewClient;

/**
 * 作者：wangjianxiong 创建时间：2020/4/24
 * <p>
 * 功能描述：
 */
public class MiddlewareWebClientBase extends WebViewClientDelegate {

    private MiddlewareWebClientBase mMiddleWrareWebClientBase;
    private static String TAG = MiddlewareWebClientBase.class.getSimpleName();

    MiddlewareWebClientBase(MiddlewareWebClientBase client) {
        super(client);
        this.mMiddleWrareWebClientBase = client;
    }

    protected MiddlewareWebClientBase(WebViewClient client) {
        super(client);
    }

    protected MiddlewareWebClientBase() {
        super(null);
    }

    final MiddlewareWebClientBase next() {
        return this.mMiddleWrareWebClientBase;
    }

    @Override
    final void setDelegate(WebViewClient delegate) {
        super.setDelegate(delegate);

    }

    final MiddlewareWebClientBase enq(MiddlewareWebClientBase middleWrareWebClientBase) {
        setDelegate(middleWrareWebClientBase);
        this.mMiddleWrareWebClientBase = middleWrareWebClientBase;
        return middleWrareWebClientBase;
    }

}
