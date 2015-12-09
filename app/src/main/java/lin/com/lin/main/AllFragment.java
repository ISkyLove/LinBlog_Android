package lin.com.lin.main;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;
import com.okhttp.OkHttpClientManager;
import com.okhttp.callback.ResultCallback;
import com.okhttp.okhttpUtils;
import com.okhttp.request.OkHttpDisplayImgRequest;
import com.okhttp.request.OkHttpRequest;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lin.com.lin.R;
import lin.com.lin.base.LBaseFragment;
import lin.com.lin.http.LinHttpClinet;
import lin.com.lin.http.Urls;
import lin.com.lin.observablescrollview.samples.fragment.ViewPagerTab2GridViewFragment;
import lin.com.lin.observablescrollview.samples.fragment.ViewPagerTab2ListViewFragment;
import lin.com.lin.observablescrollview.samples.fragment.ViewPagerTab2RecyclerViewFragment;
import lin.com.lin.observablescrollview.samples.fragment.ViewPagerTab2ScrollViewFragment;
import lin.com.lin.observablescrollview.samples.fragment.ViewPagerTab2WebViewFragment;
import lin.com.lin.observablescrollview.samples.utils.ObservableUtils;
import lin.com.observablescrollviewlibrary.observablescrollview.CacheFragmentStatePagerAdapter;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;
import lin.com.observablescrollviewlibrary.observablescrollview.Scrollable;
import lin.com.observablescrollviewlibrary.observablescrollview.SlidingTabLayout;
import lin.com.observablescrollviewlibrary.observablescrollview.TouchInterceptionFrameLayout;

/**
 * 全部
 */
public class AllFragment extends LBaseFragment {
    View view;
    Context mContext;
    boolean serviceEnabled=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = inflater.inflate(R.layout.main_all_layout, container, false);
        okhttpUtils.get(Urls.TEST_URL, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("okhttp", "message" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("okhttp", "message" + response.toString());
                Log.i("okhttp", "message" + response.body().string());
            }
        });
        ImageView ivImg = (ImageView) view.findViewById(R.id.all_image);
        view.findViewById(R.id.text_access).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent mAccessibleIntent =
                        new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(mAccessibleIntent);

            }
        });
        new OkHttpRequest.Builder()
                .url(Urls.IMG_TEST_URL)
                .imageView(ivImg)
                .displayImage(new ResultCallback<Object>() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(Object response) {
                        Log.i("okhttp","response:"+response.toString());
                    }

                    @Override
                    public void inProgress(float progress) {
                        super.inProgress(progress);
                        Log.i("okhttp","progress:"+progress);
                    }
                });
//        final TextView tvText = (TextView) view.findViewById(R.id.all_text);
//        LinHttpClinet.get(Urls.TEST_URL, null, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                tvText.setText(responseString);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                tvText.setText(responseString);
//            }
//        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAccess();
    }

    private void updateAccess(){
        serviceEnabled=false;
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals("lin.com.lin.services.AccessListenService")) {
                serviceEnabled = true;
            }
        }
        ((TextView)view.findViewById(R.id.text_access)).setText(serviceEnabled ? "辅助服务已开启" : "辅助服务已关闭");
    }
}
