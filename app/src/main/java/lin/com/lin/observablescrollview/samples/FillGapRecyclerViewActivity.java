package lin.com.lin.observablescrollview.samples;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableRecyclerView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;


/**
 * Warning: This example does not work on Android 2.3.
 */
public class FillGapRecyclerViewActivity extends FillGapBaseActivity<ObservableRecyclerView> implements ObservableScrollViewCallbacks {

    @Override
    protected ObservableRecyclerView createScrollable() {
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) findViewById(R.id.scroll);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        setDummyDataWithHeader(recyclerView, mFlexibleSpaceImageHeight);
        return recyclerView;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fillgaprecyclerview;
    }

    @Override
    protected void updateViews(int scrollY, boolean animated) {
        super.updateViews(scrollY, animated);

        // Translate list background
        ViewCompat.setTranslationY(mListBackgroundView, ViewCompat.getTranslationY(mHeader));
    }
}
