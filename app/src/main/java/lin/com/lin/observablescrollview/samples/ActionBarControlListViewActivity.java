package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.AbsListView;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableListView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;


public class ActionBarControlListViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    private static final String TAG = ActionBarControlListViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbarcontrollistview);

        ObservableListView listView = (ObservableListView) findViewById(R.id.list);
        listView.setScrollViewCallbacks(this);
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
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
