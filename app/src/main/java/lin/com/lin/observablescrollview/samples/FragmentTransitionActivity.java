

package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import lin.com.lin.R;
import lin.com.lin.observablescrollview.samples.fragment.FragmentTransitionDefaultFragment;

/**
 * This doesn't include ObservableScrollViews,
 * just shows how to show/hide Toolbar on the parent Activity of Fragment
 * to help you implement a screen that uses Fragments.
 */
public class FragmentTransitionActivity extends ObserverBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmenttransition);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initFragment();
    }

    /**
     * Fragment should be added programmatically.
     * Using fragment tag in XML causes IllegalStateException on rotation of screen
     * or restoring states of activity.
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(FragmentTransitionDefaultFragment.FRAGMENT_TAG) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment, new FragmentTransitionDefaultFragment(), FragmentTransitionDefaultFragment.FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
