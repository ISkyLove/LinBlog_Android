package lin.com.lin.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 */
public class IconTextView extends TextView {

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(getResources().getAssets(), "iconfont/iconfont.ttf");
        setTypeface(iconfont);
    }
}
