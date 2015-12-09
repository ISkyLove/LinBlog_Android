

package lin.com.lin.observablescrollview.samples;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableGridView;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableScrollViewCallbacks;


public class SlidingUpGridViewActivity extends SlidingUpBaseActivity<ObservableGridView> implements ObservableScrollViewCallbacks {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_slidingupgridview;
    }

    @Override
    protected ObservableGridView createScrollable() {
        ObservableGridView gridView = (ObservableGridView) findViewById(R.id.scroll);
        gridView.setScrollViewCallbacks(this);
        setDummyData(gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SlidingUpGridViewActivity.this, "Item " + (position + 1) + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return gridView;
    }
}
