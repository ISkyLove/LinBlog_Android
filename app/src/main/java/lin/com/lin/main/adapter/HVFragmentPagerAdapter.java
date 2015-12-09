package lin.com.lin.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * viewpager的fragment適配器，fragment管理类FragmentManager，自带缓存策略的适配器，
 * 缓存策略为显示页面左边第一个以及右边第一个
 * 
 * @author Lin
 * 
 */
public abstract class HVFragmentPagerAdapter extends PagerAdapter {

	private static final String TAG = "HVBaseFragmentPagerAdapter";

	private final FragmentManager mFragmentManager;
	private FragmentTransaction mCurTransaction = null;
	private Fragment mCurrentPrimaryItem = null;
	private List<Fragment> mFragments;

	public HVFragmentPagerAdapter(FragmentManager fm) {
		mFragmentManager = fm;

	}

	public HVFragmentPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragments) {

		mFragmentManager = fm;
		this.mFragments = fragments;

	}

	public abstract Fragment getItem(int position);

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void startUpdate(View container) {
	}

	@Override
	public Object instantiateItem(View container, int position) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		final long itemId = getItemId(position);
		String name = makeFragmentName(container.getId(), itemId);
		Fragment fragment = mFragmentManager.findFragmentByTag(name);
		if (fragment != null) {
//			mCurTransaction.detach(fragment);
			mCurTransaction.attach(fragment);
		} else {
			fragment = getItem(position);
			mCurTransaction.add(container.getId(), fragment,
					makeFragmentName(container.getId(), itemId));
		}
		if (fragment != mCurrentPrimaryItem) {
			fragment.setMenuVisibility(false);
			fragment.setUserVisibleHint(false);
		}
		return fragment;

	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		Fragment fragment = (Fragment) object;

		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}

		mCurTransaction.detach((Fragment) object);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		Fragment fragment = (Fragment) object;
		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(false);
				mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;
		}
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void finishUpdate(View container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment) object).getView() == view;
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private static String makeFragmentName(int viewId, long id) {
		return "android:switcher:" + viewId + ":" + id;
	}
	
	

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		// if (mCurTransaction == null) {
		// mCurTransaction = mFragmentManager.beginTransaction();
		// }
		// final long itemId = getItemId(position);
		// String name = makeFragmentName(container.getId(), itemId);
		// Fragment fragment = mFragmentManager.findFragmentByTag(name);
		// if (fragment != null) {
		// mCurTransaction.detach(fragment);
		// mCurTransaction.attach(fragment);
		// }
		super.notifyDataSetChanged();
	}
}
