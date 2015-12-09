package lin.com.lin.observablescrollview.samples.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableRecyclerView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class ViewPagerTabFragmentRecyclerViewFragment extends ObserverBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        final ObservableRecyclerView recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);
        setDummyData(recyclerView);

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            recyclerView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                recyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        return view;
    }
}
