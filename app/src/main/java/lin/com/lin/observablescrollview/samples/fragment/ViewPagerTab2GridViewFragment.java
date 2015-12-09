package lin.com.lin.observablescrollview.samples.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableGridView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;

public class ViewPagerTab2GridViewFragment extends ObserverBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, false);

        Activity parentActivity = getActivity();
        final ObservableGridView gridView = (ObservableGridView) view.findViewById(R.id.scroll);
        setDummyData(gridView);
        gridView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            gridView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }
}
