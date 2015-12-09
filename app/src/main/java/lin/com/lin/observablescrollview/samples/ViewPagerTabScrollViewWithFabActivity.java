

package lin.com.lin.observablescrollview.samples;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import lin.com.lin.observablescrollview.samples.fragment.ViewPagerTabScrollViewWithFabFragment;

/**
 * This example shows how to handle scroll events on both the parent Activity and Fragments.
 * (Handling FAB is not the main purpose)
 *
 * SlidingTabLayout and SlidingTabStrip are from google/iosched:
 * https://github.com/google/iosched
 */
public class ViewPagerTabScrollViewWithFabActivity extends ViewPagerTabScrollViewActivity {

    @Override
    protected NavigationAdapter newViewPagerAdapter() {
        return new NavigationAdapter(getSupportFragmentManager());
    }

    private static class NavigationAdapter extends ViewPagerTabScrollViewActivity.NavigationAdapter {
        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment newFragment() {
            return new ViewPagerTabScrollViewWithFabFragment();
        }
    }
}
