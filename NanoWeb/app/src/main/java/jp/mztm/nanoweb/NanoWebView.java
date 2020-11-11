package jp.mztm.nanoweb;

import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NanoWebView {
    private static final String TAG = "NanoWebView";
    public WebView webView;
    public NanoWebView(final WebView webView){
        this.webView = webView;
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        webView.loadUrl("http://" + MainActivity.hostname + ":" + MainActivity.port);
    }
    public void setKeyCode(int keyCode){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()){
                webView.goBack();
            }
        }
    }
}
