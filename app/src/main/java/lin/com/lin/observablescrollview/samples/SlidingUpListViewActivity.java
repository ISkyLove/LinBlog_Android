

package lin.com.lin.observablescrollview.samples;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableListView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;


public class SlidingUpListViewActivity extends SlidingUpBaseActivity<ObservableListView> implements ObservableScrollViewCallbacks {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_slidinguplistview;
    }

    @Override
    protected ObservableListView createScrollable() {
        ObservableListView listView = (ObservableListView) findViewById(R.id.scroll);
        listView.setScrollViewCallbacks(this);
        setDummyData(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SlidingUpListViewActivity.this, "Item " + (position + 1) + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return listView;
    }
}
