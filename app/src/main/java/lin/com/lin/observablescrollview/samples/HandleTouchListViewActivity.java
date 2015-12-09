

package lin.com.lin.observablescrollview.samples;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableListView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;
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
public class HandleTouchListViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {
    private static final String TAG = HandleTouchListViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handletouchlistview);

        ObservableListView listView = (ObservableListView) findViewById(R.id.scroll);
        listView.setScrollViewCallbacks(this);
        listView.setAdapter(new CustomAdapter(this, getDummyData()));
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

    public static class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter(Context context, List<String> objects) {
            super(context, R.layout.list_item_handletouch, android.R.id.text1, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(position + 1);
                }
            });
            return view;
        }

        private void click(int i) {
            String message = "Button " + i + " is clicked";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            Log.v(TAG, "click: " + message);
        }
    }
}
