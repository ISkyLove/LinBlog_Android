package lin.com.lin.observablescrollview.samples;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

/**
 * Warning: This example does not work on Android 2.3.
 */
public class FillGap2ScrollViewActivity extends FillGap2BaseActivity<ObservableScrollView> implements ObservableScrollViewCallbacks {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fillgapscrollview;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);
        return scrollView;
    }
}
