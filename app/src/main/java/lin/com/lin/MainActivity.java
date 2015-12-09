package lin.com.lin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lin.com.lin.base.LBaseActivity;
import lin.com.lin.base.LBaseFragment;
import lin.com.lin.base.LBaseViewPager;
import lin.com.lin.main.AllFragment;
import lin.com.lin.main.RecentFragment;
import lin.com.lin.main.RecommendFragment;
import lin.com.lin.main.adapter.HVFragmentSaveStatePagerAdapter;
import lin.com.lin.main.adapter.MainFragmentAdapter;
import lin.com.lin.observablescrollview.samples.ObserverMainActivity;


public class MainActivity extends LBaseActivity implements OnClickListener {
    Context mContext;
    LinearLayout llTab, llAll, llRecent, llRecommend;
    Toolbar tbHead;
    FrameLayout flContain;

    final int TAB_ALL = 0;
    final int TAB_RECENT = 1;
    final int TAB_RECOMMEND = 2;
    int cur_tab = TAB_RECENT;

    AllFragment mAllFragment;
    RecentFragment mRecentFragment;
    RecommendFragment mRecommendFragment;

    FragmentManager fragmentManager;

    LBaseViewPager vpContain;
    List<LBaseFragment> myFragments;
    HVFragmentSaveStatePagerAdapter mFragmentSaveStatePagerAdapter;

    TextView tvAllIcon, tvAllText, tvRecentIcont, tvRecentText, tvRecommendIcon, tvRecommendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initEvent();
        changeTab(TAB_RECENT);
        initViewPager();
    }

    private void initView() {


        tbHead = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tbHead);
      //  tbHead.setVisibility(View.GONE);

        flContain = (FrameLayout) findViewById(R.id.main_contain);
        vpContain = (LBaseViewPager) findViewById(R.id.main_contain_vp);

        llTab = (LinearLayout) findViewById(R.id.main_tab);
        llAll = (LinearLayout) findViewById(R.id.main_tab_all);
        llRecent = (LinearLayout) findViewById(R.id.main_tab_recent);
        llRecommend = (LinearLayout) findViewById(R.id.main_tab_recommend);

        tvAllIcon = (TextView) findViewById(R.id.main_tab_all_icon);
        tvAllText = (TextView) findViewById(R.id.main_tab_all_text);
        tvRecentIcont = (TextView) findViewById(R.id.main_tab_recent_icon);
        tvRecentText = (TextView) findViewById(R.id.main_tab_recent_text);
        tvRecommendIcon = (TextView) findViewById(R.id.main_tab_recommend_icon);
        tvRecommendText = (TextView) findViewById(R.id.main_tab_recommend_text);


        fragmentManager=getSupportFragmentManager();

        mAllFragment = new AllFragment();
        mRecentFragment = new RecentFragment();
        mRecommendFragment = new RecommendFragment();

//        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.main_contain, mAllFragment, "ALL");
//        fragmentTransaction.add(R.id.main_contain, mRecentFragment, "RECENT");
//        fragmentTransaction.add(R.id.main_contain,mRecommendFragment,"RECOMMEND");
//        fragmentTransaction.commit();
 //       changeTab(cur_tab);





    }

    private void initViewPager() {
        myFragments = new ArrayList<LBaseFragment>();
        myFragments.add(mAllFragment);
        myFragments.add(mRecentFragment);
        myFragments.add(mRecommendFragment);

        mFragmentSaveStatePagerAdapter = new HVFragmentSaveStatePagerAdapter(
                fragmentManager, vpContain, myFragments);

        vpContain.setAdapter(mFragmentSaveStatePagerAdapter);
        vpContain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeTab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        changeTab(TAB_RECENT);
        vpContain.setCurrentItem(cur_tab, false);
    }

    private void changeTab(int tab) {
        tvAllIcon.setTextColor(getResources().getColor(R.color.tab_default_color));
        tvAllText.setTextColor(getResources().getColor(R.color.tab_default_color));
        tvRecentIcont.setTextColor(getResources().getColor(R.color.tab_default_color));
        tvRecentText.setTextColor(getResources().getColor(R.color.tab_default_color));
        tvRecommendIcon.setTextColor(getResources().getColor(R.color.tab_default_color));
        tvRecommendText.setTextColor(getResources().getColor(R.color.tab_default_color));

        tvAllIcon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        tvAllText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        tvRecentIcont.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        tvRecentText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        tvRecommendIcon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        tvRecommendText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);

//        fragmentManager=getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
//        fragmentTransaction.hide(mAllFragment);
//        fragmentTransaction.hide(mRecentFragment);
//        fragmentTransaction.hide(mRecommendFragment);
        switch (tab) {
            case TAB_ALL:
//               fragmentTransaction.show(mAllFragment);
                cur_tab = TAB_ALL;
                tvAllIcon.setTextColor(getResources().getColor(R.color.tab_focus_color));
                tvAllText.setTextColor(getResources().getColor(R.color.tab_focus_color));
                tvAllIcon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                tvAllText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                break;
            case TAB_RECENT:
//                fragmentTransaction.show(mRecentFragment);
                cur_tab = TAB_RECENT;
                tvRecentIcont.setTextColor(getResources().getColor(R.color.tab_focus_color));
                tvRecentText.setTextColor(getResources().getColor(R.color.tab_focus_color));
                tvRecentIcont.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                tvRecentText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                break;
            case TAB_RECOMMEND:
//                fragmentTransaction.show(mRecommendFragment);
                cur_tab = TAB_RECOMMEND;
                tvRecommendIcon.setTextColor(getResources().getColor(R.color.tab_focus_color));
                tvRecommendText.setTextColor(getResources().getColor(R.color.tab_focus_color));
                tvRecommendIcon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                tvRecommendText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                break;
            default:
                break;
        }
 //       fragmentTransaction.commit();
    }

    private void initEvent() {
        llAll.setOnClickListener(this);
        llRecent.setOnClickListener(this);
        llRecommend.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_all:
                if (cur_tab != TAB_ALL) {
                    changeTab(TAB_ALL);
                    vpContain.setCurrentItem(cur_tab, false);
                }
                break;
            case R.id.main_tab_recent:
                if (cur_tab != TAB_RECENT) {
                    changeTab(TAB_RECENT);
                    vpContain.setCurrentItem(cur_tab, false);
                }
                break;
            case R.id.main_tab_recommend:
                if (cur_tab != TAB_RECOMMEND) {
                    changeTab(TAB_RECOMMEND);
                    vpContain.setCurrentItem(cur_tab, false);
                }
                break;
            default:
                break;
        }
    }
}
