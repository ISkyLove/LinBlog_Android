

package lin.com.lin.observablescrollview.samples;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;

public class SlidingUpWebViewActivity extends SlidingUpBaseActivity<ObservableWebView> implements ObservableScrollViewCallbacks {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_slidingupwebview;
    }

    @Override
    protected ObservableWebView createScrollable() {
        ObservableWebView webView = (ObservableWebView) findViewById(R.id.scroll);
        webView.setScrollViewCallbacks(this);
        webView.loadUrl("http://www.baidu.com");
        return webView;
    }
}
