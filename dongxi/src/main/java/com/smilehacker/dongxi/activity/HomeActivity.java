package com.smilehacker.dongxi.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.app.App;
import com.smilehacker.dongxi.fragment.HomeFragment;
import com.smilehacker.dongxi.fragment.MenuFragment;
import com.smilehacker.dongxi.model.event.CategoryEvent;

import de.greenrobot.event.EventBus;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

public class HomeActivity extends Activity {

    private App APP;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SlidingMenu mSlidingMenu;
    private MenuFragment mMenuFragment;

    private EventBus mEventBus;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        APP = (App) getApplicationContext();

        // init sliding menu
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setBehindOffset(APP.deviceInfo.screenWidth * 3 / 5);
        mSlidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        mSlidingMenu.setMenu(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getFragmentManager().beginTransaction();
            mMenuFragment = new MenuFragment();
            t.replace(R.id.menu_frame, mMenuFragment);
            t.commit();
        } else {
            mMenuFragment = (MenuFragment) this.getFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // init viewpager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i != 0) {
                    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                } else {
                    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(CategoryEvent event) {
        mViewPager.setCurrentItem(0);
        mSlidingMenu.toggle();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new HomeFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "HOME";
            }
            return null;
        }
    }

}
