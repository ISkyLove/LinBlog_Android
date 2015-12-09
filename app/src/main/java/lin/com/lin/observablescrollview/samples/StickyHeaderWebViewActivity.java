

package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;

public class StickyHeaderWebViewActivity extends ObserverBaseActivity {

    private View mHeaderView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private boolean mFirstScroll;
    private boolean mDragging;
    private int mBaseTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickyheaderwebview);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mToolbarView = findViewById(R.id.toolbar);

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(mScrollViewScrollCallbacks);

        ObservableWebView mWebView = (ObservableWebView) findViewById(R.id.web);
        mWebView.setScrollViewCallbacks(mWebViewScrollCallbacks);
        mWebView.loadUrl("http://www.baidu.com");
    }

    private ObservableScrollViewCallbacks mScrollViewScrollCallbacks = new ObservableScrollViewCallbacks() {
        @Override
        public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            if (mDragging) {
                int toolbarHeight = mToolbarView.getHeight();
                if (mFirstScroll) {
                    mFirstScroll = false;
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
            mDragging = false;
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
    };

    private ObservableScrollViewCallbacks mWebViewScrollCallbacks = new ObservableScrollViewCallbacks() {
        @Override
        public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        }

        @Override
        public void onDownMotionEvent() {
            // Workaround: WebView inside a ScrollView absorbs down motion events, so observing
            // down motion event from the WebView is required.
            mFirstScroll = mDragging = true;
        }

        @Override
        public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        }
    };

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
