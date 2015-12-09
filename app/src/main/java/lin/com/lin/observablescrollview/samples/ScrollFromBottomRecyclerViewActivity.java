

package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableRecyclerView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;

/**
 * This is a sample of using RecyclerView that scrolls from the bottom.
 * It returns incorrect scrollY and commit 'a99a0de' fixed a part of this problem.
 * (Related to the issue #3)
 * It still returns incorrect scrollY and this is a known issue.
 * Please don't submit it as a new issue.
 * (Pull request to fix this is greatly appreciated!)
 */
public class ScrollFromBottomRecyclerViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    private View mHeaderView;
    private View mToolbarView;
    private ObservableRecyclerView mRecyclerView;
    private int mBaseTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickyheaderrecyclerview);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mToolbarView = findViewById(R.id.toolbar);

        mRecyclerView = (ObservableRecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setScrollViewCallbacks(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        View headerView = LayoutInflater.from(this).inflate(R.layout.recycler_header, null);
        setDummyDataWithHeader(mRecyclerView, headerView);

        ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
            @Override
            public void run() {
                int count = mRecyclerView.getAdapter().getItemCount() - 1;
                int position = count == 0 ? 1 : count > 0 ? count : 0;
                mRecyclerView.scrollToPosition(position);
            }
        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int toolbarHeight = mToolbarView.getHeight();
        if (dragging || scrollY < toolbarHeight) {
            if (firstScroll) {
                float currentHeaderTranslationY = ViewCompat.getTranslationY(mHeaderView);
                if (-toolbarHeight < currentHeaderTranslationY && toolbarHeight < scrollY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewCompat.animate(mHeaderView).cancel();
            ViewCompat.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        float headerTranslationY = ViewCompat.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (scrollState == ScrollState.UP) {
            if (toolbarHeight < mRecyclerView.getCurrentScrollY()) {
                if (headerTranslationY != -toolbarHeight) {
                    ViewCompat.animate(mHeaderView).cancel();
                    ViewCompat.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
                }
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarHeight < mRecyclerView.getCurrentScrollY()) {
                if (headerTranslationY != 0) {
                    ViewCompat.animate(mHeaderView).cancel();
                    ViewCompat.animate(mHeaderView).translationY(0).setDuration(200).start();
                }
            }
        }
    }
}