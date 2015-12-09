

package lin.com.lin.observablescrollview.samples;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;
import lin.com.observablescrollviewlibrary.observablescrollview.Scrollable;
import lin.com.observablescrollviewlibrary.observablescrollview.TouchInterceptionFrameLayout;


public abstract class SlidingUpBaseActivity<S extends Scrollable> extends ObserverBaseActivity implements ObservableScrollViewCallbacks {

    private static final String STATE_SLIDING_STATE = "slidingState";
    private static final int SLIDING_STATE_TOP = 0;
    private static final int SLIDING_STATE_MIDDLE = 1;
    private static final int SLIDING_STATE_BOTTOM = 2;

    private View mHeader;
    private View mHeaderBar;
    private View mHeaderOverlay;
    private View mHeaderFlexibleSpace;
    private TextView mTitle;
    private TextView mToolbarTitle;
    private View mImageView;
    private View mFab;
    private Toolbar mToolbar;
    private S mScrollable;
    private TouchInterceptionFrameLayout mInterceptionLayout;

    // Fields that just keep constants like resource values
    private int mActionBarSize;
    private int mIntersectionHeight;
    private int mHeaderBarHeight;
    private int mSlidingSlop;
    private int mSlidingHeaderBlueSize;
    private int mColorPrimary;
    private int mFlexibleSpaceImageHeight;
    private int mToolbarColor;
    private int mFabMargin;

    // Fields that needs to saved
    private int mSlidingState;

    // Temporary states
    private boolean mFabIsShown;
    private boolean mMoved;
    private float mInitialY;
    private float mMovedDistanceY;
    private float mScrollYOnDownMotion;

    // These flags are used for changing header colors.
    private boolean mHeaderColorIsChanging;
    private boolean mHeaderColorChangedToBottom;
    private boolean mHeaderIsAtBottom;
    private boolean mHeaderIsNotAtBottom;
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(SlidingUpBaseActivity.this, "floating action button clicked", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        ViewCompat.setScaleY(mToolbar, 0);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("");
        }

        mToolbarColor = getResources().getColor(R.color.primary);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mToolbar.setTitle("");

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mIntersectionHeight = getResources().getDimensionPixelSize(R.dimen.intersection_height);
        mHeaderBarHeight = getResources().getDimensionPixelSize(R.dimen.header_bar_height);
        mSlidingSlop = getResources().getDimensionPixelSize(R.dimen.sliding_slop);
        mActionBarSize = getActionBarSize();
        mColorPrimary = getResources().getColor(R.color.primary);
        mSlidingHeaderBlueSize = getResources().getDimensionPixelSize(R.dimen.sliding_overlay_blur_size);

