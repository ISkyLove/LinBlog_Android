

package lin.com.lin.observablescrollview.samples;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollView;

public class ToolbarControlScrollViewActivity extends ToolbarControlBaseActivity<ObservableScrollView> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_toolbarcontrolscrollview;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        return (ObservableScrollView) findViewById(R.id.scrollable);
    }
}
