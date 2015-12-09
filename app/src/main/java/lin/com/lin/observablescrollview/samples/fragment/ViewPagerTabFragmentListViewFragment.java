

package lin.com.lin.observablescrollview.samples.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableListView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class ViewPagerTabFragmentListViewFragment extends ObserverBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);

        final ObservableListView listView = (ObservableListView) view.findViewById(R.id.scroll);
        setDummyData(listView);

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            listView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        return view;
    }
}
