package lin.com.lin.observablescrollview.samples.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableGridView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;

public class ViewPagerTabGridViewFragment extends ObserverBaseFragment {

    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, false);

        Activity parentActivity = getActivity();
        final ObservableGridView gridView = (ObservableGridView) view.findViewById(R.id.scroll);
        setDummyDataWithHeader(gridView, inflater.inflate(R.layout.padding, gridView, false));

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified position after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(gridView, new Runnable() {
                    @Override
                    public void run() {
                        // scrollTo() doesn't work, should use setSelection()
                        gridView.setSelection(initialPosition);
                    }
                });
            }

            // TouchInterceptionViewGroup should be a parent view other than ViewPager.
            // This is a workaround for the issue #117:
            // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
            gridView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));

            gridView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }
}
