

package lin.com.lin.observablescrollview.samples;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;
import lin.com.observablescrollviewlibrary.observablescrollview.Scrollable;

/**
 * Warning: This example does not work on Android 2.3.
 *
 * @param <S> Scrollable
 */
public abstract class FillGapBaseActivity<S extends Scrollable> extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    protected View mHeader;
    protected int mFlexibleSpaceImageHeight;
    protected View mHeaderBar;
    protected View mListBackgroundView;
    protected int mActionBarSize;
    protected int mIntersectionHeight;

    private View mImage;
    private View mHeaderBackground;
    private int mPrevScrollY;
    private boolean mGapIsChanging;
    private boolean mGapHidden;
    private boolean mReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();

        // Even when the top gap has began to change, header bar still can move
        // within mIntersectionHeight.
        mIntersectionHeight = getResources().getDimensionPixelSize(R.dimen.intersection_height);

        mImage = findViewById(R.id.image);
        mHeader = findViewById(R.id.header);
        mHeaderBar = findViewById(R.id.header_bar);
        mHeaderBackground = findViewById(R.id.header_background);
        mListBackgroundView = findViewById(R.id.list_background);

        final S scrollable = createScrollable();

        ((TextView) findViewById(R.id.title)).setText(getTitle());
        setTitle(null);

        ScrollUtils.addOnGlobalLayoutListener((View) scrollable, new Runnable() {
            @Override
            public void run() {
                mReady = true;
                updateViews(scrollable.getCurrentScrollY(), false);
            }
        });
    }

    protected abstract int getLayoutResId();
    protected abstract S createScrollable();

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        updateViews(scrollY, true);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    protected void updateViews(int scrollY, boolean animated) {
        // If it's ListView, onScrollChanged is called before ListView is laid out (onGlobalLayout).
        // This causes weird animation when onRestoreInstanceState occurred,
        // so we check if it's laid out already.
        if (!mReady) {
            return;
        }
        // Translate image
        ViewCompat.setTranslationY(mImage, -scrollY / 2);

        // Translate header
        ViewCompat.setTranslationY(mHeader, getHeaderTranslationY(scrollY));

        // Show/hide gap
        final int headerHeight = mHeaderBar.getHeight();
        boolean scrollUp = mPrevScrollY < scrollY;
        if (scrollUp) {
            if (mFlexibleSpaceImageHeight - headerHeight - mActionBarSize <= scrollY) {
                changeHeaderBackgroundHeightAnimated(false, animated);
            }
        } else {
            if (scrollY <= mFlexibleSpaceImageHeight - headerHeight - mActionBarSize) {
                changeHeaderBackgroundHeightAnimated(true, animated);
            }
        }
        mPrevScrollY = scrollY;
    }

    protected float getHeaderTranslationY(int scrollY) {
        final int headerHeight = mHeaderBar.getHeight();
        int headerTranslationY = mActionBarSize - mIntersectionHeight;
        if (0 <= -scrollY + mFlexibleSpaceImageHeight - headerHeight - mActionBarSize + mIntersectionHeight) {
            headerTranslationY = -scrollY + mFlexibleSpaceImageHeight - headerHeight;
        }
        return headerTranslationY;
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
}
