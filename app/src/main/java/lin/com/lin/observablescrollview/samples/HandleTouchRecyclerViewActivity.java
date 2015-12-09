

package lin.com.lin.observablescrollview.samples;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableRecyclerView;
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
public class HandleTouchRecyclerViewActivity extends ObserverBaseActivity implements ObservableScrollViewCallbacks {
    private static final String TAG = HandleTouchRecyclerViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handletouchrecyclerview);

        ObservableRecyclerView recyclerView = (ObservableRecyclerView) findViewById(R.id.scroll);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setAdapter(new CustomAdapter(this, getDummyData()));
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

    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<String> mItems;

        public CustomAdapter(Context context, ArrayList<String> items) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mItems = items;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mContext, mInflater.inflate(R.layout.list_item_handletouch, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.textView.setText(mItems.get(position));
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            Context context;

            public ViewHolder(Context context, View view) {
                super(view);
                this.context = context;
                this.textView = (TextView) view.findViewById(android.R.id.text1);
                view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click(getPosition() + 1);
                    }
                });
            }

            private void click(int i) {
                String message = "Button " + i + " is clicked";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                Log.v(TAG, "click: " + message);
            }
        }
    }
}
