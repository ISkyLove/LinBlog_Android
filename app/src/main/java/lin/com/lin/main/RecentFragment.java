package lin.com.lin.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import lin.com.lin.R;
import lin.com.lin.base.LBaseFragment;
import lin.com.lin.main.adapter.RecentAdapter;
import lin.com.lin.observablescrollview.samples.utils.ObservableUtils;
import lin.com.lin.observablescrollview.samples.ObserverMainActivity;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableRecyclerView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;

/**
 *最近
 */
public class RecentFragment extends LBaseFragment implements ObservableScrollViewCallbacks {
    View view;
    Context mContext;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private View mRecyclerViewBackground;
    private TextView mTitleView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        view = inflater.inflate(R.layout.main_recent_layout, container, false);
        view.findViewById(R.id.recent_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ObserverMainActivity.class));
            }
        });
        initView();
        return view;
    }

    private void initView(){
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = ObservableUtils.getActionBarSize(mContext);

        ObservableRecyclerView recyclerView = (ObservableRecyclerView) view.findViewById(R.id.recent_recycler);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(false);
        final View headerView = LayoutInflater.from(mContext).inflate(R.layout.recent_recycler_header, null);
        headerView.post(new Runnable() {
            @Override
            public void run() {
                headerView.getLayoutParams().height = mFlexibleSpaceImageHeight;
            }
        });
        RecentAdapter mRecentAdapter=new RecentAdapter(mContext,headerView);
        recyclerView.setAdapter(mRecentAdapter);
        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            items.add("Item " + i);
        }
        mRecentAdapter.changeData(items);


        mImageView = view.findViewById(R.id.recent_image);
        mOverlayView = view.findViewById(R.id.recent_overlay);

        mTitleView = (TextView) view.findViewById(R.id.recent_title);
        mTitleView.setText("十年磨一剑");

        // mRecyclerViewBackground makes RecyclerView's background except header view.
        mRecyclerViewBackground = view.findViewById(R.id.recent_list_background);

        //since you cannot programmatically add a header view to a RecyclerView we added an empty view as the header
        // in the adapter and then are shifting the views OnCreateView to compensate
        final float scale = 1 + MAX_TEXT_SCALE_DELTA;
        mRecyclerViewBackground.post(new Runnable() {
            @Override
            public void run() {
                ViewCompat.setTranslationY(mRecyclerViewBackground, mFlexibleSpaceImageHeight);
            }
        });
        ViewCompat.setTranslationY(mOverlayView, mFlexibleSpaceImageHeight);
        mTitleView.post(new Runnable() {
            @Override
            public void run() {
                ViewCompat.setTranslationY(mTitleView, (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale));
                ViewCompat.setPivotX(mTitleView, 0);
                ViewCompat.setPivotY(mTitleView, 0);
                ViewCompat.setScaleX(mTitleView, scale);
                ViewCompat.setScaleY(mTitleView, scale);
            }
        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewCompat.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewCompat.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewCompat.setTranslationY(mRecyclerViewBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewCompat.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 0));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewCompat.setPivotY(mTitleView, 0);
        ViewCompat.setScaleX(mTitleView, scale);
        ViewCompat.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewCompat.setTranslationY(mTitleView, titleTranslationY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewCompat.setPivotX(mTitleView, view.findViewById(android.R.id.content).getWidth());
        } else {
            ViewCompat.setPivotX(mTitleView, 0);
        }
    }
}
