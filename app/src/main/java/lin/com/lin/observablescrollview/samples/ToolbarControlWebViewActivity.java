

package lin.com.lin.observablescrollview.samples;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;

public class ToolbarControlWebViewActivity extends ToolbarControlBaseActivity<ObservableWebView> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_toolbarcontrolwebview;
    }

    @Override
    protected ObservableWebView createScrollable() {
        ObservableWebView webView = (ObservableWebView) findViewById(R.id.scrollable);
        webView.loadUrl("file:///android_asset/lipsum.html");
        return webView;
    }
}
