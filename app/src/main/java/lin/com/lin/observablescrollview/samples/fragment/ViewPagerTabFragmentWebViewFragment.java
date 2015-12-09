package lin.com.lin.observablescrollview.samples.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;

/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class ViewPagerTabFragmentWebViewFragment extends ObserverBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        final ObservableWebView webView = (ObservableWebView) view.findViewById(R.id.scroll);
        webView.loadUrl("file:///android_asset/lipsum.html");
        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            webView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                webView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        return view;
    }
}
