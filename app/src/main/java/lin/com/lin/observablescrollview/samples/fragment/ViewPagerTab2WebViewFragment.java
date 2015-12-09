package lin.com.lin.observablescrollview.samples.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import lin.com.lin.R;
import lin.com.lin.View.LWebView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;

public class ViewPagerTab2WebViewFragment extends ObserverBaseFragment {
    LWebView mLWebView;
    ProgressBar mProgressBar;
    public static final String URL = "http://www.baidu.com";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.web_progress);
        mLWebView = (LWebView) view.findViewById(R.id.scroll);

        initWebView();
//        final ObservableWebView webView = (ObservableWebView) view.findViewById(R.id.scroll);
//        webView.loadUrl("http://www.baidu.com");
        Activity parentActivity = getActivity();
        mLWebView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            mLWebView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }

    private void initWebView() {
        mLWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                view.loadData("<!DOCTYPE html><html><title>error</title><body><h1><div id=\'b\'><a onclick=\"window.Lin.Reload()\">error</a></div></h1></body></html>", "text/html", "UTF-8");
            }
        });
        mLWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                mProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // TODO Auto-generated method stub

            }

        });
        mLWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mLWebView.getSettings().setBlockNetworkImage(true);
        mLWebView.getSettings().setBuiltInZoomControls(true);
        mLWebView.getSettings().setJavaScriptEnabled(true);
        mLWebView.addJavascriptInterface(new Object() {
            public void Reload() {
                mLWebView.loadUrl(URL);
            }
        }, "Lin");
        mLWebView.loadUrl(URL);
    }
}
