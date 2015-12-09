

package lin.com.lin.observablescrollview.samples;


import android.support.v4.view.ViewCompat;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableListView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

/**
 * Warning: This example does not work on Android 2.3.
 */
public class FillGapListViewActivity extends FillGapBaseActivity<ObservableListView> implements ObservableScrollViewCallbacks {

    @Override
    protected ObservableListView createScrollable() {
        ObservableListView listView = (ObservableListView) findViewById(R.id.scroll);
        listView.setScrollViewCallbacks(this);
        setDummyDataWithHeader(listView, mFlexibleSpaceImageHeight);
        return listView;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fillgaplistview;
    }

    @Override
    protected void updateViews(int scrollY, boolean animated) {
        super.updateViews(scrollY, animated);

        // Translate list background
        ViewCompat.setTranslationY(mListBackgroundView, ViewCompat.getTranslationY(mHeader));
    }
}
