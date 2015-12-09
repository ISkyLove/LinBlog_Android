package lin.com.lin.observablescrollview.samples.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import lin.com.lin.R;

/**
 * This is a fragment to cause fragment transition(replacement).
 */
public class FragmentTransitionDefaultFragment extends ObserverBaseFragment {

    public static final String FRAGMENT_TAG = "default";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmenttransition_default, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list);
        setDummyData(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showNextFragment();
            }
        });
        return view;
    }

    private void showNextFragment() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment, new FragmentTransitionSecondFragment(), FragmentTransitionSecondFragment.FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
        fm.executePendingTransactions();
    }
}
