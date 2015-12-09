package lin.com.lin.observablescrollview.samples;


import lin.com.observablescrollviewlibrary.observablescrollview.ScrollUtils;
import lin.com.observablescrollviewlibrary.observablescrollview.Scrollable;

/**
 * Almost same as FillGapBaseActivity,
 * but in this activity, when swiping up, the filled space shrinks
 * and the header bar moves to the top.
 */
public abstract class FillGap2BaseActivity<S extends Scrollable> extends FillGapBaseActivity<S> {
    protected float getHeaderTranslationY(int scrollY) {
        return ScrollUtils.getFloat(-scrollY + mFlexibleSpaceImageHeight - mHeaderBar.getHeight(), 0, Float.MAX_VALUE);
    }
}
