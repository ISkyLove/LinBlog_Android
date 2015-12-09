package lin.com.lin.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lin.com.lin.R;
import lin.com.lin.base.HVBaseRecycleViewAdapter;
import lin.com.lin.base.HVBaseRecycleViewHolder;

/**
 *
 */
public class RecentAdapter extends HVBaseRecycleViewAdapter {
    Context mContext;
    private LayoutInflater mInflater;

    public RecentAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public RecentAdapter(Context context, View headview) {
        super(headview);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public RecentAdapter(Context context, View headview, View bottomview) {
        super(headview, bottomview);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HVBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return new HeaderViewHolder(mHeaderView);
        } else if (viewType == VIEW_TYPE_BOTTOM) {
            return null;
        } else {
            return new ItemViewHolder(mInflater.inflate(R.layout.recent_item_layut, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(HVBaseRecycleViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).tvTtile.setText((String) (mdata.get(position - 1)));
        }
    }

    static class HeaderViewHolder extends HVBaseRecycleViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    static class ItemViewHolder extends HVBaseRecycleViewHolder {
        TextView tvTtile;
        TextView tvDesc;
        ImageView ivImg;
        View rootView;

        public ItemViewHolder(View view) {
            super(view);
            rootView = view;
            tvTtile = (TextView) view.findViewById(R.id.recent_item_title);
            tvDesc = (TextView) view.findViewById(R.id.recent_item_desc);
            ivImg = (ImageView) view.findViewById(R.id.recent_item_img);
        }
    }
}
