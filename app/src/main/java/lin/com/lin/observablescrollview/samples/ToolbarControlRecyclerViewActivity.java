

package lin.com.lin.observablescrollview.samples;

import android.support.v7.widget.LinearLayoutManager;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableRecyclerView;

public class ToolbarControlRecyclerViewActivity extends ToolbarControlBaseActivity<ObservableRecyclerView> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_toolbarcontrolrecyclerview;
    }

    @Override
    protected ObservableRecyclerView createScrollable() {
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) findViewById(R.id.scrollable);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        setDummyData(recyclerView);
        return recyclerView;
    }
}
