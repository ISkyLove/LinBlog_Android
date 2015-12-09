package lin.com.lin.observablescrollview.samples.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.com.lin.R;

/**
 * This fragment shows how to show/hide toolbar of the parent Activity on fragment transitions.
 */
public class FragmentTransitionSecondFragment extends ObserverBaseFragment {

    public static final String FRAGMENT_TAG = "second";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hideToolbar();
        return inflater.inflate(R.layout.fragment_fragmenttransition_second, container, false);
    }

    @Override
    public void onDestroyView() {
        showToolbar();
        super.onDestroyView();
    }

    private void showToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar ab = activity.getSupportActionBar();
            if (ab != null) {
                ab.show();
            }
        }
    }

    private void hideToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar ab = activity.getSupportActionBar();
            if (ab != null) {
                ab.hide();
            }
        }
    }
}
