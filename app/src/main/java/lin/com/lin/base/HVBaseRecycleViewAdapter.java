package lin.com.lin.base;

import java.util.List;


import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;


public abstract class HVBaseRecycleViewAdapter extends
		Adapter<HVBaseRecycleViewHolder> {

	protected static final int VIEW_TYPE_TOP = 0;
	protected static final int VIEW_TYPE_ITEM = 1;
	protected static final int VIEW_TYPE_BOTTOM = 2;
	protected View mHeaderView;
	protected View mBottomView;

	public HVBaseRecycleViewAdapter() {

	}

	public HVBaseRecycleViewAdapter(View headview) {
		this.mHeaderView = headview;
	}

	public HVBaseRecycleViewAdapter(View headview, View bottomview) {
		this.mHeaderView = headview;
		this.mBottomView = bottomview;
	}

	/**
	 * item 短按
	 * 
	 * @author Lin
	 * 
	 */
	public interface OnItemClickListen {
		void OnItemClickListen(int position);
	}

	/**
	 * item 长按
	 * 
	 * @author Lin
	 * 
	 */
	public interface OnItemLongClickListen {
		void OnItemLongClick(int position);
	}

	protected OnItemLongClickListen mLongClickListen;

	protected OnItemClickListen mItemClickListen;

	public void setOnItemClickListen(OnItemClickListen onitemclicklisten) {
		this.mItemClickListen = onitemclicklisten;
	}

	public void setOnItemLongClickListen(
			OnItemLongClickListen onItemLongClickListen) {
		this.mLongClickListen = onItemLongClickListen;
	}

	protected List mdata;

	public void changeData(List data) {
		if (data != null) {
			mdata = data;
		}
		notifyDataSetChanged();
	}

	public void addData(List data) {
		if (mdata == null && data != null) {
			mdata = data;
			return;
		}
		if (data != null) {
			mdata.addAll(data);
		}
		notifyDataSetChanged();
	}

	public void clearData() {
		if (mdata != null) {
			mdata.clear();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		int count = 0;
		if (mdata != null) {
			count = mdata.size();
		}

		if (mHeaderView != null) {
			count++;
		}

		if (mBottomView != null) {
			count++;
		}

		return count;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (mHeaderView != null) {
			return (position == 0) ? VIEW_TYPE_TOP : VIEW_TYPE_ITEM;
		} else if (mBottomView != null && mdata != null) {
			return (position == mdata.size() + 1) ? VIEW_TYPE_BOTTOM
					: VIEW_TYPE_ITEM;
		} else {
			return VIEW_TYPE_ITEM;
		}
	}

	public class HeaderViewHolder extends HVBaseRecycleViewHolder {
		public HeaderViewHolder(View view) {
			super(view);
		}
	}

	public class BottomViewHolder extends HVBaseRecycleViewHolder {
		public BottomViewHolder(View view) {
			super(view);
		}
	}

	public List getData() {
		return mdata;
	}
}
