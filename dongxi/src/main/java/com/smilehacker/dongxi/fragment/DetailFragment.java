package com.smilehacker.dongxi.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.Utils.CircleTransform;
import com.smilehacker.dongxi.activity.HomeActivity;
import com.smilehacker.dongxi.activity.MerchantActivity;
import com.smilehacker.dongxi.activity.PhotoActivity;
import com.smilehacker.dongxi.adapter.UserCreatedAdapter;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.dongxi.model.Picture;
import com.smilehacker.dongxi.network.SimpleVolleyTask;
import com.smilehacker.dongxi.network.task.UserCreatedTask;
import com.smilehacker.dongxi.view.ScrollCompactGridView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleist on 13-12-29.
 */
public class DetailFragment extends Fragment {

    private final static String TAG = DetailFragment.class.getName();

    private ViewPager mImagePager;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private ImageView mIvAvatar;
    private TextView mTvAuthor;
    private TextView mTvComment;
    private Button mBtnBuy;
    private ScrollCompactGridView mGvCreated;
    private CirclePageIndicator mImageIndicator;

    private Dongxi mDongxi;
    private ImagePagerAdapter mAdapter;
    private UserCreatedAdapter mCreatedAdapter;
    private UserCreatedTask mUserCreatedTask;

    private CircleTransform mCircleTransform;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mDongxi = args.getParcelable(Constants.KEY_DONGXI);
        mAdapter = new ImagePagerAdapter(mDongxi.pictures);
        mCreatedAdapter = new UserCreatedAdapter(getActivity(), new ArrayList<Dongxi>());
        mCircleTransform = new CircleTransform();

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_detail, container, false);

        mImagePager = (ViewPager) view.findViewById(R.id.viewpager_images);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvPrice = (TextView) view.findViewById(R.id.tv_price);
        mBtnBuy = (Button) view.findViewById(R.id.btn_buy);
        mImageIndicator = (CirclePageIndicator) view.findViewById(R.id.image_indicator);
        mIvAvatar = (ImageView) view.findViewById(R.id.iv_author_avatar);
        mTvAuthor = (TextView) view.findViewById(R.id.tv_author_name);
        mTvComment = (TextView) view.findViewById(R.id.tv_dongxi_comment);
        mGvCreated = (ScrollCompactGridView) view.findViewById(R.id.gv_created);

        initView();

        return view;
    }

    private void initView() {

        mTvTitle.setText(mDongxi.title);
        mTvPrice.setText(mDongxi.price);
        mTvAuthor.setText(mDongxi.author.name);

        mImagePager.setAdapter(mAdapter);
        mImageIndicator.setViewPager(mImagePager);
        mGvCreated.setAdapter(mCreatedAdapter);

        Picasso.with(getActivity()).load(mDongxi.author.largeAvatar).transform(mCircleTransform).into(mIvAvatar);

        if (mDongxi.pictures.size() <= 1) {
            mImageIndicator.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mDongxi.text)) {
            mTvComment.setVisibility(View.GONE);
        } else {
            mTvComment.setText(mDongxi.text);
        }

        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MerchantActivity.class);
                intent.putExtra(Constants.KEY_DONGXI, mDongxi);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUserCreatedTask != null) {
            mUserCreatedTask.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void load() {
        if (mUserCreatedTask != null) {
            mUserCreatedTask.cancel();
        }
        mUserCreatedTask = new UserCreatedTask(getActivity(), mDongxi.author.id, new SimpleVolleyTask.VolleyTaskCallBack<List<Dongxi>>() {
            @Override
            public void onSuccess(List<Dongxi> result) {
                filterList(result);
                mCreatedAdapter.setDongxiList(result);
            }

            private void filterList(List<Dongxi> list) {
                for (Dongxi dongxi: list) {
                    if (mDongxi.id.equals(dongxi.id)) {
                        list.remove(dongxi);
                        break;
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {

            }
        });

        mUserCreatedTask.execute();
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private List<Picture> pictures;

        public ImagePagerAdapter(List<Picture> pictures) {
            this.pictures = pictures;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String src = pictures.get(position).src;
            ImageView imageView = new ImageView(getActivity());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load(src).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PhotoActivity.class);
                    intent.putExtra(Constants.KEY_DONGXI, mDongxi);
                    intent.putExtra(Constants.KEY_CURRENT_PHOTO, mImagePager.getCurrentItem());
                    startActivity(intent);
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getCount() {
            return pictures.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

    }
}
