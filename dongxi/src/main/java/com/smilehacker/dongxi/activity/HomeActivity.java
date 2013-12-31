package com.smilehacker.dongxi.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.app.App;
import com.smilehacker.dongxi.fragment.HomeFragment;
import com.smilehacker.dongxi.fragment.MenuFragment;
import com.smilehacker.dongxi.model.event.CategoryEvent;

import java.util.Locale;

import de.greenrobot.event.EventBus;

public class HomeActivity extends Activity {

    private App APP;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private EventBus mEventBus;

    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mLeftDraw;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        APP = (App) getApplicationContext();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mLeftDraw = (FrameLayout) findViewById(R.id.left_draw);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        initNavigationDrawer();
        initViewPager();

        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

    }

    private void initNavigationDrawer() {
        MenuFragment menuFragment = new MenuFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.left_draw, menuFragment);
        ft.commit();

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    private void initViewPager() {
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i != 0) {
                } else {
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onEvent(CategoryEvent event) {
        mViewPager.setCurrentItem(0);
        mDrawerLayout.closeDrawer(mLeftDraw);
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
