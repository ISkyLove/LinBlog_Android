package lin.com.lin.main.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import lin.com.lin.base.LBaseFragment;

public class MainFragmentAdapter extends HVFragmentPagerAdapter {
	private List<LBaseFragment> mFragments;

	public MainFragmentAdapter(FragmentManager fm, List<LBaseFragment> fragments) {
		super(fm);
		this.mFragments = fragments;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}

	

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

}
