package lin.com.lin.observablescrollview.samples;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;
import lin.com.observablescrollviewlibrary.observablescrollview.Scrollable;
import lin.com.observablescrollviewlibrary.observablescrollview.TouchInterceptionFrameLayout;


/**
 * Another implementation of FillGap Activity.
 * These examples uses TouchInterceptionFrameLayout to enable scrolling effect
 * even when there are few items and cannot be scrolled.
 *
 * Known issue: this example just moves TouchInterceptionFrameLayout on dragging
 * and it has no velocity, so as soon as the UP/CANCEL event occurred,
 * translation will be stopped.
 */
public abstract class FillGap3BaseActivity<S extends Scrollable> extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    private static final String STATE_TRANSLATION_Y = "translationY";

    protected View mHeader;
    protected View mHeaderBar;
    private View mImageView;
    private View mHeaderBackground;
    private TextView mTitle;
    private S mScrollable;
    private TouchInterceptionFrameLayout mInterceptionLayout;

    // Fields that needs to saved
    private float mInitialTranslationY;

    // Fields that just keep constants like resource values
    protected int mActionBarSize;
    protected int mFlexibleSpaceImageHeight;
    protected int mIntersectionHeight;
    private int mHeaderBarHeight;

    // Temporary states
    private float mScrollYOnDownMotion;

    private float mPrevTranslationY;
    private boolean mGapIsChanging;
    private boolean mGapHidden;
    private boolean mReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();
        mHeaderBarHeight = getResources().getDimensionPixelSize(R.dimen.header_bar_height);

        // Even when the top gap has began to change, header bar still can move
        // within mIntersectionHeight.
        mIntersectionHeight = getResources().getDimensionPixelSize(R.dimen.intersection_height);

        mImageView = findViewById(R.id.image);
        mHeader = findViewById(R.id.header);
        mHeaderBar = findViewById(R.id.header_bar);
        mHeaderBackground = findViewById(R.id.header_background);

        mScrollable = createScrollable();

        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.scroll_wrapper);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(getTitle());
        ViewCompat.setTranslationY(mTitle, (mHeaderBarHeight - mActionBarSize) / 2);
        setTitle(null);

        if (savedInstanceState == null) {
            mInitialTranslationY = mFlexibleSpaceImageHeight - mHeaderBarHeight;
        }

        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                mReady = true;
                updateViews(mInitialTranslationY, false);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInitialTranslationY = savedInstanceState.getFloat(STATE_TRANSLATION_Y);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(STATE_TRANSLATION_Y, ViewCompat.getTranslationY(mInterceptionLayout));
        super.onSaveInstanceState(outState);
    }

    protected abstract int getLayoutResId();
    protected abstract S createScrollable();

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            return getMinInterceptionLayoutY() < (int) ViewCompat.getY(mInterceptionLayout)
                    || (moving && mScrollable.getCurrentScrollY() - diffY < 0);
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
            mScrollYOnDownMotion = mScrollable.getCurrentScrollY();
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            float translationY = ViewCompat.getTranslationY(mInterceptionLayout) - mScrollYOnDownMotion + diffY;
            float minTranslationY = getMinInterceptionLayoutY();
            if (translationY < minTranslationY) {
                translationY = minTranslationY;
            } else if (mFlexibleSpaceImageHeight - mHeaderBarHeight < translationY) {
                translationY = mFlexibleSpaceImageHeight - mHeaderBarHeight;
            }

            updateViews(translationY, true);
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
        }
    };

    protected void updateViews(float translationY, boolean animated) {
        // If it's ListView, onScrollChanged is called before ListView is laid out (onGlobalLayout).
        // This causes weird animation when onRestoreInstanceState occurred,
        // so we check if it's laid out already.
        if (!mReady) {
            return;
        }
        ViewCompat.setTranslationY(mInterceptionLayout, translationY);

        // Translate image
        ViewCompat.setTranslationY(mImageView, (translationY - (mFlexibleSpaceImageHeight - mHeaderBarHeight)) / 2);

        // Translate title
        ViewCompat.setTranslationY(mTitle, Math.min(mIntersectionHeight, (mHeaderBarHeight - mActionBarSize) / 2));

        // Show/hide gap
        boolean scrollUp = translationY < mPrevTranslationY;
        if (scrollUp) {
            if (translationY <= mActionBarSize) {
                changeHeaderBackgroundHeightAnimated(false, animated);
            }
        } else {
            if (mActionBarSize <= translationY) {
                changeHeaderBackgroundHeightAnimated(true, animated);
            }
        }
        mPrevTranslationY = translationY;
    }

    private void changeHeaderBackgroundHeightAnimated(boolean shouldShowGap, boolean animated) {
        if (mGapIsChanging) {
            return;
        }
        final int heightOnGapShown = mHeaderBar.getHeight();
        final int heightOnGapHidden = mHeaderBar.getHeight() + mActionBarSize;
        final float from = mHeaderBackground.getLayoutParams().height;
        final float to;
        if (shouldShowGap) {
            if (!mGapHidden) {
                // Already shown
                return;
            }
            to = heightOnGapShown;
        } else {
            if (mGapHidden) {
                // Already hidden
                return;
            }
            to = heightOnGapHidden;
        }
        if (animated) {
            ViewCompat.animate(mHeaderBackground).cancel();
            ValueAnimator a = ValueAnimator.ofFloat(from, to);
            a.setDuration(100);
            a.setInterpolator(new AccelerateDecelerateInterpolator());
            a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float height = (float) animation.getAnimatedValue();
                    changeHeaderBackgroundHeight(height, to, heightOnGapHidden);
                }
            });
            a.start();
        } else {
            changeHeaderBackgroundHeight(to, to, heightOnGapHidden);
        }
    }

    private void changeHeaderBackgroundHeight(float height, float to, float heightOnGapHidden) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mHeaderBackground.getLayoutParams();
        lp.height = (int) height;
        lp.topMargin = (int) (mHeaderBar.getHeight() - height);
        mHeaderBackground.requestLayout();
        mGapIsChanging = (height != to);
        if (!mGapIsChanging) {
            mGapHidden = (height == heightOnGapHidden);
        }
    }

    private float getMinInterceptionLayoutY() {
        return mActionBarSize - mIntersectionHeight;
        // If you want to move header bar to the top, return 0 instead.
        //return 0;
    }
}
