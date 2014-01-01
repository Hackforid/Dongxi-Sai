package com.smilehacker.dongxi.activity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.LinearLayout;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.dongxi.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class PhotoActivity extends Activity {

    private final static String TAG = PhotoActivity.class.getName();

    private ViewPager mViewPager;
    private PhotoViewPagerAdapter mAdapter;

    private Dongxi mDongxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mDongxi = getIntent().getParcelableExtra(Constants.KEY_DONGXI);
        int currentPos = getIntent().getIntExtra(Constants.KEY_CURRENT_PHOTO, 0);
        mAdapter = new PhotoViewPagerAdapter(mDongxi.pictures);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(currentPos);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class PhotoViewPagerAdapter extends PagerAdapter {

        private List<Picture> pictureList;

        public PhotoViewPagerAdapter(List<Picture> pictureList) {
            this.pictureList = pictureList;
        }

        @Override
        public int getCount() {
            return pictureList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(PhotoActivity.this);
            Picasso.with(PhotoActivity.this).load(pictureList.get(position).src).into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }
    }
}
