package com.smilehacker.dongxi.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.Utils.UrlUtils;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;

public class MerchantActivity extends Activity {

    private final static String TAG = MerchantActivity.class.getName();

    private String mMerchantUrl;
    private Dongxi mDongxi;
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_merchant);
        mWebView = (WebView) findViewById(R.id.webview);

        init();
    }

    private void init() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDongxi = getIntent().getParcelableExtra(Constants.KEY_DONGXI);
        mMerchantUrl = mDongxi.purchaseUrl;

        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings =  mWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        final Activity activity = this;

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                activity.setProgress(newProgress * 100);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                activity.setTitle(view.getTitle());
            }
        });

        mWebView.loadUrl(UrlUtils.getUrlFromDoubanLink(mMerchantUrl));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
