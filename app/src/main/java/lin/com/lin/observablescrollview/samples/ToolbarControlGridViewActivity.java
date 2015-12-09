

package lin.com.lin.observablescrollview.samples;


import lin.com.lin.R;
import lin.com.observablescrollviewlibrary.observablescrollview.ObservableGridView;

public class ToolbarControlGridViewActivity extends ToolbarControlBaseActivity<ObservableGridView> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_toolbarcontrolgridview;
    }

    @Override
    protected ObservableGridView createScrollable() {
        ObservableGridView gridView = (ObservableGridView) findViewById(R.id.scrollable);
        setDummyData(gridView);
        return gridView;
    }
}
