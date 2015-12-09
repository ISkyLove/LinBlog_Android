package lin.com.lin.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.com.lin.R;
import lin.com.lin.base.LBaseFragment;

/**
 * 推荐
 */
public class RecommendFragment extends LBaseFragment {
    View view;
    Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        view = inflater.inflate(R.layout.main_recommend_layout, container, false);
        return view;
    }
}
