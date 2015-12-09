

package lin.com.lin.observablescrollview.samples;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

/**
 * Warning: This example does not work on Android 2.3.
 */
public class FillGap3ScrollViewActivity extends FillGap3BaseActivity<ObservableScrollView> implements ObservableScrollViewCallbacks {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fillgap3scrollview;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);
        return scrollView;
    }
}
