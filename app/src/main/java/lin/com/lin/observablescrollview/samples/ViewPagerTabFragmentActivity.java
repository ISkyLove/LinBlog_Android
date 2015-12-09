

package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import lin.com.lin.R;
import lin.com.lin.observablescrollview.samples.fragment.ViewPagerTabFragmentParentFragment;

/**
 * This activity just provides a toolbar.
 * Toolbar is manipulated by ViewPagerTabFragmentParentFragment.
 */
public class ViewPagerTabFragmentActivity extends ObserverBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpagertabfragment);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(ViewPagerTabFragmentParentFragment.FRAGMENT_TAG) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment, new ViewPagerTabFragmentParentFragment(),
                    ViewPagerTabFragmentParentFragment.FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
