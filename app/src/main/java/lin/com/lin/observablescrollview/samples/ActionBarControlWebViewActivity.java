package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v7.app.ActionBar;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;

public class ActionBarControlWebViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbarcontrolwebview);

        ObservableWebView webView = (ObservableWebView) findViewById(R.id.web);
        webView.setScrollViewCallbacks(this);
        webView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
