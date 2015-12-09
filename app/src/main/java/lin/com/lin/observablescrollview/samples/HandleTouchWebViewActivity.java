

package lin.com.lin.observablescrollview.samples;

import android.os.Bundle;
import android.util.Log;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;
import lin.com.observablescrollviewlibrary.observablescrollview.ScrollState;


/**
 * This sample doesn't include any scrolling effects.
 * With this sample, you can check how the callbacks occur
 * when the children views handle touch events.
 * <p/>
 * At least, ScrollView has an issue: when we touch a child which has OnClickListener
 * and drag it to expect its parent ScrollView generates onDownMotionEvent() and
 * onScrollChanged([WHATEVER], true, true), but they won't be generated.
 * <p/>
 * https://github.com/ksoichiro/Android-ObservableScrollView/issues/18
 */
public class HandleTouchWebViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {
    private static final String TAG = HandleTouchWebViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handletouchwebview);

        ObservableWebView webView = (ObservableWebView) findViewById(R.id.scroll);
        webView.setScrollViewCallbacks(this);
        webView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        Log.v(TAG, "onScrollChanged: scrollY: " + scrollY + " firstScroll: " + firstScroll + " dragging: " + dragging);
    }

    @Override
    public void onDownMotionEvent() {
        Log.v(TAG, "onDownMotionEvent");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Log.v(TAG, "onUpOrCancelMotionEvent: scrollState: " + scrollState);
    }
}
