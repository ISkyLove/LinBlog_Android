package lin.com.lin.observablescrollview.samples.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;

import lin.com.lin.R;

/**
 *
 */
public class ObservableUtils {
    public static int getActionBarSize(Context context) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public static View getDummyDataWithHeader(Context context, int headerHeight) {
        View headerView = new View(context);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
        headerView.setMinimumHeight(headerHeight);
        // This is required to disable header's list selector effect
        headerView.setClickable(true);
        return headerView;
    }

    public static int getScreenHeight(Activity activtity) {
        return activtity.getWindow().getDecorView().findViewById(android.R.id.content).getHeight();
    }
}
