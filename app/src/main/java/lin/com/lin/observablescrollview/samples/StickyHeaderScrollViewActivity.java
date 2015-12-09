

package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;


public class StickyHeaderScrollViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    private View mHeaderView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mBaseTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickyheaderscrollview);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mToolbarView = findViewById(R.id.toolbar);

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            if (firstScroll) {
                float currentHeaderTranslationY = ViewCompat.getTranslationY(mHeaderView);
                if (-toolbarHeight < currentHeaderTranslationY) {
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

        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            int toolbarHeight = mToolbarView.getHeight();
            int scrollY = mScrollView.getCurrentScrollY();
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (!toolbarIsShown() && !toolbarIsHidden()) {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewCompat.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewCompat.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewCompat.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewCompat.animate(mHeaderView).cancel();
            ViewCompat.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
    }

    private void hideToolbar() {
        float headerTranslationY = ViewCompat.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewCompat.animate(mHeaderView).cancel();
            ViewCompat.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
    }
}
