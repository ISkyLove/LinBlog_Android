

package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import lin.com.lin.R;
import lin.com.lin.observablescrollview.samples.fragment.FragmentActionBarControlListViewFragment;
import lin.com.lin.observablescrollview.samples.fragment.FragmentTransitionDefaultFragment;

public class FragmentActionBarControlListViewActivity extends ObserverBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentactionbarcontrol);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(FragmentTransitionDefaultFragment.FRAGMENT_TAG) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, new FragmentActionBarControlListViewFragment(),
                    FragmentActionBarControlListViewFragment.FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
