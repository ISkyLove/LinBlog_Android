

package lin.com.lin.observablescrollview.samples;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

public class SlidingUpScrollViewActivity extends SlidingUpBaseActivity<ObservableScrollView> implements ObservableScrollViewCallbacks {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_slidingupscrollview;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);
        return scrollView;
    }
}
