package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;

public class FlexibleSpaceToolbarWebViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    private View mFlexibleSpaceView;
    private View mToolbarView;
    private TextView mTitleView;
    private int mFlexibleSpaceHeight;
    private View mWebViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexiblespacetoolbarwebview);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFlexibleSpaceView = findViewById(R.id.flexible_space);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(getTitle());
        setTitle(null);
        mToolbarView = findViewById(R.id.toolbar);

        mWebViewContainer = findViewById(R.id.webViewContainer);

        final ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/lipsum.html");

        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_height);
        int flexibleSpaceAndToolbarHeight = mFlexibleSpaceHeight + getActionBarSize();

        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) webView.getLayoutParams();
        layoutParams.topMargin = flexibleSpaceAndToolbarHeight;
        webView.setLayoutParams(layoutParams);

        mFlexibleSpaceView.getLayoutParams().height = flexibleSpaceAndToolbarHeight;

        ScrollUtils.addOnGlobalLayoutListener(mTitleView, new Runnable() {
            @Override
            public void run() {
                updateFlexibleSpaceText(scrollView.getCurrentScrollY());
            }
        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        updateFlexibleSpaceText(scrollY);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void updateFlexibleSpaceText(final int scrollY) {
        ViewCompat.setTranslationY(mFlexibleSpaceView, -scrollY);
        int adjustedScrollY = (int) ScrollUtils.getFloat(scrollY, 0, mFlexibleSpaceHeight);

        // Special logic for WebView.
        adjustTopMargin(mWebViewContainer, adjustedScrollY <= mFlexibleSpaceHeight ? 0 : mFlexibleSpaceHeight + getActionBarSize());

        float maxScale = (float) (mFlexibleSpaceHeight - mToolbarView.getHeight()) / mToolbarView.getHeight();
        float scale = maxScale * ((float) mFlexibleSpaceHeight - adjustedScrollY) / mFlexibleSpaceHeight;

        ViewCompat.setPivotX(mTitleView, 0);
        ViewCompat.setPivotY(mTitleView, 0);
        ViewCompat.setScaleX(mTitleView, 1 + scale);
        ViewCompat.setScaleY(mTitleView, 1 + scale);
        int maxTitleTranslationY = mToolbarView.getHeight() + mFlexibleSpaceHeight - (int) (mTitleView.getHeight() * (1 + scale));
        int titleTranslationY = (int) (maxTitleTranslationY * ((float) mFlexibleSpaceHeight - adjustedScrollY) / mFlexibleSpaceHeight);
        ViewCompat.setTranslationY(mTitleView, titleTranslationY);
    }

    private void adjustTopMargin(View view, int topMargin) {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();

        if (layoutParams.topMargin == topMargin) {
            return;
        }

        layoutParams.topMargin = topMargin;

        view.setLayoutParams(layoutParams);
    }
}
