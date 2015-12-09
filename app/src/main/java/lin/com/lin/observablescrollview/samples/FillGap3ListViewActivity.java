package lin.com.lin.observablescrollview.samples;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableListView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

/**
 * Warning: This example does not work on Android 2.3.
 */
public class FillGap3ListViewActivity extends FillGap3BaseActivity<ObservableListView> implements ObservableScrollViewCallbacks {

    @Override
    protected ObservableListView createScrollable() {
        ObservableListView listView = (ObservableListView) findViewById(R.id.scroll);
        listView.setScrollViewCallbacks(this);
        setDummyDataFew(listView);
        return listView;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fillgap3listview;
    }
}
