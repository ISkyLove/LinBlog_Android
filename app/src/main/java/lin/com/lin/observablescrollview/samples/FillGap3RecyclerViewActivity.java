package lin.com.lin.observablescrollview.samples;

import android.support.v7.widget.LinearLayoutManager;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableRecyclerView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;


/**
 * Warning: This example does not work on Android 2.3.
 */
public class FillGap3RecyclerViewActivity extends FillGap3BaseActivity<ObservableRecyclerView> implements ObservableScrollViewCallbacks {

    @Override
    protected ObservableRecyclerView createScrollable() {
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) findViewById(R.id.scroll);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        setDummyDataFew(recyclerView);
        return recyclerView;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fillgap3recyclerview;
    }
}
