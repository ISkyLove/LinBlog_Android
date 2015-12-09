

package lin.com.lin.observablescrollview.samples;

import android.util.Log;
import android.widget.AbsListView;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableListView;


public class ToolbarControlListViewActivity extends ToolbarControlBaseActivity<ObservableListView> {

    private static final String TAG = ToolbarControlListViewActivity.class.getSimpleName();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_toolbarcontrollistview;
    }

    @Override
    protected ObservableListView createScrollable() {
        ObservableListView listView = (ObservableListView) findViewById(R.id.scrollable);
        setDummyData(listView);

        // ObservableListView uses setOnScrollListener, but it still works.
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.v(TAG, "onScrollStateChanged: " + scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.v(TAG, "onScroll: firstVisibleItem: " + firstVisibleItem + " visibleItemCount: " + visibleItemCount + " totalItemCount: " + totalItemCount);
            }
        });
        return listView;
    }
}