        mHeader = findViewById(R.id.header);
        mHeaderBar = findViewById(R.id.header_bar);
        mHeaderOverlay = findViewById(R.id.header_overlay);
        mHeaderFlexibleSpace = findViewById(R.id.header_flexible_space);
        mImageView = findViewById(R.id.image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideOnClick();
            }
        });
        mScrollable = createScrollable();

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(fabClickListener);
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);

        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.scroll_wrapper);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(getTitle());
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(mTitle.getText());
        ViewCompat.setAlpha(mToolbarTitle, 0);
        ViewCompat.setTranslationY(mTitle, (mHeaderBarHeight - mActionBarSize) / 2);

        if (savedInstanceState == null) {
            mSlidingState = SLIDING_STATE_BOTTOM;
        }

        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                if (mFab != null) {
                    ViewCompat.setTranslationX(mFab, mTitle.getWidth() - mFabMargin - mFab.getWidth());
                    ViewCompat.setTranslationY(mFab, ViewCompat.getX(mTitle) - (mFab.getHeight() / 2));
                }
                changeSlidingState(mSlidingState, false);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // All the related temporary states can be restored by slidingState
        mSlidingState = savedInstanceState.getInt(STATE_SLIDING_STATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SLIDING_STATE, mSlidingState);
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
            final int minInterceptionLayoutY = -mIntersectionHeight;

            // slight fix for untappable floating action button for larger screens
            Rect fabRect = new Rect();
            mFab.getHitRect(fabRect);
            // if the user's touch is within the floating action button's touch area, don't intercept
            if (fabRect.contains((int) ev.getX(), (int) ev.getY())) {
                return false;
            } else {
                return minInterceptionLayoutY < (int) ViewCompat.getY(mInterceptionLayout)
                        || (moving && mScrollable.getCurrentScrollY() - diffY < 0);
            }
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
            mScrollYOnDownMotion = mScrollable.getCurrentScrollY();
            mInitialY = ViewCompat.getTranslationY(mInterceptionLayout);
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            mMoved = true;
            float translationY = ViewCompat.getTranslationY(mInterceptionLayout) - mScrollYOnDownMotion + diffY;
            if (translationY < -mIntersectionHeight) {
                translationY = -mIntersectionHeight;
            } else if (getScreenHeight() - mHeaderBarHeight < translationY) {
                translationY = getScreenHeight() - mHeaderBarHeight;
            }

            slideTo(translationY, true);

            mMovedDistanceY = ViewCompat.getTranslationY(mInterceptionLayout) - mInitialY;
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            if (!mMoved) {
                // Invoke slide animation only on header view
                Rect outRect = new Rect();
                mHeader.getHitRect(outRect);
                if (outRect.contains((int) ev.getX(), (int) ev.getY())) {
                    slideOnClick();
                }
            } else {
                stickToAnchors();
            }
            mMoved = false;
        }
    };

    private void changeSlidingState(final int slidingState, boolean animated) {
        mSlidingState = slidingState;
        float translationY = 0;
        switch (slidingState) {
            case SLIDING_STATE_TOP:
                translationY = 0;
                break;
            case SLIDING_STATE_MIDDLE:
                translationY = getAnchorYImage();
                break;
            case SLIDING_STATE_BOTTOM:
                translationY = getAnchorYBottom();
                break;
        }
        if (animated) {
            slideWithAnimation(translationY);
        } else {
            slideTo(translationY, false);
        }
    }

    private void slideOnClick() {
        float translationY = ViewCompat.getTranslationY(mInterceptionLayout);
        if (translationY == getAnchorYBottom()) {
            changeSlidingState(SLIDING_STATE_MIDDLE, true);
        } else if (translationY == getAnchorYImage()) {
            changeSlidingState(SLIDING_STATE_BOTTOM, true);
        }
    }

    private void stickToAnchors() {
        // Slide to some points automatically
        if (0 < mMovedDistanceY) {
            // Sliding down
            if (mSlidingSlop < mMovedDistanceY) {
                // Sliding down to an anchor
                if (getAnchorYImage() < ViewCompat.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_BOTTOM, true);
                } else {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                }
            } else {
                // Sliding up(back) to an anchor
                if (getAnchorYImage() < ViewCompat.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                } else {
                    changeSlidingState(SLIDING_STATE_TOP, true);
                }
            }
        } else if (mMovedDistanceY < 0) {
            // Sliding up
            if (mMovedDistanceY < -mSlidingSlop) {
                // Sliding up to an anchor
                if (getAnchorYImage() < ViewCompat.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                } else {
                    changeSlidingState(SLIDING_STATE_TOP, true);
                }
            } else {
                // Sliding down(back) to an anchor
                if (getAnchorYImage() < ViewCompat.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_BOTTOM, true);
                } else {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                }
            }
        }
    }

    private void slideTo(float translationY, final boolean animated) {
        ViewCompat.setTranslationY(mInterceptionLayout, translationY);

        if (translationY < 0) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
            lp.height = (int) -translationY + getScreenHeight();
            mInterceptionLayout.requestLayout();
        }

        // Translate title
        float hiddenHeight = translationY < 0 ? -translationY : 0;
        ViewCompat.setTranslationY(mTitle, Math.min(mIntersectionHeight, (mHeaderBarHeight + hiddenHeight - mActionBarSize) / 2));

        // Translate image
        float imageAnimatableHeight = getScreenHeight() - mHeaderBarHeight;
        float imageTranslationScale = imageAnimatableHeight / (imageAnimatableHeight - mImageView.getHeight());
        float imageTranslationY = Math.max(0, imageAnimatableHeight - (imageAnimatableHeight - translationY) * imageTranslationScale);
        ViewCompat.setTranslationY(mImageView, imageTranslationY);

        // Show/hide FAB
        if (ViewCompat.getTranslationY(mInterceptionLayout) < mFlexibleSpaceImageHeight) {
            hideFab(animated);
        } else {
            if (animated) {
                ViewCompat.animate(mToolbar).scaleY(0).setDuration(200).start();
            } else {
                ViewCompat.setScaleY(mToolbar, 0);
            }
            showFab(animated);
        }
        if (ViewCompat.getTranslationY(mInterceptionLayout) <= mFlexibleSpaceImageHeight) {
            if (animated) {
                ViewCompat.animate(mToolbar).scaleY(1).setDuration(200).start();
            } else {
                ViewCompat.setScaleY(mToolbar, 1);
            }
            mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
        }

        changeToolbarTitleVisibility();
        changeHeaderBarColorAnimated(animated);
        changeHeaderOverlay();
    }

    private void slideWithAnimation(float toY) {
        float layoutTranslationY = ViewCompat.getTranslationY(mInterceptionLayout);
        if (layoutTranslationY != toY) {
            ValueAnimator animator = ValueAnimator.ofFloat(ViewCompat.getTranslationY(mInterceptionLayout), toY).setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    slideTo((float) animation.getAnimatedValue(), true);
                }
            });
            animator.start();
        }
    }

    private void changeToolbarTitleVisibility() {
        if (ViewCompat.getTranslationY(mInterceptionLayout) <= mIntersectionHeight) {
            if (ViewCompat.getAlpha(mToolbarTitle) != 1) {
                ViewCompat.animate(mToolbarTitle).cancel();
                ViewCompat.animate(mToolbarTitle).alpha(1).setDuration(200).start();
            }
        } else if (ViewCompat.getAlpha(mToolbarTitle) != 0) {
            ViewCompat.animate(mToolbarTitle).cancel();
            ViewCompat.animate(mToolbarTitle).alpha(0).setDuration(200).start();
        } else {
            ViewCompat.setAlpha(mToolbarTitle, 0);
        }
    }

    private void changeHeaderBarColorAnimated(boolean animated) {
        if (mHeaderColorIsChanging) {
            return;
        }
        boolean shouldBeWhite = getAnchorYBottom() == ViewCompat.getTranslationY(mInterceptionLayout);
        if (!mHeaderIsAtBottom && !mHeaderColorChangedToBottom && shouldBeWhite) {
            mHeaderIsAtBottom = true;
            mHeaderIsNotAtBottom = false;
            if (animated) {
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(100);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float alpha = (float) animation.getAnimatedValue();
                        mHeaderColorIsChanging = (alpha != 1);
                        changeHeaderBarColor(alpha);
                    }
                });
                animator.start();
            } else {
                changeHeaderBarColor(1);
            }
        } else if (!mHeaderIsNotAtBottom && !shouldBeWhite) {
            mHeaderIsAtBottom = false;
            mHeaderIsNotAtBottom = true;
            if (animated) {
                ValueAnimator animator = ValueAnimator.ofFloat(1, 0).setDuration(100);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float alpha = (float) animation.getAnimatedValue();
                        mHeaderColorIsChanging = (alpha != 0);
                        changeHeaderBarColor(alpha);
                    }
                });
                animator.start();
            } else {
                changeHeaderBarColor(0);
            }
        }
    }

    private void changeHeaderBarColor(float alpha) {
        mHeaderBar.setBackgroundColor(ScrollUtils.mixColors(mColorPrimary, Color.WHITE, alpha));
        mTitle.setTextColor(ScrollUtils.mixColors(Color.WHITE, Color.BLACK, alpha));
        mHeaderColorChangedToBottom = (alpha == 1);
    }

    private void changeHeaderOverlay() {
        final float translationY = ViewCompat.getTranslationY(mInterceptionLayout);
        if (translationY <= mToolbar.getHeight() - mSlidingHeaderBlueSize) {
            mHeaderOverlay.setVisibility(View.VISIBLE);
            mHeaderFlexibleSpace.getLayoutParams().height = (int) (mToolbar.getHeight() - mSlidingHeaderBlueSize - translationY);
            mHeaderFlexibleSpace.requestLayout();
            mHeaderOverlay.requestLayout();
        } else {
            mHeaderOverlay.setVisibility(View.INVISIBLE);
        }
    }

    private void showFab(boolean animated) {
        if (mFab == null) {
            return;
        }
        if (!mFabIsShown) {
            if (animated) {
                ViewCompat.animate(mFab).cancel();
                ViewCompat.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            } else {
                ViewCompat.setScaleX(mFab, 1);
                ViewCompat.setScaleY(mFab, 1);
            }
            mFabIsShown = true;
        } else {
            // Ensure that FAB is shown
            ViewCompat.setScaleX(mFab, 1);
            ViewCompat.setScaleY(mFab, 1);
        }
    }

    private void hideFab(boolean animated) {
        if (mFab == null) {
            return;
        }
        if (mFabIsShown) {
            if (animated) {
                ViewCompat.animate(mFab).cancel();
                ViewCompat.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            } else {
                ViewCompat.setScaleX(mFab, 0);
                ViewCompat.setScaleY(mFab, 0);
            }
            mFabIsShown = false;
        } else {
            // Ensure that FAB is hidden
            ViewCompat.setScaleX(mFab, 0);
            ViewCompat.setScaleY(mFab, 0);
        }
    }

    private float getAnchorYBottom() {
        return getScreenHeight() - mHeaderBarHeight;
    }

    private float getAnchorYImage() {
        return mImageView.getHeight();
    }
}
